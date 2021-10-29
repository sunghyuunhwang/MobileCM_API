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
	public ArrayList<DataResult> erp_NotifyList(HashMap<String, Object> param) {
		ArrayList<DataResult> allitems;
		HashMap<String, Object> params;
		
		try {
			String as_sti_cd = (String) param.get("sti_cd");
			
			params = new HashMap<String, Object>();
	        params.put("sti_cd", as_sti_cd);
	        
	        allitems = erpsigongasMapper.selectNotifyList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
	
	@Override
	public BaseResponse erp_Fcm_SendNotify(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		HashMap<String, Object> params;
		
		int res = 1;
		
		try {
			
			String DELIMETER = "";

			String as_command = "MobileCM.NOTIFICATION";
			String as_send_from_system = (String) param.get("send_from_system");
			String as_send_to_system = (String) param.get("send_to_system");
			String as_com_scd = (String) param.get("com_scd");
			String as_title = (String) param.get("title");
			String as_message = (String) param.get("message");
			String as_user_id = (String) param.get("user_id");
			String send_dt = "";
			StringBuffer send_text = new StringBuffer();
			int noti_seqno = 0;
						
			params = new HashMap<String, Object>();
			params.put("com_scd", as_com_scd);
	        
			dataResult = erpsigongasMapper.selectNotifyGetDate(params);
			if (dataResult != null) {
				send_dt = dataResult.getData1();
			}
			
			ArrayList<ERPPushMessage> allItems = erpsigongasMapper.selectPhoneID(params);
			if (allItems != null) {
    			for(int i=0; i<allItems.size(); i++) {
    				send_text = new StringBuffer();
    				send_text.append(as_command);
    				send_text.append(DELIMETER + noti_seqno);
    				send_text.append(DELIMETER + as_user_id);
    				send_text.append(DELIMETER + allItems.get(i).getSti_cd());
    				send_text.append(DELIMETER + as_title);
    				send_text.append(DELIMETER + as_message);
    				send_text.append(DELIMETER + send_dt);
    				
    				params = new HashMap<String, Object>();			
    				params.put("send_from_system", as_send_from_system);
    				params.put("send_to_system", as_send_to_system);
    				params.put("sender_id", as_user_id);
    				params.put("send_text", send_text.toString());
    				params.put("receive_id", allItems.get(i).getSti_cd());
    				params.put("receive_phone_id", allItems.get(i).getToken());
    				    				
    	    		res = erpsigongasMapper.insertNotify(params);
    	    		if (res < 1) {
    	    			txManager.rollback(status);
    					response.setResultCode("5001");
    					response.setResultMessage("erp_Fcm_SendNotify insertNotify 오류 [" + res + "]");
    					return response;
    				}
    	    		
    	    		noti_seqno = (int) params.get("seq_no");
    	    		
    	    		send_text = new StringBuffer();
    				send_text.append(as_command);
    				send_text.append(DELIMETER + noti_seqno);
    				send_text.append(DELIMETER + as_user_id);
    				send_text.append(DELIMETER + allItems.get(i).getSti_cd());
    				send_text.append(DELIMETER + as_title);
    				send_text.append(DELIMETER + as_message);
    				send_text.append(DELIMETER + send_dt);
    				
    	    		System.out.println("send_text ="  + send_text.toString());
    	    		
    	    		allItems.get(i).setCommand(as_command);
    	    		allItems.get(i).setMessage(send_text.toString());
    	    		
    				FcmMessage.Send(allItems.get(i));
    	    		
    			}
    		} else {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("전송 대상자가 없습니다.");
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
	public ArrayList<ERPAttachFileList> erp_AttachFileList(HashMap<String, Object> param) {
		ArrayList<ERPAttachFileList> allitems;
		HashMap<String, Object> params;
		
		try {
			String as_attch_file_id = (String) param.get("attch_file_id");
			String as_attch_div_cd = (String) param.get("attch_div_cd");
			
			params = new HashMap<String, Object>();
	        params.put("attch_file_id", as_attch_file_id);
	        params.put("attch_div_cd", as_attch_div_cd);

	        allitems = erpsigongasMapper.selectSigongAttachFileList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
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
				response.setResultMessage("selectWayPointList 오류 [최소2개 이상의 출발/경유/도착지 정보가 필요합니다.]");
				return response;
			}
			
			gogovan.setWaypoint(waypoint);

			System.out.println(gson.toJson(gogovan));
			
			GoGoVanResponse gogovan_res = GoGoVan.sumit(gogovan);
			if (!"200".equals(gogovan_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("고고벤 호출 오류[" + gogovan_res.getResultMessage() +"]");
				return response;
			}
			
			params = new HashMap<String, Object>();
			params.put("rem_dt", rem_dt);
			params.put("rem_seq", rem_seq);
						
			res = erpCalculateMoneyMapper.updateGoGoVan(params);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateGoGoVan 오류 [" + res + "]");
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
					response.setResultMessage("updateAsResultRemark 오류 [" + res + "]");
					return response;
				}
			}
			
			if (!"".equals(as_com_pldsec_arr)) {
				res = crs0010_m01Mapper.updateAsResult(params);
	            if (res < 1) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("updateAsResult 오류 [" + res + "]");
					return response;
				}
			}
                        
            res = schedulemainlistMapper.updateAddAsEndTime(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
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
				response.setResultMessage("insertTcStiReq 오류 [" + res + "]");
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
			
			if ("C68000".equals(as_com_undsec)) { // 모바일사유코드 -> ERP사유코드로 치환
				as_com_undsec = "C52B01";//	상차전반품
			} else if ("C68001".equals(as_com_undsec)) {
				as_com_undsec = "C52B02";//	상차후반품
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
				response.setResultMessage("retrievesTcPlanmstList 오류.");
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
	        
	        //일반
	        //inVar.put("com_pldsec", "C090");	        
	        codeList = new ArrayList<String>();
	        codeList.add("C090");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "A");
	        ArrayList<ds_tcPlandtlList1> input1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);	        

	        //매장경유,반품회수
	        //inVar.put("com_pldsec", "'C092','C096','C095'");
	        codeList = new ArrayList<String>();
	        codeList.add("C092");
	        codeList.add("C096");
	        codeList.add("C095");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "B");
	        ArrayList<ds_tcPlandtlList1> input2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
	        
	        //분해설치,폐기장
	        //inVar.put("com_pldsec", "'C094','C09A'");
	        codeList = new ArrayList<String>();
	        codeList.add("C094");
	        codeList.add("C09A");
	        inVar.put("code_list", codeList);
	        inVar.put("com_pldsec_gbn", "C");
	        ArrayList<ds_tcPlandtlList2> input3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);

	        //기타추가비용 - 모바일에서는 없음
	        //ArrayList<ds_tcPlandtlList3> input3 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);	        	
	        
	    	//월마감 확인
	    	if("Y".equals(ds_tcPlanmstList.med_yn)){
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("월마감 대상 건입니다.");
				return response;
	    	}

	    	String plmNo = "", ComPldsec = "", OrdSseq = "", OrdIseq = "", ItmCd = "", dsGubun = "", setDsC090 = "", setDsC092 = "", setDsC096 = "";		    	
	    	int chk= 0;
	    	for(int i = 0; i < input1.size(); i++){
    			if("C13Y".equals(input1.get(i).com_rdsec)){
    				txManager.rollback(status);
    				response.setResultCode("5001");
    				response.setResultMessage("결과구분이 완결인 건은 미결사유등록 할 수 없습니다.");
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
        					System.out.println("eqty == fqty 같으면 확정수량을 0 으로 변경 update");
        					
        					Map.put("PLD_FQTY", "0");
        					Map.put("PLD_CFAMT", "0");
        				}
        			
        				System.out.println("PLD_FQTY1 ==" + Map.get("PLD_FQTY"));
        			}
        			
        			//2018.04.27 |주요한| 통합테스트 요청사항 
        			//상차전반품인 경우 확정수량 0으로 변경
        			else if("C52B01".equals(undsec)){
        				
        				//Map2.put("PLD_FQTY", "0");
        				//Map2.put("PLD_CFAMT", "0");
        				
        				Map.put("PLD_FQTY", pld_fqty_arr1[i]);
        				
        			}
        			
        			//미결처리
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
        					System.out.println("eqty == fqty 같으면 확정수량을 0 으로 변경 update");
        					
        					Map2.put("PLD_FQTY", "0");
        					Map2.put("PLD_CFAMT", "0");
        				}
        			
        			}
        			//2018.04.27 |주요한| 통합테스트 요청사항 
        			//상차전반품인 경우 확정수량 0으로 변경
        			else if("C52B01".equals(undsec)){
        				
        				//Map2.put("PLD_FQTY", "0");
        				//Map2.put("PLD_CFAMT", "0");
        				
        				Map2.put("PLD_FQTY", pld_fqty_arr2[j]);
        				
        			}
        			
        			//미결처리
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
        					System.out.println("eqty == fqty 같으면 확정수량을 0 으로 변경 update");
        					
        					Map3.put("PLD_FQTY", "0");
        					Map3.put("PLD_CFAMT", "0");
        				}
        			
        			}
        			//2018.04.27 |주요한| 통합테스트 요청사항 
        			//상차전반품인 경우 확정수량 0으로 변경
        			else if("C52B01".equals(undsec)){
        				
        				//Map2.put("PLD_FQTY", "0");
        				//Map2.put("PLD_CFAMT", "0");
        				
        				Map3.put("PLD_FQTY", pld_fqty_arr3[x]);
        				
        			}
        			
        			//미결처리
        			crs0010_m01Mapper.modyfyTcPlandtlSuspense_U_2nd(Map3);
                	updateCount++;
        		}
        	}
        	
        	System.out.println("미결처리 성공 갯수 [ " + updateCount + " ] ");
        	
        	List <Map> chkList = null;
            
        	//mst 결과값 변경을 위해 dtl결과값 조회
            chkList = crs0010_m01Mapper.retrievesTcPlandtlCompleChk(inVar);
            
            for(int j = 0 ; j < chkList.size() ; j++){
            	Map chkMap = chkList.get(j);
            	
            	int nCount = Integer.parseInt(chkMap.get("C13N_COUNT").toString());
            	int wCount = Integer.parseInt(chkMap.get("C13W_COUNT").toString());
            	
            	
            	//실행중값이 0 보다 클때
            	if (wCount > 0){
            		inVar.put("com_rmfg_aft", "C13W");
            		inVar.put("com_plmfg_aft", "C102");
            	}
            	//미결값이 0 보다 클떄
            	else if(nCount > 0 ){
            		inVar.put("com_rmfg_aft", "C13N");
            		inVar.put("com_plmfg_aft", "C103");
            	}
            	//미결 ,실행중이 전부 0보다 작을때 전부 완결 처리 
            	else{
            		inVar.put("com_rmfg_aft", "C13Y");
            		inVar.put("com_plmfg_aft", "C103");
            	}
            }

            int mstChk = crs0010_m01Mapper.modyfyTcPlanMstComple(inVar);
            System.out.println("시공정보 종합 완결처리 갯수 [ "+ mstChk+" ] ");
	        
        	//처리결과시간
        	params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddSigongEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
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
			
//	        //일반
//	        inVar.put("plm_no", as_plm_no);
//	        inVar.put("com_pldsec", "'C090'");            
//	        ds_tcPlandtlList1 ds_out1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //매장경유,반품회수
//	        inVar.put("com_pldsec", "'C092','C096','C095'");
//	        ds_tcPlandtlList1 ds_out2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //분해설치,폐기장            
//	        inVar.put("com_pldsec", "'C094','C09A'");
//	        ds_tcPlandtlList2 ds_out3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);
//	        
//	        //기타추가비용            
//	        ds_tcPlandtlList3 ds_out4 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);
	        	        	        
	        //월마감 확인
	    	if ("Y".equals(ds_tcPlanmstList.med_yn)) {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("월마감 대상 건입니다.");
				return response;
	        }
	        
			if(!"C103".equals(ds_tcPlanmstList.com_plmfg) && !"C104".equals(ds_tcPlanmstList.com_plmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("상태가 결과등록이나 정산확정이 아니면 일괄 정산/취소 할 수 없습니다.");
				return response;
			}
			
			if("C13W".equals(ds_tcPlanmstList.com_rmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("결과처리를 먼저 해주시길 바랍니다.");
				return response;
			}
			
			if("".equals(ds_tcPlanmstList.plm_ftm) || ds_tcPlanmstList.plm_ftm == null){
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("확정시간이 등록되지 않았습니다.");
				return response;
			}
			
			int rv_actcnt = 0;
			dataResult = crs0010_m01Mapper.getActCnt(param);
			if (dataResult != null) {
				rv_actcnt = dataResult.getValue1();
			}
			
			if("C103".equals(ds_tcPlanmstList.com_plmfg) && rv_actcnt > 0){
				//"STI_NM팀의 ORM_NM건이 이미 정산확정이 등록되어 있습니다."
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(ds_tcPlanmstList.sti_nm + "팀의 " + ds_tcPlanmstList.orm_nm + "(" + ds_tcPlanmstList.orm_no + ") 건이 이미 정산확정이 등록되어 있습니다.");
				return response;
			} else if("C104".equals(ds_tcPlanmstList.com_plmfg) && rv_actcnt == 0) {
				//"STI_NM팀의 ORM_NM건이 이미 정산확정이 취소되어 있습니다."
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage(ds_tcPlanmstList.sti_nm + "팀의 " + ds_tcPlanmstList.orm_nm + "(" + ds_tcPlanmstList.orm_no + ") 건이 이미 정산확정이 취소되어 있습니다.");
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
        	
        	String ormGgubun = (String) mapMst.get("ORM_GGUBUN");	//정신시 현장,소액 구분
        	ormGgubun = ormGgubun.replaceAll(" ", "");
        	
        	//COM_PLMFG 쿼리로 가져오기(넥사크로 화면을 refresh 안하고 2명이상이 같은건을 처리할 경우 데이터가 꼬임)        	
        	dataResult = crs0010_m01Mapper.retrieveTcPlanmstComPlmfg(param);
			if (dataResult != null) { 
				com_plmfg = StringUtil.isNullToString(dataResult.getData1());
			}
        	mapMst.put("COM_PLMFG", com_plmfg);
        	
        	//결과등록일시 정산처리 시작
        	if("C103".equals(mapMst.get("COM_PLMFG"))){
            	//바로스
            	if("C21B".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2402";
    				vndt = "C2505";
    				svnd = "C21B";	
            	}
            	//퍼시스
            	else if("C21F".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2403";
    				vndt = "C2505";
    				svnd = "C21F";
            	}
            	//일룸
            	else if("C21I".equals(mapMst.get("BELONG_CMP"))){
            		vndf = "C2404";
    				vndt = "C2505";
    				svnd = "C21I";
            	}
            	else{
            		txManager.rollback(status);
    				response.setResultCode("5001");
    				response.setResultMessage("시공팀의 소속회사가 존재하지 않는 시공팀이 있습니다. 시공팀관리에서 시공팀의 소속회사를 등록하고 진행해주세요.");
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
            	
            	//금액이 0이지만 건수가 있는경우 insert하기 위하여 카운트 체크
            	pldsecCountList = crs0010_m01Mapper.retrievePldsecCountList(mapMst);
            	
            	
            	//일룸일때
            	if("C02I".equals(mapMst.get("COM_AGSEC"))){

            		//일룸일시 정산계산
            		C02I_List = crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
            		
            		int LD_AMT;
            		double LD_AMT_D;
            		double LD_AMT_P;
            		
            		for(int t = 0; t < C02I_List.size();t++){
            			
            			C02I_90List 	=	crs0010_m01Mapper.retrieveC02I_Sum(mapMst);
            			
            			Map C090Map = C02I_90List.get(t);
            			
            			pldCnt = Integer.parseInt(pldsecCountList.get(0).get("C090_COUNT").toString());
            			
            			if(pldCnt > 0){
            				//일반일때
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
	            				vfromP = svnd;       //서비스업체
	            				vtP    = "C2501";	 //업체to구분 : 시공팀
	            				vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
	            				vfD  = vndf;      // 
	            				vfromD = svnd;       //서비스업체
	            				vtD    = "C2505";     //업체to구분 : 대리점
	            				vtoD   = (String) mapMst.get("COM_AGSEC");    //대행사구분
	            			
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
	                			LD_AMT_D = ( LD_AMT - Integer.parseInt(C090Map.get("C090_LD_STOCK1").toString()) - Integer.parseInt(C090Map.get("C090_LD_STOCK2").toString()) ) * 1.2; //청구금액 = 시공비 * 1.2

	                			LD_AMT_P = LD_AMT; 

	                			if(!"".equals(actinfList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
	                				C090Map.put("ACL_AMT_D", LD_AMT_D);
	                				C090Map.put("ACL_RMK_D", "청구정산금액 = 시공비용( " + LD_AMT + " - " + C090Map.get("C090_LD_STOCK1") + " - " + C090Map.get("C090_LD_STOCK2") +" )");
	                			
	                				crs0010_m01Mapper.modyfyTcActlistD_I(C090Map);
	                			}
	                			
	                			//2018.06.01 |주요한| 전달건 조건 변경 항성현대리님 요청
	                			//if("O1007".equals(mapMst.get("ORM_ATTR"))){
	                			ormNmCut = "";
	                			
	                			//전달건인지 확인
	                			ormNmCut = (String) mapMst.get("ORM_NM");
	                			if(ormNmCut.length() > 3){
	                				ormNmCut = ormNmCut.substring(1, 3);
	                			}
	                			
	                			System.out.println("ormNmCut   :  " + ormNmCut);
	                			
	            				if("전달".equals(ormNmCut)){	
	            					C02I_RelayList = crs0010_m01Mapper.retrievesC090RelayAmt(mapMst);
	            					
	            					if(LD_AMT_P < Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString())){
	            						
	            						
	            						//2018.06.01 |주요한| 기존금액이 전달비 2000원 보다 적을때 기존금액으로 계산으로 변경 [현재 무조껀 0으로 바꿨음]
	            						//C090Map.put("ACL_AMT_P", "0");
		            					
		            					//LD_AMT_P = 0;
	            						
	            					}else{
	            						C090Map.put("ACL_AMT_P", LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString()));
		            					
		            					LD_AMT_P = LD_AMT_P - Integer.parseInt(C02I_RelayList.get(0).get("AMT").toString());
	            					}
	            					
	            				}
	                			
	                			if(!"".equals(actinfList.get(0).get("COM_ACD_P")) && LD_AMT_P >= 0  || "전달".equals(ormNmCut)){
	                				C090Map.put("ACL_AMT_P", LD_AMT_P);
	                				C090Map.put("ACL_RMK_P", "지급정산금액 = 시공비용( " + LD_AMT_P + " )");
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
            				//매장경유 
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
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
	            				
	            				//매장경우 * 요율
	            				LD_AMT_D = Integer.parseInt(C092Map.get("C092_LD_AMT").toString()) * 0.9;
	            				LD_AMT_P = Integer.parseInt(C092Map.get("C092_LD_AMT").toString()) * 0.82;
	            				
	            				if(!"".equals(actinfList.get(0).get("COM_ACD_D")) && LD_AMT_D > 0  ){
	            					
	            					C092Map.put("ACL_RMK_D", "청구정산금액 = 시공비용( " + C092Map.get("C092_LD_AMT") + ") * 청구율( 0.9 )");
		            				C092Map.put("ACL_AMT_D", LD_AMT_D);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C092Map);
	            				}
	            				
	            				C092Map.put("ACL_RMK_P", "지급정산금액 = 시공비용( "+ C092Map.get("C092_LD_AMT") + ") * 청구율( 0.82 )");
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
	    						vfromP = svnd ;       //서비스센터
	    						vtP    = "C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	    								
	            				vfD = vndf;
	            				vfromD = (String) C092Map.get("COM_AGSEC");
            					vtD = "C2505";
	        					vtoD = (String) mapMst.get("AGT_CD");		
    							
	                			C092Map.put("COM_VFSEC", vfD);
	                			
	                			//최초1회 매장경유 비용 대상 조회
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
		        						
		        						C092Map.put("ACL_RMK_D", "청구정산금액 ( " + LD_AMT_D + " ) = 시공비용( " + LD_AMT + " ) * 청구율 ( " + C02I_PassList.get(0).get("ACI_RATEAMT_D") + " )" );
		        						C092Map.put("ACL_AMT_D", LD_AMT_D);
		        						
		        						crs0010_m01Mapper.modyfyTcActlistD_I(C092Map);
		        					}
		        					
		        					if(!"".equals(C02I_PassList.get(0).get("COM_ACD_P")) && LD_AMT_P > 0  ){
		        						
		        						C092Map.put("ACL_RMK_P", "지급정산금액  = 시공비용( " + LD_AMT_P + " )" );
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
            				//반품회수
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
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
	            					
	            					C096Map.put("ACL_RMK_D", "청구정산금액 = 시공비용( " + C096Map.get("C096_LD_AMT") + ") * 청구율(" + C096Map.get("ACI_RATEAMT_D") + " )");
		            				C096Map.put("ACL_AMT_D", LD_AMT_D);
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C096Map);
	            				}
	            				
	            				C096Map.put("ACL_RMK_P", "지급정산금액 = 시공비용( " + C096Map.get("C096_LD_AMT") + " )");
	            				C096Map.put("ACL_AMT_P", LD_AMT_P);
	            				
	            				//확인필요 상위 분해설치 ,일반 이랑 로직다름 
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
            				//분해설치
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
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
	            					C094Map.put("ACL_RMK_D", "청구정산금액 = 분해설치 ( " + LD_AMT_D + " )");
           				
		            				C094Map.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C094Map);
		            				
	            				}
	            				
	            				if(LD_AMT_P >= 0){
	            					C094Map.put("ACL_AMT_P", LD_AMT_P);
	            					C094Map.put("ACL_RMK_P", "지급정산금액 = 분해설치 ( " + LD_AMT_P + " )");
	            					
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
            				//폐기장
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
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
	            					C09AMap.put("ACL_RMK_D", "청구정산금액 = 폐기장 ( " + LD_AMT_D + " )");
           				
		            				C09AMap.put("COM_CESEC", actinfList.get(0).get("COM_CESEC"));
		            				
		            				crs0010_m01Mapper.modyfyTcActlistD_I(C09AMap);
		            				
	            				}
	            				
	            				if(LD_AMT_P >= 0){
	            					C09AMap.put("ACL_AMT_P", LD_AMT_P);
	            					C09AMap.put("ACL_RMK_P", "지급정산금액 = 폐기장 ( " + LD_AMT_P + " )");
	            					
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
            	//일룸이아닐때
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
            				//일반일때
	            			if("C090".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				vfP    = vndf; 
	            				vfromP = svnd;       //서비스업체
	            				vtP    = "C2501";	 //업체to구분 : 시공팀
	            				vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
	            				vfD  = vndf;      // 
	            				vfromD = svnd;       //서비스업체
	            				vtD    = "C2505";     //업체to구분 : 대리점
	            				vtoD   = (String) mapMst.get("COM_AGSEC");    //대행사구분
	            			
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
	            						dtlMap.put("ACL_RMK_D", "일반 청구정산금액 = " + 0 );
	            					}else{
	            						dtlMap.put("ACL_AMT_D", amt);
	            						dtlMap.put("ACL_RMK_D", "일반 청구정산금액 = " + amt );
	            					}
	            					
	            					dtlMap.put("ACL_AMT_P", amt);
	            					dtlMap.put("ACL_RMK_P", "일반 지급정산금액 = " + amt );
	            				}else{
	            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
	            					dtlMap.put("ACL_RMK_D", "일반 청구정산금액 = " + amt + " * " + AciRateamtD );
	            					
	            					if("C02F".equals(mapMst.get("COM_AGSEC")) || "C02P".equals(mapMst.get("COM_AGSEC")) ){
	            						//현장 Y
	            						if("Y".equals(ormGgubun)){
	            							dtlMap.put("ACL_AMT_P", amt );
	            							dtlMap.put("ACL_RMK_P", "일반 지급정산금액 = " + amt  );
	            						}
	            						//소액  N
	            						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
	            							
	            							if("C02F".equals(mapMst.get("COM_AGSEC"))){
	            								dtlMap.put("ACL_AMT_P", amt * 1.39);
		            							dtlMap.put("ACL_RMK_P", "일반 지급정산금액 = " + amt + " * 1.39" );	
	            							
	            							}else if("C02P".equals(mapMst.get("COM_AGSEC"))){
	            								dtlMap.put("ACL_AMT_P", amt * 1.37);
		            							dtlMap.put("ACL_RMK_P", "일반 지급정산금액 = " + amt + " * 1.37" );
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
            				//매장경유일때
	            			if("C092".equals(dtlMap.get("COM_PLDSEC"))){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				//20180724 매장경유일 때 퍼시스는 공장도가로 정산되어야 해서 추가
	            				amt_mnf = Integer.parseInt(dtlMap.get("TOTAL_SUM_MNF_AMT").toString());
	            				
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C092(dtlMap);
	            				
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
			            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt_mnf + " * " + AciRateamtD );
			            					/*dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt + " * " + AciRateamtD );*/
		            				}else{
		            					dtlMap.put("ACL_AMT_D", AciRateamtD);
		            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " +  AciRateamtD );
		            				}
	            					
	            					//현장 Y
	        						if("Y".equals(ormGgubun)){
	        							dtlMap.put("ACL_AMT_P", amt );
	        							dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " + amt);
	        						}
	        						//소액 N
	        						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
	        							
	        							if("C02F".equals(mapMst.get("COM_AGSEC"))){
	        								dtlMap.put("ACL_AMT_P", amt * 1.39);
		        							dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " + amt + " * 1.39 " );
	        								
	        							}else if("C02P".equals(mapMst.get("COM_AGSEC"))){
	        								dtlMap.put("ACL_AMT_P", amt * 1.37);
		        							dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " + amt + " * 1.37 " );
	        							}
	                				}
	            				}
	            				else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
	            					dtlMap.put("ACL_AMT_D", amt * 0.9);
	            					dtlMap.put("ACL_AMT_P", amt * 0.82);
	            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt + " * 0.9");
	            					dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " + amt + " * 0.82");
	            					
	            				}
	            				else{
	            					
	            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
		            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
		            					dtlMap.put("ACL_AMT_P", amt * AciRateamtP);
		            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt + " * " + AciRateamtD );
		            					dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " + amt + " * " + AciRateamtP );
	            					}else{
		            					dtlMap.put("ACL_AMT_D", AciRateamtD);
		            					dtlMap.put("ACL_AMT_P", AciRateamtP);
		            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " +  AciRateamtD );
		            					dtlMap.put("ACL_RMK_P", "매장경유 지급정산금액 = " +  AciRateamtP );
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
            				//반품회수일때
	            			if("C096".equals(dtlMap.get("COM_PLDSEC"))){
	            				
	            				amt = Integer.parseInt(dtlMap.get("TOTAL_SUM_PLD_AMT").toString());
	            				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				
	            				vfP    = vndf; 			
	    						vfromP = svnd ;       //서비스센터
	    						vtP    ="C2501";			//업체to구분 : 시공팀
	    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
	            				
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
			            					dtlMap.put("ACL_RMK_D", "반품회수 청구정산금액 = " + amt + " * " + AciRateamtD );
			            				}else{
			            					dtlMap.put("ACL_AMT_D", AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "반품회수 청구정산금액 = " +  AciRateamtD );
			            				}
		            					
			            			}
			            			else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
		            					dtlMap.put("ACL_AMT_D", amt * 0.9);
		            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt + " * 0.9");
		            				}
		            				else{
		            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_D"))){
			            					dtlMap.put("ACL_AMT_D", amt * AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " + amt + " * " + AciRateamtD );
		            					}else{
			            					dtlMap.put("ACL_AMT_D", AciRateamtD);
			            					dtlMap.put("ACL_RMK_D", "매장경유 청구정산금액 = " +  AciRateamtD );
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
		            					
		            					//현장 Y
		        						if("Y".equals(ormGgubun)){
		        							dtlMap.put("ACL_AMT_P", amt );
		        							dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt  );
		                				}
		        						//소액 N
		        						else if("N".equals(ormGgubun)|| "".equals(ormGgubun) ){
		        							dtlMap.put("ACL_AMT_P", amt * 1.39);
		        							dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt + " * 1.39");
		                				}
		            					
		            				}	
		            				else if("C02P".equals(mapMst.get("COM_AGSEC")) ){
		            					
		            					//현장 Y
		        						if("Y".equals(ormGgubun)){
		        							dtlMap.put("ACL_AMT_P", amt );
		        							dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt  );
		                				}
		        						//소액 N
		        						else if("N".equals(ormGgubun) || "".equals(ormGgubun) ){
		        							dtlMap.put("ACL_AMT_P", amt * 1.37);
		        							dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt + " * 1.37");
		                				}					            					
			            			}
			            			else if("C02I".equals(mapMst.get("COM_AGSEC")) ){
		            					dtlMap.put("ACL_AMT_P", amt * 0.82);
		            					dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt + " * 0.82");
		            				}
		            				else{
		            					if("C12R".equals(actinfList.get(0).get("COM_RASEC_P"))){
			            					dtlMap.put("ACL_AMT_P", amt * AciRateamtP);
			            					dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " + amt + " * " + AciRateamtP );
		            					}else{
			            					dtlMap.put("ACL_AMT_P", AciRateamtP);
			            					dtlMap.put("ACL_RMK_P", "반품회수 지급정산금액 = " +  AciRateamtP );
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
            				//분해설치 일때
	            			if("C094".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				
	            				AciRateamtD = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_D").toString());
	            	            AciRateamtP = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_P").toString());
	          				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				if(actinfList.size() > 0){
	            				

		            				vfP    = vndf; 			
		    						vfromP = svnd ;       //서비스센터
		    						vtP    ="C2501";			//업체to구분 : 시공팀
		    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
		            				
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
		        						dtlMap.put("ACL_RMK_D", "분해설치 청구정산금액 = " + AciRateamtD);
		        						dtlMap.put("ACL_RMK_P", "분해설치 지급정산금액 = " + AciRateamtP );
		        					}
		        					//else if("C09A".equals(dtlMap.get("COM_PLDSEC"))){
		        					//	dtlMap.put("ACL_RMK_D", "폐기장 청구정산금액 = " + AciRateamtD );
		        					//	dtlMap.put("ACL_RMK_P", "폐기장 지급정산금액 = " + AciRateamtP );
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
            				//폐기장 일때
	            			if("C09A".equals(dtlMap.get("COM_PLDSEC")) ){
	            				
	            				
	            				AciRateamtD = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_D").toString());
	            	            AciRateamtP = Double.parseDouble(dtlMap.get("TOTAL_SUM_TRI_AMT_P").toString());
	          				
	            				dtlMap.put("COM_AGSEC", mapMst.get("COM_AGSEC"));
	            				dtlMap.put("COM_VFSEC", vndf);
	            				
	            				actinfList = crs0010_m01Mapper.retrievesTcActinf_C096(dtlMap);
	            				
	            				if(actinfList.size() > 0){
	            				

		            				vfP    = vndf; 			
		    						vfromP = svnd ;       //서비스센터
		    						vtP    ="C2501";			//업체to구분 : 시공팀
		    						vtoP   = (String) mapMst.get("STI_CD");     //시공팀 
		            				
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
		        						dtlMap.put("ACL_RMK_D", "분해설치 청구정산금액 = " + AciRateamtD);
		        						dtlMap.put("ACL_RMK_P", "분해설치 지급정산금액 = " + AciRateamtP );
		        					}
		        					else if("C09A".equals(dtlMap.get("COM_PLDSEC"))){
		        						dtlMap.put("ACL_RMK_D", "폐기장 청구정산금액 = " + AciRateamtD );
		        						dtlMap.put("ACL_RMK_P", "폐기장 지급정산금액 = " + AciRateamtP );
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
    				//기타추가비용일때
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
        				vfromP = svnd;        //서비스센터
        				vtP    = "C2501";			//업체to구분 : 시공팀
        				vtoP   = (String) mapMst.get("STI_CD");     //시공팀  
        		
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
                		dtlMap.put("ACL_RMK_P", "기타추가비용 지급정산금액 = " + amt );
						
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
        					dtlMap.put("ACL_RMK_D", "기타추가비용 청구정산금액 = " + amt + " * 청구율 " + actinfList.get(0).get("ACL_RATEAMT_D") + " ");
        					dtlMap.put("COM_VFSEC", actinfList.get(0).get("COM_VFSEC_D"));
	        				dtlMap.put("COM_VTSEC", actinfList.get(0).get("COM_VTSEC_D"));	
	        				
	        				crs0010_m01Mapper.modyfyTcActlistD_I(dtlMap);
        				}	
                	}	
    			}
    			
    			mapMst.put("COM_PLMFG_AFT", "C104");
    			crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);
        	}	
        	
        	//정산완료일시 정산 취소 
        	else if("C104".equals(mapMst.get("COM_PLMFG"))){
        		
        		crs0010_m01Mapper.modyfyTcActlist_D(mapMst);
        		mapMst.put("COM_PLMFG_AFT", "C103");
        		crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);
        	}

        	/*//완결인 경우 실행중으로 변경 // 실행중인경우 완결로 변경 
        	if("C103".equals(mapMst.get("COM_PLMFG"))){
        		mapMst.put("COM_PLMFG_AFT", "C104");
        	}else if("C104".equals(mapMst.get("COM_PLMFG"))){
        		mapMst.put("COM_PLMFG_AFT", "C103");
        	}
        	
        	crs0010_m01Mapper.TcplanmstAllCalculate(mapMst);*/
        	
        	//일룸일 경우, 
        	if("C02I".equals(mapMst.get("COM_AGSEC"))){
        		
    			HashMap<String, Object> params = new HashMap<String, Object>();
    			params.put("plm_no", ds_tcPlanmstList.plm_no);
    			params.put("sti_cd", ds_tcPlanmstList.sti_cd);
    			params.put("com_scd", ds_tcPlanmstList.com_scd);
    			params.put("plm_cdt", ds_tcPlanmstList.plm_cdt);
    			params.put("com_brand", ds_tcPlanmstList.com_brand);        			
    			
    			//벽고정 추가정산
        		if (ds_tcPlanmstList.orm_nm.indexOf("(벽고정)") == 0) {
        			
        			res = erpsigongasMapper.insertSigongWallFix(params);
        			if (res < 1) { 
            			txManager.rollback(status);
        				response.setResultCode("5001");
        				response.setResultMessage("insertSigongWallFix 오류 [" + res + "]");
        				return response;
            		}
        			
        			res = erpsigongasMapper.insertSigongWallFixAcc(params);
        			if (res < 1) { 
            			txManager.rollback(status);
        				response.setResultCode("5001");
        				response.setResultMessage("insertSigongWallFixAcc 오류 [" + res + "]");
        				return response;
            		}
        		}

        		//저녁시공 자동정산
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
        		
        		//시공시간이 18:00 이후이고, 도착안내시간이 18:00 이후인 경우,
    			if ("Y".equals(over_time) && !"".equals(rem_ftm)) {				
    				res = erpsigongasMapper.insertSigonWorkTimeOver(params);
    				if (res < 1) { 
    	    			txManager.rollback(status);
    					response.setResultCode("5001");
    					response.setResultMessage("insertSigonWorkTimeOver 오류 [" + res + "]");
    					return response;
    	    		}
    				
    				res = erpsigongasMapper.insertSigonWorkTimeOverAcc(params);
    				if (res < 1) { 
    	    			txManager.rollback(status);
    					response.setResultCode("5001");
    					response.setResultMessage("insertSigonWorkTimeOverAcc 오류 [" + res + "]");
    					return response;
    	    		}    				
    			}
        	}        	
        	
        	//처리결과시간
        	HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddSigongEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
				return response;
    		}        	
        	
        	mstUpdChk++;
        	System.out.println("시공건당 일괄정산 처리 update 갯수 [ " + mstUpdChk + "] ");
	        
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

			//완결 - A
			//미결 - 재일정 : B
			//미결 - 반품 : C
			//미결 - AS접수 : D
			
			//IF 완결이거나 AS 인 경우 + 저녁 여섯시 이후
			if(mob_std.equals("A") || mob_std.equals("D")) {

				dataResult = schedulemainlistMapper.getNowTime();
				
				if (dataResult == null) {
					txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("getNowTime 오류");
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
						response.setResultMessage("insertNightTimeJungsan 오류 [" + res + "]");
						return response;
					}				
				}		
			}

			
			ds_tcPlanmstList ds_tcPlanmstList = crs0010_m01Mapper.retrievesTcPlanmstList(param);
			
//	        //일반
//	        inVar.put("plm_no", as_plm_no);
//	        inVar.put("com_pldsec", "'C090'");            
//	        ds_tcPlandtlList1 ds_out1 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //매장경유,반품회수
//	        inVar.put("com_pldsec", "'C092','C096','C095'");
//	        ds_tcPlandtlList1 ds_out2 = crs0010_m01Mapper.retrievesTcPlandtlList_1(inVar);
//	        
//	        //분해설치,폐기장            
//	        inVar.put("com_pldsec", "'C094','C09A'");
//	        ds_tcPlandtlList2 ds_out3 = crs0010_m01Mapper.retrievesTcPlandtlList_2(inVar);
//	        
//	        //기타추가비용            
//	        ds_tcPlandtlList3 ds_out4 = crs0010_m01Mapper.retrievesTcPlandtlList_3(inVar);
	        	        
	        //월마감 확인
	    	if ("Y".equals(ds_tcPlanmstList.med_yn)) {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("월마감 대상 건입니다.");
				return response;
	        }
	    	
			if("C104".equals(ds_tcPlanmstList.com_plmfg)){
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("상태가 정산완료인 건은 일괄 완결/취소 할 수 없습니다.");
				return response;
			}

			if (!"C13W".equals(ds_tcPlanmstList.com_rmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("결과가 실행중인 건만 완결 처리할 수 있습니다.");
				return response;
			}	        
	        
            int mstUpdChk = 0;
            int dtlUpdChk = 0;
            
            Map mapMst = new HashMap<String, String>();
            mapMst.put("plm_no", as_plm_no);
            mapMst.put("usr_id", as_usr_id);
            mapMst.put("mob_remark", mob_remark);
            
        	// 실행중인경우 완결로 변경        	
            mapMst.put("com_rdsec_aft", "C13Y");
    		mapMst.put("gubun", "Y");    		
    		
    		res = crs0010_m01Mapper.modyfyTcplanmstAllComplete(mapMst);
    		if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modyfyTcplanmstAllComplete 오류 [" + res + "]");
				return response;
			}
    		
            List<Map> chkList = crs0010_m01Mapper.retrievesTcPlandtlCompleChk(mapMst);            
            for(int x = 0 ; x < chkList.size() ; x++){
            	Map chkMap = chkList.get(x);
            	
            	int nCount = Integer.parseInt(chkMap.get("C13N_COUNT").toString());
            	int wCount = Integer.parseInt(chkMap.get("C13W_COUNT").toString());
            	
            	//실행중값이 0 보다 클때
            	if (wCount > 0){
            		mapMst.put("com_rmfg_aft", "C13W");
            		mapMst.put("com_plmfg_aft", "C102");
            	}
            	//미결값이 0 보다 클떄
            	else if(nCount > 0 ){
            		mapMst.put("com_rmfg_aft", "C13N");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            	//미결 ,실행중이 전부 0보다 작을때 전부 완결 처리 
            	else{
            		mapMst.put("com_rmfg_aft", "C13Y");
            		mapMst.put("com_plmfg_aft", "C103");
            	}
            }
            
            res = crs0010_m01Mapper.modyfyTcPlanMstComple(mapMst);
            System.out.println("시공정보 종합 완결처리 갯수 [ "+ res+" ] ");
            
            
            //일룸 소파건의 경우 알림톡 완결 알림톡 발송기능 추가
            //2021-02-02 황성현
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
					response.setResultMessage("getSigongBrand 오류");
					return response;
				}	
				
				com_brand = dataResult.getData1() ;
    	    	
				//퍼시스의 경우 시공완결 시 대리점 영업사원에게 완결 문자메시지 발송
				if("T60F01".equals(com_brand)) {
					
					String agt_hpno = "";
					String sms_message = "";
					
					dataResult = schedulemainlistMapper.getSigongAgtCd(mapMst2);
					
					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("getSigongAgtCd 오류");
						return response;
					}	
					
					String orm_nm = "";
					String sti_nm = "";
					
					agt_hpno = dataResult.getData1();
					orm_nm = dataResult.getData2();
					sti_nm = dataResult.getData3();
					
	    	    	JSONObject obj = new JSONObject();
	    	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
	    	    	 
	    	    	JSONObject api_token = new JSONObject();
	    	    	JSONObject sendList = new JSONObject();	    	 
	    	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
	    	    		        		
	        		title = "퍼시스 시공 완료";
	        		subject = "퍼시스 시공 완료";
	        		    	    	
	    	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
	 
	    	    	String orm_hdphone = agt_hpno;
	    	    	String sti_cd = as_usr_id;
	    	    	String orm_no = as_plm_no;
	    	    	
					sms_message = orm_nm+" 고객님 시공건이 완료 되었습니다. \r\n"+ "담당 시공팀 : " + sti_nm;

					from_no = "1588-1244";
					
	    	       	for (int i = 0; i < 1; i++)//배열
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
	    				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
	    				return response;
	    			}					
						
				}

				if("T60I01".equals(com_brand)) { //일룸인경우 

					dataResult = schedulemainlistMapper.getSigongSofaYn(mapMst2);

					if (dataResult == null) {
						txManager.rollback(status);
						response.setResultCode("5001");
						response.setResultMessage("getSigongSofaYn 오류");
						return response;
					}						
					
					sofa_yn = dataResult.getData1() ;
					
					if("Y".equals(sofa_yn)) { //일룸 - 소파 구매 품목인 경우 

		    	    	JSONObject obj = new JSONObject();
		    	    	JSONArray jArray = new JSONArray(); //배열이 필요할때
		    	    	 
		    	    	JSONObject api_token = new JSONObject();
		    	    	JSONObject sendList = new JSONObject();	    	 
		    	    	JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
		    	    		        		
		        		title = "일룸 소파 시공 완료";
		        		subject = "일룸 소파 시공 완료";
		        		
			    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
			    		from_no = "1577-5670";
			    		templateCode = "iloomsofasigongmessage02";
		    	    	
		    	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");
		 
		    	    	String orm_nm = "";
		    	    	String sti_nm = "일룸시공팀";
		    	    	String orm_hdphone = "";
		    	    	String sti_cd = as_usr_id;
		    	    	String orm_no = as_plm_no;
		    	    	String biztalkmessage = "";
		    	    	String chatbot_url = "http://pf.kakao.com/_xnlbxnl/chat";
		    	    	
						dataResult = schedulemainlistMapper.getSigongOrmNm(mapMst2);

						if (dataResult == null) {
							txManager.rollback(status);
							response.setResultCode("5001");
							response.setResultMessage("getSigongOrmNm 오류");
							return response;
						}	
						
						orm_nm = dataResult.getData1() ;
						
		    	    	biztalkmessage = orm_nm+" 고객님!\r\n" +
						    	    	"일룸 제품을 구매해주셔서 감사합니다.\r\n"+
						    	    	"고객님께서 주문하신 제품이 이상 없이 시공되었으며,\r\n"+
						    	    	"소파 및 마감재(가죽 혹은 패브릭)의 특성과 유지관리에 대한 내용을\r\n"+
						    	    	"시공 진행 시 모두 전달받으신 것으로 확인되었습니다.\r\n\r\n"+
						    	    	"제품 사용 시, 궁금하신 사항은 동봉된 사용자가이드를 참고하시거나\r\n"+
						    	    	"챗봇을 통해 문의 바랍니다.\r\n\r\n"+
						    	    	"카카오톡 대화창에 “시작”을 입력해주세요.\r\n"+
						    	    	"■ 챗봇상담 : "+chatbot_url+"\r\n"+
						    	    	"(운영시간 평일 09:30-17:30, 주말 및 공휴일 휴무)";


								
						dataResult = schedulemainlistMapper.getSigongOrmHdphone(mapMst2);

						if (dataResult == null) {
							txManager.rollback(status);
							response.setResultCode("5001");
							response.setResultMessage("getSigongOrmHdphone 오류");
							return response;
						}
						
						orm_hdphone = dataResult.getData1() ;
						
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
		    				response.setResultMessage("알림톡전송결과 오류  [" + kakao_res.getResultMessage() + "]");
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
				response.setResultMessage("selectResDtlList 오류 ");
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
				response.setResultMessage("출고완료 상태 이외의 일정은 결과등록 처리 할 수 없습니다.");
				return response;				
			}
			
			if ("C16PH".equals(m01.btn_allset_onclick.v_comScd)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("포항센터 건은 미결처리 할 수 없습니다. 정보기술팀 연락 요망.!");
				return response;
			}
			
			if (!"C13W".equals(m01.btn_allset_onclick.v_comRmfg)) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("이미 결과가 등록되어 있는 건입니다.");
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
				response.setResultMessage("선택된 품목이 없습니다.");
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
							response.setResultMessage("mergeTaPlanDtl 오류 [" + res + "]");
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
				response.setResultMessage("modifyAllsetRptReq_U 오류 [" + res + "]");
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
				return response;
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
				return response;
			}
			
			params = new HashMap<String, Object>();
			params.put("com_unmsec", m01.as_com_unsec); //모바일 사유코드
			params.put("rptq_dt", as_rptq_dt);
			params.put("rpt_no", m01.btnSaveSP.v_rptNo);
			params.put("rpt_seq", m01.btnSaveSP.v_rptSeq);
			params.put("remark", as_remark);
			
			res = erppraacctlistsaveMapper.modifyAsReqRemark(params);
			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyAllsetPlanDtl_U 오류 [" + res + "]");
				return response;
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
				response.setResultMessage("재 AS예약 처리할 품목들이 없습니다.");
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
				response.setResultMessage("재 AS예약 처리할 품목들이 없습니다.");
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
					return response;
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
					return response;
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
					return response;
        		}
    			
    			// AS 예정정보dtl 생성
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg =  "selectInsertPlanDtl_I 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}

    			// 이전 시공번호 update
    			res = erppraacctlistsaveMapper.modifyPlmPno_U(params);
    			if (res < 1) {    				
    				res_msg = "modifyPlmPno_U 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}
    		
    		} else {
    			
    			// 재일정반영처리가 된경우
    			res = erppraacctlistsaveMapper.modifyTaPlanDtl_D(params);
    			if (res < 1) {    				
    				res_msg = "modifyTaPlanDtl_D 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
    			}
    			
    			// AS 예정정보dtl 생성
    			res = erppraacctlistsaveMapper.selectInsertPlanDtl_I(params);
    			if (res < 1) {    				
    				res_msg = "selectInsertPlanDtl_I 오류 [" + res + "]";
    				txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage(res_msg);
					return response;
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
				return response;
			}

			if (m01.btn_selectRecProc_onclick.v_rePlmNo.length() < 13) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("v_rePlmNo SIZE 오류.");
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
						response.setResultMessage("modifyComUnpsec_U 오류 [" + res + "]");
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
						response.setResultCode("재AS예약 됐으므로 AS예정정보를 생성하였습니다.");
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
				response.setResultMessage("우편번호 자릿수를 변경해야 합니다. -> '정보기술팀 요청'");
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
				response.setResultMessage("executeFaAseqrem 오류");
				return response;
			}
			
			p02.ds_result.new_rem_seq = dataResult.getData1();
			
			res = erppraacctlistsaveMapper.modifyTcResMst_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResMst_U 오류 [" + res + "]");
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
					response.setResultMessage("modifyTaPlanMst_U 오류 [" + res + "]");
					return response;
				}   
			}
			
			res = erppraacctlistsaveMapper.modifyTcResDtl_I(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyTcResDtl_I 오류 [" + res + "]");
				return response;
			}
			
			res = erppraacctlistsaveMapper.modifyRptEnddt_U(p02.ds_result);
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("modifyRptEnddt_U 오류 [" + res + "]");
				return response;
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
				return response;
			}
			
			//AS처리결과시간
			params = new HashMap<String, Object>();
			params.put("plm_no", as_plm_no);
    		res = schedulemainlistMapper.updateAddAsEndTime(params);
    		if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("updateAddAsEndTime 오류 [" + res + "]");
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
