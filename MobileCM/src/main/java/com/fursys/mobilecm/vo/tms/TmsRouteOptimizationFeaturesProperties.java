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
public class TmsRouteOptimizationFeaturesProperties {	

	private String index;
	private String viaPointId;
	private String viaPointName;
	private String arriveTime;
	private String completeTime;
	private String distance;
	private String deliveryTime;
	private String waitTime;
	private String pointType;
}
