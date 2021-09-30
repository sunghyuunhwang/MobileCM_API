package com.fursys.mobilecm.vo.tms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsVehicleBase {
	private String vehicleId;
	private String vehicleName;
	private String weight;
	private String vehicleType;
	private String zoneCode;
}
