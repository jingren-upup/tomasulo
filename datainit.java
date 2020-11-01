import java.io.*;
import java.awt.image.*;
import java.applet.Applet;
import java.net.*;
import java.util.*;

public class datainit{
	data tomdata;
	public void init(){
		tomdata.pc = 0;
		tomdata.clock = 0;
		
		tomdata.ins_op = new int[4];
		
		tomdata.icr_issue = new int[1000];
		tomdata.icr_complete = new int[1000];
		tomdata.icr_result = new int[1000];
		tomdata.icr_comr = new int[1000];
		for(int i = 0;i < 1000;i ++){
			tomdata.icr_comr[i] = 0;
		}
		
		tomdata.ld_time = new int[3];
		tomdata.ld_address = new int[3];
		tomdata.ld_busy = new int[3];
		tomdata.ld_pc = new int[3];
		tomdata.ld_q = new int[3];
		
		tomdata.st_time = new int[3];
		tomdata.st_address = new int[3];
		tomdata.st_busy = new int[3];
		tomdata.st_q = new int[3];
		tomdata.st_pc = new int[3];
		tomdata.st_da = new double[3];
		tomdata.st_alloc = new int[3];
		
		tomdata.rs_time = new int[5];
		tomdata.rs_busy = new int[5];
		tomdata.rs_op = new int[5];
		tomdata.rs_vj = new double[5];
		tomdata.rs_vk = new double[5];
		tomdata.rs_vjt = new int[5];
		tomdata.rs_vkt = new int[5];
		tomdata.rs_qj = new int[5];
		tomdata.rs_qk = new int[5];
		tomdata.rs_pc = new int[5];
		
		tomdata.rrs_f = new int[30];
		tomdata.rrs_fd = new double[30];
		tomdata.rrs_ff = new int[30];
		
		for(int i = 0;i < 30;i ++){
			tomdata.rrs_f[i] = 0;
			tomdata.rrs_ff[i] = 0;
		}
		tomdata.mem = new double[4096];
		for(int i = 0;i < 4096;i ++){
			tomdata.mem[i] = 0;
		}
	}
}