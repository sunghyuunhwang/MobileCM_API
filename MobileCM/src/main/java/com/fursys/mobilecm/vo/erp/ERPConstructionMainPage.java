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
public class ERPConstructionMainPage {
	private String com_ssec ;
	private String com_brand;
	private String plm_no ;
	private String orm_no ;
	private String orm_nm ; 
	private String orm_gaddr ;
	private String orm_gemptel;
	private String orm_hdphone ;
	private String agt_cd ; 
	private String agt_nm ;
	private String wallfix_yn ; 
	private String orm_ggubun ; 
	private String cannot_one ; 
	private String lddr_yn ; 
	private String plm_itr ; 
	private String plm_evt ; 
	private String trash_yn ; 
	private String trans_yn ; 
	private String vnd_telno;
	private String rem_rmk;
	private String rem_rmk2;
	private String file_id;
	private String rem_csec;
	private String com_brand_nm;
	private String com_brand_cd;
	private String com_ssec_nm ;
	private String com_ssec_cd ;
	private String start_yn;	
	private String end_yn;
	private String com_agsec;
	private String rem_dt;
	private String rem_seq;
	private String banpum_yn;
	private String sti_nm;
	private String sti_cd;
	private String com_stsec;
	private String com_rfg;
	private String vnd_addr;
	private String plm_ptmnm;
	private String rem_ftmnm;	
	private String cep_nm;
	private String cep_mp;
	private String stm_no;
	private String stm_nm;
	private String ord_amt;
	private Integer pld_sum;
	
	
	private ArrayList<ERPAsFileList> fileList_0;
	private ArrayList<ERPAsFileList> fileList_1;
	private ArrayList<ERPAsFileList> fileList_2;
}


