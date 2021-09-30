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
public class ERPSigongSearchInfo {
	
	private String rem_dt = "";
	private String rem_seq = "";
	private String orm_nm = "";
	private String com_ssec = "";
	private String com_scd_nm = "";
	private String sti_nm = "";
	private Integer orm_amt;
	private String rem_ftm = "";
	private String orm_gaddr = "";
	private String orm_hdphone = "";
	private String orm_no = "";
}


