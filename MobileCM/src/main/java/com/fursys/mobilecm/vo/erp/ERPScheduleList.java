package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPScheduleList {
	private String com_ssec = "" ; // COM_SSEC
	private String com_brand = "" ; //COM_BRAND
	private String rem_dt = "" ; //REM_DT
	private String rem_seq = "" ; //REM_SEQ
	private String plm_no = "" ; //PLM_NO
	private String orm_no = "" ; //ORM_NO
	private String plm_ptm = "" ; //PLM_PTM
	private String plm_ptmnm = ""; //PLM_PTMNM
	private String rem_ftm = "" ; //REM_FTM
	private String rem_ftmnm = "" ; //REM_FTMNM
	private String orm_nm = "" ; //ORM_NM
	private String orm_gaddr = "" ; //ORM_GADDR
	private String orm_hdphone = "" ; //ORM_HDPHONE
	private Integer ord_amt = 0 ; //ORD_AMT
	private String wallfix_yn = "" ; //WALLFIX_YN
	private String orm_ggubun = "" ; //ORM_GGUBUN
	private String cannot_one = "" ; //CANNOT_ONE
	private String lddr_yn = "" ; //LDDR_YN
	private String plm_itr = "" ; //PLM_ITR
	private String plm_evt = "" ; //PLM_EVT
	private String trash_yn = "" ; //TRASH_YN
	private String trans_yn = "" ; // TRANS_YN
	private String com_rmfg = "" ; // COM_RMFG
	private String com_rmfg_sub = "" ; // COM_RMFG
	private String com_rmfg_sub2 = "" ; // COM_RMFG
	private String rem_tmfyn = "" ; //REM_TMFYN
	private String latitude = "" ; 
	private String longitude = "" ;
	private String arrival_seq = "";
	private Integer pld_sum = 0;
}


