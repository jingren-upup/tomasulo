public class tomasulo{
	public void go(data d){
		switch (d.ins_op[0]){
			case 1://load
				for(int i = 0;i < 3;i ++){
					if(d.ld_busy[i] == 0){//no busy
						if(d.rrs_f[d.ins_op[1]] != 0){//WAW HAZARD
							break;
						}
						d.ld_busy[i] = 1;
						d.ld_address[i] = d.ins_ls;
						d.rrs_f[d.ins_op[1]] = 1 * 10 + i;
						d.ld_time[i] = 3;
						d.icr_issue[d.pc] = d.clock;
						d.ld_q[i] = d.ins_op[1];
						d.ld_pc[i] = d.pc;
						d.pc ++;
						break;
					}
				}
				break;
			case 2://STORE
				for(int i = 0;i < 3;i ++){
					if(d.st_busy[i] == 0){

						d.st_busy[i] = 1;
						d.st_address[i] = d.ins_ls;
						d.st_time[i] = 3;
						d.icr_issue[d.pc] = d.clock;
						d.st_q[i] = d.ins_op[1];
						d.st_pc[i] = d.pc;
						d.pc ++;
						if(d.rrs_f[d.ins_op[1]] != 0){
							d.st_time[i] = 0;
							d.st_alloc[i] = 1;
						}else{
							d.st_time[i] = 3;
							d.st_da[i] = d.rrs_fd[d.ins_op[1]];
							d.st_alloc[i] = 0;
						}
						break;
					}
				}
				break;
			case 3:
			case 4:
			case 5:
			case 6:
				if(d.rrs_f[d.ins_op[1]] != 0){//WAW hazard
					break;
				}
				int m,n,r_time,r_op;
				if(d.ins_op[0] == 3){//add
					m = 0;
					n = 3;
					r_time = 3;
					r_op = 3;
				}else if(d.ins_op[0] == 4){//sub
					m = 0;
					n = 3;
					r_time = 3;
					r_op = 4;
				}else if(d.ins_op[0] == 5){//mul
					m = 3;
					n = 5;
					r_time = 11;
					r_op = 5;
				}else{//div
					m = 3;
					n = 5;
					r_time = 41;
					r_op = 6;
				}
				for(int i = m;i < n;i ++){
					if(d.rs_busy[i] == 0){
						d.rs_busy[i] = 1;
						d.rs_op[i] = r_op;
						d.rrs_f[d.ins_op[1]] = r_op * 10 + i;
						
						if(d.rrs_f[d.ins_op[2]] != 0){
							d.rs_vjt[i] = 1;
							d.rs_qj[i] = d.ins_op[2];
						}else{
							d.rs_vjt[i] = 0;
							d.rs_vj[i] = d.rrs_fd[d.ins_op[2]];
						}
						
						if(d.rrs_f[d.ins_op[3]] != 0){
							d.rs_vkt[i] = 1;
							d.rs_qk[i] = d.ins_op[3];
						}else{
							d.rs_vkt[i] = 0;
							d.rs_vk[i] = d.rrs_fd[d.ins_op[3]];
						}
						
						if(d.rs_vjt[i] == 0 && d.rs_vkt[i] == 0){
							d.rs_time[i] = r_time;
						}else{
							d.rs_time[i] = 0;
						}
						d.rs_pc[i] = d.pc;
						d.icr_issue[d.pc] = d.clock;
						d.pc ++;
						break;
					}
				}
				break;
			default:
				break;
		}

		double[] fud = new double[30];
		int[] fu = new int[30];
		for(int i = 0;i < 30;i ++){
			fu[i] = d.rrs_f[i];
			fud[i] = d.rrs_fd[i];
		}
		int[] icr_com = new int[1000];
		for(int i = 0;i < d.pc;i ++){
			icr_com[i] = d.icr_comr[i];
			d.icr_comr[i] = 0;
		}
		int[][] run_order = new int[11][2];
		for(int i = 0;i < 5;i ++){
			run_order[i][0] = i;
			if(d.rs_busy[i] == 1){	
				run_order[i][1] = d.rs_pc[i];
			}
			else{
				run_order[i][1] = 0;
			}
		}
		for(int i = 0;i < 3;i ++){
			run_order[5 + i][0] = 5 + i;
			run_order[8 + i][0] = 8 + i;
			if(d.ld_busy[i] == 1){
				run_order[5 + i][1] = d.ld_pc[i];
			}else{
				run_order[5 + i][1] = 0;
			}
			if(d.st_busy[i] == 1){
				run_order[8 + i][1] = d.st_pc[i];
			}else{
				run_order[8 + i][1] = 0;
			}
		}
		//reorder
		for(int i = 0;i < 11;i ++){
			for(int j = 0;j < 10;j ++){
				if(run_order[j][1] > run_order[j + 1][1]){
					int temp;
					temp = run_order[j][1];
					run_order[j][1] = run_order[j + 1][1];
					run_order[j + 1][1] = temp;
					temp = run_order[j][0];
					run_order[j][0] = run_order[j + 1][0];
					run_order[j + 1][0] = temp;
				}
			}
		}
		//judge by pc
		for(int i = 0;i < 11;i ++){
			if(run_order[i][0] < 5){//rs
				if(d.rs_busy[run_order[i][0]] == 1){
					if(d.rs_time[run_order[i][0]] != 0){
						d.rs_time[run_order[i][0]] --;
						if(d.rs_time[run_order[i][0]] == 0){//finish exe
							d.icr_comr[run_order[i][1]]	= 1;
							d.icr_complete[run_order[i][1]] = d.clock;
							for(int k = 0;k < 30;k ++){
								if(fu[k] / 10 > 2){
									if(fu[k] % 10 == run_order[i][0]){
										d.rrs_f[k] = 0;
										if(fu[k] / 10 == 3){
											d.rrs_fd[k] = d.rs_vj[run_order[i][0]] + d.rs_vk[run_order[i][0]];
										}else if(fu[k] / 10 == 4){
											d.rrs_fd[k] = d.rs_vj[run_order[i][0]] - d.rs_vk[run_order[i][0]];
										}else if(fu[k] / 10 == 5){
											d.rrs_fd[k] = d.rs_vj[run_order[i][0]] * d.rs_vk[run_order[i][0]];
										}else if(fu[k] / 10 == 6){
											d.rrs_fd[k] = d.rs_vj[run_order[i][0]] / d.rs_vk[run_order[i][0]];
										}
									}
								}
							}

						}
					}else{//wait for instrument
						if(d.rs_vjt[run_order[i][0]] == 1 || d.rs_vkt[run_order[i][0]] == 1){
							if(fu[d.rs_qj[run_order[i][0]]] == 0 && d.rs_vjt[run_order[i][0]] == 1){
								d.rs_vjt[run_order[i][0]] = 0;
								d.rs_vj[run_order[i][0]] = fud[d.rs_qj[run_order[i][0]]];
							}
							if(fu[d.rs_qk[run_order[i][0]]] == 0 && d.rs_vkt[run_order[i][0]] == 1){
								d.rs_vkt[run_order[i][0]] = 0;
								d.rs_vk[run_order[i][0]] = fud[d.rs_qk[run_order[i][0]]];
							}
							if(d.rs_vjt[run_order[i][0]] == 0 && d.rs_vkt[run_order[i][0]] == 0){
								if(d.rs_op[run_order[i][0]] == 3){
									d.rs_time[run_order[i][0]] = 2;
								}else if(d.rs_op[run_order[i][0]] == 4){
									d.rs_time[run_order[i][0]] = 2;
								}else if(d.rs_op[run_order[i][0]] == 5){
									d.rs_time[run_order[i][0]] = 10;
								}else if(d.rs_op[run_order[i][0]] == 6){
									d.rs_time[run_order[i][0]] = 40;
								}
							}
						}else{
							d.rs_busy[run_order[i][0]] = 0;
						}
					}
				}
			}else if(run_order[i][0] < 8){//load

				int load = run_order[i][0] - 5;

				if(d.ld_busy[load] == 1){
					if(d.ld_time[load] != 0){
						d.ld_time[load] --;
						if(d.ld_time[load] == 0){
							d.rrs_f[d.ld_q[load]] = 0;
							d.rrs_fd[d.ld_q[load]] = d.mem[d.ld_address[load]];
							d.icr_comr[d.ld_pc[load]] = 1;
							d.icr_complete[d.ld_pc[load]] = d.clock;
							//d.ld_busy[load] = 0;
						}
					}else{
						d.ld_busy[load] = 0;
					}
				}
			}else{//store
				int std = run_order[i][0] - 8;
//				int std_pc = run_order[i][1];
				if(d.st_busy[std] == 1){
					if(d.st_time[std] != 0){
						d.st_time[std] --;
						if(d.st_time[std] == 0){
							d.rrs_f[d.st_q[std]] = 0;
							d.mem[d.st_address[std]] = d.st_da[std];
							d.icr_complete[d.st_pc[std]] = d.clock;
						}
					}else{
						if(d.st_alloc[std] == 1){
							if(fu[d.st_q[std]] == 0){
								d.st_time[std] = 2;
								d.st_da[std] = fud[std];
								d.st_alloc[std] = 0;
							}
						}else{
							d.st_busy[std] = 0;
						}
					}
				}
			}
		}
		for(int i = 0;i < 30;i ++){
			if(fu[i] != 0 && d.rrs_f[i] == 0){
				d.rrs_ff[i] = fu[i];
			}else{
				d.rrs_ff[i] = d.rrs_f[i];
			}
		}
		for(int i = 1;i < d.pc;i ++){
			if(icr_com[i] == 1){
				d.icr_result[i] = d.clock;
			}
		}

	}						
}
		