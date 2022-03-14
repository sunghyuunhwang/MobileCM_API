package com.fursys.mobilecm.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.fursys.mobilecm.mapper.UserMapper;
import com.fursys.mobilecm.security.FursysPasswordEncoder;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.FursysUser;
import com.fursys.mobilecm.vo.mobile.response.UserInfoResponse;
import com.google.gson.Gson;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/api/mobile")
public class ApiMobileController {
	@Autowired UserMapper userMapper;
	@Autowired CRS0010_M01Mapper crs0010_p01Mapper;
	@Autowired ErpSigongAsMapper erpsigongasMapper;
	
	@Autowired private PlatformTransactionManager txManager;

	@Value("${spring.datasource.url}")
    private String mDBUrl;

	Gson gson = new Gson();
	
	@ApiOperation(value = "CheckPassWord", notes = "비밀번호 확인")
	@ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 5001, message = "비밀번호 확인 Fail !!")
	})
	@GetMapping("/CheckPassWord") 
	@RequestMapping(value="/CheckPassWord",method=RequestMethod.POST)
	public String CheckPassWord(
			@RequestParam(name="id", required=false) String id,
			@RequestParam(name="pw", required=false) String pw) { 
		
		BaseResponse loginResponse = new BaseResponse();
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		
		HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("login_id", id);
    	FursysUser user = userMapper.getUserInfo(params);
    	if (user == null){
    		loginResponse.setResultCode("5001");
    		loginResponse.setResultMessage("사용자 정보가 존재하지 않습니다.");
    		return gson.toJson(loginResponse);
    	}
    	
    	FursysPasswordEncoder fursysPasswordEncoder = new FursysPasswordEncoder();
    	
    	String encPw = fursysPasswordEncoder.encode(pw);
    	
    	if (encPw.matches(user.getCmjungsan_pwd())) {
    		loginResponse.setResultCode("200");
    	} else {    		
    		loginResponse.setResultCode("5001");
    		loginResponse.setResultMessage("비밀번호가 맞지 않습니다.");
    	}

    	int res = 0;
    	
    	res = userMapper.updateLoginDt(params);
    	
    	if (res < 1) {
    		
    		txManager.rollback(status);
    		loginResponse.setResultCode("5001");
    		loginResponse.setResultMessage("Login 오류");
    		return gson.toJson(loginResponse);
    		
    	} else {
    		
    		txManager.commit(status);
    	}
    	
    	
    	
		return gson.toJson(loginResponse);
	}
	
	@ApiOperation(value = "LoginPasswordChange", notes = "로그인 비밀번호변경")
	@ApiResponses({
        @ApiResponse(code = 200, message = "OK !!"),
        @ApiResponse(code = 5001, message = "로그인 비밀번호변경 실패 !!")
	})
	@GetMapping("/LoginPasswordChange") 
	@RequestMapping(value="/LoginPasswordChange",method=RequestMethod.POST)
	public String LoginPasswordChange(
			@RequestParam(name="id", required=false) String id,
			@RequestParam(name="pw", required=false) String pw,
			@RequestParam(name="gbn", required=false) String gbn) { 
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		BaseResponse response = new BaseResponse();
		FursysPasswordEncoder fursysPasswordEncoder = new FursysPasswordEncoder();    	
    	String encPw = fursysPasswordEncoder.encode(pw);
    	
		HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
    	params.put("pw", encPw);
    	
    	//System.out.println("pw="+pw);
    	//System.out.println("encPw="+encPw);
    	 
    	int res = 0;
    	if ("LOGIN".equals(gbn)) {
    		res = userMapper.updateLoginPassWord(params);
    	} else {
    		res = userMapper.updateCalPassWord(params);
    	}
    	if (res < 1){
    		txManager.rollback(status);
    		response.setResultCode("5001");
    		response.setResultMessage("비밀번호 변경에 실패하였습니다.");
    		return gson.toJson(response);
    	}
    	
    	txManager.commit(status);
    	response.setResultCode("200");
    	response.setResultMessage("비밀번호가 변경되었습니다.");
    	
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
			@RequestParam(name="id", required=false) String id,
			@RequestParam(name="phone_id", required=false) String phone_id) { 
		
		UserInfoResponse userInfoResponse = new UserInfoResponse();
		int res = 0;
		
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
        	user.setTmapkey(etc.get("TMAP_APPKEY").toString());
    		
        	params.put("com_scd", user.getCom_scd());
        	params.put("sti_cd", user.getSti_cd());
        	Map<String, Object> com_stec = userMapper.getCom_Stsec(params);
        	user.setCom_stsec(com_stec.get("COM_STSEC").toString());
        	
        	if (!"".equals(phone_id)) {
        		params.put("phone_id", phone_id);
	        	
        		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
        			        	
	        	//기존 테이블에 PhoneID가 없을수 있으므로, return check안함
	        	res = erpsigongasMapper.deleteUsedPhoneID(params);
	        	
	        	if (res < 0){
	        		txManager.rollback(status);
	        		userInfoResponse.setResultCode("5001");
	        		userInfoResponse.setResultMessage("PhoneId 변경에 실패하였습니다.(2)");
	        		return gson.toJson(userInfoResponse);
	        	}	        	
	        	txManager.commit(status);
	        	
	        	status = txManager.getTransaction(new DefaultTransactionDefinition());
	        	res = erpsigongasMapper.updatePhoneID(params);
	        	if (res < 1){
	        		txManager.rollback(status);
	        		userInfoResponse.setResultCode("5001");
	        		userInfoResponse.setResultMessage("PhoneId 변경에 실패하였습니다.");
	        		return gson.toJson(userInfoResponse);
	        	}
	        	txManager.commit(status);
	    		
        	}
        	
        	userInfoResponse.setUser(user);
    		userInfoResponse.setResultCode("200");
    	}
		System.out.println(userInfoResponse.getUser().toString());
		return gson.toJson(userInfoResponse);
	}
}
