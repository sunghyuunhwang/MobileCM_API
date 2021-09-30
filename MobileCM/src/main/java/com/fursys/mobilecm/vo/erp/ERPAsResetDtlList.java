package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsResetDtlList {
	private String com_rdsec;
	private String com_undsec;
	private String com_undsec1;
	private String com_undsec2;
	private String com_unpsec;
	private String pld_rcdt;
	private String pld_rmk;
	private int pld_fqty;
	private int pld_cfamt;
	private int pld_eqty;
}
