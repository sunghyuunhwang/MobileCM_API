package com.fursys.mobilecm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fursys.mobilecm.lib.MobileCMLib;
import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpCalculateMoneyMapper;
import com.fursys.mobilecm.mapper.ErpPraAcctListSaveMapper;
import com.fursys.mobilecm.mapper.ErpSigongAsMapper;
import com.fursys.mobilecm.mapper.ScheduleMainListMapper;
import com.fursys.mobilecm.service.ApiErpSigongAsService;
import com.fursys.mobilecm.utils.FcmMessage;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAddActList;
import com.fursys.mobilecm.vo.erp.ERPAsItemReport;
import com.fursys.mobilecm.vo.erp.ERPAsReport;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPConstructionItemPage;
import com.fursys.mobilecm.vo.erp.ERPDeliveryItemList;
import com.fursys.mobilecm.vo.erp.ERPFcmNotify;
import com.fursys.mobilecm.vo.erp.ERPHappyCall;
import com.fursys.mobilecm.vo.erp.ERPPendencyList;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;
import com.fursys.mobilecm.vo.erp.ERPSigongItemReport;
import com.fursys.mobilecm.vo.erp.ERPSigongReport;
import com.fursys.mobilecm.vo.erp.ERPTrinfList;
import com.fursys.mobilecm.vo.mobile.response.AsReportResponse;
import com.fursys.mobilecm.vo.mobile.response.PendencyDetailListResponse;
import com.fursys.mobilecm.vo.mobile.response.SigongReportResponse;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Service
public class ApiErpSigongAsServiceImpl implements ApiErpSigongAsService {
	@Autowired ErpSigongAsMapper erpsigongasMapper;
	@Autowired ErpPraAcctListSaveMapper erppraacctlistsaveMapper;
	@Autowired CRS0010_M01Mapper crs0010_m01Mapper; 
	@Autowired ScheduleMainListMapper schedulemainlistMapper;
	@Autowired ErpCalculateMoneyMapper erpCalculateMoneyMapper;
	
	@Autowired private PlatformTransactionManager txManager;
	Gson gson = new Gson();

	@Override
	public BaseResponse erp_Fcm_SendCommand(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		HashMap<String, Object> params;
		ArrayList<ERPPushMessage> allItems;
		int res = 1;
		int send = 0;
		
		try {
			
			String DELIMETER = "";

			String as_command = "MobileCM.COMMAND";
			String as_send_from_system = (String) param.get("send_from_system");
			String as_send_to_system = (String) param.get("send_to_system");
			String as_com_scd = (String) param.get("com_scd");
			String as_action = (String) param.get("action");
			String as_action_data = (String) param.get("action_data");
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
			
			if ("ALL".equals(as_com_scd)) {
				//allItems = erpsigongasMapper.selectPhoneIDAll(params);
				allItems = erpsigongasMapper.selectPhoneID(params);				
			} else {
				allItems = erpsigongasMapper.selectPhoneID(params);
			}
			
			if (allItems != null) {
    			for(int i=0; i<allItems.size(); i++) {
    				send++;
    				send_text = new StringBuffer();
    				send_text.append(as_user_id);
    				send_text.append(DELIMETER + allItems.get(i).getSti_cd());
    				send_text.append(DELIMETER + as_action);
    				send_text.append(DELIMETER + as_action_data);
    				send_text.append(DELIMETER + send_dt);
    				
    	    		//System.out.println("send_text ="  + send_text.toString());
    	    		
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

		response.setResultCount(String.format("%d", send));
		txManager.commit(status);		
		response.setResultCode("200");		
		return response;

	}	
	
	@Override		
	public ArrayList<ERPTrinfList> erp_selectActItemList(HashMap<String, Object> param) {
		ArrayList<ERPTrinfList> allitems;
		HashMap<String, Object> params;
		
		try {
			String orm_no = (String) param.get("orm_no");
			String com_agsec = (String) param.get("com_agsec");
			String com_brand = (String) param.get("com_brand");
			String trs_sec = (String) param.get("trs_sec");
			String tri_inm = "%" + (String) param.get("tri_inm") + "%";
			
			params = new HashMap<String, Object>();
			params.put("orm_no", orm_no);
			params.put("com_agsec", com_agsec);
			params.put("com_brand", com_brand);
			params.put("trs_sec", trs_sec);
			params.put("tri_inm", tri_inm);
			
			allitems = erpsigongasMapper.selectActItemList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
	
	@Override		
	public ArrayList<ERPAddActList> erp_addActList(HashMap<String, Object> param) {
		ArrayList<ERPAddActList> allitems;
		HashMap<String, Object> params;
		
		try {
			String plm_no = (String) param.get("plm_no");
			String com_ssec = (String) param.get("com_ssec");
			
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_ssec", com_ssec);
			
			allitems = erpsigongasMapper.selectAddActList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
	
	@Override
	public BaseResponse erp_happyCallKakao(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		ERPHappyCall happycall = new ERPHappyCall();
		HashMap<String, Object> params;		
		int res = 1;
		
		try {
			String message = "", attachmentUrl = "", fromNm = "", fromNo = "", biztalkmessage = "", templateCode = "", senderkey = "";
			String companyCd = "T01B", client_ssec = "", dist_cd = "000011", orm_purcst = "", send_dt = "", process_cd = "", attachmentName = "";
			long send_seq = 0, ctm_url_key = 0;
			int notSendCnt = 0;
			
			String plm_no = (String) param.get("plm_no");
			String rpt_no = (String) param.get("rpt_no");
			String rpt_seq = (String) param.get("rpt_seq");
			String rem_dt = (String) param.get("rem_dt");
			String rem_seq = (String) param.get("rem_seq");
			String com_ssec = (String) param.get("com_ssec");
			String com_agsec = (String) param.get("com_agsec");
			String com_brand = (String) param.get("com_brand");
			String ctm_nm = (String) param.get("ctm_nm");
			String ctm_hp = (String) param.get("ctm_hp");
			String sti_cd = (String) param.get("sti_cd");
			String sot_cd = "";
			
			// 수신거부 고객 체크
			notSendCnt = erpsigongasMapper.selectNotSendCtm(ctm_hp);
			if(notSendCnt > 0) {
				txManager.rollback(status);
				response.setResultCode("200");				
				return response;				
			}
			
			// AS건 일단 발송하지 않음.
			if ("C18C".equals(com_ssec)) {
				sot_cd = "C65001";
			} else {
				sot_cd = "C65002";
				txManager.rollback(status);
				response.setResultCode("200");				
				return response;
			}
			
			params = new HashMap<String, Object>();
			params.put("plm_no", plm_no);
			params.put("com_ssec", com_ssec);
			params.put("com_agsec", com_agsec);
			params.put("com_brand", com_brand);
			params.put("sot_cd", sot_cd);
			params.put("companyCd", companyCd);
			params.put("dist_cd", dist_cd);
			params.put("rpt_no", rpt_no);
			params.put("rpt_seq", rpt_seq);
			params.put("rem_dt", rem_dt);
			params.put("rem_seq", rem_seq);
			
			happycall = erpsigongasMapper.selectHappyCallMessage(params);
			if (happycall == null) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_happyCallKakao selectHappyCallMessage 오류 [" + res + "]");
				return response;
			}
			
			biztalkmessage = happycall.getBiztalkmessage();
			message = happycall.getMessage();
			attachmentUrl = happycall.getAttachmentUrl();
			fromNm = happycall.getFromNm();
			fromNo = happycall.getFromNo();
			client_ssec = happycall.getClient_ssec();
			send_seq = happycall.getSend_seq();
			orm_purcst = happycall.getOrm_purcst();
			send_dt = happycall.getSend_dt();
			attachmentName = happycall.getAttachmentName();
			templateCode = happycall.getTemplateCode();			

			System.out.println(String.format("happycall=[%s]", happycall.toString()));
/*			
			System.out.println(String.format("orm_purcst=[%s]", orm_purcst));
			System.out.println(String.format("ctm_nm=[%s]", ctm_nm));
			System.out.println(String.format("send_dt=[%s]", send_dt));
			System.out.println(String.format("send_seq=[%d]", send_seq));
			System.out.println(String.format("attachmentName=[%s]", attachmentName));
			System.out.println(String.format("templateCode=[%s]", templateCode));
*/			
			biztalkmessage = biztalkmessage.replace("{1}", ctm_nm);
			try {
				biztalkmessage = biztalkmessage.replace("{2}", send_dt.substring(0,4) + "년"+ send_dt.substring(4,6) + "월" + send_dt.substring(6,8) + "일");
			} catch (Exception e) {
				biztalkmessage = biztalkmessage.replace("{2}", send_dt);
			}
			
			System.out.println(String.format("biztalkmessage=[%s]", biztalkmessage));				    	
	    	
	    	if("T60F01".equals(com_brand)) {	//퍼시스브랜드
	    		senderkey = "8615d8c99db78a8ec996e0c0d659ed11313eb781";
	    		
	    	} else if("T60I01".equals(com_brand)) {	//일룸브랜드
	    		senderkey = "dbf8669a88dd7926fd653ff3ff9b23d331fbbb4c";
	    		
	    	} else if("T60I02".equals(com_brand)) {	//데스커브랜드
	    		senderkey = "9917d09567d2ebf1acc89662d7f9ff10db1488d7";
	    		
	    	} else if("T60I03".equals(com_brand)) {	//슬로우브랜드	    		
	    		senderkey = "3ed320702f733d0b5a31e99a3ba931d9f2f9f960";
	    		
	    	} else if("T60P01".equals(com_brand)) {	//시디즈브랜드	    		
	    		senderkey = "6b94c758a1f689223024765ae6e2b0aede351955";
	    		      		
	    	} else if("T60P02".equals(com_brand)) {	//알로소브랜드	    		
	    		senderkey = "a75beb8ed88e9fa60be384f82eeeafe2f3dccc9a";

	    	} else {
	    		txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_happyCallKakao 등록되지 않은 브랜드 오류 [" + com_brand + "]");
				return response;
	    	}
	    	           
            params.put("companyCd", companyCd);
            params.put("client_ssec", client_ssec);
            params.put("send_seq", send_seq);            
            //params.put("quest_seq", 1);            
            //params.put("send_title", "");	//일시공1207
            params.put("resv_yn", "N");
            params.put("msg_gubun_cd", "C62008");
            params.put("biztalkmessage", biztalkmessage);
            params.put("message", message);
            params.put("attachmentUrl", attachmentUrl);
            params.put("sti_cd", sti_cd);
            
            if (send_seq == 0) {
            	txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("해피콜 전송 기준정보가 없습니다. 서비스관리팀에 문의하세요.");
				return response;
            }

            //System.out.println(String.format("send_seq=[%d]", send_seq));		
            
            //데이타가 있을경우 해피콜전송하지 않음
			dataResult = erpsigongasMapper.selectHappyCallSendCheck(params);
			if (dataResult != null) {
				txManager.rollback(status);
				response.setResultCode("200");				
				return response;
			}
		
			params.put("orm_purcst", orm_purcst);
			params.put("ctm_hp", ctm_hp);
			params.put("process_cd", process_cd);
			
            res = erpsigongasMapper.insertHappyCallDetail(params);
			if (res < 1) {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_happyCallKakao insertHappyCallDetail 오류 [" + res + "]");
				return response;
			}
			ctm_url_key = (int) params.get("ctm_url_key");
			
			//System.out.println(String.format("ctm_url_key=[%d]", ctm_url_key));		
			
			String v_rtnEncrypt = MobileCMLib.makeEncryptValue(String.format("%s", ctm_url_key));
			
			//System.out.println(String.format("v_rtnEncrypt=[%s]", v_rtnEncrypt));		
			
			//개발사이트
			//attachmentUrl = "http://192.9.202.101:8080/customer/happycall.do?id=";
					
			attachmentUrl = attachmentUrl + v_rtnEncrypt;
						
			System.out.println(String.format("attachmentUrl=[%s]", attachmentUrl));		
								
			JSONArray jArray = new JSONArray();
			JSONObject sendList = new JSONObject();	    	 
	    	JSONObject sObject = new JSONObject();
			
	    	sendList.put("authKey", "D62D413F25CD43B3BD06636F2B3F570ABFB5008BD727901E341F041448D22C3A6593D58D45C68E60171F7FB2B2C345459361A08D20298BAE6A3A1B74196A95C3");

			sObject.put("sendDiv", "BIZTALK" );
			sObject.put("fromNm", fromNm );
			sObject.put("toNm", ctm_nm );
			sObject.put("fromNo", fromNo ); 
			sObject.put("toNo", ctm_hp);
			sObject.put("companyCd", companyCd );		        	 
			sObject.put("fstUsr", sti_cd );
			sObject.put("systemNm", "MOBILE CM" );
			sObject.put("reserveDiv", "I");
			sObject.put("reserveDt", "" );
			sObject.put("keyNo", plm_no);
			sObject.put("message", message);
			sObject.put("bizTalkMessage", biztalkmessage );
			sObject.put("templateCode", templateCode );
			sObject.put("senderKey", senderkey);
			sObject.put("attachmentType", "button");
			sObject.put("attachmentName", attachmentName);
			sObject.put("attachmentUrl", attachmentUrl);
			sObject.put("comBrd", com_brand);
			sObject.put("keyNoGubun", "TO4P");
					
			jArray.add(sObject);

			sendList.put("list" ,jArray); 
			
			System.out.println(String.format("RestCallObject=[%s]", sendList.toString()));	

			BaseResponse kakao_res = MobileCMLib.RestCall("https://msg-api.fursys.com/v1/api/message/SendMsg", sendList);	
        	if (!"200".equals(kakao_res.getResultCode())) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("만족도조사 해피콜 전송결과 오류  [" + kakao_res.getResultMessage() + "]");
				return response;
			}
       	 	
	       	res = erpsigongasMapper.insertHappyCallAnswer(params);
	    	if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_happyCallKakao insertHappyCallAnswer 오류 [" + res + "]");
				return response;
			}
	    	
	    	res = erpsigongasMapper.updateHappyCallCount(params);
	    	if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_happyCallKakao updateHappyCallCount 오류 [" + res + "]");
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
	public BaseResponse erp_sigongDelivery(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;		
		int res = 1;
		
		try {
			
			params = new HashMap<String, Object>();
			
    		res = erpsigongasMapper.erp_sigongDelivery(params);
    		if (res < 1) {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_sigongDelivery 오류 [" + res + "]");
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
	public AsReportResponse erp_selectAsReport(HashMap<String, Object> param) {
		AsReportResponse reponse = new AsReportResponse();
		ERPAsReport as;	
    	ArrayList<ERPAsItemReport> list;
    	HashMap<String, Object> params;
		
		try {
			String orm_no = (String) param.get("orm_no");
			
			params = new HashMap<String, Object>();
	        params.put("orm_no", orm_no);
	            		
	        as = erpsigongasMapper.erp_selectAsReport(params);
	        
	        list = erpsigongasMapper.erp_selectAsItemReport(params);
	        
	        reponse.setAs(as);
	        reponse.setList(list);
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return reponse;
	}

	@Override		
	public SigongReportResponse erp_selectSigongReport(HashMap<String, Object> param) {
		SigongReportResponse reponse = new SigongReportResponse();
		ERPSigongReport sigong;	
    	ArrayList<ERPSigongItemReport> list;
    	HashMap<String, Object> params;
		
		try {
			String plm_no = (String) param.get("plm_no");
			
			params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	            		
	        sigong = erpsigongasMapper.erp_selectSigongReport(params);
	        
	        list = erpsigongasMapper.erp_selectSigongItemReport(params);
	        
	        reponse.setSigong(sigong);
	        reponse.setList(list);
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return reponse;
	}
	
	@Override		
	public ArrayList<ERPDeliveryItemList> erp_selectDeliveryItemList(HashMap<String, Object> param) {
		ArrayList<ERPDeliveryItemList> allitems;
		HashMap<String, Object> params;
		
		try {
			String sti_cd = (String) param.get("sti_cd");
			String rem_dt = (String) param.get("rem_dt");
			
			params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);
	        params.put("rem_dt", rem_dt);

	        allitems = erpsigongasMapper.erp_selectDeliveryItemList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
	
	@Override		
	public PendencyDetailListResponse erp_selectPendencyDetailList(HashMap<String, Object> param) {
		PendencyDetailListResponse reponse = new PendencyDetailListResponse();
		DataResult dataResult = new DataResult();
		ArrayList<ERPAttachFileList> file_list;
		ArrayList<ERPConstructionItemPage> item_list;
		HashMap<String, Object> params;
		
		try {
			String plm_no = (String) param.get("plm_no");
			String attch_file_id = (String) param.get("attch_file_id");
			String attch_div_cd = (String) param.get("attch_div_cd");
			
			params = new HashMap<String, Object>();
	        params.put("plm_no", plm_no);
	        params.put("attch_file_id", attch_file_id);
	        params.put("attch_div_cd", attch_div_cd);

	        dataResult = erpsigongasMapper.selectLgsStat(params);
    		
	        file_list = erpsigongasMapper.selectSigongAttachFileList(params);
	        
	        item_list = erpsigongasMapper.selectPendencyItemList(params);
	        
	        reponse.setDataResult(dataResult);
	        reponse.setFile_list(file_list);
	        reponse.setItem_list(item_list);
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return reponse;
	}
		
	@Override		
	public ArrayList<ERPPendencyList> erp_selectPendencyList(HashMap<String, Object> param) {
		ArrayList<ERPPendencyList> allitems;
		HashMap<String, Object> params;
		
		try {
			String com_scd = (String) param.get("com_scd");
			String sti_cd = (String) param.get("sti_cd");
			String fr_date = (String) param.get("fr_date");
			String to_date = (String) param.get("to_date");
			
			params = new HashMap<String, Object>();
			params.put("com_scd", com_scd);
			params.put("sti_cd", sti_cd);
	        params.put("fr_date", fr_date);
	        params.put("to_date", to_date);

	        allitems = erpsigongasMapper.selectPendencyList(params);		
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}		
		return allitems;
	}
		
	@Override
	public BaseResponse erp_finishScheduleResult(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;		
		int res = 1;
		
		try {
			
			String seq_no = (String) param.get("seq_no");
			String result = (String) param.get("result");
			String remark = (String) param.get("remark");
						
			params = new HashMap<String, Object>();
			params.put("seq_no", seq_no);
			params.put("result", result);
			params.put("remark", remark);
			
    		res = erpsigongasMapper.finishScheduleHistory(params);
    		if (res < 1) {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_finishScheduleResult finishScheduleHistory 오류 [" + res + "]");
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
	public BaseResponse erp_startScheduleResult(HashMap<String, Object> param) {
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		HashMap<String, Object> params;		
		int res = 1;
		
		try {
			String schedule_server = (String) param.get("schedule_server");
			String schedule_id = (String) param.get("schedule_id");
			String schedule_name = (String) param.get("schedule_name");
			String result = (String) param.get("result");
			String remark = (String) param.get("remark");
			
			if (result == null) result = "";
			if (remark == null) remark = "";
			
			params = new HashMap<String, Object>();
			params.put("schedule_server", schedule_server);
			params.put("schedule_id", schedule_id);
			params.put("schedule_name", schedule_name);
			params.put("result", result);
			params.put("remark", remark);
			
    		res = erpsigongasMapper.startScheduleHistory(params);
    		if (res < 1) {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("erp_startScheduleResult startScheduleHistory 오류 [" + res + "]");
				return response;
			}
    		
    		int seq_no = (int) params.get("seq_no");
    		
    		System.out.println(String.format("erp_startScheduleResult return is %d", seq_no));
    		
    	    response.setResultCount(String.format("%d", seq_no));
    		
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
	public BaseResponse erp_selectScheduledtFcmNotifyList(HashMap<String, Object> param) {
		BaseResponse response = new BaseResponse();
		ArrayList<ERPFcmNotify> allitems;
		HashMap<String, Object> params;
		
		try {			
			params = new HashMap<String, Object>();
	        
	        allitems = erpsigongasMapper.selectScheduledtFcmNotifyList(params);		
	        for(int i=0; i<allitems.size(); i++) {
	        	params = new HashMap<String, Object>();
	        	params.put("send_from_system", allitems.get(i).getSend_from_system());
	            params.put("send_to_system", allitems.get(i).getSend_to_system());
	            params.put("com_scd", allitems.get(i).getCom_scd());
	            params.put("title", allitems.get(i).getTitle());
	            params.put("message", allitems.get(i).getMessage());
	            params.put("user_id", "SCHEDULED_SYSTEM");
	            
	        	response = erp_Fcm_SendNotify(params);
	        	
	        	if (!"200".equals(response.getResultCode()) && !("5001".equals(response.getResultCode()) && "전송 대상자가 없습니다.".equals(response.getResultMessage()))) {	        		
	        		return response;
	        	}	        	
	        }
	        
		} catch (Exception e) {
			System.out.println(e.toString());
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return response;
		}	
		
		response.setResultCode("200");		
		return response;
	}
	
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
		int send = 0;
		
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
    				send++;
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
    				
    	    		//System.out.println("send_text ="  + send_text.toString());
    	    		
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

		response.setResultCount(String.format("%d", send));
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
	

}
