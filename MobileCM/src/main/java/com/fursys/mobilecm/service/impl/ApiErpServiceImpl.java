package com.fursys.mobilecm.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fursys.mobilecm.dto.MemberDto;
import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpCalculateMoneyMapper;
import com.fursys.mobilecm.mapper.ErpPraAcctListSaveMapper;
import com.fursys.mobilecm.mapper.ErpSigongAsMapper;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.mapper.ScheduleMainListMapper;
import com.fursys.mobilecm.mapper.UserMapper;
import com.fursys.mobilecm.role.Role;
import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.utils.CommonObjectUtils;
import com.fursys.mobilecm.utils.FcmMessage;
import com.fursys.mobilecm.utils.GoGoVan;
import com.fursys.mobilecm.utils.StringUtil;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.FursysUser;
import com.fursys.mobilecm.vo.erp.ERPAsCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPAsResult;
import com.fursys.mobilecm.vo.erp.ERPGoGoVan;
import com.fursys.mobilecm.vo.erp.ERPGoGoVanWayPoint;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;
import com.fursys.mobilecm.vo.erp.ERPScheduleList;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoney;
import com.fursys.mobilecm.vo.erp.ERPSigongCalculateMoneyTeam;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_M01;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_P02;
import com.fursys.mobilecm.vo.erp.apm0020_m01.APM0020_P03;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_btrip;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_list1;
import com.fursys.mobilecm.vo.erp.apm0020_m01.ds_tabList1;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList1;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList2;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlandtlList3;
import com.fursys.mobilecm.vo.erp.crs0010_m01.ds_tcPlanmstList;
import com.fursys.mobilecm.vo.mobile.response.AsResultResponse;
import com.fursys.mobilecm.vo.mobile.response.GoGoVanResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongResultResponse;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Service
public class ApiErpServiceImpl  implements ApiErpService {
	@Autowired ErpSigongAsMapper erpsigongasMapper;
	@Autowired ErpPraAcctListSaveMapper erppraacctlistsaveMapper;
	@Autowired CRS0010_M01Mapper crs0010_m01Mapper; 
	@Autowired ScheduleMainListMapper schedulemainlistMapper;
	@Autowired ErpCalculateMoneyMapper erpCalculateMoneyMapper;
	
	@Autowired private PlatformTransactionManager txManager;
	Gson gson = new Gson();
		
	@Override		
	public SigongResultResponse erp_SiGongMigeulResultSave(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		SigongResultResponse response = new SigongResultResponse();
		
		try {			
			int res = 0;
			DataResult dataResult = new DataResult();
		
			String as_plm_no = (String) param.get("plm_no");
			String as_usr_id = (String) param.get("usr_id");
			String mob_std = (String) param.get("mob_std");
			String mob_remark = (String) param.get("mob_remark");
			HashMap<String,Object> inVar = new HashMap<String, Object>();

			ds_tcPlanmstList ds_tcPlanmstList = crs0010_m01Mapper.retrievesTcPlanmstList(param);

	        //????????? ??????
	    	if ("Y".equals(ds_tcPlanmstList.med_yn)) {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????? ????????????.");
				return response;
	        }
	    	
			if("C104".equals(ds_tcPlanmstList.com_plmfg)){
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ??????????????? ?????? ?????? ??????/?????? ??? ??? ????????????.");
				return response;
			}

			if (!"C13W".equals(ds_tcPlanmstList.com_rmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ???????????? ?????? ?????? ????????? ??? ????????????.");
				return response;
			}	        
	        
            int mstUpdChk = 0;
            int dtlUpdChk = 0;
            
            Map mapMst = new HashMap<String, String>();
            mapMst.put("plm_no", as_plm_no);
            mapMst.put("usr_id", as_usr_id);
            mapMst.put("mob_remark", mob_remark);
            
        	// ?????????????????? ????????? ??????        	
            mapMst.put("com_rdsec_aft", "C13Y");
    		mapMst.put("gubun", "Y");    		
    		
    		res = crs0010_m01Mapper.modyfyTcplanmstAllComplete(mapMst);
    		if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modyfyTcplanmstAllComplete ?????? [" + res + "]");
				return response;
			}
    		
            List<Map> chkList = crs0010_m01Mapper.retrievesTcPlandtlCompleChk(mapMst);            
            for(int x = 0 ; x < chkList.size() ; x++){
            	Map chkMap = chkList.get(x);
            	
            	int nCount = Integer.parseInt(chkMap.get("C13N_COUNT").toString());
            	int wCount = Integer.parseInt(chkMap.get("C13W_COUNT").toString());
            	
            	//??????????????? 0 ?????? ??????
            	if (wCount > 0){
            		mapMst.put("com_rmfg_aft", "C13W");
            		mapMst.put("com_plmfg_aft", "C102");
            	}
            	//???????????? 0 ?????? ??????
            	else if(nCount > 0 ){
            		mapMst.put("com_rmfg_aft", "C13N");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            	//?????? ,???????????? ?????? 0?????? ????????? ?????? ?????? ?????? 
            	else{
            		mapMst.put("com_rmfg_aft", "C13Y");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            }
            
            res = crs0010_m01Mapper.modyfyReTcPlanMstComple(mapMst);
            System.out.println("???????????? ?????? ???????????? ?????? [ "+ res+" ] ");
            					
            
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
	public BaseResponse erp_requestGoGoVan(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		int res = 0;
		try {
			String rem_dt = (String) param.get("rem_dt");
			String rem_seq = (String) param.get("rem_seq");
			String order_id = rem_dt + rem_seq;
			String appointment_at = (String) param.get("appointment_at");
			String requestor = (String) param.get("requestor");
			String requestor_telno = (String) param.get("requestor_telno");
			String vehicle = (String) param.get("vehicle");
			String pay = (String) param.get("pay");
			String remark = (String) param.get("remark");
			String name_arr = (String) param.get("name_arr");
			String addr_arr = (String) param.get("addr_arr");
			String mobile_no_arr = (String) param.get("mobile_no_arr");
			String request_at_arr = (String) param.get("request_at_arr");
			String company_arr = (String) param.get("company_arr");
			String team_arr = (String) param.get("team_arr");
			String description_arr = (String) param.get("description_arr");
			
			ERPGoGoVan gogovan = new ERPGoGoVan();
			gogovan.setUser_code("300000104");
			gogovan.setCustomer_order_id(order_id);
			gogovan.setAppointment_at(appointment_at);
			gogovan.setVehicle(vehicle);
			gogovan.setPay(pay);
			gogovan.setRemark(remark);
			
			params = new HashMap<String, Object>();
			params.put("addr_arr", addr_arr);
			params.put("name_arr", name_arr);
			params.put("mobile_no_arr", mobile_no_arr);
			params.put("request_at_arr", request_at_arr);
			params.put("description_arr", description_arr);
			params.put("company_arr", company_arr);
			params.put("team_arr", team_arr);
			
			ArrayList<ERPGoGoVanWayPoint> waypoint = erpCalculateMoneyMapper.selectWayPointList(params);		
			if (waypoint.size() < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectWayPointList ?????? [??????2??? ????????? ??????/??????/????????? ????????? ???????????????.]");
				return response;
			}
			
			gogovan.setWaypoint(waypoint);

			System.out.println(gson.toJson(gogovan));
			
			GoGoVanResponse gogovan_res = GoGoVan.sumit(gogovan);
			if (!"200".equals(gogovan_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????? ??????[" + gogovan_res.getResultMessage() +"]");
				return response;
			}
			
			params = new HashMap<String, Object>();
			params.put("rem_dt", rem_dt);
			params.put("rem_seq", rem_seq);
						
			res = erpCalculateMoneyMapper.updateGoGoVan(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateGoGoVan ?????? [" + res + "]");
				return response;
			}
			
		} catch (Exception e) {
			txManager.rollback(status);
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}
		
		txManager.commit(status);
		//response.setResultCode("200");		
		return response;

	}
	
	@Override		
	public ERPAsCalculateMoney erp_selectAsCalculateMoney(HashMap<String, Object> param) {
		ERPAsCalculateMoney item;
		HashMap<String, Object> params;
		
		try {
			String as_fr_dt = (String) param.get("fr_dt");
			String as_to_dt = (String) param.get("to_dt");
			String as_sti_cd = (String) param.get("sti_cd");

			params = new HashMap<String, Object>();
	        params.put("fr_dt", as_fr_dt);
	        params.put("to_dt", as_to_dt);
	        params.put("sti_cd", as_sti_cd);

	        item = erpCalculateMoneyMapper.selectAsCalculateMoney(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return item;
	}
	
	@Override		
	public ArrayList<ERPSigongCalculateMoneyTeam> erp_selectSigongCalculateMoneyTeam(HashMap<String, Object> param) {
		ArrayList<ERPSigongCalculateMoneyTeam> allitems;
		HashMap<String, Object> params;
		
		try {
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_yyyymm = (String) param.get("yyyymm");

			params = new HashMap<String, Object>();
	        params.put("k_sti_cd", as_k_sti_cd);
	        params.put("yyyymm", as_yyyymm + "01");
	        
	        DataResult data = erpCalculateMoneyMapper.selectCalculateMoneyDate(params);
	        String fr_year = data.getData1() + "%";
	        String fr_prev_month = data.getData2() + "01";
	        String to_prev_month = data.getData2() + "31";
	        String fr_dt = data.getData3() + "01";
	        String to_dt = data.getData3() + "31";
	        
	        params.put("fr_year", fr_year);
	        params.put("fr_prev_month", fr_prev_month);
	        params.put("to_prev_month", to_prev_month);
	        params.put("fr_dt", fr_dt);
	        params.put("to_dt", to_dt);
	        
	        allitems = erpCalculateMoneyMapper.selectSigongCalculateMoneyTeam(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
	
	@Override		
	public ERPSigongCalculateMoney erp_sigongCalculateMoney(HashMap<String, Object> param) {
		ERPSigongCalculateMoney item;
		HashMap<String, Object> params;
		
		try {
			String as_fr_dt = (String) param.get("fr_dt");
			String as_to_dt = (String) param.get("to_dt");
			String as_k_sti_cd = (String) param.get("k_sti_cd");
			String as_sti_cd = (String) param.get("sti_cd");

			params = new HashMap<String, Object>();
	        params.put("fr_dt", as_fr_dt);
	        params.put("to_dt", as_to_dt);
	        params.put("k_sti_cd", as_k_sti_cd);
	        params.put("sti_cd", as_sti_cd);

	        item = erpCalculateMoneyMapper.selectSigongCalculateMoney(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return item;
	}
	
	@Override
	public BaseResponse erp_AsResultSave(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		int res = 0;
		
		try {			
			String as_plm_no = (String) param.get("plm_no");
			String as_remark = (String) param.get("remark");
			String as_usr_id = (String) param.get("usr_id");
			
			String as_com_pldsec_arr = (String) param.get("com_pldsec_arr");
			String as_ord_sseq_arr = (String) param.get("ord_sseq_arr");
			String as_pld_fqty_arr = (String) param.get("pld_fqty_arr");
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
			params.put("remark", as_remark);
			params.put("usr_id", as_usr_id);
			params.put("com_pldsec_arr", as_com_pldsec_arr);
			params.put("ord_sseq_arr", as_ord_sseq_arr);
			params.put("pld_fqty_arr", as_pld_fqty_arr);
			
			if (!"".equals(as_remark)) {
				res = crs0010_m01Mapper.updateAsResultRemark(params);
	            if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateAsResultRemark ?????? [" + res + "]");
					return response;
				}
			}
			
			if (!"".equals(as_com_pldsec_arr)) {
				res = crs0010_m01Mapper.updateAsResult(params);
	            if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateAsResult ?????? [" + res + "]");
					return response;
				}
			}
                        
            res = schedulemainlistMapper.updateAddAsEndTime(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime ?????? [" + res + "]");
				return response;
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
	public BaseResponse erp_insertTcStiReq(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;
		int res = 0;
		
		try {
			String as_plm_no = (String) param.get("plm_no");
			String as_sti_cd = (String) param.get("sti_cd");
			String as_usr_id = (String) param.get("usr_id");
						
			params = new HashMap<String, Object>();
			params.put("PLM_NO", as_plm_no);
			params.put("STI_CD", as_sti_cd);
			params.put("USR_CD", as_usr_id);
			
    		res = crs0010_m01Mapper.insertTcStiReq(params);
    		if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertTcStiReq ?????? [" + res + "]");
				return response;
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
	public SigongResultResponse erp_SigonResultMigeulReason(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		SigongResultResponse response = new SigongResultResponse();
		DataResult dataResult = new DataResult();
		HashMap<String, Object> params;
		int res = 0;
		
		try {
			List<String> codeList = new ArrayList<String>();
			
			String as_plm_no = (String) param.get("plm_no");
			String as_usr_id = (String) param.get("usr_id");
			
			String as_com_unpsec = (String) param.get("com_unpsec");
			String as_com_undsec = (String) param.get("com_undsec");
			String as_pld_rcdt = (String) param.get("pld_rcdt");
			String as_pld_rasdt = (String) param.get("pld_rasdt");
			String as_pld_rmk = (String) param.get("pld_rmk");
			String as_mob_rmk = (String) param.get("mob_rmk");

			String as_com_pldsec_gbn_arr = (String) param.get("com_pldsec_gbn_arr");
			String as_com_pldsec_arr = (String) param.get("com_pldsec_arr");
			String as_ord_sseq_arr = (String) param.get("ord_sseq_arr");
			String as_ord_iseq_arr = (String) param.get("ord_iseq_arr");
			String as_pld_fqty_arr = (String) param.get("pld_fqty_arr");
			String as_ord_rmk_arr = (String) param.get("ord_rmk_arr");
			
			if ("C68000".equals(as_com_undsec)) { // ????????????????????? -> ERP??????????????? ??????
				as_com_undsec = "C52B01";//	???????????????
			} else if ("C68001".equals(as_com_undsec)) {
				as_com_undsec = "C52B02";//	???????????????
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
			params.put("com_unpsec", as_com_unpsec);
			params.put("com_undsec", as_com_undsec);
			params.put("pld_rmk", as_pld_rmk);
			params.put("user_id", as_usr_id);
			params.put("usr_id", as_usr_id);						

			ds_tcPlanmstList ds_tcPlanmstList = crs0010_m01Mapper.retrievesTcPlanmstList(param);
			if(ds_tcPlanmstList == null){
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("retrievesTcPlanmstList ??????.");
				return response;
	    	}
			
			HashMap<String,Object> inVar = new HashMap<String, Object>();
	        inVar.put("PLM_NO", as_plm_no);
	        inVar.put("COM_UNPSEC", as_com_unpsec);
	        inVar.put("COM_UNDSEC", as_com_undsec);
	        inVar.put("PLD_RMK", as_pld_rmk);
	        inVar.put("mob_remark", as_mob_rmk);
			inVar.put("USR_ID", as_usr_id);   
			inVar.put("usr_id", as_usr_id);
			inVar.put("USER_CD", as_usr_id);			
	        inVar.put("gv_userId", as_usr_id);
	        inVar.put("PLD_RCDT", as_pld_rcdt.replaceAll("-",  ""));
	        inVar.put("PLD_RASDT" , as_pld_rasdt.replaceAll("-",  ""));
	        
	        inVar.put("com_pldsec_gbn_arr", as_com_pldsec_gbn_arr);
	        inVar.put("com_pldsec_arr", as_com_pldsec_arr);
	        inVar.put("ord_sseq_arr", as_ord_sseq_arr);
	        inVar.put("ord_iseq_arr", as_ord_iseq_arr);
	        inVar.put("pld_fqty_arr", as_pld_fqty_arr);
	        inVar.put("ord_rmk_arr", as_ord_rmk_arr);	        
			
	        inVar.put("plm_no", as_plm_no);
	        
	        //??????
	        //inVar.put("com_pldsec", "C090");	        
	        codeList = new ArrayList<String>();
	        codeList.add("C090");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "A");
	        ArrayList<ds_tcPlandtlList1> input1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);	        

	        //????????????,????????????
	        //inVar.put("com_pldsec", "'C092','C096','C095'");
	        codeList = new ArrayList<String>();
	        codeList.add("C092");
	        codeList.add("C096");
	        codeList.add("C095");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "B");
	        ArrayList<ds_tcPlandtlList1> input2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
	        
	        //????????????,?????????
	        //inVar.put("com_pldsec", "'C094','C09A'");
	        codeList = new ArrayList<String>();
	        codeList.add("C094");
	        codeList.add("C09A");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "C");
	        ArrayList<ds_tcPlandtlList2> input3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);

	        //?????????????????? - ?????????????????? ??????
	        //ArrayList<ds_tcPlandtlList3> input3 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);	        	
	        
	    	//????????? ??????
	    	if("Y".equals(ds_tcPlanmstList.med_yn)){
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????? ????????????.");
				return response;
	    	}

	    	String plmNo = "", ComPldsec = "", OrdSseq = "", OrdIseq = "", ItmCd = "", dsGubun = "", setDsC090 = "", setDsC092 = "", setDsC096 = "";		    	
	    	int chk= 0;
	    	for(int i = 0; i < input1.size(); i++){
    			if("C13Y".equals(input1.get(i).com_rdsec)){
    				txManager.rollback(status);
    				response.setResultCode("5001");
    				response.setResultMessage("??????????????? ????????? ?????? ?????????????????? ??? ??? ????????????.");
    				return response;
    			}
    				
    			plmNo = input1.get(i).plm_no;
    			ComPldsec = input1.get(i).com_pldsec; 
    			OrdSseq = input1.get(i).ord_sseq;
    			OrdIseq = input1.get(i).ord_iseq; 
    			ItmCd = input1.get(i).itm_cd;

    			chk++;
	    	}	
	    		    		        
        	int updateCount = 0;
        	String suspenChk = "";
        	String rdsecChk = "";
        	System.out.println("input1_size()   =  "  + input1.size());
        	System.out.println("input2_size()   =  "  + input2.size());
        	System.out.println("input3_size()   =  "  + input3.size());
        	
        	String pld_fqty_arr1[] = as_pld_fqty_arr.split(",");
        	
        	for(int i = 0; i < input1.size();i++){
        		Map Map = CommonObjectUtils.convertObjectToMapUpperCase(input1.get(i));
        		suspenChk = (String) Map.get("SUSPEN_CHK");
        		rdsecChk = (String) Map.get("COM_RDSEC");
        		String undsec = "";
        		int fqty = 0;
        		int eqty = 0;
        		System.out.println(Map);        		
        		
        		if("1".equals(suspenChk) && "C13W".equals(rdsecChk)){
        			
        			Map.put("COM_UNPSEC", inVar.get("COM_UNPSEC"));
        			Map.put("COM_UNDSEC", inVar.get("COM_UNDSEC"));
        			Map.put("PLD_RCDT"	, inVar.get("PLD_RCDT"));
        			Map.put("PLD_RASDT"	, inVar.get("PLD_RASDT"));
        			Map.put("PLD_RMK"	, inVar.get("PLD_RMK"));
        			Map.put("USER_CD"	, inVar.get("gv_userId"));
        			
        			undsec = (String) inVar.get("COM_UNDSEC");
        			
        			if(/*!"C52B02".equals(undsec) && !"C52C01".equals(undsec) && */!"C52B01".equals(undsec)){
        				
        				//System.out.println("undsec == C52C01");
        				
        				fqty = Integer.parseInt(Map.get("PLD_EQTY").toString()) ;
        				eqty = Integer.parseInt(Map.get("PLD_FQTY").toString()) ;

        				if(fqty == eqty){
        					System.out.println("eqty == fqty ????????? ??????????????? 0 ?????? ?????? update");
        					
        					Map.put("PLD_FQTY", "0");
        					Map.put("PLD_CFAMT", "0");
        				}
        			
        				System.out.println("PLD_FQTY1 ==" + Map.get("PLD_FQTY"));
        			}
        			
        			//2018.04.27 |?????????| ??????????????? ???????????? 
        			//?????????????????? ?????? ???????????? 0?????? ??????
        			else if("C52B01".equals(undsec)){
        				
        				Map.put("PLD_FQTY", "0");
        				Map.put("PLD_CFAMT", "0");
        				
//        				Map.put("PLD_FQTY", pld_fqty_arr1[i]);
        				
        			}
        			
        			//????????????
        			crs0010_m01Mapper.modyfyTcPlandtlSuspense_U_2nd(Map);
                	updateCount++;
        		}
        	}
        	
        	String pld_fqty_arr2[] = as_pld_fqty_arr.split(",");
        	
        	for(int j = 0; j < input2.size();j++){
        		Map Map2 = CommonObjectUtils.convertObjectToMapUpperCase(input2.get(j));
        		suspenChk = (String) Map2.get("SUSPEN_CHK");
        		rdsecChk = (String) Map2.get("COM_RDSEC");
        		String undsec = "";
        		int fqty = 0;
        		int eqty = 0;
        		System.out.println(Map2);
        		
        		if("1".equals(suspenChk) && "C13W".equals(rdsecChk)){
        			
        			Map2.put("COM_UNPSEC", inVar.get("COM_UNPSEC"));
        			Map2.put("COM_UNDSEC", inVar.get("COM_UNDSEC"));
        			Map2.put("PLD_RCDT"	, inVar.get("PLD_RCDT"));
        			Map2.put("PLD_RASDT", inVar.get("PLD_RASDT"));
        			Map2.put("PLD_RMK"	, inVar.get("PLD_RMK"));
        			Map2.put("USER_CD"	, inVar.get("gv_userId"));
        			
        			undsec = (String) inVar.get("COM_UNDSEC");
        			
        			if(!"C52B02".equals(undsec) && !"C52C01".equals(undsec) && !"C52B01".equals(undsec)){
        				
        				//System.out.println("undsec == C52C01");
        				
        				fqty = Integer.parseInt(Map2.get("PLD_EQTY").toString()) ;
        				eqty = Integer.parseInt(Map2.get("PLD_FQTY").toString()) ;

        				if(fqty == eqty){
        					System.out.println("eqty == fqty ????????? ??????????????? 0 ?????? ?????? update");
        					
        					Map2.put("PLD_FQTY", "0");
        					Map2.put("PLD_CFAMT", "0");
        				}
        			
        			}
        			//2018.04.27 |?????????| ??????????????? ???????????? 
        			//?????????????????? ?????? ???????????? 0?????? ??????
        			else if("C52B01".equals(undsec)){
        				
        				Map2.put("PLD_FQTY", "0");
        				Map2.put("PLD_CFAMT", "0");
        				
//       				Map2.put("PLD_FQTY", pld_fqty_arr2[j]);
        				
        			}
        			
        			//????????????
        			crs0010_m01Mapper.modyfyTcPlandtlSuspense_U_2nd(Map2);
                	updateCount++;
        		}
        	}
        	        	
        	String pld_fqty_arr3[] = as_pld_fqty_arr.split(",");
        	for(int x = 0; x < input3.size();x++){
        		Map Map3 = CommonObjectUtils.convertObjectToMapUpperCase(input3.get(x));
        		suspenChk = (String) Map3.get("SUSPEN_CHK");
        		rdsecChk = (String) Map3.get("COM_RDSEC");
        		String undsec = "";
        		int fqty = 0;
        		int eqty = 0;
        		System.out.println(Map3);
        		
        		if("1".equals(suspenChk) && "C13W".equals(rdsecChk)){
        			
        			Map3.put("COM_UNPSEC", inVar.get("COM_UNPSEC"));
        			Map3.put("COM_UNDSEC", inVar.get("COM_UNDSEC"));
        			Map3.put("PLD_RCDT"	, inVar.get("PLD_RCDT"));
        			Map3.put("PLD_RASDT", inVar.get("PLD_RASDT"));
        			Map3.put("PLD_RMK"	, inVar.get("PLD_RMK"));
        			Map3.put("USER_CD"	, inVar.get("gv_userId"));
        			
        			undsec = (String) inVar.get("COM_UNDSEC");
        			
        			if(!"C52B02".equals(undsec) && !"C52C01".equals(undsec) && !"C52B01".equals(undsec)){
        				
        				//System.out.println("undsec == C52C01");
        				
        				fqty = Integer.parseInt(Map3.get("PLD_EQTY").toString()) ;
        				eqty = Integer.parseInt(Map3.get("PLD_FQTY").toString()) ;

        				if(fqty == eqty){
        					System.out.println("eqty == fqty ????????? ??????????????? 0 ?????? ?????? update");
        					
        					Map3.put("PLD_FQTY", "0");
        					Map3.put("PLD_CFAMT", "0");
        				}
        			
        			}
        			//2018.04.27 |?????????| ??????????????? ???????????? 
        			//?????????????????? ?????? ???????????? 0?????? ??????
        			else if("C52B01".equals(undsec)){
        				
        				Map3.put("PLD_FQTY", "0");
        				Map3.put("PLD_CFAMT", "0");
        				
//        				Map3.put("PLD_FQTY", pld_fqty_arr3[x]);
        				
        			}
        			
        			//????????????
        			crs0010_m01Mapper.modyfyTcPlandtlSuspense_U_2nd(Map3);
                	updateCount++;
        		}
        	}
        	
        	System.out.println("???????????? ?????? ?????? [ " + updateCount + " ] ");
        	
        	List <Map> chkList = null;
            
        	//mst ????????? ????????? ?????? dtl????????? ??????
            chkList = crs0010_m01Mapper.retrievesTcPlandtlCompleChk(inVar);
            
            for(int j = 0 ; j < chkList.size() ; j++){
            	Map chkMap = chkList.get(j);
            	
            	int nCount = Integer.parseInt(chkMap.get("C13N_COUNT").toString());
            	int wCount = Integer.parseInt(chkMap.get("C13W_COUNT").toString());
            	
            	
            	//??????????????? 0 ?????? ??????
            	if (wCount > 0){
            		inVar.put("com_rmfg_aft", "C13W");
            		inVar.put("com_plmfg_aft", "C102");
            	}
            	//???????????? 0 ?????? ??????
            	else if(nCount > 0 ){
            		inVar.put("com_rmfg_aft", "C13N");
            		inVar.put("com_plmfg_aft", "C103");
            	}
            	//?????? ,???????????? ?????? 0?????? ????????? ?????? ?????? ?????? 
            	else{
            		inVar.put("com_rmfg_aft", "C13Y");
            		inVar.put("com_plmfg_aft", "C103");
            	}
            }

            int mstChk = crs0010_m01Mapper.modyfyTcPlanMstComple(inVar);
            System.out.println("???????????? ?????? ???????????? ?????? [ "+ mstChk+" ] ");
	        
        	//??????????????????
        	params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddSigongEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime ?????? [" + res + "]");
				return response;
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
	public SigongResultResponse erp_SiGongAccSave(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		SigongResultResponse response = new SigongResultResponse();
		DataResult dataResult = new DataResult();
		
		try {
			ds_tcPlanmstList ds_tcPlanmstList = crs0010_m01Mapper.retrievesTcPlanmstList(param);
			int res = 0;
			String as_plm_no = (String) param.get("plm_no");
			String as_usr_id = (String) param.get("usr_id");
			HashMap<String,Object> inVar = new HashMap<String, Object>();

	        inVar.put("plm_no", as_plm_no);
	        inVar.put("usr_id", as_usr_id);            
	        inVar.put("gv_userId", as_usr_id);
			
//	        //??????
//	        inVar.put("plm_no", as_plm_no);
//	        inVar.put("com_pldsec", "'C090'");            
//	        ds_tcPlandtlList1 ds_out1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //????????????,????????????
//	        inVar.put("com_pldsec", "'C092','C096','C095'");
//	        ds_tcPlandtlList1 ds_out2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //????????????,?????????            
//	        inVar.put("com_pldsec", "'C094','C09A'");
//	        ds_tcPlandtlList2 ds_out3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);
//	        
//	        //??????????????????            
//	        ds_tcPlandtlList3 ds_out4 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);
	        	        	        
	        //????????? ??????
	    	if ("Y".equals(ds_tcPlanmstList.med_yn)) {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????? ????????????.");
				return response;
	        }
	        
			if(!"C103".equals(ds_tcPlanmstList.com_plmfg) && !"C104".equals(ds_tcPlanmstList.com_plmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????????????????? ??????????????? ????????? ?????? ??????/?????? ??? ??? ????????????.");
				return response;
			}
			
			if("C13W".equals(ds_tcPlanmstList.com_rmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("??????????????? ?????? ???????????? ????????????.");
				return response;
			}
			
			if("".equals(ds_tcPlanmstList.plm_ftm) || ds_tcPlanmstList.plm_ftm == null){
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("??????????????? ???????????? ???????????????.");
				return response;
			}
			
			int rv_actcnt = 0;
			dataResult = crs0010_m01Mapper.getActCnt(param);
			if (dataResult != null) {
				rv_actcnt = dataResult.getValue1();
			}
			
			if("C103".equals(ds_tcPlanmstList.com_plmfg) && rv_actcnt > 0){
				//"STI_NM?????? ORM_NM?????? ?????? ??????????????? ???????????? ????????????."
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(ds_tcPlanmstList.sti_nm + "?????? " + ds_tcPlanmstList.orm_nm + "(" + ds_tcPlanmstList.orm_no + ") ?????? ?????? ??????????????? ???????????? ????????????.");
				return response;
			} else if("C104".equals(ds_tcPlanmstList.com_plmfg) && rv_actcnt == 0) {
				//"STI_NM?????? ORM_NM?????? ?????? ??????????????? ???????????? ????????????."
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(ds_tcPlanmstList.sti_nm + "?????? " + ds_tcPlanmstList.orm_nm + "(" + ds_tcPlanmstList.orm_no + ") ?????? ?????? ??????????????? ???????????? ????????????.");
				return response;
			}
			
            int mstUpdChk = 0;
            int dtlUpdChk = 0;
            List <Map> C02I_List = null;
            List <Map> C02I_90List = null;
            List <Map> C02I_92List = null;
            List <Map> C02I_94List = null;
            List <Map> C02I_96List = null;
            List <Map> C02I_9AList = null;
            List <Map> C02I_PassList = null;
            List <Map> C02I_RelayList = null;
            List <Map> dtlList = null;
            List <Map> actinfList = null;
            List <Map> trinfList = null;
            List <Map> pldsecCountList = null;
            int qty = 0;
            int amt = 0;
            int amt_mnf = 0;
            int pldCnt = 0;
            double triAmt = 0;
            String vndf = "";
            String vndt = "";
            String svnd = "";
            String lsCmp = "";
            String vfP = "";
            String vfromP = "";
            String vtP= "";
            String vtoP= "";
            String vfD = "";
            String vfromD = "";
            String vtD = "";
            String vtoD = "";
            String acdP = "";
            String acdD = "";
            String ormNmCut = "";
            String com_plmfg = "";
            
            double AciRateamtD = 0;
            double AciRateamtP = 0;

            Map mapMst = CommonObjectUtils.convertObjectToMapUpperCase(ds_tcPlanmstList);
            mapMst.put("PLM_NO", as_plm_no);
        	mapMst.put("USER_ID", as_usr_id);
        	mapMst.put("USR_ID", as_usr_id);
        	
        	String ormGgubun = (String) mapMst.get("ORM_GGUBUN");	//????????? ??????,?????? ??????
        	ormGgubun = ormGgubun.replaceAll(" ", "");
        	
        	//COM_PLMFG ????????? ????????????(???????????? ????????? refresh ????????? 2???????????? ???????????? ????????? ?????? ???????????? ??????)        	
        	dataResult = crs0010_m01Mapper.retrieveTcPlanmstComPlmfg(param);
			if (dataResult != null) { 
				com_plmfg = StringUtil.isNullToString(dataResult.getData1());
			}
        	mapMst.put("COM_PLMFG", com_plmfg);
        	
        	//?????????????????? ???????????? ??????
        	if("C103".equals(mapMst.get("COM_PLMFG"))){
            	//?????????
            	if("C21B".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2402";
    				vndt = "C2505";
    				svnd = "C21B";	
            	}
            	//?????????
            	else if("C21F".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2403";
    				vndt = "C2505";
    				svnd = "C21F";
            	}
            	//??????
            	else if("C21I".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2404";
    				vndt = "C2505";
    				svnd = "C21I";
            	}
            	else{
            		txManager.rollback(status);
    				response.setResultCode("5001");
    				response.setResultMessage("???????????? ??????????????? ???????????? ?????? ???????????? ????????????. ????????????????????? ???????????? ??????????????? ???????????? ??????????????????.");
    				return response;
            	}
            	
            	if("C02I".equals(mapMst.get("COM_AGSEC"))){
            		lsCmp = "T01I";
            	}
            	else if("C02F".equals(mapMst.get("COM_AGSEC"))){
            		lsCmp = "T01F";
            	}
            	else if("C02P".equals(mapMst.get("COM_AGSEC"))){
            		lsCmp = "T01P";
            	}
            	
            	//????????? 0????????? ????????? ???????????? insert?????? ????????? ????????? ??????
            	pldsecCountList = crs0010_m01Mapper.retrievePldsecCountList(mapMst);
            	
            	
            	//????????????
            	if("C02I".equals(mapMst.get("COM_AGSEC"))){

            		//???????????? ????????????
            		C02I_List = crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
            		
            		int LD_AMT;
            		double LD_AMT_D;
            		double LD_AMT_P;
            		
            		for(int t = 0; t < C02I_List.size();t++){
            			
            			C02I_90List 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
            			
            			Map C090Map = C02I_90List.get(t);
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C090_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//????????????
                			if(!"0".equals(C090Map.get("C090_LD_AMT"))){
                				
                				C090Map.put("USER_ID", inVar.get("gv_userId"));
	                			C090Map.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C090Map.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C090Map.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C090Map.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C090Map.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			C090Map.put("COM_PLDSEC", "C090");
	                			C090Map.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	                			vfP    = vndf; 
	            				vfromP = svnd;       //???????????????
	            				vtP    = "C2501";	 //??????to?????? : ?????????
	            				vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD  = vndf;      // 
	            				vfromD = svnd;       //???????????????
	            				vtD    = "C2505";     //??????to?????? : ?????????
	            				vtoD   = (String) mapMst.get("COM_AGSEC");    //???????????????
	            			
	            				C090Map.put("COM_VFSEC", vndf);
	            				C090Map.put("COM_VTSEC", vndt);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C090(C090Map);
	            				
	            				C090Map.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				C090Map.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
	            				C090Map.put("COM_SVND", svnd);
	            				C090Map.put("ACL_VFROM", vfromD);
	            				C090Map.put("ACL_VTO", vtoD);
	            				C090Map.put("COM_VFSEC", vfD);
	            				C090Map.put("COM_VTSEC", vtD);
	            				C090Map.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));

	                			LD_AMT = Integer.parseInt(C090Map.get("C090_LD_AMT").toString()) + Integer.parseInt(C090Map.get("C090_LD_AMT2").toString());
	                			LD_AMT_D = ( LD_AMT - Integer.parseInt(C090Map.get("C090_LD_STOCK1").toString()) - Integer.parseInt(C090Map.get("C090_LD_STOCK2").toString()) ) * 1.2; //???????????? = ????????? * 1.2

	                			LD_AMT_P = LD_AMT; 

	                			if(!"".equals(actinfList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
	                				C090Map.put("ACL_AMT_D", LD_AMT_D);
	                				C090Map.put("ACL_RMK_D", "?????????????????? = ????????????( " + LD_AMT + " - " + C090Map.get("C090_LD_STOCK1") + " - " + C090Map.get("C090_LD_STOCK2") +" )");
	                			
	                				crs0010_m01Mapper.modyfyTcActlistD_I(C090Map);
	                			}
	                			
	                			//2018.06.01 |?????????| ????????? ?????? ?????? ?????????????????? ??????
	                			//if("O1007".equals(mapMst.get("ORM_ATTR"))){
	                			ormNmCut = "";
	                			
	                			//??????????????? ??????
	                			ormNmCut = (String) mapMst.get("ORM_NM");
	                			if(ormNmCut.length() > 3){
	                				ormNmCut = ormNmCut.substring(1, 3);
	                			}
	                			
	                			System.out.println("ormNmCut   :  " + ormNmCut);
	                			
	            				if("??????".equals(ormNmCut)){	
	            					C02I_RelayList = crs0010_m01Mapper.retrievesC090RelayAmt(mapMst);
	            					
	            					if(LD_AMT_P < Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString())){
	            						
	            						
	            						//2018.06.01 |?????????| ??????????????? ????????? 2000??? ?????? ????????? ?????????????????? ???????????? ?????? [?????? ????????? 0?????? ?????????]
	            						//C090Map.put("ACL_AMT_P", "0");
		            					
		            					//LD_AMT_P = 0;
	            						
	            					}else{
	            						C090Map.put("ACL_AMT_P", LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString()));
		            					
		            					LD_AMT_P = LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString());
	            					}
	            					
	            				}
	                			
	                			if(!"".equals(actinfList.get(0).get("COM_ACD_P")) && LD_AMT_P >= 0  || "??????".equals(ormNmCut)){
	                				C090Map.put("ACL_AMT_P", LD_AMT_P);
	                				C090Map.put("ACL_RMK_P", "?????????????????? = ????????????( " + LD_AMT_P + " )");
	                				C090Map.put("ACL_VFROM", vfromP);
	                				C090Map.put("ACL_VTO", vtoP);
	                				C090Map.put("COM_VFSEC", vfP);
	                				C090Map.put("COM_VTSEC", vtP);
	                				
	                				crs0010_m01Mapper.modyfyTcActlistP_I(C090Map);
	                			}	
                				
                			}
            				
            			}
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C092_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//???????????? 
            				C02I_92List 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
                			
                			Map C092Map = C02I_92List.get(t);
            				
                			System.out.println("test c092MapAmt  :   "+C092Map.get("C092_LD_AMT"));
                			if( Integer.parseInt(C092Map.get("C092_LD_AMT").toString()) > 0){
                				C092Map.put("USER_ID", inVar.get("gv_userId"));
	                			C092Map.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C092Map.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C092Map.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C092Map.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C092Map.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			C092Map.put("COM_PLDSEC", "C092");	
	                			C092Map.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	                			C092Map.put("COM_VFSEC", vndf);
	                			
	                			actinfList = crs0010_m01Mapper.retrievesTcActinf_C092(C092Map);

	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) C092Map.get("COM_AGSEC");
	            				
	            				if("C02V".equals(C092Map.get("COM_AGSEC"))){
		        					vtD = "C2507";
		        					vtoD = (String) C092Map.get("COM_AGSEC");
		        				}else{
		        					vtD = "C2505";
		        					vtoD = (String) mapMst.get("AGT_CD");
		        				}
	            				
	            				C092Map.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				C092Map.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
	            				C092Map.put("COM_SVND", svnd);
	            				C092Map.put("ACL_VFROM", vfromD);
	            				C092Map.put("ACL_VTO", vtoD);
	            				C092Map.put("COM_VFSEC", vfD);
	            				C092Map.put("COM_VTSEC", vtD);            				
	            				C092Map.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));	            				
	            				
	            				//???????????? * ??????
	            				LD_AMT_D = Integer.parseInt(C092Map.get("C092_LD_AMT").toString()) * 0.9;
	            				LD_AMT_P = Integer.parseInt(C092Map.get("C092_LD_AMT").toString()) * 0.82;
	            				
	            				if(!"".equals(actinfList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
	            					
	            					C092Map.put("ACL_RMK_D", "?????????????????? = ????????????( " + C092Map.get("C092_LD_AMT") + ") * ?????????( 0.9 )");
		            				C092Map.put("ACL_AMT_D", LD_AMT_D);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C092Map);
	            				}
	            				
	            				C092Map.put("ACL_RMK_P", "?????????????????? = ????????????( "+ C092Map.get("C092_LD_AMT") + ") * ?????????( 0.82 )");
	            				C092Map.put("ACL_AMT_P", LD_AMT_P);
	            				
	            				
	            				if("O1007".equals(mapMst.get("ORM_ATTR"))){
	            					
	            					C02I_RelayList = crs0010_m01Mapper.retrievesC092RelayAmt(mapMst);
	            					
	            					if(LD_AMT_P < Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString())){
	            						C092Map.put("ACL_AMT_P", "0");
	            						
	            					}else{
	            						C092Map.put("ACL_AMT_P", LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString()));
	            					}
	            					
	            					
	            				}
	            				
	            				C092Map.put("ACL_VFROM", vfromP);
	            				C092Map.put("ACL_VTO", vtoP);
	            				C092Map.put("COM_VFSEC", vfP);
	            				C092Map.put("COM_VTSEC", vtP);
	            				
	            				crs0010_m01Mapper.modyfyTcActlistP_I(C092Map);
	            				
	            				LD_AMT = Integer.parseInt(C092Map.get("C092_LD_AMT").toString());
	            				
	            				C092Map.clear();
	            				
	            				C092Map.put("USER_ID", inVar.get("gv_userId"));
	                			C092Map.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C092Map.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C092Map.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C092Map.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C092Map.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			C092Map.put("COM_PLDSEC", "C092");	
	                			C092Map.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	                			vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    = "C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	    								
	            				vfD = vndf;
	            				vfromD = (String) C092Map.get("COM_AGSEC");
            					vtD = "C2505";
	        					vtoD = (String) mapMst.get("AGT_CD");		
    							
	                			C092Map.put("COM_VFSEC", vfD);
	                			
	                			//??????1??? ???????????? ?????? ?????? ??????
	                			C02I_PassList = crs0010_m01Mapper.retrievesC092PassList(C092Map);
	                			
	                			if(C02I_PassList.size() > 0){

		                			
		        					if("C12R".equals(C02I_PassList.get(0).get("COM_RASEC_D"))){
		        						LD_AMT_D = LD_AMT * Double.parseDouble(C02I_PassList.get(0).get("ACI_RATEAMT_D").toString());
		        						
		        					}else{
		        						
		        						LD_AMT_D =  Double.parseDouble(C02I_PassList.get(0).get("ACI_RATEAMT_D").toString());
		        					}
		        					
		        					C092Map.put("COM_ACD_D", C02I_PassList.get(0).get("COM_ACD_D"));
		            				C092Map.put("COM_ACD_P", C02I_PassList.get(0).get("COM_ACD_P"));
		            				C092Map.put("COM_SVND", svnd);
		            	         	C092Map.put("COM_CESEC", C02I_PassList.get(0).get("COM_CESEC"));	
		            	         	C092Map.put("ACL_VFROM", vfromD);
		            				C092Map.put("ACL_VTO", vtoD);
		            				C092Map.put("COM_VFSEC", vfD);
		            				C092Map.put("COM_VTSEC", vtD);
		        					
		        					if("C12R".equals(C02I_PassList.get(0).get("COM_RASEC_P"))){
		        						LD_AMT_P = LD_AMT * Double.parseDouble(C02I_PassList.get(0).get("ACI_RATEAMT_P").toString());
		        						
		        					}else{
		        						LD_AMT_P = Double.parseDouble(C02I_PassList.get(0).get("ACI_RATEAMT_P").toString());
		        					}
		        					
		        					if(!"".equals(C02I_PassList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
		        						
		        						C092Map.put("ACL_RMK_D", "?????????????????? ( " + LD_AMT_D + " ) = ????????????( " + LD_AMT + " ) * ????????? ( " + C02I_PassList.get(0).get("ACI_RATEAMT_D") + " )" );
		        						C092Map.put("ACL_AMT_D", LD_AMT_D);
		        						
		        						crs0010_m01Mapper.modyfyTcActlistD_I(C092Map);
		        					}
		        					
		        					if(!"".equals(C02I_PassList.get(0).get("COM_ACD_P")) && LD_AMT_P > 0  ){
		        						
		        						C092Map.put("ACL_RMK_P", "??????????????????  = ????????????( " + LD_AMT_P + " )" );
		        						C092Map.put("ACL_AMT_P", LD_AMT_P);
		        						C092Map.put("ACL_VFROM", vfromP);
			            				C092Map.put("ACL_VTO", vtoP);
			            				C092Map.put("COM_VFSEC", vfP);
			            				C092Map.put("COM_VTSEC", vtP);
			            				
			            				crs0010_m01Mapper.modyfyTcActlistP_I(C092Map);
		        					}
	                			}
	                			
                			}
            				
            			}
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C096_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//????????????
        					C02I_96List 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
                			
                			Map C096Map = C02I_96List.get(t);
        					
        					System.out.println(C096Map.get("C096_LD_AMT"));
        					
        					if( Integer.parseInt(C096Map.get("C096_LD_AMT").toString()) > 0){
        						C096Map.put("USER_ID", inVar.get("gv_userId"));
	        					C096Map.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C096Map.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C096Map.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C096Map.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C096Map.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			C096Map.put("COM_PLDSEC", "C096");
	                			C096Map.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	                			C096Map.put("COM_VFSEC", vndf);
	                			
	                			actinfList = crs0010_m01Mapper.retrievesC096List(C096Map);
	                									
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) mapMst.get("COM_AGSEC");
	            			
	            				if("C02V".equals(mapMst.get("COM_AGSEC"))){
	            					vtD = "C2507";
	            					vtoD = (String) mapMst.get("COM_AGSEC");
	            				}else{
	            					vtD = "C2505";
	            					vtoD = (String) mapMst.get("AGT_CD");
	            				}
	
	            				C096Map.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				C096Map.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
	            				C096Map.put("COM_SVND", svnd);
	            				C096Map.put("ACL_VFROM", vfromD);
	            				C096Map.put("ACL_VTO", vtoD);
	            				C096Map.put("COM_VFSEC", vfD);
	            				C096Map.put("COM_VTSEC", vtD);            				
	            				C096Map.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
	        					
	            				LD_AMT_D = Integer.parseInt(C096Map.get("C096_LD_AMT").toString()) * 0.9;
	            				LD_AMT_P = Integer.parseInt(C096Map.get("C096_LD_AMT").toString()) * 0.82;
	            				
	            				if(!"".equals(actinfList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
	            					
	            					C096Map.put("ACL_RMK_D", "?????????????????? = ????????????( " + C096Map.get("C096_LD_AMT") + ") * ?????????(" + C096Map.get("ACI_RATEAMT_D") + " )");
		            				C096Map.put("ACL_AMT_D", LD_AMT_D);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C096Map);
	            				}
	            				
	            				C096Map.put("ACL_RMK_P", "?????????????????? = ????????????( " + C096Map.get("C096_LD_AMT") + " )");
	            				C096Map.put("ACL_AMT_P", LD_AMT_P);
	            				
	            				//???????????? ?????? ???????????? ,?????? ?????? ???????????? 
	            				if("O1007".equals(mapMst.get("ORM_ATTR"))){
	            				
	            					C02I_RelayList = crs0010_m01Mapper.retrievesC096RelayAmt(mapMst);
	            					
	            					C096Map.put("ACL_AMT_P", LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString()));
	            				}
	            				
	            				C096Map.put("ACL_VFROM", vfromP);
	            				C096Map.put("ACL_VTO", vtoP);
	            				C096Map.put("COM_VFSEC", vfP);
	            				C096Map.put("COM_VTSEC", vtP);
	            				
	            				crs0010_m01Mapper.modyfyTcActlistP_I(C096Map);
        					
        					}
            				
            			}
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C094_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//????????????
            				C02I_94List 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
                			
                			Map C094Map = C02I_94List.get(t);
            				
            				System.out.println("testc094map_AMT      :        "+C094Map.get("C094_LD_AMT_P"));
            				
            				if( Double.parseDouble(C094Map.get("C094_LD_AMT_P").toString()) >= 0){
            					C094Map.put("USER_ID", inVar.get("gv_userId"));
	            				C094Map.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C094Map.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C094Map.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C094Map.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C094Map.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			

	                			C094Map.put("COM_VFSEC", vndf);
	                			C094Map.put("COM_PLDSEC", "C094");
	                			C094Map.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(C094Map);
	
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) mapMst.get("COM_AGSEC");
	            			
	            				if("C02V".equals(mapMst.get("COM_AGSEC"))){
	            					vtD = "C2507";
	            					vtoD = (String) mapMst.get("COM_AGSEC");
	            				}else{
	            					vtD = "C2505";
	            					vtoD = (String) mapMst.get("AGT_CD");
	            				}
	            				
	            				C094Map.put("TRI_ICD", mapMst.get("ITM_CD"));
	            				
	            				//trinfList = crs0010_m01Mapper.retrievesTcTrinfList(dtlMap);
	            				
	            				//AciRateamtD = Double.parseDouble(trinfList.get(0).get("TRI_DRATE").toString());;
	            				//AciRateamtP = Double.parseDouble(trinfList.get(0).get("TRI_PRATE").toString());; 
	            				
	            				LD_AMT_D = Integer.parseInt(C094Map.get("C094_LD_AMT_D").toString());
	            				LD_AMT_P = Integer.parseInt(C094Map.get("C094_LD_AMT_P").toString());

            					C094Map.put("COM_ACD_D", "C01041");
            					C094Map.put("COM_ACD_P", "C01041");
	        					C094Map.put("COM_SVND", svnd);
	        					C094Map.put("ACL_VFROM", vfromD);
	        					C094Map.put("ACL_VTO", vtoD);
	            				C094Map.put("COM_VFSEC", vfD);
	            				C094Map.put("COM_VTSEC", vtD); 
	            				
	            				if(LD_AMT_D > 0){
	            					C094Map.put("ACL_AMT_D", LD_AMT_D);
	            					C094Map.put("ACL_RMK_D", "?????????????????? = ???????????? ( " + LD_AMT_D + " )");
           				
		            				C094Map.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C094Map);
		            				
	            				}
	            				
	            				if(LD_AMT_P >= 0){
	            					C094Map.put("ACL_AMT_P", LD_AMT_P);
	            					C094Map.put("ACL_RMK_P", "?????????????????? = ???????????? ( " + LD_AMT_P + " )");
	            					
	            					C094Map.put("ACL_VFROM", vfromP);
	            					C094Map.put("ACL_VTO", vtoP);
	            					C094Map.put("COM_VFSEC", vfP);
	            					C094Map.put("COM_VTSEC", vtP);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistP_I(C094Map);
	            				}
            				}
            				
            			}
    					
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C09A_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//?????????
            				C02I_9AList 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
                			
                			Map C09AMap = C02I_9AList.get(t);
            				
            				if( Double.parseDouble(C09AMap.get("C09A_LD_AMT_P").toString()) >= 0){
            					C09AMap.put("USER_ID", inVar.get("gv_userId"));
	            				C09AMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	                			C09AMap.put("COM_SCD", mapMst.get("COM_SCD"));
	                			C09AMap.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
	                			C09AMap.put("PLM_CDT", mapMst.get("PLM_CDT"));
	                			C09AMap.put("COM_BRAND", mapMst.get("COM_BRAND"));	
	                			

	                			C09AMap.put("COM_VFSEC", vndf);
	                			C09AMap.put("COM_PLDSEC", "C09A");
	                			C09AMap.put("PLM_NO", mapMst.get("PLM_NO"));
	                			
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(C09AMap);
	
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) mapMst.get("COM_AGSEC");
	            			
	            				if("C02V".equals(mapMst.get("COM_AGSEC"))){
	            					vtD = "C2507";
	            					vtoD = (String) mapMst.get("COM_AGSEC");
	            				}else{
	            					vtD = "C2505";
	            					vtoD = (String) mapMst.get("AGT_CD");
	            				}
	            				
	            				C09AMap.put("TRI_ICD", mapMst.get("ITM_CD"));
	            				
	            				//trinfList = crs0010_m01Mapper.retrievesTcTrinfList(dtlMap);
	            				
	            				//AciRateamtD = Double.parseDouble(trinfList.get(0).get("TRI_DRATE").toString());;
	            				//AciRateamtP = Double.parseDouble(trinfList.get(0).get("TRI_PRATE").toString());; 
	            				
	            				LD_AMT_D = Integer.parseInt(C09AMap.get("C09A_LD_AMT_D").toString());
	            				LD_AMT_P = Integer.parseInt(C09AMap.get("C09A_LD_AMT_P").toString());

            					C09AMap.put("COM_ACD_D", "C01041");
            					C09AMap.put("COM_ACD_P", "C01041");
	        					C09AMap.put("COM_SVND", svnd);
	        					C09AMap.put("ACL_VFROM", vfromD);
	        					C09AMap.put("ACL_VTO", vtoD);
	            				C09AMap.put("COM_VFSEC", vfD);
	            				C09AMap.put("COM_VTSEC", vtD); 
	            				
	            				if(LD_AMT_D > 0){
	            					C09AMap.put("ACL_AMT_D", LD_AMT_D);
	            					C09AMap.put("ACL_RMK_D", "?????????????????? = ????????? ( " + LD_AMT_D + " )");
           				
		            				C09AMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C09AMap);
		            				
	            				}
	            				
	            				if(LD_AMT_P >= 0){
	            					C09AMap.put("ACL_AMT_P", LD_AMT_P);
	            					C09AMap.put("ACL_RMK_P", "?????????????????? = ????????? ( " + LD_AMT_P + " )");
	            					
	            					C09AMap.put("ACL_VFROM", vfromP);
	            					C09AMap.put("ACL_VTO", vtoP);
	            					C09AMap.put("COM_VFSEC", vfP);
	            					C09AMap.put("COM_VTSEC", vtP);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistP_I(C09AMap);
	            				}
            					
            				}
            				
            			}
        				
            		}
            	}
            	//??????????????????
            	else{

                	dtlList = crs0010_m01Mapper.retrieveTcPlandtlPldsecSum(mapMst);
                	
                	for(int x = 0; x < dtlList.size() ; x++ ){
                		Map dtlMap = dtlList.get(x);
                		
                		dtlMap.put("USER_ID", inVar.get("gv_userId"));
                		dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
                		dtlMap.put("COM_SCD", mapMst.get("COM_SCD"));
                		dtlMap.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
                		dtlMap.put("PLM_CDT", mapMst.get("PLM_CDT"));
                		dtlMap.put("COM_BRAND", mapMst.get("COM_BRAND"));
            			
                		pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C090_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//????????????
	            			if("C090".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				vfP    = vndf; 
	            				vfromP = svnd;       //???????????????
	            				vtP    = "C2501";	 //??????to?????? : ?????????
	            				vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD  = vndf;      // 
	            				vfromD = svnd;       //???????????????
	            				vtD    = "C2505";     //??????to?????? : ?????????
	            				vtoD   = (String) mapMst.get("COM_AGSEC");    //???????????????
	            			
	            				dtlMap.put("COM_VFSEC", vndf);
	            				dtlMap.put("COM_VTSEC", vndt);
	            				dtlMap.put("ACL_AMT", dtlMap.get("TOTAL_SUM_PLD_AMT"));
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C090(dtlMap);
	            				
	            				dtlMap.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				dtlMap.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
	            				dtlMap.put("COM_SVND", svnd);
	            				dtlMap.put("ACL_VFROM", vfromD);
	            				dtlMap.put("ACL_VTO", vtoD);
	            				dtlMap.put("COM_VFSEC", vfD);
	            				dtlMap.put("COM_VTSEC", vtD);
	            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
	            				
	            				AciRateamtD = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_D").toString());;

	            				AciRateamtP = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_P").toString());;
	            				
	            				
	            				
	            				if("C02I".equals(mapMst.get("COM_AGSEC"))){
	            					if("0.00000".equals(actinfList.get(0).get("ACI_RATEAMT_D").toString())){
	            						dtlMap.put("ACL_AMT_D", 0);
	            						dtlMap.put("ACL_RMK_D", "?????? ?????????????????? = " + 0 );
	            					}else{
	            						dtlMap.put("ACL_AMT_D", amt);
	            						dtlMap.put("ACL_RMK_D", "?????? ?????????????????? = " + amt );
	            					}
	            					
	            					dtlMap.put("ACL_AMT_P", amt);
	            					dtlMap.put("ACL_RMK_P", "?????? ?????????????????? = " + amt );
	            				}else{
	            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
	            					dtlMap.put("ACL_RMK_D", "?????? ?????????????????? = " + amt + " * " + AciRateamtD );
	            					
	            					if("C02F".equals(mapMst.get("COM_AGSEC")) || "C02P".equals(mapMst.get("COM_AGSEC")) ){
	            						//?????? Y
	            						if("Y".equals(ormGgubun)){
	            							dtlMap.put("ACL_AMT_P", amt );
	            							dtlMap.put("ACL_RMK_P", "?????? ?????????????????? = " + amt  );
	            						}
	            						//??????  N
	            						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
	            							
	            							if("C02F".equals(mapMst.get("COM_AGSEC"))){
	            								dtlMap.put("ACL_AMT_P", amt * 1.39);
		            							dtlMap.put("ACL_RMK_P", "?????? ?????????????????? = " + amt + " * 1.39" );	
	            							
	            							}else if("C02P".equals(mapMst.get("COM_AGSEC"))){
	            								dtlMap.put("ACL_AMT_P", amt * 1.37);
		            							dtlMap.put("ACL_RMK_P", "?????? ?????????????????? = " + amt + " * 1.37" );
	            							}
	            						}
	            					}
	            					
	            				}
	            				if(!"0.00000".equals(actinfList.get(0).get("ACI_RATEAMT_D").toString())){
	            					crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
	            				}
	            				
	            				dtlMap.put("ACL_VFROM", vfromP);
	            				dtlMap.put("ACL_VTO", vtoP);
	            				dtlMap.put("COM_VFSEC", vfP);
	            				dtlMap.put("COM_VTSEC", vtP);
	            				
	            				
	            				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
	            				
	            			}
            				
            			}

                		pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C092_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//??????????????????
	            			if("C092".equals(dtlMap.get("COM_PLDSEC"))){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				//20180724 ??????????????? ??? ???????????? ??????????????? ??????????????? ?????? ??????
	            				amt_mnf = Integer.parseInt(dtlMap.get("TOTAL_SUM_MNF_AMT").toString());
	            				
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C092(dtlMap);
	            				
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) dtlMap.get("COM_AGSEC");
	            				
	            				if("C02V".equals(dtlMap.get("COM_AGSEC"))){
		        					vtD = "C2507";
		        					vtoD = (String) dtlMap.get("COM_AGSEC");
		        				}else{
		        					vtD = "C2505";
		        					vtoD = (String) mapMst.get("AGT_CD");
		        				}
	            				
	            				dtlMap.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				dtlMap.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
	            				dtlMap.put("COM_SVND", svnd);
	            				dtlMap.put("ACL_VFROM", vfromD);
	            				dtlMap.put("ACL_VTO", vtoD);
	            				dtlMap.put("COM_VFSEC", vfD);
	            				dtlMap.put("COM_VTSEC", vtD);            				
	            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
	            				
	            				AciRateamtD = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_D").toString());;
	            				AciRateamtP = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_P").toString());;           				
	            				
	            				if("C02F".equals(mapMst.get("COM_AGSEC")) || "C02P".equals(mapMst.get("COM_AGSEC")) ){
		            			
	            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
	            							dtlMap.put("ACL_AMT_D", amt_mnf * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt_mnf + " * " + AciRateamtD );
			            					/*dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * " + AciRateamtD );*/
		            				}else{
		            					dtlMap.put("ACL_AMT_D", AciRateamtD);
		            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " +  AciRateamtD );
		            				}
	            					
	            					//?????? Y
	        						if("Y".equals(ormGgubun)){
	        							dtlMap.put("ACL_AMT_P", amt );
	        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt);
	        						}
	        						//?????? N
	        						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
	        							
	        							if("C02F".equals(mapMst.get("COM_AGSEC"))){
	        								dtlMap.put("ACL_AMT_P", amt * 1.39);
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 1.39 " );
	        								
	        							}else if("C02P".equals(mapMst.get("COM_AGSEC"))){
	        								dtlMap.put("ACL_AMT_P", amt * 1.37);
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 1.37 " );
	        							}
	                				}
	            				}
	            				else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
	            					dtlMap.put("ACL_AMT_D", amt * 0.9);
	            					dtlMap.put("ACL_AMT_P", amt * 0.82);
	            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * 0.9");
	            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 0.82");
	            					
	            				}
	            				else{
	            					
	            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
		            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
		            					dtlMap.put("ACL_AMT_P", amt * AciRateamtP);
		            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * " + AciRateamtD );
		            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * " + AciRateamtP );
	            					}else{
		            					dtlMap.put("ACL_AMT_D", AciRateamtD);
		            					dtlMap.put("ACL_AMT_P", AciRateamtP);
		            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " +  AciRateamtD );
		            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " +  AciRateamtP );
	            					}
	            					
	            				}
	            				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
	            				
	            				dtlMap.put("ACL_VFROM", vfromP);
	            				dtlMap.put("ACL_VTO", vtoP);
	            				dtlMap.put("COM_VFSEC", vfP);
	            				dtlMap.put("COM_VTSEC", vtP);
	            				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
	            			}
            				
            			}	                			
            			
                		pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C096_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//??????????????????
	            			if("C096".equals(dtlMap.get("COM_PLDSEC"))){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //???????????????
	    						vtP    ="C2501";			//??????to?????? : ?????????
	    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
	            				
	            				vfD = vndf;
	            				vfromD = (String) dtlMap.get("COM_AGSEC");
	            			
	            				if("C02V".equals(dtlMap.get("COM_AGSEC"))){
	            					vtD = "C2507";
	            					vtoD = (String) dtlMap.get("COM_AGSEC");
	            				}else{
	            					vtD = "C2505";
	            					vtoD = (String) mapMst.get("AGT_CD");
	            				}
	
	            				dtlMap.put("COM_ACD_D", actinfList.get(0).get("COM_ACD_D"));
	            				dtlMap.put("COM_SVND", svnd);
	            				dtlMap.put("ACL_VFROM", vfromD);
	            				dtlMap.put("ACL_VTO", vtoD);
	            				dtlMap.put("COM_VFSEC", vfD);
	            				dtlMap.put("COM_VTSEC", vtD);            				
	            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
	            				
	            				acdP = (String) actinfList.get(0).get("COM_ACD_P");
	            				acdD = (String) actinfList.get(0).get("COM_ACD_D");
	            				
	            				System.out.println("acdP  :    "+ acdP + "   //   acdD   :   " + acdD );
	            				
	            				if(!"".equals(acdD)){

		            				AciRateamtD = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_D").toString());;         				
		            				
		            				if("C02F".equals(mapMst.get("COM_AGSEC")) || "C02P".equals(mapMst.get("COM_AGSEC")) ){
			            			
		            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
			            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * " + AciRateamtD );
			            				}else{
			            					dtlMap.put("ACL_AMT_D", AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " +  AciRateamtD );
			            				}
		            					
			            			}
			            			else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
		            					dtlMap.put("ACL_AMT_D", amt * 0.9);
		            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * 0.9");
		            				}
		            				else{
		            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
			            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + amt + " * " + AciRateamtD );
		            					}else{
			            					dtlMap.put("ACL_AMT_D", AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " +  AciRateamtD );
		            					}	
			            			}
		            				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
	            					
	            				}
	            				
	            				if(!"".equals(acdP)){
	            					dtlMap.put("COM_ACD_P", actinfList.get(0).get("COM_ACD_P"));
		            				dtlMap.put("COM_SVND", svnd);
		            				dtlMap.put("ACL_VFROM", vfromD);
		            				dtlMap.put("ACL_VTO", vtoD);
		            				dtlMap.put("COM_VFSEC", vfD);
		            				dtlMap.put("COM_VTSEC", vtD);            				
		            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				AciRateamtP = Double.parseDouble(actinfList.get(0).get("ACI_RATEAMT_P").toString());;           				
		            				
		            				if("C02F".equals(mapMst.get("COM_AGSEC")) ){
		            					
		            					//?????? Y
		        						if("Y".equals(ormGgubun)){
		        							dtlMap.put("ACL_AMT_P", amt );
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt  );
		                				}
		        						//?????? N
		        						else if("N".equals(ormGgubun)|| "".equals(ormGgubun) ){
		        							dtlMap.put("ACL_AMT_P", amt * 1.39);
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 1.39");
		                				}
		            					
		            				}	
		            				else if("C02P".equals(mapMst.get("COM_AGSEC")) ){
		            					
		            					//?????? Y
		        						if("Y".equals(ormGgubun)){
		        							dtlMap.put("ACL_AMT_P", amt );
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt  );
		                				}
		        						//?????? N
		        						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
		        							dtlMap.put("ACL_AMT_P", amt * 1.37);
		        							dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 1.37");
		                				}					            					
			            			}
			            			else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
		            					dtlMap.put("ACL_AMT_P", amt * 0.82);
		            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * 0.82");
		            				}
		            				else{
		            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_P"))){
			            					dtlMap.put("ACL_AMT_P", amt * AciRateamtP);
			            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + amt + " * " + AciRateamtP );
		            					}else{
			            					dtlMap.put("ACL_AMT_P", AciRateamtP);
			            					dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " +  AciRateamtP );
		            					}	
			            			}
		            				
		            				
		            				dtlMap.put("ACL_VFROM", vfromP);
		            				dtlMap.put("ACL_VTO", vtoP);
		            				dtlMap.put("COM_VFSEC", vfP);
		            				dtlMap.put("COM_VTSEC", vtP);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
	            				}
	            			}	
            			}
            			
                		pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C094_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//???????????? ??????
	            			if("C094".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				
	            				AciRateamtD = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_D").toString());
	            	            AciRateamtP = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_P").toString());
	          				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				if(actinfList.size() > 0){
	            				

		            				vfP    = vndf; 			
		    						vfromP = svnd ;       //???????????????
		    						vtP    ="C2501";			//??????to?????? : ?????????
		    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
		            				
		            				vfD = vndf;
		            				vfromD = (String) dtlMap.get("COM_AGSEC");
		            			
		            				if("C02V".equals(dtlMap.get("COM_AGSEC"))){
		            					vtD = "C2507";
		            					vtoD = (String) dtlMap.get("COM_AGSEC");
		            				}else{
		            					vtD = "C2505";
		            					vtoD = (String) mapMst.get("AGT_CD");
		            				}
		            				
		            				dtlMap.put("TRI_ICD", dtlMap.get("ITM_CD"));
		            				
		            				//trinfList = crs0010_m01Mapper.retrievesTcTrinfList(dtlMap);
		            				
		            				//AciRateamtD = Double.parseDouble(trinfList.get(0).get("TRI_DRATE").toString());;
		            				//AciRateamtP = Double.parseDouble(trinfList.get(0).get("TRI_PRATE").toString());; 
		            				
		            				dtlMap.put("ACL_AMT_D", AciRateamtD);
		        					dtlMap.put("ACL_AMT_P", AciRateamtP);
		            				
		        					if("C094".equals(dtlMap.get("COM_PLDSEC"))){
		        						dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + AciRateamtD);
		        						dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + AciRateamtP );
		        					}
		        					//else if("C09A".equals(dtlMap.get("COM_PLDSEC"))){
		        					//	dtlMap.put("ACL_RMK_D", "????????? ?????????????????? = " + AciRateamtD );
		        					//	dtlMap.put("ACL_RMK_P", "????????? ?????????????????? = " + AciRateamtP );
		        					//}
		        					
		        					dtlMap.put("COM_ACD_D", "C01041");
		        					dtlMap.put("COM_ACD_P", "C01041");
		            				dtlMap.put("COM_SVND", svnd);
		            				dtlMap.put("ACL_VFROM", vfromD);
		            				dtlMap.put("ACL_VTO", vtoD);
		            				dtlMap.put("COM_VFSEC", vfD);
		            				dtlMap.put("COM_VTSEC", vtD);            				
		            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
		            				
		            				dtlMap.put("ACL_VFROM", vfromP);
		            				dtlMap.put("ACL_VTO", vtoP);
		            				dtlMap.put("COM_VFSEC", vfP);
		            				dtlMap.put("COM_VTSEC", vtP);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
	            				}	
	            			}	
	                	}
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C09A_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//????????? ??????
	            			if("C09A".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				
	            				AciRateamtD = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_D").toString());
	            	            AciRateamtP = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_P").toString());
	          				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				if(actinfList.size() > 0){
	            				

		            				vfP    = vndf; 			
		    						vfromP = svnd ;       //???????????????
		    						vtP    ="C2501";			//??????to?????? : ?????????
		    						vtoP   = (String) mapMst.get("STI_CD");     //????????? 
		            				
		            				vfD = vndf;
		            				vfromD = (String) dtlMap.get("COM_AGSEC");
		            			
		            				if("C02V".equals(dtlMap.get("COM_AGSEC"))){
		            					vtD = "C2507";
		            					vtoD = (String) dtlMap.get("COM_AGSEC");
		            				}else{
		            					vtD = "C2505";
		            					vtoD = (String) mapMst.get("AGT_CD");
		            				}
		            				
		            				dtlMap.put("TRI_ICD", dtlMap.get("ITM_CD"));
		            				
		            				//trinfList = crs0010_m01Mapper.retrievesTcTrinfList(dtlMap);
		            				
		            				//AciRateamtD = Double.parseDouble(trinfList.get(0).get("TRI_DRATE").toString());;
		            				//AciRateamtP = Double.parseDouble(trinfList.get(0).get("TRI_PRATE").toString());; 
		            				
		            				dtlMap.put("ACL_AMT_D", AciRateamtD);
		        					dtlMap.put("ACL_AMT_P", AciRateamtP);
		            				
		        					if("C094".equals(dtlMap.get("COM_PLDSEC"))){
		        						dtlMap.put("ACL_RMK_D", "???????????? ?????????????????? = " + AciRateamtD);
		        						dtlMap.put("ACL_RMK_P", "???????????? ?????????????????? = " + AciRateamtP );
		        					}
		        					else if("C09A".equals(dtlMap.get("COM_PLDSEC"))){
		        						dtlMap.put("ACL_RMK_D", "????????? ?????????????????? = " + AciRateamtD );
		        						dtlMap.put("ACL_RMK_P", "????????? ?????????????????? = " + AciRateamtP );
		        					}
		        					
		        					dtlMap.put("COM_ACD_D", "C01041");
		        					dtlMap.put("COM_ACD_P", "C01041");
		            				dtlMap.put("COM_SVND", svnd);
		            				dtlMap.put("ACL_VFROM", vfromD);
		            				dtlMap.put("ACL_VTO", vtoD);
		            				dtlMap.put("COM_VFSEC", vfD);
		            				dtlMap.put("COM_VTSEC", vtD);            				
		            				dtlMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
		            				
		            				dtlMap.put("ACL_VFROM", vfromP);
		            				dtlMap.put("ACL_VTO", vtoP);
		            				dtlMap.put("COM_VFSEC", vfP);
		            				dtlMap.put("COM_VTSEC", vtP);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
	            				}	
	            			}	
	                	}
            		}
            			
            	}
            	
            	pldCnt = Integer.parseInt(pldsecCountList.get(0).get("ETC_COUNT").toString());
    			
    			if(pldCnt > 0){
    				//????????????????????????
                	dtlList = crs0010_m01Mapper.retrieveTcPlandtlEtcSum(mapMst);
                	
                	for(int j = 0 ; j < dtlList.size() ; j++){
                		
                		Map dtlMap = dtlList.get(j);
            			
                		dtlMap.put("USER_ID", inVar.get("gv_userId"));
                		dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
                		dtlMap.put("COM_SCD", mapMst.get("COM_SCD"));
                		dtlMap.put("COM_CTSEC", mapMst.get("COM_CTSEC"));
                		dtlMap.put("PLM_CDT", mapMst.get("PLM_CDT"));
                		dtlMap.put("COM_BRAND", mapMst.get("COM_BRAND"));
                		
                		vfP    = vndf; 			
        				vfromP = svnd;        //???????????????
        				vtP    = "C2501";			//??????to?????? : ?????????
        				vtoP   = (String) mapMst.get("STI_CD");     //?????????  
        		
        				vfD    = vndf;
        				
                		if("C2401".equals(dtlMap.get("COM_VFSEC"))){
                			vfromD = (String) mapMst.get("STI_CD");
                		}
                		else if("C2402".equals(dtlMap.get("COM_VFSEC"))){
                			vfromD = svnd;
                		}
                		else if("C2403".equals(dtlMap.get("COM_VFSEC")) || "C2404".equals(dtlMap.get("COM_VFSEC"))){
                			vfromD = (String) dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
                		}
                		else if("C2405".equals(dtlMap.get("COM_VFSEC"))){
                			vfromD = (String) mapMst.get("AGT_CD");
                		}
                		
                		vtD	  = "C2505";
                		
                		if("C2501".equals(dtlMap.get("COM_VTSEC"))){
                			vtoD = (String) mapMst.get("STI_CD");
                		}
                		else if("C2502".equals(dtlMap.get("COM_VTSEC"))){
                			vtoD = svnd;
                		}
                		else if("C2503".equals(dtlMap.get("COM_VTSEC")) || "C2504".equals(dtlMap.get("COM_VTSEC"))){
                			vtoD = (String) dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
                		}
                		else if("C2505".equals(dtlMap.get("COM_VTSEC"))){
                			vtoD = (String) mapMst.get("AGT_CD");
                		}
                		else if("C2507".equals(dtlMap.get("COM_VTSEC"))){
                			vtoD = (String) dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
                		}
                		
                		if("C097".equals(dtlMap.get("COM_PLDSEC"))){
                			
                			amt = 0;
                		}else{
                			
                			amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_CFAMT_P").toString());
                		}
                		
                		dtlMap.put("ACL_AMT_P", amt);
                		dtlMap.put("ACL_RMK_P", "?????????????????? ?????????????????? = " + amt );
						
                		actinfList = crs0010_m01Mapper.retrievesTcActinf_0000(dtlMap);
                		
                		
                		
                		
                		dtlMap.put("COM_ACD_D", dtlMap.get("COM_ACD"));
                		dtlMap.put("COM_ACD_P", dtlMap.get("COM_ACD"));
        				//dtlMap.put("COM_SVND", dtlMap.get("COM_SVND"));
        				dtlMap.put("ACL_VFROM", vfromP);
        				dtlMap.put("ACL_VTO", vtoP);
        				dtlMap.put("COM_VFSEC", dtlMap.get("COM_VFSEC_P"));
        				dtlMap.put("COM_VTSEC", dtlMap.get("COM_VTSEC_P"));            				
        				dtlMap.put("COM_CESEC", "C292");
        				
        				crs0010_m01Mapper.modyfyTcActlistP_I(dtlMap);
        				
        				dtlMap.put("ACL_VFROM", vfromD);
        				dtlMap.put("ACL_VTO", vtoD);
        				
        				if(actinfList.size() > 0 ){
        					
        					amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_CFAMT_D").toString());
        					double rate = Double.parseDouble(actinfList.get(0).get("ACL_RATEAMT_D").toString()) ;
        					dtlMap.put("ACL_AMT_D", amt * rate);
        					dtlMap.put("ACL_RMK_D", "?????????????????? ?????????????????? = " + amt + " * ????????? " + actinfList.get(0).get("ACL_RATEAMT_D") + " ");
        					dtlMap.put("COM_VFSEC", actinfList.get(0).get("COM_VFSEC_D"));
	        				dtlMap.put("COM_VTSEC", actinfList.get(0).get("COM_VTSEC_D"));	
	        				
	        				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
        				}	
                	}	
    			}
    			
    			mapMst.put("COM_PLMFG_AFT", "C104");
    			crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);
        	}	
        	
        	//?????????????????? ?????? ?????? 
        	else if("C104".equals(mapMst.get("COM_PLMFG"))){
        		
        		crs0010_m01Mapper.modyfyTcActlist_D(mapMst);
        		mapMst.put("COM_PLMFG_AFT", "C103");
        		crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);
        	}

        	/*//????????? ?????? ??????????????? ?????? // ?????????????????? ????????? ?????? 
        	if("C103".equals(mapMst.get("COM_PLMFG"))){
        		mapMst.put("COM_PLMFG_AFT", "C104");
        	}else if("C104".equals(mapMst.get("COM_PLMFG"))){
        		mapMst.put("COM_PLMFG_AFT", "C103");
        	}
        	
        	crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);*/
        	
        	//????????? ??????, 
        	if("C02I".equals(mapMst.get("COM_AGSEC"))){
        		
    			HashMap<String, Object> params = new HashMap<String, Object>();
    			params.put("plm_no", ds_tcPlanmstList.plm_no);
    			params.put("sti_cd", ds_tcPlanmstList.sti_cd);
    			params.put("com_scd", ds_tcPlanmstList.com_scd);
    			params.put("plm_cdt", ds_tcPlanmstList.plm_cdt);
    			params.put("com_brand", ds_tcPlanmstList.com_brand);        			
    			
    			//????????? ????????????
        		if (ds_tcPlanmstList.orm_nm.indexOf("(?????????)") == 0) {
        			
        			res = erpsigongasMapper.insertSigongWallFix(params);
        			if (res < 1) { 
            			txManager.rollback(status);
        				response.setResultCode("5001");
        				response.setResultMessage("insertSigongWallFix ?????? [" + res + "]");
        				return response;
            		}
        			
        			res = erpsigongasMapper.insertSigongWallFixAcc(params);
        			if (res < 1) { 
            			txManager.rollback(status);
        				response.setResultCode("5001");
        				response.setResultMessage("insertSigongWallFixAcc ?????? [" + res + "]");
        				return response;
            		}
        		}

        		//???????????? ????????????
        		String over_time = "", rem_ftm = "";
        		dataResult = erpsigongasMapper.selectSigongWorkTimeCheck(params);
        		if (dataResult != null) {
        			over_time = dataResult.getData1();
        		}
        		
    			params.put("rem_dt", ds_tcPlanmstList.rem_dt);
    			params.put("rem_seq", ds_tcPlanmstList.rem_seq);

    			dataResult = erpsigongasMapper.selectSigongArrivalTimeCheck(params);
        		if (dataResult != null) {
        			rem_ftm = dataResult.getData1();
        		}
        		
        		//??????????????? 18:00 ????????????, ????????????????????? 18:00 ????????? ??????,
    			if ("Y".equals(over_time) && !"".equals(rem_ftm)) {				
    				res = erpsigongasMapper.insertSigonWorkTimeOver(params);
    				if (res < 1) { 
    	    			txManager.rollback(status);
    					response.setResultCode("5001");
    					response.setResultMessage("insertSigonWorkTimeOver ?????? [" + res + "]");
    					return response;
    	    		}
    				
    				res = erpsigongasMapper.insertSigonWorkTimeOverAcc(params);
    				if (res < 1) { 
    	    			txManager.rollback(status);
    					response.setResultCode("5001");
    					response.setResultMessage("insertSigonWorkTimeOverAcc ?????? [" + res + "]");
    					return response;
    	    		}    				
    			}
        	}        	
        	
        	//??????????????????
        	HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddSigongEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime ?????? [" + res + "]");
				return response;
    		}        	
        	
        	mstUpdChk++;
        	System.out.println("???????????? ???????????? ?????? update ?????? [ " + mstUpdChk + "] ");
	        
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
	public SigongResultResponse erp_SiGongResultSave(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		SigongResultResponse response = new SigongResultResponse();
		
		try {			
			int res = 0;
			DataResult dataResult = new DataResult();
		
			String as_plm_no = (String) param.get("plm_no");
			String as_usr_id = (String) param.get("usr_id");
			String mob_std = (String) param.get("mob_std");
			String mob_remark = (String) param.get("mob_remark");
			HashMap<String,Object> inVar = new HashMap<String, Object>();

			//?????? - A
			//?????? - ????????? : B
			//?????? - ?????? : C
			//?????? - AS?????? : D
			
			//IF ??????????????? AS ??? ?????? + ?????? ????????? ??????
			if(mob_std.equals("A") || mob_std.equals("D")) {

				dataResult = schedulemainlistMapper.getNowTime();
				
				if (dataResult == null) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("getNowTime ??????");
					return response;
				}			
				
				int nowTime = dataResult.getValue1();	
				
				if (nowTime >= 18) {
					HashMap<String,Object> params = new HashMap<String, Object>();
					params.put("plm_no", as_plm_no);
					params.put("user_id", as_usr_id);
					res = schedulemainlistMapper.insertNightTimeJungsan(params);
					if (res < 1) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("insertNightTimeJungsan ?????? [" + res + "]");
						return response;
					}				
				}		
			}

			
			ds_tcPlanmstList ds_tcPlanmstList = crs0010_m01Mapper.retrievesTcPlanmstList(param);
			
//	        //??????
//	        inVar.put("plm_no", as_plm_no);
//	        inVar.put("com_pldsec", "'C090'");            
//	        ds_tcPlandtlList1 ds_out1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //????????????,????????????
//	        inVar.put("com_pldsec", "'C092','C096','C095'");
//	        ds_tcPlandtlList1 ds_out2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //????????????,?????????            
//	        inVar.put("com_pldsec", "'C094','C09A'");
//	        ds_tcPlandtlList2 ds_out3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);
//	        
//	        //??????????????????            
//	        ds_tcPlandtlList3 ds_out4 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);
	        	        
	        //????????? ??????
	    	if ("Y".equals(ds_tcPlanmstList.med_yn)) {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ?????? ????????????.");
				return response;
	        }
	    	
			if("C104".equals(ds_tcPlanmstList.com_plmfg)){
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ??????????????? ?????? ?????? ??????/?????? ??? ??? ????????????.");
				return response;
			}

			if (!"C13W".equals(ds_tcPlanmstList.com_rmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ???????????? ?????? ?????? ????????? ??? ????????????.");
				return response;
			}	        
	        
            int mstUpdChk = 0;
            int dtlUpdChk = 0;
            
            Map mapMst = new HashMap<String, String>();
            mapMst.put("plm_no", as_plm_no);
            mapMst.put("usr_id", as_usr_id);
            mapMst.put("mob_remark", mob_remark);
            
        	// ?????????????????? ????????? ??????        	
            mapMst.put("com_rdsec_aft", "C13Y");
    		mapMst.put("gubun", "Y");    		
    		
    		res = crs0010_m01Mapper.modyfyTcplanmstAllComplete(mapMst);
    		if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modyfyTcplanmstAllComplete ?????? [" + res + "]");
				return response;
			}
    		
            List<Map> chkList = crs0010_m01Mapper.retrievesTcPlandtlCompleChk(mapMst);            
            for(int x = 0 ; x < chkList.size() ; x++){
            	Map chkMap = chkList.get(x);
            	
            	int nCount = Integer.parseInt(chkMap.get("C13N_COUNT").toString());
            	int wCount = Integer.parseInt(chkMap.get("C13W_COUNT").toString());
            	
            	//??????????????? 0 ?????? ??????
            	if (wCount > 0){
            		mapMst.put("com_rmfg_aft", "C13W");
            		mapMst.put("com_plmfg_aft", "C102");
            	}
            	//???????????? 0 ?????? ??????
            	else if(nCount > 0 ){
            		mapMst.put("com_rmfg_aft", "C13N");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            	//?????? ,???????????? ?????? 0?????? ????????? ?????? ?????? ?????? 
            	else{
            		mapMst.put("com_rmfg_aft", "C13Y");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            }
            
            res = crs0010_m01Mapper.modyfyTcPlanMstComple(mapMst);
            System.out.println("???????????? ?????? ???????????? ?????? [ "+ res+" ] ");
            
            
            //?????? ???????????? ?????? ????????? ?????? ????????? ???????????? ??????
            //2021-02-02 ?????????
            if(mob_std.equals("A")) {

    	    	String templateCode = "";
    	    	String senderkey = "";
    	    	String title = "";
    	    	String subject = "";
    	    	String from_no = "";
    	    	String com_brand = "";
    	    	String sofa_yn = "";

    	    	
    	    	System.out.println(as_plm_no);
    	    	
                Map mapMst2 = new HashMap<String, String>();
                mapMst2.put("plm_no", as_plm_no);
                
				dataResult = schedulemainlistMapper.getSigongBrand(mapMst2);

				if (dataResult == null) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("getSigongBrand ??????");
					return response;
				}	
				
				com_brand = dataResult.getData1() ;
    	    	
				//???????????? ?????? ???????????? ??? ????????? ?????????????????? ?????? ??????????????? ??????
				if("T60F01".equals(com_brand)) {
					
					String agt_hpno = "";
					String sms_message = "";
					
					dataResult = schedulemainlistMapper.getSigongAgtCd(mapMst2);
					
					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("getSigongAgtCd ??????");
						return response;
					}	
					
					String orm_nm = "";
					String sti_nm = "";
					
					agt_hpno = dataResult.getData1();
					orm_nm = dataResult.getData2();
					sti_nm = dataResult.getData3();
					
	    	    	JSONObject obj = new JSONObject();
	    	    	JSONArray jArray = new JSONArray(); //????????? ????????????
	    	    	 
	    	    	JSONObject api_token = new JSONObject();
	    	    	JSONObject sendList = new JSONObject();	    	 
	    	    	JSONObject sObject = new JSONObject();//?????? ?????? ????????? json
	    	    		        		
	        		title = "????????? ?????? ??????";
	        		subject = "????????? ?????? ??????";
	        		    	    	
	    	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
	 
	    	    	String orm_hdphone = agt_hpno;
	    	    	String sti_cd = as_usr_id;
	    	    	String orm_no = as_plm_no;
	    	    	
					sms_message = orm_nm+" ????????? ???????????? ?????? ???????????????. \r\n"+ "?????? ????????? : " + sti_nm;

					from_no = "1588-1244";
					
	    	       	for (int i = 0; i < 1; i++)//??????
	    	       	{
	    		        	 
	    	       		sObject.put("sendDiv", "SMS" );
	    		        sObject.put("title", title);
	    		        sObject.put("subject", subject );		  
	    		        sObject.put("message",sms_message);
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
	    		        sObject.put("msgType", "TI4Z" );	        	
	    		        sObject.put("comBrd", "T60F01" );
	    		        
	    		        jArray.add(sObject);
	    	       	 }        	 
	            		            			
	            	sendList.put("list" ,jArray);  
	            	 
//	            	RestCall("http://localhost:8082/intgmsg.do",sendList);

//	            	BaseResponse kakao_res = RestCall("http://erp-api.fursys.com/intgmsg.do",sendList);	
//					RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);		            	
	            	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
	            	
	            	if (!"200".equals(kakao_res.getResultCode())) {
	    				txManager.rollback(status);
	    				response.setResultCode("5001");
	    				response.setResultMessage("????????????????????? ??????  [" + kakao_res.getResultMessage() + "]");
	    				return response;
	    			}					
						
				}

				if("T60I01".equals(com_brand)) { //??????????????? 

					dataResult = schedulemainlistMapper.getSigongSofaYn(mapMst2);

					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("getSigongSofaYn ??????");
						return response;
					}						
					
					sofa_yn = dataResult.getData1() ;
					
					if("Y".equals(sofa_yn)) { //?????? - ?????? ?????? ????????? ?????? 

		    	    	JSONObject obj = new JSONObject();
		    	    	JSONArray jArray = new JSONArray(); //????????? ????????????
		    	    	 
		    	    	JSONObject api_token = new JSONObject();
		    	    	JSONObject sendList = new JSONObject();	    	 
		    	    	JSONObject sObject = new JSONObject();//?????? ?????? ????????? json
		    	    		        		
		        		title = "?????? ?????? ?????? ??????";
		        		subject = "?????? ?????? ?????? ??????";
		        		
			    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
			    		from_no = "1577-5670";
			    		templateCode = "iloomsofasigongmessage02";
		    	    	
		    	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
		 
		    	    	String orm_nm = "";
		    	    	String sti_nm = "???????????????";
		    	    	String orm_hdphone = "";
		    	    	String sti_cd = as_usr_id;
		    	    	String orm_no = as_plm_no;
		    	    	String biztalkmessage = "";
		    	    	String chatbot_url = "http://pf.kakao.com/_xnlbxnl/chat";
		    	    	
						dataResult = schedulemainlistMapper.getSigongOrmNm(mapMst2);

						if (dataResult == null) {
							txManager.rollback(status);
							response.setResultCode("5001");
							response.setResultMessage("getSigongOrmNm ??????");
							return response;
						}	
						
						orm_nm = dataResult.getData1() ;
						
		    	    	biztalkmessage = orm_nm+" ?????????!\r\n" +
						    	    	"?????? ????????? ?????????????????? ???????????????.\r\n"+
						    	    	"??????????????? ???????????? ????????? ?????? ?????? ??????????????????,\r\n"+
						    	    	"?????? ??? ?????????(?????? ?????? ?????????)??? ????????? ??????????????? ?????? ?????????\r\n"+
						    	    	"?????? ?????? ??? ?????? ??????????????? ????????? ?????????????????????.\r\n\r\n"+
						    	    	"?????? ?????? ???, ???????????? ????????? ????????? ????????????????????? ??????????????????\r\n"+
						    	    	"????????? ?????? ?????? ????????????.\r\n\r\n"+
						    	    	"???????????? ???????????? ??????????????? ??????????????????.\r\n"+
						    	    	"??? ???????????? : "+chatbot_url+"\r\n"+
						    	    	"(???????????? ?????? 09:30-17:30, ?????? ??? ????????? ??????)";


								
						dataResult = schedulemainlistMapper.getSigongOrmHdphone(mapMst2);

						if (dataResult == null) {
							txManager.rollback(status);
							response.setResultCode("5001");
							response.setResultMessage("getSigongOrmHdphone ??????");
							return response;
						}
						
						orm_hdphone = dataResult.getData1() ;
						
		    	       	for (int i = 0; i < 1; i++)//??????
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
		    		        sObject.put("msgType", "TI4Z" );		        	 
		    		        sObject.put("senderKey", senderkey);
		    		        sObject.put("templateCode", templateCode );
		    		        sObject.put("bizTalkMessage", biztalkmessage );
		    		        sObject.put("comBrd", "T60I01" );
		    		        
		    		        jArray.add(sObject);
		    	       	 }        	 
		            		            			
		            	sendList.put("list" ,jArray);  
		            	 
//		            	RestCall("http://localhost:8082/intgmsg.do",sendList);
//		            	BaseResponse kakao_res = RestCall("http://erp-api.fursys.com/intgmsg.do",sendList);		
		            	
		            	BaseResponse kakao_res = RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg",sendList);	
		            	
		            	if (!"200".equals(kakao_res.getResultCode())) {
		    				txManager.rollback(status);
		    				response.setResultCode("5001");
		    				response.setResultMessage("????????????????????? ??????  [" + kakao_res.getResultMessage() + "]");
		    				return response;
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
                System.out.println("?????? ??????");
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
        	
            System.out.println("?????? ??????");
            res.setResultCode("200");
            res.setResultMessage("");
        	
        	return res;
        }
    	
    	return res;
    }		
	@Override
	public ArrayList<ds_tabList1> selectAsDtlList(HashMap<String, Object> param) {
		ArrayList<ds_tabList1> allItems = erppraacctlistsaveMapper.selectResDtlList(param);
		return allItems;
	}

	@Override
	public AsResultResponse erp_AsResultSaveRe(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		AsResultResponse response = new AsResultResponse();
		DataResult dataResult = new DataResult();
		boolean	isDeBug = true;
		
		try {
			String as_plm_no = (String) param.get("plm_no");
			String as_rptq_dt = (String) param.get("rptq_dt");
			String as_com_unsec = (String) param.get("com_unsec");
			String as_user_id = (String) param.get("usr_id");
			String as_remark = (String) param.get("remark");
			
			APM0020_M01 m01 = new APM0020_M01();			
			HashMap<String, Object> params;
			m01.as_com_unsec = as_com_unsec;
			
			if ("C67002".equals(m01.as_com_unsec)) { // ????????????????????? -> ERP??????????????? ??????
				m01.v_com_unsec = "A7102";//	????????????
			} else if ("C67003".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7103";//	???????????????
			} else if ("C67004".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7104";//	????????????
			} else if ("C67005".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7105";//	???????????????
			} else if ("C67006".equals(m01.v_com_unsec)) {
				m01.v_com_unsec = "A7106";//	????????????
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
						
			m01.ds_list1 = erppraacctlistsaveMapper.selectResMstList(params);
			if (m01.ds_list1 == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectResMstList ?????? ");
				return response;
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
				response.setResultMessage("selectResDtlList ?????? ");
				return response;
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
				response.setResultMessage("???????????? ?????? ????????? ????????? ???????????? ?????? ??? ??? ????????????.");
				return response;				
			}
			
			if ("C16PH".equals(m01.btn_allset_onclick.v_comScd)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("???????????? ?????? ???????????? ??? ??? ????????????. ??????????????? ?????? ??????.!");
				return response;
			}
			
			if (!"C13W".equals(m01.btn_allset_onclick.v_comRmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("?????? ????????? ???????????? ?????? ????????????.");
				return response;
			}

			
			for (int i=0; i<m01.ds_tabList1.size(); i++){				
				if ("C13W".equals(m01.ds_tabList1.get(i).com_rdsec)){
					m01.btn_allset_onclick.nCnt++;
				}
			}
			
			if (m01.btn_allset_onclick.nCnt == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("????????? ????????? ????????????.");
				return response;
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
				p03.btn_rstReg_onclick.v_rmk = "???A/S?????????:"+ p03.btn_rstReg_onclick.v_date +" "+ p03.btn_rstReg_onclick.v_rmkTmp;
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
					response.setResultMessage("modifyPlanDtl_U ?????? [" + i + "][" + res + "]");
					return response;
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
							response.setResultMessage("mergeTaPlanDtl ?????? [" + res + "]");
							return response;
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
				response.setResultMessage("modifyAllsetRptReq_U ?????? [" + res + "]");
				return response;
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
				//??????????????? 'C103' ??? ?????????.(com_plmfg)
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
				response.setResultMessage("modifyAllsetPlanMst_U ?????? [" + res + "]");
				return response;
			}

			params = new HashMap<String, Object>();
			params.put("com_unmsec", m01.as_com_unsec); //????????? ????????????
			params.put("rptq_dt", as_rptq_dt);
			params.put("plm_no", as_plm_no);
			res = erppraacctlistsaveMapper.modifyAllsetPlanDtl_U(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetPlanDtl_U ?????? [" + res + "]");
				return response;
			}
			
			params = new HashMap<String, Object>();
			params.put("com_unmsec", m01.as_com_unsec); //????????? ????????????
			params.put("rptq_dt", as_rptq_dt);
			params.put("rpt_no", m01.btnSaveSP.v_rptNo);
			params.put("rpt_seq", m01.btnSaveSP.v_rptSeq);
			params.put("remark", as_remark);
			
			res = erppraacctlistsaveMapper.modifyAsReqRemark(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetPlanDtl_U ?????? [" + res + "]");
				return response;
			}			
			
			
			
			//////////////////////////////////////////////////////////////////
			//????????? 			
			//////////////////////////////////////////////////////////////////
			
			m01.beforeRptNo  = m01.ds_list1.rpt_no;
			m01.beforeRptSeq = m01.ds_list1.rpt_seq;
			
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);			
			m01.ds_list2 = erppraacctlistsaveMapper.selectNotFinishList(params);
			if (m01.ds_list2 == null || m01.ds_list2.size() == 0) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("selectNotFinishList ?????? ");
				return response;
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
				response.setResultMessage("??? AS?????? ????????? ???????????? ????????????.");
				return response;
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
				response.setResultMessage("??? AS?????? ????????? ???????????? ????????????.");
				return response;
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
			params.put("com_nosec", "C08PLM");	//????????????
			params.put("com_agsec", v_com_agsec);				
			String res_msg = "";			
    		String lSzplmNo = "";
    		dataResult = erppraacctlistsaveMapper.selectPlmPno(params);
    		if (dataResult != null) {
    			lSzplmNo = dataResult.getData1();
    		}

    		params.put("lSzplmNo", lSzplmNo);
    		if ("".equals(lSzplmNo) || lSzplmNo == null){ //???????????????????????? ????????????
        		//???????????? ??????    			
    			int iSeqNo = 0;
    			params.put("seq_setdt", as_rptq_dt.replace("-",  ""));
    			dataResult = erppraacctlistsaveMapper.selectNewSeq(params);
    			if (dataResult == null) {
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("selectNewSeq ??????");
					return response;
    			} else {
    				iSeqNo = dataResult.getValue1();
    			}
    			    			
    			params.put("seq_setdt", as_rptq_dt.replace("-",  ""));
    			params.put("seq_no", iSeqNo);
        		res = erppraacctlistsaveMapper.modifySeqnoinf(params);
        		if (res < 1) { 
        			res_msg = "???????????? ?????? ??? ????????? ?????????????????????.";
        			txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
        		}
        		
        		String strSetdt = (String) params.get("seq_setdt");
        		String strAgsec = (String) params.get("com_agsec");
        		
    			// ???????????? ??????    			
        		lSzplmNo = StringUtils.right(strAgsec, 1) + strSetdt + StringUtils.leftPad(String.valueOf(iSeqNo), 4, "0");
    			
        		params.put("lSzplmNo", lSzplmNo);
    			// AS ????????????mst ??????
        		iSeqNo = erppraacctlistsaveMapper.selectInsertPlanMst_I(params);
    			if (iSeqNo < 1) { 
    				res_msg =  "???????????? ?????? ??? ????????? ?????????????????????2.";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
        		}
    			
    			// AS ????????????dtl ??????
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg =  "selectInsertPlanDtl_I ?????? [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}

    			// ?????? ???????????? update
    			res = erppraacctlistsaveMapper.modifyPlmPno_U(params);
    			if (res < 1) {    				
    				res_msg = "modifyPlmPno_U ?????? [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}
    		
    		} else {
    			
    			// ???????????????????????? ?????????
    			res = erppraacctlistsaveMapper.modifyTaPlanDtl_D(params);
    			if (res < 1) {    				
    				res_msg = "modifyTaPlanDtl_D ?????? [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}
    			
    			// AS ????????????dtl ??????
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg = "selectInsertPlanDtl_I ?????? [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}   			
    		}
    		
    		outVar.put("rePlmNo", lSzplmNo);

    		// ?????????
    		
    		
			if ("".equals(res_msg)) {
				m01.btn_selectRecProc_onclick.v_rePlmNo = (String) outVar.get("rePlmNo");
				if (isDeBug) {
					System.out.println("v_rePlmNo = " + m01.btn_selectRecProc_onclick.v_rePlmNo);
				}
			} else {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("rePlmnoCreate ?????? [" + res_msg + "]");
				return response;
			}

			if (m01.btn_selectRecProc_onclick.v_rePlmNo.length() < 13) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("v_rePlmNo SIZE ??????.");
				return response;
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
						response.setResultMessage("modifyComUnpsec_U ?????? [" + res + "]");
						return response;
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
						response.setResultCode("???AS?????? ???????????? AS??????????????? ?????????????????????.");
						System.out.println(response.toString());
						return response;
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
			p02.reProcYn = "Y";		//??????????????? Flag
		
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
				response.setResultMessage("selectRePlanMstInfo ?????? ");
				return response;
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
				response.setResultMessage("???????????? ???????????? ???????????? ?????????. -> '??????????????? ??????'");
				return response;
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
			p02.ds_result.com_scd = p02.v_comScd;	   //???????????????
			p02.ds_result.plm_no = p02.strPlmNo;
			p02.ds_result.zip_cd = p02.v_ctmZip;   //????????????
			
			p02.ds_result.rpt_no = p02.strRptNo;
			p02.ds_result.rpt_seq = p02.strRptSeq;
			p02.ds_result.pld_rcdt = p02.strOrmAdt;
			p02.ds_result.new_plm_no = p02.strRePlmNo;
			p02.ds_result.rem_dt = p02.strRemDt;
			p02.ds_result.rem_seq = p02.strRemSeq;
			p02.ds_result.com_rfg =	"C141";  // 20180614 LMK ??????????????? ??????????????? ???
			p02.ds_result.usr_cd =	as_user_id;
			//p02.div_detail.cal_ormAdt.set_value(p02.strOrmAdt);						
			
			
			
			
			
			String rptFtm = "C4133";
			
			p02.ds_result.rem_ptm =	rptFtm;
			p02.ds_result.rem_ftm = rptFtm;
			p02.ds_result.rem_tmfyn = "N";   //?????????????????? check
			p02.ds_result.rem_rmk = "";//	this.div_detail.ta_remRmk.value); //?????????????????? 
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
					response.setResultMessage("??????????????? ???????????? ?????? ?????????????????????.");
					return response;
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
				response.setResultMessage("executeFaAseqrem ??????");
				return response;
			}
			
			p02.ds_result.new_rem_seq = dataResult.getData1();
			
			res = erppraacctlistsaveMapper.modifyTcResMst_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResMst_U ?????? [" + res + "]");
				return response;
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
					response.setResultMessage("modifyTaPlanMst_U ?????? [" + res + "]");
					return response;
				}   
			}
			
			res = erppraacctlistsaveMapper.modifyTcResDtl_I(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResDtl_I ?????? [" + res + "]");
				return response;
			}
			
			res = erppraacctlistsaveMapper.modifyRptEnddt_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyRptEnddt_U ?????? [" + res + "]");
				return response;
			}
			
///============================>
			m01.btnReASP.v_pldRmk = "???AS?????????:"+ as_rptq_dt;
			m01.btnReASP.v_plmNo = m01.ds_list1.plm_no;
			
			for (int i=0; i<m01.ds_list2.size(); i++){
				m01.ds_list2.get(i).pld_rmk = m01.btnReASP.v_pldRmk;  //?????????????????? ???????????? ?????????
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
				response.setResultMessage("modifyComUnpsec_U2 ?????? [" + res + "]");
				return response;
			}
			
			//AS??????????????????
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddAsEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime ?????? [" + res + "]");
				return response;
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
			return response;
		}
		
		txManager.commit(status);
		response.setResultCode("200");
		System.out.println(response.toString());	
		return response;
	}

}
