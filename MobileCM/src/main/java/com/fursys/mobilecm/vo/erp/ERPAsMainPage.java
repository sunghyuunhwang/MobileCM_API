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
public class ERPAsMainPage {
	
	private String com_ssec ;
	private String com_ssec_nm ;
	private String plm_no ;
	private String rpt_no ;
	private String orm_nm ; 
	private String agt_cd ; 
	private String agt_nm ;	
	private String orm_gaddr ;
	private String orm_gemptel;
	private String orm_hdphone ;
	private String ctm_dnm ;
	private String com_spycod ;
	private String rpt_desc ;
	private String rpt_descplus ;
	private String wallfix_yn ; 
	private String orm_ggubun ; 
	private String cannot_one ; 
	private String lddr_yn ; 
	private String plm_itr ; 
	private String plm_evt ; 
	private String trash_yn ; 
	private String trans_yn ; 
	private String file_id ;
	private String com_rpsec ;
	private String ctm_tel;
	private String refund_yn ;
	private String sigong_yn ;
	private String rem_rmk ;
	private String com_brand;
	private String com_brand_nm;
	private String start_yn;
	private String end_yn;
	private String rpt_seq;
	private String com_agsec;
	private String rem_dt;
	private String rem_seq;
	private String rem_csec;
	private String orm_no;
	private String migeul_add_yn;
	private String sigong_plm_no;
	private String sti_nm;
	private String sti_cd;
	private String com_stsec;
	private String com_rfg;
	private String ctm_dnm2;	
	private String vnd_addr;
	private String plm_ptmnm;
	private String rem_ftmnm;
	private String req_time;
	private String req_location;
	
	private ArrayList<ERPAsFileList> fileList;
	
}


