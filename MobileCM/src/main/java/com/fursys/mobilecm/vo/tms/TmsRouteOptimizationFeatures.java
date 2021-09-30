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
public class TmsRouteOptimizationFeatures {	
	private String type;
	private TmsRouteOptimizationFeaturesGeometry geometry;
	private TmsRouteOptimizationFeaturesProperties properties;
}
