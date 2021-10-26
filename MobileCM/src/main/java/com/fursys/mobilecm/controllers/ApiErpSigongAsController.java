package com.fursys.mobilecm.controllers;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.DataResult;
import com.fursys.mobilecm.vo.erp.ERPAttachFileList;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/v1/api/erp_sigongas")
public class ApiErpSigongAsController {
	
	@Autowired ApiErpService apiErpService;
	@Autowired ErpSigongAsMapper erpSigongAsMapper;
	@Autowired CRS0010_M01Mapper crs0010_m01Mapper;
	
	@Autowired private PlatformTransactionManager txManager;
	
	Gson gson = new Gson();
	boolean	isDeBug = false;	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	
	@ApiOperation(value = "erp_insertWallFix", notes = "일룸 벽고정 자동정산")
	@GetMapping("/erp_insertWallFix")
	@RequestMapping(value = "/erp_insertWallFix", method = RequestMethod.GET)
	public String erp_insertWallFix(
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

			res = crs0010_m01Mapper.insertWallFix(params);
			if (res < 1) { 
    			txManager.rollback(status);
				response.setResultCode("5001");
				response.setResultMessage("insertWallFix 오류 [" + res + "]");
				return gson.toJson(response);
    		}
			
			params.put("com_scd", as_com_scd);
			params.put("plm_cdt", as_plm_cdt);
			params.put("com_brand", as_com_brand);        			
			
			res = crs0010_m01Mapper.insertWallFixAcc(params);
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
        
		response = apiErpService.erp_Fcm_SendNotify(params);
				
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
		
        ArrayList<ERPAttachFileList> allItems = apiErpService.erp_AttachFileList(params);
        
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
		DataResult dataResult = new DataResult();
		
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attch_file_id", attch_file_id);
			params.put("attch_div_cd", attch_div_cd);
			params.put("attch_file_snum", attch_file_snum);
			
			res = erpSigongAsMapper.deleteAttachFile(params);			
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
