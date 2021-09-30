package com.fursys.mobilecm.vo.erp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPBusinessTripDetail {
	private String plm_no = "";
	private String mob_starttm = "";
	private String com_rmfg = "";
	private String orm_nm = "";
	private String orm_gaddr = "";
	private String mob_endtm = "";
	private String latitude = "";
	private String longitude = "";
}


