package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPSigonResult {
	private String rmk;
	private String re_plm_no;
	private String re_rpt_no;
	private String re_rpt_seq;
	private String pld_rmk;
	private String pld_rcdt;
}
