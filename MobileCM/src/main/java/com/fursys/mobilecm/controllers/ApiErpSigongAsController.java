package com.fursys.mobilecm.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.service.ApiErpService;
import com.fursys.mobilecm.vo.erp.ERPSigongAttachFileList;
import com.google.gson.Gson;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/v1/api/erp_sigongas")
public class ApiErpSigongAsController {
	
	@Autowired ApiErpService apiErpService;
	@Autowired private PlatformTransactionManager txManager;
	
	Gson gson = new Gson();
	boolean	isDeBug = false;	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ApiOperation(value = "erp_sigongAttachFileList", notes = "시공첨부파일리스트")
	@GetMapping("/erp_sigongAttachFileList")  
	public String erp_sigongAttachFileList (
			@ApiParam(value = "PLM_NO", required=true, example = "F202109060125")
			@RequestParam(name="plm_no", required=true) String as_plm_no
		) { 	
        
		HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("plm_no", as_plm_no);
		
        ArrayList<ERPSigongAttachFileList> allItems = apiErpService.erp_sigongAttachFileList(params);
        
		return gson.toJson(allItems);
	}
	
	
}
