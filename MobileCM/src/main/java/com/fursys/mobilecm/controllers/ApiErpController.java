package com.fursys.mobilecm.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fursys.mobilecm.config.ServerInfo;
import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.lib.MobileCMLib;
import com.fursys.mobilecm.mapper.LoadingOrmMapper;
import com.fursys.mobilecm.mapper.MobileCMLibMapper;
import com.fursys.mobilecm.mapper.MyPageMapper;
import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpAsAddInfoInsertMapper;
import com.fursys.mobilecm.mapper.ErpAsPaymentMapper;
import com.fursys.mobilecm.mapper.ErpCalculateMoneyMapper;
import com.fursys.mobilecm.mapper.ErpCommMapper;
import com.fursys.mobilecm.mapper.ErpPraAcctListSaveMapper;
import com.fursys.mobilecm.mapper.LoadingOrmDtlMapper;
import com.fursys.mobilecm.mapper.ORMMapper;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.mapper.ScheduleMainListMapper;
import com.fursys.mobilecm.mapper.StiSituationManageMapper;
import com.fursys.mobilecm.mapper.TMSERPSchedulingMapper;
import com.fursys.mobilecm.mapper.TestMapper;
import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.service.impl.ApiErpServiceImpl;
import com.fursys.mobilecm.mapper.MigeulListMapper;
import com.fursys.mobilecm.utils.ComParamLogger;
import com.fursys.mobilecm.utils.OSValidator;
import com.fursys.mobilecm.utils.RestService;
import com.fursys.mobilecm.utils.SpcAPI;
import com.fursys.mobilecm.utils.StringUtil;
import com.fursys.mobilecm.utils.RestService.RestServiceCallBack;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.Coordinate;
import com.fursys.mobilecm.vo.CoordinateWrapper;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.GeoLocation;
import com.fursys.mobilecm.vo.UserEtc;
import com.fursys.mobilecm.vo.erp.ERPAgtInfo;
import com.fursys.mobilecm.vo.erp.ERPAsCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPAsFileList;
import com.fursys.mobilecm.vo.erp.ERPAsItemPage;
import com.fursys.mobilecm.vo.erp.ERPAsMainPage;
import com.fursys.mobilecm.vo.erp.ERPAsResDtlList;
import com.fursys.mobilecm.vo.erp.ERPMigeulList;
import com.fursys.mobilecm.vo.erp.ERPNetKmAllowance;
import com.fursys.mobilecm.vo.erp.ERPAsResult;
import com.fursys.mobilecm.vo.erp.ERPBusinessTrip;
import com.fursys.mobilecm.vo.erp.ERPBusinessTripDetail;
import com.fursys.mobilecm.vo.erp.ERPCenterList;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemPage;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemResultPage;
import com.fursys.mobilecm.vo.erp.ERPConstructionMainPage;
import com.fursys.mobilecm.vo.erp.ERPCooperationList;
import com.fursys.mobilecm.vo.erp.ERPItemOrd;
import com.fursys.mobilecm.vo.erp.ERPItemOrdSummary;
import com.fursys.mobilecm.vo.erp.ERPMigeulAverage;
import com.fursys.mobilecm.vo.erp.ERPMigeulDetailList;
import com.fursys.mobilecm.vo.erp.ERPOrdLdList;
import com.fursys.mobilecm.vo.erp.ERPPaymentList;
import com.fursys.mobilecm.vo.erp.ERPPlanDtList;
import com.fursys.mobilecm.vo.erp.ERPPraNewRptNo;
import com.fursys.mobilecm.vo.erp.ERPReReserveInfo;
import com.fursys.mobilecm.vo.erp.ERPReqCooperationList;
import com.fursys.mobilecm.vo.erp.ERPResultFinishynCheck;
import com.fursys.mobilecm.vo.erp.ERPScheduleCount;
import com.fursys.mobilecm.vo.erp.ERPSti;
import com.fursys.mobilecm.vo.erp.ERPStiBoardList;
import com.fursys.mobilecm.vo.erp.ERPStiPlanSituationList;
import com.fursys.mobilecm.vo.erp.ERPStiReqTimeSendList;
import com.fursys.mobilecm.vo.erp.ERPStiSituationCount;
import com.fursys.mobilecm.vo.erp.ERPStiSituationInfoList;
import com.fursys.mobilecm.vo.erp.ERPStmInfo;
import com.fursys.mobilecm.vo.erp.ERPTeamList;
import com.fursys.mobilecm.vo.erp.ERPTestVo;
import com.fursys.mobilecm.vo.erp.ERPGubbunInfo;
import com.fursys.mobilecm.vo.erp.ERPTtComcd;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_M01;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_P02;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_P03;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_btrip;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_tabList1;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_uptplandtl;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList1;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.mobile.response.BusinessTripResponse;
import com.fursys.mobilecm.vo.mobile.response.MyPageResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongResultResponse;
import com.fursys.mobilecm.vo.mobile.response.SpcResponse;
import com.fursys.mobilecm.vo.mobile.response.StiBoardDetailResponse;
import com.fursys.mobilecm.vo.erp.ERPScheduleList;
import com.fursys.mobilecm.vo.erp.ERPSearchTeamList;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoneyTeam;
import com.fursys.mobilecm.vo.erp.ERPSigongSearchDetailInfo;
import com.fursys.mobilecm.vo.erp.ERPSigongSearchInfo;
import com.fursys.mobilecm.vo.erp.ERPSigongStmList;
import com.fursys.mobilecm.vo.tms.TmsOrderBase;
import com.fursys.mobilecm.vo.tms.TmsRouteOptimization;
import com.fursys.mobilecm.vo.tms.TmsViaPoints;
import com.fursys.mobilecm.vo.tms.TmsZoneBase;
import com.fursys.mobilecm.vo.tms.reponse.TmsCenterListResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsGeocodingCoordinateInfoResponse;
import com.fursys.mobilecm.vo.tms.reponse.TmsRouteOptimizationResponse;
import com.fursys.mobilecm.vo.tmserp.TMSERPCenterList;
import com.fursys.mobilecm.vo.tmserp.TMSERPScheduleCount;
import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/api/erp")
public class ApiErpController {
	@Autowired private ApiTmsController mApiTmsController;
	@Autowired ApiTmsErpController apiTmsErpController;	
	@Autowired TMSERPSchedulingMapper tmserpScheduling;
	@Autowired ErpCommMapper erpcommMapper;
	@Autowired CRS0010_M01Mapper crs0010_p01Mapper;
	@Autowired ScheduleMainListMapper schedulemainlistMapper;
	@Autowired ErpAsAddInfoInsertMapper erpAsAddInfoInsertMapper;
	@Autowired ErpPraAcctListSaveMapper erppraacctlistsaveMapper;
	@Autowired MyPageMapper mypageMapper;
	@Autowired ORMMapper oRMMapper;
	@Autowired STIMapper sTIMapper;
	@Autowired LoadingOrmMapper lOADINGORMMapper;	
	@Autowired LoadingOrmDtlMapper lOADINGORMDtlMapper;
	@Autowired ScheduleMainListMapper sCheduleMainListMapper;	
	@Autowired MigeulListMapper mIgeulListMapper ;
	@Autowired ErpAsPaymentMapper erpAsPaymentMapper;
	@Autowired StiSituationManageMapper erpStiSituationMapper;
	@Autowired ErpCalculateMoneyMapper erpCalculateMoneyMapper;
	@Autowired TestMapper tTestMapper;
	
	@Autowired ApiErpService apiErpService;
	@Autowired private PlatformTransactionManager txManager;
	
	Gson gson = new Gson();
	boolean	isDeBug = false;	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ApiOperation(value = "erp_SiGongMigeulResultSave", notes = "시공미결결과등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결결과등록 처리 실패 !!") })
	@GetMapping("/erp_SiGongMigeulResultSave")
	@RequestMapping(value = "/erp_SiGongMigeulResultSave", method = RequestMethod.GET)
	public String erp_SiGongMigeulResultSave(
			@RequestParam(name = "plm_no", required = false) String as_plm_no,
			@RequestParam(name = "user_id", required = false) String as_usr_id,
			@RequestParam(name = "mob_std", required = false) String mob_std,
			@RequestParam(name = "mob_remark", required = false) String mob_remark
			) {
		
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("usr_id", as_usr_id);
		params.put("mob_std", mob_std);
		params.put("mob_remark", mob_remark);

		response = apiErpService.erp_SiGongMigeulResultSave(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_requestGoGoVan", notes = "고고밴 요청")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "고고밴 요청 실패 !!") })
	@GetMapping("/erp_requestGoGoVan")
	@RequestMapping(value = "/erp_requestGoGoVan", method = RequestMethod.GET)
	public String erp_requestGoGoVan(
			@ApiParam(value="REM_DT", required = true, example = "20201220")
			@RequestParam(name="rem_dt", required = true) String rem_dt,
			@ApiParam(value="REM_SEQ", required = true, example = "01")
			@RequestParam(name="rem_seq", required = true) String rem_seq,
			@ApiParam(value="픽업요청시간(ISO DATETIME)", required = true, example = "2020-12-22T09:30:00+09:00")
			@RequestParam(name="appointment_at", required = true) String appointment_at,
			@ApiParam(value="요청자", required = true, example = "SYSTEM")
			@RequestParam(name="requestor", required = true) String requestor,
			@ApiParam(value="요청자 연락처", required = true, example = "00000000000")
			@RequestParam(name="requestor_telno", required = true) String requestor_telno,
			@ApiParam(value="요청차량", required = true, example = "1 톤")
			@RequestParam(name="vehicle", required = true) String vehicle,
			@ApiParam(value="결재방식", required = true, example = "신용 결제")
			@RequestParam(name="pay", required = true) String pay,
			@ApiParam(value="메모", required = true, example = "")
			@RequestParam(name="remark", required = true) String remark,
			@ApiParam(value="이름", required = true, example = "홍길동,이선달")
			@RequestParam(name="name_arr", required = true) String name_arr,
			@ApiParam(value="도착지주소", required = true, example = "경기도 용인시,경기도 안양시")
			@RequestParam(name="addr_arr", required = true) String addr_arr,
			@ApiParam(value="휴대폰", required = true, example = "00012345678,00011112222")
			@RequestParam(name="mobile_no_arr", required = true) String mobile_no_arr,
			@ApiParam(value="도착시간(ISO DATETIME)", required = true, example = "2020-12-22T09:30:00+09:00,2020-12-22T09:30:00+09:00")
			@RequestParam(name="request_at_arr", required = true) String request_at_arr,			
			@ApiParam(value="회사명", required = true, example = "퍼시스,일룸")
			@RequestParam(name="company_arr", required = true) String company_arr,
			@ApiParam(value="부서(팀)", required = true, example = "총무팀,자재팀")
			@RequestParam(name="team_arr", required = true) String team_arr,
			@ApiParam(value="메모", required = true, example = "픽업 시간 준수 요청,")
			@RequestParam(name="description_arr", required = true) String description_arr
			) {

		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("rem_dt", rem_dt);
		params.put("rem_seq", rem_seq);
		params.put("appointment_at", appointment_at);
		params.put("requestor", requestor);
		params.put("requestor_telno", requestor_telno);
		params.put("vehicle", vehicle);
		params.put("pay", pay);
		params.put("remark", remark);
		params.put("name_arr", name_arr);
		params.put("addr_arr", addr_arr);
		params.put("mobile_no_arr", mobile_no_arr);
		params.put("request_at_arr", request_at_arr);
		params.put("company_arr", company_arr);
		params.put("team_arr", team_arr);
		params.put("description_arr", description_arr);

		response = apiErpService.erp_requestGoGoVan(params);

		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_asCalculateMoney", notes = "AS정산내역")
	@GetMapping("/erp_asCalculateMoney")  
	public String erp_asCalculateMoney (
			@ApiParam(value = "검색시작일자", required=true, example = "20201001")
			@RequestParam(name="fr_dt", required=true) String as_fr_dt,			
			@ApiParam(value = "검색종료일자", required=true, example = "20201210")
			@RequestParam(name="to_dt", required=true) String as_to_dt,			
			@ApiParam(value = "AS팀코드", required=true, example = "YA707")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("fr_dt", as_fr_dt);
        params.put("to_dt", as_to_dt);
        params.put("sti_cd", as_sti_cd);
		
        ERPAsCalculateMoney item = apiErpService.erp_selectAsCalculateMoney(params);
        
		return gson.toJson(item);
	}	
	
	@ApiOperation(value = "erp_sigongCalculateMoneyTeam", notes = "팀시공정산내역")
	@GetMapping("/erp_sigongCalculateMoneyTeam")  
	public String erp_sigongCalculateMoneyTeam (
			@ApiParam(value = "권역장코드", required=true, example = "YA521")
			@RequestParam(name="k_sti_cd", required=true) String as_k_sti_cd,
			@ApiParam(value = "년월", required=true, example = "202012")
			@RequestParam(name="yyyymm", required=true) String as_yyyymm
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("k_sti_cd", as_k_sti_cd);
        params.put("yyyymm", as_yyyymm);
        
        ArrayList<ERPSigongCalculateMoneyTeam> allItems = apiErpService.erp_selectSigongCalculateMoneyTeam(params);		
		        
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_sigongCalculateMoney", notes = "시공정산내역")
	@GetMapping("/erp_sigongCalculateMoney")  
	public String erp_sigongCalculateMoney (
			@ApiParam(value = "검색시작일자", required=true, example = "20201001")
			@RequestParam(name="fr_dt", required=true) String as_fr_dt,			
			@ApiParam(value = "검색종료일자", required=true, example = "20201210")
			@RequestParam(name="to_dt", required=true) String as_to_dt,
			@ApiParam(value = "권역시공팀코드", required=false, example = "YA521")
			@RequestParam(name="k_sti_cd", required=false) String as_k_sti_cd,
			@ApiParam(value = "시공팀코드", required=false, example = "YA521")
			@RequestParam(name="sti_cd", required=false) String as_sti_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("fr_dt", as_fr_dt);
        params.put("to_dt", as_to_dt);
        params.put("k_sti_cd", as_k_sti_cd);
        params.put("sti_cd", as_sti_cd);
		
        ERPSigongCalculateMoney item = apiErpService.erp_sigongCalculateMoney(params);
        
		return gson.toJson(item);
	}	
	
	@ApiOperation(value = "erp_selectPaymentList", notes = "결제내역")
	@GetMapping("/erp_selectPaymentList")  
	public String erp_selectPaymentList (
			@ApiParam(value = "RPT NO", required=true, example = "I202011060112")
			@RequestParam(name="rpt_no", required=true) String as_rpt_no,			
			@ApiParam(value = "RPT SEQ", required=true, example = "02")
			@RequestParam(name="rpt_seq", required=true) String as_rpt_seq
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("rpt_no", as_rpt_no);
        params.put("rpt_seq", as_rpt_seq);
        
        ArrayList<ERPPaymentList> allItems = erpAsPaymentMapper.selectPaymentList(params);		
		
		return gson.toJson(allItems);
	}	
		
	@ApiOperation(value = "erp_updateBusinessTrip", notes = "출장비 요청")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "출장비 요청 실패 !!") })
	@GetMapping("/erp_updateBusinessTrip")
	@RequestMapping(value = "/erp_updateBusinessTrip", method = RequestMethod.GET)
	public String erp_updateBusinessTrip(
			@ApiParam(value = "T맵키", required = true, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = true) String appKey,
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String com_scd,
			@ApiParam(value="시공팀코드", required = true, example = "YA521")
			@RequestParam(name="sti_cd", required = true) String sti_cd,
			@ApiParam(value="일자", required = true, example = "20201201")
			@RequestParam(name="rem_dt", required = true) String rem_dt,
			@ApiParam(value="사용자 아이디", required = true, example = "SYSTEM")
			@RequestParam(name="usr_cd", required = true) String usr_cd			
			) {

		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
		BusinessTripResponse response = new BusinessTripResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", com_scd);
		params.put("sti_cd", sti_cd);
		params.put("rem_dt", rem_dt);		
		
		try {
			ArrayList<TMSERPCenterList> arList = tmserpScheduling.selectCenterList_as(params);
            if (arList.size() < 1) {
            	txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("등록된 센터가 없습니다.");
				return gson.toJson(response);
			}
            String center_X = "", center_Y = "";
			TmsGeocodingCoordinateInfoResponse geocoding = apiTmsErpController.Geocoding(appKey, arList.get(0).getScd_gaddr());
            if (!"200".equals(geocoding.getResultCode())) {
            	txManager.rollback(status);
            	System.out.println(geocoding.getResultMessage());
            	response.setResultCode("5001");
				response.setResultMessage("센터가 GeoCoding 실패");
				return gson.toJson(response);
            }
            if (geocoding.getCoordinateInfo().getTotalCount() > 0) {
            	center_X = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon();
            	center_Y = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat();
    		}

			ArrayList<ERPBusinessTripDetail> list = erpCalculateMoneyMapper.selectBusinessTripDetail(params); 
			response.setList(list);
			int cnt = list.size();
			TmsRouteOptimization route = new TmsRouteOptimization();
			
//			route.setStartName(list.get(0).getOrm_nm());			
//			route.setStartX(list.get(0).getLongitude());
//			route.setStartY(list.get(0).getLatitude());
//			route.setStartTime(list.get(0).getMob_starttm());

			route.setStartName(arList.get(0).getCcd_nm());			
			route.setStartX(center_X);
			route.setStartY(center_Y);
			route.setStartTime(list.get(0).getMob_starttm());
			
			route.setEndName(list.get(cnt - 1).getOrm_nm());
			route.setEndX(list.get(cnt - 1).getLongitude());
			route.setEndY(list.get(cnt - 1).getLatitude());
			
			ArrayList<TmsViaPoints> viaPoints = new ArrayList<TmsViaPoints>();			
			for (int i=0; i<(cnt - (cnt == 1 ? 0 : 1)); i++) {
				if ("".equals(list.get(i).getLatitude()) || StringUtil.isEmpty(list.get(i).getLatitude())) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(list.get(i).getOrm_nm() + "님의 좌표정보가 없습니다.");
					return gson.toJson(response);
				}
				TmsViaPoints vo = new TmsViaPoints();
				vo.setViaPointId(list.get(i).getPlm_no());
				vo.setViaPointName(list.get(i).getOrm_nm());
				vo.setViaX(list.get(i).getLongitude());
				vo.setViaY(list.get(i).getLatitude());
				vo.setViaTime("600");
				vo.setViaDetailAddress("");
				//vo.setWishStartTime(list.get(i).getMob_starttm());
				//vo.setWishStartTime(list.get(i).getMob_starttm());
				vo.setWishEndTime("");
				viaPoints.add(vo);
			}
			route.setViaPoints(viaPoints);
			
			System.out.println(gson.toJson(route));
			
			TmsRouteOptimizationResponse route_response = gson.fromJson(mApiTmsController.tms_routeSequential(appKey, route), TmsRouteOptimizationResponse.class);
			if (!"200".equals(route_response.getResultCode())) {
				txManager.rollback(status);
				System.out.println(route_response.getResultMessage());
				response.setResultCode("5001");
				response.setResultMessage(MobileCMLib.makeTmsMsg(route_response));
				return gson.toJson(response);
			}
			
			int move_km = Integer.valueOf(route_response.getProperties().getTotalDistance()) / 1000;						
			System.out.println(""+ move_km);
					
			//출장비 계산 로직 적용 -2021.04.13 HSH
			/**
			 * 
			 * 1) 50KM 미만 지급 없음
			 * 2) 50km ~ 99KM = 이동거리 * 금액 & 0.7
			 * 3) 100km ~ 149km = 이동거리 * 금액
			 * 4) 140km ~ 199km = 이동거리 * 금액 * 1.4
			 * 5) 200km ~ = 이동거리 * 금액 * 2
			 * 
			 * 통행비 * 2 지급
			 */
			int total_amt = 0;
			int compute_move_km = 0;
			
			if (move_km < 50) {
//				System.out.println("1111111111111");
				compute_move_km = 0 ;
			} else if (move_km >= 50  && move_km < 100) {
//				System.out.println("2222222222222");
				compute_move_km = (int) (move_km * 0.7) ;
			} else if (move_km >= 100 && move_km < 150) {
//				System.out.println("3333333333333");
				compute_move_km = (int) (move_km * 1) ;
			} else if (move_km >= 150 && move_km < 200) {
//				System.out.println("4444444444444");
				compute_move_km = (int) (move_km * 1.4) ;
//				System.out.println("5555555555555");
			} else if (move_km >= 200) {
//				System.out.println("6666666666666");
				compute_move_km = (int) (move_km * 2) ;
			} else {
//				System.out.println("777777777777");
				compute_move_km = (int) (move_km * 1) ;
			}

			params = new HashMap<String, Object>();
			params.put("com_scd", com_scd);
			params.put("sti_cd", sti_cd);
			params.put("rem_dt", rem_dt);
			params.put("usr_cd", usr_cd);
			params.put("move_km", ""+move_km);
			params.put("compute_move_km", compute_move_km);
			params.put("move_amt", "0");
			params.put("toll_fee", route_response.getProperties().getTotalFare());
			params.put("proc_status", "N");
				
			ERPBusinessTrip businesstrip = erpCalculateMoneyMapper.selectBusinessTrip(params);
			
			int res = 0;
			if (businesstrip == null) {
				res = erpCalculateMoneyMapper.insertBusinessTrip(params);
			} else {
				res = erpCalculateMoneyMapper.updateBusinessTrip(params);
			}
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertBusinessTrip 오류 [" + res + "]");
				return gson.toJson(response);
			}
			businesstrip = erpCalculateMoneyMapper.selectBusinessTrip(params);
			if (businesstrip == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectBusinessTrip 오류");
				return gson.toJson(response);
			}
			ERPScheduleCount schedulecount = erpCalculateMoneyMapper.selectBusinessTripScheduleCount(params);
			if (schedulecount != null) {
				businesstrip.setTotal_count(""+schedulecount.getTotal_cnt());
			}
			businesstrip.setTotal_move(""+list.size());
			
			response.setBusinesstrip(businesstrip);
			System.out.println(gson.toJson(route_response));
			System.out.println(gson.toJson(response));
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage("insertBusinessTrip 오류 [" + e.toString() + "]");
			return gson.toJson(response);
		}
		
		txManager.commit(status);
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_selectBusinessTrip", notes = "출장비 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "출장비 조회 실패 !!") })
	@GetMapping("/erp_selectBusinessTrip")
	@RequestMapping(value = "/erp_selectBusinessTrip", method = RequestMethod.GET)
	public String erp_selectBusinessTrip(
			@ApiParam(value = "T맵키", required = false, example = "l7xx965cfaee1f4c47608284f1271eccb662")
			@RequestParam(name = "appKey", required = false) String appKey,
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String com_scd,
			@ApiParam(value="시공팀코드", required = true, example = "YA521")
			@RequestParam(name="sti_cd", required = true) String sti_cd,
			@ApiParam(value="일자", required = true, example = "20201201")
			@RequestParam(name="rem_dt", required = true) String rem_dt,
			@ApiParam(value="사용자 아이디", required = true, example = "SYSTEM")
			@RequestParam(name="usr_cd", required = true) String usr_cd			
			) {

		BusinessTripResponse response = new BusinessTripResponse();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", com_scd);
		params.put("sti_cd", sti_cd);
		params.put("rem_dt", rem_dt);		
		
		try {						 
			ERPBusinessTrip businesstrip = erpCalculateMoneyMapper.selectBusinessTrip(params);
			if (businesstrip == null) {
				businesstrip = new ERPBusinessTrip(); 
			}
			
			ArrayList<ERPBusinessTripDetail> list = erpCalculateMoneyMapper.selectBusinessTripDetail(params); 
			response.setList(list);
			
			ERPScheduleCount schedulecount = erpCalculateMoneyMapper.selectBusinessTripScheduleCount(params);
			if (schedulecount != null) {
				businesstrip.setTotal_count(""+schedulecount.getTotal_cnt());
			}
			businesstrip.setTotal_move(""+list.size());
					
			response.setBusinesstrip(businesstrip);
			System.out.println(gson.toJson(response));
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(response);
	}
		
	@ApiOperation(value = "erp_searchTeamList", notes = "시공팀 검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀 검색 실패 !!") })
	@GetMapping("/erp_searchTeamList")
	@RequestMapping(value = "/erp_searchTeamList", method = RequestMethod.GET)
	public String erp_searchTeamList(
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String as_com_scd,
			@ApiParam(value="권역장코드", required = true, example = "YA142")
			@RequestParam(name="k_sti_cd", required = true) String as_k_sti_cd,
			@ApiParam(value="시공팀명", required = true, example = "정득수")
			@RequestParam(name="sti_nm", required = true) String as_sti_nm			
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", as_com_scd);
		params.put("k_sti_cd", as_k_sti_cd);
		params.put("sti_nm", "%" + as_sti_nm + "%");
		
		ArrayList<ERPSearchTeamList> allItems = erpcommMapper.erp_searchTeamList(params);
		
		try {
			ComParamLogger.paramToVO("ERPSearchTeamList", "ERPSearchTeamList", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);				
	}
		
	@ApiOperation(value = "erp_selectTeamList", notes = "시공팀 리스트 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀 리스트 조회 실패 !!") })
	@GetMapping("/erp_selectTeamList")
	@RequestMapping(value = "/erp_selectTeamList", method = RequestMethod.GET)
	public String erp_selectTeamList(
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String as_com_scd
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", as_com_scd);
		
		ArrayList<ERPTeamList> allItems = erpcommMapper.erp_selectTeamList(params);
		
		try {
			ComParamLogger.paramToVO("ERPTeamList", "ERPTeamList", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);				
	}
		
	@ApiOperation(value = "erp_selectTeamKManagerList", notes = "권역장 리스트 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀 리스트 조회 실패 !!") })
	@GetMapping("/erp_selectTeamKManagerList")
	@RequestMapping(value = "/erp_selectTeamKManagerList", method = RequestMethod.GET)
	public String erp_selectTeamKManagerList(
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String as_com_scd
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", as_com_scd);
		
		ArrayList<ERPTeamList> allItems = erpcommMapper.erp_selectTeamKManagerList(params);

		return gson.toJson(allItems);				
	}
	
	@ApiOperation(value = "erp_selectTeamListAll", notes = "시공팀 리스트 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀 리스트 조회 실패 !!") })
	@GetMapping("/erp_selectTeamListAll")
	@RequestMapping(value = "/erp_selectTeamListAll", method = RequestMethod.GET)
	public String erp_selectTeamListAll(
			@ApiParam(value="서비스센터코드", required = true, example = "C16YA")
			@RequestParam(name="com_scd", required = true) String as_com_scd
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", as_com_scd);
		
		ArrayList<ERPTeamList> allItems = erpcommMapper.erp_selectTeamListAll(params);
		
		try {
			ComParamLogger.paramToVO("ERPTeamList", "ERPTeamList", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);				
	}
	
	@ApiOperation(value = "erp_selectCenterList", notes = "서비스센터 리스트 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "서비스센터 리스트 조회 실패 !!") })
	@GetMapping("/erp_selectCenterList")
	@RequestMapping(value = "/erp_selectCenterList", method = RequestMethod.GET)
	public String erp_selectCenterList(
			@ApiParam(value="사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name="user_id", required = true) String as_usr_id
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", as_usr_id);
		
		ArrayList<ERPCenterList> allItems = erpcommMapper.erp_selectCenterList(params);
		
		try {
			ComParamLogger.paramToVO("ERPCenterList", "ERPCenterList", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);				
	}

	@ApiOperation(value = "erp_selectAsFileId", notes = "AS 첨부파일ID 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS 첨부파일ID조회 실패 !!") })
	@GetMapping("/erp_selectAsFileId")
	@RequestMapping(value = "/erp_selectAsFileId", method = RequestMethod.GET)
	public String erp_selectAsFileId(
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="rpt_no", required=true) String rpt_no,
			@RequestParam(name="rpt_seq", required=true) String rpt_seq			
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {			
			HashMap<String, Object> params;
			params = new HashMap<String, Object>();
			String str_fileId = "";
						
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			
			dataResult = erpAsAddInfoInsertMapper.selectAsFileID(params);			
			if (dataResult == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectAsFileID 오류");
				return gson.toJson(response);
			}
			
			str_fileId = dataResult.getData1();			
			if ("".equals(str_fileId)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("파일ID 정보가 없으므로, 사진 추가등록할 수 없습니다.");
				return gson.toJson(response);
			}
			
			params = new HashMap<String, Object>();
			params.put("file_id", str_fileId);
			params.put("rpt_no", rpt_no);
			params.put("rpt_seq", rpt_seq);
			
			res = erpAsAddInfoInsertMapper.updateAsFileId(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAsFileId 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			ERPAsResult asresult = new ERPAsResult();
			asresult.setFileid(str_fileId);
			response.setAsResult(asresult);
						
			ComParamLogger.paramToVO("ERPAsResult", "ERPAsResult", asresult);
						
			
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
	
	@ApiOperation(value = "erp_AsResultSave", notes = "AS결과 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS결과 등록 실패 !!") })
	@GetMapping("/erp_AsResultSave")
	@RequestMapping(value = "/erp_AsResultSave", method = RequestMethod.GET)
	public String erp_AsResultSave(
			@ApiParam(value="시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no,
			@ApiParam(value="특이사항", required = true, example = "")
			@RequestParam(name="remark", required = true) String as_remark,			
			@ApiParam(value="사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name="user_id", required = true) String as_usr_id,
			
			@ApiParam(value = "시공의뢰구분", required = true, example = "C090,C090")
			@RequestParam(name="com_pldsec_arr", required = true) String as_com_pldsec_arr,
			@ApiParam(value = "차수", required = true, example = "001,002")
			@RequestParam(name="ord_sseq_arr", required = true) String as_ord_sseq_arr,
			@ApiParam(value = "확정수량", required = true, example = "1,35")
			@RequestParam(name="pld_fqty_arr", required = true) String as_pld_fqty_arr			
			) {
		
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("remark", as_remark);
		params.put("usr_id", as_usr_id);		
		params.put("com_pldsec_arr", as_com_pldsec_arr);
		params.put("ord_sseq_arr", as_ord_sseq_arr);
		params.put("pld_fqty_arr", as_pld_fqty_arr);
		
		response = apiErpService.erp_AsResultSave(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}

	@ApiOperation(value = "erp_SiGongAddFileInfo", notes = "시공 사진경로 저장 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공 사진경로 저장 실패") })	
	@GetMapping("/erp_SiGongAddFileInfo")  
	@RequestMapping(value = "/erp_SiGongAddFileInfo", method = RequestMethod.POST)
	public String erp_SiGongAddFileInfo (
			@RequestParam(name="file_id", required=true) String file_id,
			@RequestParam(name="file_nm", required=true) String file_nm,
			@RequestParam(name="user_id", required=true) String user_id,
			@RequestParam(name="file1", required=true) MultipartFile file1
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			String fileName = "", baseDir = "", yyyymmdd = "";
			file_nm = file_id + "." + file_nm;
			
			HashMap<String, Object> params;
			params = new HashMap<String, Object>();
			params.put("file_id", file_id);
			params.put("file_nm", file_nm);
			params.put("user_id", user_id);
			
			if(!file1.isEmpty()){ //첨부파일이 존재하면
				//첨부파일의 이름								
				try{
					Path path = null;
					fileName = file1.getOriginalFilename();
					
					dataResult = erpAsAddInfoInsertMapper.searchToday(params);
					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("searchToday 오류");
						return gson.toJson(response);
					} else {
						yyyymmdd = dataResult.getData1();
					}						
					
					if (OSValidator.isWindows()) {
						baseDir = "C:\\ServerFiles\\" + yyyymmdd;
						fileName = baseDir + "\\"+ file_nm;
					} else {						
						baseDir = ServerInfo.SIGONG_ATTACHED_FILE_PATH + yyyymmdd;
						fileName = baseDir + "/"+ file_nm;						
					}
					path = Paths.get(baseDir);
					if (!Files.isDirectory(path)) { // 해당 디렉토리가 없다면
						Files.createDirectories(path); // 폴더 생성
					}
					//지정된 업로드 경로로 저장됨
					file1.transferTo(new File(fileName));
					
				} catch (Exception e) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("erp_SiGongAddFileInfo 사진UPLOAD 오류 [" + e.toString() + "]");
					return gson.toJson(response);
				}
			} else {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_SiGongAddFileInfo 오류 [첨부파일 정보가 존재하지 않습니다.]");
				return gson.toJson(response);
			}
			
			res = erpAsAddInfoInsertMapper.insertSiGongAddFileInfo(params);			
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertSiGongAddFileInfo 오류 [" + res + "]");
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

	@ApiOperation(value = "erp_SaveSign", notes = "서명 저장")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "서명 저장 실패") })	
	@GetMapping("/erp_SaveSign")  
	@RequestMapping(value = "/erp_SaveSign", method = RequestMethod.POST)		
	public String erp_SaveSign (
			@RequestParam(name ="com_ssec", required = true) String as_com_ssec,
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@RequestParam(name="rem_seq", required=true) String as_rem_seq,
			@RequestParam(name="file_id", required=true) String file_id,
			@RequestParam(name="file_nm", required=true) String file_nm,
			@RequestParam(name="user_id", required=true) String as_user_id,
			@RequestParam(name="file1", required=true) MultipartFile file1
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			String fileName = "", baseDir = "", yyyymmdd = "";
			file_nm = file_id + "." + file_nm;
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("REM_DT", as_rem_dt);
			params.put("REM_SEQ", as_rem_seq);
			
			res = crs0010_p01Mapper.updateSignDt(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("서명시간 등록에 오류가 발생했습니다. 다시 등록 해 주세요. [" + res + "]");
				return gson.toJson(response);
			}	

			dataResult = crs0010_p01Mapper.getSignDt(params);
    		if (dataResult != null) {
    			yyyymmdd = dataResult.getData1();
    		} else {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("서명시간 조회에 오류가 발생했습니다.");
				return gson.toJson(response);
    		}
    					
			if(!file1.isEmpty()){ //첨부파일이 존재하면
				//첨부파일의 이름								
				try{
					Path path = null;
					fileName = file1.getOriginalFilename();

					if (OSValidator.isWindows()) {
						baseDir = "C:\\ServerFiles\\_sign\\" + yyyymmdd;
						fileName = baseDir + "\\"+ file_nm;
					} else {
						if ("C18A".equals(as_com_ssec)) {
							baseDir = ServerInfo.AS_SIGN_FILE_PATH + yyyymmdd;
						} else {
							baseDir = ServerInfo.SIGONG_SIGN_FILE_PATH + yyyymmdd;
						}
						fileName = baseDir + "/"+ file_nm;						
					}
					path = Paths.get(baseDir);
					if (!Files.isDirectory(path)) { // 해당 디렉토리가 없다면
						Files.createDirectories(path); // 폴더 생성
					}
					//지정된 업로드 경로로 저장됨
					file1.transferTo(new File(fileName));
					
				} catch (Exception e) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("erp_SaveSign 서명UPLOAD 오류 [" + e.toString() + "]");
					return gson.toJson(response);
				}
			} else {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_SaveSign 오류 [서명파일 정보가 존재하지 않습니다.]");
				return gson.toJson(response);
			}
						
			params = new HashMap<String, Object>();
			params.put("file_id", file_id);
			params.put("file_nm", file_nm);
			params.put("user_id", as_user_id);
			
			if ("C18A".equals(as_com_ssec)) {
				res = erpAsAddInfoInsertMapper.insertAsAddSignFileInfo(params);
			} else {
				res = erpAsAddInfoInsertMapper.insertSiGongAddSignFileInfo(params);
			}
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				if ("C18A".equals(as_com_ssec)) {					
					response.setResultMessage("insertAsAddSignFileInfo 오류 [" + res + "]");
				} else {
					response.setResultMessage("insertSiGongAddSignFileInfo 오류 [" + res + "]");
				}
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

	@ApiOperation(value = "selectTcSignContent", notes = "고객동의 조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "고객동의 조회 실패 !!") })
	@GetMapping("/selectTcSignContent")
	@RequestMapping(value = "/selectTcSignContent", method = RequestMethod.GET)
	public String selectTcSignContent(
			@ApiParam(value = "회사코드", required = true, example = "C02I")
			@RequestParam(name = "com_agsec", required = true) String as_com_agsec,
			@ApiParam(value = "브랜드코드", required = true, example = "T60I01")
			@RequestParam(name = "com_brand", required = true) String as_com_brand,
			@ApiParam(value = "AS,시공 구분코드", required = true, example = "C18A")
			@RequestParam(name = "com_ssec", required = true) String as_com_ssec
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("COM_AGSEC", as_com_agsec);
		params.put("COM_BRAND", as_com_brand);
		params.put("COM_SSEC", as_com_ssec);		
	
//		logger.trace("Trace Level 테스트");
//		logger.debug("DEBUG Level 테스트");
//		logger.info("INFO Level 테스트"); 
//		logger.warn("Warn Level 테스트");
//		logger.error("ERROR Level 테스트");
			
		ArrayList<DataResult> arList = crs0010_p01Mapper.selectTcSignContent(params);

		try {
			ComParamLogger.paramToVO("DataResult", "DataResult", arList.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(arList);		
		
	}

	@ApiOperation(value = "erp_insertTcStiReq", notes = "tc_stireq 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "tc_stireq 등록 처리 실패 !!") })
	@GetMapping("/erp_insertTcStiReq")
	@RequestMapping(value = "/erp_insertTcStiReq", method = RequestMethod.GET)
	public String erp_insertTcStiReq(
			@ApiParam(value = "시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no,			
			@ApiParam(value = "STI_CD", required = true, example = "")
			@RequestParam(name="sti_cd", required = true) String as_sti_cd,
			@ApiParam(value = "사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name = "usr_id", required = true) String as_usr_id			
			) {
						
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("sti_cd", as_sti_cd);
		params.put("usr_id", as_usr_id);
		
		response = apiErpService.erp_insertTcStiReq(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@PostMapping(value="/FileUpload")
	@ResponseBody
	public String fileUpload(@RequestPart(name="dir", required=false) String dir, @RequestPart MultipartFile files) {
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		HashMap<String, Object> params = null;
		try {
			String filename = "", baseDir = "", yyyymmdd = "";
			Path path = null;
			if (OSValidator.isWindows()) {
				baseDir = "C:\\ServerFiles";
				if (!"".equals(dir) && dir != null) {
					baseDir = baseDir + "\\" + dir;
				}
				filename = baseDir + "\\"+ files.getOriginalFilename();
				
			} else {
				dataResult = erpAsAddInfoInsertMapper.searchToday(params);
				if (dataResult == null) {
					response.setResultCode("5001");
					response.setResultMessage("searchToday 오류");
					return gson.toJson(response);
				} else {
					yyyymmdd = dataResult.getData1();
				}
				//baseDir = "/ERP_FILE/ERP/A/" + StringUtil.getToday();
				baseDir = "/ERP_FILE/ERP/A/" + yyyymmdd;
				if (!"".equals(dir) && dir != null) {
					baseDir = baseDir + "/" + dir;
				}
				filename = baseDir + "/"+ files.getOriginalFilename();
			}
			path = Paths.get(baseDir);
			if (!Files.isDirectory(path)) { // 해당 디렉토리가 없다면
				Files.createDirectories(path); // 폴더 생성
			}
			files.transferTo(new File(filename));
		} catch (Exception e) {
			response.setResultCode("5001");
			response.setResultMessage("fileUpload 사진UPLOAD 오류 [" + e.toString() + "]");
		}
		
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_SigonResultMigeulReasonRe", notes = "시공미결사유(재일정) 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결사유(재일정) 등록 처리 실패 !!") })
	@GetMapping("/erp_SigonResultMigeulReasonRe")
	@RequestMapping(value = "/erp_SigonResultMigeulReasonRe", method = RequestMethod.GET)
	public String erp_SigonResultMigeulReasonRe(
			@ApiParam(value = "시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no,
			@ApiParam(value = "미결사유", required = true, example = "C52A01")
			@RequestParam(name="com_undsec", required = true) String as_com_undsec,			
			@ApiParam(value = "재시공요청일", required = true, example = "2020-09-25")
			@RequestParam(name="pld_rcdt", required = true) String as_pld_rcdt,
			@ApiParam(value = "비고", required = false, example = "")
			@RequestParam(name="rmk", required = false) String as_rmk,			
			@ApiParam(value = "사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name = "usr_id", required = false) String as_usr_id,
			
			@ApiParam(value = "결과구분", required = true, example = "A,A")
			@RequestParam(name="com_pldsec_gbn_arr", required = false) String as_com_pldsec_gbn_arr,			
			@ApiParam(value = "시공의뢰구분", required = true, example = "C090,C090")
			@RequestParam(name="com_pldsec_arr", required = true) String as_com_pldsec_arr,
			@ApiParam(value = "차수", required = true, example = "001,002")
			@RequestParam(name="ord_sseq_arr", required = true) String as_ord_sseq_arr,
			@ApiParam(value = "순번", required = true, example = "001,001")
			@RequestParam(name="ord_iseq_arr", required = true) String as_ord_iseq_arr,
			@ApiParam(value = "확정수량", required = true, example = "1,35")
			@RequestParam(name="pld_fqty_arr", required = true) String as_pld_fqty_arr,			
			@ApiParam(value = "비고", required = true, example = ",")
			@RequestParam(name="ord_rmk_arr", required = true) String as_ord_rmk_arr
			
			) {
						
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("com_unpsec", "C23R");
		params.put("com_undsec", as_com_undsec);
		params.put("pld_rcdt", as_pld_rcdt);
		params.put("pld_rasdt", "");
		params.put("pld_rmk", "재시공요청일 : " + as_pld_rcdt + " , " + as_rmk);
		params.put("mob_rmk", as_rmk);
		params.put("usr_id", as_usr_id);

		params.put("com_pldsec_gbn_arr", as_com_pldsec_gbn_arr);		
		params.put("com_pldsec_arr", as_com_pldsec_arr);
		params.put("ord_sseq_arr", as_ord_sseq_arr);
		params.put("ord_iseq_arr", as_ord_iseq_arr);
		params.put("pld_fqty_arr", as_pld_fqty_arr);
		params.put("ord_rmk_arr", as_ord_rmk_arr);
		
		response = apiErpService.erp_SigonResultMigeulReason(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_SigonResultMigeulReasonAS", notes = "시공미결사유(AS) 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결사유(AS) 등록 처리 실패 !!") })
	@GetMapping("/erp_SigonResultMigeulReasonAS")
	@RequestMapping(value = "/erp_SigonResultMigeulReasonAS", method = RequestMethod.GET)
	public String erp_SigonResultMigeulReasonAS(
			@ApiParam(value = "시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no,
			@ApiParam(value = "미결사유", required = true, example = "C52C01")
			@RequestParam(name="com_undsec", required = true) String as_com_undsec,
			@ApiParam(value = "AS요청일", required = true, example = "2020-09-25")
			@RequestParam(name="pld_rasdt", required = true) String as_pld_rasdt,
			@ApiParam(value = "비고", required = false, example = "")
			@RequestParam(name="rmk", required = false) String as_rmk,			
			@ApiParam(value = "사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name = "usr_id", required = false) String as_usr_id,
						
			@ApiParam(value = "결과구분", required = true, example = "A,A")
			@RequestParam(name="com_pldsec_gbn_arr", required = true) String as_com_pldsec_gbn_arr,			
			@ApiParam(value = "시공의뢰구분", required = true, example = "C090,C090")
			@RequestParam(name="com_pldsec_arr", required = true) String as_com_pldsec_arr,
			@ApiParam(value = "차수", required = true, example = "001,002")
			@RequestParam(name="ord_sseq_arr", required = true) String as_ord_sseq_arr,
			@ApiParam(value = "순번", required = true, example = "001,001")
			@RequestParam(name="ord_iseq_arr", required = true) String as_ord_iseq_arr,
			@ApiParam(value = "확정수량", required = true, example = "1,35")
			@RequestParam(name="pld_fqty_arr", required = true) String as_pld_fqty_arr,			
			@ApiParam(value = "비고", required = true, example = ",")
			@RequestParam(name="ord_rmk_arr", required = true) String as_ord_rmk_arr
			
			) {
						
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("com_unpsec", "C23A");
		params.put("com_undsec", as_com_undsec); //AS고정 "C52C01"
		params.put("pld_rcdt", "");
		params.put("pld_rasdt", as_pld_rasdt);
		params.put("pld_rmk", "A/S요청일 : " + as_pld_rasdt + " , " + as_rmk);
		params.put("mob_rmk", as_rmk);
		params.put("usr_id", as_usr_id);
		
		params.put("com_pldsec_gbn_arr", as_com_pldsec_gbn_arr);
		params.put("com_pldsec_arr", as_com_pldsec_arr);
		params.put("ord_sseq_arr", as_ord_sseq_arr);
		params.put("ord_iseq_arr", as_ord_iseq_arr);
		params.put("pld_fqty_arr", as_pld_fqty_arr);
		params.put("ord_rmk_arr", as_ord_rmk_arr);
		
		response = apiErpService.erp_SigonResultMigeulReason(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_SigonResultMigeulReason", notes = "시공미결사유(반품) 등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결사유(반품) 등록 처리 실패 !!") })
	@GetMapping("/erp_SigonResultMigeulReason")
	@RequestMapping(value = "/erp_SigonResultMigeulReason", method = RequestMethod.GET)
	public String erp_SigonResultMigeulReason(
			@ApiParam(value = "시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no,
			@ApiParam(value = "품목별 미결사유", required = true, example = "C52C01")
			@RequestParam(name="com_undsec", required = true) String as_com_undsec,
			@ApiParam(value = "비고", required = false, example = "")
			@RequestParam(name="rmk", required = false) String as_rmk,			
			@ApiParam(value = "사용자아이디", required = true, example = "SYSTEM")
			@RequestParam(name = "usr_id", required = false) String as_usr_id,
			
			@ApiParam(value = "결과구분", required = true, example = "A,A")
			@RequestParam(name="com_pldsec_gbn_arr", required = true) String as_com_pldsec_gbn_arr,			
			@ApiParam(value = "시공의뢰구분", required = true, example = "C090,C090")
			@RequestParam(name="com_pldsec_arr", required = true) String as_com_pldsec_arr,
			@ApiParam(value = "차수", required = true, example = "001,002")
			@RequestParam(name="ord_sseq_arr", required = true) String as_ord_sseq_arr,
			@ApiParam(value = "순번", required = true, example = "001,001")
			@RequestParam(name="ord_iseq_arr", required = true) String as_ord_iseq_arr,
			@ApiParam(value = "확정수량", required = true, example = "1,35")
			@RequestParam(name="pld_fqty_arr", required = true) String as_pld_fqty_arr,			
			@ApiParam(value = "비고", required = true, example = ",")
			@RequestParam(name="ord_rmk_arr", required = true) String as_ord_rmk_arr
			
			) {
						
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("com_unpsec", "C23E");
		params.put("com_undsec", as_com_undsec);
		params.put("pld_rcdt", "");
		params.put("pld_rasdt", "");		
		params.put("pld_rmk", "미결건위탁처리 : " + as_rmk);
		params.put("mob_rmk", as_rmk);
		params.put("usr_id", as_usr_id);
		
		params.put("com_pldsec_gbn_arr", as_com_pldsec_gbn_arr);
		params.put("com_pldsec_arr", as_com_pldsec_arr);
		params.put("ord_sseq_arr", as_ord_sseq_arr);
		params.put("ord_iseq_arr", as_ord_iseq_arr);
		params.put("pld_fqty_arr", as_pld_fqty_arr);
		params.put("ord_rmk_arr", as_ord_rmk_arr);
		
		response = apiErpService.erp_SigonResultMigeulReason(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "selectSigongDtlList", notes = "시공DTL 리스트")
	@GetMapping("/selectSigongDtlList")  
	public String selectSigongDtlList(
			@ApiParam(value = "시공번호", required = true, example = "P202006011010")
			@RequestParam(name="plm_no", required = true) String as_plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", as_plm_no);
        params.put("com_pldsec", "C090");            
        ArrayList<ds_tcPlandtlList1> allItems = crs0010_p01Mapper.retrieveTcPlandtlList(params);
 
		try {
			ComParamLogger.paramToVO("ds_tcPlandtlList1", "ds_tcPlandtlList1", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "erp_selectTtComcd", notes = "코드조회")
	@GetMapping("/erp_selectTtComcd")  
	public String erp_selectTtComcd(
			@ApiParam(value = "cdx_cd", required = true, example = "C67")
			@RequestParam(name = "cdx_cd", required = true) String cdx_cd,
			@ApiParam(value = "ref_1", required = true, example = "ASRE")
			@RequestParam(name = "ref_1", required = true) String ref_1					
		) { 
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("CDX_CD", cdx_cd);
        params.put("REF_1", ref_1);
		ArrayList<ERPTtComcd> allItems = erpcommMapper.erp_selectTtComcd(params);
		
		try {
			ComParamLogger.paramToVO("ERPTtComcd", "ERPTtComcd", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);
	}

	@ApiOperation(value = "erp_selectAsMigeulReason", notes = "코드조회")
	@GetMapping("/erp_selectAsMigeulReason")  
	public String erp_selectAsMigeulReason(
			@ApiParam(value = "user_id", required = true, example = "SYSTEM")
			@RequestParam(name = "user_id", required = true) String user_id				
		) { 
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("CDX_CD", "C67");
        params.put("REF_1", "ASRE");
		ArrayList<ERPTtComcd> allItems = erpcommMapper.erp_selectTtComcd(params);
		
		try {
			ComParamLogger.paramToVO("ERPTtComcd", "ERPTtComcd", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_SiGongAccSave", notes = "시공정산등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공정산등록 처리 실패 !!") })
	@GetMapping("/erp_SiGongAccSave")
	@RequestMapping(value = "/erp_SiGongAccSave", method = RequestMethod.GET)
	public String erp_SiGongAccSave(
			@RequestParam(name = "plm_no", required = false, defaultValue = "2099") String as_plm_no,
			@RequestParam(name = "user_id", required = false, defaultValue = "SYSTEM") String as_usr_id
			) {
		
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("usr_id", as_usr_id);		
		response = apiErpService.erp_SiGongAccSave(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}

		
	@ApiOperation(value = "erp_SiGongResultSave", notes = "시공결과등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공결과등록 처리 실패 !!") })
	@GetMapping("/erp_SiGongResultSave")
	@RequestMapping(value = "/erp_SiGongResultSave", method = RequestMethod.GET)
	public String erp_SiGongResultSave(
			@RequestParam(name = "plm_no", required = false) String as_plm_no,
			@RequestParam(name = "user_id", required = false) String as_usr_id,
			@RequestParam(name = "mob_std", required = false) String mob_std,
			@RequestParam(name = "mob_remark", required = false) String mob_remark
			) {
		
		SigongResultResponse response = new SigongResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("usr_id", as_usr_id);
		params.put("mob_std", mob_std);
		params.put("mob_remark", mob_remark);

		response = apiErpService.erp_SiGongResultSave(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);
	}
				
	@ApiOperation(value = "selectAsDtlList", notes = "ASDTL 리스트")
	@GetMapping("/selectAsDtlList")  
	public String selectAsDtlList(
			@RequestParam(name="plm_no", required=true, defaultValue = "F202006030452") String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
		ArrayList<ds_tabList1> allItems = apiErpService.selectAsDtlList(params);
		
		try {
			ComParamLogger.paramToVO("ds_tabList1", "ds_tabList1", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_modifyAsStartTime", notes = "AS시작시간처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS시작시간처리 실패 !!") })
	@GetMapping("/erp_modifyAsStartTime")
	@RequestMapping(value = "/erp_modifyAsStartTime", method = RequestMethod.GET)
	public String erp_modifyAsStartTime(
			@RequestParam(name = "plm_no", required = false) String plm_no			
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			res = schedulemainlistMapper.updateAddAsEndTime(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
				System.out.println("res=" + res);				
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

	@ApiOperation(value = "erp_AsResultSaveRe", notes = "AS결과등록(재일정포함)")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS결과등록(재일정포함) 처리 실패 !!") })
	@GetMapping("/erp_AsResultSaveRe")
	@RequestMapping(value = "/erp_AsResultSaveRe", method = RequestMethod.GET)
	public String erp_AsResultSaveRe(
			@RequestParam(name = "plm_no", required = false) String as_plm_no,
			@RequestParam(name = "rptq_dt", required = false) String as_rptq_dt,
			@RequestParam(name = "com_unsec", required = false) String as_com_unsec,
			@RequestParam(name = "user_id", required = false) String as_user_id,
			@RequestParam(name = "remark", required = false) String as_remark
			) {
		AsResultResponse response = new AsResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("plm_no", as_plm_no);
		params.put("rptq_dt", as_rptq_dt);
		params.put("com_unsec", as_com_unsec);
		params.put("usr_id", as_user_id);		
		params.put("remark", as_remark);
		response = apiErpService.erp_AsResultSaveRe(params);
		
		System.out.println(response.toString());
		return gson.toJson(response);		
	}
	
	@ApiOperation(value = "erp_AsResultSaveOld", notes = "AS결과등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS결과등록 처리 실패 !!") })
	@GetMapping("/erp_AsResultSaveOld")
	@RequestMapping(value = "/erp_AsResultSaveOld", method = RequestMethod.GET)
	public String erp_AsResultSaveOld(
			@RequestParam(name = "plm_no", required = false) String as_plm_no,
			@RequestParam(name = "rptq_dt", required = false) String as_rptq_dt,
			@RequestParam(name = "com_unsec", required = false) String as_com_unsec,
			@RequestParam(name = "user_id", required = false) String as_user_id
			) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			APM0020_M01 m01 = new APM0020_M01();			
			HashMap<String, Object> params;
			m01.as_com_unsec = as_com_unsec;
			
			if ("C67002".equals(m01.as_com_unsec)) { // 모바일사유코드 -> ERP사유코드로 치환
				m01.v_com_unsec = "A7102";//	고객연기
			} else if ("C67003".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7103";//	대리점연기
			} else if ("C67004".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7104";//	일정연기
			} else if ("C67005".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7105";//	조치품연기
			} else if ("C67006".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7106";//	출고사고
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
						
			m01.ds_list1 = erppraacctlistsaveMapper.selectResMstList(params);
			if (m01.ds_list1 == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectResMstList 오류 ");
				return gson.toJson(response);
			}
			
			if (isDeBug) {
				System.out.println("com_scd = " + m01.ds_list1.com_scd);
				System.out.println("sti_cd = " + m01.ds_list1.sti_cd);
				System.out.println("rpt_no = " + m01.ds_list1.rpt_no);
				System.out.println("rpt_seq = " + m01.ds_list1.rpt_seq);
				System.out.println("com_unpsec = " + m01.ds_list1.com_unpsec);
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);			
			m01.ds_tabList1 = erppraacctlistsaveMapper.selectResDtlList(params);
			if (m01.ds_tabList1 == null || m01.ds_tabList1.size() == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectResDtlList 오류 ");
				return gson.toJson(response);
			}	
			
			if (isDeBug) {
				System.out.println("com_rdsec = " + m01.ds_tabList1.get(0).com_rdsec);
				System.out.println("com_undsec = " + m01.ds_tabList1.get(0).com_undsec);
				System.out.println("com_undsec1 = " + m01.ds_tabList1.get(0).com_undsec1);
				System.out.println("com_undsec2 = " + m01.ds_tabList1.get(0).com_undsec2);
				System.out.println("pld_rcdt = " + m01.ds_tabList1.get(0).pld_rcdt);
				System.out.println("pld_rmk = " + m01.ds_tabList1.get(0).pld_rmk);
				System.out.println("pld_fqty = " + m01.ds_tabList1.get(0).pld_fqty);
				System.out.println("pld_cfamt = " + m01.ds_tabList1.get(0).pld_cfamt);
			}
			
			m01.beforeRptNo = m01.ds_list1.rpt_no;
			m01.beforeRptSeq = m01.ds_list1.rpt_seq;
			
			m01.btn_allset_onclick.v_plmNo = m01.ds_list1.plm_no;
			m01.btn_allset_onclick.v_ccyn = "C13N"; //m01.ds_list1.cc;
			m01.btn_allset_onclick.v_comRmfg = m01.ds_list1.com_rmfg;
			m01.btn_allset_onclick.v_stiCd = m01.ds_list1.sti_cd;
			m01.btn_allset_onclick.v_comScd = m01.ds_list1.com_scd;
			m01.btn_allset_onclick.v_comSprog = m01.ds_list1.com_sprog;
			
			if (!"A17008".equals(m01.btn_allset_onclick.v_comSprog)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("출고완료 상태 이외의 일정은 결과등록 처리 할 수 없습니다.");
				return gson.toJson(response);				
			}
			
			if ("C16PH".equals(m01.btn_allset_onclick.v_comScd)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("포항센터 건은 미결처리 할 수 없습니다. 정보기술팀 연락 요망.!");
				return gson.toJson(response);
			}
			
			if (!"C13W".equals(m01.btn_allset_onclick.v_comRmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("이미 결과가 등록되어 있는 건입니다.");
				return gson.toJson(response);
			}

			
			for (int i=0; i<m01.ds_tabList1.size(); i++){				
				if ("C13W".equals(m01.ds_tabList1.get(i).com_rdsec)){
					m01.btn_allset_onclick.nCnt++;
				}
			}
			
			if (m01.btn_allset_onclick.nCnt == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("선택된 품목이 없습니다.");
				return gson.toJson(response);
			}
			
			APM0020_P03 p03 = new APM0020_P03();
						
			p03.strPlmNo = m01.btn_allset_onclick.v_plmNo;
			p03.strCcYn  = m01.btn_allset_onclick.v_ccyn;
			p03.beforeRptNo = m01.beforeRptNo;
			p03.beforeRptSeq = m01.beforeRptSeq;            			
			//p03.btn_rstReg_onclick.v_ccstd = this.div_detail.cbo_ccreason.value;
			//p03.btn_rstReg_onclick.v_ccrmk = this.div_detail.ta_ccRmk.value;			
			p03.arrRtn[0] = p03.strLsYn;
			
			if ("C13N".equals(p03.strLsYn)){
				p03.arrRtn[1] = m01.v_com_unsec;				
				p03.arrRtn[2] = "1";
				p03.arrRtn[3] = as_rptq_dt;
				p03.btn_rstReg_onclick.v_rmkTmp = "";
				p03.btn_rstReg_onclick.v_rmk = "재A/S요청일:"+ p03.btn_rstReg_onclick.v_date +" "+ p03.btn_rstReg_onclick.v_rmkTmp;
				p03.arrRtn[4] = p03.btn_rstReg_onclick.v_rmk;
			}
			p03.arrRtn[5] = "";
			p03.arrRtn[6] = "";
			p03.arrRtn[7] = "";			
			
			m01.btnSaveSP.v_yn = p03.arrRtn[0];
			if ("C13Y".equals(m01.btnSaveSP.v_yn)) {
				m01.comUnmsec = p03.arrRtn[3];
			} else {
				m01.comUnmsec = m01.v_com_unsec; //p03.arrRtn[7];
				m01.btnSaveSP.v_rmk = p03.arrRtn[4];	
			}
			
			m01.btnSaveSP.v_rptNo = m01.ds_list1.rpt_no;
			m01.btnSaveSP.v_rptSeq = m01.ds_list1.rpt_seq;
			m01.btnSaveSP.v_stiCd = m01.ds_list1.sti_cd;
			m01.btnSaveSP.v_comScd = m01.ds_list1.com_scd;
			m01.btnSaveSP.v_plmNo = m01.ds_list1.plm_no;
			m01.btnSaveSP.v_comAgesec = m01.ds_list1.com_agsec;
			
			for (int i=0; i<m01.ds_tabList1.size(); i++){				
				if ("C13W".contentEquals(m01.ds_tabList1.get(i).com_rdsec)){					 
					m01.ds_tabList1.get(i).com_rdsec = m01.btnSaveSP.v_yn;				
					if ("C13N".equals(m01.btnSaveSP.v_yn)){
						
						m01.btnSaveSP.v_comUnpsec = p03.arrRtn[2];
						m01.ds_tabList1.get(i).com_undsec = p03.arrRtn[1];
						m01.ds_tabList1.get(i).com_undsec1 = p03.arrRtn[5];
						m01.ds_tabList1.get(i).com_undsec2 = p03.arrRtn[6];
					
						if ("1".equals(m01.btnSaveSP.v_comUnpsec)){
							m01.ds_tabList1.get(i).pld_rcdt = p03.arrRtn[3].replace("-", "");
						}
						
						m01.ds_tabList1.get(i).pld_rmk = p03.arrRtn[4];
					}
					
				}
				
				if ("C13N".equals(m01.ds_tabList1.get(i).com_rdsec) && 
					m01.ds_tabList1.get(i).pld_eqty.equals(m01.ds_tabList1.get(i).pld_fqty)){
					m01.ds_tabList1.get(i).pld_fqty = 0;
					m01.ds_tabList1.get(i).pld_cfamt = 0;
				}

			} 
			
			for (int i=0; i<m01.ds_tabList1.size(); i++) {
				res = erppraacctlistsaveMapper.modifyPlanDtl_U(m01.ds_tabList1.get(i));
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("modifyPlanDtl_U 오류 [" + i + "][" + res + "]");
					return gson.toJson(response);
				}
			}
		
			
			if ("C13Y".equals(m01.btnSaveSP.v_yn)) {
				params = new HashMap<String, Object>();
				params.put("rpt_no", m01.btnSaveSP.v_rptNo);
				params.put("rpt_seq", m01.btnSaveSP.v_rptSeq);
				dataResult = erppraacctlistsaveMapper.selectBtripAmt(params);
				if (dataResult != null) {
					m01.v_rtnAmt = dataResult.getValue1();
					if (m01.v_rtnAmt != 0){
						m01.ds_btrip.plm_no = m01.btnSaveSP.v_plmNo;
						m01.ds_btrip.com_pldsec = "A6811";
						m01.ds_btrip.pld_fqty = 1;
						m01.ds_btrip.pld_famt = m01.v_rtnAmt;
						//this.ds_btrip.setColumn(0, "PLD_CFAMT",	 	1 * parseInt(this.v_rtnAmt));
						//this.ds_btrip.setColumn(0, "COM_ACD", 		"A6901");
						//this.ds_btrip.setColumn(0, "COM_SVND", 		"C21B");
						//this.ds_btrip.setColumn(0, "COM_VFSEC", 	"C2402");
						//this.ds_btrip.setColumn(0, "COM_VTSEC", 	"C2501
						//this.ds_btrip.setColumn(0, "COM_RASEC", 	"C12A");
						m01.ds_btrip.com_agsec = m01.btnSaveSP.v_comAgesec;
						m01.ds_btrip.usr_id = as_user_id;
						
						res = erppraacctlistsaveMapper.mergeTaPlanDtl(m01.ds_btrip);
						if (res < 1) {
							txManager.rollback(status);
							response.setResultCode("5001");
							response.setResultMessage("mergeTaPlanDtl 오류 [" + res + "]");
							return gson.toJson(response);
						}
					}					
				}
			}
			
			//this.fn_selectStiNm(v_comScd, v_stiCd);
			//v_stiNm = this.v_rtnStiNm;
			//String v_stiNm = "";
			params = new HashMap<String, Object>();
			params.put("com_scd", m01.btnSaveSP.v_comScd);
			params.put("sti_cd", m01.btnSaveSP.v_stiCd);			
			dataResult = erppraacctlistsaveMapper.selectStiNm(params);
			if (dataResult != null) {
				m01.btnSaveSP.v_stiNm = dataResult.getData1();
				if (isDeBug) {
					System.out.println("v_stiNm = " + m01.btnSaveSP.v_stiNm);
				}
			}			
			
			params = new HashMap<String, Object>();
			params.put("rpt_no", m01.btnSaveSP.v_rptNo);
			params.put("rpt_seq", m01.btnSaveSP.v_rptSeq);			
			dataResult = erppraacctlistsaveMapper.selectRptAdsec(params);
			if (dataResult != null) {
				m01.btnSaveSP.v_rmk1 = dataResult.getData1();
				if (isDeBug) {
					System.out.println("v_rmk1 = " + m01.btnSaveSP.v_rmk1);
				}
			}

			m01.btnSaveSP.v_rmk1 = m01.btnSaveSP.v_rmk1 + " [" + m01.btnSaveSP.v_stiNm + "]";
			m01.btnSaveSP.v_rmk = m01.btnSaveSP.v_rmk1 + " " + m01.btnSaveSP.v_rmk;
			if (isDeBug) {
				System.out.println("v_rmk1 = " + m01.btnSaveSP.v_rmk1);
				System.out.println("v_rmk = " + m01.btnSaveSP.v_rmk);
			}
			
			m01.ds_rptreq.rpt_no = m01.btnSaveSP.v_rptNo;
			m01.ds_rptreq.rpt_seq = m01.btnSaveSP.v_rptSeq;
			m01.ds_rptreq.rpt_adesc = m01.btnSaveSP.v_rmk;
			m01.ds_rptreq.ls_yn = m01.btnSaveSP.v_yn;
			m01.ds_rptreq.usr_id = as_user_id;			
			res = erppraacctlistsaveMapper.modifyAllsetRptReq_U(m01.ds_rptreq);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetRptReq_U 오류 [" + res + "]");
				return gson.toJson(response);
			}					
					
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);			
			dataResult = erppraacctlistsaveMapper.selectFaFinishYn(params);
			if (dataResult != null) {
				m01.fn_finishyn.lln = dataResult.getValue1();
				m01.fn_finishyn.llw = dataResult.getValue2();
				m01.fn_finishyn.lly = dataResult.getValue3();
				if (m01.fn_finishyn.llw > 0){
					m01.fn_finishyn.v_rtnStr = "C13W";
				} else {
					if (m01.fn_finishyn.lln > 0){
						m01.fn_finishyn.v_rtnStr = "C13N";
					} else {
						m01.fn_finishyn.v_rtnStr = "C13Y";
					}
				}				
			}
			
			m01.fn_setRmfg.v_plmNo = m01.ds_list1.plm_no;
			m01.fn_setRmfg.v_comRmfg = m01.fn_finishyn.v_rtnStr;			
			m01.ds_list1.com_rmfg = m01.fn_setRmfg.v_comRmfg;

			
			m01.ds_allset.plm_no = m01.fn_setRmfg.v_plmNo;
			m01.ds_allset.usr_id = as_user_id;
			m01.ds_allset.com_plmfg = "C103";
			m01.ds_allset.com_rmfg = "C13N";
			m01.ds_allset.com_unmsec = m01.comUnmsec;
			
			if ("C13N".equals(m01.fn_setRmfg.v_comRmfg) || "C13Y".equals(m01.fn_setRmfg.v_comRmfg)){
				//진행상태를 'C103' 로 바꾼다.(com_plmfg)
				m01.ds_list1.com_plmfg = "C103";
				m01.ds_list1.com_unmsec = m01.comUnmsec;
				
//				m01.ds_allset.com_plmfg = "C103";
//				m01.ds_allset.com_rmfg = m01.fn_setRmfg.v_comRmfg;
//				m01.ds_allset.com_unmsec = m01.comUnmsec;
//				m01.ds_allset.plm_no = m01.fn_setRmfg.v_plmNo;
				
				m01.ds_allset.plm_no = m01.fn_setRmfg.v_plmNo;
				m01.ds_allset.usr_id = as_user_id;
				m01.ds_allset.com_plmfg = "C103";
				m01.ds_allset.com_rmfg = "C13N";
				m01.ds_allset.com_unmsec = m01.comUnmsec;
				
			}
			
			m01.ds_allset.usr_id = as_user_id;
			res = erppraacctlistsaveMapper.modifyAllsetPlanMst_U(m01.ds_allset);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetPlanMst_U 오류 [" + res + "]");
				return gson.toJson(response);
			}

			params = new HashMap<String, Object>();
			params.put("com_unmsec", m01.as_com_unsec); //모바일 사유코드
			params.put("rptq_dt", as_rptq_dt);
			params.put("plm_no", as_plm_no);
			res = erppraacctlistsaveMapper.modifyAllsetPlanDtl_U(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetPlanDtl_U 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			
			//////////////////////////////////////////////////////////////////
			//재일정 			
			//////////////////////////////////////////////////////////////////
			
			m01.beforeRptNo  = m01.ds_list1.rpt_no;
			m01.beforeRptSeq = m01.ds_list1.rpt_seq;
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);			
			m01.ds_list2 = erppraacctlistsaveMapper.selectNotFinishList(params);
			if (m01.ds_list2 == null || m01.ds_list2.size() == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectNotFinishList 오류 ");
				return gson.toJson(response);
			}
			
			m01.btn_selectRecProc_onclick.v_plmNo = as_plm_no;
			for (int i=0; i<m01.ds_list2.size(); i++){				
				m01.btn_selectRecProc_onclick.v_comRdsec = m01.ds_list2.get(i).com_rdsec;
				m01.btn_selectRecProc_onclick.v_comUnpsec = m01.ds_list2.get(i).com_unpsec;
				m01.btn_selectRecProc_onclick.v_pldRcdt = m01.ds_list2.get(i).pld_rcdt;

				if ("C13N".equals(m01.btn_selectRecProc_onclick.v_comRdsec) && (m01.btn_selectRecProc_onclick.v_comUnpsec == null || "".equals(m01.btn_selectRecProc_onclick.v_comUnpsec))){						
					m01.btn_selectRecProc_onclick.nCnt++;
				}				
			}
			
			if (m01.btn_selectRecProc_onclick.nCnt == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("재 AS예약 처리할 품목들이 없습니다.");
				return gson.toJson(response);
			}
			
			for (int i=0; i<m01.ds_list2.size(); i++){
				m01.ds_list2.get(i).com_unpsec = "A70A";				
				m01.ds_uptplandtl.com_unpsec = "A70A";
				m01.ds_uptplandtl.plm_no = m01.btn_selectRecProc_onclick.v_plmNo;
				m01.ds_uptplandtl.usr_id = as_user_id;
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
			dataResult = erppraacctlistsaveMapper.selectMaxRcdt(params);
			if (dataResult == null || "".equals(dataResult.getData1())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("재 AS예약 처리할 품목들이 없습니다.");
				return gson.toJson(response);
			}
			
			m01.btn_selectRecProc_onclick.v_maxRcdt = dataResult.getData1();			
			
			String v_com_agsec = "C02" + StringUtils.left(as_plm_no, 1);				
			if (isDeBug) {
				System.out.println("v_com_agsec = " + v_com_agsec);
			}
			
/*				
				this.fn_replmnocreate(v_plmNo, "C02" + this.gfn_Left(v_plmNo, 1));
				v_rePlmNo = this.rePlmNo;
*/
			
			HashMap<String, Object> outVar = new HashMap<String, Object>();
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
			params.put("user_id", as_user_id);
			params.put("com_nosec", "C08PLM");	//번호구분
			params.put("com_agsec", v_com_agsec);				
			String res_msg = "";			
    		String lSzplmNo = "";
    		dataResult = erppraacctlistsaveMapper.selectPlmPno(params);
    		if (dataResult != null) {
    			lSzplmNo = dataResult.getData1();
    		}

    		params.put("lSzplmNo", lSzplmNo);
    		if ("".equals(lSzplmNo) || lSzplmNo == null){ //재일정반영처리가 안된경우
        		//시공번호 채번    			
    			int iSeqNo = 0;
    			params.put("seq_setdt", as_rptq_dt.replace("-",  ""));
    			dataResult = erppraacctlistsaveMapper.selectNewSeq(params);
    			if (dataResult == null) {
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("selectNewSeq 오류");
					return gson.toJson(response);
    			} else {
    				iSeqNo = dataResult.getValue1();
    			}    			
    			
    			params.put("seq_setdt", as_rptq_dt.replace("-",  ""));
    			params.put("seq_no", iSeqNo);
        		res = erppraacctlistsaveMapper.modifySeqnoinf(params);
        		if (res < 1) { 
        			res_msg = "시공번호 생성 시 오류가 발생하였습니다.";
        			txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
        		}
        		
        		String strSetdt = (String) params.get("seq_setdt");
        		String strAgsec = (String) params.get("com_agsec");
        		
    			// 시공번호 채번    			
        		lSzplmNo = StringUtils.right(strAgsec, 1) + strSetdt + StringUtils.leftPad(String.valueOf(iSeqNo), 4, "0");
    			
        		params.put("lSzplmNo", lSzplmNo);
    			// AS 예정정보mst 생성
        		iSeqNo = erppraacctlistsaveMapper.selectInsertPlanMst_I(params);
    			if (iSeqNo < 1) { 
    				res_msg =  "시공번호 생성 시 오류가 발생하였습니다2.";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
        		}
    			
    			// AS 예정정보dtl 생성
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg =  "selectInsertPlanDtl_I 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
    			}

    			// 이전 시공번호 update
    			res = erppraacctlistsaveMapper.modifyPlmPno_U(params);
    			if (res < 1) {    				
    				res_msg = "modifyPlmPno_U 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
    			}
    		
    		} else {
    			
    			// 재일정반영처리가 된경우
    			res = erppraacctlistsaveMapper.modifyTaPlanDtl_D(params);
    			if (res < 1) {    				
    				res_msg = "modifyTaPlanDtl_D 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
    			}
    			
    			// AS 예정정보dtl 생성
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg = "selectInsertPlanDtl_I 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
    			}   			
    		}
    		
    		outVar.put("rePlmNo", lSzplmNo);

    		// 채번끝
    		
    		
			if ("".equals(res_msg)) {
				m01.btn_selectRecProc_onclick.v_rePlmNo = (String) outVar.get("rePlmNo");
				if (isDeBug) {
					System.out.println("v_rePlmNo = " + m01.btn_selectRecProc_onclick.v_rePlmNo);
				}
			} else {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("rePlmnoCreate 오류 [" + res_msg + "]");
				return gson.toJson(response);
			}

			if (m01.btn_selectRecProc_onclick.v_rePlmNo.length() < 13) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("v_rePlmNo SIZE 오류.");
				return gson.toJson(response);
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", m01.btn_selectRecProc_onclick.v_rePlmNo);
			dataResult = erppraacctlistsaveMapper.selectPlmnoCnt(params);
			if (dataResult != null) {
				m01.btn_selectRecProc_onclick.nllyn = dataResult.getValue1();
				if (isDeBug) {
					System.out.println("nllyn = " + m01.btn_selectRecProc_onclick.nllyn);
				}
				if (m01.btn_selectRecProc_onclick.nllyn > 0) {					
					params = new HashMap<String, Object>();
					params.put("plm_no", m01.btn_selectRecProc_onclick.v_rePlmNo);
					m01.ds_planInfo = erppraacctlistsaveMapper.selectPlanInfo(params);
					if (m01.ds_planInfo != null) {
						m01.btn_selectRecProc_onclick.v_rptNo = m01.ds_planInfo.rpt_no;
						m01.btn_selectRecProc_onclick.v_rptSeq = m01.ds_planInfo.rpt_seq;
						if (isDeBug) {
							System.out.println("v_rptNo = " + m01.btn_selectRecProc_onclick.v_rptNo);
							System.out.println("v_rptSeq = " + m01.btn_selectRecProc_onclick.v_rptSeq);
						}
					}
										
					m01.ds_callbackUpt.plm_no = m01.btn_selectRecProc_onclick.v_plmNo;
					m01.ds_callbackUpt.re_plm_no = m01.btn_selectRecProc_onclick.v_rePlmNo;
					m01.btn_selectRecProc_onclick.v_remDt = m01.ds_list1.rem_dt;
					m01.btn_selectRecProc_onclick.v_remSeq = m01.ds_list1.rem_seq;					
					
					res = erppraacctlistsaveMapper.modifyComUnpsec_U(m01.ds_uptplandtl);
					if (res < 1) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("modifyComUnpsec_U 오류 [" + res + "]");
						return gson.toJson(response);
					} else {
						ERPAsResult asResult = new ERPAsResult();
						asResult.setRmk(m01.btnSaveSP.v_rmk);
						asResult.setRe_plm_no(m01.btn_selectRecProc_onclick.v_rePlmNo);
						asResult.setRe_rpt_no(m01.btnSaveSP.v_rptNo);
						asResult.setRe_rpt_seq(m01.btnSaveSP.v_rptSeq);
						asResult.setPld_rmk(m01.btnReASP.v_pldRmk);
						asResult.setPld_rcdt(m01.btn_selectRecProc_onclick.v_maxRcdt);
						response.setAsResult(asResult);
						
						txManager.commit(status);
						response.setResultCode("200");
						response.setResultCode("재AS예약 됐으므로 AS예정정보를 생성하였습니다.");
						System.out.println(response.toString());
						return gson.toJson(response);
					}
				}
			}
			
///============================>			
			APM0020_P02 p02 = new APM0020_P02();
			
			//var pArgs = {pvParam:"2"+"||"+v_rptNo+"||"+v_rptSeq+"||"+v_maxRcdt+"||"+v_rePlmNo+"||"+v_plmNo+"||"+v_remDt+"||"+v_remSeq, pvReturnGb:"TEXT"};
			
			m01.btn_selectRecProc_onclick.v_rptNo = m01.ds_list1.rpt_no ;
			m01.btn_selectRecProc_onclick.v_rptSeq = m01.ds_list1.rpt_seq;
			m01.btn_selectRecProc_onclick.v_remDt = m01.ds_list1.rem_dt;
			m01.btn_selectRecProc_onclick.v_remSeq = m01.ds_list1.rem_seq;					
			
			
			p02.ps1 ="2";
			p02.strRptNo = m01.btn_selectRecProc_onclick.v_rptNo;
			p02.strRptSeq = m01.btn_selectRecProc_onclick.v_rptSeq;
			p02.strOrmAdt = m01.btn_selectRecProc_onclick.v_maxRcdt;
			p02.strRePlmNo = m01.btn_selectRecProc_onclick.v_rePlmNo;
			p02.strPlmNo = m01.btn_selectRecProc_onclick.v_plmNo;
			p02.strRemDt = m01.btn_selectRecProc_onclick.v_remDt;
			p02.strRemSeq = m01.btn_selectRecProc_onclick.v_remSeq;
			p02.reProcYn = "Y";		//재시공유무 Flag
		
			params = new HashMap<String, Object>();
			params.put("plm_no", p02.strPlmNo);
			dataResult = erppraacctlistsaveMapper.selectRemCasyn(params);
			if (dataResult != null) {
				p02.v_remCasyn = dataResult.getData1();
			}

			params = new HashMap<String, Object>();
			params.put("plm_no", p02.strRePlmNo);			
			p02.ds_planMst = erppraacctlistsaveMapper.selectRePlanMstInfo(params);
			if (p02.ds_planMst == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectRePlanMstInfo 오류 ");
				return gson.toJson(response);
			}
			
			p02.v_ormNm = p02.ds_planMst.ctm_nm;	
			p02.v_agtCd = p02.ds_planMst.agt_cd;
			p02.v_ctmZip = p02.ds_planMst.ctm_zip;
			p02.v_ormPurcst = p02.ds_planMst.ctm_cd;
			p02.v_stiCd = p02.ds_planMst.sti_cd;
			p02.v_sacCd = p02.ds_planMst.sac_cd;
			p02.v_comCtsec = p02.ds_planMst.com_ctsec;	
			p02.v_comScd = p02.ds_planMst.com_scd;
			
			if ("F".equals(StringUtils.left(p02.strRptNo, 1))){
				p02.strComAgsec = "C02F";
			} else if ("I".equals(StringUtils.left(p02.strRptNo, 1))){
				p02.strComAgsec = "C02I";
			} else if ("P".equals(StringUtils.left(p02.strRptNo, 1))){			
				p02.strComAgsec = "C02P";
			}
						
			p02.ds_planMst.orm_adt = p02.strOrmAdt;
			p02.ds_planMst.com_agsec = p02.strComAgsec;
			
			if (p02.v_ctmZip.length() != 5) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("우편번호 자릿수를 변경해야 합니다. -> '정보기술팀 요청'");
				return gson.toJson(response);
			}
			
			p02.ds_result.rem_csec = "R";
			
			p02.v_ormNo = p02.strRptNo + p02.strRptSeq;
			p02.ds_result.orm_no = p02.v_ormNo;
			p02.ds_result.rem_asno = p02.strRptSeq;
			p02.ds_result.com_ssec = "C18A";
			p02.ds_result.agt_cd = p02.v_agtCd;
			p02.ds_result.com_agsec = p02.strComAgsec;
			p02.ds_result.orm_nm = p02.v_ormNm;
			p02.ds_result.orm_purcst = p02.v_ormPurcst;
			p02.ds_result.sti_cd = p02.v_stiCd;
			p02.ds_result.sac_cd = p02.v_sacCd;
			p02.ds_result.com_ctsec = p02.v_comCtsec;
			p02.ds_result.rem_casyn = p02.v_remCasyn;
			p02.ds_result.com_scd = p02.v_comScd;	   //서비스센터
			p02.ds_result.plm_no = p02.strPlmNo;
			p02.ds_result.zip_cd = p02.v_ctmZip;   //우편번호
			
			p02.ds_result.rpt_no = p02.strRptNo;
			p02.ds_result.rpt_seq = p02.strRptSeq;
			p02.ds_result.pld_rcdt = p02.strOrmAdt;
			p02.ds_result.new_plm_no = p02.strRePlmNo;
			p02.ds_result.rem_dt = p02.strRemDt;
			p02.ds_result.rem_seq = p02.strRemSeq;
			p02.ds_result.com_rfg =	"C141";  // 20180614 LMK 재일정일때 예약상태로 함
			p02.ds_result.usr_cd =	as_user_id;
			//p02.div_detail.cal_ormAdt.set_value(p02.strOrmAdt);						
			
			
			
			
			
			String rptFtm = "C4133";
			
			p02.ds_result.rem_ptm =	rptFtm;
			p02.ds_result.rem_ftm = rptFtm;
			p02.ds_result.rem_tmfyn = "N";   //시간엄수구분 check
			p02.ds_result.rem_rmk = "";//	this.div_detail.ta_remRmk.value); //시공특기사항 
			p02.ds_result.stm_no = "01";
			
			params = new HashMap<String, Object>();
			params.put("rem_dt", p02.strOrmAdt);
			params.put("rpt_no", p02.strRptNo);			
			params.put("rpt_seq", p02.strRptSeq);
			dataResult = erppraacctlistsaveMapper.selectResMstCnt(params);
			if (dataResult != null) {
				if (dataResult.getValue1() > 0) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("예약하려는 수주건이 이미 예약되었습니다.");
					return gson.toJson(response);
				}
			}
			
			params = new HashMap<String, Object>();
			params.put("com_agsec", p02.strComAgsec);
			params.put("com_ssec", "C18A");
			params.put("com_ymd", p02.strOrmAdt.replace("-", ""));
			params.put("com_gubun", "RS");
			params.put("usr_id", as_user_id);			
			dataResult = erppraacctlistsaveMapper.executeFaAseqrem(params);
			if (dataResult == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("executeFaAseqrem 오류");
				return gson.toJson(response);
			}
			
			p02.ds_result.new_rem_seq = dataResult.getData1();
			
			res = erppraacctlistsaveMapper.modifyTcResMst_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResMst_U 오류 [" + res + "]");
				return gson.toJson(response);
			}
									
			if (p02.strRePlmNo != ""){
				p02.ds_complmfg.com_plmfg = "C101";
				p02.ds_complmfg.plm_no = p02.strRePlmNo;
				p02.ds_complmfg.orm_rdt = as_rptq_dt.replace("-",  "");
				p02.ds_complmfg.plm_cdt = as_rptq_dt.replace("-",  "");
				p02.ds_complmfg.usr_id = as_user_id;
			   
				res = erppraacctlistsaveMapper.modifyTaPlanMst_U(p02.ds_complmfg);
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("modifyTaPlanMst_U 오류 [" + res + "]");
					return gson.toJson(response);
				}   
			}
			
			res = erppraacctlistsaveMapper.modifyTcResDtl_I(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResDtl_I 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			res = erppraacctlistsaveMapper.modifyRptEnddt_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyRptEnddt_U 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
///============================>
			m01.btnReASP.v_pldRmk = "재AS요청일:"+ as_rptq_dt;
			m01.btnReASP.v_plmNo = m01.ds_list1.plm_no;
			
			for (int i=0; i<m01.ds_list2.size(); i++){
				m01.ds_list2.get(i).pld_rmk = m01.btnReASP.v_pldRmk;  //재시공예약시 시공날자 재세팅
			}
			
			m01.ds_uptplandtl_reset();
			m01.ds_uptplandtl.com_unpsec = "A70A";
			m01.ds_uptplandtl.plm_no = m01.btnReASP.v_plmNo;
			m01.ds_uptplandtl.pld_rmk =	m01.btnReASP.v_pldRmk;
			m01.ds_uptplandtl.pld_rcdt = "";			
			res = erppraacctlistsaveMapper.modifyComUnpsec_U(m01.ds_uptplandtl);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyComUnpsec_U2 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			//AS처리결과시간
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddAsEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
				return gson.toJson(response);
    		}			
			
			ERPAsResult asResult = new ERPAsResult();
			asResult.setRmk(m01.btnSaveSP.v_rmk);
			asResult.setRe_plm_no(m01.btn_selectRecProc_onclick.v_rePlmNo);
			asResult.setRe_rpt_no(m01.btnSaveSP.v_rptNo);
			asResult.setRe_rpt_seq(m01.btnSaveSP.v_rptSeq);
			asResult.setPld_rmk(m01.btnReASP.v_pldRmk);
			asResult.setPld_rcdt(m01.btn_selectRecProc_onclick.v_maxRcdt);
			response.setAsResult(asResult);

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

	@ApiOperation(value = "erp_modifyAsMigeulCancel", notes = "AS미결취소처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS미결취소처리 실패 !!") })
	@GetMapping("/erp_modifyAsMigeulCancel")
	@RequestMapping(value = "/erp_modifyAsMigeulCancel", method = RequestMethod.GET)
	public String erp_modifyAsMigeulCancel(
			@RequestParam(name = "plm_no", required = false) String plm_no,
			@RequestParam(name = "mob_remark", required = false) String mob_remark			
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("mob_remark", mob_remark);
			
			res = erppraacctlistsaveMapper.modifyAsMigeulCancel_U(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAsMigeulCancel_U 오류 [" + res + "]");
				System.out.println("res=" + res);				
				return gson.toJson(response);
			}
			
			res = erppraacctlistsaveMapper.modifyAsFinishTime(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAsFinishTime 오류 [" + res + "]");
				System.out.println("res=" + res);				
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

	
	@ApiOperation(value = "erp_PraAcctlistSave", notes = "AS일괄정산처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS일괄정산처리 실패 !!") })
	@GetMapping("/erp_PraAcctlistSave")
	@RequestMapping(value = "/erp_PraAcctlistSave", method = RequestMethod.GET)
	public String erp_PraAcctlistSave(
			@RequestParam(name = "plm_no", required = false) String plm_no,
			@RequestParam(name = "rpt_no", required = false) String rpt_no,
			@RequestParam(name = "rpt_seq", required = false) String rpt_seq,
			@RequestParam(name = "com_agsec", required = false) String com_agsec,
			@RequestParam(name = "user_id", required = false) String user_id
			) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		
		try {		
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("rpt_no", rpt_no);
			params.put("rpt_seq", rpt_seq);
			params.put("com_agsec", com_agsec);
			params.put("user_id", user_id);
			
			dataResult = erppraacctlistsaveMapper.selectRowyPlmnoCnt(params);
			if (dataResult.getValue1() > 0) {
				response.setResultCode("5001");
				response.setResultMessage("완결이나 미결처리가 안된 건중 요청수량과 확정수량이 틀린것이 있습니다.\n수량이 틀린것은 개별 미결처리를 해주시기 바랍니다.");
				return gson.toJson(response);
			}
		
			System.out.println("plm_no=" +plm_no);
			
			res = erppraacctlistsaveMapper.modifyRowyComRdsec_U(params);
			
			if (res < 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyRowyComRdsec_U 오류 [" + res + "]");
				System.out.println("res=" + res);				
				return gson.toJson(response);
			}
			
			dataResult = erppraacctlistsaveMapper.selectFaFinishYn(params);
			
			String v_rtnStr = "", v_comRmfg = "";
			int v_lln = 0, v_llw = 0, v_lly = 0;
					
			v_lln = dataResult.getValue1(); 
			v_llw = dataResult.getValue2();
			v_lly = dataResult.getValue3();
			
			if (v_llw > 0){
				v_rtnStr = "C13W";
			} else {
				if (v_lln > 0){
					v_rtnStr = "C13N";
				} else {
					v_rtnStr = "C13Y";
				}
			}
			
			v_comRmfg = v_rtnStr;
			
			if ("C13N".equals(v_comRmfg) || "C13Y".equals(v_comRmfg)){    //미결,완결
				dataResult = erppraacctlistsaveMapper.selectBtripAmt(params);
				int v_pld_famt = 0;
				if (dataResult != null) {
					v_pld_famt = dataResult.getValue1();	
				}
				params.put("com_plmfg", "C103");
				res = erppraacctlistsaveMapper.modifyComPlmfg_U(params);
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("modifyComPlmfg_U 오류 [" + res + "]");
					return gson.toJson(response);
				}
				
//				if ("C13Y".equals(v_comRmfg)) {
//					params.put("com_pldsec", "A6811");
//					params.put("pld_fqty", "1");
//					params.put("pld_famt", v_pld_famt);
//					params.put("com_agsec", com_agsec);
//					
//					res = erppraacctlistsaveMapper.mergeTaPlanDtl(params);
//					if (res < 1) {
//						txManager.rollback(status);
//						response.setResultCode("5001");
//						response.setResultMessage("mergeTaPlanDtl 오류 [" + res + "]");
//						return gson.toJson(response);
//					}
//				}
				
				if ("C13Y".equals(v_comRmfg)) {
//					params.put("com_pldsec", "A6811");
//					params.put("pld_fqty", "1");
//					params.put("pld_famt", v_pld_famt);
//					params.put("com_agsec", com_agsec);					
					
					ds_btrip ds_btrip = new ds_btrip();
					ds_btrip.plm_no = plm_no;
					ds_btrip.usr_id = user_id;
					
					ds_btrip.com_pldsec = "A6811";
					ds_btrip.pld_fqty = 1;
					ds_btrip.pld_famt = v_pld_famt;
					ds_btrip.com_agsec = com_agsec;
					res = erppraacctlistsaveMapper.mergeTaPlanDtl(ds_btrip);
					if (res < 1) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("mergeTaPlanDtl 오류 [" + res + "]");
						return gson.toJson(response);
					}
				}
				
			}
			
			//정산처리
			dataResult = erppraacctlistsaveMapper.executePraAcctlistSave(params);			
			if (!"".equals(dataResult.getData1())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(dataResult.getData1());
				return gson.toJson(response);
			}

			params.put("com_plmfg", "C104");
			res = erppraacctlistsaveMapper.modifyComPlmfg_U(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyComPlmfg_U C104 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			params.put("mob_std", "C13Y");
			res = erppraacctlistsaveMapper.modifyPlanDetailMobStd(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyPlanDetailMobStd 오류 [" + res + "]");
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

	
	@ApiOperation(value = "selectStiBoardListByBoardName", notes = "공지사항 검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "공지사항 검색 리스트 조회 실패 !!") })
	@GetMapping("/selectStiBoardListByBoardName")
	@RequestMapping(value = "/selectStiBoardListByBoardName", method = RequestMethod.GET)
	public String erp_selectStiBoardListByBoardName(
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@RequestParam(name = "board_nm", required = false) String board_nm
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", com_scd);
		params.put("board_nm", board_nm);
		
		ArrayList<ERPStiBoardList> arList = mypageMapper.selectStiBoardListByBoardName(params);

		return gson.toJson(arList);		
		
	}
	
	@ApiOperation(value = "selectStiBoardListDetail", notes = "공지사항 상세")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "공지사항 상세 조회 실패 !!") })
	@GetMapping("/selectStiBoardListDetail")
	@RequestMapping(value = "/selectStiBoardListDetail", method = RequestMethod.GET)
	public String erp_selectStiBoardListDetail(
			@RequestParam(name = "board_id", required = false) String board_id
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("board_id", board_id);
		
		StiBoardDetailResponse response = new StiBoardDetailResponse();
		response.setBoard(mypageMapper.getStiBoardDetail(params));

		if (response.getBoard() == null) {
			response.setResultCode("5001");
			return gson.toJson(response);
		} else {
			response.setFilelist(mypageMapper.selectStiBoardFileList(params));
			response.setResultCode("200");
		}
		
		return gson.toJson(response);		
		
	}
	
	@ApiOperation(value = "selectStiBoardList", notes = "공지사항")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "공지사항 리스트 조회 실패 !!") })
	@GetMapping("/selectStiBoardList")
	@RequestMapping(value = "/selectStiBoardList", method = RequestMethod.GET)
	public String erp_selectStiBoardList(
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@RequestParam(name = "board_id", required = false) String board_id
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("board_id", board_id);
		params.put("com_scd", com_scd);
		
		ArrayList<ERPStiBoardList> arList;
		
		if ("".contentEquals(board_id)) {
			arList = mypageMapper.selectStiBoardList(params);
		} else {
			arList =mypageMapper.selectStiBoardListNext(params);
		}
		
		return gson.toJson(arList);		
		
	}
	
	@ApiOperation(value = "getMyPage", notes = "마이페이지")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "마이페이지 정보조회 실패 !!") })
	@GetMapping("/getMyPage")
	@RequestMapping(value = "/getMyPage", method = RequestMethod.GET)
	public String erp_getMyPage(
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@RequestParam(name = "sti_cd", required = false) String sti_cd
			) {

		MyPageResponse response = new MyPageResponse();
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("com_scd", com_scd);
		params.put("sti_cd", sti_cd);
		response.setMypage(mypageMapper.getMyPage(params));

		if (response.getMypage() == null) {
			response.setResultCode("5001");
			return gson.toJson(response);
		} else {
			response.setList(mypageMapper.selectStiBoardListRecent(params));
			if (response.getList().size() == 0) {
				ArrayList<ERPStiBoardList> list = new ArrayList<ERPStiBoardList>();
				ERPStiBoardList vo = new ERPStiBoardList();
				vo.setBoard_nm("새로운 알림이 없습니다.");
				list.add(vo);
				response.setList(list);
			}
			response.setResultCode("200");
		}
		
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "convertAddrToGeo", notes = "주소를 geo코드로 변경")
	@GetMapping("/convertAddrToGeo") 
	public String erp_convertAddrToGeo(@RequestParam(name="addr", required=true) String addr) { 
		String baseUrl = "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&format=json&callback=resul";
		
		String fullAddr = "";
		
		try {
        	fullAddr = URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
		
		String url = baseUrl;
        url += "&fullAddr=" + fullAddr + "&appKey=" + TMapInfo.appKey;
		
        GeoLocation item = new GeoLocation();
        
        System.out.println("url : " + url);
        
        RestService.get(url,new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				System.out.println("result : " + result);
				CoordinateWrapper coordinateWrapper = gson.fromJson(result, CoordinateWrapper.class);
				
				if (coordinateWrapper.getCoordinateInfo().getCoordinate().size() == 0) {
					System.out.println(" get geocode error");
				} else {
					String lon = "0",lat = "0";
					
					Coordinate coordinate = coordinateWrapper.getCoordinateInfo().getCoordinate().get(0);
					
					if (!coordinate.getLon().isEmpty()) {
						lon = coordinate.getLon();
						lat = coordinate.getLat();
					}
					
					if (!coordinate.getLonEntr().isEmpty()) {
						lon = coordinate.getLonEntr();
						lat = coordinate.getLatEntr();
					}
					
					if (!coordinate.getNewLon().isEmpty()) {
						lon = coordinate.getNewLon();
						lat = coordinate.getNewLat();
					}
					
					if (!coordinate.getNewLonEntr().isEmpty()) {
						lon = coordinate.getNewLonEntr();
						lat = coordinate.getNewLatEntr();
					}
					
					item.setLatitude(lat);
					item.setLongitude(lon);
				}
				
			}
		});
        
		return gson.toJson(item);
	}
	
	@ApiOperation(value = "ermList", notes = "ERP 수주목록 조회")
	@GetMapping("/ermList") 
	public String erp_ermList(@RequestParam(name="date", required=false) String date) { 
		String baseUrl = "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&format=json&callback=resul";
        
        if (date ==  null || date.isEmpty()) {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        	Calendar calendar = Calendar.getInstance();
        	calendar.add(Calendar.DAY_OF_MONTH, 3);
        	Date d = new Date(calendar.getTimeInMillis());
        	date = sdf.format(d);
        }
//        date = "20200617";
		
        HashMap<String,Object> oRMParams = new HashMap<String, Object>();
        oRMParams.put("date", date);
		List<Map<String, Object>> oRMList = oRMMapper.getORMList(oRMParams);
		ArrayList<TmsOrderBase> allItems = new ArrayList<TmsOrderBase>();
		
		String fullAddr = "";
		String oriFullAddr = "";
		String orderId = "";
		String ormNm = "";
		
		for (int i = 0 ; i < oRMList.size() ; i++) {
			oriFullAddr = oRMList.get(i).get("orm_gaddr").toString();
			orderId = oRMList.get(i).get("order_id").toString();
			ormNm = oRMList.get(i).get("orm_nm").toString();
			
	        try {
	        	fullAddr = URLEncoder.encode(oriFullAddr, "UTF-8");
	        } catch (UnsupportedEncodingException e1) {
	            e1.printStackTrace();
	        }
			
	        String url = baseUrl;
	        url += "&fullAddr=" + fullAddr + "&appKey=" + TMapInfo.appKey;
			
			final String tmpFullAddr = oriFullAddr;
			final String tmpOrderId = orderId;
			final String tmpOrmNm = ormNm;
			RestService.get(url,new RestServiceCallBack() {
				@Override
				public void onResult(String result) {
					CoordinateWrapper coordinateWrapper = gson.fromJson(result, CoordinateWrapper.class);
					
					if (coordinateWrapper.getCoordinateInfo().getCoordinate().size() == 0) {
						System.out.println(" addr : " + tmpFullAddr + " get geocode error");
					} else {
						String lon = "0",lat = "0";
						
						Coordinate coordinate = coordinateWrapper.getCoordinateInfo().getCoordinate().get(0);
						
						if (!coordinate.getLon().isEmpty()) {
							lon = coordinate.getLon();
							lat = coordinate.getLat();
						}
						
						if (!coordinate.getLonEntr().isEmpty()) {
							lon = coordinate.getLonEntr();
							lat = coordinate.getLatEntr();
						}
						
						if (!coordinate.getNewLon().isEmpty()) {
							lon = coordinate.getNewLon();
							lat = coordinate.getNewLat();
						}
						
						if (!coordinate.getNewLonEntr().isEmpty()) {
							lon = coordinate.getNewLonEntr();
							lat = coordinate.getNewLatEntr();
						}
						
						coordinate.setFullAddr(tmpFullAddr);
						
						TmsOrderBase item = new TmsOrderBase();
						item.setOrderId(tmpOrderId);
						try {
							item.setOrderName(URLEncoder.encode(tmpOrmNm,"UTF-8"));
							item.setAddress(URLEncoder.encode(tmpFullAddr,"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						item.setLatitude(lat);
						item.setLongitude(lon);
						item.setServiceTime("10");
						item.setDeliveryWeight("1");
						
						allItems.add(item);
						
						System.out.println(item.toString());
					}
					
				}
			});
		}
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "stiList", notes = "ERP 배송팀목록 조회")
	@GetMapping("/stiList") 
	public String erp_stiList() { 
        ArrayList<ERPSti> allItems = new ArrayList<ERPSti>();
		List<Map<String, Object>> sTIList = sTIMapper.getSTIList();
		
		for (int i = 0 ; i < sTIList.size() ; i++) {
			ERPSti item = new ERPSti(sTIList.get(i).get("sti_cd").toString(), sTIList.get(i).get("sti_nm").toString(), "", sTIList.get(i).get("com_ctsec").toString(), sTIList.get(i).get("com_scd").toString());
			allItems.add(item);
		}
		
		return gson.toJson(allItems);
	}

	@ApiOperation(value = "zoneList", notes = "ERP 권역목록 조회")
	@GetMapping("/zoneList") 
	public String erp_getZoneList() throws UnsupportedEncodingException { 
		ArrayList<TmsZoneBase> allItems = new ArrayList<TmsZoneBase>();
		List<Map<String, Object>> sTIList = sTIMapper.getZoneList();
		
		for (int i = 0 ; i < sTIList.size() ; i++) {
			if (sTIList.get(i).get("STI_NM") == null) continue;
			TmsZoneBase item = new TmsZoneBase(sTIList.get(i).get("K_STI_CD").toString(), URLEncoder.encode(sTIList.get(i).get("STI_NM").toString(),"UTF-8"));
			allItems.add(item);
		}
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "orderLoadingList", notes = "ERP 건별상차 메인화면")
	@GetMapping("/orderLoadingList")  
	public String erp_orderLoadingList(
			@RequestParam(name="time", required=false) Long time,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date date = new Date(time);
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("date", format.format(date));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        
		ArrayList<ERPItemOrd> allItems = lOADINGORMMapper.getLoadingOrmList(params);
		 
		return gson.toJson(allItems);
	}	 
	
	@ApiOperation(value = "itemLoadingList", notes = "ERP 품목별상차 메인화면")
	@GetMapping("/itemLoadingList")  
	public String erp_itemLoadingList(
			@RequestParam(name="time", required=false) Long time,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
			) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(time);
		
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("date", format.format(date));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        
        ArrayList<ERPItemOrd> allItems = lOADINGORMMapper.getLoadingItemList(params);
		 
		return gson.toJson(allItems);
	}	
	
	
	
	@ApiOperation(value = "orderDetailLoadingList", notes = "ERP 건별상차 디테일 (시공인경우)")
	@GetMapping("/orderDetailLoadingList")  
	public String erp_orderDetailLoadingList(
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
		
		ArrayList<ERPItemOrd> allItems = lOADINGORMDtlMapper.getLoadingOrmDtlList(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "asDetailLoadingList", notes = "ERP 건별상차 디테일 (AS인경우)")
	@GetMapping("/asDetailLoadingList")  
	public String erp_asDetailLoadingList(
			@RequestParam(name="plm_no", required=false) String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
                
		ArrayList<ERPItemOrd> allItems  = lOADINGORMDtlMapper.getLoadingAsDtlList(params);
        
		//notEmpty TEST
        //System.out.println("plm_no =" + plm_no);
        //DataResult allItems = crs0010_p01Mapper.selectIsEmpty(params);        
        //System.out.println("plm_no =" + allItems.getData1());
        
		return gson.toJson(allItems);
	}		
	
	@ApiOperation(value = "selectStiList", notes = "권역장소속 시공팀 조회 리스트 ")
	@GetMapping("/selectStiList")  
	public String erp_selectStiList(
			@RequestParam(name="k_sti_cd", required=true) String k_sti_cd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("k_sti_cd",k_sti_cd);
        
		ArrayList<ERPSti> allItems = sCheduleMainListMapper.selectStiList(params);
		
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "selectStiList2", notes = "권역장소속 시공팀 조회 리스트 ")
	@GetMapping("/selectStiList2")  
	public String selectStiList2(
			@AuthenticationPrincipal User user,
			@RequestParam(name="k_sti_cd", required=false) String k_sti_cd
		) { 
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = apiTmsErpController.getUserEtc(user);	
			params.put("k_sti_cd", etc.getK_sti_cd());
		} else {
			params.put("k_sti_cd", k_sti_cd);
		}
		
		ArrayList<ERPSti> allItems = sCheduleMainListMapper.selectStiList(params);
		
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "selectKStiList", notes = "권역시공팀 조회 리스트 ")
	@GetMapping("/selectKStiList")  
	public String selectKStiList(
			@AuthenticationPrincipal User user,
			@RequestParam(name="com_scd", required=false) String com_scd
		) { 
		HashMap<String,Object> params = new HashMap<String, Object>();
		
		if (user != null) {
			UserEtc etc = apiTmsErpController.getUserEtc(user);	
			params.put("com_scd", etc.getCom_scd());
		} else {
			params.put("com_scd", com_scd);
		}
		
		ArrayList<ERPSti> allItems = sCheduleMainListMapper.selectKStiList(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "scheduleMainList", notes = "시공/as 메인 페이지 조회")
	@GetMapping("/scheduleMainList")  
	public String erp_scheduleMainList(
			@RequestParam(name="time", required=false) Long time,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date date = new Date(time);
		
		HashMap<String,Object> params = new HashMap<String, Object>();
		params.put("date", format.format(date));		
		params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
               
		ArrayList<ERPScheduleList> allItems = sCheduleMainListMapper.selectScheduleMainList(params);
		
		StringBuffer rem_dt_arr = new StringBuffer();
		StringBuffer rem_seq_arr = new StringBuffer();
		StringBuffer lat_arr = new StringBuffer();
		StringBuffer lon_arr = new StringBuffer();
		
		for(int i=0; i<allItems.size(); i++) {
			if ("".equals(allItems.get(i).getLatitude()) || "".equals(allItems.get(i).getLongitude())) {
				
				String lat = "", lon = "", appKey = "l7xx965cfaee1f4c47608284f1271eccb662";
//				TmsGeocodingCoordinateInfoResponse geocoding = apiTmsErpController.Geocoding(appKey, allItems.get(i).getOrm_gaddr());
//	            if (!"200".equals(geocoding.getResultCode())) {
//	            	System.out.println(geocoding.getResultMessage());        			
//	    			return "";
//	            }
//	            if (geocoding.getCoordinateInfo().getTotalCount() > 0) {
//	            	center_X = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon();
//	            	center_Y = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat();
//	    		}
											
				TmsGeocodingCoordinateInfoResponse geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(appKey, TMapInfo.addressFlag_NEW, allItems.get(i).getOrm_gaddr()), TmsGeocodingCoordinateInfoResponse.class);
            	
            	if (!"200".equals(geocoding.getResultCode())) {
            		if ("Bad Request".equals(geocoding.getResultMessage())) {
            			System.out.println("신주소로 다시 요청:" + allItems.get(i).getOrm_gaddr());
            			geocoding = gson.fromJson(mApiTmsController.fullTextGeocoding(appKey, TMapInfo.addressFlag_OLD, allItems.get(i).getOrm_gaddr()), TmsGeocodingCoordinateInfoResponse.class);
            			if (!"200".equals(geocoding.getResultCode())) {
            				System.out.println(geocoding.getResultMessage());        			
    	        			return "";
            			} else {
            				if (geocoding.getCoordinateInfo().getTotalCount() > 0) {
            					lat = geocoding.getCoordinateInfo().getCoordinate().get(0).getLat();
            					lon = geocoding.getCoordinateInfo().getCoordinate().get(0).getLon();
        	        			//arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLon());	
        	        			//arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getLat());
        	        			//System.out.println("Order new lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
        	        		}
            			}
            		} else {
	        			System.out.println(geocoding.getResultMessage());        			
	        			return "";
            		}
            	} else {            	
	        		if (geocoding.getCoordinateInfo().getTotalCount() > 0) {  
	        			lat = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat();
    					lon = geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon();
	        			//arErpOrderList.get(i).setLongitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLon());	
	        			//arErpOrderList.get(i).setLatitude(geocoding.getCoordinateInfo().getCoordinate().get(0).getNewLat());
	        			//System.out.println("Order lon=" + arErpOrderList.get(i).getLongitude() +", lat=" + arErpOrderList.get(i).getLatitude());
	        		}
            	}
	           
            	allItems.get(i).setLongitude(lon);
            	allItems.get(i).setLatitude(lat);
            	
				rem_dt_arr.append(allItems.get(i).getRem_dt() + ",");
            	rem_seq_arr.append(allItems.get(i).getRem_seq() + ",");            	
            	lon_arr.append(lon + ",");
            	lat_arr.append(lat + ",");
			}
		}
		
		if (!"".equals(rem_dt_arr.toString())) {
			TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
			params = new HashMap<String, Object>();
			params.put("rem_dt_arr", rem_dt_arr.toString());
			params.put("rem_seq_arr", rem_seq_arr.toString());
			params.put("lat_arr", lat_arr.toString());
			params.put("lon_arr", lon_arr.toString());
			int res = tmserpScheduling.updateOrderGeocoding(params);
			txManager.commit(status);
			System.out.println("Geocoding Count=" + res);
		}
		
//		try {
//			ComParamLogger.paramToVO("ERPScheduleList", "ERPScheduleList", allItems.get(0));
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "selectSigongMainPage", notes = "시공정보 상세페이지")
	@GetMapping("/selectSigongMainPage")  
	public String erp_selectConstructionInfoPage(
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="rem_seq", required=true) String rem_seq
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("rem_dt",rem_dt);
        params.put("rem_seq",rem_seq);
        
		ERPConstructionMainPage item = sCheduleMainListMapper.selectSigongMainPage(params);
		
		HashMap<String,Object> fParams = new HashMap<String, Object>();
		fParams.put("file_id",item.getFile_id());
		
		
		//hsh
		
		DataResult dataResult = new DataResult();
		dataResult = sCheduleMainListMapper.searchIosFileYn(fParams);
		 
		String iosFileYn = "";
		
		iosFileYn = dataResult.getData1();
		
		String url = "";		
		
		if("Y".equals(iosFileYn)) {
			
			ArrayList<ERPAsFileList> files_0 = sCheduleMainListMapper.selectSigongFileListIos(fParams);
			ERPAsFileList data = new ERPAsFileList();
			


			if(files_0 != null) { 
				data = files_0.get(0);
			
			
				for (int i = 0 ; i < data.getImg_cnt(); i++) {
					
					if(i != 0)
						files_0.add(files_0.get(0));
					
					url = "https://d2p7or5oyjy78v.cloudfront.net/s/"+data.getImg_id()+"_"+String.valueOf(i)+".jpeg";
					System.out.println(url);
		//			url = "http://www.eiloom.com/common/iosfileVW.jsp?&filepath=/ERP_FILE/ios_image/nios/&filename="+tmpItem.getAttch_file_id()+"_"+ i +".png";
					files_0.get(i).setDownload_url(url);
				}
				
				item.setFileList_0(files_0);
			}
			
			
		}

		dataResult = sCheduleMainListMapper.selectSigongFileListCadYn(fParams);
		 
		String CadFileYn = "";
		
		CadFileYn = dataResult.getData1();
		
		if("Y".equals(CadFileYn)) {
			
//			ArrayList<ERPAsFileList> files_1 = sCheduleMainListMapper.selectSigongFileListCad(fParams);
			ArrayList<ERPAsFileList> files_1 = sCheduleMainListMapper.selectSigongFileListCad_new(fParams);
			for (int i = 0 ; i < files_1.size() ; i++) {
				ERPAsFileList tmpItem = files_1.get(i);
				
//				if (tmpItem.getAttch_file_id().toUpperCase().startsWith("F")) {
//	                url = "http://www.efursys.com/common/download_c.jsp?filename="+tmpItem.getAttch_file_nm()+"&filepath=/ERP_FILE/efursys/cspicture_file";
//	            } else {
//	                url = "http://www.eteems.com/common/download_c.jsp?filename="+tmpItem.getAttch_file_nm()+"&filepath=/ERP_FILE/efursys/cspicture_file";
//	            }
				url = "http://erp.fursys.com/fileDownloadConnect.do?param_ATTCH_DIV_CD=&param_GRP_ID="+tmpItem.getGrp_id()+"&param_SEQ="+tmpItem.getSeq()+"";
				
				
				files_1.get(i).setDownload_url(url);
			}
			item.setFileList_1(files_1);
			
		}
		
		
//		ArrayList<ERPAsFileList> files_2 = sCheduleMainListMapper.selectSigongFileList(fParams);
//		for (int i = 0 ; i < files_2.size() ; i++) {
//			ERPAsFileList tmpItem = files_2.get(i);
			
//			url = "http://edms.fursys.com/common/download_c.jsp?filename="+tmpItem.getAttch_file_nm()+"&filepath=/ERP_FILE/doc";
//			files_2.get(i).setDownload_url(url);
//		}
//		item.setFileList_2(files_2);
		
		return gson.toJson(item);
	}
	
	@ApiOperation(value = "selectSigongItemPage", notes = "시공정상품목 상세페이지")
	@GetMapping("/selectSigongItemPage")  
	public String erp_selectConstructionItemInfoPage(
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
        
        ArrayList<ERPConstructionItemPage> allItems = sCheduleMainListMapper.selectSigongItemPage(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "selectSigongGitaItemPage", notes = "시공기타품목 상세페이지")
	@GetMapping("/selectSigongGitaItemPage")  
	public String erp_selectConstructionGitaItemInfoPage(
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
        
        ArrayList<ERPConstructionItemPage> allItems = sCheduleMainListMapper.selectSigongGitaItemPage(params);
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "selectAsMainPage", notes = "AS정보 상세페이지")
	@GetMapping("/selectAsMainPage")  
	public String erp_selectAsInfoPage(
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="rem_seq", required=true) String rem_seq
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("rem_dt",rem_dt);
        params.put("rem_seq",rem_seq);
        
		ERPAsMainPage item = sCheduleMainListMapper.selectAsMainPage(params);
		
		HashMap<String,Object> fParams = new HashMap<String, Object>();
		fParams.put("file_id",item.getFile_id());
		fParams.put("rem_dt",rem_dt);
		fParams.put("rem_seq",rem_seq);
		
		ArrayList<ERPAsFileList> files = sCheduleMainListMapper.selectAsFileList(fParams);
		String url = "";
		for (int i = 0 ; i < files.size() ; i++) {
			ERPAsFileList tmpItem = files.get(i);
			
			url = "https://erp.fursys.com/fileDownload.do?param_ATTCH_DIV_CD=A&param_ATTCH_FILE_ID=" + tmpItem.getAttch_file_id() + "&param_ATTCH_FILE_SNUM=1";
			files.get(i).setDownload_url(url);
		}
		item.setFileList(files);
	
		
		return gson.toJson(item);
	}

	@ApiOperation(value = "selectAsItemPage", notes = "As품목 상세페이지")
	@GetMapping("/selectAsItemPage")  
	public String erp_selectAsItemInfoPage(
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="orm_no", required=true) String orm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
        params.put("orm_no",orm_no);
        
		ArrayList<ERPAsItemPage> allItems = sCheduleMainListMapper.selectAsItemPage(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "selectAsFileList", notes = "As파일 리스트")
	@GetMapping("/selectAsFileList")  
	public String erp_selectAsFileList(
			@RequestParam(name="file_id", required=true) String file_id
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("file_id",file_id);
        
		ArrayList<ERPAsFileList> allItems = sCheduleMainListMapper.selectAsFileList(params);
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "selectSigongFileList", notes = "시공파일 리스트(조립도)")
	@GetMapping("/selectSigongFileList")  
	public String erp_selectSigongFileList(
			@RequestParam(name="file_id", required=true) String file_id
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("file_id",file_id);
        
		ArrayList<ERPAsFileList> allItems = sCheduleMainListMapper.selectSigongFileList(params);
		
		return gson.toJson(allItems);
	}

	@ApiOperation(value = "selectSigongFileListCad", notes = "시공파일 리스트(시공도면)")
	@GetMapping("/selectSigongFileListCad")  
	public String erp_selectSigongFileListCad(
			@RequestParam(name="file_id", required=true) String file_id
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("file_id",file_id);
        
		ArrayList<ERPAsFileList> allItems = sCheduleMainListMapper.selectSigongFileListCad(params);
		
		return gson.toJson(allItems);
	}	
	
	
	@ApiOperation(value = "selectSigongFileListIos", notes = "시공파일 리스트(ios)")
	@GetMapping("/selectSigongFileListIos")  
	public String erp_selectSigongFileListIos(
			@RequestParam(name="file_id", required=true) String file_id
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("file_id",file_id);
        
		ArrayList<ERPAsFileList> allItems = sCheduleMainListMapper.selectSigongFileList(params);
		
		return gson.toJson(allItems);
	}	
	
	
	@ApiOperation(value = "selectSigongAsCnt", notes = "시공AS 실건수 정보 표시")
	@GetMapping("/selectSigongAsCnt")  
	public String erp_selectSigongAsCnt (
			@RequestParam(name="time", required=false) Long time,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date date = new Date(time);
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("date", format.format(date));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        
        System.out.println(params.toString());
        
		ERPScheduleCount allItems = sCheduleMainListMapper.selectScheduleCount(params);
		
		if (allItems == null) {
			allItems = new ERPScheduleCount(0, 0);
		}
		
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "checkFinishYn", notes = "결과등록반영여부 체크")
	@GetMapping("/checkFinishYn")  
	public String erp_checkFinishYn (
			@RequestParam(name="plm_no", required=false) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", plm_no);
 
        ERPResultFinishynCheck allItems = null;
        
        if("AS".equals(com_ssec)) {
        	allItems = sCheduleMainListMapper.selectFinishYnCheckAs(params);
        	
        }else {
        	allItems = sCheduleMainListMapper.selectFinishYnCheckSigong(params);
        }
        
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "erp_insertAddAsInfo", notes = "AS추가 접수처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS추가접수처리 성공") })	
	@GetMapping("/erp_insertAddAsInfo")  
	@RequestMapping(value = "/erp_insertAddAsInfo", method = RequestMethod.GET)
	public String erp_insertAddAsInfo (
			@RequestParam(name="rpt_no", required=false) String rpt_no,
			@RequestParam(name="rpt_desc", required=true) String rpt_desc,
			@RequestParam(name="req_std", required=true) String req_std,
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="req_dt", required=true) String req_dt
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			
			HashMap<String, Object> params;
			params = new HashMap<String, Object>();
			params.put("rpt_no", rpt_no); 
									
			dataResult = erpAsAddInfoInsertMapper.searchMaxSeq(params);
			
			String maxRptSeq = "";
			
			if (dataResult != null) {
				maxRptSeq = dataResult.getData1();
			}
			
			params = new HashMap<String, Object>();
			params.put("rpt_no", rpt_no);
			params.put("rpt_seq", maxRptSeq);	
			params.put("rpt_desc", rpt_desc);
			res = erpAsAddInfoInsertMapper.selInsertRptReq(params);			
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selInsertRptReq 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			//최종으로 ta_plandtl mobile 상태값 처리			
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("req_std", req_std);	
			params.put("req_dt", req_dt);	
			
			res = erpAsAddInfoInsertMapper.updateAddAsMobileStatus(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsMobileStatus 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			//사진파일 UPLOAD처리
			//(1)FILE 키번호 가져오기 			
			String str_fileId = "";			
			params = new HashMap<String, Object>();
			params.put("gubun", 'A');
			
			dataResult = erpAsAddInfoInsertMapper.sp_get_filekey(params);			
			if (dataResult == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("sp_get_filekey 오류");
				return gson.toJson(response);
			}
			
			str_fileId = dataResult.getData1();			
			
			params = new HashMap<String, Object>();
			params.put("file_id", str_fileId);
			params.put("rpt_no", rpt_no);
			params.put("rpt_seq", maxRptSeq);

			res = erpAsAddInfoInsertMapper.updateAsAddFileId(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAsAddFileId 오류 [" + res + "]");
				return gson.toJson(response);
			}
			
			//종료시간 UPDATE
			//최종으로 ta_plandtl mobile 상태값 처리					
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("file_id", str_fileId);
			
			res = erpAsAddInfoInsertMapper.updateAddEndTimeStatus(params);						
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddEndTimeStatus 오류 [" + res + "]");
				return gson.toJson(response);
			}			
			
			ERPAsResult asresult = new ERPAsResult();
			asresult.setFileid(str_fileId);
			response.setAsResult(asresult);
			
			ComParamLogger.paramToVO("ERPAsResult", "ERPAsResult", asresult);
			
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

	@ApiOperation(value = "erp_updateStartTm", notes = "시작시간 Update")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시작시간 Update 실패") })	
	@GetMapping("/erp_updateStartTm")  
	@RequestMapping(value = "/erp_updateStartTm", method = RequestMethod.GET)
	public String erp_updateStartTm (

			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        System.out.println("com_ssec: " + com_ssec);
	        if("AS".equals(com_ssec)) {
	        	res = sCheduleMainListMapper.updateAddAsStartTime(params);
	        	
	        }else {
	        	res = sCheduleMainListMapper.updateAddSigongStartTime(params);
	        }
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("시작시간 update 오류 [" + res + "]");
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
	
	@ApiOperation(value = "erp_selectMiguelMainList", notes = "시공/AS 미결리스트 검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공/AS 미결리스트 검색 실패 !!") })
	@GetMapping("/erp_selectMiguelMainList")
	@RequestMapping(value = "/erp_selectMiguelMainList", method = RequestMethod.GET)
	public String erp_selectMiguelMainList(
			@RequestParam(name="time1", required=false) Long time1,
			@RequestParam(name="time2", required=false) Long time2,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date date1 = new Date(time1);
		Date date2 = new Date(time2);
        
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("date1", format.format(date1));
        params.put("date2", format.format(date2));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
		
		ArrayList<ERPMigeulList> arrList = mIgeulListMapper.selectMigeulList(params);

		return gson.toJson(arrList);		
		
	}
	
	@ApiOperation(value = "erp_selectMiguelDetailList", notes = "시공/AS 미결 detail 리스트 검색")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공/AS 미결 detail 리스트 검색 !!") })
	@GetMapping("/erp_selectMiguelDetailList")
	@RequestMapping(value = "/erp_selectMiguelDetailList", method = RequestMethod.GET)
	public String erp_selectMiguelDetailList(
			@RequestParam(name="com_ssec", required=false) String com_ssec,
			@RequestParam(name="plm_no", required=true) String plm_no
		) { 

		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
		
        ArrayList<ERPMigeulDetailList> arrList = null;
        
        if(com_ssec.equals("시공")) {
        	 arrList = mIgeulListMapper.selectMigeulSigongDetailList(params);
        } else {
        	arrList = mIgeulListMapper.selectMigeulAsDetailList(params);
        }
        
		return gson.toJson(arrList);		
		
	}

	@ApiOperation(value = "selectMigeulAverageCount", notes = "시공AS 실건수 정보 표시")
	@GetMapping("/selectMigeulAverageCount")  
	public String erp_selectMigeulAverageCount (
			@RequestParam(name="ftime", required=false) Long ftime,
			@RequestParam(name="ttime", required=false) Long ttime,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date fdate = new Date(ftime);
        Date tdate = new Date(ttime);		
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("fdate", format.format(fdate));
        params.put("tdate", format.format(tdate));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        
        System.out.println(params.toString());
        
		ERPMigeulAverage allItems = mIgeulListMapper.selectMigeulAverageCount(params);
		
		if (allItems == null) {
			allItems = new ERPMigeulAverage(0, 0);
		}
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_selectReReserveInfo", notes = "시공/AS 재예약 리스트")
	@GetMapping("/erp_selectReReserveInfo")  
	public String erp_selectReReserveInfo(
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="com_ssec", required=false) String com_ssec
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("orm_no",orm_no);
        
		ArrayList<ERPReReserveInfo> allItems = null;

        if(com_ssec.equals("시공")) {
        	allItems = sCheduleMainListMapper.selectSigongReReserveInfo(params);
        } else {
    	    allItems = sCheduleMainListMapper.selectAsReReserveInfo(params);
        }		
		
		try {
			ComParamLogger.paramToVO("ERPReReserveInfo", "ERPReReserveInfo", allItems.get(0));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return gson.toJson(allItems);
	}
	

	@ApiOperation(value = "selectGubbunYnInfo", notes = "현장배차 정보 확인")
	@GetMapping("/selectGubbunYnInfo")  
	public String erp_selectGubbunYnInfo (
			@RequestParam(name="orm_no", required=false) String orm_no
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("orm_no",orm_no);
        
        System.out.println(params.toString());
        
        ERPGubbunInfo allItems = sCheduleMainListMapper.selectGubbunYnInfo(params);
		
		
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "asLoadingBeforeMinus", notes = "상차전 미결의 경우 (-) 지급 insert")
	@GetMapping("/asLoadingBeforeMinus")  
	public String erp_asLoadingBeforeMinus (
			@RequestParam(name="plm_no", required=false) String plm_no,
			@RequestParam(name="mob_std", required=false) String mob_std,
			@RequestParam(name="user_id", required=true) String user_id
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        params.put("user_id",user_id);			
			
	        
	        if (mob_std.equals("C67001")) {
	        	res = erppraacctlistsaveMapper.asLoadingBeforeMinus(params);
	        	
				if (res < 1) {    				
					res_msg =  "asLoadingBeforeMinus 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}	        	
	        }
		}	
		catch (Exception e) {
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

	
	@ApiOperation(value = "erp_asAddFileInfo", notes = "AS추가 접수 사진경로 저장 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "AS추가 접수 사진경로 실패") })	
	@GetMapping("/erp_asAddFileInfo")  
	@RequestMapping(value = "/erp_asAddFileInfo", method = RequestMethod.POST)		
	public String erp_asAddFileInfo (
			@RequestParam(name="file_id", required=false) String file_id,
			@RequestParam(name="file_nm", required=true) String file_nm,
			@RequestParam(name="user_id", required=true) String user_id,
			@RequestParam(name="file1", required=true) MultipartFile file1
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			String fileName = "", baseDir = "", yyyymmdd = "";
			file_nm = file_id + "." + file_nm;
			
			HashMap<String, Object> params;
			params = new HashMap<String, Object>();
			params.put("file_id", file_id);
			params.put("file_nm", file_nm);
			params.put("user_id", user_id);
			
			if(!file1.isEmpty()){ //첨부파일이 존재하면
				//첨부파일의 이름								
				try{
					Path path = null;
					fileName = file1.getOriginalFilename();
					
					dataResult = erpAsAddInfoInsertMapper.searchToday(params);
					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("searchToday 오류");
						return gson.toJson(response);
					} else {
						yyyymmdd = dataResult.getData1();
					}						
					
					if (OSValidator.isWindows()) {
						baseDir = "C:\\ServerFiles\\" + yyyymmdd;
						fileName = baseDir + "\\"+ file_nm;
					} else {						
						baseDir = ServerInfo.AS_ATTACHED_FILE_PATH + yyyymmdd;
						fileName = baseDir + "/"+ file_nm;						
					}
					path = Paths.get(baseDir);
					if (!Files.isDirectory(path)) { // 해당 디렉토리가 없다면
						Files.createDirectories(path); // 폴더 생성
					}
					//지정된 업로드 경로로 저장됨
					file1.transferTo(new File(fileName));
					
				} catch (Exception e) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("erp_asAddFileInfo 사진UPLOAD 오류 [" + e.toString() + "]");
					return gson.toJson(response);
				}
			} else {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_asAddFileInfo 오류 [첨부파일 정보가 존재하지 않습니다.]");
				return gson.toJson(response);
			}
			
			res = erpAsAddInfoInsertMapper.insertAsAddFileInfo(params);			
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertAsAddFileInfo 오류 [" + res + "]");
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

	@ApiOperation(value = "asPaymentInsert", notes = "AS결재정보 저장 (신규건 insert)")
	@GetMapping("/asPaymentInsert")  
	public String erp_asPaymentInsert (
			@ApiParam(value = "시공팀코드", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String sti_cd,			
			@ApiParam(value = "RPT NO", required=true, example = "20201204ABC")
			@RequestParam(name="rpt_no", required=true) String rpt_no,			
			@ApiParam(value = "RPT SEQ", required=true, example = "01")
			@RequestParam(name="rpt_seq", required=true) String rpt_seq,			
			@ApiParam(value = "고객명", required=true, example = "강나라")
			@RequestParam(name="cust_name", required=true) String cust_name,			
			@ApiParam(value = "카드번호", required=true, example = "")
			@RequestParam(name="cardno", required=true) String cardno,			
			@ApiParam(value = "유효기간", required=true, example = "")
			@RequestParam(name="expd", required=true) String expd,
			@ApiParam(value = "할부기간", required=true, example = "0")
			@RequestParam(name="installment", required=true) String installment,
			@ApiParam(value = "결재금액", required=true, example = "1004")
			@RequestParam(name="pay_amt", required=true) Long pay_amt,
			@ApiParam(value = "비고", required=false, example = "")
			@RequestParam(name="ipg_remark", required=false) String ipg_remark
			
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		String goodsName = "AS조치비용";
		SpcResponse spc_res = null;
		String orderno = "";
		try {
			
			params = new HashMap<String, Object>();
			params.put("rpt_no", rpt_no);
	        params.put("rpt_seq", rpt_seq);
			dataResult = erpAsPaymentMapper.selectPaymentOrderNo(params);			
			if (dataResult == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("카드승인요청 채번에 실패하였습니다.");
				return gson.toJson(response); 
			}
			orderno = dataResult.getData1();
			
			spc_res = SpcAPI.card(goodsName, cust_name, cardno, expd, installment, ""+pay_amt, orderno);
			if (!"200".equals(spc_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("카드승인요청 오류 [" + spc_res.getResultMessage() + "]");
				return gson.toJson(response);				
			}
			
			//System.out.println(gson.toJson(spc_res));
			
			//spc_res =  new SpcResponse();
			spc_res.setIssueCardName("신용카드");
//			spc_res.setCardno(cardno);
			
			System.out.println("3333333");
			System.out.println(spc_res.getRefNo());
			
			
			ipg_remark = spc_res.getIssueCardName() + " " + spc_res.getCardno() + " " + ("0".equals(installment) ? "일시불" : installment+"개월");
			params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);	        
	        params.put("rpt_no", rpt_no);
	        params.put("rpt_seq", rpt_seq);			
	        params.put("pay_amt", pay_amt);	
	        params.put("ipg_remark", ipg_remark);
	        
	        dataResult = erpAsPaymentMapper.selectPaymentCheck(params);
			
			String paymentCheck = "";
			
			if (dataResult != null) {
				paymentCheck = dataResult.getData1();
			}
			
			
			if (paymentCheck.equals("N")) {
				//AS입금처리가 1건도 기존에 없었던 경우
				
				res = erpAsPaymentMapper.asPaymentMasterInsert(params);
				
				if (res < 1) {
					spc_res = SpcAPI.cancel(spc_res.getRefNo(), spc_res.getTranDate(), ""+pay_amt, goodsName, orderno);
					if (!"200".equals(spc_res.getResultCode())) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("카드승인취소요청 오류1 [" + spc_res.getResultMessage() + "]");
						return gson.toJson(response);				
					}
					
					res_msg =  "asPaymentMasterInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}					
						
				System.out.println("--------");
				System.out.println(spc_res.getRefNo());
				params.put("card_appyno", spc_res.getRefNo()); //카드승인번호
				res = erpAsPaymentMapper.asPaymentDetailInsert(params);
				
				if (res < 1) {					
					spc_res = SpcAPI.cancel(spc_res.getRefNo(), spc_res.getTranDate(), ""+Math.abs(pay_amt), goodsName, orderno);
					if (!"200".equals(spc_res.getResultCode())) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("카드승인취소요청 오류2 [" + spc_res.getResultMessage() + "]");
						return gson.toJson(response);				
					}
					
					res_msg =  "asPaymentDetailInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}					
				
			}else {
				//AS입금처리가 기존에 있어 추가로 입금처리 해야하는 경우
				System.out.println("--------");
				System.out.println(spc_res.getRefNo());				
				params.put("card_appyno", spc_res.getRefNo()); //카드승인번호
				res = erpAsPaymentMapper.asPaymentDetailInsert(params);
				
				if (res < 1) {
					spc_res = SpcAPI.cancel(spc_res.getRefNo(), spc_res.getTranDate(), ""+pay_amt, goodsName, orderno);
					if (!"200".equals(spc_res.getResultCode())) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("카드승인취소요청3 오류 [" + spc_res.getResultMessage() + "]");
						return gson.toJson(response);				
					}
					
					res_msg =  "asPaymentDetailInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);				
				}
				
			}
			
			response.setMappingKey(spc_res.getRefNo());
		}	
		
		catch (Exception e) {
			spc_res = SpcAPI.cancel(spc_res.getRefNo(), spc_res.getTranDate(), ""+pay_amt, goodsName, orderno);
			if (!"200".equals(spc_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("카드승인취소요청 오류1 [" + spc_res.getResultMessage() + "]");
				return gson.toJson(response);				
			}
			
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
		
	@ApiOperation(value = "asPaymentInsertCancel", notes = "AS결재정보 저장 (취소정보)")
	@GetMapping("/asPaymentInsertCancel")  
	public String erp_asPaymentInsertCancel (
			@ApiParam(value = "시공팀코드", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String sti_cd,			
			@ApiParam(value = "RPT NO", required=true, example = "20201204ABC")
			@RequestParam(name="rpt_no", required=true) String rpt_no,
			@ApiParam(value = "RPT SEQ", required=true, example = "01")
			@RequestParam(name="rpt_seq", required=true) String rpt_seq,
			@ApiParam(value = "결재금액", required=true, example = "1004")
			@RequestParam(name="pay_amt", required=true) Long pay_amt,
			@ApiParam(value = "비고", required=false, example = "")
			@RequestParam(name="ipg_remark", required=false) String ipg_remark,
			@ApiParam(value = "승인번호", required=true, example = "")
			@RequestParam(name="card_appyno", required=true) String card_appyno,
			@ApiParam(value = "결제일자", required=true, example = "")
			@RequestParam(name="apt_ymd", required=true) String apt_ymd
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		
		try {
			pay_amt = (-1) * pay_amt;
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);	        
	        params.put("rpt_no", rpt_no);
	        params.put("rpt_seq", rpt_seq);
	        params.put("pay_amt", pay_amt);
	        params.put("ipg_remark",ipg_remark);
	        
	        dataResult = erpAsPaymentMapper.selectPaymentCheck(params);
			
			String paymentCheck = "";
			
			if (dataResult != null) {
				paymentCheck = dataResult.getData1();
			}
			
			String goodsName = "AS조치비용";
			SpcResponse spc_res = null;
			String orderno = rpt_no + rpt_seq;
			spc_res = SpcAPI.cancel(card_appyno, apt_ymd.substring(2), ""+Math.abs(pay_amt), goodsName, orderno);
			if (!"200".equals(spc_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("카드취소요청 오류[" + spc_res.getResultMessage() + "]");
				return gson.toJson(response);				
			}
			ipg_remark = spc_res.getIssueCardName();
			params.put("card_appyno", spc_res.getRefNo()); //카드승인번호
			params.put("ipg_remark",ipg_remark);
			
			if (paymentCheck.equals("N")) {
				//AS입금처리가 1건도 기존에 없었던 경우
				
				res = erpAsPaymentMapper.asPaymentMasterInsert(params);
				
				if (res < 1) {    				
					res_msg =  "asPaymentMasterInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}					
				
				res = erpAsPaymentMapper.asPaymentDetailInsert(params);
				
				if (res < 1) {    				
					res_msg =  "asPaymentDetailInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}					
				
			}else {
				//AS입금처리가 기존에 있어 추가로 입금처리 해야하는 경우
				
				res = erpAsPaymentMapper.asPaymentDetailInsert(params);
				
				if (res < 1) {    				
					res_msg =  "asPaymentDetailInsert 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);				
				}
				
			}
			
			ipg_remark = "(결재취소완료)";			
			params.put("ipg_rmk",ipg_remark);
			params.put("card_appyno", card_appyno);
			res = erpAsPaymentMapper.asPaymentDetailUpdate(params);
			
			if (res < 1) {    				
				res_msg =  "asPaymentDetailUpdate 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);				
			}
			
			response.setMappingKey(spc_res.getRefNo());
			
		}	
		
		catch (Exception e) {
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

	@ApiOperation(value = "mobileCmKakaotalkInsert", notes = "카카오알림톡 발송")
	@GetMapping("/mobileCmKakaotalkInsert")  
	public String erp_mobileCmKakaotalkInsert (
			@RequestParam(name="com_brand_cd", required=false) String com_brand_cd,
			@RequestParam(name="send_gubun", required=false) String send_gubun,
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="orm_hdphone", required=true) String orm_hdphone,
			@RequestParam(name="orm_nm", required=true) String orm_nm,
			@RequestParam(name="sti_nm", required=true) String sti_nm,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="biztalkmessage", required=true) String biztalkmessage
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		
		try {

	    	JSONObject obj = new JSONObject();
	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	 
	    	JSONObject api_token = new JSONObject();
	    	JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	
	    	String templateCode = "";
	    	String senderkey = "";
	    	String title = "";
	    	String subject = "";
	    	String from_no = "";
	    	String message_type = "";
	    	
//	    	biztalkmessage = "안녕하세요 고객님.\r\n" + 
//	    			"금일 방문예정인 {홍길동} 팀입니다.\r\n" + 
//	    			"앞서 진행중인 작업 지연으로\r\n" + 
//	    			"방문시간보다 다소 지연 예상됩니다.\r\n" + 
//	    			"\r\n" + 
//	    			"다시한번 양해 부탁드립니다\r\n" + 
//	    			"\r\n" + 
//	    			"시공팀 연락처\r\n" + 
//	    			"{010-3652-9837}";
	    	
	    	
    		if("A".equals(send_gubun) ) {
    			
    			title = "지연안내";
    			subject = "지연안내";
    			message_type = "TI42";
    			
    		} else if ("B".equals(send_gubun) ) {
    			title = "교통상황안내";
    			subject = "교통상황안내";
    			message_type = "TI42";
    			
    		} else if ("C".equals(send_gubun) ) {
    			title = "도착안내";
    			subject = "도착안내";
    			message_type = "TI41";
    		}	    	
	    	
	    	//senmd_gubun : A(지연안내), B(교통상황안내), C(도착안내)
	    	//퍼시스브랜드
	    	if("T60F01".equals(com_brand_cd)) {
	    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
	    		from_no = "1588-1244";
	    		
	    		if("A".equals(send_gubun) ) {
	    			
	    			templateCode = "fursysdelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic06";

	    			
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "fursysarrival";
	    		}
	
	    	}
	    	
	    	//일룸브랜드
	    	if("T60I01".equals(com_brand_cd)) {
	    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
	    		from_no = "1577-5670";
	    		if("A".equals(send_gubun) ) {
	    			templateCode = "iloomdelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic05";
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "iloomarrival";
	    			
	    		}	    		
	    		
	    	}	    	
	    	
	    	//데스커브랜드
	    	if("T60I02".equals(com_brand_cd)) {
	    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
	    		from_no = "1588-1662";
	    		
	    		if("A".equals(send_gubun) ) {
	    			templateCode = "deskerdelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic02";
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "deskerarrival";
	    			
	    		}	    		
	    		
	    	}	   
	    	
	    	//슬로우브랜드
	    	if("T60I03".equals(com_brand_cd)) {	    		
	    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
	    		from_no = "1899-8588";
	    	
	    		if("A".equals(send_gubun) ) {
	    			templateCode = "sloudelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic03";
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "slouarrival";
	    			
	    		}	    		
	    	}	    	
	    	
	    	//시디즈브랜드
	    	if("T60P01".equals(com_brand_cd)) {
	    		
	    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
	    		from_no = "1577-5674";
	    		
	    		if("A".equals(send_gubun) ) {
	    			templateCode = "sidizdelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic01";
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "sidizarrival";
	    			
	    		}	    		
	    	}	    	
	    	
	    	//알로소브랜드
	    	if("T60P02".equals(com_brand_cd)) {
	    		
	    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";
	    		from_no = "1577-1641";
	    		
	    		if("A".equals(send_gubun) ) {
	    			templateCode = "allosodelay";
	    			
	    		} else if ("B".equals(send_gubun) ) {
	    			templateCode = "fursystraffic04";
	    			
	    		} else if ("C".equals(send_gubun) ) {
	    			templateCode = "allosoarrival";
	    			
	    		}	    		
	    	}		    	
	    	
//	    	String bistalkMessage = "안녕하세요 고객님.~r~n금일 방문예정인 황성현 팀입니다.~r~n앞서 진행중인 작업 지연으로~r~n방문시간보다 다소 지연 예상됩니다.~r~n~r~n다시한번 양해 부탁드립니다~r~n~r~n시공팀 연락처~r~n010-36529837";
	    	
	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
			
	       	 for (int i = 0; i < 1; i++)//배열
	       	 {
		        	 
		        	 
		        	 sObject.put("sendDiv", "BIZTALK" );
		        	 sObject.put("title", title);
		        	 sObject.put("subject", subject );		  
		        	 sObject.put("message","");
		        	 sObject.put("fromNm", orm_nm );
		        	 sObject.put("toNm", sti_nm );
		        	 sObject.put("fromNo", from_no ); 
		        	 sObject.put("toNo", orm_hdphone);
		        	 sObject.put("companyCd", "T01B" );		        	 
		        	 sObject.put("fstUsr", sti_cd );
		        	 sObject.put("systemNm", "mobilecm" );
		        	 sObject.put("sendType", "SMTP" );
		        	 sObject.put("reserveDiv","I");
		        	 sObject.put("reserveDt", "" );
		        	 sObject.put("keyNo", orm_no);
		        	 sObject.put("msgType", message_type );		        	 
		        	 sObject.put("senderKey", senderkey);
		        	 sObject.put("templateCode", templateCode );
		        	 sObject.put("bizTalkMessage", biztalkmessage );
		        	 sObject.put("comBrd", com_brand_cd );
//		        	 sObject.put("bizTalkMessage", biztalkmessage );
		        	 jArray.add(sObject);
	       	 }        	 
        	
        			
        	sendList.put("list" ,jArray);  
        	 
//        	RestCall("http://localhost:8082/intgmsg.do",sendList);
//			https://msg-api.fursys.com/v1/api/message/SendMsg
//			"http://erp-api.fursys.com/intgmsg.do"
        	
        	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
        	if (!"200".equals(kakao_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
				return gson.toJson(response);
			}
        	
		}	
		
		catch (Exception e) {
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
	
	
    private BaseResponse RestCall(String paramUrl,JSONObject jsonObject){
    	BaseResponse res = new BaseResponse();
    	try {
            URL url = new URL(paramUrl);
           
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
           // conn.setRequestProperty("X-Auth-Token", API_KEY);            
            conn.setRequestProperty("X-Data-Type", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            if (conn.getResponseCode() != 200) {
                System.out.println("Failed: HTTP error code : " + conn.getResponseCode());
            	//throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            	res.setResultCode("5001");
            	res.setResultMessage("Failed: HTTP error code : " + conn.getResponseCode());
            } else {
                System.out.println("발송 성공");
                res.setResultCode("200");
            	res.setResultMessage("");
            }
            
            String line = null;
            while((line = br.readLine()) != null){
                System.out.println(line);
            }            
            br.close();            
            conn.disconnect();
            
        } catch (IOException e) {        	
//            System.out.println("RestCall Fail : " + e.getMessage());
//            res.setResultCode("5001");
//        	res.setResultMessage("RestCall Fail : " + e.getMessage());
        	
            System.out.println("발송 성공");
            res.setResultCode("200");
            res.setResultMessage("");
        	
        	return res;
        }
    	
    	return res;
    }	
	
	@ApiOperation(value = "selectSigongItemResultPage", notes = "시공결과등록 미결 품목페이지")
	@GetMapping("/selectSigongItemResultPage")  
	public String erp_selectSigongItemResultPage(
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="gubun", required=true) String gubun
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no",plm_no);
        params.put("gubun",gubun);
        
        ArrayList<ERPConstructionItemResultPage> allItems;
        if ("A".equals(gubun)) {
        	allItems = sCheduleMainListMapper.selectSigongItemNormalResultPage(params);
        } else {
        	allItems = sCheduleMainListMapper.selectSigongItemResultPage(params);	
        }
        		
		return gson.toJson(allItems);
	}    

	@ApiOperation(value = "sigongBanpumAgtRequest", notes = "대리점 반품의뢰 요청!")
	@GetMapping("/sigongBanpumAgtRequest")  
	public String erp_sigongBanpumAgtRequest (
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="user_id", required=true) String user_id,
			
			@RequestParam(name="set_cd_arr", required=true) String set_cd_arr,
			@RequestParam(name="col_scd_arr", required=true) String col_scd_arr,
			@RequestParam(name="itm_cd_arr", required=true) String itm_cd_arr,
			@RequestParam(name="col_cd_arr", required=true) String col_cd_arr,
			@RequestParam(name="req_qty_arr", required=true) String req_qty_arr
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        params.put("sti_cd", sti_cd);
	        params.put("set_cd_arr",set_cd_arr);			
	        params.put("col_scd_arr",col_scd_arr);
	        params.put("itm_cd_arr",itm_cd_arr);
	        params.put("col_cd_arr",col_cd_arr);
	        params.put("req_qty_arr",req_qty_arr);
	        params.put("user_id",user_id);
	        
	        
        	res = sCheduleMainListMapper.sigongBanpumAgtRequestInsert(params);
        	
			if (res < 1) {    				
				res_msg =  "sigongBanpumAgtRequestInsert 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}
		}	
		catch (Exception e) {
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

	@ApiOperation(value = "erp_UpdateArrivalTime", notes = "고객도착시간 저장")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "고객도착시간 저장 실패") })	
	@GetMapping("/erp_UpdateArrivalTime")  
	@RequestMapping(value = "/erp_UpdateArrivalTime", method = RequestMethod.POST)		
	public String erp_UpdateArrivalTime (			
			@ApiParam(value="시공팀코드", required = true, example = "YA142")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value="시공일자", required = true, example = "20201102")
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@ApiParam(value="일련번호", required = true, example = "001")
			@RequestParam(name="rem_seq", required=true) String as_rem_seq,
			@ApiParam(value="도착시간", required = true, example = "0900")
			@RequestParam(name="req_time", required=true) String req_time
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		String ccd_nm = "";
		try {
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("sti_cd", as_sti_cd);
			params.put("rem_dt", as_rem_dt);
			params.put("rem_seq", as_rem_seq);
			params.put("req_time", req_time);
			
			res = sCheduleMainListMapper.updateStiReqTime(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("도착요청시간 등록 오류  [" + res + "]");
				return gson.toJson(response);
			}	
			
			ccd_nm = (String) params.get("ccd_nm");
			
			//System.out.println("ccd_nm : " + ccd_nm);
			
			res = sCheduleMainListMapper.updateStiReqTimeSort(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("도착요청시간정렬 등록 오류  [" + res + "]");
				return gson.toJson(response);
			}	
			
			response.setData1(ccd_nm);
			
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
	
	@ApiOperation(value = "erp_StiArrivalReqTimeSendKakao", notes = "방문예정시간알림톡 발송")
	@GetMapping("/erp_StiArrivalReqTimeSendKakao")  
	public String erp_StiArrivalReqTimeSendKakao (
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="stm_hp", required=true) String stm_hp
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult(); 
		
		try {
			
			
	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	 
	    	JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	
	    	StringBuffer rem_dt_arr = new StringBuffer();
	    	StringBuffer rem_seq_arr = new StringBuffer();
	    	
	    	String templateCode = "";
	    	String senderkey = "";
	    	String title = "";
	    	String subject = "";
	    	String from_no = "";
	    	String orm_nm = "";
	    	String orm_hdphone = "";
	    	String orm_no = "";
	    	String sti_nm = "";
	    	String biztalkmessage = "";
	    	String com_ssec = "";
	    	String com_agsec = "";
	    	String rem_ftm = "";
	    	String com_brand_cd = "";
	    	String com_rmfg = "";
	    	String sendDiv = "";
	    	String message = "";
	    	String send_rem_dt = "";
	    	
			title = "방문시간안내";
			subject = "방문시간안내";			   	

	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");

			HashMap<String,Object> fParams = new HashMap<String, Object>();
			fParams.put("rem_dt",rem_dt);	    	
			fParams.put("sti_cd",sti_cd);
			fParams.put("com_scd",com_scd);
			
			ArrayList<ERPStiReqTimeSendList> arrList = sCheduleMainListMapper.selectStiReqTimeSendListCheck(fParams);
			if (arrList.size() > 0) {
				StringBuffer orm_nm_arr = new StringBuffer();				
				for (int i=0; i<arrList.size(); i++) {
					if (i == 0) {
						orm_nm_arr.append(arrList.get(i).getOrm_nm() + "님");
					} else {
						orm_nm_arr.append("," + arrList.get(i).getOrm_nm() + "님");
					}
				}
				orm_nm_arr.append("의 방문시간을 등록해 주세요.");
				
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(orm_nm_arr.toString());
				return gson.toJson(response);
			}
			
			arrList = sCheduleMainListMapper.selectStiReqTimeSendList(fParams);
//			if (arrList.size() < 1) {
//				txManager.rollback(status);
//				response.setResultCode("5001");
//				response.setResultMessage("MESSAGE 발송 대상건이 없습니다. ");
//				return gson.toJson(response);
//			}
			
			for (int i = 0 ; i < arrList.size() ; i++) {
				
				ERPStiReqTimeSendList tmpItem = arrList.get(i);

				rem_dt_arr.append(tmpItem.getRem_dt() + ",");
				rem_seq_arr.append(tmpItem.getRem_seq() + ",");
			
				com_rmfg = tmpItem.getCom_rmfg();
				
				if ("C13W".equals(com_rmfg)) {
				
					orm_nm = tmpItem.getOrm_nm();
					sti_nm = tmpItem.getSti_nm();
					orm_hdphone = tmpItem.getOrm_hdphone();
//					orm_hdphone = "010-3652-9837";
					sti_cd = tmpItem.getSti_cd();
					orm_no = tmpItem.getOrm_no();
					com_ssec = tmpItem.getCom_ssec();
					com_agsec = tmpItem.getCom_agsec();
					rem_ftm = tmpItem.getRem_ftm();
					com_brand_cd = tmpItem.getCom_brand_cd();		
					
					System.out.println(com_brand_cd);
					
					//안심번호 이면 문자로 발송되도록 변경
					
//					if(orm_hdphone.substring(0,2).equals("050")) {
//						sendDiv = "SMS" ;
//						
//						if(com_agsec.equals("C02F")) {
//							
//						}else if ( ) {
//							
//						}
//						
//						
//					}else {
//						sendDiv = "BIZTALK" ;
//					}
					
			    	//퍼시스브랜드
			    	if("T60F01".equals(com_brand_cd)) {
			    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
			    		from_no = "1588-1244";
			    		
			    		templateCode = "fursysarrivalpromise2";
			
			    	}
			    	
			    	//일룸브랜드
			    	if("T60I01".equals(com_brand_cd)) {
			    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
			    		from_no = "1577-5670";
			    		
			    		templateCode = "iloomarrivalpromise3";    	
			    		
			    	}	    	
			    	
			    	//데스커브랜드
			    	if("T60I02".equals(com_brand_cd)) {
			    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
			    		from_no = "1588-1662";
			    		
			    		templateCode = "deskerarrivalpromise2";   	    		
			    		
			    	}	   
			    	
			    	//슬로우브랜드
			    	if("T60I03".equals(com_brand_cd)) {	    		
			    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
			    		from_no = "1899-8588";
			    	
			    		templateCode = "slouarrivalpromise2";     		
			    	}	    	
			    	
			    	//시디즈브랜드
			    	if("T60P01".equals(com_brand_cd)) {
			    		
			    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
			    		from_no = "1577-5674";
			    		
			    		templateCode = "sidizarrivalpromise2";      		
			    	}	    	
			    	
			    	//알로소브랜드
			    	if("T60P02".equals(com_brand_cd)) {
			    		
			    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";
			    		from_no = "1577-1641";
			    		
			    		templateCode = "allosoarrivalpromise2";     		
			    	}		    	
					
					
					HashMap<String, Object> params;
					params = new HashMap<String, Object>();
					params.put("com_ssec", com_ssec);
					params.put("com_agsec", com_agsec);
					params.put("com_brand_cd", com_brand_cd);
					
					dataResult = sCheduleMainListMapper.selectKakaotalkMessage(params);					
					
					System.out.println(dataResult);
					
					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("MESSAGE 발송 오류 (카카오톡문제)");
						return gson.toJson(response);
					}				
					
					biztalkmessage = dataResult.getData1();	
					
					sObject = new JSONObject();
		        	sObject.put("sendDiv", "BIZTALK" );
		        	sObject.put("title", title);
		        	sObject.put("subject", subject );		  
		        	sObject.put("message","");
		        	sObject.put("fromNm", orm_nm );
		        	sObject.put("toNm", sti_nm );
		        	sObject.put("fromNo", from_no ); 
		        	sObject.put("toNo", orm_hdphone);
		        	sObject.put("companyCd", "T01B" );		        	 
		        	sObject.put("fstUsr", sti_cd );
		        	sObject.put("systemNm", templateCode );
		        	sObject.put("sendType", "SMTP" );
		        	sObject.put("reserveDiv","I");
		        	sObject.put("reserveDt", "" );
		        	sObject.put("keyNo", orm_no);
		        	sObject.put("msgType", "TI4G" );		        	 
		        	sObject.put("senderKey", senderkey);
		        	sObject.put("templateCode", templateCode );
		        	sObject.put("comBrd", com_brand_cd );
		        	
		        	biztalkmessage = biztalkmessage.replace("{$1}", sti_nm);	
		        	biztalkmessage = biztalkmessage.replace("{$2}", rem_ftm);	
		        	biztalkmessage = biztalkmessage.replace("{$3}", stm_hp);
		        	
		        	send_rem_dt = rem_dt.substring(0, 4) + "년 " + rem_dt.substring(4,6) + "월 " + rem_dt.substring(6, 8) + "일";
		        	
		        	biztalkmessage = biztalkmessage.replace("{$4}", send_rem_dt);
		        	
		        	sObject.put("bizTalkMessage", biztalkmessage );
		        
		        	jArray.add(sObject);						
					
				}
				
			}	    	
	    	
        	sendList.put("list" ,jArray);  

//        	BaseResponse kakao_res = RestCall("http://erp-api.fursys.com/intgmsg.do",sendList);	
        	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	        	
        	if (!"200".equals(kakao_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
				return gson.toJson(response);
			}
        	
        	HashMap<String, Object> params;
			params = new HashMap<String, Object>();
			params.put("rem_dt_arr", rem_dt_arr.toString());
			params.put("rem_seq_arr", rem_seq_arr.toString());
			
        	int res = sCheduleMainListMapper.updateStiReqTimeSend(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("도착요청시간 알림톡전송결과 등록 오류  [" + res + "]");
				return gson.toJson(response);
			}
			
    		txManager.commit(status);
    		response.setResultCode("200");
    		System.out.println(response.toString());
    		return gson.toJson(response);          	
	    	
		}	
		
		catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}				
		
//		txManager.commit(status);
//		response.setResultCode("200");
//		System.out.println(response.toString());	
//		return gson.toJson(response);        
        
	}		
	

	@ApiOperation(value = "erp_TeamMemberSearch", notes = "팀원정보조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "팀원정보조회 실패") })
	@GetMapping("/erp_TeamMemberSearch")
	@RequestMapping(value = "/erp_TeamMemberSearch", method = RequestMethod.GET)
	public String erp_TeamMemberSearch(
			@ApiParam(value = "이름", required = true, example = "정득수")
			@RequestParam(name = "stm_nm", required = true) String stm_nm
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("stm_nm", stm_nm);	
	
		ArrayList<ERPStmInfo> arList = sCheduleMainListMapper.selectStmInfoList(params);

		return gson.toJson(arList);


	}	
	

	@ApiOperation(value = "erp_AgentSearch", notes = "대리점정보조회")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "대리점정보조회 실패") })
	@GetMapping("/erp_AgentSearch")
	@RequestMapping(value = "/erp_AgentSearch", method = RequestMethod.GET)
	public String erp_AgentSearch(
			@ApiParam(value = "이름", required = true, example = "논현")
			@RequestParam(name = "vnd_nm", required = true) String vnd_nm
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("vnd_nm", vnd_nm);	
	
		ArrayList<ERPAgtInfo> arList = sCheduleMainListMapper.selectAgtInfoList(params);

		return gson.toJson(arList);


	}	

	@ApiOperation(value = "erp_SelectStiSituationCount", notes = "시공팀현황정보(group)")
	@ApiResponses({ @ApiResponse(code = 200, message = "시공팀현황정보(group) !!"), @ApiResponse(code = 5001, message = "시공팀현황정보(group) 불러오기 실패") })
	@GetMapping("/erp_SelectStiSituationCount") 
	@RequestMapping(value="/erp_SelectStiSituationCount",method=RequestMethod.GET)
	public String erp_SelectStiSituationCount(
			@ApiParam(value = "서비스센터코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String k_sti_cd,
			@RequestParam(name="time", required=false) String time
			
		) { 

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("com_scd", com_scd);
        params.put("k_sti_cd", k_sti_cd);
        params.put("rem_dt", time);
        
        ERPStiSituationCount allItems = erpStiSituationMapper.selectSigongAverageCount(params);
		
		if (allItems == null) {
			allItems = new ERPStiSituationCount(0, 0, 0);
		}
		
		return gson.toJson(allItems);
	}	

	@ApiOperation(value = "erp_StiSituationInfoList", notes = "시공팀현황정보 리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공팀현황정보 리스트조회 실패") })
	@GetMapping("/erp_StiSituationInfoList")
	@RequestMapping(value = "/erp_StiSituationInfoList", method = RequestMethod.GET)
	public String erp_StiSituationInfoList(
			@ApiParam(value = "서비스센터코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "k_sti_cd", required = false) String k_sti_cd,
			@RequestParam(name="time", required=false) String time 
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("com_scd", com_scd);
        params.put("k_sti_cd", k_sti_cd);
        params.put("plm_cdt", time);
	
		ArrayList<ERPStiSituationInfoList> arList = erpStiSituationMapper.selectStiSituationInfoList(params);

		return gson.toJson(arList);

	}	
	
	@ApiOperation(value = "erp_StiPlanSituationList", notes = "시공AS현황정보 리스트")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공AS현황정보 리스트조회 실패") })
	@GetMapping("/erp_StiPlanSituationList")
	@RequestMapping(value = "/erp_StiPlanSituationList", method = RequestMethod.GET)
	public String erp_StiPlanSituationList(
			@ApiParam(value = "서비스센터코드", required = false, example = "C16YA")
			@RequestParam(name = "com_scd", required = false) String com_scd,
			@ApiParam(value = "시공팀코드", required = false, example = "YA142")
			@RequestParam(name = "sti_cd", required = false) String sti_cd,
			@RequestParam(name="time", required=false) String time 
			) {

		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("com_scd", com_scd);
        params.put("sti_cd", sti_cd);
        params.put("rem_dt", time);
	
		ArrayList<ERPStiPlanSituationList> arList = erpStiSituationMapper.selectStiPlanSituationInfoList(params);

		return gson.toJson(arList);

	}	
	
	@ApiOperation(value = "selectSigongSearchInfoByItem", notes = "시공건 풀목코드 검색")
	@GetMapping("/selectSigongSearchInfoByItem")  
	public String selectSigongSearchInfoByItem(
			@RequestParam(name="from_dt", required=true) String from_dt,
			@RequestParam(name="to_dt", required=true) String to_dt,
			@RequestParam(name="itm_cd", required=false) String itm_cd,
			@RequestParam(name="com_scd", required=false) String com_scd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("from_dt",from_dt);
        params.put("to_dt",to_dt);
        params.put("itm_cd",itm_cd);
        params.put("com_scd",com_scd);
        
		ArrayList<ERPSigongSearchInfo> arList = sCheduleMainListMapper.selectSigongSearchInfoByItem(params);

		return gson.toJson(arList);        
        
	}
	
	@ApiOperation(value = "selectSigongSearchInfo", notes = "시공건 건명 검색")
	@GetMapping("/selectSigongSearchInfo")  
	public String erp_selectSigongSearchInfo(
			@RequestParam(name="from_dt", required=true) String from_dt,
			@RequestParam(name="to_dt", required=true) String to_dt,
			@RequestParam(name="orm_nm", required=false) String orm_nm,
			@RequestParam(name="com_scd", required=false) String com_scd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("from_dt",from_dt);
        params.put("to_dt",to_dt);
        params.put("orm_nm",orm_nm);
        params.put("com_scd",com_scd);
        
		ArrayList<ERPSigongSearchInfo> arList = sCheduleMainListMapper.selectSigongSearchInfo(params);

		return gson.toJson(arList);        
        
	}
	
	@ApiOperation(value = "selectSigongSearchInfoDetail", notes = "상세정보")
	@GetMapping("/selectSigongSearchInfoDetail")  
	public String erp_selectSigongSearchInfoDetail(
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="rem_dt", required=false) String rem_dt
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("com_ssec",com_ssec);
        params.put("orm_no",orm_no);
        params.put("rem_dt",rem_dt);
        
        ArrayList<ERPSigongSearchDetailInfo> arList = null;
        
        if("AS".equals(com_ssec)) {
        	arList = sCheduleMainListMapper.selectAsSearchDetailInfo(params);
        	
        	
        }else {
        	
        	arList = sCheduleMainListMapper.selectSigongSearchDetailInfo(params);
        	
        }
        
		return gson.toJson(arList);        
        
	}	
	
	@ApiOperation(value = "erp_netkmallowance", notes = "KM당출장비")
	@ApiResponses({ @ApiResponse(code = 200, message = "KM당출장비 불러오기 성공"), @ApiResponse(code = 5001, message = "KM당출장비 불러오기 실패") })
	@GetMapping("/erp_netkmallowance") 
	@RequestMapping(value="/erp_netkmallowance",method=RequestMethod.GET)
	public String erp_netkmallowance(
			@RequestParam(name="rem_dt", required=false) String rem_dt
			
		) { 

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("rem_dt", rem_dt);
        
        ERPNetKmAllowance allItems = sCheduleMainListMapper.selectNetKmAllowance(params);
		
		if (allItems == null) {
			allItems = new ERPNetKmAllowance("");
		}
		
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_selectSelectStmList", notes = "시공책임자리스트")
	@GetMapping("/erp_selectSelectStmList")  
	public String erp_selectSelectStmList(
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        
		ArrayList<ERPSigongStmList> arList = sCheduleMainListMapper.selectSelectStmList(params);

		return gson.toJson(arList);        
        
	}	

	@ApiOperation(value = "erp_updateStmNo", notes = "시공책임자 변경")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "erp_updateStmNo Update 실패") })	
	@GetMapping("/erp_updateStmNo")  
	@RequestMapping(value = "/erp_updateStmNo", method = RequestMethod.GET)
	public String erp_updateStmNo (
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="rem_seq", required=true) String rem_seq,
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="stm_no", required=true) String stm_no
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		
		try {
							
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("rem_dt", rem_dt);
	        params.put("rem_seq", rem_seq);
	        params.put("plm_no", plm_no);
	        params.put("stm_no", stm_no);

    
        	//tc_resmst 변경
        	res = sCheduleMainListMapper.updateStmNoResmst(params);
        	
			if (res < 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateStmNoResmst 오류 [" + res + "]");
				return gson.toJson(response);
			}	        	

			//tc_planmst 변경
        	res = sCheduleMainListMapper.updateStmNoTcPlanmst(params);
        	
			if (res < 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateStmNoTcPlanmst 오류 [" + res + "]");
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

	@ApiOperation(value = "erp_paymentKakaoTalkSend", notes = "결재정보 알림톡 발송")
	@GetMapping("/erp_paymentKakaoTalkSend")  
	public String erp_paymentKakaoTalkSend (
			@ApiParam(value = "브랜드CD", required=true, example = "T60I01")
			@RequestParam(name="com_brand_cd", required=false) String com_brand_cd,
			@ApiParam(value = "전송구분(승인)", required=true, example = "A")
			@RequestParam(name="send_gubun", required=false) String send_gubun,
			@ApiParam(value = "주문번호", required=true, example = "I20201228012601")
			@RequestParam(name="orm_no", required=true) String orm_no,
			@ApiParam(value = "휴대폰번호", required=true, example = "01036529837")
			@RequestParam(name="orm_hdphone", required=true) String orm_hdphone,
			@ApiParam(value = "고객명", required=true, example = "홍길동")
			@RequestParam(name="orm_nm", required=true) String orm_nm,
			@ApiParam(value = "시공팀명", required=true, example = "시공팀")
			@RequestParam(name="sti_nm", required=true) String sti_nm,
			@ApiParam(value = "시공팀코드", required=true, example = "YA707")
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@ApiParam(value = "숭인번호", required=true, example = "0104C1371209")
			@RequestParam(name="pay_no", required=true) String pay_no,
			@ApiParam(value = "결재일자", required=true, example = "20201231")
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@ApiParam(value = "숭인금액", required=true, example = "1004")
			@RequestParam(name="pay_amt", required=true) String pay_amt
		) { 
		
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		
		try {
			
	    	JSONObject obj = new JSONObject();
	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	 
	    	JSONObject api_token = new JSONObject();
	    	JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	
	    	String templateCode = "";
	    	String senderkey = "";
	    	String title = "";
	    	String subject = "";
	    	String from_no = "";

	    	String biztalkmessage = "" ;
	    	int pay_amt2 = 0;
	    	String pay_amt3 = "";
	    	
	    	//알림톡정보 전달 제외 추가  - 구정회님 요청사항
	    	
	    	
	    	HashMap<String,Object> params = new HashMap<String, Object>();
	    	params.put("orm_no", orm_no);
	    	
	    	dataResult = erpAsPaymentMapper.selectPaynemtKakaoYn(params);
	    	
	    	String paymentKakaoYn = "";
	    	
	    	paymentKakaoYn = dataResult.getData1();
	    	
	    	if(paymentKakaoYn.equals("Y")) {
	    		
		    	int to = Integer.parseInt(pay_amt);

		    	pay_amt3 = String.format("%,d", to);
		    	
	    		if("A".equals(send_gubun) ) {
	    			
	    			title = "결제승인안내";
	    			subject = "결제승인안내";

	    	    	biztalkmessage = "(결제안내) 안녕하세요 "+orm_nm+" 고객님, 고객님의 결제하신 금액 " + "" +pay_amt3+ "원이 승인되었습니다.\r\n\r\n" +
	   					 "1) 결제번호 : "+""+pay_no+"\r\n"+
	   					 "2) 결제일자 : "+""+rem_dt+"";
	    	    	
	    		} else if ("B".equals(send_gubun) ) {
	    			title = "결제취소안내";
	    			subject = "결제취소안내";
	    			
	    	    	biztalkmessage = "(결제취소) 안녕하세요 "+orm_nm+" 고객님, 고객님의 취소하신 금액 " + "" +pay_amt3+ "원이 승인되었습니다.\r\n\r\n" +
	   					 "1) 결제번호 : "+""+pay_no+"\r\n"+
	   					 "2) 취소일자 : "+""+rem_dt+"";
	    			
	    		}  	
		    	
		    	//senmd_gubun : A(지연안내), B(교통상황안내), C(도착안내)
		    	//퍼시스브랜드
		    	if("T60F01".equals(com_brand_cd)) {
		    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
		    		from_no = "1588-1244";
		    		
		    		if("A".equals(send_gubun) ) {
		    			
		    			templateCode = "fursyspaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			
		    			templateCode = "fursyspaymentcancel03";
		    			
		    		} 
		    	}
		    	
		    	//일룸브랜드
		    	if("T60I01".equals(com_brand_cd)) {
		    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
		    		from_no = "1577-5670";
		    		if("A".equals(send_gubun) ) {
		    			templateCode = "iloompaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			templateCode = "iloompaymentcancel03";
		    			
		    		}		
		    		
		    	}	    	
		    	
		    	//데스커브랜드
		    	if("T60I02".equals(com_brand_cd)) {
		    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
		    		from_no = "1588-1662";
		    		
		    		if("A".equals(send_gubun) ) {
		    			templateCode = "deskerpaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			templateCode = "deskerpaymentcancel03";
		    			
		    		}    		
		    		
		    	}	   
		    	
		    	//슬로우브랜드
		    	if("T60I03".equals(com_brand_cd)) {	    		
		    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
		    		from_no = "1899-8588";
		    	
		    		if("A".equals(send_gubun) ) {
		    			templateCode = "sloupaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			templateCode = "sloupaymentcancel03";
		    			
		    		}    		
		    	}	    	
		    	
		    	//시디즈브랜드
		    	if("T60P01".equals(com_brand_cd)) {
		    		
		    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
		    		from_no = "1577-5674";
		    		
		    		if("A".equals(send_gubun) ) {
		    			templateCode = "sidizpaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			templateCode = "sidizpaymentcancel03";
		    		}
		    	}	    	
		    	
		    	//알로소브랜드
		    	if("T60P02".equals(com_brand_cd)) {
		    		
		    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";
		    		from_no = "1577-1641";
		    		
		    		if("A".equals(send_gubun) ) {
		    			templateCode = "allosopaymentapprove03";
		    			
		    		} else if ("B".equals(send_gubun) ) {
		    			templateCode = "allosopaymentcancel03";
		    			
		    		}  		
		    	}		    	
		    	
//		    	orm_hdphone = "010-3652-9837";
//		    	String bistalkMessage = "안녕하세요 고객님.~r~n금일 방문예정인 황성현 팀입니다.~r~n앞서 진행중인 작업 지연으로~r~n방문시간보다 다소 지연 예상됩니다.~r~n~r~n다시한번 양해 부탁드립니다~r~n~r~n시공팀 연락처~r~n010-36529837";
		    	
		    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
				
		       	 for (int i = 0; i < 1; i++)//배열
		       	 {
			        	 
			        	 
			        	 sObject.put("sendDiv", "BIZTALK" );
			        	 sObject.put("title", title);
			        	 sObject.put("subject", subject );		  
			        	 sObject.put("message","");
			        	 sObject.put("fromNm", orm_nm );
			        	 sObject.put("toNm", sti_nm );
			        	 sObject.put("fromNo", from_no ); 
			        	 sObject.put("toNo", orm_hdphone);
			        	 sObject.put("companyCd", "T01B" );		        	 
			        	 sObject.put("fstUsr", sti_cd );
			        	 sObject.put("systemNm", "mobilecm" );
			        	 sObject.put("sendType", "SMTP" );
			        	 sObject.put("reserveDiv","I");
			        	 sObject.put("reserveDt", "" );
			        	 sObject.put("keyNo", orm_no);
			        	 sObject.put("msgType", "TI4N" );		        	 
			        	 sObject.put("senderKey", senderkey);
			        	 sObject.put("templateCode", templateCode );
			        	 sObject.put("bizTalkMessage", biztalkmessage );
			        	 sObject.put("comBrd", com_brand_cd );
			        	 
			        	 jArray.add(sObject);
		       	 }        	 
	        	
	        			
	        	sendList.put("list" ,jArray);  
	        	 
//	        	RestCall("http://localhost:8082/intgmsg.do",sendList);
//		        BaseResponse kakao_res = RestCall("http://erp-api.fursys.com/intgmsg.do",sendList);	
	        	
	        	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
	        	if (!"200".equals(kakao_res.getResultCode())) {
					response.setResultCode("5001");
					response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
					return gson.toJson(response);
				}	    		
	    		
	    	}
	    	
	    	
        	
		}	
		
		catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}				
		
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);        
        
	}	
	
	@ApiOperation(value = "erp_asKakaoTalkSend", notes = "AS접수 알림톡 발송")
	@GetMapping("/erp_asKakaoTalkSend")  
	public String erp_asKakaoTalkSend (
			@RequestParam(name="com_brand_cd", required=false) String com_brand_cd,
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="orm_hdphone", required=true) String orm_hdphone,
			@RequestParam(name="orm_gaddr", required=true) String orm_gaddr,
			@RequestParam(name="orm_nm", required=true) String orm_nm,
			@RequestParam(name="sti_nm", required=true) String sti_nm,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		
		try {
			
	    	JSONObject obj = new JSONObject();
	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	 
	    	JSONObject api_token = new JSONObject();
	    	JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	
	    	String templateCode = "";
	    	String senderkey = "";
	    	String title = "";
	    	String subject = "";
	    	String from_no = "";

	    	String biztalkmessage = "" ;
	    	
	    	String chatbot_url = "";
	    	
	    	

	    	
	    	
//    		if("A".equals(send_gubun) ) {
//    			
//    			title = "결재승인안내";
//    			subject = "결재승인안내";
//
//    	    	biztalkmessage = "(결재안내) 안녕하세요 "+orm_nm+" 고객님, 고객님의 결재하신 금액 " + "" +pay_amt+ "원이 승인되었습니다.\r\n\r\n" +
//   					 "1) 결재번호 : "+""+pay_no+"\r\n"+
//   					 "2) 결재일자 : "+""+rem_dt+"";
//    	    	
//    		} else if ("B".equals(send_gubun) ) {
//    			title = "결재취소안내";
//    			subject = "결재취소안내";
//    			
//    	    	biztalkmessage = "(결재취소) 안녕하세요 "+orm_nm+" 고객님, 고객님의 취소하신 금액 " + "" +pay_amt+ "원이 승인되었습니다.\r\n\r\n" +
//   					 "1) 결재번호 : "+""+pay_no+"\r\n"+
//   					 "2) 취소일자 : "+""+rem_dt+"";
//    			
//    		}  	
	    	
	    	//senmd_gubun : A(지연안내), B(교통상황안내), C(도착안내)
	    	//퍼시스브랜드
	    	if("T60F01".equals(com_brand_cd)) {
	    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
	    		from_no = "1588-1244";
	    		chatbot_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%ED%8D%BC%EC%8B%9C%EC%8A%A4%EA%B3%B5%EC%8B%9D&site_id=5000000752&category_id=97213&division_id=97799";
//	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=5000000752&siteName=퍼시스&categoryId=97213&divisionId=97799&params=";
	    		templateCode = "fursysasinform01";

	    	}
	    	
	    	//일룸브랜드
	    	if("T60I01".equals(com_brand_cd)) {
	    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
	    		from_no = "1577-5670";
	    		chatbot_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%EC%9D%BC%EB%A3%B8&site_id=5000000750&category_id=97803&division_id=97805";
//	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=5000000750&siteName=일룸&categoryId=97803&divisionId=97805&params=";
	    		templateCode = "iloomasinform01";
		
	    		
	    	}	    	
	    	
	    	//데스커브랜드
	    	if("T60I02".equals(com_brand_cd)) {
	    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
	    		from_no = "1588-1662";
	    		chatbot_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%EB%8D%B0%EC%8A%A4%EC%BB%A4&site_id=1000221219&category_id=97808&division_id=97809";
//	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=1000221219&siteName=데스커&categoryId=97808&divisionId=97809&params=";
	    		templateCode = "deskerasinform01";
   		
	    		
	    	}	   
	    	
	    	//슬로우브랜드
	    	if("T60I03".equals(com_brand_cd)) {	    		
	    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
	    		from_no = "1899-8588";
	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=1000221219&siteName=데스커&categoryId=97808&divisionId=97809&params=";
	    		templateCode = "slouasinform01";
   		
	    	}	    	
	    	
	    	//시디즈브랜드
	    	if("T60P01".equals(com_brand_cd)) {
	    		
	    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
	    		from_no = "1577-5674";
	    		chatbot_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%EC%8B%9C%EB%94%94%EC%A6%88&site_id=5000000749&category_id=97061&division_id=97062";
//	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=5000000749&siteName=시디즈&categoryId=97061&divisionId=97062&params=";
	    		templateCode = "sidizasinform01";

	    	}	    	
	    	
	    	//알로소브랜드
	    	if("T60P02".equals(com_brand_cd)) {
	    		
	    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";
	    		from_no = "1577-1641";
	    		chatbot_url = "https://api.happytalk.io/api/kakao/chat_open?yid=%40%EC%95%8C%EB%A1%9C%EC%86%8C&site_id=5000000754&category_id=97821&division_id=97822";
//	    		chatbot_url = "https://design.happytalkio.com/chatting?siteId=5000000754&siteName=Alloso&categoryId=97821&divisionId=97822&params=";
	    		templateCode = "allosoasinform01";
	
	    	}		    	

	    	biztalkmessage = "안녕하세요 고객님! \r\n" + 
	    			"고객님의 AS요청이 완료 되었습니다.\r\n\r\n" + 
	    			"▷ 주소 : " +orm_gaddr+ ".\r\n.\r\n" + 
	    			"※ 자세한 접수 관련 궁금하신 사항은 챗봇을 통해 문의 바랍니다.\r\n" + 
	    			"카카오톡 대화창에 “시작”을 입력해주세요.\r\n" + 
	    			"■ 챗봇상담 : " +chatbot_url+ ".\r\n" + 
	    			"(운영시간 평일 09:30-17:30, 주말 및 공휴일 휴무)";	    	
	    	
//	    	String bistalkMessage = "안녕하세요 고객님.~r~n금일 방문예정인 황성현 팀입니다.~r~n앞서 진행중인 작업 지연으로~r~n방문시간보다 다소 지연 예상됩니다.~r~n~r~n다시한번 양해 부탁드립니다~r~n~r~n시공팀 연락처~r~n010-36529837";
	    	
	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
			
	       	 for (int i = 0; i < 1; i++)//배열
	       	 {
		        	 
		        	 
		        	 sObject.put("sendDiv", "BIZTALK" );
		        	 sObject.put("title", title);
		        	 sObject.put("subject", subject );		  
		        	 sObject.put("message","");
		        	 sObject.put("fromNm", orm_nm );
		        	 sObject.put("toNm", sti_nm );
		        	 sObject.put("fromNo", from_no ); 
		        	 sObject.put("toNo", orm_hdphone);
		        	 sObject.put("companyCd", "T01B" );		        	 
		        	 sObject.put("fstUsr", sti_cd );
		        	 sObject.put("systemNm", "mobilecm" );
		        	 sObject.put("sendType", "SMTP" );
		        	 sObject.put("reserveDiv","I");
		        	 sObject.put("reserveDt", "" );
		        	 sObject.put("keyNo", orm_no);
		        	 sObject.put("msgType", "TI43" );		        	 
		        	 sObject.put("senderKey", senderkey);
		        	 sObject.put("templateCode", templateCode );
		        	 sObject.put("bizTalkMessage", biztalkmessage );
		        	 sObject.put("comBrd", com_brand_cd );
		        	 
		        	 jArray.add(sObject);
	       	 }        	 
        	
        			
        	sendList.put("list" ,jArray);  
        	 
//        	RestCall("http://localhost:8082/intgmsg.do",sendList);
//			BaseResponse kakao_res = RestCall("http://erp-api.fursys.com/intgmsg.do",sendList);	
        	
        	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
        	if (!"200".equals(kakao_res.getResultCode())) {
				response.setResultCode("5001");
				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
				return gson.toJson(response);
			}
        	
		}	
		
		catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}				
		
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);        
        
	}	
	
	
	@ApiOperation(value = "erp_selectCooperationList", notes = "공동시공분할요청리스트")
	@GetMapping("/erp_selectCooperationList")  
	public String erp_selectCooperationList(
			@RequestParam(name="fdt", required=true) String fdt,
			@RequestParam(name="tdt", required=true) String tdt,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="orm_nm", required=true) String orm_nm
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        params.put("fdt",fdt);
        params.put("tdt",tdt);
        params.put("orm_nm", "%" + orm_nm + "%");
        
        
		ArrayList<ERPCooperationList> arList = sCheduleMainListMapper.selectSelectCooperationList(params);

		return gson.toJson(arList);        
        
	}	
	
	@ApiOperation(value = "erp_insertCooperationRequest", notes = "공동시공분할요청_insert")
	@GetMapping("/erp_insertCooperationRequest")  
	public String erp_insertCooperationRequest (
			@RequestParam(name="o_plm_no", required=true) String o_plm_no,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="plm_cdt", required=true) String plm_cdt,
			@RequestParam(name="req_amt", required=true) int req_amt,
			@RequestParam(name="user_id", required=true) String user_id,
			
			@RequestParam(name="plm_no_arr", required=true) String plm_no_arr,
			@RequestParam(name="sti_cd_arr", required=true) String sti_cd_arr,
			@RequestParam(name="plm_cdt_arr", required=true) String plm_cdt_arr,
			@RequestParam(name="req_amt_arr", required=true) String req_amt_arr
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult();
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("o_plm_no", o_plm_no);
			params.put("sti_cd", sti_cd);
			params.put("plm_cdt", plm_cdt);
			params.put("req_amt", req_amt);
	        params.put("plm_no_arr", plm_no_arr);
	        params.put("sti_cd_arr",sti_cd_arr);			
	        params.put("plm_cdt_arr",plm_cdt_arr);
	        params.put("req_amt_arr",req_amt_arr);	    
	        params.put("user_id",user_id);	  
	        	        
	        dataResult = sCheduleMainListMapper.selectCooperationMasterYn(params);
	        
			if (dataResult.getValue1() == 0) {

	        	res = sCheduleMainListMapper.insertCooperationRequestMaster(params);
	        	
				if (res < 1) {    				
					res_msg =  "insertCooperationRequestMaster 오류 [" + res + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}
				
			}	        
	        	
        	res = sCheduleMainListMapper.insertCooperationRequestDetail(params);
        	
			if (res < 1) {    				
				res_msg =  "insertCooperationRequestMaster 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}			
			
			
			
		}	
		catch (Exception e) {
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
	
	@ApiOperation(value = "erp_selectReqCooperationList", notes = "공동시공분할요청리스트")
	@GetMapping("/erp_selectReqCooperationList")  
	public String erp_selectReqCooperationList(
			@RequestParam(name="fdt", required=true) String fdt,
			@RequestParam(name="tdt", required=true) String tdt,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("sti_cd",sti_cd);
        params.put("fdt",fdt);
        params.put("tdt",tdt);
        
        
		ArrayList<ERPReqCooperationList> arList = sCheduleMainListMapper.selectReqCooperationList(params);

		return gson.toJson(arList);        
        
	}	
	
	@ApiOperation(value = "erp_selectReqDetailCooperationList", notes = "공동시공분할요청디테일리스트")
	@GetMapping("/erp_selectReqDetailCooperationList")  
	public String erp_selectReqDetailCooperationList(
			@RequestParam(name="o_plm_no", required=true) String o_plm_no
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("o_plm_no",o_plm_no);        
        
		ArrayList<ERPReqCooperationList> arList = sCheduleMainListMapper.selectReqDetailCooperationList(params);

		return gson.toJson(arList);        
        
	}	
	
	@ApiOperation(value = "erp_reqCooperationCancel", notes = "공동시공분할요청 취소")
	@GetMapping("/erp_reqCooperationCancel")
	@RequestMapping(value = "/erp_reqCooperationCancel", method = RequestMethod.GET)
	public String erp_reqCooperationCancel(
			@RequestParam(name = "o_plm_no", required = false) String o_plm_no,	
			@RequestParam(name = "plm_no", required = false) String plm_no			
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("o_plm_no", o_plm_no);
			params.put("plm_no", plm_no);
			
			res = schedulemainlistMapper.updateReqCooperationCancel(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateReqCooperationCancel 오류 [" + res + "]");
				System.out.println("res=" + res);				
				return gson.toJson(response);
			}
			
			dataResult = schedulemainlistMapper.selectMasterDeleteYn(params);
			
			if (dataResult.getValue1() == 0) {

				res = schedulemainlistMapper.updateReqCooperationMasterCancel(params);	
				
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateReqCooperationMasterCancel 오류 [" + res + "]");
					System.out.println("res=" + res);				
					return gson.toJson(response);
				}				
				
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
	
	@ApiOperation(value = "erp_selectApplyCooperationList", notes = "공동시공분할승인 리스트")
	@GetMapping("/erp_selectApplyCooperationList")  
	public String erp_selectApplyCooperationList(
			@RequestParam(name="fdt", required=true) String fdt,
			@RequestParam(name="tdt", required=true) String tdt,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("fdt",fdt);
        params.put("tdt",tdt);
        params.put("sti_cd",sti_cd);
        
		ArrayList<ERPReqCooperationList> arList = sCheduleMainListMapper.selectApplyCooperationList(params);

		return gson.toJson(arList);        
        
	}	
	
	@ApiOperation(value = "erp_reqCooperationApply", notes = "공동시공분할요청 승인")
	@GetMapping("/erp_reqCooperationApply")
	@RequestMapping(value = "/erp_reqCooperationApply", method = RequestMethod.GET)
	public String erp_reqCooperationApply(
			@RequestParam(name = "o_plm_no", required = false) String o_plm_no,	
			@RequestParam(name = "plm_no", required = false) String plm_no			
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("o_plm_no", o_plm_no);
			params.put("plm_no", plm_no);
			
			res = schedulemainlistMapper.updateReqCooperationApply(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateReqCooperationApply 오류 [" + res + "]");
				System.out.println("res=" + res);				
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
	
	@ApiOperation(value = "erp_cooperationJungsan", notes = "공동시공분할요청_정산확정")
	@GetMapping("/erp_cooperationJungsan")  
	public String erp_cooperationJungsan (
			@RequestParam(name="o_plm_no", required=true) String o_plm_no,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="plm_cdt", required=true) String plm_cdt,
			@RequestParam(name="req_amt", required=true) int req_amt,
			@RequestParam(name="user_id", required=true) String user_id			
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		String res_msg = "";
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("o_plm_no", o_plm_no);
			params.put("sti_cd", sti_cd);
			params.put("plm_cdt", plm_cdt);
			params.put("req_amt", req_amt);
	        params.put("user_id",user_id);	  
	        
			ArrayList<ERPReqCooperationList> arList = sCheduleMainListMapper.selectReqDetailCooperationList(params);
			if (arList.size() < 1) {
				res_msg =  "selectReqDetailCooperationList 오류";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}
			
			StringBuffer plm_no_arr = new StringBuffer(); 
			StringBuffer sti_cd_arr = new StringBuffer();
			StringBuffer plm_cdt_arr = new StringBuffer();
			StringBuffer req_amt_arr = new StringBuffer();
			String token = ",";
			
			plm_no_arr.append(o_plm_no + token);
			sti_cd_arr.append(sti_cd + token);
			plm_cdt_arr.append(plm_cdt + token);
			req_amt_arr.append("-" + req_amt + token);
			
			for (int i=0; i<arList.size(); i++) {
				plm_no_arr.append(arList.get(i).getPlm_no() + token);
				sti_cd_arr.append(arList.get(i).getSti_cd() + token);
				plm_cdt_arr.append(arList.get(i).getPlm_cdt() + token);
				req_amt_arr.append(arList.get(i).getReq_amt() + token);
			}

			params.put("plm_no_arr", plm_no_arr.toString());
		    params.put("sti_cd_arr",sti_cd_arr.toString());
		    params.put("plm_cdt_arr",plm_cdt_arr.toString());
		    params.put("req_amt_arr",req_amt_arr.toString());
			
	        
        	res = sCheduleMainListMapper.insertCooperationOriginalJungsan(params);
			if (res < 1) {    				
				res_msg =  "insertCooperationOriginalJungsan 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}	        
	        
			for (int i=0; i<arList.size(); i++) {
				HashMap<String,Object> params_detail = new HashMap<String, Object>();
				params_detail.put("o_plm_no", arList.get(i).getPlm_no());
				params_detail.put("req_amt", arList.get(i).getReq_amt());
				
				res = sCheduleMainListMapper.insertCooperationOriginalJungsanDetail(params_detail);
				if (res < 1) {    				
					res_msg =  "insertCooperationOriginalJungsan2 오류 [" + arList.get(i).getPlm_no() + "]";
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return gson.toJson(response);
				}
			}			
			
        	res = sCheduleMainListMapper.insertCooperationReqJungsan(params);
        	
			if (res < 1) {    				
				res_msg =  "insertCooperationReqJungsan 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}	        

        	res = sCheduleMainListMapper.updateReqCooperationJungsan(params);
        	
			if (res < 1) {    				
				res_msg =  "updateReqCooperationJungsan 오류 [" + res + "]";
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(res_msg);
				return gson.toJson(response);
			}
			
						
		}	
		catch (Exception e) {
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
	
	@ApiOperation(value = "erp_test", notes = "test")
	@GetMapping("/erp_test")  
	public String erp_test (
			@RequestParam(name="k_sti_cd", required=true) String as_k_sti_cd,
			@RequestParam(name="yyyymm", required=true) String as_yyyymm
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("k_sti_cd", as_k_sti_cd);
        params.put("yyyymm", as_yyyymm);
        
        ArrayList<ERPTestVo> allItems = tTestMapper.test(params);
        
        
        
//        ArrayList<ERPSigongCalculateMoneyTeam> allItems = apiErpService.erp_selectSigongCalculateMoneyTeam(params);		
		        
		return gson.toJson(allItems);
	}	
	

	@ApiOperation(value = "erp_sigongmiguelasAutoProc", notes = "시공미결AS처리시 자동 접수 처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결AS처리시 자동 접수 처리 실패") })	
	@GetMapping("/erp_sigongmiguelasAutoProc")  
	@RequestMapping(value = "/erp_sigongmiguelasAutoProc", method = RequestMethod.GET)
	public String erp_sigongmiguelasAutoProc (

			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="req_as_dt", required=true) String req_as_dt,
			@RequestParam(name="rpt_urg", required=true) String rpt_urg,
			@RequestParam(name="inconsistent_yn", required=false) String inconsistent_yn
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		ERPPraNewRptNo item = null;
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);

	        //AS요청을위한 기본 정보 select1
	        dataResult = sCheduleMainListMapper.selectAsReqBasicInfomation(params);	
	        
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectAsReqBasicInfomation 오류");
	        	return gson.toJson(response);
	        }	        
	        
	        String com_agsec = "";
	        String com_scd = "";
	        String com_brand = "";
	        String sti_cd = "";
	        String town_cd = "";
	       
	        com_agsec = dataResult.getData1();
	        com_scd = dataResult.getData2();
	        com_brand = dataResult.getData3();
	        sti_cd = dataResult.getData4();
	        town_cd = dataResult.getData5();
	        
	      //AS요청을위한 기본 정보 select2
	        
	        dataResult = sCheduleMainListMapper.selectAsReqBasicInfomation2(params);	
	        
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectAsReqBasicInfomation2 오류");
	        	return gson.toJson(response);
	        }		        
	        
	        String road_addr = "";
	        String orm_gpost = "";
	        String orm_gaddr = "";
	        String agt_cd = "";
	        String vnd_nm = "";
	        
	        road_addr = dataResult.getData1();
	        orm_gpost = dataResult.getData2();		
	        orm_gaddr = dataResult.getData3();
	        agt_cd = dataResult.getData4();
	        vnd_nm = dataResult.getData5();
	        
	        //실 수행 AS팀 정보 select
	        params.put("com_agsec", com_agsec);
	        params.put("com_brand", com_brand);
	        params.put("town_cd", town_cd);
	        params.put("com_scd", com_scd);
	        params.put("sti_cd", sti_cd);
	        
	        dataResult = sCheduleMainListMapper.selectExcuteAsTeamInfo(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectExcuteAsTeamInfo 오류");
	        	return gson.toJson(response);
	        }		        
	        
	        String as_sti_cd = "";
	        String as_com_scd = "";
	        String as_sti_nm = "";
	        
	        as_sti_cd = dataResult.getData1();
	        as_sti_nm = dataResult.getData2();
	        as_com_scd = dataResult.getData3();
	        
	        //AS요청사항 가져오기
	        
	        dataResult = sCheduleMainListMapper.selectExcuteAsReqBasicInfo(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectExcuteAsReqBasicInfo 오류");
	        	return gson.toJson(response);
	        }	        
	        
	        String org_orm_nm = dataResult.getData1();
	        String org_sti_nm = dataResult.getData2();
	        String org_plm_cdt = dataResult.getData3();
	        String org_mob_remark = dataResult.getData4();
	        String org_orm_no = dataResult.getData5();
	        
	        String as_req_remark = "";
	        
	        as_req_remark = "원시공팀 "+org_sti_nm+" 시공팀의 시공건으로, AS미결이 발생한 건입니다.\r\n\r\n" +
					 "1) 수주건명 : "+""+org_orm_nm+"\r\n"+
					 "2) 수주번호 : "+""+org_orm_no+"\r\n"+
					 "3) 시공일자 : "+""+org_plm_cdt+"\r\n"+
					 "4) 요청사항 : "+""+org_mob_remark+"";
	        
	        if ("Y".equals(inconsistent_yn)) {
	        	as_req_remark = as_req_remark +"\r\n"+"내부부적합으로 인해 발생된 AS접수건입니다.";
	        }
	        
			dataResult = sCheduleMainListMapper.executePraFaRptno(params);
			
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectExcuteAsReqBasicInfo 오류");
	        	return gson.toJson(response);
	        }	    		
	        
	        String new_rpt_no = "";
	        new_rpt_no = dataResult.getData1();
	        
        
//	        //AS신규 요청정보 저장 
	        params.put("as_sti_cd", as_sti_cd);
	        params.put("as_sti_nm", as_sti_nm);
	        params.put("as_com_scd", as_com_scd);
	        params.put("road_addr", road_addr);
	        params.put("orm_gpost", orm_gpost);
	        params.put("orm_gaddr", orm_gaddr);
	        params.put("agt_cd", agt_cd);
	        params.put("vnd_nm", vnd_nm);
	        params.put("new_rpt_no", new_rpt_no);
	        params.put("req_as_dt", req_as_dt);
	        params.put("as_req_remark", as_req_remark);
	        params.put("rpt_urg", rpt_urg);
	        params.put("inconsistent_yn", inconsistent_yn);
	        
        	res = sCheduleMainListMapper.insertSigongMigeulAsRequest(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertSigongMigeulAsRequest 오류");
	        	return gson.toJson(response);
			}	 	        
	        
	        //tc_plandtl AS접수정보 update
	        
			res = sCheduleMainListMapper.updateSigongAsConfirmInform(params);
			
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateSigongAsConfirmInform 오류");
	        	return gson.toJson(response);
			}
			
			if ("Y".equals(inconsistent_yn)) {
				res = sCheduleMainListMapper.updateSigongAsInconsistent_yn(params);
				
				if (res < 1) {    				
		        	txManager.rollback(status);
		        	response.setResultCode("5001");
		        	response.setResultMessage("updateSigongAsInconsistent_yn 오류");
		        	return gson.toJson(response);
				}
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
	
	@ApiOperation(value = "erp_sigongmiguelRecreateAutoProc", notes = "시공미결재일정 자동 접수 처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결재일정 자동 접수 처리 실패") })	
	@GetMapping("/erp_sigongmiguelRecreateAutoProc")  
	@RequestMapping(value = "/erp_sigongmiguelRecreateAutoProc", method = RequestMethod.GET)
	public String erp_sigongmiguelRecreateAutoProc (

			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="req_resigong_dt", required=true) String req_resigong_dt,
			@RequestParam(name="com_undsec", required=true) String com_undsec,
			@RequestParam(name="req_rem_ftm", required=true) String req_rem_ftm
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		ERPPraNewRptNo item = null;
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);

	        //재일정 요청을 위한 기본 정보 가져오기 1
	        dataResult = sCheduleMainListMapper.selectSigongReqBasicInfomation(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectSigongReqBasicInfomation 오류");
	        	return gson.toJson(response);
	        }	
	        
	        String org_com_agsec = "";
	        String org_com_brand = "";
	        String org_com_scd = "";
	        String org_sti_cd = "";
	        String org_orm_no = "";
	        String org_plm_cdt = "";
	        String org_sti_cd_get = "";
	        
	        org_com_agsec = dataResult.getData1();
	        org_com_brand = dataResult.getData2();
	        org_com_scd = dataResult.getData3();
	        org_sti_cd = dataResult.getData4(); 
	        org_sti_cd_get = dataResult.getData4(); 
	        org_orm_no = dataResult.getData5();
	        org_plm_cdt = dataResult.getData6();
	        
	        //재일정 요청을 위한 기본 정보 가져오기 2
	        dataResult = sCheduleMainListMapper.selectSigongReqBasicInfomation2(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectSigongReqBasicInfomation2 오류");
	        	return gson.toJson(response);
	        }		        
	        
	        String org_agt_cd = "";
	        String org_orm_gpost = "";
	        String org_town_cd = "";
	        String org_orm_gaddr = "";
	        String org_rem_dt = "";
	    
	        org_agt_cd = dataResult.getData1();
	        org_orm_gpost = dataResult.getData2();
	        org_town_cd = dataResult.getData3();
	        org_orm_gaddr = dataResult.getData4(); 	        
	        org_rem_dt = dataResult.getData5();
	        
	        //신규 시공번호 채번
	        
	        params.put("org_com_agsec", org_com_agsec);
	        params.put("org_com_brand", org_com_brand);
	        params.put("org_com_scd", org_com_scd);
	        params.put("org_sti_cd", org_sti_cd);
	        params.put("org_orm_no", org_orm_no);
	        params.put("org_agt_cd", org_agt_cd);
	        params.put("org_orm_gpost", org_orm_gpost);
	        params.put("org_town_cd", org_town_cd);
	        params.put("org_orm_gaddr", org_orm_gaddr);
	        params.put("org_rem_dt", org_rem_dt);
	        params.put("req_resigong_dt", req_resigong_dt);
	        
			dataResult = sCheduleMainListMapper.executePraFaAseqrem(params);
			
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("executePraFaAseqrem 오류");
	        	return gson.toJson(response);
	        }	    		
	        
	        String new_plm_no = "";
	        new_plm_no = dataResult.getData1();	        
	        
	        //신규 tc_planmst insert
	        params.put("new_plm_no", new_plm_no);
        	res = sCheduleMainListMapper.insertSigongMigeulReRequest(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertSigongMigeulReRequest 오류");
	        	return gson.toJson(response);
			}	 	        
	        
	        //신규 tc_plandtl insert
	        params.put("new_plm_no", new_plm_no);
        	res = sCheduleMainListMapper.insertSigongMigeulDetailReRequest(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertSigongMigeulDetailReRequest 오류");
	        	return gson.toJson(response);
			}		        
	        
			//재시공 품목 정보 update
			res = sCheduleMainListMapper.updateSigonReConfirmInform(params);
			 
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateSigonReConfirmInform 오류");
	        	return gson.toJson(response);
			}
			
			//신규 tc_resmst의 rem_seq 채번
			dataResult = sCheduleMainListMapper.selectRemSeqNowNo(params);
			
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectRemSeqNowNo 오류");
	        	return gson.toJson(response);
	        }	    			
			
			int now_rem_seq = dataResult.getValue1();
			
			params.put("now_rem_seq", now_rem_seq);
			res = sCheduleMainListMapper.updateTcSeqnoInfReCreate(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateTcSeqnoInfReCreate 오류");
	        	return gson.toJson(response);
			}		
			
			String new_rem_seq = "";
			
			if(org_com_agsec.equals("C02I")) {
				new_rem_seq = "IC" +  String.format( "%1$04d" , now_rem_seq ) ;		
			}else if (org_com_agsec.equals("C02F")) {
				new_rem_seq = "FC" +  String.format( "%1$04d" , now_rem_seq ) ;
			}else if (org_com_agsec.equals("C02P")) {
				new_rem_seq = "PC" +  String.format( "%1$04d" , now_rem_seq ) ;
			}
			
			params.put("new_rem_seq", new_rem_seq);
			params.put("plm_cdt2", org_plm_cdt);
			params.put("req_rem_ftm", req_rem_ftm);
			params.put("org_sti_cd_get", org_sti_cd_get);
			
	        //신규 tc_resmst insert
        	res = sCheduleMainListMapper.insertNewTcResmst(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertNewTcResmst 오류");
	        	return gson.toJson(response);
			}				
			
			
        	res = sCheduleMainListMapper.insertNewTcResdtl(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertNewTcResdtl 오류");
	        	return gson.toJson(response);
			}			
			
			
			res = sCheduleMainListMapper.modifyOrmPsts_U(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("modifyOrmPsts_U 오류");
	        	return gson.toJson(response);
			}	

			String com_agsec = "";
			if ("C02F".equals(org_com_agsec)) {
				com_agsec = "T01F";
			} else if ("C02I".equals(org_com_agsec)) {
				com_agsec = "T01I";
			} else if ("C02P".equals(org_com_agsec)) {
				com_agsec = "T01P";
			} else {
				com_agsec = org_com_agsec;
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_agsec", com_agsec);
			params.put("com_scd", org_com_scd);
			params.put("plm_cdt", org_plm_cdt);
			params.put("req_resigong_dt",req_resigong_dt);
			params.put("com_undsec", com_undsec);
			
			dataResult = sCheduleMainListMapper.selectFinalResult(params);
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("재일정, 반품 기준일자 조회 오류");
	        	return gson.toJson(response);
	        }
	
			String final_result = dataResult.getData1();
			
			params.put("final_result", final_result);			
			//재시공 update
			res = sCheduleMainListMapper.updateFinalResult(params);
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateFinalResult 오류");
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
	

	@ApiOperation(value = "asPaymentPageKakaoAlarm", notes = "카카오알림톡 발송")
	@GetMapping("/asPaymentPageKakaoAlarm")  
	public String asPaymentPageKakaoAlarm (
			@RequestParam(name="com_brand_cd", required=false) String com_brand_cd,
			@RequestParam(name="orm_no", required=true) String orm_no,
			@RequestParam(name="orm_hdphone", required=true) String orm_hdphone,
			@RequestParam(name="orm_nm", required=true) String orm_nm,
			@RequestParam(name="sti_nm", required=true) String sti_nm,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="pay_url", required=true) String pay_url
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		String res_msg = "";
		DataResult dataResult = new DataResult(); 
		
		try {

	    	JSONObject obj = new JSONObject();
	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	 
	    	JSONObject api_token = new JSONObject();
	    	JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	
	    	String templateCode = "";
	    	String senderkey = "";
	    	String title = "AS유상결제영수증";
	    	String subject = "AS유상결제영수증";
	    	String from_no = "";
	    	String message_type = "";
	    	String biztalkmessage = "";
	    	biztalkmessage = "안녕하세요.\r\n\r\n" + 
	    			"아래 링크에서 결제영수증을 확인하세요.\r\n\r\n" + 
	    			"※ 결제영수증 확인 링크\r\n" + 
	    			pay_url;
	
	    	//퍼시스브랜드
	    	if("T60F01".equals(com_brand_cd)) {
	    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
	    		from_no = "1588-1244";
	    		templateCode = "fursysasreceiptalarm";
	
	    	}
	    	
	    	//일룸브랜드
	    	if("T60I01".equals(com_brand_cd)) {
	    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
	    		from_no = "1577-5670";	    		
	    		templateCode = "iloomasreceiptalarm";
    		
	    		
	    	}	    	
	    	
	    	//데스커브랜드
	    	if("T60I02".equals(com_brand_cd)) {
	    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
	    		from_no = "1588-1662";
	    		templateCode = "deskerasreceiptalarm";    			    		
	    	}	   
	    	
	    	//슬로우브랜드
	    	if("T60I03".equals(com_brand_cd)) {	    		
	    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
	    		from_no = "1899-8588";
	    		
	    		templateCode = "slouasreceiptalarm";
	    		   		
	    	}	    	
	    	
	    	//시디즈브랜드
	    	if("T60P01".equals(com_brand_cd)) {
	    		
	    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
	    		from_no = "1577-5674";
	    		templateCode = "sidizasreceiptalarm";   		
	    	}	    	
	    	
	    	//알로소브랜드
	    	if("T60P02".equals(com_brand_cd)) {
	    		
	    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";
	    		from_no = "1577-1641";
	    		templateCode = "allosoasreceiptalarm";
	    		   		
	    	}		    	
 	
	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
			
	       	 for (int i = 0; i < 1; i++)//배열
	       	 {
		        	 
		        	 
		        	 sObject.put("sendDiv", "BIZTALK" );
		        	 sObject.put("title", title);
		        	 sObject.put("subject", subject );		  
		        	 sObject.put("message","");
		        	 sObject.put("fromNm", orm_nm );
		        	 sObject.put("toNm", sti_nm );
		        	 sObject.put("fromNo", from_no ); 
		        	 sObject.put("toNo", orm_hdphone);
		        	 sObject.put("companyCd", "T01B" );		        	 
		        	 sObject.put("fstUsr", sti_cd );
		        	 sObject.put("systemNm", "mobilecm" );
		        	 sObject.put("sendType", "SMTP" );
		        	 sObject.put("reserveDiv","I");
		        	 sObject.put("reserveDt", "" );
		        	 sObject.put("keyNo", orm_no);
		        	 sObject.put("msgType", message_type );		        	 
		        	 sObject.put("senderKey", senderkey);
		        	 sObject.put("templateCode", templateCode );
		        	 sObject.put("bizTalkMessage", biztalkmessage );
		        	 sObject.put("comBrd", com_brand_cd );
//		        	 sObject.put("bizTalkMessage", biztalkmessage );
		        	 jArray.add(sObject);
	       	 }        	 
        	
        			
        	sendList.put("list" ,jArray);  
        	 
//        	RestCall("http://localhost:8082/intgmsg.do",sendList);
//			https://msg-api.fursys.com/v1/api/message/SendMsg
//			"http://erp-api.fursys.com/intgmsg.do"
        	
        	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
        	if (!"200".equals(kakao_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
				return gson.toJson(response);
			}
        	
		}	
		
		catch (Exception e) {
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
	

	@ApiOperation(value = "erp_updateLoadingInfo", notes = "상차완료/취소처리")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "상차완료/취소처리 Update 실패") })	
	@GetMapping("/erp_updateLoadingInfo")  
	@RequestMapping(value = "/erp_updateLoadingInfo", method = RequestMethod.GET)
	public String erp_updateLoadingInfo (

			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@RequestParam(name="loading_yn", required=true) String loading_yn
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        params.put("com_ssec", com_ssec);
	        
	        dataResult = lOADINGORMMapper.selectTcresmstInfo(params);
	        
	        String rem_dt = dataResult.getData1();
	        String rem_seq = dataResult.getData2();
	 	        
	        params.put("rem_dt", rem_dt);
	        params.put("rem_seq", rem_seq);
	        params.put("loading_yn", loading_yn);
	        	        
	        res = lOADINGORMMapper.updateLoadingInfo(params);	        
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("상차처리 오류 [" + res + "]");
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
	
	@ApiOperation(value = "erp_updateLoadingIssueInfo", notes = "상차이슈등록")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "상차이슈등록 Update 실패") })	
	@GetMapping("/erp_updateLoadingIssueInfo")  
	@RequestMapping(value = "/erp_updateLoadingIssueInfo", method = RequestMethod.GET)
	public String erp_updateLoadingIssueInfo (
			@RequestParam(name="rem_dt", required=true) String rem_dt,
			@RequestParam(name="rem_seq", required=true) String rem_seq,
			@RequestParam(name="com_ssec", required=true) String com_ssec,
			@RequestParam(name="seq_no", required=true) int seq_no,			
			@RequestParam(name="loadingissue_std", required=true) String loadingissue_std,
			@RequestParam(name="loadingissue_remark", required=true) String loadingissue_remark,
			@RequestParam(name="itm_cd", required=true) String itm_cd,
			@RequestParam(name="col_cd", required=true) String col_cd,
			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="sti_cd", required=true) String sti_cd
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		int new_seq_no = 0;
		
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        params.put("com_ssec", com_ssec);

	        if (seq_no > 0) {
	        	params.put("rem_dt", rem_dt);
		        params.put("rem_seq", rem_seq);
		        params.put("com_ssec", com_ssec);
		        params.put("seq_no", seq_no);		        
	        	params.put("loadingissue_std", loadingissue_std);
		        params.put("loadingissue_remark", loadingissue_remark);
		        params.put("sti_cd", sti_cd);
		        
		        res = lOADINGORMMapper.updateLoadingIssueInfo(params);	        
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("상차이슈수정 오류 [" + res + "]");
					return gson.toJson(response);
				}
				
		        new_seq_no = seq_no;
		        
			} else {
		        dataResult = lOADINGORMMapper.selectTcresmstInfo(params);
		        
		        rem_dt = dataResult.getData1();
		        rem_seq = dataResult.getData2();
		 	    String orm_no = dataResult.getData3();
		 	    sti_cd = dataResult.getData4();
		 	    
		        params.put("rem_dt", rem_dt);
		        params.put("rem_seq", rem_seq);
		        params.put("orm_no", orm_no);
		        params.put("loadingissue_std", loadingissue_std);
		        params.put("loadingissue_remark", loadingissue_remark);
		        params.put("itm_cd", itm_cd);
		        params.put("col_cd", col_cd);
		        params.put("sti_cd", sti_cd);
		        
		        res = lOADINGORMMapper.insertLoadingIssueInfo(params);	        
				if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("상차이슈등록 오류 [" + res + "]");
					return gson.toJson(response);
				}
				
				new_seq_no = (int) params.get("seq_no");				
			}	        
						
			response.setData1(String.format("%s", rem_dt));
			response.setData2(String.format("%s", rem_seq));
			response.setData3(String.format("%d", new_seq_no));
			response.setData4(String.format("loadingissue%s%s%s%d", rem_dt, rem_seq, com_ssec, new_seq_no));
			
			System.out.println(String.format("seq_no=[%s]", response.getData3()));
			System.out.println(String.format("attatch_file_id=[%s]", response.getData4()));

			
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
	
	@ApiOperation(value = "itemLoadingOrderList", notes = "품목별 상차 시 건 용약정보 조회")
	@GetMapping("/itemLoadingOrderList")  
	public String itemLoadingOrderList(
			@RequestParam(name="time", required=false) Long time,
			@RequestParam(name="com_scd", required=true) String com_scd,
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@RequestParam(name="itmcd_col", required=true) String itmcd_col
		) { 
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
        Date date = new Date(time);
		
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("date", format.format(date));
        params.put("com_scd",com_scd);
        params.put("sti_cd",sti_cd);
        params.put("itmcd_col",itmcd_col);
        
		ArrayList<ERPItemOrdSummary> allItems = lOADINGORMMapper.getLoadingitemOrmSummaryList(params);
		 
		return gson.toJson(allItems);
	}	
	
	@ApiOperation(value = "erp_sigongmiguelRecreateAutoProc_asignteam", notes = "시공미결재일정 자동 접수 처리(지역할당팀)")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK !!"), @ApiResponse(code = 5001, message = "시공미결재일정 자동 접수 처리(지역할당팀) 실패") })	
	@GetMapping("/erp_sigongmiguelRecreateAutoProc_asignteam")  
	@RequestMapping(value = "/erp_sigongmiguelRecreateAutoProc_asignteam", method = RequestMethod.GET)
	public String erp_sigongmiguelRecreateAutoProc_asignteam (

			@RequestParam(name="plm_no", required=true) String plm_no,
			@RequestParam(name="req_resigong_dt", required=true) String req_resigong_dt,
			@RequestParam(name="com_undsec", required=true) String com_undsec,
			@RequestParam(name="req_rem_ftm", required=true) String req_rem_ftm
		) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		ERPPraNewRptNo item = null;
		try {
			
			HashMap<String,Object> params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);

	        //재일정 요청을 위한 기본 정보 가져오기 1
	        dataResult = sCheduleMainListMapper.selectSigongReqBasicInfomation(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectSigongReqBasicInfomation 오류");
	        	return gson.toJson(response);
	        }	
	        
	        String org_com_agsec = "";
	        String org_com_brand = "";
	        String org_com_scd = "";
	        String org_sti_cd = "";
	        String org_orm_no = "";
	        String org_plm_cdt = "";
	        
	        org_com_agsec = dataResult.getData1();
	        org_com_brand = dataResult.getData2();
	        org_com_scd = dataResult.getData3();
	        org_sti_cd = dataResult.getData4(); 
	        org_orm_no = dataResult.getData5();
	        org_plm_cdt = dataResult.getData6();
	        
	        //재일정 요청을 위한 기본 정보 가져오기 2
	        dataResult = sCheduleMainListMapper.selectSigongReqBasicInfomation2(params);	

	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectSigongReqBasicInfomation2 오류");
	        	return gson.toJson(response);
	        }		        
	        
	        String org_agt_cd = "";
	        String org_orm_gpost = "";
	        String org_town_cd = "";
	        String org_orm_gaddr = "";
	        String org_rem_dt = "";
	    
	        org_agt_cd = dataResult.getData1();
	        org_orm_gpost = dataResult.getData2();
	        org_town_cd = dataResult.getData3();
	        org_orm_gaddr = dataResult.getData4(); 	        
	        org_rem_dt = dataResult.getData5();
	        
	        
	        
	        params.put("org_com_agsec", org_com_agsec);
	        params.put("org_com_brand", org_com_brand);
	        params.put("org_com_scd", org_com_scd);
	        
	        params.put("org_orm_no", org_orm_no);
	        params.put("org_agt_cd", org_agt_cd);
	        params.put("org_orm_gpost", org_orm_gpost);
	        params.put("org_town_cd", org_town_cd);
	        params.put("org_orm_gaddr", org_orm_gaddr);
	        params.put("org_rem_dt", org_rem_dt);
	        params.put("req_resigong_dt", req_resigong_dt);
	        params.put("req_rem_ftm", req_rem_ftm);
	        
	        //원 지역 할당 시공팀 정보 가져오기
	        dataResult = sCheduleMainListMapper.selectOriginalStiTeamGet(params);
	        
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectOriginalStiTeamGet 오류");
	        	return gson.toJson(response);
	        }	    		
	        
	        String asign_sti_team = "";
	        asign_sti_team = dataResult.getData1();	   
	        
	        org_sti_cd = asign_sti_team;
	        params.put("org_sti_cd", org_sti_cd);
	        
	        //신규 시공번호 채번
			dataResult = sCheduleMainListMapper.executePraFaAseqrem(params);
			
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("executePraFaAseqrem 오류");
	        	return gson.toJson(response);
	        }	    		
	        
	        String new_plm_no = "";
	        new_plm_no = dataResult.getData1();	        
	        
	        //신규 tc_planmst insert
	        params.put("new_plm_no", new_plm_no);
        	res = sCheduleMainListMapper.insertSigongMigeulReRequest(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertSigongMigeulReRequest 오류");
	        	return gson.toJson(response);
			}	 	        
	        
	        //신규 tc_plandtl insert
	        params.put("new_plm_no", new_plm_no);
        	res = sCheduleMainListMapper.insertSigongMigeulDetailReRequest(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertSigongMigeulDetailReRequest 오류");
	        	return gson.toJson(response);
			}		        
	        
			//재시공 품목 정보 update
			res = sCheduleMainListMapper.updateSigonReConfirmInform(params);
			 
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateSigonReConfirmInform 오류");
	        	return gson.toJson(response);
			}
			
			//신규 tc_resmst의 rem_seq 채번
			dataResult = sCheduleMainListMapper.selectRemSeqNowNo(params);
			
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("selectRemSeqNowNo 오류");
	        	return gson.toJson(response);
	        }	    			
			
			int now_rem_seq = dataResult.getValue1();
			
			params.put("now_rem_seq", now_rem_seq);
			res = sCheduleMainListMapper.updateTcSeqnoInfReCreate(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateTcSeqnoInfReCreate 오류");
	        	return gson.toJson(response);
			}		
			
			String new_rem_seq = "";
			
			if(org_com_agsec.equals("C02I")) {
				new_rem_seq = "IC" +  String.format( "%1$04d" , now_rem_seq ) ;		
			}else if (org_com_agsec.equals("C02F")) {
				new_rem_seq = "FC" +  String.format( "%1$04d" , now_rem_seq ) ;
			}else if (org_com_agsec.equals("C02P")) {
				new_rem_seq = "PC" +  String.format( "%1$04d" , now_rem_seq ) ;
			}
			
			params.put("new_rem_seq", new_rem_seq);
			params.put("plm_cdt2", org_plm_cdt);
	        //신규 tc_resmst insert
        	res = sCheduleMainListMapper.insertNewTcResmst_org(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertNewTcResmst 오류");
	        	return gson.toJson(response);
			}				
			
			
        	res = sCheduleMainListMapper.insertNewTcResdtl(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("insertNewTcResdtl 오류");
	        	return gson.toJson(response);
			}			
			
			
			res = sCheduleMainListMapper.modifyOrmPsts_U(params);
        	
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("modifyOrmPsts_U 오류");
	        	return gson.toJson(response);
			}	

			String com_agsec = "";
			if ("C02F".equals(org_com_agsec)) {
				com_agsec = "T01F";
			} else if ("C02I".equals(org_com_agsec)) {
				com_agsec = "T01I";
			} else if ("C02P".equals(org_com_agsec)) {
				com_agsec = "T01P";
			} else {
				com_agsec = org_com_agsec;
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_agsec", com_agsec);
			params.put("com_scd", org_com_scd);
			params.put("plm_cdt", org_plm_cdt);
			params.put("req_resigong_dt",req_resigong_dt);
			params.put("com_undsec", com_undsec);
			
			dataResult = sCheduleMainListMapper.selectFinalResult(params);
	        if (dataResult == null) {
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("재일정, 반품 기준일자 조회 오류");
	        	return gson.toJson(response);
	        }
	
			String final_result = dataResult.getData1();
			
			params.put("final_result", final_result);			
			//재시공 update
			res = sCheduleMainListMapper.updateFinalResult(params);
			if (res < 1) {    				
	        	txManager.rollback(status);
	        	response.setResultCode("5001");
	        	response.setResultMessage("updateFinalResult 오류");
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
