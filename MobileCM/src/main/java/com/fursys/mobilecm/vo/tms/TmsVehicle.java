package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsVehicle extends TmsVehicleBase{
	private String updateDate;
	private String startAddress;
	private String endLatitude;
	private String speed;
	private String volume;
	private String costPerHour;
	private String startLatitude;
	private String waitcostPerHour;
	private String endLongitude;
	private String startLongitude;
	private String costPerKm;
	private String seq;
	private String endAddress;
}
