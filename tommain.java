import java.io.*;
import java.awt.*;
import java.applet.Applet;
import java.net.*;
import java.util.*;
import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class tommain{
	public static final long serialVersionUID = -4892684184268025880L;
	public static final Timer time = new Timer("tommain");
	public static JFrame f;
	public static JButton openfile,run_step,run_continue;
	public static TextArea textarea;
	public static String filename;
	public static datainit ud;
	public static tommain tomm;
	public static int runing;
	public static int pc_read;
	public static tomasulo tom = new tomasulo();

	public static String[] information;
	public static Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	public static JTable table;
	public static JTable table_t;
	public static JTable table_ld;
	public static JTable table_st;
	public static JTable table_rs;
	public static  JTable table_f;
	public static  JTable table_icr;
	public static  JTable table_mem;
	public static JTable table_clock;
	public static boolean step_or;

	public  static Vector<Vector<Object>> rowData_ld = new Vector<Vector<Object>>();
	public  static Vector<Vector<Object>> rowData_st = new Vector<Vector<Object>>();
	public  static Vector<Vector<Object>> rowData_rs = new Vector<Vector<Object>>();
	public  static Vector<Vector<Object>> rowData_f = new Vector<Vector<Object>>();
	public  static Vector<Vector<Object>> rowData_icr = new Vector<Vector<Object>>();
	public  static  Vector<Vector<Object>> rowData_mem = new Vector<Vector<Object>>();
	public static Vector<Vector<Object>> rowData_clock = new Vector<Vector<Object>>();
	public static tommain tomma;

	public static void main(String[] args){
		ud = new datainit();
		//datainit testd = new datainit();
		ud.init();
		information = new String[1000];
		runing = 0;
		tomma = new tommain();
		//tomm.run();
		int order = 1;
		tomma.set();
		try{//读取文件
			String ch1,ch2;
			InputStreamReader r1 = new InputStreamReader(System.in);
			BufferedReader r2 = new BufferedReader(r1);
			System.out.println("输入要读取的文件名: ");
			filename = r2.readLine();
			File file_or = new File(filename);
			if(!file_or.exists()){
				System.out.println("文件不存在");
				return;
			}
			FileInputStream is = new FileInputStream(filename);
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(ir);
			while(true){
				information[order] = in.readLine();
				if(information[order].equals("END")){
					System.out.println(information[order] + " " + order);
					break;
				}
				order ++;
			}
		}catch(IOException e){
			System.out.println("Extraordinarily");
		}

		Vector<String> columnNames = new Vector<String>();
		columnNames.add("OP");
		columnNames.add("F1");
		columnNames.add("F2");
		columnNames.add("F3");
		//JTable table;
		table_t = new JTable(new DefaultTableModel(rowData, columnNames));
		JScrollPane jsp = new JScrollPane(table_t);
		f.add(jsp);
		jsp.setBounds(20,50,200,150);
		tomma.update1();
		tomma.run();

	}

	public void set(){
		String ch1,ch2;
		ch1 = new String();
		ch2 = new String();
		while(true){
			try{//读取文件
				InputStreamReader r1 = new InputStreamReader(System.in);
				BufferedReader r2 = new BufferedReader(r1);
				System.out.println("输入命令: ");
				ch1 = r2.readLine();
			}catch(IOException e){
				System.out.println("Extraordinarily");
			}
			String[] s;
			s = ch1.split(" ");
			if(s[0].equals("quit")){
				break;
			}else if(s[0].equals("setf")){
				//设置寄存器的值
				if(s.length > 2){
					int fn = 0;
					double fd = 0;
					for(int i = 0;i < s[1].length();i ++){
						fn = fn * 10 + s[1].charAt(i) - 48;
					}
					int fj,fk;
					fj = 0;
					fk = 0;
					for(int i = 0;i < s[2].length();i ++){
						if(s[2].charAt(i) == '.'){
							fj = 1;
						}else{
							fd = fd * 10 + s[2].charAt(i) - 48;
							if(fj == 1){
								fk ++;
							}
						}
					}
					//System.out.println(fd + " " + fj + " " + fk);

					double ffd = Math.pow(10,fk);
					fd = fd / ffd;
					//System.out.println(ffd + " " + fd + " " + fn);
					if(fn < 30){
						ud.tomdata.rrs_fd[fn] = fd;
						run_uping();
						up();
					}
				}
			}else if(s[0].equals("setm")){
				//设置MEM的值
				if(s.length > 2){
					int fn = 0;
					double fd = 0;
					for(int i = 0;i < s[1].length();i ++){
						fn = fn * 10 + s[1].charAt(i) - 48;
					}
					int fj,fk;
					fj = 0;
					fk = 0;
					for(int i = 0;i < s[2].length();i ++){
						if(s[2].charAt(i) == '.'){
							fj = 1;
						}else{
							fd = fd * 10 + s[2].charAt(i) - 48;
							if(fj == 1){
								fk ++;
							}
						}
					}
					//System.out.println(fd + " " + fj + " " + fk);

					double ffd = Math.pow(10,fk);
					fd = fd / ffd;
					//System.out.println(ffd + " " + fd + " " + fn);
					if(fn < 4096){
						ud.tomdata.mem[fn] = fd;
						run_uping();
						up();
					}
				}
			}else{
				System.out.println("error order");
			}
		}
		System.out.println("选择运行模式:step ,continue");
		while(true){
			try{//读取文件
				InputStreamReader r1 = new InputStreamReader(System.in);
				BufferedReader r2 = new BufferedReader(r1);
				System.out.println("输入命令: ");
				ch2 = r2.readLine();
			}catch(IOException e){
				System.out.println("Extraordinarily");
			}
			if(ch2.equals("step")){
				step_or = true;
				break;
			}else if(ch2.equals("continue")){
				step_or = false;
				break;
			}
		}
	}

	public void run_uping(){
		Vector<String> columnNames_ld = new Vector<String>();
		Vector<String> columnNames_st = new Vector<String>();
		Vector<String> columnNames_rs = new Vector<String>();
		Vector<String> columnNames_f = new Vector<String>();
		Vector<String> columnNames_icr = new Vector<String>();
		Vector<String> columnNames_mem = new Vector<String>();
		Vector<String> columnNames_clock = new Vector<String>();

		columnNames_ld.add("");
		columnNames_ld.add("busy");
		columnNames_ld.add("address");
		columnNames_ld.add("cache");

		columnNames_st.add("");
		columnNames_st.add("busy");
		columnNames_st.add("address");
		columnNames_st.add("Qi");

		columnNames_rs.add("time");
		columnNames_rs.add("name");
		columnNames_rs.add("busy");
		columnNames_rs.add("OP");
		columnNames_rs.add("vi");
		columnNames_rs.add("vk");
		columnNames_rs.add("qi");
		columnNames_rs.add("qk");
			/*
			for(int i = 0;i < 30;i ++){
				String s = new String();
				s = s + 'F';
				s = s + i;
				columnNames_f.add(s);
			}
			*/
		columnNames_f.add("F_number");
		columnNames_f.add("q");
		columnNames_f.add("data");

		columnNames_icr.add("issue");
		columnNames_icr.add("complete");
		columnNames_icr.add("result");

		columnNames_mem.add("address");
		columnNames_mem.add("data");

		columnNames_clock.add("clock");

		table_ld = new JTable(new DefaultTableModel(rowData_ld,columnNames_ld));
		table_st = new JTable(new DefaultTableModel(rowData_st,columnNames_st));
		table_rs = new JTable(new DefaultTableModel(rowData_rs,columnNames_rs));
		table_f = new JTable(new DefaultTableModel(rowData_f,columnNames_f));
		table_icr = new JTable(new DefaultTableModel(rowData_icr,columnNames_icr));
		table_mem = new JTable(new DefaultTableModel(rowData_mem,columnNames_mem));
		table_clock = new JTable(new DefaultTableModel(rowData_clock,columnNames_clock));


		JScrollPane jsp_ld = new JScrollPane(table_ld);
		JScrollPane jsp_st = new JScrollPane(table_st);
		JScrollPane jsp_rs = new JScrollPane(table_rs);
		JScrollPane jsp_f = new JScrollPane(table_f);
		JScrollPane jsp_icr = new JScrollPane(table_icr);
		JScrollPane jsp_mem = new JScrollPane(table_mem);
		JScrollPane jsp_clock = new JScrollPane(table_clock);

		f.add(jsp_ld);
		f.add(jsp_st);
		f.add(jsp_rs);
		f.add(jsp_f);
		f.add(jsp_icr);
		f.add(jsp_mem);
		f.add(jsp_clock);

		jsp_ld.setBounds(20,210,200,100);
		jsp_st.setBounds(20,320,200,100);
		jsp_rs.setBounds(20,440,500,120);
		jsp_f.setBounds(550,50,220,600);
		jsp_icr.setBounds(250,50,200,200);
		jsp_mem.setBounds(800,50,150,600);
		jsp_clock.setBounds(300,320,50,37);
		f.setVisible(true);
	}

	public void run(){

		pc_read = 0;
		ud.tomdata.pc = 1;
		ud.tomdata.clock = 0;
		tomma.run_uping();
		boolean run_end = true;
		while(run_end){
			run_p();
			up();
			if(!step_or){
				try{
					Thread.sleep(1000);
				}catch(InterruptedException e){
					System.out.println("Extraordinarily");
				}
			}else{
				try{
					String ch1;
					InputStreamReader r1 = new InputStreamReader(System.in);
					BufferedReader r2 = new BufferedReader(r1);
					System.out.println("请按任意键继续: ");
					ch1 = r2.readLine();
				}catch(IOException e){
					System.out.println("Extraordinarily");
				}
			}
			if(information[ud.tomdata.pc].equals("END")){
				int count = 0;
				for(int i = 0;i < 3;i ++){
					if(ud.tomdata.ld_busy[i] == 1){
						continue;
					}
					if(ud.tomdata.st_busy[i] == 1){
						continue;
					}
					count ++;
				}
				if(count == 3){
					int countent = 0;
					for(int i = 0;i < 5;i ++){
						if(ud.tomdata.rs_busy[i] == 1){
							continue;
						}
						countent ++;
					}
					if(countent == 5){
						run_end = false;
					}
				}
			}
		}
		//run_p();
	}
	public void run_p(){
		//while(true){

		if(pc_read != ud.tomdata.pc){
			int f1,f2,f3;
			f1 = 0;
			f2 = 0;
			f3 = 0;
			char ch;
			ch = information[ud.tomdata.pc].charAt(0);
			if(information[ud.tomdata.pc].charAt(0) == 'A'){//加法
				ud.tomdata.ins_op[0] = 3;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
			}else if(information[ud.tomdata.pc].charAt(0) == 'M'){//乘法
				ud.tomdata.ins_op[0] = 5;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
			}else if(information[ud.tomdata.pc].charAt(0) == 'D'){//除法
				ud.tomdata.ins_op[0] = 6;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
			}else if(information[ud.tomdata.pc].charAt(0) == 'L'){//LD
				ud.tomdata.ins_op[0] = 1;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == ','){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
						}
						ud.tomdata.ins_op[k] = ork;
						ud.tomdata.ins_ls = ork;
					}
				}
			}else if(information[ud.tomdata.pc].charAt(0) == 'S' && information[ud.tomdata.pc].charAt(1) == 'U'){//sub
				ud.tomdata.ins_op[0] = 4;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
			}else if(information[ud.tomdata.pc].charAt(0) == 'S' && information[ud.tomdata.pc].charAt(1) == 'T'){//ST
				ud.tomdata.ins_op[0] = 2;
				int k = 1;
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == 'F'){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							if(information[ud.tomdata.pc].charAt(j) == ','){
								break;
							}else{
								ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
							}
						}
						ud.tomdata.ins_op[k] = ork;
						k ++;
					}
				}
				for(int i = 0;i < information[ud.tomdata.pc].length();i ++){
					if(information[ud.tomdata.pc].charAt(i) == ','){
						int ork = 0;
						for(int j = i + 1;j < information[ud.tomdata.pc].length();j ++){
							ork = 10 * ork + information[ud.tomdata.pc].charAt(j) - 48;
						}
						ud.tomdata.ins_op[k] = ork;
						ud.tomdata.ins_ls = ork;
					}
				}
			}else{
				ud.tomdata.ins_op[0] = 0;
			}
		}else{
			ud.tomdata.ins_op[0] = 0;
		}
		pc_read = ud.tomdata.pc;
		ud.tomdata.clock ++;
		System.out.println(ud.tomdata.ins_op[0] + " " + ud.tomdata.ins_op[1]);
		tom.go(ud.tomdata);
	}
	public tommain(){
		f = new JFrame("tom");
		f.setBounds(0,0,1000,700);
		//openfile = new JButton("open file");
		//run_step = new JButton("run step");
		//run_continue = new JButton("run continue");
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.add(openfile);
		//f.add(run_step);
		//f.add(run_continue);
		//openfile.setBounds(40,20,100,20);
		//run_step.setBounds(240,20,100,20);
		//run_continue.setBounds(440,20,100,20);
		textarea = new TextArea();
		f.add(new JScrollPane(textarea));
		//openfile.addActionListener(this);
		//run_step.addActionListener(this);
		//run_continue.addActionListener(this);

		Vector<String> columnNames = new Vector<String>();
		columnNames.add("OP");
		columnNames.add("F1");
		columnNames.add("F2");
		columnNames.add("F3");
		JTable table;
		table = new JTable(new DefaultTableModel(rowData, columnNames));
		JScrollPane jsp = new JScrollPane(table);
		f.add(jsp);
		jsp.setBounds(20,50,200,150);
		rowData.clear();
		for (int i = 0; i < 10; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			rowData.add(info);
		}
		//update();
		Vector<String> columnNames_ld = new Vector<String>();
		Vector<String> columnNames_st = new Vector<String>();
		Vector<String> columnNames_rs = new Vector<String>();
		Vector<String> columnNames_f = new Vector<String>();
		Vector<String> columnNames_icr = new Vector<String>();
		Vector<String> columnNames_mem = new Vector<String>();
		Vector<String> columnNames_clock = new Vector<String>();

		columnNames_ld.add("");
		columnNames_ld.add("busy");
		columnNames_ld.add("address");
		columnNames_ld.add("cache");

		columnNames_st.add("");
		columnNames_st.add("busy");
		columnNames_st.add("address");
		columnNames_st.add("Qi");

		columnNames_rs.add("time");
		columnNames_rs.add("name");
		columnNames_rs.add("busy");
		columnNames_rs.add("OP");
		columnNames_rs.add("vi");
		columnNames_rs.add("vk");
		columnNames_rs.add("qi");
		columnNames_rs.add("qk");
			/*
			for(int i = 0;i < 30;i ++){
				String s = new String();
				s = s + 'F';
				s = s + i;
				columnNames_f.add(s);
			}
			*/

		columnNames_f.add("F_number");
		columnNames_f.add("q");
		columnNames_f.add("data");

		columnNames_icr.add("issue");
		columnNames_icr.add("complete");
		columnNames_icr.add("result");

		columnNames_mem.add("address");
		columnNames_mem.add("data");

		columnNames_clock.add("clock");

		table_ld = new JTable(new DefaultTableModel(rowData_ld,columnNames_ld));
		table_st = new JTable(new DefaultTableModel(rowData_st,columnNames_st));
		table_rs = new JTable(new DefaultTableModel(rowData_rs,columnNames_rs));
		table_f = new JTable(new DefaultTableModel(rowData_f,columnNames_f));
		table_icr = new JTable(new DefaultTableModel(rowData_icr,columnNames_icr));
		table_mem = new JTable(new DefaultTableModel(rowData_mem,columnNames_mem));
		table_clock = new JTable(new DefaultTableModel(rowData_clock,columnNames_clock));

		JScrollPane jsp_ld = new JScrollPane(table_ld);
		JScrollPane jsp_st = new JScrollPane(table_st);
		JScrollPane jsp_rs = new JScrollPane(table_rs);
		JScrollPane jsp_f = new JScrollPane(table_f);
		JScrollPane jsp_icr = new JScrollPane(table_icr);
		JScrollPane jsp_mem = new JScrollPane(table_mem);
		JScrollPane jsp_clock = new JScrollPane(table_clock);

		f.add(jsp_ld);
		f.add(jsp_st);
		f.add(jsp_rs);
		f.add(jsp_f);
		f.add(jsp_icr);
		f.add(jsp_mem);
		f.add(jsp_clock);
		jsp_ld.setBounds(20,210,200,100);
		jsp_st.setBounds(20,320,200,100);
		jsp_rs.setBounds(20,440,500,120);
		jsp_f.setBounds(550,50,220,600);
		jsp_icr.setBounds(250,50,200,200);
		jsp_mem.setBounds(800,50,150,600);
		jsp_clock.setBounds(300,320,50,37);

		rowData_clock.clear();
		Vector<Object> info_c = new Vector<Object>();
		info_c.add("");
		rowData_clock.add(info_c);

		rowData_ld.clear();
		for (int i = 0; i < 3; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			rowData_ld.add(info);
		}

		rowData_st.clear();
		for (int i = 0; i < 3; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			rowData_st.add(info);
		}

		rowData_rs.clear();
		for (int i = 0; i < 5; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			rowData_rs.add(info);
		}

		rowData_f.clear();
		for (int i = 0; i < 30; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			rowData_f.add(info);
		}

		rowData_icr.clear();
		for (int i = 0; i < 10; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			rowData_icr.add(info);
		}

		rowData_mem.clear();
		for (int i = 0; i < 4096; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			rowData_mem.add(info);
		}

		/*
		rowData.clear();
		for (int i = 0; i < 10; i++) {
			Vector<Object> info = new Vector<Object>();
			info.add("");
			info.add("");
			info.add("");
			info.add("");
			rowData.add(info);
		}
		*/
		f.setSize(1000,700);
		f.setVisible(true);
		//update();
	}

	private void up(){

		new SwingWorker<Object, Object>() {
			protected Object doInBackground() throws Exception {


				rowData_ld.clear();
				rowData_st.clear();
				rowData_rs.clear();
				rowData_f.clear();
				rowData_icr.clear();
				rowData_mem.clear();
				rowData_clock.clear();

				Vector<Object> info_c = new Vector<Object>();
				info_c.add(ud.tomdata.clock);
				rowData_clock.add(info_c);

				for(int i = 1;i < ud.tomdata.pc;i ++){
					Vector<Object> info = new Vector<Object>();
					info.add(i);
					if(ud.tomdata.icr_complete[i] != 0){
						info.add(ud.tomdata.icr_complete[i]);
					}else{
						info.add("");
					}
					if(ud.tomdata.icr_result[i] != 0){
						info.add(ud.tomdata.icr_result[i]);
					}else{
						info.add("");
					}
					rowData_icr.add(info);
				}

				for(int i = 0;i < 3;i ++){
					Vector<Object> info = new Vector<Object>();
					String s = new String();
					s = s + "load";
					int b = i + 1;
					s = s + b;
					info.add(s);
					if(ud.tomdata.ld_busy[i] == 0){
						info.add("NO");
						info.add("");
						info.add("");
					}else{
						info.add("YES");
						info.add(ud.tomdata.ld_address[i]);
						info.add("");
					}
					rowData_ld.add(info);
				}

				for(int i = 0;i < 3;i ++){
					Vector<Object> info = new Vector<Object>();
					String s = new String();
					s = s + "store";
					int b = i + 1;
					s = s + b;
					info.add(s);
					if(ud.tomdata.st_busy[i] == 0){
						info.add("NO");
						info.add("");
						info.add("");
					}else{
						info.add("YES");
						info.add(ud.tomdata.ld_address[i]);
						String sf = new String();
						sf = sf + "F";
						sf = sf + ud.tomdata.st_q[i];
						info.add(sf);
					}
					rowData_st.add(info);
				}

				for(int i = 0;i < 5;i ++){
					Vector<Object> info = new Vector<Object>();
					String s = new String();
					int b;
					if(i < 3){
						s = s + "ADD";
						b = i + 1;
					}else{
						s = s + "MUL";
						b = i - 2;
					}
					s = s + b;
					info.add(ud.tomdata.rs_time[i]);
					info.add(s);
					if(ud.tomdata.rs_busy[i] == 0){
						info.add("NO");
						info.add("");
						info.add("");
						info.add("");
						info.add("");
						info.add("");
					}else{
						info.add("YES");
						int rsop = ud.tomdata.rs_op[i];
						if(rsop == 3){
							info.add("ADDD");
						}else if(rsop == 4){
							info.add("SUBD");
						}else if(rsop == 5){
							info.add("MULD");
						}else if(rsop == 6){
							info.add("DIVD");
						}else{
							info.add("");
						}
						int vjtt = ud.tomdata.rs_vjt[i];
						int vktt = ud.tomdata.rs_vkt[i];
						String rsf = new String();
						rsf = rsf + 'F';
						if(vjtt == 0 && vktt == 0){
							info.add(ud.tomdata.rs_vj[i]);
							info.add(ud.tomdata.rs_vk[i]);
							info.add("");
							info.add("");
						}else if(vjtt == 0 && vktt == 1){
							info.add(ud.tomdata.rs_vj[i]);
							info.add("");
							info.add("");
							rsf = rsf + ud.tomdata.rs_qk[i];
							info.add(rsf);
						}else if(vjtt == 1 && vktt == 0){
							info.add("");
							info.add(ud.tomdata.rs_vk[i]);
							rsf = rsf + ud.tomdata.rs_qj[i];
							info.add(rsf);
							info.add("");
						}else{
							info.add("");
							info.add("");
							rsf = rsf + ud.tomdata.rs_qj[i];
							info.add(rsf);
							String rsfk = new String();
							rsfk = rsfk + 'F';
							rsfk = rsfk + ud.tomdata.rs_qk[i];
							info.add(rsfk);
						}
					}
					rowData_rs.add(info);
				}

				for(int i = 0;i < 30;i ++){
					Vector<Object> info = new Vector<Object>();
					String s = new String();
					s = s + 'F';
					s = s + i;
					info.add(s);
					int a,b;
					String ss = new String();
					a = ud.tomdata.rrs_ff[i] / 10;
					b = ud.tomdata.rrs_ff[i] % 10;
					if(a == 1){
						ss = ss + "load";
						b += 1;
						ss = ss + b;
					}else if(a == 2){
						ss = ss + "store";
						b += 1;
						ss = ss + b;
					}else if(a == 3 || a == 4){
						ss = ss + "ADD";
						b += 1;
						ss = ss + b;
					}else if(a == 5 || a == 6){
						ss = ss + "MUL";
						b -= 2;
						ss = ss + b;
					}else{
						ss = ss + '0';
					}

					info.add(ss);
					if(a == 0){
						info.add(ud.tomdata.rrs_fd[i]);
					}else{
						info.add("UNKOWN");
					}
					rowData_f.add(info);
				}


				for(int i = 0;i < 4096;i ++){
					Vector<Object> info = new Vector<Object>();
					info.add(i);
					info.add(ud.tomdata.mem[i]);
					rowData_mem.add(info);
				}

				//((DefaultTableModel) table_ld.getModel()).fireTableDataChanged();
				return null;
			}
			protected void done() {
				((DefaultTableModel) table_ld.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_st.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_rs.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_f.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_icr.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_mem.getModel()).fireTableDataChanged();
				((DefaultTableModel) table_clock.getModel()).fireTableDataChanged();
			}
		}.execute();

	}
	private void update(){

		new SwingWorker<Object, Object>() {
			protected Object doInBackground() throws Exception {

				rowData.clear();
				for (int i = 0; i < 10; i++) {
					Vector<Object> info = new Vector<Object>();
					info.add("");
					info.add("");
					info.add("");
					info.add("");
					rowData.add(info);
				}

				return null;
			}
			protected void done() {
				((DefaultTableModel) table.getModel()).fireTableDataChanged();
			}
		}.execute();

	}

	public void update1(){

		new SwingWorker<Object, Object>() {
			protected Object doInBackground() throws Exception {

				rowData.clear();
				for (int i = 1; i < information.length; i++) {
					//information[i];
					String[] order_information;
					String[] FS;
					order_information = information[i].split(" ");
					FS = order_information[1].split(",");
					Vector<Object> info = new Vector<Object>();
					info.add(order_information[0]);
					info.add(FS[0]);
					info.add(FS[1]);
					if(FS.length > 2){
						info.add(FS[2]);
					}else{
						info.add("");
					}
					rowData.add(info);
				}

				return null;
			}
			protected void done() {
				((DefaultTableModel) table_t.getModel()).fireTableDataChanged();
			}
		}.execute();

	}

	/*
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == openfile){
			if(runing == 0){
				JFileChooser f1 = new JFileChooser();
				int a;
				a = f1.showOpenDialog(f);
				if (a == JFileChooser.APPROVE_OPTION){
					File file = f1.getSelectedFile();
					textarea.append(file.getAbsolutePath()+"\n");

					filename = file.getName();
					System.out.println(filename);
					//tomm.run();
					int order = 1;
					try{//读取文件
						String ch1,ch2;
						//InputStreamReader r1 = new InputStreamReader(System.in);
						//BufferedReader r2 = new BufferedReader(r1);
						//System.out.println("输入要读取的文件名: ");
						//ch1 = r2.readLine();
						File file_or = new File(filename);
						if(!file_or.exists()){
							System.out.println("文件不存在");
							return;
						}
						FileInputStream is = new FileInputStream(filename);
						InputStreamReader ir = new InputStreamReader(is);
						BufferedReader in = new BufferedReader(ir);
						while(true){
							information[order] = in.readLine();
							if(information[order].equals("END")){
								System.out.println(information[order] + " " + order);
								break;
							}
							order ++;
						}
					}catch(IOException e){
						System.out.println("Extraordinarily");
					}

					Vector<String> columnNames = new Vector<String>();
					columnNames.add("OP");
					columnNames.add("F1");
					columnNames.add("F2");
					columnNames.add("F3");
					//JTable table;
					table_t = new JTable(new DefaultTableModel(rowData, columnNames));
					JScrollPane jsp = new JScrollPane(table_t);
					f.add(jsp);
					jsp.setBounds(50,50,150,150);
					update1();
					runing ++;
					tomma.run();
				}
			}
		}
		else if(event.getSource() == run_step){
			if(runing == 1){
				f.run();
				runing ++;
			}
		}else if(event.getSource() == run_continue){
			if(runing == 1){
				f.run();
				runing ++;
			}
		}

	}*/
}