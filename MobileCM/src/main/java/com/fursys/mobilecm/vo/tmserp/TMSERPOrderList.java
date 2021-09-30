package com.fursys.mobilecm.vo.tmserp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TMSERPOrderList {
	
	private String orderId;
	private String orderName;
	private String address;
	private String latitude;
	private String longitude;
	private String vehicleType = "01";
	private String serviceTime = "10";
	private String zoneCode = "";	
	private String deliveryWeight = "0";
	private String sti_cd;
	private String com_ctsec;
	private String com_scd;
	private String rem_dt;
	private String rem_seq;	
	private String plm_no;
	private String orm_no;
	private String sti_nm;
	private String com_ssec;
	private String com_rfg;
	
}


