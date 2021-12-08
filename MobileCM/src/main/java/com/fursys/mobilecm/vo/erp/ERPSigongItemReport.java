package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPSigongItemReport {
	private String set_cd = "";
	private String itm_cd = "";
	private String col_cd = "";
	private int pld_fqty = 0;
	private int pld_famt = 0;
	private int pld_cramt = 0;
	private String itm_nm = "";
	private String com_rdsec = "";
}


