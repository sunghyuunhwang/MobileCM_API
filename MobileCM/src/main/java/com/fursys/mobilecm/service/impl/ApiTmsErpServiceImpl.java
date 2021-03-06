package com.fursys.mobilecm.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fursys.mobilecm.config.ServerInfo;
import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.controllers.ApiTmsController;
import com.fursys.mobilecm.controllers.ApiTmsErpController;
import com.fursys.mobilecm.lib.MobileCMLib;
import com.fursys.mobilecm.mapper.TMSERPSchedulingMapper;
import com.fursys.mobilecm.service.ApiTmsErpService;
import com.fursys.mobilecm.utils.StringUtil;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.tms.TmsRouteOptimization;
import com.fursys.mobilecm.vo.tms.TmsViaPoints;
import com.fursys.mobilecm.vo.tms.reponse.TmsAllocationDataResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsGeocodingCoordinateInfoResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsRouteOptimizationResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsVehicleListResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsZoneListResponse;
import com.fursys.mobilecm.vo.tmserp.TMSERPCenterList;
import com.fursys.mobilecm.vo.tmserp.TMSERPOrderList;
import com.fursys.mobilecm.vo.tmserp.TMSERPVehicleList;
import com.google.gson.Gson;

@Service
public class ApiTmsErpServiceImpl  implements ApiTmsErpService {
	@Autowired private TMSERPSchedulingMapper tmserpScheduling; 

	@Autowired private ApiTmsController mApiTmsController;
	@Autowired private ApiTmsErpController apiTmsErpController;
	@Autowired private PlatformTransactionManager txManager;

	Gson gson = new Gson();

	@Override
	public BaseResponse tmserp_RouteSequential(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
				
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");			
			String as_com_scd = (String) param.get("com_scd");
			String as_rem_dt = (String) param.get("rem_dt");
			String as_sti_cd = (String) param.get("sti_cd");

			params = new HashMap<String, Object>();
			params.put("rem_dt", as_rem_dt);	    	
			params.put("sti_cd", as_sti_cd);
			params.put("com_scd", as_com_scd);
			
			//???????????????
			ArrayList<TmsViaPoints> arrList = tmserpScheduling.selectRouteOptimizationList(params);
			int cnt = arrList.size();
			if (cnt < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ?????? ??????AS?????? ????????????.");
				return response;
			}

			if (cnt < 3) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ????????? ?????? 3??? ??????????????? ?????????.");
				return response;
			}
			
			for (int i=0; i<cnt; i++) {
				if ("".equals(arrList.get(i).getViaDetailAddress())) {
					
				}
				
	            TmsGeocodingCoordinateInfoResponse geocoding = new TmsGeocodingCoordinateInfoResponse();
	            if ("".equals(arrList.get(i).getViaX()) || "".equals(arrList.get(i).getViaY())) {
	            	geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_NEW, arrList.get(i).getViaDetailAddress()), TmsGeocodingCoordinateInfoResponse.class);	            	            	
	            	if (!"200".equals(geocoding.getResultCode())) {
	            		if ("Bad Request".equals(geocoding.getResultMessage())) {
	            			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_OLD, arrList.get(i).getViaDetailAddress()), TmsGeocodingCoordinateInfoResponse.class);
	            			if (!"200".equals(geocoding.getResultCode())) {
	            				txManager.rollback(status);
	            				System.out.println(geocoding.getResultMessage());        			
	    	        			response.setResultCode("5001");
	    	        			response.setResultMessage(geocoding.getResultMessage());
	    	        			return response;
	            			} else {
	            				if (geocoding.getCoordinateInfo().getTotalCount() > 0) {
	            					arrList.get(i).setViaX(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());	
	            					arrList.get(i).setViaY(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
	        	        		}
	            			}
	            		} else {
	            			txManager.rollback(status);
		        			System.out.println(geocoding.getResultMessage());        			
		        			response.setResultCode("5001");
		        			response.setResultMessage(geocoding.getResultMessage());
		        			return response;
	            		}
	            	} else {            	
		        		if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
		        			arrList.get(i).setViaX(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
		        			arrList.get(i).setViaY(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
		        		}
	            	}
	            }
			}

			TmsRouteOptimization route = new TmsRouteOptimization();
			route.setStartName(arrList.get(0).getViaPointName());			
			route.setStartX(arrList.get(0).getViaX());
			route.setStartY(arrList.get(0).getViaY());
			route.setStartTime(arrList.get(0).getWishStartTime());
			
			route.setEndName(arrList.get(cnt - 1).getViaPointName());
			route.setEndX(arrList.get(cnt - 1).getViaX());
			route.setEndY(arrList.get(cnt - 1).getViaY());
			
			ArrayList<TmsViaPoints> viaPoints = new ArrayList<TmsViaPoints>();
			for (int i=1; i<(cnt - 1); i++) {
				arrList.get(i).setViaPointName(arrList.get(i).getViaPointName());
				arrList.get(i).setViaDetailAddress("");
				arrList.get(i).setWishStartTime("");
				viaPoints.add(arrList.get(i));
			}
			
			route.setViaPoints(viaPoints);
			
			//System.out.println(gson.toJson(route));
			
			TmsRouteOptimizationResponse route_response = gson.fromJson(mApiTmsController.tms_routeSequential(as_appKey, route), TmsRouteOptimizationResponse.class);
			if (!"200".equals(route_response.getResultCode())) {
				System.out.println(route_response.getResultMessage());        			
				response.setResultCode("5001");
				response.setResultMessage(MobileCMLib.makeTmsMsg(route_response));
				return response;
			}
						
			//System.out.println(gson.toJson(route_response));
			
    		response.setResultCode("200");
    		response.setResultMessage(route_response.getResultMessage());

		} catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		return response;

	}
	
	@Override
	public BaseResponse tmserp_RouteOptimization(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
				
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");			
			String as_com_scd = (String) param.get("com_scd");
			String as_rem_dt = (String) param.get("rem_dt");
			String as_sti_cd = (String) param.get("sti_cd");

			params = new HashMap<String, Object>();
			params.put("rem_dt", as_rem_dt);	    	
			params.put("sti_cd", as_sti_cd);
			params.put("com_scd", as_com_scd);
			
			DataResult center =  tmserpScheduling.getCenterAddr(params);
			if (center == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("?????? ??????????????? ????????????.");
				return response;
			}
			String center_name = "", center_addr = "", center_time = "", center_X = "", center_Y = "";
			center_name = center.getData1();
			center_addr = center.getData2();
			center_time = center.getData3();
			TmsGeocodingCoordinateInfoResponse geocoding = apiTmsErpController.Geocoding(as_appKey, center_addr);
            if (!"200".equals(geocoding.getResultCode())) {
            	txManager.rollback(status);
				System.out.println(geocoding.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(geocoding.getResultMessage());
    			return response;
            }
            if (geocoding.getCoordinateInfo().getTotalCount() > 0) {
            	center_X = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon();
            	center_Y = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat();
    		}
			
			//???????????????
			ArrayList<TmsViaPoints> arrList = tmserpScheduling.selectRouteOptimizationList(params);
			int cnt = arrList.size();
			if (cnt < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ?????? ??????AS?????? ????????????.");
				return response;
			}

			if (cnt < 3) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ????????? ?????? 3??? ??????????????? ?????????.");
				return response;
			}
			
			StringBuffer pointname_arr = new StringBuffer();
			for (int i=0; i<cnt; i++) {
				if ("C142".equals(arrList.get(i).getCom_rfg())) {
					if ("".equals(pointname_arr.toString())) {
						pointname_arr.append(arrList.get(i).getViaPointName());
					} else {
						pointname_arr.append("," + arrList.get(i).getViaPointName());
					}
				}
			}
			if (!"".equals(pointname_arr.toString())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(pointname_arr.toString() + "?????? ??????????????? ?????????. ???????????? ????????? ???????????????.");
				return response;
			}

			for (int i=0; i<cnt; i++) {
	            geocoding = new TmsGeocodingCoordinateInfoResponse();	            
	            geocoding = apiTmsErpController.Geocoding(as_appKey, arrList.get(i).getViaDetailAddress());
	            if (!"200".equals(geocoding.getResultCode())) {
	            	txManager.rollback(status);
    				System.out.println(geocoding.getResultMessage());        			
        			response.setResultCode("5001");
        			response.setResultMessage(geocoding.getResultMessage());
        			return response;
	            }
	            if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
        			arrList.get(i).setViaX(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
        			arrList.get(i).setViaY(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
        		}	            
			}

			TmsRouteOptimization route = new TmsRouteOptimization();
			route.setStartName(center_name);
			route.setStartX(center_X);
			route.setStartY(center_Y);
			route.setStartTime(center_time);
			
			route.setEndName(arrList.get(cnt - 1).getViaPointName());
			route.setEndX(arrList.get(cnt - 1).getViaX());
			route.setEndY(arrList.get(cnt - 1).getViaY());
			
			ArrayList<TmsViaPoints> viaPoints = new ArrayList<TmsViaPoints>();
			for (int i=0; i<(cnt - 1); i++) {
				arrList.get(i).setViaPointName(arrList.get(i).getViaPointName());
				arrList.get(i).setViaDetailAddress("");
				arrList.get(i).setWishStartTime("");
				viaPoints.add(arrList.get(i));
			}
			
			route.setViaPoints(viaPoints);
			
			System.out.println(gson.toJson(route));
			
			TmsRouteOptimizationResponse route_response = gson.fromJson(mApiTmsController.tms_routeOptimization(as_appKey, route), TmsRouteOptimizationResponse.class);
			if (!"200".equals(route_response.getResultCode())) {
				System.out.println(route_response.getResultMessage());        			
				response.setResultCode("5001");
				response.setResultMessage(MobileCMLib.makeTmsMsg(route_response));
				return response;
			}
			StringBuffer rem_dt_arr = new StringBuffer();
			StringBuffer rem_seq_arr = new StringBuffer();
			StringBuffer arrival_arr = new StringBuffer();
			
			int seq = 0;
			for(int i=1; i<(route_response.getFeatures().size() - 2); i++) {
				if (!"Point".equals(route_response.getFeatures().get(i).getGeometry().getType())) continue;
				rem_dt_arr.append(as_rem_dt + ",");
				rem_seq_arr.append(route_response.getFeatures().get(i).getProperties().getViaPointId() + ",");
				arrival_arr.append(++seq + ",");
			}
			
			rem_dt_arr.append(as_rem_dt + ",");
			rem_seq_arr.append(arrList.get(cnt - 1).getViaPointId() + ",");
			arrival_arr.append(++seq + ",");
			
			//System.out.println("rem_dt_arr=" + rem_dt_arr);
			//System.out.println("rem_seq_arr=" + rem_seq_arr);
			//System.out.println("arrival_arr=" + arrival_arr);
			
            params = new HashMap<String, Object>();
			params.put("rem_dt_arr", rem_dt_arr.toString());
			params.put("rem_seq_arr", rem_seq_arr.toString());
			params.put("arrival_arr", arrival_arr.toString());
			int res = tmserpScheduling.updateRouteOptimization(params);
			
    		response.setResultCode("200");
    		response.setResultMessage(route_response.getResultMessage());

		} catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		return response;

	}
	
	@Override
	public BaseResponse tmserp_AllocationUpdate(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params, result_parmas;
		
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_mappingKey = (String) param.get("mappingKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_sti_cd_area_arr = (String) param.get("sti_cd_area_arr");
			String as_rem_dt = (String) param.get("rem_dt");
			String as_usr_cd = (String) param.get("usr_cd");
						
			System.out.println("as_mappingKey :" + as_mappingKey);
			
			//???????????? 
			TmsAllocationDataResponse tmsAllocationDataResponse = gson.fromJson(mApiTmsController.tms_allocationData(as_appKey, as_mappingKey, "N"), TmsAllocationDataResponse.class);			
			if ("".equals(tmsAllocationDataResponse.getVehicleCount()) || tmsAllocationDataResponse.getVehicleList() == null) {
				txManager.rollback(status);
    			System.out.println("????????? ??????????????? ????????????");
    			response.setResultCode("4001");
    			response.setResultMessage("????????? ??????????????? ????????????");
    			return response;
			}			
			System.out.println("????????? ?????????:" + tmsAllocationDataResponse.getVehicleCount());

			params = new HashMap<String, Object>();
			params.put("k_sti_cd", as_k_sti_cd);
			params.put("sti_cd_arr", as_sti_cd_area_arr);
			params.put("rem_dt", as_rem_dt);
			ArrayList<TMSERPOrderList> arErpOrderList = tmserpScheduling.selectOrderList(params);
			System.out.println("ERP ????????????:" + arErpOrderList.size());
			
			String com_scd = "C16YA", sti_cd = "", delimiter = ",";
			String res_str = "";
			int res = 0, count = 0;
						
			for(int i=0; i<tmsAllocationDataResponse.getVehicleList().size(); i++ ) {
				String vehicleid = tmsAllocationDataResponse.getVehicleList().get(i).getVehicleId();
				sti_cd = vehicleid;

				result_parmas = new HashMap<String, Object>();
				result_parmas.put("com_scd", com_scd);
				result_parmas.put("rem_dt", as_rem_dt);
				result_parmas.put("sti_cd", sti_cd);
				result_parmas.put("usr_cd", as_usr_cd);
				result_parmas.put("total_km", tmsAllocationDataResponse.getVehicleList().get(i).getDeliveryDistance());				
				
				res = tmserpScheduling.deleteAllocationResultDetail(result_parmas);
				res = tmserpScheduling.deleteAllocationResult(result_parmas);				
				res = tmserpScheduling.insertAllocationResult(result_parmas);
				
				params = new HashMap<String, Object>();
				params.put("sti_cd", vehicleid);
				params.put("com_scd", com_scd);
				DataResult com =  tmserpScheduling.selectComCtsec(params);
				if (com == null) {					
				}
				String com_ctsec = com.getData1();
				//System.out.println("sti_cd=" + vehicleid +", com_ctsec=" + com_ctsec);
				
				for(int j=0; j<tmsAllocationDataResponse.getVehicleList().get(i).getOrderList().size(); j++) {				
					String seq_no = ""+(j+1);
					String orderid = tmsAllocationDataResponse.getVehicleList().get(i).getOrderList().get(j).getOrderId();
					result_parmas.put("seq_no", seq_no);
					result_parmas.put("orm_no", orderid);
					res = tmserpScheduling.insertAllocationResultDetail(result_parmas);
					int check = 0;
					for(int k=0; k<arErpOrderList.size(); k++) {
						if (orderid.equals(arErpOrderList.get(k).getOrderId())) {
							check++;
//							StringBuffer buff = new StringBuffer();
//							buff.append("erp_updateSticd vehicleid:"+vehicleid);
//							buff.append(", com_ctsec:"+com_ctsec);
//							buff.append(", Com_scd:"+arErpOrderList.get(k).getCom_scd());
//							buff.append(", Rem_dt:"+arErpOrderList.get(k).getRem_dt());
//							buff.append(", Rem_seq:"+arErpOrderList.get(k).getRem_seq());
//							buff.append(", Plm_no:"+arErpOrderList.get(k).getPlm_no());
//							buff.append(", Orm_no:"+arErpOrderList.get(k).getOrm_no());
//							buff.append(", Sti_nm:"+arErpOrderList.get(k).getSti_nm());
//							buff.append(", Com_ssec:"+arErpOrderList.get(k).getCom_ssec());
//							buff.append(", seq_no:"+seq_no);							
//							System.out.println(buff.toString());							
							 res_str = apiTmsErpController.erp_updateSticd(
									as_usr_cd,
									vehicleid,
									com_ctsec,
									arErpOrderList.get(k).getCom_scd(),
									arErpOrderList.get(k).getRem_dt(),
									arErpOrderList.get(k).getRem_seq(),
									arErpOrderList.get(k).getPlm_no(),
									arErpOrderList.get(k).getOrm_no(),
									tmsAllocationDataResponse.getVehicleList().get(i).getVehicleName(),
									arErpOrderList.get(k).getCom_ssec(),									
									seq_no);
							 
							 //System.out.println("erp_updateSticd=" + res_str);
							 count++;
						}
					}
					if (check ==0) {
						System.out.println("====================================================");
						System.out.println("vehicleid=[" + vehicleid + "], orderid=[" + orderid + "], not found.");
						System.out.println("====================================================");
					}
				}
			}
			
			//System.out.println("erp_updateSticd counted=" + count);
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;
	}
	
	@Override
	public BaseResponse tmserp_UpdateOrderGeocoding(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_rem_dt = (String) param.get("rem_dt");
			
			//ERP????????? ?????????
			params = new HashMap<String, Object>();
			params.put("k_sti_cd", as_k_sti_cd);
			params.put("rem_dt", as_rem_dt);
			ArrayList<TMSERPOrderList> arErpOrderList = tmserpScheduling.selectOrderListGeocoding(params);
			//System.out.println("ERP ????????????:" + arErpOrderList.size());
						
			StringBuffer rem_dt_arr = new StringBuffer();
			StringBuffer rem_seq_arr = new StringBuffer();
			StringBuffer lat_arr = new StringBuffer();
			StringBuffer lon_arr = new StringBuffer();
			
            TmsGeocodingCoordinateInfoResponse geocoding = new TmsGeocodingCoordinateInfoResponse();            
            for(int i=0; i<arErpOrderList.size(); i++) {
            	geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_NEW, arErpOrderList.get(i).getAddress()), TmsGeocodingCoordinateInfoResponse.class);
            	            	
            	if (!"200".equals(geocoding.getResultCode())) {
            		if ("Bad Request".equals(geocoding.getResultMessage())) {
            			//System.out.println("???????????? ?????? ??????:" + arErpOrderList.get(i).getAddress());
            			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_OLD, arErpOrderList.get(i).getAddress()), TmsGeocodingCoordinateInfoResponse.class);
            			if (!"200".equals(geocoding.getResultCode())) {
            				//txManager.rollback(status);
            				//System.out.println(geocoding.getResultMessage());        			
    	        			//response.setResultCode("5001");
    	        			//response.setResultMessage(geocoding.getResultMessage());
    	        			//return response;
    	        			arErpOrderList.get(i).setLongitude("");	
    	        			arErpOrderList.get(i).setLatitude("");
            			} else {
            				if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
        	        			arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());	
        	        			arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
        	        			//System.out.println("Order new lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
        	        		}
            			}
            		} else {
            			//txManager.rollback(status);
	        			//System.out.println(geocoding.getResultMessage());        			
	        			//response.setResultCode("5001");
	        			//response.setResultMessage(geocoding.getResultMessage());
	        			//return response;
	        			arErpOrderList.get(i).setLongitude("");	
	        			arErpOrderList.get(i).setLatitude("");
            		}
            	} else {            	
	        		if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
	        			arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
	        			arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
	        			//System.out.println("Order lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
	        		}
            	}
            	
            	rem_dt_arr.append(arErpOrderList.get(i).getRem_dt() + ",");
            	rem_seq_arr.append(arErpOrderList.get(i).getRem_seq() + ",");
            	lat_arr.append(arErpOrderList.get(i).getLatitude() + ",");
            	lon_arr.append(arErpOrderList.get(i).getLongitude() + ",");
            }
            
            params = new HashMap<String, Object>();
			params.put("rem_dt_arr", rem_dt_arr.toString());
			params.put("rem_seq_arr", rem_seq_arr.toString());
			params.put("lat_arr", lat_arr.toString());
			params.put("lon_arr", lon_arr.toString());
			
			int res = tmserpScheduling.updateOrderGeocoding(params);			
			            
			if(res < 1) {
    			txManager.rollback(status);
    			//System.out.println(geocoding.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(geocoding.getResultMessage());
    			return response;				
			}

            response.setResultCount(""+res);
            response.setResultMessage(res + "?????? ????????? ????????? ?????????????????????.");
            //System.out.println("ERP ????????? Geocoding???:" + res);
            
		} catch (Exception e) {
			txManager.rollback(status);
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;

	}

	@Override
	public BaseResponse tmserp_AllocationConfirmSet(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		
		try {
			String as_usr_id = (String) param.get("usr_id");
			String as_rem_dt = (String) param.get("rem_dt");
			
			String as_com_scd_arr = (String) param.get("com_scd_arr");
			String as_sti_cd_arr = (String) param.get("sti_cd_arr");
			
			params = new HashMap<String, Object>();
			params.put("REM_DT", as_rem_dt);			
			params.put("COM_SCD_ARR", as_com_scd_arr);
			params.put("STI_CD_ARR", as_sti_cd_arr);
			
			List <Map> inputMap = tmserpScheduling.select_1(params);
			if (inputMap.size() < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("???????????? ????????? ??????????????? ???????????? ????????????.");
				return response;
			}

			List <Map> records              = null;
			
			String TcPlmRcsec 		= "";
			String comSprog = "";
			
			for(int i=0 ; i < inputMap.size() ; i++){        	
				Map mapMst = inputMap.get(i);									
				List <Map> dtlList = null;
	            
				//????????????=N, ????????????=N
    			if ("Y".equals(inputMap.get(i).get("SCHDIV_YN")) && "N".equals(inputMap.get(i).get("ALLFIXYN"))) {
					//???????????? ?????? ????????? ??????
					dtlList = tmserpScheduling.retrieveBeabunDataList(mapMst);
					
					for(int j = 0;j < dtlList.size();j++){
						
						Map dtlMap = dtlList.get(j);
						dtlMap.put("gv_userId", as_usr_id);
						
						TcPlmRcsec = "";
						comSprog = "";
											
						/*//???????????? ?????? ????????? ??????
						if(!"hyunjun_noh".equals(dtlMap.get("gv_userId")) && !"hyojong_yoo".equals(dtlMap.get("gv_userId")) && !"dolpung_na".equals(dtlMap.get("gv_userId"))){
							return;
						}
						//*/
	
						//????????????????????? ????????? ????????? ???????????? ??? + ??????????????? ?????? ???
						if("C141".equals(dtlMap.get("COM_RFG")) && "Y".equals(dtlMap.get("SCHDIV_YN")) && !"".equals(dtlMap.get("REM_FTM"))){
							
							records = null;
							
							//PLM_RCSEC ??? ??????
							records = tmserpScheduling.retrievesTcPlanmstGetPlmRcsec(dtlMap);
							
							if(records.size() > 0){
								TcPlmRcsec = (String) records.get(0).get("PLM_RCSEC");
							}
							
							//tc_resmst update
							tmserpScheduling.modyfyTcResmstComRfgC142_U(dtlMap);
							
							//tc_resdtl update
							tmserpScheduling.modyfyTcResdtlComRfgC142_U(dtlMap);
							
							if("C18C".equals(dtlMap.get("COM_SSEC"))){
								
								
								if(!"2".equals(TcPlmRcsec) && "Y".equals(dtlMap.get("REM_CASYN")) ){
									
									List <Map> taAstitemList = null;
									
									taAstitemList = tmserpScheduling.retrieveTaAstitemCount(dtlMap);
									
									if(taAstitemList.size() > 0){//??????????????????
										
										if(taAstitemList.get(0).get("CNT") == taAstitemList.get(0).get("A03001_CNT")){ //?????????????????????
											
											comSprog = "A17008";
										
										}else{
											
											comSprog = "A17007";
										}
									}
									
									dtlMap.put("COM_SPROG", comSprog);
									
									//ta_rptreq update
									tmserpScheduling.modyfyTaRptreqComSprog_U(dtlMap);
									
									/* ???????????? ???????????? why ?????? ??????????????? ?????? ?????? ???????????? ???????????? ??????????????????????????? 2006.04. 25 by ksl */	
									tmserpScheduling.modyfyTcToOrddetC090_U(dtlMap);
									tmserpScheduling.modyfyTrSchedule_U(dtlMap);
									
								}
								
								else if ("2".equals(TcPlmRcsec) && "N".equals(dtlMap.get("REM_CASYN")) ){
									
									//tc_planmst ????????????
									tmserpScheduling.modyfyTcPlanmstComPlmfgC102_U(dtlMap);
									
									//tc_plandtl ????????????
									tmserpScheduling.modyfyTcPlandtlComRdsecC13W_U(dtlMap);
									
									/* ???????????? ???????????? why ?????? ??????????????? ?????? ?????? ???????????? ???????????? ??????????????????????????? 2006.04. 25 by ksl */	
									
									tmserpScheduling.modyfyTcToOrddetC090_U(dtlMap);
									
									tmserpScheduling.modyfyTrSchedule_U(dtlMap);
								}
								else{
									
									//tc_planmst ????????????
									tmserpScheduling.modyfyTcPlanmstComPlmfgC102_U(dtlMap);
									//tc_plandtl ????????????
									tmserpScheduling.modyfyTcPlandtlComRdsecC13W_U(dtlMap);
									
									tmserpScheduling.modyfyTcToOrddetC090_U(dtlMap);
									tmserpScheduling.modyfyTrSchedule_U(dtlMap);
								}
								
							}
							//as??????
							else{
								//???????????????
								if ("2".equals(TcPlmRcsec) ){
									
									tmserpScheduling.modyfyTaPlanmstComPlmfgC102_U(dtlMap);
									
									tmserpScheduling.modyfyTaPlandtlComRdsecC13W_U(dtlMap);
									
								}
								//???????????? ?????????
								else{
									
									tmserpScheduling.modyfyTaPlanmstComPlmfgC102_2_U(dtlMap);
									
									tmserpScheduling.modyfyTaRptReqTrsY_U(dtlMap);
									
								}
							}
						}
					}
    			}

			}			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;
	}	
	
	@Override
	public BaseResponse tmserp_AllocationConfirm(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		
		try {
			String as_usr_id = (String) param.get("usr_id");
			String as_rem_dt = (String) param.get("rem_dt");
			
			String as_com_scd_arr = (String) param.get("com_scd_arr");
			String as_sti_cd_arr = (String) param.get("sti_cd_arr");
			
			params = new HashMap<String, Object>();
			params.put("REM_DT", as_rem_dt);			
			params.put("COM_SCD_ARR", as_com_scd_arr);
			params.put("STI_CD_ARR", as_sti_cd_arr);
			
			List <Map> inputMap = tmserpScheduling.select_1(params);
			if (inputMap.size() < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("???????????? ????????? ??????????????? ???????????? ????????????.");
				return response;
			}
			
			for(int i=0 ; i < inputMap.size() ; i++){
    			Map mapMst = inputMap.get(i);
    			List <Map> dtlList = null;
    			
    			//????????????=N, ????????????=N
    			if ("N".equals(inputMap.get(i).get("SCHDIV_YN")) && "N".equals(inputMap.get(i).get("ALLFIXYN"))) {
    				//???????????? ?????? ????????? ??????
    				dtlList = tmserpScheduling.retrieveBeabunDataList(mapMst);				
    				//System.out.println("dtlList Size=" + dtlList.size());
    				//System.out.println("dtlList=" + dtlList);
    				
    				for(int j = 0;j < dtlList.size();j++){
    					Map dtlMap = dtlList.get(j);
    					dtlMap.put("gv_userId", as_usr_id);

    					/*//???????????? ?????? ????????? ??????
    					if(!"hyunjun_noh".equals(dtlMap.get("gv_userId")) && !"hyojong_yoo".equals(dtlMap.get("gv_userId")) && !"dolpung_na".equals(dtlMap.get("gv_userId"))){
    						return;
    					}
    					//*/

    					//2018.08.16 JAVA??? ??????????????? ???????????? ????????? ??????????????? ??????
    					List <Map> records= tmserpScheduling.packagebunbaeproc(dtlMap);    					
    					//System.out.println("records=" + records);
    				}    				
    			}
    		}

		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;
	}
	
	@Override
	public BaseResponse tmserp_Allocation(HashMap<String, Object> param) {
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
				
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_rem_dt = (String) param.get("rem_dt");
			String as_start_time = (String) param.get("start_time");
			String as_sti_cd_area_arr = (String) param.get("sti_cd_area_arr");
			String as_sti_cd_arr = (String) param.get("sti_cd_arr");
			
			//????????????
			BaseResponse center_response = new BaseResponse();
			params = new HashMap<String, Object>();
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
			center_response = tmserp_UpdateCenter(params);
			if (!"200".equals(center_response.getResultCode())) {
    			System.out.println(center_response.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(MobileCMLib.makeTmsMsg(center_response));
    			return response;
			}
			System.out.println(center_response.toString());

			//????????????
			TmsZoneListResponse tmsZoneListResponse = gson.fromJson(mApiTmsController.tms_zoneList(as_appKey), TmsZoneListResponse.class);
			if (!"200".equals(tmsZoneListResponse.getResultCode())) {
    			System.out.println(tmsZoneListResponse.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(MobileCMLib.makeTmsMsg(tmsZoneListResponse));
    			return response;
			}
			if (tmsZoneListResponse.getResultData().size() > 0) {
				response.setResultCode("5001");
				response.setResultMessage("??????????????? ?????? ??????????????? ????????????.");
				return response;
			}
			System.out.println("TMS ?????????=" + tmsZoneListResponse.getResultData().size());
			
			//???????????????
			BaseResponse order_response = new BaseResponse();
			params = new HashMap<String, Object>();
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);			
			params.put("rem_dt", as_rem_dt);
			params.put("sti_cd_arr", as_sti_cd_area_arr);
			
			order_response = tmserp_UpdateOrder(params);		
			if (!"200".equals(order_response.getResultCode())) {
    			System.out.println(order_response.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(MobileCMLib.makeTmsMsg(order_response));
    			return response;
			}
			String orderList = order_response.getMappingKey();
			System.out.println("OrderList=" + orderList);
			System.out.println("OrderCount=" + order_response.getResultCount());
			
			//???????????????
			BaseResponse vehicle_response = new BaseResponse();
			params = new HashMap<String, Object>();			
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);			
			params.put("sti_cd_arr", as_sti_cd_arr);			
			
			vehicle_response = tmserp_UpdateVehicle(params);
						
			if (!"200".equals(vehicle_response.getResultCode())) {
    			System.out.println(vehicle_response.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(MobileCMLib.makeTmsMsg(vehicle_response));
    			return response;
			}
			String vehicleList = vehicle_response.getMappingKey();
			System.out.println("VehicleList=" + vehicleList);
						
			TmsAllocationDataResponse tmpTmsAllocationDataResponse;
			//tmpTmsAllocationDataResponse = gson.fromJson(mApiTmsController.tms_allocation("1", "", "", as_start_time), TmsAllocationDataResponse.class);
			//tmpTmsAllocationDataResponse = gson.fromJson(mApiTmsController.tms_allocation("2", orderList, vehicleList, as_start_time), TmsAllocationDataResponse.class);
			tmpTmsAllocationDataResponse = gson.fromJson(mApiTmsController.tms_allocationRequest(as_appKey, "1", "", "", as_start_time), TmsAllocationDataResponse.class);
			if (!"200".equals(tmpTmsAllocationDataResponse.getResultCode())) {
    			System.out.println(tmpTmsAllocationDataResponse.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(MobileCMLib.makeTmsMsg(tmpTmsAllocationDataResponse));
    			return response;
			}			
            System.out.println("Allocate ??????:" + gson.toJson(tmpTmsAllocationDataResponse));
            
            System.out.println("==========================================================");
            System.out.println("mappingKey=" + tmpTmsAllocationDataResponse.getMappingKey());
            System.out.println("==========================================================");
        
            response.setMappingKey(tmpTmsAllocationDataResponse.getMappingKey());
    		response.setResultCode("200");
    		response.setResultMessage(tmpTmsAllocationDataResponse.getResultMessage());
    		
		} catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		return response;

	}
		
	@Override
	public BaseResponse tmserp_UpdateOrder(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_rem_dt = (String) param.get("rem_dt");
			String as_sti_cd_arr = (String) param.get("sti_cd_arr");
			
			//TMS????????? ?????? ???????????????
			BaseResponse orderDeleteResponse = gson.fromJson(mApiTmsController.tms_orderDelete(as_appKey, "1", ""), BaseResponse.class);			
			if (!"200".equals(orderDeleteResponse.getResultCode())) {
				txManager.rollback(status);
    			System.out.println(orderDeleteResponse.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(orderDeleteResponse.getResultMessage());
    			return response;
			}
			
			//ERP????????? ?????????
			params = new HashMap<String, Object>();
			params.put("k_sti_cd", as_k_sti_cd);
			params.put("rem_dt", as_rem_dt);
			params.put("sti_cd_arr", as_sti_cd_arr);
			ArrayList<TMSERPOrderList> arErpOrderList = tmserpScheduling.selectOrderList(params);
			System.out.println("ERP ????????????:" + arErpOrderList.size());			
			
			if (arErpOrderList.size() < 1) {
				txManager.rollback(status);
    			System.out.println("????????? ???????????? ???????????? ????????????.");        			
    			response.setResultCode("5001");
    			response.setResultMessage("????????? ???????????? ???????????? ????????????.");
    			return response;
			}
			
			StringBuffer pointname_arr = new StringBuffer();
			for (int i=0; i<arErpOrderList.size(); i++) {
				if ("C142".equals(arErpOrderList.get(i).getCom_rfg())) {
					if ("".equals(pointname_arr.toString())) {
						pointname_arr.append(arErpOrderList.get(i).getOrderName());
					} else {
						pointname_arr.append("," + arErpOrderList.get(i).getOrderName());
					}
				}
			}
			if (!"".equals(pointname_arr.toString())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(pointname_arr.toString() + "?????? ??????????????? ?????????. ???????????? ????????? ???????????????.");
				return response;
			}
			
            TmsGeocodingCoordinateInfoResponse geocoding = new TmsGeocodingCoordinateInfoResponse();            
            for(int i=0; i<arErpOrderList.size(); i++) {
            	geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_NEW, arErpOrderList.get(i).getAddress()), TmsGeocodingCoordinateInfoResponse.class);
            	            	
            	if (!"200".equals(geocoding.getResultCode())) {
            		if ("Bad Request".equals(geocoding.getResultMessage())) {
            			System.out.println("???????????? ?????? ??????:" + arErpOrderList.get(i).getAddress());
            			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_OLD, arErpOrderList.get(i).getAddress()), TmsGeocodingCoordinateInfoResponse.class);
            			if (!"200".equals(geocoding.getResultCode())) {
            				txManager.rollback(status);
            				System.out.println(geocoding.getResultMessage());        			
    	        			response.setResultCode("5001");
    	        			response.setResultMessage(geocoding.getResultMessage());
    	        			return response;
            			} else {
            				if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
        	        			arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());	
        	        			arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
        	        			System.out.println("Order new lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
        	        		}
            			}
            		} else {
            			txManager.rollback(status);
	        			System.out.println(geocoding.getResultMessage());        			
	        			response.setResultCode("5001");
	        			response.setResultMessage(geocoding.getResultMessage());
	        			return response;
            		}
            	} else {            	
	        		if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
	        			arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
	        			arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
	        			System.out.println("Order lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
	        		}
            	}
            }
            
			//TMS???????????????
			BaseResponse orderInsertResponse = gson.fromJson(mApiTmsController.tms_orderListInsert(as_appKey, arErpOrderList), BaseResponse.class);
			if (!"200".equals(orderInsertResponse.getResultCode())) {
				txManager.rollback(status);
				System.out.println(orderInsertResponse.getResultMessage());        			
				response.setResultCode("5001");
				response.setResultMessage(orderInsertResponse.getResultMessage());
				return response;
			}
			
			StringBuffer orderId = new StringBuffer();
            for(int i=0; i<arErpOrderList.size(); i++) {
            	if (i==0) {
            		orderId.append(arErpOrderList.get(i).getOrderId());
            	} else {
            		orderId.append(","+arErpOrderList.get(i).getOrderId());
            	}
            }
            response.setMappingKey(orderId.toString());
            response.setResultCount(""+arErpOrderList.size());
            response.setResultMessage(arErpOrderList.size() + "?????? ???????????? ?????????????????????.");
            System.out.println("TMS ?????? ????????????:" + arErpOrderList.size());
            
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;

	}
		
	@Override
	public BaseResponse tmserp_UpdateVehicle(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_sti_cd_arr = (String) param.get("sti_cd_arr");
			
			//TMS????????? ?????? ????????????
			TmsVehicleListResponse tmsVehicleListResponse = gson.fromJson(mApiTmsController.tms_vehicleList(as_appKey), TmsVehicleListResponse.class);
			System.out.println("TMS ????????? ?????????:" + tmsVehicleListResponse.getResultData().size());

			//TMS????????? ?????? ????????????
			BaseResponse vehicleDeleteResponse = gson.fromJson(mApiTmsController.tms_vehicleDelete(as_appKey, "1", ""), BaseResponse.class);			
			if (!"200".equals(vehicleDeleteResponse.getResultCode())) {
				txManager.rollback(status);
    			System.out.println(vehicleDeleteResponse.getResultMessage());        			
    			response.setResultCode("5001");
    			response.setResultMessage(vehicleDeleteResponse.getResultMessage());
    			return response;
			}
			
			ArrayList<TMSERPVehicleList> arErpVehicleList= new ArrayList<TMSERPVehicleList>();
			//ArrayList<TMSERPVehicleList> arErpVehicleListTemp = null;
			//ERP??????????????????
			params = new HashMap<String, Object>();
			params.put("k_sti_cd", as_k_sti_cd);
			if (StringUtil.notEmpty(as_sti_cd_arr)) {
				params.put("sti_cd_arr", as_sti_cd_arr);
				arErpVehicleList = tmserpScheduling.selectVehicleListTeam(params);	
			} else if (StringUtil.notEmpty(as_sti_cd_arr)) {
				arErpVehicleList = tmserpScheduling.selectVehicleList(params);
			}
						
			System.out.println("ERP ????????????:" + arErpVehicleList.size());
			if (arErpVehicleList.size() < 2) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("ERP ???????????? ??????");
				return response;
			}
			
//			ArrayList<TMSERPVehicleList> arErpVehicleList= new ArrayList<TMSERPVehicleList>();
//			int cars = (ai_order_count / 2) - 1;
//			if (cars == 0) cars = 1;
//			if (cars > arErpVehicleListTemp.size()) cars = arErpVehicleListTemp.size();
//			for(int i=0; i<cars; i++) {
//				arErpVehicleList.add(i, arErpVehicleListTemp.get(i));
//			}
			
//			if(ai_order_count == 0) ai_order_count = arErpVehicleList.size();
//			if (ai_order_count < arErpVehicleList.size()) {
//				System.out.println("ERP ????????????:" + arErpVehicleList.size() +"?????? ?????????:" + ai_order_count + "??? ????????? ???????????? ??????");
//				for(int i=(arErpVehicleList.size()-1); i>=(ai_order_count-1); i--) {
//					arErpVehicleList.remove(i);
//				}
//			}
//			System.out.println("ERP ????????? ????????????:" + arErpVehicleList.size());
			
			//TMS????????????
			BaseResponse vehicleInsertResponse = gson.fromJson(mApiTmsController.tms_vehicleListInsert(as_appKey, arErpVehicleList), BaseResponse.class);
			if (!"200".equals(vehicleInsertResponse.getResultCode())) {
				txManager.rollback(status);
				System.out.println(vehicleInsertResponse.getResultMessage());        			
				response.setResultCode("5001");
				response.setResultMessage(vehicleInsertResponse.getResultMessage());
				return response;
			}
            response.setResultMessage(arErpVehicleList.size() + "?????? ????????? ?????????????????????.");
            System.out.println("TMS ?????? ?????????:" + arErpVehicleList.size());
            
            StringBuffer vehicleId = new StringBuffer();
            for(int i=0; i<arErpVehicleList.size(); i++) {
            	if (i==0) {
            		vehicleId.append(arErpVehicleList.get(i).getVehicleId());
            	} else {
            		vehicleId.append(","+arErpVehicleList.get(i).getVehicleId());
            	}
            }
            response.setMappingKey(vehicleId.toString());
            
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;

	}
		
	@Override
	public BaseResponse tmserp_UpdateCenter(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		int res = 0, count = 0;
		String baseUrl = "";
		
		try {			
			String as_appKey = (String) param.get("appKey");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
						
			//?????? ????????????
        	response= gson.fromJson(mApiTmsController.tms_allCenterDelete(as_appKey), BaseResponse.class);			
			
        	//ERP???????????????
			params = new HashMap<String, Object>();
			params.put("k_sti_cd", as_k_sti_cd);
			ArrayList<TMSERPCenterList> arList = tmserpScheduling.selectCenterList(params);
            if (arList.size() < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ????????????. [" + res + "]");
				return response;
			}
            
            System.out.println("Center Count= " + arList.size());
                        
            TmsGeocodingCoordinateInfoResponse geocoding = new TmsGeocodingCoordinateInfoResponse();            
            for(int i=0; i<arList.size(); i++) {
            	baseUrl = ServerInfo.LOCALHOST + "/v1/api/tms/fullTextGeocoding";
            	baseUrl += "?fullAddr=" + URLEncoder.encode("?????? ????????? ????????? ????????? ????????? 111-73 (?????????)", "UTF-8"); //arList.get(i).getScd_gaddr();            	
            	//baseUrl += "?fullAddr=" + URLEncoder.encode(arList.get(i).getScd_gaddr(), "UTF-8");
            	//System.out.println("baseUrl = " + baseUrl);
            	//System.out.println("getScd_gaddr = " + arList.get(i).getScd_gaddr());            	            	
            	
            	//jsonStr = RestService.sendREST(baseUrl, "");
            	//geocoding = gson.fromJson(jsonStr, TmsGeocodingCoordinateInfoResponse.class);
            	//baseUrl = "?????? ????????? ????????? ????????? ????????? 111-73 (?????????)";
            	baseUrl = arList.get(i).getScd_gaddr();
            	geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(TMapInfo.appKey, TMapInfo.addressFlag_OLD, baseUrl), TmsGeocodingCoordinateInfoResponse.class);            	
            	if (!"200".equals(geocoding.getResultCode())) {
            		if ("Bad Request".equals(geocoding.getResultMessage())) {
            			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(TMapInfo.appKey, TMapInfo.addressFlag_NEW, baseUrl), TmsGeocodingCoordinateInfoResponse.class);
            			if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
    	        			arList.get(i).setLoc_lon(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
    	        			arList.get(i).setLoc_lat(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
    	        			System.out.println("Center New lon=" + arList.get(i).getLoc_lon() +", lat=" + arList.get(i).getLoc_lat());
    	        		}	
            		} else {
	        			System.out.println(geocoding.getResultMessage());        			
	        			response.setResultCode("5001");
	        			response.setResultMessage(geocoding.getResultMessage());
	        			return response;
            		}
            	} else {            	
	        		if (geocoding.getCoordinateInfo().getTotalCount() > 0) {        			
	        			arList.get(i).setLoc_lon(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());	
	        			arList.get(i).setLoc_lat(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
	        			System.out.println("Center lon=" + arList.get(i).getLoc_lon() +", lat=" + arList.get(i).getLoc_lat());
	        		}
            	}
            	
    			BaseResponse centerInsertResponse = gson.fromJson(mApiTmsController.tms_centerInsert(as_appKey, arList.get(i)), BaseResponse.class);
    			if (!"200".equals(centerInsertResponse.getResultCode()) && !"4003".equals(centerInsertResponse.getResultCode())) {
            		txManager.rollback(status);
        			System.out.println(centerInsertResponse.getResultMessage());        			
        			response.setResultCode("5001");
        			response.setResultMessage(centerInsertResponse.getResultMessage());
        			return response;
            	}
    			count++;

            }
                    		
            response.setResultMessage(count + "?????? ????????? ?????????????????????.");
            
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");		
		return response;

	}


	
	
}
