package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPCenterList {
	
	private String com_scd;
	private String ccd_nm;
	private String scd_gaddr;
	
	private String loc_lat;
	private String loc_lon;
	
}


