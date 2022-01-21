package com.fursys.mobilecm.vo.erp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPMobileContentList {
	
	private int req_no;
	private String req_dt;
	private String req_usr_cd;
	private String req_std;
	private String req_std_nm;
	private String req_proc;
	private String req_proc_nm;
	private String req_title;
	private String req_content;
	private String ans_content;
	private String ans_dt;
	private String ans_usr_cd;
	private String fst_usr_cd;
	private String fst_sys_dt;
	private String usr_cd;
	private String sys_dt;

	private int file_cnt = 0;
}


