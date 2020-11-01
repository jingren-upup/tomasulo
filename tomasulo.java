import java.io.*;
import java.awt.image.*;
import java.applet.Applet;
import java.net.*;
import java.util.*;
public class tomasulo{
	public void go(data d){
		switch (d.ins_op[0]){
			case 1://LD 指令
				for(int i = 0;i < 3;i ++){
					if(d.ld_busy[i] == 0){//no busy
						if(d.rrs_f[d.ins_op[1]] != 0){//未知结果，即有写写冲突
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
			case 2://ST 指令
				for(int i = 0;i < 3;i ++){
					if(d.st_busy[i] == 0){
						//if(d.rrs_f[d.ins_op[1]] != 0){//未知结果，即有写写冲突
						//	break;
						//}
						d.st_busy[i] = 1;
						d.st_address[i] = d.ins_ls;
						//d.rrs_f[d.ins_op[1]] == 2 * 10 + i;
						d.st_time[i] = 3;
						d.icr_issue[d.pc] = d.clock;
						d.st_q[i] = d.ins_op[1];
						d.st_pc[i] = d.pc;
						d.pc ++;
						if(d.rrs_f[d.ins_op[1]] != 0){//未知结果
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
			case 3://add
			case 4://sub
			case 5://mul
			case 6://div 指令
				if(d.rrs_f[d.ins_op[1]] != 0){//未知结果，即有写写冲突
				/*
							if(d.rrs_f[d.ins_op[1]] / 10 == 1){
								if(d.ld_time[d.rrs_f[d.ins_op[1]] % 10] > 1){
									break;
								}
							}
							if(d.rrs_f[d.ins_op[1]] / 10 > 2){
								if(d.rs_time[d.rrs_f[d.ins_op[1]] % 10] > 1){
									break;
								}
							}
							*/
					break;
				}
				int m,n,r_time,r_op;
				if(d.ins_op[0] == 3){
					m = 0;
					n = 3;
					r_time = 3;
					r_op = 3;
				}else if(d.ins_op[0] == 4){
					m = 0;
					n = 3;
					r_time = 3;
					r_op = 4;
				}else if(d.ins_op[0] == 5){
					m = 3;
					n = 5;
					r_time = 11;
					r_op = 5;
				}else{
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
						
						if(d.rrs_f[d.ins_op[2]] != 0){//未知结果，即前面有指令还未写回
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
		//进行内部运算
		//将浮点寄存器的状态存储
		/*
		for(int i = 0;i < 3;i ++){
			System.out.println(d.ld_busy[i]);
		}
		*/
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
		//找到在栈中的所有指令的执行顺序
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
		//按pc进行排序
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
		//按pc进行判断
		for(int i = 0;i < 11;i ++){
			if(run_order[i][0] < 5){//rs
				if(d.rs_busy[run_order[i][0]] == 1){
					if(d.rs_time[run_order[i][0]] != 0){//指令在执行的过程中
						d.rs_time[run_order[i][0]] --;
						if(d.rs_time[run_order[i][0]] == 0){//指令执行结束
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
							//d.rs_busy[run_order[i][0]] = 0;
						}
					}else{//指令在等待中
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
			}else if(run_order[i][0] < 8){//ld
				//System.out.println("调用ld");
				int load = run_order[i][0] - 5;
				int load_pc = run_order[i][1];
				if(d.ld_busy[load] == 1){
					if(d.ld_time[load] != 0){
						d.ld_time[load] --;
						if(d.ld_time[load] == 0){
							d.rrs_f[d.ld_q[load]] = 0;
							d.rrs_fd[d.ld_q[load]] = d.mem[d.ld_address[load]];//调用内存函数，待定
							d.icr_comr[d.ld_pc[load]] = 1;
							d.icr_complete[d.ld_pc[load]] = d.clock;
							//d.ld_busy[load] = 0;
						}
					}else{
						d.ld_busy[load] = 0;
					}
				}
			}else{//st
				int std = run_order[i][0] - 8;
				int std_pc = run_order[i][1];
				if(d.st_busy[std] == 1){
					if(d.st_time[std] != 0){
						d.st_time[std] --;
						if(d.st_time[std] == 0){
							d.rrs_f[d.st_q[std]] = 0;
							d.mem[d.st_address[std]] = d.st_da[std];
							//d.rrs_fd[d.st_q[load]] = 1;//调用内存函数，待定
							d.icr_comr[d.st_pc[std]] = 1;
							d.icr_complete[d.st_pc[std]] = d.clock;
							//d.st_busy[std] = 0;
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
		