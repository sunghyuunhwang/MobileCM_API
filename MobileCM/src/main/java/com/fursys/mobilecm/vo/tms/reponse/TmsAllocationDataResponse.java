package com.fursys.mobilecm.vo.tms.reponse;

import java.util.ArrayList;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.tms.TmsVehicleBaseForAllocation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TmsAllocationDataResponse extends BaseResponse{
	private String vehicleCount;
	private ArrayList<TmsVehicleBaseForAllocation> vehicleList;
	private ArrayList<TmsVehicleBaseForAllocation> vehicleRouteList;
}
