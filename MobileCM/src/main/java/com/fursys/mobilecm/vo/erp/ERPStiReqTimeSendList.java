package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPStiReqTimeSendList {
	private String rem_dt ; //REM_DT
	private String rem_seq ; //REM_SEQ
	private String orm_no ; //ORM_NO
	private String rem_ftm ; //REM_FTM
	private String orm_nm ; //ORM_NM
	private String orm_hdphone ; //ORM_HDPHONE
	private String sti_nm;
	private String sti_cd;
	private String com_ssec;
	private String com_agsec;
	private String com_brand_cd;
	private String com_rmfg;
}


