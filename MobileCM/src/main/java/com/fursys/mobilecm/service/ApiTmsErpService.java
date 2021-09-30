package com.fursys.mobilecm.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_tabList1;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongResultResponse;

public interface ApiTmsErpService {
	
	public BaseResponse tmserp_RouteSequential(HashMap<String, Object> param);
	public BaseResponse tmserp_RouteOptimization(HashMap<String, Object> param);
	public BaseResponse tmserp_AllocationUpdate(HashMap<String, Object> param);
	public BaseResponse tmserp_UpdateOrderGeocoding(HashMap<String, Object> param);
	public BaseResponse tmserp_AllocationConfirmSet(HashMap<String, Object> param);
	public BaseResponse tmserp_AllocationConfirm(HashMap<String, Object> param);
	public BaseResponse tmserp_Allocation(HashMap<String, Object> param);
	public BaseResponse tmserp_UpdateOrder(HashMap<String, Object> param);
	public BaseResponse tmserp_UpdateVehicle(HashMap<String, Object> param);
	public BaseResponse tmserp_UpdateCenter(HashMap<String, Object> param);
	
}
