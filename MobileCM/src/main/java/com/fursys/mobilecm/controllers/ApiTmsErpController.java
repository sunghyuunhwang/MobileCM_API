package com.fursys.mobilecm.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.mapper.ErpCommMapper;
import com.fursys.mobilecm.mapper.TMSERPSchedulingMapper;
import com.fursys.mobilecm.mapper.UserMapper;
import com.fursys.mobilecm.schedul.JobScheduler;
import com.fursys.mobilecm.security.FursysPasswordEncoder;
import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.service.ApiTmsErpService;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.FursysUser;
import com.fursys.mobilecm.vo.UserEtc;
import com.fursys.mobilecm.vo.erp.ERPScheduleCount;
import com.fursys.mobilecm.vo.erp.ERPScheduleList;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.mobile.response.UserInfoResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsGeocodingCoordinateInfoResponse;
import com.fursys.mobilecm.vo.tmserp.TMSERPAllMigyeolRepo;
import com.fursys.mobilecm.vo.tmserp.TMSERPDefectDetail;
import com.fursys.mobilecm.vo.tmserp.TMSERPDefectInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPStimemberInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPFile;
import com.fursys.mobilecm.vo.tmserp.TMSERPKstiList;
import com.fursys.mobilecm.vo.tmserp.TMSERPKsticdAllList;
import com.fursys.mobilecm.vo.tmserp.TMSERPMigyeolDetailInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPMigyeolInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPResdtl;
import com.fursys.mobilecm.vo.tmserp.TMSERPResmst;
import com.fursys.mobilecm.vo.tmserp.TMSERPScheduleCount;
import com.fursys.mobilecm.vo.tmserp.TMSERPSigongAsItemList;
import com.fursys.mobilecm.vo.tmserp.TMSERPSigongAsList;
import com.fursys.mobilecm.vo.tmserp.TMSERPStimemberDetailInfo;
import com.fursys.mobilecm.vo.tmserp.TMSERPTeamMigyeolRepo;
import com.fursys.mobilecm.vo.tmserp.TMSERPVehicleList;
import com.fursys.mobilecm.vo.tmserp.TMSERPVndBanpum;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/api/tmserp")
public class ApiTmsErpController {
	
	@Autowired private ApiTmsErpService apiTmsErpService;
	
	@Autowired private ErpCommMapper erpcommMapper;
	@Autowired private UserMapper userMapper;
	@Autowired private TMSERPSchedulingMapper tmserpScheduling;
	@Autowired private ApiTmsController mApiTmsController;
	@Autowired Environment environment; 
	
	@Autowired private PlatformTransactionManager txManager;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${spring.datasource.url}")
    private String mDBUrl;

	Gson gson = new Gson();
	boolean	isDeBug = false;
	
	public UserEtc getUserEtc(User user) {
		UserEtc etc = new UserEtc();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
	    	params.put("vnd_cd", user.getUsername());
	    	Map<String, Object> map = userMapper.getUserEtc(params);
	    	
	    	etc.setCom_scd(map.get("COM_SCD").toString());
	    	etc.setSti_cd(map.get("STI_CD").toString());
	    	etc.setK_sti_cd(map.get("K_STI_CD").toString());
	    	etc.setTmap_appkey(map.get("TMAP_APPKEY").toString());
	    	etc.setUsr_id(user.getUsername());
	    	
		} catch(Exception e) {
			System.out.println("getUserEtc:" + e.toString());
		}
		
		return etc;
	}
	
	public TmsGeocodingCoordinateInfoResponse Geocoding(String as_appKey, String address) {
		TmsGeocodingCoordinateInfoResponse geocoding = new TmsGeocodingCoordinateInfoResponse();		
		geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_NEW, address), TmsGeocodingCoordinateInfoResponse.class);	            	            	
    	if (!"200".equals(geocoding.getResultCode())) {
    		if ("Bad Request".equals(geocoding.getResultMessage())) {
    			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(as_appKey, TMapInfo.addressFlag_OLD, address), TmsGeocodingCoordinateInfoResponse.class);    			
    			if (!"200".equals(geocoding.getResultCode())) {
    				System.out.println(geocoding.getResultMessage());        			
        			return geocoding;
    			} else {
    				geocoding.getCoordinateInfo().getCoordinate().get(0).setNewLon(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());
    				geocoding.getCoordinateInfo().getCoordinate().get(0).setNewLat(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
    			}
    		} else {
    			System.out.println(geocoding.getResultMessage());        			
    			return geocoding;
    		}
    	}
		return geocoding;
	}
		
	@ApiOperation(value = "tmserp_RouteSequential", notes = "다중 경유지안내 요청")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "다중 경유지안내 요청 실패 !!") })
	@GetMapping("/tmserp_RouteSequential")
	@RequestMapping(value = "/tmserp_RouteSequential", method = RequestMethod.GET)
	public String tmserp_RouteSequential(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "시공팀코드", required = true, example = "YA203")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "시공일자", required = true, example = "20201102")
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@ApiParam(value = "센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("com_scd", as_com_scd);
		params.put("rem_dt", as_rem_dt);
		params.put("sti_cd", as_sti_cd);

		response = apiTmsErpService.tmserp_RouteSequential(params);

		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_RouteOptimization", notes = "경유지최적화 요청")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "경유지최적화 실패 !!") })
	@GetMapping("/tmserp_RouteOptimization")
	@RequestMapping(value = "/tmserp_RouteOptimization", method = RequestMethod.GET)
	public String tmserp_RouteOptimization(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "시공팀코드", required = true, example = "YA203")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "시공일자", required = true, example = "20201102")
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@ApiParam(value = "센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("com_scd", as_com_scd);
		params.put("rem_dt", as_rem_dt);
		params.put("sti_cd", as_sti_cd);

		response = apiTmsErpService.tmserp_RouteOptimization(params);

		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_AllocationUpdate", notes = "배차결과 ERP등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "배차결과 ERP등록 실패 !!") })
	@GetMapping("/tmserp_AllocationUpdate")
	@RequestMapping(value = "/tmserp_AllocationUpdate", method = RequestMethod.GET)
	public String tmserp_AllocationUpdate(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "아이디", required = false, example = "jds2014")
			@RequestParam(name = "usr_cd", required = false) String as_usr_cd,
			@ApiParam(value = "배차요청 매핑키", required = true, example = "e664a065-846c-4105-aa58-0d89e980964b")
			@RequestParam(name="mappingKey", required=true) String as_mappingKey,
			@ApiParam(value = "지역시공팀코드", required = true, example = "YA284,YA035,")
			@RequestParam(name = "sti_cd_area_arr", required = true) String as_sti_cd_area_arr,
			@ApiParam(value = "작업일자", required = true, example = "20201102")
			@RequestParam(name = "rem_dt", required = true) String as_rem_dt
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
			params.put("usr_cd", etc.getUsr_id());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
			params.put("usr_cd", as_usr_cd);
		}
		
		params.put("mappingKey", as_mappingKey);
		params.put("sti_cd_area_arr", as_sti_cd_area_arr);
		params.put("rem_dt", as_rem_dt);		
		
		response = apiTmsErpService.tmserp_AllocationUpdate(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_UpdateOrderGeocoding", notes = "배송지 위치 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "배송지 위치 등록 실패 !!") })
	@GetMapping("/tmserp_UpdateOrderGeocoding")
	@RequestMapping(value = "/tmserp_UpdateOrderGeocoding", method = RequestMethod.GET)
	public String tmserp_UpdateOrderGeocoding(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "작업일자", required = true, example = "20201028")
			@RequestParam(name = "rem_dt", required = true) String as_rem_dt,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd
			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("rem_dt", as_rem_dt);
		
		response = apiTmsErpService.tmserp_UpdateOrderGeocoding(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_AllocationConfirmSet", notes = "예약확정 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "예약확정 등록 실패 !!") })
	@GetMapping("/tmserp_AllocationConfirmSet")
	@RequestMapping(value = "/tmserp_AllocationConfirmSet", method = RequestMethod.GET)
	public String tmserp_AllocationConfirmSet(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "아이디", required = false, example = "jds2014")
			@RequestParam(name = "usr_cd", required = false) String as_usr_cd,
			@ApiParam(value="시공일자", required = true, example = "2020-06-04")
			@RequestParam(name="rem_dt", required = true) String as_rem_dt,
			@ApiParam(value = "서비스센터코드", required = true, example = "C16YA,C16YA")
			@RequestParam(name="com_scd_arr", required = true) String as_com_scd_arr,
			@ApiParam(value = "시공팀코드", required = true, example = "YA142,YA217")
			@RequestParam(name="sti_cd_arr", required = true) String as_sti_cd_arr			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("usr_cd", etc.getUsr_id());
		} else {
			params.put("usr_cd", as_usr_cd);
		}

		params.put("rem_dt", as_rem_dt.replace("-", ""));		
		params.put("com_scd_arr", as_com_scd_arr);
		params.put("sti_cd_arr", as_sti_cd_arr);
				
		response = apiTmsErpService.tmserp_AllocationConfirmSet(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_AllocationConfirm", notes = "분배확정 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "분배확정 등록 실패 !!") })
	@GetMapping("/tmserp_AllocationConfirm")
	@RequestMapping(value = "/tmserp_AllocationConfirm", method = RequestMethod.GET)
	public String tmserp_AllocationConfirm(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "아이디", required = false, example = "jds2014")
			@RequestParam(name = "usr_cd", required = false) String as_usr_cd,
			@ApiParam(value="시공일자", required = true, example = "2020-06-04")
			@RequestParam(name="rem_dt", required = true) String as_rem_dt,
			@ApiParam(value = "서비스센터코드", required = true, example = "C16YA,C16YA")
			@RequestParam(name="com_scd_arr", required = true) String as_com_scd_arr,
			@ApiParam(value = "시공팀코드", required = true, example = "YA142,YA217")
			@RequestParam(name="sti_cd_arr", required = true) String as_sti_cd_arr			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("usr_cd", etc.getUsr_id());
		} else {
			params.put("usr_cd", as_usr_cd);
		}
		params.put("rem_dt", as_rem_dt.replace("-", ""));		
		params.put("com_scd_arr", as_com_scd_arr);
		params.put("sti_cd_arr", as_sti_cd_arr);
				
		response = apiTmsErpService.tmserp_AllocationConfirm(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_selectVehicleList", notes = "시공팀 리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀리스트 요청 실패 !!") })
	@GetMapping("/tmserp_selectVehicleList")
	@RequestMapping(value = "/tmserp_selectVehicleList", method = RequestMethod.GET)
	public String tmserp_selectVehicleList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd
			) {		
		
		BaseResponse response = new BaseResponse();
		
		//ERP시공팀리스트
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("k_sti_cd", as_k_sti_cd);
		}

		ArrayList<TMSERPVehicleList> arErpVehicleListTemp = tmserpScheduling.selectVehicleList(params);
		System.out.println("ERP 시공팀수:" + arErpVehicleListTemp.size());
		ArrayList<TMSERPVehicleList> arErpVehicleList= new ArrayList<TMSERPVehicleList>();
		System.out.println("ERP UPDATE 시공팀수:" + arErpVehicleList.size());

		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_Allocation", notes = "배차요청")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "배차요청 실패 !!") })
	@GetMapping("/tmserp_Allocation")
	@RequestMapping(value = "/tmserp_Allocation", method = RequestMethod.GET)
	public String tmserp_Allocation(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "지역시공팀코드", required = true, example = "YA284,YA035,")
			@RequestParam(name = "sti_cd_area_arr", required = true) String as_sti_cd_area_arr,
			@ApiParam(value = "시공팀코드", required = true, example = "YA142,YA217,YA234,YA256,YA223,")
			@RequestParam(name = "sti_cd_arr", required = true) String as_sti_cd_arr,
			@ApiParam(value = "작업일자", required = true, example = "20201102")
			@RequestParam(name = "rem_dt", required = true) String as_rem_dt,
			@ApiParam(value = "작업시작시간", required = true, example = "0900")
			@RequestParam(name = "start_time", required = true) String as_start_time
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("sti_cd_area_arr", as_sti_cd_area_arr);
		params.put("sti_cd_arr", as_sti_cd_arr);
		params.put("rem_dt", as_rem_dt);
		params.put("start_time", as_start_time);
		
		response = apiTmsErpService.tmserp_Allocation(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_UpdateOrder", notes = "배송지 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "배송지 등록 실패 !!") })
	@GetMapping("/tmserp_UpdateOrder")
	@RequestMapping(value = "/tmserp_UpdateOrder", method = RequestMethod.GET)
	public String tmserp_UpdateOrder(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "시공팀코드(지역)", required = true, example = "YA035,YA284")
			@RequestParam(name = "sti_cd_arr", required = true) String as_sti_cd_arr,			
			@ApiParam(value = "작업일자", required = true, example = "20201102")
			@RequestParam(name = "rem_dt", required = true) String as_rem_dt
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("sti_cd_arr", as_sti_cd_arr);
		params.put("rem_dt", as_rem_dt);
		
		response = apiTmsErpService.tmserp_UpdateOrder(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_UpdateVehicle", notes = "시공팀 차량 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀 차량 등록 실패 !!") })
	@GetMapping("/tmserp_UpdateVehicle")
	@RequestMapping(value = "/tmserp_UpdateVehicle", method = RequestMethod.GET)
	public String tmserp_UpdateVehicle(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String as_appKey,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd,
			@ApiParam(value = "시공팀코드", required = true, example = "YA142,YA217")
			@RequestParam(name = "sti_cd_arr", required = true) String as_sti_cd_arr
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("appKey", etc.getTmap_appkey());
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("appKey", as_appKey);
			params.put("k_sti_cd", as_k_sti_cd);
		}
		params.put("sti_cd_arr", as_sti_cd_arr);
		
		response = apiTmsErpService.tmserp_UpdateVehicle(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_UpdateCenter", notes = "센터 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "센터 등록 실패 !!") })
	@GetMapping("/tmserp_UpdateCenter")
	@RequestMapping(value = "/tmserp_UpdateCenter", method = RequestMethod.GET)
	public String tmserp_UpdateCenter(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "아이디", required = false, example = "jds2014")
			@RequestParam(name = "usr_cd", required = false) String as_usr_cd			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("usr_cd", etc.getUsr_id());
		} else {
			params.put("usr_cd", as_usr_cd);
		}
		
		response = apiTmsErpService.tmserp_UpdateCenter(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "login", notes = "mobile용 로그인")
	@ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 5001, message = "LoginFail !!")
	})
	@GetMapping("/login") 
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(
			@RequestParam(name="id", required=false) String id,
			@RequestParam(name="pw", required=false) String pw) { 
		
		BaseResponse loginResponse = new BaseResponse();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("login_id", id);
    	FursysUser user = userMapper.getUserInfo(params);
  
    	if (user == null){
    		loginResponse.setResultCode("5001");
    		return gson.toJson(loginResponse);
    	}

    	FursysPasswordEncoder fursysPasswordEncoder = new FursysPasswordEncoder();
    	
    	String encPw = fursysPasswordEncoder.encode(pw);
    	
    	if (encPw.matches(user.getUsr_pwd())) {
    		loginResponse.setResultCode("200");
    	} else {
    		loginResponse.setResultCode("5001");
    	}
		
		return gson.toJson(loginResponse);
	}
	
	@ApiOperation(value = "login", notes = "mobile용 로그인")
	@ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 5001, message = "LoginFail !!")
	})
	
	@GetMapping("/userInfo") 
	@RequestMapping(value="/userInfo",method=RequestMethod.GET)
	public String userInfo(
			@RequestParam(name="id", required=false) String id) { 
		
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("login_id", id);
    	FursysUser user = userMapper.getUserInfo(params);
  
    	if (user == null) {
    		userInfoResponse.setResultCode("5001");
    		return gson.toJson(userInfoResponse);
    	} else {
    		params = new HashMap<String, Object>();
        	params.put("login_id", id);
        	Map<String, Object> vnd_cd = userMapper.getUserVND_CD(params);
        	
        	user.setVnd_cd(vnd_cd.get("VND_CD").toString());
        	if (mDBUrl.contains("192.9.201.220")) {
        		user.setDb("REAL");
        	} else {
        		user.setDb("TEST");
        	}

        	params = new HashMap<String, Object>();
        	params.put("vnd_cd", user.getVnd_cd());
        	Map<String, Object> etc = userMapper.getUserEtc(params);
        	
        	user.setCom_scd(etc.get("COM_SCD").toString());
        	user.setSti_cd(etc.get("STI_CD").toString());
        	user.setK_sti_cd(etc.get("K_STI_CD").toString());
    		
    		userInfoResponse.setUser(user);
    		userInfoResponse.setResultCode("200");
    	}
		System.out.println(userInfoResponse.getUser().toString());
		return gson.toJson(userInfoResponse);
	}
	
	@ApiOperation(value = "erp_SelectSigongAverageInfo", notes = "tms시공현황정보")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공현황정보 불러오기 실패") })
	@GetMapping("/erp_SelectSigongAverageInfo") 
	@RequestMapping(value="/erp_SelectSigongAverageInfo",method=RequestMethod.GET)
	public String erp_SelectSigongAverageInfo(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "회사코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String as_com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "sti_cd", required = false) String as_sti_cd,
			@RequestParam(name="time", required=false) String time
			
		) { 

        HashMap<String, Object> params = new HashMap<String, Object>();

        if (user != null) {
			UserEtc etc = getUserEtc(user);
			params.put("com_scd", etc.getCom_scd());
			params.put("sti_cd", etc.getSti_cd());			
		} else {
			params.put("com_scd", as_com_scd);
			params.put("sti_cd", as_sti_cd);
		}
        params.put("date", time);
        
        TMSERPScheduleCount allItems = tmserpScheduling.selectSigongAverageCount(params);
		
		if (allItems == null) {
			allItems = new TMSERPScheduleCount(0, 0, 0);
		}
		
		return gson.toJson(allItems);
	}	
//	
//	@ApiOperation(value = "erp_SelectSigongAverageInfo", notes = "tms시공현황정보")
//	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공현황정보 불러오기 실패") })
//	@GetMapping("/erp_SelectSigongAverageInfo") 
//	@RequestMapping(value="/erp_SelectSigongAverageInfo",method=RequestMethod.GET)
//	public String erp_SelectSigongAverageInfo(
//			@RequestParam(name="time", required=false) Long time,
//			@RequestParam(name="com_scd", required=true) String com_scd,
//			@RequestParam(name="sti_cd", required=true) String sti_cd
//		) { 
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
//        Date date = new Date(time);
//		
//		HashMap<String,Object> params = new HashMap<String, Object>();
//        params.put("date", format.format(date));
//        params.put("com_scd",com_scd);
//        params.put("sti_cd",sti_cd);
//        
//        TMSERPScheduleCount allItems = tmserpScheduling.selectSigongAverageCount(params);
//		
//		if (allItems == null) {
//			allItems = new TMSERPScheduleCount(0, 0, 0);
//		}
//		
//		return gson.toJson(allItems);
//	}		
//	
	
	@ApiOperation(value = "erp_SelectTeamSigongList", notes = "tms시공팀현황리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀현황리스트 불러오기 실패") })
	@GetMapping("/erp_SelectTeamSigongList") 
	@RequestMapping(value="/erp_SelectTeamSigongList",method=RequestMethod.GET)
	public String erp_SelectTeamSigongList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "회사코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String as_com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "sti_cd", required = false) String as_sti_cd,
			@RequestParam(name="time", required=false) String time
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);
			params.put("com_scd", etc.getCom_scd());
			params.put("sti_cd", etc.getSti_cd());			
		} else {
			params.put("com_scd", as_com_scd);
			params.put("sti_cd", as_sti_cd);
		}
		params.put("date", time);
        
        ArrayList<TMSERPKstiList> allItems = tmserpScheduling.selectKstiScheduleList(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "`", notes = "tms시공팀시공/as리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "tms시공팀시공/as리스트 불러오기 실패") })
	@GetMapping("/erp_SelectSigongAsList") 
	@RequestMapping(value="/erp_SelectSigongAsList",method=RequestMethod.GET)
	public String erp_SelectSigongAsList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "회사코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String as_com_scd,			
			@RequestParam(name="sti_cd", required=false) String sti_cd,
			@RequestParam(name="time", required=false) String time,
			@RequestParam(name="sort_type", required=false, defaultValue="ORM_NM") String sort_type,
			@RequestParam(name="sort_seq", required=false, defaultValue="DESC") String sort_seq
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);
			params.put("com_scd", etc.getCom_scd());			
		} else {
			params.put("com_scd", as_com_scd);
		}
        params.put("date", time);
        params.put("sti_cd", sti_cd);
        
        //2021.10.20 일정조회 정렬 기능 추가, hong
        params.put("sort_type", sort_type);
        params.put("sort_seq", sort_seq);
        
        ArrayList<TMSERPSigongAsList> allItems = tmserpScheduling.selectSigongAsList(params);
		
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "erp_SelectSigongItemList", notes = "tms시공팀시공 품목 리스트 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "tms시공팀시공 품목 리스트 불러오기 실패") })
	@GetMapping("/erp_SelectSigongItemList") 
	@RequestMapping(value="/erp_SelectSigongItemList",method=RequestMethod.GET)
	public String erp_SelectSigongItemList(
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec
		) { 
			
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
        
         if("시공".equals(com_ssec)) {
        	 ArrayList<TMSERPSigongAsItemList> allItems = tmserpScheduling.selectSigongItemList(params);
        	 return gson.toJson(allItems);
         }else {
        	 ArrayList<TMSERPSigongAsItemList> allItems = tmserpScheduling.selectAsItemList(params);
        	 return gson.toJson(allItems);
         }
       
	}		

	@ApiOperation(value = "erp_SelectAsItemList", notes = "tms시공팀as 품목 리스트 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "tms시공팀as 품목 리스트 불러오기 실패") })
	@GetMapping("/erp_SelectAsItemList") 
	@RequestMapping(value="/erp_SelectAsItemList",method=RequestMethod.GET)
	public String erp_SelectAsItemList(
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
        
        ArrayList<TMSERPSigongAsItemList> allItems = tmserpScheduling.selectAsItemList(params);

		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_SelectKstcdList", notes = "권역시공팀소속 시공팀 리스트 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "권역시공팀소속 시공팀 리스트 불러오기 실패") })
	@GetMapping("/erp_SelectKstcdList") 
	@RequestMapping(value="/erp_SelectKstcdList",method=RequestMethod.GET)
	public String erp_SelectKstcdList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "권역장코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String as_k_sti_cd			
		) { 
		 
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);		
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("k_sti_cd", as_k_sti_cd);
		}
        
        ArrayList<TMSERPKsticdAllList> allItems = tmserpScheduling.selectKsticdAllList(params);

		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_updateSticd", notes = "단건 팀 변경")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "단건 팀 변경 Update 실패") })	
	@GetMapping("/erp_updateSticd")  
	@RequestMapping(value = "/erp_updateSticd", method = RequestMethod.GET)
	public String erp_updateSticd (
			@RequestParam(name="login_id", required=true) String login_id,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="com_ctsec", required=true) String com_ctsec,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="rem_seq", required=true) String rem_seq,	
			@RequestParam(name="plm_no", required=false) String plm_no,
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="sti_nm", required=false) String sti_nm,
			@RequestParam(name="com_ssec", required=false) String com_ssec,
			@RequestParam(name="treq_time", required=false) String treq_time
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		DataResult dataResult = new DataResult();
		AsResultResponse response = new AsResultResponse();
		
		try {
							
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("sti_cd", sti_cd);
	        params.put("com_ctsec", com_ctsec);
	        params.put("com_scd", com_scd);
	        params.put("rem_dt", rem_dt);
	        params.put("rem_seq", rem_seq);
	        params.put("plm_no", plm_no);
	        params.put("orm_no", orm_no);
	        params.put("sti_nm", sti_nm);
	        params.put("com_ssec", com_ssec);
	        params.put("treq_time", treq_time);
	        params.put("login_id", login_id);
	        
//	        dataResult = tmserpScheduling.getKstiCdYn(params);
//	        int kstiyn_check = dataResult.getValue1();
//	        
//			if (kstiyn_check == 0) {				
//				txManager.rollback(status);				
//				response.setResultCode("5001");
//				response.setResultMessage("권역장이 아닌경우 팀변경이 불가합니다.");
//				return gson.toJson(response);
//			}	        
	        
	        
	        dataResult = tmserpScheduling.getComRfg(params);
	        String com_rfg = dataResult.getData1();
	        String orm_nm = dataResult.getData2();
			if ("C142".equals(com_rfg)) {				
				txManager.rollback(status);				
				response.setResultCode("5001");
				response.setResultMessage(orm_nm + "님 건은 예약확정건 입니다. 확정취소 처리후 작업하세요.");
				return gson.toJson(response);
			}
	        
        	//tc_resmst 변경
        	res = tmserpScheduling.updateSticdResmst(params);
        	
			if (res < 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateSticdResmst 오류 [" + res + "]");
				return gson.toJson(response);
			}	        	

			//tc_planmst 변경
        	res = tmserpScheduling.updateSticdTcPlanmst(params);
        	
			if (res < 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateSticdTcPlanmst 오류 [" + res + "]");
				return gson.toJson(response);
			}				
 		
	        if("C18A".equals(com_ssec)) {
	        	
	        	//ta_rptreq 변경
	        	res = tmserpScheduling.updateSticdTaRptReq(params);
	        	
				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaRptReq 오류 [" + res + "]");
					return gson.toJson(response);
				}	        	

				//ta_otpminf 변경
	        	res = tmserpScheduling.updateSticdTaOtpMinf(params);
	        	
				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaOtpMinf 오류 [" + res + "]");
					return gson.toJson(response);
				}				
				
				//시공팀 hp 가져오기
				
//				dataResult = tmserpScheduling.getStmHpNo(params);
//				
//				String stmHpNo2 = dataResult.getData1();;			
//
//				if (stmHpNo2 == null) {
//					stmHpNo2 = "";
//				}			
//				
//				params.put("stm_hp", stmHpNo2);
				 
				//ta_planmst 변경
	        	res = tmserpScheduling.updateSticdTaPlanmst(params);									

				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaPlanMst 오류 [" + res + "]");
					return gson.toJson(response);
				}	
				
	        } else if ("AS".equals(com_ssec) )	{

	        	//ta_rptreq 변경
	        	res = tmserpScheduling.updateSticdTaRptReq(params);
	        	
				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaRptReq 오류 [" + res + "]");
					return gson.toJson(response);
				}	        	

				//ta_otpminf 변경
	        	res = tmserpScheduling.updateSticdTaOtpMinf(params);
	        	
				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaOtpMinf 오류 [" + res + "]");
					return gson.toJson(response);
				}				
				
				//시공팀 hp 가져오기
				
//				dataResult = tmserpScheduling.getStmHpNo(params);
//				
//				String stmHpNo2 = dataResult.getData1();;			
//
//				if (stmHpNo2 == null) {
//					stmHpNo2 = "";
//				}			
//				
//				params.put("stm_hp", stmHpNo2);
				 
				//ta_planmst 변경
	        	res = tmserpScheduling.updateSticdTaPlanmst(params);									

				if (res < 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateSticdTaPlanMst 오류 [" + res + "]");
					return gson.toJson(response);
				}	
				
				
				
	        } else {
				//시공팀 hp 가져오기
				
//				dataResult = tmserpScheduling.getStmHpNo(params);
//					
//				String stmHpNo = dataResult.getData1();;			
//	
//				if (stmHpNo == null) {
//					stmHpNo = "";
//				}			
//				
//				params.put("stm_hp", stmHpNo);
//				
//				//tr_schedule 변경
//				res = tmserpScheduling.updateSticdTrSchedule(params);
//	        	
//				if (res < 0) {
//					txManager.rollback(status);
//					response.setResultCode("5001");
//					response.setResultMessage("updateSticdTrSchedule 오류 [" + res + "]");
//					return gson.toJson(response);
//				}	 	        	
	        }
	        	

			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
		
	}	
	
	@ApiOperation(value = "erp_stiteamAllList", notes = "tms분배할시공팀리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "tms분배할시공팀리스트 불러오기 실패") })
	@GetMapping("/erp_stiteamAllList") 
	@RequestMapping(value="/erp_stiteamAllList",method=RequestMethod.GET)
	public String erp_stiteamAllList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "회사코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String as_com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "sti_cd", required = false) String as_sti_cd,			
			@RequestParam(name="time", required=false) String time
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = getUserEtc(user);
			params.put("com_scd", etc.getCom_scd());
			params.put("sti_cd", etc.getSti_cd());			
		} else {
			params.put("com_scd", as_com_scd);
			params.put("sti_cd", as_sti_cd);
		}
		params.put("date", time);
                
        ArrayList<TMSERPKstiList> allItems = tmserpScheduling.selectKstiTeamSelectList(params);
		
		return gson.toJson(allItems);
	}	
	
		
	@ApiOperation(value="/getResmstList", notes="tms시공건검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공건검색실패") })
	@GetMapping("/getResmstList")
	@RequestMapping(value="/getResmstList",method=RequestMethod.GET)
	public String getResmstList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "from_dt", required = true, example = "20211003")
			@RequestParam(name = "from_dt", required = true) String from_dt,
			@ApiParam(value = "to_dt", required = true, example = "20211013")
			@RequestParam(name = "to_dt", required = true) String to_dt,
			//@ApiParam(value = "com_scd", required = true, example = "C16YA")
			//@RequestParam(name = "com_scd", required = true) String com_scd,
			//@ApiParam(value = "ksti_cd", required = true, example = "YA601")
			//@RequestParam(name = "ksti_cd", required = true) String ksti_cd,
			@ApiParam(value = "orm_nm", required = false, example = "김")
			@RequestParam(name = "orm_nm", required = false) String orm_nm,
			@ApiParam(value = "itm_cd", required = false, example = "")
			@RequestParam(name = "itm_cd", required = false) String itm_cd
			) throws Exception {
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPResmst> resList = null;
		
		try {
			if (user != null) {
				UserEtc etc = getUserEtc(user);
				params.put("com_scd", etc.getCom_scd());
				params.put("ksti_cd", etc.getSti_cd());
				//params.put("com_scd", "C16YA");
				//params.put("ksti_cd", "YA601");
				params.put("from_dt", from_dt.replace("-", ""));
				params.put("to_dt", to_dt.replace("-", ""));
				params.put("orm_nm", orm_nm);
				params.put("itm_cd", itm_cd);
				
				resList = tmserpScheduling.selectResmstList(params);
				return gson.toJson(resList);	
			} else {
				//return gson.toJson(resList);
				throw new Exception();
			}			
		} catch(Exception e) {
			//return gson.toJson(resList);
			throw new Exception();
		} 
	}
	
	@ApiOperation(value="/getResdtlList", notes="tms시공건상세검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공건검색실패") })
	@GetMapping("/getResdtlList")
	@RequestMapping(value="/getResdtlList",method=RequestMethod.GET)
	public String getResdtlList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "com_ssec", required = true, example = "C18C")
			@RequestParam(name = "com_ssec", required = true) String com_ssec,
			@ApiParam(value = "orm_no", required = false, example = "I20210922138300")
			@RequestParam(name = "orm_no", required = false) String orm_no,
			@ApiParam(value = "rpt_no", required = false, example = "I202109180059")
			@RequestParam(name = "rpt_no", required = false) String rpt_no,
			@ApiParam(value = "rpt_seq", required = false, example = "01")
			@RequestParam(name = "rpt_seq", required = false) String rpt_seq
			) throws Exception {
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPResdtl> resDtlList = null;
		
		try {
			System.out.println("user : "+user);
			if (user != null) {
				if(com_ssec.equals("C18C")) {
					params.put("orm_no", orm_no);
					resDtlList = tmserpScheduling.selectSigongResdtlList(params);
				} else if (com_ssec.equals("C18A")) {
					params.put("rpt_no", rpt_no);
					params.put("rpt_seq", rpt_seq);
					resDtlList = tmserpScheduling.selectAsResdtlList(params);
				} else {
					throw new Exception();
				}
				return gson.toJson(resDtlList);				
			} else {
				//return gson.toJson(resList);
				throw new Exception();
			}			
		} catch(Exception e) {
			//return gson.toJson(resDtlList);
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getVndBanpum", notes="tms대리점반품의뢰현황조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getVndBanpum")
	@RequestMapping(value="/getVndBanpum",method=RequestMethod.GET)
	public String getVntBanpum(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "from_dt", required = true, example = "20211001")
			@RequestParam(name = "from_dt", required = true) String from_dt,
			@ApiParam(value = "to_dt", required = true, example = "20211019")
			@RequestParam(name = "to_dt", required = true) String to_dt
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPVndBanpum> vndBanpumList = null;
		
		try {
			if (user != null) {
				UserEtc etc = getUserEtc(user);
				params.put("com_scd", etc.getCom_scd());
				params.put("ksti_cd", etc.getSti_cd());
				params.put("from_dt", from_dt.replace("-", ""));
				params.put("to_dt", to_dt.replace("-", ""));
				vndBanpumList = tmserpScheduling.selectVndBanpumList(params);
				return gson.toJson(vndBanpumList);				
			} else {
				throw new Exception();
			}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getAttachFileList", notes="tms첨부파일 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getAttachFileList")
	@RequestMapping(value="/getAttachFileList",method=RequestMethod.GET)
	public String getAttachFileList(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "plm_no", required = true, example = "plm_no")
			@RequestParam(name = "plm_no", required = true) String plm_no
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPFile> fileList = null;
		
		try {
			String file_id;
			String proof_file_id;
			if (user != null) {
				
				file_id = "cresult" + plm_no;
				proof_file_id = "proof" + plm_no;
				
				params.put("proof_file_id", proof_file_id);
				params.put("file_id", file_id);
				
				fileList = tmserpScheduling.selectFileList(params);
				return gson.toJson(fileList);				
			} else {
				throw new Exception();
			}			
		} catch(Exception e) {
			throw new Exception();
		}
	}

	@ApiOperation(value="/fileDownload", notes="tms첨부파일 다운로드")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@PostMapping("/fileDownload")
	@RequestMapping(value="/fileDownload",method=RequestMethod.POST)
	public void fileDownload(HttpServletRequest request,  
			HttpServletResponse response,
			@AuthenticationPrincipal User user,
			@ApiParam(value = "file_id", required = true, example = "")
			@RequestParam(name = "file_id", required = true) String file_id,
			@ApiParam(value = "file_snum", required = true, example = "")
			@RequestParam(name = "file_snum", required = true) String file_snum
			) throws Exception {
		
	    BufferedInputStream inputStream = null;
	    OutputStream outStream = null;	
	    HashMap<String,Object> params = new HashMap<String, Object>();
	    TMSERPFile fileMap = new TMSERPFile();
	    
		try {
			
			if(user == null) {
				throw new Exception();
			}
			
	        if ("".equals(file_id)) {
	        	throw new Exception();
	        }
			
	        params.put("file_id", file_id);
	        params.put("file_snum", Integer.parseInt(file_snum));        
	        fileMap = tmserpScheduling.searchFileInfo(params);

	        if (fileMap == null) {
	        	throw new Exception();
	        }
			String uploadBasePath =  environment.getProperty("file.upload.directory");

			File downloadFile = new File (uploadBasePath + "/" + fileMap.getAttch_file_path(), fileMap.getVirtual_attch_file_name());
			int fSize = (int) downloadFile.length();

			if (fSize > 0) {
				
				String fullPath = downloadFile.getAbsolutePath();

				// get absolute path of the application
	            ServletContext context = request.getSession().getServletContext();

	            inputStream = new BufferedInputStream(new FileInputStream(downloadFile));

	            // get MIME type of the file
	            String mimeType = context.getMimeType(fullPath);
	            if (mimeType == null) {
	                // set to binary type if MIME mapping not found
	                mimeType = "application/octet-stream";
	            }

				response.setBufferSize(fSize);
				response.setContentType("utf-8");
				response.setContentType("application/octet;charset=utf-8");
				response.setContentType(mimeType);

				String disposition = getDisposition(fileMap.getReal_attch_file_name(), getBrowser(request), "", mimeType);
				response.addHeader("Content-disposition", disposition);
				response.setContentLength(fSize);


				// get output stream of the response
	            outStream = response.getOutputStream();

	            byte[] buffer = new byte[8*1024];
	            int bytesRead = -1;

	            // write bytes read from the input stream into the output stream
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outStream.write(buffer, 0, bytesRead);
	            }
	            inputStream.close();
	            outStream.flush();
			} else {
				//예외처리
				throw new Exception();
			}
		} catch(Exception e) {
			throw new Exception();
		} finally {
		}
	}
		
	private String getBrowser(HttpServletRequest request) {
	        String header = request.getHeader("User-Agent");
	        if (header.indexOf("MSIE") > -1) {
	            return "MSIE";
	        } else if (header.indexOf("Chrome") > -1) {
	            return "Chrome";
	        } else if (header.indexOf("Opera") > -1) {
	            return "Opera";
	        } else if (header.indexOf("Trident/7.0") > -1){ //IE 11 이상 //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8) return "MSIE";
	        	return "MSIE";
	        }
	        return "Firefox";
	    }
	
    private String getDisposition(String filename, String browser, String requestType, String mimeType) throws Exception {

    	String dispositionPrefix = String.format("attachment; filename=", filename);
        if (requestType.equals("inline")) {
            if (!mimeType.equals("application/octet-stream")) {
            	dispositionPrefix = String.format("inline; filename=", filename);
            }
        }
        String encodedFilename = "";
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new UnsupportedOperationException("Not supported browser");
        }
        return dispositionPrefix + encodedFilename;
    }
    
	@ApiOperation(value="/getMigyeolReportInfo", notes="tms미결현황요약정보")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getMigyeolReportInfo")
	@RequestMapping(value="/getMigyeolReportInfo",method=RequestMethod.GET)
	public String getMigyeolReportInfo(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "from_dt", required = true, example = "20211003")
			@RequestParam(name = "from_dt", required = true) String from_dt,
			@ApiParam(value = "to_dt", required = true, example = "20211013")
			@RequestParam(name = "to_dt", required = true) String to_dt
			) throws Exception {
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPTeamMigyeolRepo> teamMigyeolRepoList = null;
		TMSERPTeamMigyeolRepo migyeolInfo = null;
		TMSERPAllMigyeolRepo allInfo = new TMSERPAllMigyeolRepo();
		HashMap<Object, Object> result = new HashMap();
		
		int tot_cnt = 0;
		int migyeol_cnt = 0;
		int comp_cnt = 0;
		int com_unpsec_a = 0;
		int com_unpsec_e = 0;
		int com_unpsec_c = 0;
		int com_unpsec_r = 0;
		int comp_per = 0;
		int migyeol_per = 0;
		int com_unpsec_a_per = 0;
		int com_unpsec_e_per = 0;
		int com_unpsec_c_per = 0;
		int com_unpsec_r_per = 0;
		
		int all_tot_cnt = 0;
		int all_migyeol_cnt = 0;
		int all_comp_cnt = 0;
		int all_com_unpsec_a = 0;
		int all_com_unpsec_e = 0;
		int all_com_unpsec_c = 0;
		int all_com_unpsec_r = 0;
		int all_comp_per = 0;
		int all_migyeol_per = 0;
		int all_com_unpsec_a_per = 0;
		int all_com_unpsec_e_per = 0;
		int all_com_unpsec_c_per = 0;
		int all_com_unpsec_r_per = 0;
		
		try {
			if (user != null) {
				UserEtc etc = getUserEtc(user);
				params.put("com_scd", etc.getCom_scd());
				params.put("ksti_cd", etc.getSti_cd());
			    //params.put("com_scd", "C16YA");
			    //params.put("ksti_cd", "YA601");
				params.put("from_dt", from_dt.replace("-", ""));
				params.put("to_dt", to_dt.replace("-", ""));
				
				teamMigyeolRepoList = tmserpScheduling.selectMigyeolReportInfo(params);
				
				for(int i=0; i<teamMigyeolRepoList.size(); i++) {					
					migyeolInfo = teamMigyeolRepoList.get(i);
					tot_cnt = migyeolInfo.getTot_cnt();
					com_unpsec_a = migyeolInfo.getCom_unpsec_a();
					com_unpsec_e = migyeolInfo.getCom_unpsec_e();
					com_unpsec_c = migyeolInfo.getCom_unpsec_c();
					com_unpsec_r = migyeolInfo.getCom_unpsec_r();
					
					migyeol_cnt = com_unpsec_a + com_unpsec_e + com_unpsec_c + com_unpsec_r;
					comp_cnt = tot_cnt - migyeol_cnt;
					
					all_tot_cnt += tot_cnt;
					all_migyeol_cnt += migyeol_cnt;
					all_comp_cnt += comp_cnt;
					all_com_unpsec_a += com_unpsec_a;
					all_com_unpsec_e += com_unpsec_e;
					all_com_unpsec_c += com_unpsec_c;
					all_com_unpsec_r += com_unpsec_r;
										
					if(tot_cnt > 0) {
						
						//migyeol_per = (double)migyeol_cnt/(double)tot_cnt*100;
						migyeol_per = calcPercentage(migyeol_cnt, tot_cnt);
						//comp_per = (double)comp_cnt/(double)tot_cnt*100;
						comp_per = 100 - migyeol_per;
					}
					
					if(migyeol_cnt > 0) {
						//com_unpsec_a_per = Math.round((double)com_unpsec_a/(double)migyeol_cnt*100);
						//com_unpsec_e_per = Math.round((double)com_unpsec_e/(double)migyeol_cnt*100);
						//com_unpsec_c_per = Math.round((double)com_unpsec_c/(double)migyeol_cnt*100);
						com_unpsec_a_per = calcPercentage(com_unpsec_a, migyeol_cnt);
						com_unpsec_e_per = calcPercentage(com_unpsec_e, migyeol_cnt);
						com_unpsec_c_per = calcPercentage(com_unpsec_c, migyeol_cnt);
						//com_unpsec_r_per = (double)com_unpsec_r/(double)migyeol_cnt*100;
						com_unpsec_r_per = 100 - com_unpsec_a_per - com_unpsec_e_per - com_unpsec_c_per;
					}
					
					migyeolInfo.setMigyeol_cnt(migyeol_cnt);
					migyeolInfo.setComp_cnt(comp_cnt);
					migyeolInfo.setMigyeol_per(migyeol_per);
					migyeolInfo.setComp_per(comp_per);
					migyeolInfo.setCom_unpsec_a_per(com_unpsec_a_per);
					migyeolInfo.setCom_unpsec_c_per(com_unpsec_c_per);
					migyeolInfo.setCom_unpsec_e_per(com_unpsec_e_per);
					migyeolInfo.setCom_unpsec_r_per(com_unpsec_r_per);
					
				}
				if(all_tot_cnt > 0) {
					//all_migyeol_per = (double)all_migyeol_cnt/(double)all_tot_cnt*100;
					all_migyeol_per = calcPercentage(all_migyeol_cnt, all_tot_cnt);
					//all_comp_per = (double)all_comp_cnt/(double)all_tot_cnt*100;	
					all_comp_per = 100 - all_migyeol_per;
				}

				if(all_migyeol_cnt > 0) {
					//all_com_unpsec_a_per = (double)all_com_unpsec_a/(double)all_migyeol_cnt*100;
					//all_com_unpsec_e_per = (double)all_com_unpsec_e/(double)all_migyeol_cnt*100;
					//all_com_unpsec_c_per = (double)all_com_unpsec_c/(double)all_migyeol_cnt*100;
					//all_com_unpsec_r_per = (double)all_com_unpsec_r/(double)all_migyeol_cnt*100;
					all_com_unpsec_a_per = calcPercentage(all_com_unpsec_a, all_migyeol_cnt);
					all_com_unpsec_e_per = calcPercentage(all_com_unpsec_e, all_migyeol_cnt);
					all_com_unpsec_c_per = calcPercentage(all_com_unpsec_c, all_migyeol_cnt);
					all_com_unpsec_r_per = 100 - all_com_unpsec_a_per - all_com_unpsec_e_per - all_com_unpsec_c_per;
				}
				
				allInfo.setCom_unpsec_a(all_com_unpsec_a);
				allInfo.setCom_unpsec_c(all_com_unpsec_c);
				allInfo.setCom_unpsec_e(all_com_unpsec_e);
				allInfo.setCom_unpsec_r(all_com_unpsec_r);
				allInfo.setTot_cnt(all_tot_cnt);
				allInfo.setMigyeol_cnt(all_migyeol_cnt);
				allInfo.setComp_cnt(all_comp_cnt);
				allInfo.setMigyeol_per(all_migyeol_per);
				allInfo.setComp_per(all_comp_per);
				allInfo.setCom_unpsec_a_per(all_com_unpsec_a_per);
				allInfo.setCom_unpsec_c_per(all_com_unpsec_c_per);
				allInfo.setCom_unpsec_e_per(all_com_unpsec_e_per);
				allInfo.setCom_unpsec_r_per(all_com_unpsec_r_per);	
				
				result.put("allInfo", allInfo);
				result.put("info", teamMigyeolRepoList);
				
				return gson.toJson(result);	
			} else {
				//return gson.toJson(result);
				throw new Exception();
			}			
		} catch(Exception e) {
			//return gson.toJson(result);
			e.printStackTrace();
			throw new Exception();
		} 
	} 
	
	private int calcPercentage(int a, int b) {
		return (int) Math.round((double)a/(double)b*100);	
	}
	
	@ApiOperation(value="/getMigyeolnfo", notes="tms미결정보조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getMigyeolnfo")
	@RequestMapping(value="/getMigyeolnfo",method=RequestMethod.GET)
	public String getMigyeolnfo(
			@AuthenticationPrincipal User user,
			@ApiParam(value = "from_dt", required = true, example = "20211003")
			@RequestParam(name = "from_dt", required = true) String from_dt,
			@ApiParam(value = "to_dt", required = true, example = "20211013")
			@RequestParam(name = "to_dt", required = true) String to_dt,
			@ApiParam(value = "sti_cd", required = true, example = "YA611")
			@RequestParam(name = "sti_cd", required = true) String sti_cd
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPMigyeolInfo> migyeolInfoList = null;
		
		try {

			if (user != null) {
				UserEtc etc = getUserEtc(user);
				params.put("com_scd", etc.getCom_scd());
				params.put("sti_cd", sti_cd);
			    //params.put("com_scd", "C16YA");
				params.put("from_dt", from_dt.replace("-", ""));
				params.put("to_dt", to_dt.replace("-", ""));				
				migyeolInfoList = tmserpScheduling.selectMigyeolInfo(params);
				return gson.toJson(migyeolInfoList);				
			} else {
				throw new Exception();
			}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getMigyeoDetaillnfo", notes="tms미결상세정보조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getMigyeoDetaillnfo")
	@RequestMapping(value="/getMigyeoDetaillnfo",method=RequestMethod.GET)
	public String getMigyeoDetaillnfo(
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "plm_no", required = true, example = "I202111010031")
			@RequestParam(name = "plm_no", required = true) String plm_no
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPMigyeolDetailInfo> migyeolInfoList = null;
		
		try {
			//if (user != null) {
				params.put("plm_no", plm_no);
				migyeolInfoList = tmserpScheduling.selectMigyeolDetailInfo(params);
				return gson.toJson(migyeolInfoList);				
			//} else {
			//	throw new Exception();
			//}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getDefectInfoList", notes="tms하자내역조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getDefectInfoList")
	@RequestMapping(value="/getDefectInfoList",method=RequestMethod.GET)
	public String getDefectInfoList(
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "from_dt", required = true, example = "20211001")
			@RequestParam(name = "from_dt", required = true) String from_dt,
			@ApiParam(value = "to_dt", required = true, example = "20211019")
			@RequestParam(name = "to_dt", required = true) String to_dt,
			@ApiParam(value = "ctm_nm", required = false, example = "")
			@RequestParam(name = "ctm_nm", required = false) String ctm_nm
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPDefectInfo> defectInfoList = null;
		
		try {
			//if (user != null) {
				//UserEtc etc = getUserEtc(user);
				//params.put("com_scd", etc.getCom_scd());
				//params.put("ksti_cd", etc.getSti_cd());
				params.put("com_scd", "C16YA");
				params.put("ksti_cd", "YA601");
				params.put("from_dt", from_dt.replace("-", ""));
				params.put("to_dt", to_dt.replace("-", ""));
				params.put("ctm_nm", ctm_nm);
				defectInfoList = tmserpScheduling.selectDefectInfoList(params);
				return gson.toJson(defectInfoList);				
			//} else {
			//	throw new Exception();
			//}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getDefectDetail", notes="tms하자내역상세조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@GetMapping("/getDefectDetail")
	@RequestMapping(value="/getDefectDetail",method=RequestMethod.GET)
	public String getDefectDetail(
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "rpt_no", required = true, example = "I202109290263")
			@RequestParam(name = "rpt_no", required = true) String rpt_no,
			@ApiParam(value = "rpt_seq", required = true, example = "01")
			@RequestParam(name = "rpt_seq", required = true) String rpt_seq
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPDefectDetail> defectDetail = null;
		
		try {
			//if (user != null) {
				params.put("rpt_no", rpt_no);
				params.put("rpt_seq", rpt_seq);
				defectDetail = tmserpScheduling.selectDefectDetail(params);
				return gson.toJson(defectDetail);				
			//} else {
			//	throw new Exception();
			//}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/saveOpinion", notes="tms첨부파일 다운로드")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "") })
	@PostMapping("/saveOpinion")
	@RequestMapping(value="/saveOpinion",method=RequestMethod.POST)
	public String saveOpinion(HttpServletRequest request,  
			HttpServletResponse response,
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "rpt_no", required = true, example = "I202109290263")
			@RequestParam(name = "rpt_no", required = true) String rpt_no,
			@ApiParam(value = "rpt_seq", required = true, example = "01")
			@RequestParam(name = "rpt_seq", required = true) String rpt_seq,
			@ApiParam(value = "opinion", required = true, example = "이의!제기내용을 수정합니다.")
			@RequestParam(name = "opinion", required = true) String opinion		
			) throws Exception {
		HashMap<Object, Object> result = new HashMap();    
		try {
			result.put("response", "200");
			return gson.toJson(result);
		} catch(Exception e) {
			throw new Exception();
		} finally {
			return gson.toJson(result);
		}
	}
	
	
	@ApiOperation(value="/getStiMemberInfo", notes="시공팀원정보현황 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀원정보현황 조회 실패") })
	@GetMapping("/getStiMemberInfo")
	@RequestMapping(value="/getStiMemberInfo",method=RequestMethod.GET)
	public String getStiMemberInfo(
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "k_sti_cd", required = true, example = "YA521")
			@RequestParam(name = "k_sti_cd", required = true) String k_sti_cd
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPStimemberInfo> stiMember = null;
		
		try {
			//if (user != null) {
				params.put("k_sti_cd", k_sti_cd);
				stiMember = tmserpScheduling.selectStimemberInfo(params);
				return gson.toJson(stiMember);				
			//} else {
			//	throw new Exception();
			//}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	@ApiOperation(value="/getStiMemberDetailInfo", notes="시공팀원정보Detail현황 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀원정보Detail현황 조회 실패") })
	@GetMapping("/getStiMemberDetailInfo")
	@RequestMapping(value="/getStiMemberDetailInfo",method=RequestMethod.GET)
	public String getStiMemberDetailInfo(
			//@AuthenticationPrincipal User user,
			@ApiParam(value = "sti_cd", required = true, example = "YA521")
			@RequestParam(name = "sti_cd", required = true) String sti_cd,
			@ApiParam(value = "com_scd", required = true, example = "C16YA")
			@RequestParam(name = "com_scd", required = true) String com_scd		
			) throws Exception {
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		ArrayList<TMSERPStimemberDetailInfo> stiDetailMember = null;
		
		try {
			//if (user != null) {
				params.put("sti_cd", sti_cd);
				params.put("com_scd", com_scd);
				stiDetailMember = tmserpScheduling.selectStimemberDetailInfo(params);
				return gson.toJson(stiDetailMember);				
			//} else {
			//	throw new Exception();
			//}			
		} catch(Exception e) {
			throw new Exception();
		}
	}
	
	
	@ApiOperation(value = "tmserp_updatestimemberinfo", notes = "시공팀원정보 수정")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀원정보 수정 실패 !!") })
	@GetMapping("/tmserp_updatestimemberinfo")
	@RequestMapping(value = "/tmserp_updatestimemberinfo", method = RequestMethod.GET)
	public String tmserp_updatestimemberinfo(
			@ApiParam(value = "sti_cd", required = true, example = "YA521")
			@RequestParam(name = "sti_cd", required = true) String sti_cd,
			@ApiParam(value = "com_scd", required = true, example = "C16YA")
			@RequestParam(name = "com_scd", required = true) String com_scd,			
			@ApiParam(value = "stm_no", required = true, example = "01")
			@RequestParam(name = "stm_no", required = true) String stm_no,
			@ApiParam(value = "stm_nm", required = true, example = "황성현팀")
			@RequestParam(name = "stm_nm", required = true) String stm_nm,
			@ApiParam(value = "stm_hp", required = true, example = "010-0000-0000")
			@RequestParam(name = "stm_hp", required = true) String stm_hp,		
			@ApiParam(value = "stm_mdt", required = true, example = "164라7028")
			@RequestParam(name = "stm_mdt", required = true) String stm_mdt,
			@ApiParam(value = "stm_zdt", required = true, example = "20211211")
			@RequestParam(name = "stm_zdt", required = true) String stm_zdt,		
			@ApiParam(value = "com_pos", required = true, example = "C112")
			@RequestParam(name = "com_pos", required = true) String com_pos,			
			@ApiParam(value = "stm_addr", required = true, example = "서울 서울 서울 ")
			@RequestParam(name = "stm_addr", required = true) String stm_addr					
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		int res = 0;
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("stm_no", stm_no);
			params.put("stm_nm", stm_nm);		
			params.put("stm_hp", stm_hp);		
			params.put("stm_mdt", stm_mdt);		
			params.put("stm_zdt", stm_zdt);		
			params.put("com_pos", com_pos);		
			params.put("stm_addr", stm_addr);		
			
	    	res = tmserpScheduling.updateStimemberInfo(params);
	    	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateStimemberInfo 오류");
	        	return gson.toJson(response);
			}				
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "tmserp_insertstimemberinfo", notes = "시공팀원정보 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀원정보 등록 실패 !!") })
	@GetMapping("/tmserp_insertstimemberinfo")
	@RequestMapping(value = "/tmserp_insertstimemberinfo", method = RequestMethod.GET)
	public String tmserp_insertstimemberinfo(
			@ApiParam(value = "sti_cd", required = true, example = "YA521")
			@RequestParam(name = "sti_cd", required = true) String sti_cd,
			@ApiParam(value = "com_scd", required = true, example = "C16YA")
			@RequestParam(name = "com_scd", required = true) String com_scd,				
			@ApiParam(value = "stm_nm", required = true, example = "황성현팀")
			@RequestParam(name = "stm_nm", required = true) String stm_nm,
			@ApiParam(value = "stm_hp", required = true, example = "010-0000-0000")
			@RequestParam(name = "stm_hp", required = true) String stm_hp,		
			@ApiParam(value = "stm_mdt", required = true, example = "164라7028")
			@RequestParam(name = "stm_mdt", required = true) String stm_mdt,
			@ApiParam(value = "stm_zdt", required = true, example = "20211211")
			@RequestParam(name = "stm_zdt", required = true) String stm_zdt,		
			@ApiParam(value = "com_pos", required = true, example = "C112")
			@RequestParam(name = "com_pos", required = true) String com_pos,			
			@ApiParam(value = "stm_addr", required = true, example = "서울 서울 서울 ")
			@RequestParam(name = "stm_addr", required = true) String stm_addr					
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		int res = 0;
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("stm_nm", stm_nm);		
			params.put("stm_hp", stm_hp);		
			params.put("stm_mdt", stm_mdt);		
			params.put("stm_zdt", stm_zdt);		
			params.put("com_pos", com_pos);		
			params.put("stm_addr", stm_addr);		
			
	    	res = tmserpScheduling.insertStimemberInfo(params);
	    	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertStimemberInfo 오류");
	        	return gson.toJson(response);
			}				
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}	
	
	@ApiOperation(value = "tmserp_deletestimemberinfo", notes = "시공팀원정보 삭제")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀원정보 삭제 실패 !!") })
	@GetMapping("/tmserp_deletestimemberinfo")
	@RequestMapping(value = "/tmserp_deletestimemberinfo", method = RequestMethod.GET)
	public String tmserp_deletestimemberinfo(
			@ApiParam(value = "sti_cd", required = true, example = "YA521")
			@RequestParam(name = "sti_cd", required = true) String sti_cd,
			@ApiParam(value = "com_scd", required = true, example = "C16YA")
			@RequestParam(name = "com_scd", required = true) String com_scd,		
			@ApiParam(value = "stm_no", required = true, example = "01")
			@RequestParam(name = "stm_no", required = true) String stm_no			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		int res = 0;
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("stm_no", stm_no);			
			
	    	res = tmserpScheduling.deleteStimemberInfo(params);
	    	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("deleteStimemberInfo 오류");
	        	return gson.toJson(response);
			}				
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}		
}
