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
public class TmsVehicleBaseForAllocation extends TmsVehicleBase {
	private String deliveryCount;
	private String deliveryTime;
	private String deliveryDistance;
	private String deliveryWeight;
	private ArrayList<TmsOrderBaseForAllocation> orderList;
	private TmsLocation startLocation;
	private TmsLocation endLocation;
	private ArrayList<TmsRoute> routeList;
}
