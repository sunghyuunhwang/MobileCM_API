package com.fursys.mobilecm.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.config.ServerInfo;
import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.mapper.ORMMapper;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.mapper.TMSERPSchedulingMapper;
import com.fursys.mobilecm.service.ApiTmsErpService;
import com.fursys.mobilecm.utils.RestService;
import com.fursys.mobilecm.utils.RestService.RestServiceCallBack;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPSti;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.tms.TmsOrderBase;
import com.fursys.mobilecm.vo.tms.TmsRouteOptimization;
import com.fursys.mobilecm.vo.tms.TmsVehicle;
import com.fursys.mobilecm.vo.tms.TmsVehicleBase;
import com.fursys.mobilecm.vo.tms.TmsVehicleBaseForAllocation;
import com.fursys.mobilecm.vo.tms.TmsZoneBase;
import com.fursys.mobilecm.vo.tms.reponse.TmsAllocationDataResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsCenterListResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsGeocodingCoordinateInfoResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsOrderResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsRouteOptimizationResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsVehicleListResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsZoneListResponse;
import com.fursys.mobilecm.vo.tmserp.TMSERPCenterList;
import com.fursys.mobilecm.vo.tmserp.TMSERPOrderList;
import com.fursys.mobilecm.vo.tmserp.TMSERPVehicleList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/api/tms")
public class ApiTmsController {
	@Autowired ORMMapper oRMMapper;
	@Autowired STIMapper sTIMapper;
	@Autowired TMSERPSchedulingMapper tmserpScheduling;
	@Autowired ApiTmsErpController apiTmsErpController;
	Gson gson = new Gson();
	
	
	TmsRouteOptimizationResponse routeSequentialResponse = new TmsRouteOptimizationResponse();
	@ApiOperation(value = "routeSequentialResponse", notes = "다중경유지")
	@RequestMapping(value="/routeSequentialResponse",method=RequestMethod.POST)
	public String tms_routeSequential(String appKey, TmsRouteOptimization routesequential) throws UnsupportedEncodingException {
		
		String baseUrl = "https://apis.openapi.sk.com/tmap/routes/routeSequential30?version=1";
		
		RestService.postAppKey(baseUrl, gson.toJson(routesequential), appKey, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {				
				//System.out.println(result);
				try {
					routeSequentialResponse = gson.fromJson(result, TmsRouteOptimizationResponse.class);
					routeSequentialResponse.setResultCode("200");
				} catch (Exception e) {
					routeSequentialResponse.setResultCode("5001");
					if ("Bad Request".equals(result)) {
						routeSequentialResponse.setResultMessage(result);
					} else {
						routeSequentialResponse.setResultMessage(e.toString());
					}
				}
			}
		});
		return gson.toJson(routeSequentialResponse);
	}
	
	TmsRouteOptimizationResponse routeOptimizationResponse = new TmsRouteOptimizationResponse();
	@ApiOperation(value = "routeOptimization", notes = "경유지 최적화")
	@RequestMapping(value="/routeOptimization",method=RequestMethod.POST)
	public String tms_routeOptimization(String appKey, TmsRouteOptimization routeoptimization) throws UnsupportedEncodingException {
		
		String baseUrl = "https://apis.openapi.sk.com/tmap/routes/routeOptimization30?version=1&format=json";
		
		RestService.postAppKey(baseUrl, gson.toJson(routeoptimization), appKey, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {				
				//System.out.println(result);
				try {
					routeOptimizationResponse = gson.fromJson(result, TmsRouteOptimizationResponse.class);
					routeOptimizationResponse.setResultCode("200");
				} catch (Exception e) {
					routeOptimizationResponse.setResultCode("5001");
					if ("Bad Request".equals(result)) {
						routeOptimizationResponse.setResultMessage(result);
					} else {
						routeOptimizationResponse.setResultMessage(e.toString());
					}
				}
			}
		});
		return gson.toJson(routeOptimizationResponse);
	}
	
	@ApiOperation(value = "tms_allocationRequest", notes = "TMS 배차요청")
	@RequestMapping(value="/tms_allocationRequest",method=RequestMethod.GET)
	public String tms_allocationRequest(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="allocationType", required=true, defaultValue = "1") String allocationType,
			@RequestParam(name="orderIdList", required=false) String orderIdList,
			@RequestParam(name="vehicleIdList", required=false)String vehicleIdList,
			@RequestParam(name="startTime", required=true,defaultValue = "0900")String startTime) { 
		
		String baseUrl = "https://apis.openapi.sk.com/tms/allocation";
		baseUrl += "?appKey=" + appKey;
		baseUrl += "&allocationType=" + allocationType;
		if (allocationType.equals("2")) {
			baseUrl += "&orderIdList=" + orderIdList;
			baseUrl += "&vehicleIdList=" + vehicleIdList;
		}
		baseUrl += "&startTime=" + startTime;
		
		System.out.println(baseUrl);
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				allocationResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		System.out.println(allocationResponse.toString());
		
		return gson.toJson(allocationResponse);
	}
	
	BaseResponse allocationResponse = new BaseResponse();
	TmsAllocationDataResponse tmpTmsAllocationDataResponse = new TmsAllocationDataResponse();
	
	BaseResponse orderListInsertResponse = new BaseResponse();
	@ApiOperation(value = "orderListInsert", notes = "TMS 배송지 여러건 추가")
	@RequestMapping(value="/orderListInsert",method=RequestMethod.POST)
	public String tms_orderListInsert(String appKey, ArrayList<TMSERPOrderList> arErpOrderList) throws UnsupportedEncodingException {
		
		for (int i = 0 ; i < arErpOrderList.size() ; i++) {
			arErpOrderList.get(i).setOrderName(URLEncoder.encode(arErpOrderList.get(i).getOrderName(),"UTF-8"));
			arErpOrderList.get(i).setAddress(URLEncoder.encode(arErpOrderList.get(i).getAddress(),"UTF-8"));
		}
		
		String baseUrl = "https://apis.openapi.sk.com/tms/orderListInsert";
		baseUrl += "?appKey=" + appKey;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reqDatas", gson.toJson(arErpOrderList));
		
		RestService.post(baseUrl, params.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				vehicleListInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(vehicleListInsertResponse);
	}

	BaseResponse centerInsertResponse = new BaseResponse();	
	@ApiOperation(value = "centerInsert", notes = "TMS 센터  등록")
	@RequestMapping(value="/centerInsert",method=RequestMethod.GET)
	public String tms_centerInsert(String appKey, TMSERPCenterList vo) {
		
		StringBuffer baseUrl = new StringBuffer();
		
		try {
			baseUrl.append("https://apis.openapi.sk.com/tms/centerInsert");
			//baseUrl.append(baseUrl + "?appKey=" + TMapInfo.appKey);
			baseUrl.append("?appKey=" + appKey);
			baseUrl.append("&centerId=" + vo.getCom_scd());
			baseUrl.append("&centerName=" + URLEncoder.encode(vo.getCcd_nm(), "UTF-8"));
			baseUrl.append("&address=" + URLEncoder.encode(vo.getScd_gaddr(), "UTF-8"));
			baseUrl.append("&latitude=" + vo.getLoc_lat());
			baseUrl.append("&longitude=" + vo.getLoc_lon());
		} catch (Exception e) {
			centerInsertResponse.setResultCode("5001");
			centerInsertResponse.setResultMessage(e.toString());			
		}
		RestService.get(baseUrl.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				if ("Not Found".equals(result)) {
					centerInsertResponse.setResultCode("5001");
					centerInsertResponse.setResultMessage("Not Found");
				} else {
					centerInsertResponse = gson.fromJson(result, BaseResponse.class);
				}
			}
		});
		return gson.toJson(centerInsertResponse);
	}	
	
	TmsGeocodingCoordinateInfoResponse mTmsGeocodingCoordinateInfoResponse = new TmsGeocodingCoordinateInfoResponse();
		
	@ApiOperation(value = "fullTextGeocoding", notes = "Geocoding")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "지오코딩 실패 !!") })
	@RequestMapping(value="/fullTextGeocoding",method=RequestMethod.GET)
	public String fullTextGeocoding(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="addressFlag", required=true, defaultValue = "F02") String addressFlag,
			@RequestParam(name="fullAddr", required=true, defaultValue = "경기 용인시 처인구 양지면 남평로 111-73 (양지리)") String fullAddr
			) { 
		
		try {
			
			//System.out.println("fullAddr=" + fullAddr);
			String baseUrl = "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?addressFlag=%s&coordType=WGS84GEO&version=1&format=json&fullAddr=%s&appKey=%s";
			//baseUrl = String.format(baseUrl, "F01", URLEncoder.encode(fullAddr, "UTF-8"), TMapInfo.appKey); //구주소(지번)
			//baseUrl = String.format(baseUrl, "F02",  URLEncoder.encode(fullAddr, "UTF-8"), TMapInfo.appKey); //신주소(도로명)
			
			//주소가 비어있을 경우 에러 처리
			if(fullAddr == null || fullAddr.equals("")) { 
				mTmsGeocodingCoordinateInfoResponse.setResultCode("5001"); 
				return gson.toJson(mTmsGeocodingCoordinateInfoResponse); 
			}
			 
			baseUrl = String.format(baseUrl, addressFlag,  URLEncoder.encode(fullAddr, "UTF-8"), appKey);
			
			//System.out.println(baseUrl);
			RestService.get(baseUrl, new RestServiceCallBack() {
				@Override
				public void onResult(String result) {
					mTmsGeocodingCoordinateInfoResponse = new TmsGeocodingCoordinateInfoResponse();					
					System.out.println("result=" + result);
					try {
						mTmsGeocodingCoordinateInfoResponse = gson.fromJson(result, TmsGeocodingCoordinateInfoResponse.class);
						mTmsGeocodingCoordinateInfoResponse.setResultCode("200");
					} catch (Exception e) {
						if ("Bad Request".equals(result)) {
							mTmsGeocodingCoordinateInfoResponse.setResultMessage(result);
						} else {
							mTmsGeocodingCoordinateInfoResponse.setResultMessage(e.toString());
						}
					}
				}
			});
			
			//System.out.println(mTmsGeocodingCoordinateInfoResponse.toString());
			
			if (mTmsGeocodingCoordinateInfoResponse == null || !"200".equals(mTmsGeocodingCoordinateInfoResponse.getResultCode())) {
				mTmsGeocodingCoordinateInfoResponse.setResultCode("5001");
				return gson.toJson(mTmsGeocodingCoordinateInfoResponse);
			}
					
		} catch (Exception e) {
			mTmsGeocodingCoordinateInfoResponse.setResultCode("5001");
			mTmsGeocodingCoordinateInfoResponse.setResultMessage(e.toString());
			return gson.toJson(e.toString());
		}
		return gson.toJson(mTmsGeocodingCoordinateInfoResponse);
	}

	
	@ApiOperation(value = "allocation", notes = "TMS 배차요청")
	@RequestMapping(value="/allocation",method=RequestMethod.GET)
	public String tms_allocation(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="allocationType", required=true, defaultValue = "1") String allocationType,
			@RequestParam(name="orderIdList", required=false) String orderIdList,
			@RequestParam(name="vehicleIdList", required=false)String vehicleIdList,
			@RequestParam(name="startTime", required=true,defaultValue = "0900")String startTime) { 
		
		String baseUrl = "https://apis.openapi.sk.com/tms/allocation";
		baseUrl += "?appKey=" + appKey;
		baseUrl += "&allocationType=" + allocationType;
		if (allocationType.equals("2")) {
			baseUrl += "&orderIdList=" + orderIdList;
			baseUrl += "&vehicleIdList=" + vehicleIdList;
		}
		baseUrl += "&startTime=" + startTime;
		
		System.out.println(baseUrl);
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				allocationResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		System.out.println(allocationResponse.toString());
		
		if (allocationResponse == null || !allocationResponse.getResultCode().equals("200")) {
			return gson.toJson(allocationResponse);
		}		
		
		baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/allocationData";
		baseUrl += "?appKey=" + appKey;
		baseUrl += "&mappingKey=" + allocationResponse.getMappingKey();
		baseUrl += "&routeYn=" + "Y";
        
		System.out.println(baseUrl);
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmpTmsAllocationDataResponse = gson.fromJson(result, TmsAllocationDataResponse.class);
			}
		});
		
		System.out.println(gson.toJson(tmpTmsAllocationDataResponse));
		
		return gson.toJson(tmpTmsAllocationDataResponse);
	}
	
	TmsAllocationDataResponse tmsAllocationDataResponse = new TmsAllocationDataResponse();
	
	@ApiOperation(value = "allocationData", notes = "TMS 배차 결과 요청")
	@RequestMapping(value="/allocationData",method=RequestMethod.GET)
	public String tms_allocationData(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="mappingKey", required=true) String mappingKey,
			@RequestParam(name="routeYn", required=false, defaultValue = "N") String routeYn
			) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/allocationData";
		baseUrl += "?appKey=" + appKey;
		baseUrl += "&mappingKey=" + mappingKey;
		baseUrl += "&routeYn=" + routeYn;
		
		System.out.println("mappingKey=" + mappingKey);
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsAllocationDataResponse = gson.fromJson(result, TmsAllocationDataResponse.class);
			}
		});
		
// tmserp_AllocationUpdate 으로 이동		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("k_sti_cd", "YA123");
//		params.put("rem_dt", "20201103");
//		ArrayList<TMSERPOrderList> arErpOrderList = tmserpScheduling.selectOrderList(params);
//		System.out.println("ERP 배송지수:" + arErpOrderList.size());
//		
//		//apiTmsErpController.erp_updateSticd(sti_cd, com_ctsec, com_scd, rem_dt, rem_seq, plm_no, orm_no, sti_nm, com_ssec)
//
////				String sti_cd = "";
////				String com_ctsec = "";
////				String com_scd = "";
////				String rem_dt = "";
////				String rem_seq = "";	
////				String plm_no = "";
////				String orm_no = "";
////				String sti_nm = "";
////				String com_ssec = "";
//				
//		//AsResultResponse response = new AsResultResponse();
//		String res = "";
//		int count = 0;
//		for(int i=0; i<tmsAllocationDataResponse.getVehicleList().size(); i++ ) {
//			String vehicleid = tmsAllocationDataResponse.getVehicleList().get(i).getVehicleId();
//			params = new HashMap<String, Object>();
//			params.put("sti_cd", vehicleid);
//			params.put("com_scd", "C16YA");
//			DataResult com =  tmserpScheduling.selectComCtsec(params);
//			if (com == null) {
//				
//			}			
//			
//			String com_ctsec = com.getData1();
//			System.out.println("sti_cd=" + vehicleid +", com_ctsec=" + com_ctsec);
//			
//			for(int j=0; j<tmsAllocationDataResponse.getVehicleList().get(i).getOrderList().size(); j++) {				
//				String orderid = tmsAllocationDataResponse.getVehicleList().get(i).getOrderList().get(j).getOrderId();
//				for(int k=0; k<arErpOrderList.size(); k++) {
//					if (orderid.equals(arErpOrderList.get(k).getOrderId())) {
//						 res = apiTmsErpController.erp_updateSticd(
//								vehicleid,
//								com_ctsec,
//								arErpOrderList.get(k).getCom_scd(),
//								arErpOrderList.get(k).getRem_dt(),
//								arErpOrderList.get(k).getRem_seq(),
//								arErpOrderList.get(k).getPlm_no(),
//								arErpOrderList.get(k).getOrm_no(),
//								arErpOrderList.get(k).getSti_nm(),
//								arErpOrderList.get(k).getCom_ssec());
//						 
//						 System.out.println("erp_updateSticd=" + res);
//						 count++;
//					}
//				}
//			}
//		}
//		
//		System.out.println("erp_updateSticd counted=" + count);
		
		System.out.println(gson.toJson(tmsAllocationDataResponse));
		return gson.toJson(tmsAllocationDataResponse);
	}
	
	TmsCenterListResponse tmsCenterListResponse = null;
	
	@ApiOperation(value = "centerList", notes = "TMS 센터목록 조회")
	@RequestMapping(value="/centerList",method=RequestMethod.GET)
	public String tms_centerList(String appKey) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/centerList";
		baseUrl += "?appKey=" + appKey;
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsCenterListResponse = gson.fromJson(result, TmsCenterListResponse.class);
			}
		});
		return gson.toJson(tmsCenterListResponse);
	}
	
	TmsVehicleListResponse tmsVehicleListResponse = null;
	
	@ApiOperation(value = "vehicleList", notes = "TMS 차량목록 조회")
	@RequestMapping(value="/vehicleList",method=RequestMethod.GET)
	public String tms_vehicleList(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey
			) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/vehicleList";
		baseUrl += "?appKey=" + appKey; //TMapInfo.appKey;
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsVehicleListResponse = gson.fromJson(result, TmsVehicleListResponse.class);
			}
		});
		return gson.toJson(tmsVehicleListResponse);
	}
	

	BaseResponse vehicleListInsertResponse = new BaseResponse();
	BaseResponse tmpVehicleListUpdateFromERPResponse = new BaseResponse();
	ArrayList<ERPSti> stiList = new ArrayList<ERPSti>();
	@ApiOperation(value = "vehicleListUpdateFromERP", notes = "TMS 차량 여러건 추가")
	@RequestMapping(value="/vehicleListUpdateFromERP",method=RequestMethod.POST)
	public String tms_vehicleListUpdateFromERP() throws UnsupportedEncodingException { 
		String baseUrl = ServerInfo.SERVER_URL + "/v1/api/erp/stiList";
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				stiList = gson.fromJson(result, new TypeToken<List<ERPSti>>(){}.getType());
			}
		});
		
		baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/vehicleDelete";
		baseUrl += "?deleteFlag=" + "1";
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmpVehicleListUpdateFromERPResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		if (!tmpVehicleListUpdateFromERPResponse.getResultCode().equals("200")) {
			return gson.toJson(tmpVehicleListUpdateFromERPResponse);
		}
		
		ArrayList<TmsVehicleBase> allItems = new ArrayList<TmsVehicleBase>();
		
		for (int i = 0 ; i < stiList.size() ; i++) {
			TmsVehicleBase item = new TmsVehicleBase(stiList.get(i).getSti_cd(),URLEncoder.encode(stiList.get(i).getSti_nm(),"UTF-8"),"1","01","");
			allItems.add(item);
		}
		
		baseUrl = "https://apis.openapi.sk.com/tms/vehicleListInsert";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reqDatas", gson.toJson(allItems));
		
		RestService.post(baseUrl, params.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				vehicleListInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(vehicleListInsertResponse);
	}

	@ApiOperation(value = "vehicleListInsert", notes = "TMS 차량 여러건 추가")
	@RequestMapping(value="/vehicleListInsert",method=RequestMethod.POST)
	public String tms_vehicleListInsert(String appKey, ArrayList<TMSERPVehicleList> arErpVehicleList) throws UnsupportedEncodingException {
		
		for (int i = 0 ; i < arErpVehicleList.size() ; i++) {
			arErpVehicleList.get(i).setVehicleName(URLEncoder.encode(arErpVehicleList.get(i).getVehicleName(),"UTF-8"));
		}
		
		String baseUrl = "https://apis.openapi.sk.com/tms/vehicleListInsert";
		baseUrl += "?appKey=" + appKey;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reqDatas", gson.toJson(arErpVehicleList));
		
		RestService.post(baseUrl, params.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				vehicleListInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(vehicleListInsertResponse);
	}

	BaseResponse vehicleDeleteResponse = new BaseResponse();
	
	@ApiOperation(value = "vehicleDelete", notes = "TMS 차량 삭제")
	@RequestMapping(value="/vehicleDelete",method=RequestMethod.GET)
	public String tms_vehicleDelete(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="deleteFlag", required=true, defaultValue = "1") String deleteFlag,
			@RequestParam(name="vehicleId", required=false) String vehicleId) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/vehicleDelete";
		baseUrl += "?appKey=" + appKey; //TMapInfo.appKey;		
		baseUrl += "&deleteFlag=" + deleteFlag;
		
		if (vehicleId != null && !vehicleId.isEmpty()) {
			baseUrl += "&vehicleId=" + vehicleId;
		}
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				vehicleDeleteResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(vehicleDeleteResponse);
	}
	
	BaseResponse vehicleInsertResponse = new BaseResponse();
	
	@ApiOperation(value = "vehicleInsert", notes = "TMS 등록 삭제")
	@RequestMapping(value="/vehicleInsert",method=RequestMethod.GET)
	public String tms_vehicleInsert(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="vehicleId", required=true, defaultValue = "C16YAYA142") String vehicleId,
			@RequestParam(name="vehicleName", required=true, defaultValue = "HA01정득수팀") String vehicleName,
			@RequestParam(name="weight", required=true, defaultValue = "10") String weight,
			@RequestParam(name="vehicleType", required=false) String vehicleType,
			@RequestParam(name="zoneCode", required=false) String zoneCode) throws UnsupportedEncodingException { 
		String baseUrl = "https://apis.openapi.sk.com/tms/vehicleInsert";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		baseUrl += "&vehicleId=" + vehicleId + "&vehicleName=" + URLEncoder.encode(vehicleName, "UTF-8") + "&weight=" + weight;
		
		if (zoneCode != null && !zoneCode.isEmpty()) {
			baseUrl += "&zoneCode=" + zoneCode;
		}
		
		if (vehicleType != null && !vehicleType.isEmpty()) {
			baseUrl += "&vehicleType=" + vehicleType;
		}
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				vehicleInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(vehicleInsertResponse);
	}
	
	TmsOrderResponse tmsOrderResponse = null;
	
	@ApiOperation(value = "orderList", notes = "TMS 배송지 목록 조회")
	@RequestMapping(value="/orderList",method=RequestMethod.GET)
	public String tms_orderList(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey
			) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/orderList";
		baseUrl += "?appKey=" + appKey; //TMapInfo.appKey;
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsOrderResponse = gson.fromJson(result, TmsOrderResponse.class);
			}
		});
		return gson.toJson(tmsOrderResponse);
	}
	
	BaseResponse orderInsertResponse = null;
	
	@ApiOperation(value = "orderInsert", notes = "TMS 배송지 추가")
	@RequestMapping(value="/orderInsert",method=RequestMethod.GET)
	public String tms_orderInsert(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="orderId", required=true) String orderId,
			@RequestParam(name="orderName", required=false) String orderName,
			@RequestParam(name="address", required=false) String address,
			@RequestParam(name="latitude", required=true) String latitude,
			@RequestParam(name="longitude", required=true) String longitude,
			@RequestParam(name="vehicleType", required=false) String vehicleType,
			@RequestParam(name="serviceTime", required=false) String serviceTime,
			@RequestParam(name="zoneCode", required=false) String zoneCode,
			@RequestParam(name="deliveryWeight", required=true) String deliveryWeight) throws UnsupportedEncodingException { 
		String baseUrl = "https://apis.openapi.sk.com/tms/orderInsert";
		baseUrl += "?appKey=" + appKey;
		baseUrl += "&orderId=" + orderId + "&latitude=" + latitude + "&longitude=" + longitude + "&deliveryWeight=" + deliveryWeight;
		
		if (orderName != null && !orderName.isEmpty()) {
			baseUrl += "&orderName=" + URLEncoder.encode(orderName, "UTF-8");
		}
		
		if (address != null && !address.isEmpty()) {
			baseUrl += "&address=" + URLEncoder.encode(address, "UTF-8");
		}
		
		if (vehicleType != null && !vehicleType.isEmpty()) {
			baseUrl += "&vehicleType=" + vehicleType;
		}
		
		if (serviceTime != null && !serviceTime.isEmpty()) {
			baseUrl += "&serviceTime=" + serviceTime;
		}
		
		if (zoneCode != null && !zoneCode.isEmpty()) {
			baseUrl += "&zoneCode=" + zoneCode;
		}
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				orderInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(orderInsertResponse);
	}
	
	BaseResponse erporderListInsertResponse = new BaseResponse();
	ArrayList<TmsOrderBase> orderListAllIItems = new ArrayList<TmsOrderBase>();
	BaseResponse tmpOrderListDeleteResponse = new BaseResponse();
	
	@ApiOperation(value = "orderListUpdateFromErp", notes = "TMS 배송지를 ERP에서 가져와 TMS에 추가함.")
	@RequestMapping(value="/orderListUpdateFromErp",method=RequestMethod.POST)
	public String tms_orderListUpdateFromErp() throws UnsupportedEncodingException { 
		String baseUrl = ServerInfo.SERVER_URL + "/v1/api/erp/ermList";
        
		System.out.println(baseUrl);
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				orderListAllIItems = gson.fromJson(result, new TypeToken<List<TmsOrderBase>>(){}.getType());
			}
		});
		
		baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/orderDelete";
		baseUrl += "?deleteFlag=" + "1";
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmpOrderListDeleteResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		if (tmpOrderListDeleteResponse == null || !tmpOrderListDeleteResponse.getResultCode().equals("200")) {
			
		}
	
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reqDatas", gson.toJson(orderListAllIItems));
		
		System.out.println( params.toString());
		
		baseUrl = "https://apis.openapi.sk.com/tms/orderListInsert";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		RestService.post(baseUrl, params.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				erporderListInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		System.out.println(erporderListInsertResponse.toString());
		return gson.toJson(erporderListInsertResponse);
	}
	
	BaseResponse orderDeleteResponse = null;
	
	@ApiOperation(value = "orderDelete", notes = "TMS 배송지  삭제")
	@RequestMapping(value="/orderDelete",method=RequestMethod.GET)
	public String tms_orderDelete(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey,
			@RequestParam(name="deleteFlag", required=true, defaultValue = "2") String deleteFlag,
			@RequestParam(name="orderId", required=false) String orderId) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/orderDelete";
		baseUrl += "?appKey=" + appKey; //TMapInfo.appKey ;
		baseUrl += "&deleteFlag=" + deleteFlag;
		
		if (orderId != null && !orderId.isEmpty()) {
			baseUrl += "&orderId=" + orderId;
		}
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				centerDeleteResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(centerDeleteResponse);
	}
	
	BaseResponse centerDeleteResponse = null;
	
	@ApiOperation(value = "centerDelete", notes = "TMS 센터  삭제")
	@RequestMapping(value="/centerDelete",method=RequestMethod.GET)
	public String tms_centerDelete(@RequestParam(name="centerId", required=true) String centerId) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/centerDelete";
		baseUrl += "?appKey=" + TMapInfo.appKey + "&centerId=" + centerId;
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				centerDeleteResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(centerDeleteResponse);
	}
	
	@ApiOperation(value = "allCenterDelete", notes = "TMS 센터 전체  삭제")
	@RequestMapping(value="/allCenterDelete",method=RequestMethod.GET)
	public String tms_allCenterDelete(String appKey) { 
		
		TmsCenterListResponse tmsCenterListResponse = gson.fromJson(tms_centerList(appKey),TmsCenterListResponse.class);
		
		for (int i = 0 ; i < tmsCenterListResponse.getResultData().size() ; i++) {
			String centerId = tmsCenterListResponse.getResultData().get(i).getCenterId();
			
			tms_centerDelete(centerId);
		}
		
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setResultCode("200");
		
		return gson.toJson(baseResponse);
	}
	
	TmsZoneListResponse tmsZoneListResponse = new TmsZoneListResponse();
	
	@ApiOperation(value = "zoneList", notes = "TMS 권역 목록조회")
	@RequestMapping(value="/zoneList",method=RequestMethod.GET)
	public String tms_zoneList(
			@RequestParam(name="appKey", required=true, defaultValue = "l7xx965cfaee1f4c47608284f1271eccb662") String appKey
			) { 
		String baseUrl = "https://apis.openapi.sk.com/tms/zoneList";
		baseUrl += "?appKey=" + appKey;
	
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsZoneListResponse = gson.fromJson(result, TmsZoneListResponse.class);
			}
		});
		return gson.toJson(tmsZoneListResponse);
	}
	
	BaseResponse zoneInsertResponse = null;
	
	@ApiOperation(value = "zoneInsert", notes = "TMS 권역 추가")
	@RequestMapping(value="/zoneInsert",method=RequestMethod.GET)
	public String tms_zoneInsert(
			@RequestParam(name="code", required=true) String code,
			@RequestParam(name="name", required=true) String name) throws UnsupportedEncodingException { 
		String baseUrl = "https://apis.openapi.sk.com/tms/zoneInsert";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		baseUrl += "&code=" + code;
		baseUrl += "&name=" + URLEncoder.encode(name, "UTF-8");
	
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				zoneInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(zoneInsertResponse);
	}
	
	BaseResponse zoneListInsertResponse = null;
	ArrayList<TmsZoneBase> tmsZoneBaseList = new ArrayList<TmsZoneBase>();
	@ApiOperation(value = "zoneListUpdateFromErp", notes = "TMS 권역 erp에서 가져와 TMS에 업데이트")
	@RequestMapping(value = "/zoneListUpdateFromErp", method = RequestMethod.POST)
	public String tms_zoneListUpdateFromErp() throws UnsupportedEncodingException { 
		String baseUrl = ServerInfo.SERVER_URL + "/v1/api/erp/zoneList";
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsZoneBaseList = gson.fromJson(result, new TypeToken<List<TmsZoneBase>>(){}.getType());
			}
		});
		
		baseUrl = "https://apis.openapi.sk.com/tms/zoneListInsert";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("reqDatas", gson.toJson(tmsZoneBaseList));
		
		RestService.post(baseUrl,params.toString(), new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				zoneListInsertResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(zoneListInsertResponse);
	}
	
	BaseResponse zoneDeleteResponse = null;
	
	@ApiOperation(value = "zoneDelete", notes = "TMS 권역 삭제")
	@RequestMapping(value="/zoneDelete",method=RequestMethod.GET)
	public String tms_zoneDelete(
			@RequestParam(name="code", required=true) String code){ 
		String baseUrl = "https://apis.openapi.sk.com/tms/zoneDelete";
		baseUrl += "?appKey=" + TMapInfo.appKey;
		baseUrl += "&code=" + code;
	
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				zoneDeleteResponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		return gson.toJson(zoneDeleteResponse);
	}
	
	TmsZoneListResponse tmpTmsZoneListResponse = new TmsZoneListResponse();
	BaseResponse tmpZoneDeleteResponse = new BaseResponse();
	
	@ApiOperation(value = "zoneDeleteAll", notes = "TMS 모든 권역 삭제")
	@RequestMapping(value="/zoneDeleteAll",method=RequestMethod.GET)
	public String tms_zoneDeleteAll() { 
		
		String baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/zoneList";
		
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmpTmsZoneListResponse = gson.fromJson(result, TmsZoneListResponse.class);
			}
		});
		
		for (int i = 0 ; i < tmpTmsZoneListResponse.getResultData().size() ; i++) {
			baseUrl = "https://apis.openapi.sk.com/tms/zoneDelete";
			baseUrl += "?appKey=" + TMapInfo.appKey;
			baseUrl += "&code=" + tmpTmsZoneListResponse.getResultData().get(i).getCode();
		
			RestService.get(baseUrl, new RestServiceCallBack() {
				@Override
				public void onResult(String result) {
					System.out.println("result : " + result);
				}
			});
		}
		
		tmpZoneDeleteResponse.setResultCode("200");
		
		return gson.toJson(tmpZoneDeleteResponse);
	}
}
