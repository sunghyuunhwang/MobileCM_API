package com.fursys.mobilecm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpCalculateMoneyMapper;
import com.fursys.mobilecm.mapper.ErpPraAcctListSaveMapper;
import com.fursys.mobilecm.mapper.ErpSigongAsMapper;
import com.fursys.mobilecm.mapper.ScheduleMainListMapper;
import com.fursys.mobilecm.service.ApiErpSigongAsService;
import com.fursys.mobilecm.utils.FcmMessage;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.fursys.mobilecm.vo.erp.ERPFcmNotify;
import com.fursys.mobilecm.vo.erp.ERPPushMessage;
import com.google.gson.Gson;

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
