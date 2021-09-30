package com.fursys.mobilecm.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.mapper.ORMMapper;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.utils.RestService;
import com.fursys.mobilecm.utils.RestService.RestServiceCallBack;
import com.fursys.mobilecm.vo.tms.reponse.TmsCenterListResponse;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/api")
public class AppController {
	@ApiOperation(value = "exam", notes = "exam.")
    @ApiImplicitParams({ 
    	@ApiImplicitParam (name = "name", value="value", required = true),
    	@ApiImplicitParam (name = "name2", value="value2", required = true),
    	@ApiImplicitParam (name = "name3", value="value3", required = true)
    	})
	@ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 500, message = "Internal Server Error !!"),
        @ApiResponse(code = 404, message = "Not Found !!")
	})
	@GetMapping("/demoapi") 
	public String demoapi() { 
		
		return "";
	}
	
	
}
