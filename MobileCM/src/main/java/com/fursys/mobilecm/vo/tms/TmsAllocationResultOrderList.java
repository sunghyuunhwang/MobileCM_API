package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsAllocationResultOrderList {
	
	private String orderId;
	private String orderName;
	private String address;
	private String latitude;
	private String longitude;
	private String serviceTime;
	private String zoneCode;
	private String expectedArrivalTime;
	private String expectedDepartureTime;
    	
}
