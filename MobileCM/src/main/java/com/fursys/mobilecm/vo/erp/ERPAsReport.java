package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPAsReport {
	private String rpt_no = "";
	private String vnd_nm = "";
	private String vnd_telno = "";
	private String ctm_nm = "";
	private String ctm_addr = "";
	private String rpt_usrnm = "";
	private String rpt_dt = "";
	private String rpt_desc = "";
	private String rpt_descplus = "";
	private String rpt_enddt = "";
	private String rpt_qdt = "";
	private String com_scd_nm = "";
	private String sti_nm = "";
	private String mob_remark = "";
	
}


