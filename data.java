import java.io.*;
import java.awt.image.*;
import java.applet.Applet;
import java.net.*;
import java.util.*;
public class data{
	public static int pc;
	public static int clock;
	
	public static int[] ins_op;
	public static int ins_ls;
	
	public static int[] icr_issue;
	public static int[] icr_complete;
	public static int[] icr_result;
	public static int[] icr_comr;
	
	public static int[] ld_time;
	public static int[] ld_address;
	public static int[] ld_busy;
	public static int[] ld_pc;
	public static int[] ld_q;
	
	public static int[] st_time;
	public static int[] st_address;
	public static int[] st_busy;
	public static int[] st_q;
	public static int[] st_alloc;
	public static int[] st_pc;
	public static double[] st_da;
	
	public static int[] rs_time;
	public static int[] rs_busy;
	public static int[] rs_op;
	public static double[] rs_vj;
	public static double[] rs_vk;
	public static int[] rs_vjt;
	public static int[] rs_vkt;
	public static int[] rs_qj;
	public static int[] rs_qk;
	public static int[] rs_pc;

	public static int[] rrs_f;
	public static double[] rrs_fd;
	public static int[] rrs_ff;
	
	public static double[] mem;
}