package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPMigeulDetailList {
	private String itm_cd ; 
	private String col_cd; 
	private String itm_nm ;
	private int pld_fqty ;
	private String mob_nfinished ;
	private String remark;
	private String com_pldsec;
}


