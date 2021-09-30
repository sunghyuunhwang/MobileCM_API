package com.fursys.mobilecm.vo.tms.reponse;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.tms.TmsRouteOptimizationFeatures;
import com.fursys.mobilecm.vo.tms.TmsRouteOptimizationProperties;
import com.fursys.mobilecm.vo.tms.TmsVehicleBaseForAllocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsRouteOptimizationResponse extends BaseResponse{
	
	private String type;
	private TmsRouteOptimizationProperties properties;
	private ArrayList<TmsRouteOptimizationFeatures> features;

}
