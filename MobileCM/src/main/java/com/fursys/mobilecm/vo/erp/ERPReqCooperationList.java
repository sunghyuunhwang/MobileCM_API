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
public class ERPReqCooperationList {
	private String plm_cdt ;
	private String orm_nm ; 
	private String com_brand ; 
	private String plm_no ; 
	private int req_amt;
	private int req_amt_detail;
	private int req_stat_m;
	private String sti_cd;
	private String sti_nm;
	private String req_stat;
	private String o_sti_cd;
	private String o_sti_nm;
	private String o_plm_no;
}


