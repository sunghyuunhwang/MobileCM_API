package com.fursys.mobilecm.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.mapper.CRS0010_M01Mapper;
import com.fursys.mobilecm.mapper.ErpSigongAsMapper;
import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.service.ApiErpSigongAsService;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/v1/api/erp_sigongas")
public class ApiErpSigongAsController {
	
	@Autowired ApiErpSigongAsService apiErpSigongAsService;	
	@Autowired ErpSigongAsMapper erpsigongasMapper;	
	@Autowired private PlatformTransactionManager txManager;
	private SqlSession sql;
	
	Gson gson = new Gson();
	boolean	isDeBug = false;	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ApiOperation(value = "erp_Test", notes = "TEST")
	@GetMapping("/erp_Test")
	@RequestMapping(value = "/erp_Test", method = RequestMethod.GET)
	public String erp_Test(
			@ApiParam(value = "MOVE_KM", required=true, example = "72")
			@RequestParam(name="move_km", required=true) int move_km
			) {
		       
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("move_km", move_km);
			
			int total_amt = 0;
			int total_move_km = 0;
			int total_move_km_new = 0;
			float flovar1 = 0.0f;
			
			if (move_km < 50) {
				System.out.println("1111111111111");
				total_move_km = 0 ;
			} else if (move_km >= 50  && move_km < 100) {
				System.out.println("2222222222222");
				total_move_km = (int) (move_km * 0.7);
				flovar1 = (float) (move_km * 0.7);
			} else if (move_km >= 100 && move_km < 150) {
				System.out.println("3333333333333");
				total_move_km = (int) (move_km * 1) ;
			} else if (move_km >= 150 && move_km < 200) {
				System.out.println("4444444444444");
				total_move_km = (int) (move_km * 1.4) ;
				System.out.println("5555555555555");
			} else if (move_km >= 200) {
				System.out.println("6666666666666");
				total_move_km = (int) (move_km * 2) ;
			} else {
				System.out.println("777777777777");
				total_move_km = (int) (move_km * 1) ;
			}
			
			System.out.println("total_move_km =====>" + total_move_km);
			System.out.println("flovar1 =====>" + flovar1);
					
			
        				
		} catch (Exception e) {
			System.out.println(e.toString());			
			response.setResultCode("5001");
			response.setResultMessage(e.toString());
			return gson.toJson(response);
		}
		
		response.setResultCode("200");
		System.out.println(response.toString());	
		return gson.toJson(response);
	}
	
	@ApiOperation(value = "erp_UpdatePhoneID", notes = "Phone ID UPDATE")
	@GetMapping("/erp_UpdatePhoneID")
	@RequestMapping(value = "/erp_UpdatePhoneID", method = RequestMethod.GET)
	public String erp_UpdatePhoneID(
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String com_scd,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String sti_cd,
			@ApiParam(value = "PHONE_ID", required=true, example = "ciOBoXqaQ1qPDcVL5CraXk:APA91bFaBEPkasZlm0L9e2d_C6QYYDj6CTXs6XHT3QlPCiMOee47SE-a_rb0VzQAc_OCsuR0rVzQKNJRZ3DYcUHsVVqs7pfor2OQuc0RcKiOsqvVJc8g7cp3AHDfWcaWKSo6Uv9FCy--")
			@RequestParam(name="phone_id", required=true) String phone_id
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("sti_cd", sti_cd);
			params.put("com_scd", com_scd);
			params.put("phone_id", phone_id);
					
			//기존 테이블에 PhoneID가 없을수 있으므로, return check안함
			res = erpsigongasMapper.deleteUsedPhoneID(params);        	
			txManager.commit(status);
			
			status = txManager.getTransaction(new DefaultTransactionDefinition());
        	res = erpsigongasMapper.updatePhoneID(params);
        	if (res < 1){
        		txManager.rollback(status);
        		response.setResultCode("5001");
        		response.setResultMessage("PhoneId 변경에 실패하였습니다.");
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
	
	@ApiOperation(value = "erp_NotifyList", notes = "알림리스트")
	@GetMapping("/erp_NotifyList")  
	public String erp_NotifyList (
			@ApiParam(value = "STI_CD", required=true, example = "YA521")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("sti_cd", as_sti_cd);
        
        ArrayList<DataResult> allItems = apiErpSigongAsService.erp_NotifyList(params);
        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_insertSigongOverTimeWork", notes = "일룸 저녁시간 자동정산")
	@GetMapping("/erp_insertSigongOverTimeWork")
	@RequestMapping(value = "/erp_insertSigongOverTimeWork", method = RequestMethod.GET)
	public String erp_insertSigongOverTimeWork(
			@ApiParam(value = "PLM_NO", required=true, example = "P202103090529")
			@RequestParam(name="plm_no", required=true) String as_plm_no,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "PLM_CDT", required=true, example = "20210309")
			@RequestParam(name="plm_cdt", required=true) String as_plm_cdt,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60P01")
			@RequestParam(name="com_brand", required=true) String as_com_brand,
			@ApiParam(value = "REM_DT", required=true, example = "20210309")
			@RequestParam(name="rem_dt", required=true) String as_rem_dt,
			@ApiParam(value = "REM_SEQ", required=true, example = "PC0827")
			@RequestParam(name="rem_seq", required=true) String as_rem_seq
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		DataResult dataResult = new DataResult();
		String over_time = "", rem_ftm = "";
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();		
			params.put("plm_no", as_plm_no);
			params.put("sti_cd", as_sti_cd);

			dataResult = erpsigongasMapper.selectSigongWorkTimeCheck(params);
    		if (dataResult != null) {
    			over_time = dataResult.getData1();
    		} else {
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("시공시작시간 조회에 오류가 발생했습니다.");
				return gson.toJson(response);
    		}
			
			params.put("rem_dt", as_rem_dt);
			params.put("rem_seq", as_rem_seq);

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
					return gson.toJson(response);
	    		}
				
				params.put("com_scd", as_com_scd);
				params.put("plm_cdt", as_plm_cdt);
				params.put("com_brand", as_com_brand);        			
			
				res = erpsigongasMapper.insertSigonWorkTimeOverAcc(params);
				if (res < 1) { 
	    			txManager.rollback(status);
					response.setResultCode("5001");
					response.setResultMessage("insertSigonWorkTimeOverAcc 오류 [" + res + "]");
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
	
	@ApiOperation(value = "erp_insertSigongWallFix", notes = "일룸 벽고정 자동정산")
	@GetMapping("/erp_insertSigongWallFix")
	@RequestMapping(value = "/erp_insertSigongWallFix", method = RequestMethod.GET)
	public String erp_insertSigongWallFix(
			@ApiParam(value = "PLM_NO", required=true, example = "P202103090529")
			@RequestParam(name="plm_no", required=true) String as_plm_no,
			@ApiParam(value = "STI_CD", required=true, example = "YA551")
			@RequestParam(name="sti_cd", required=true) String as_sti_cd,
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "PLM_CDT", required=true, example = "20210309")
			@RequestParam(name="plm_cdt", required=true) String as_plm_cdt,
			@ApiParam(value = "COM_BRAND", required=true, example = "T60P01")
			@RequestParam(name="com_brand", required=true) String as_com_brand
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();		
			params.put("plm_no", as_plm_no);
			params.put("sti_cd", as_sti_cd);

			res = erpsigongasMapper.insertSigongWallFix(params);
			if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertWallFix 오류 [" + res + "]");
				return gson.toJson(response);
    		}
			
			params.put("com_scd", as_com_scd);
			params.put("plm_cdt", as_plm_cdt);
			params.put("com_brand", as_com_brand);        			
			
			res = erpsigongasMapper.insertSigongWallFixAcc(params);
			if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertWallFixAcc 오류 [" + res + "]");
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
		
	@ApiOperation(value = "erp_FcmSendNotify", notes = "FCM Notify")
	@GetMapping("/erp_FcmSendNotify")
	@RequestMapping(value="/erp_FcmSendNotify",method=RequestMethod.POST)
	public String erp_FcmSendNotify (
			@ApiParam(value = "SEND_FROM_SYSTEM", required=true, example = "MOBILECM")
			@RequestParam(name="send_from_system", required=true) String as_send_from_system,
			@ApiParam(value = "SEND_TO_SYSTEM", required=true, example = "MOBILECM")
			@RequestParam(name="send_to_system", required=true) String as_send_to_system,
			@ApiParam(value = "COM_SCD", required=true, example = "C16YA")
			@RequestParam(name="com_scd", required=true) String as_com_scd,
			@ApiParam(value = "TITLE", required=true, example = "This is Title")
			@RequestParam(name="title", required=true) String as_title,
			@ApiParam(value = "MESSAGE", required=true, example = "This is First Message.")
			@RequestParam(name="message", required=true) String as_message,
			@ApiParam(value = "USER_ID", required=true, example = "Ya521")
			@RequestParam(name="user_id", required=true) String as_user_id
		) { 	
        
		BaseResponse response = new BaseResponse();
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("send_from_system", as_send_from_system);
        params.put("send_to_system", as_send_to_system);
        params.put("com_scd", as_com_scd);
        params.put("title", as_title);
        params.put("message", as_message);
        params.put("user_id", as_user_id);
    
		response = apiErpSigongAsService.erp_Fcm_SendNotify(params);

		return gson.toJson(response);
		
	}
	
	@ApiOperation(value = "erp_AttachFileList", notes = "시공,AS첨부파일리스트")
	@GetMapping("/erp_AttachFileList")  
	public String erp_AttachFileList (
			@ApiParam(value = "ATTCH_FILE_ID", required=true, example = "cresultF202109060125")
			@RequestParam(name="attch_file_id", required=true) String as_attch_file_id,
			@ApiParam(value = "ATTCH_DIV_CD", required=true, example = "C")
			@RequestParam(name="attch_div_cd", required=true) String as_attch_div_cd
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("attch_file_id", as_attch_file_id);
        params.put("attch_div_cd", as_attch_div_cd);
		
        ArrayList<ERPAttachFileList> allItems = apiErpSigongAsService.erp_AttachFileList(params);
        
		return gson.toJson(allItems);
	}
	
	@ApiOperation(value = "erp_AttachFileDelete", notes = "첨부파일 삭제")
	@GetMapping("/erp_AttachFileDelete")
	@RequestMapping(value = "/erp_AttachFileDelete", method = RequestMethod.GET)
	public String erp_reqCooperationCancel(
			@RequestParam(name = "attch_file_id", required = false) String attch_file_id,	
			@RequestParam(name = "attch_div_cd", required = false) String attch_div_cd,
			@RequestParam(name = "attch_file_snum", required = false) int attch_file_snum
			) {
		       
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		int res = 0;
		BaseResponse response = new BaseResponse();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attch_file_id", attch_file_id);
			params.put("attch_div_cd", attch_div_cd);
			params.put("attch_file_snum", attch_file_snum);
			
			res = erpsigongasMapper.deleteAttachFile(params);			
			if (res < 1) {
				txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("deleteAttachFile 오류 [" + res + "]");
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
	
}
