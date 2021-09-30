package com.fursys.mobilecm.vo.tms;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsAllocationResultVehicleList {
	
	private String vehicleId;
	private String vehicleName;
	private String deliveryCount;
	private String deliveryTime;
	private String deliveryDistance;
	private String deliveryWeight;
	
	private ArrayList<TmsAllocationResultOrderList> orderList;
	
}
