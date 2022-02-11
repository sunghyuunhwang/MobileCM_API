package com.fursys.mobilecm.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fursys.mobilecm.config.ServerInfo;
import com.fursys.mobilecm.config.TMapInfo;
import com.fursys.mobilecm.mapper.ORMMapper;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.mapper.UserMapper;
import com.fursys.mobilecm.utils.RestService;
import com.fursys.mobilecm.utils.RestService.RestServiceCallBack;
import com.fursys.mobilecm.vo.BaseResponse;
import com.fursys.mobilecm.vo.Coordinate;
import com.fursys.mobilecm.vo.CoordinateInfo;
import com.fursys.mobilecm.vo.CoordinateWrapper;
import com.fursys.mobilecm.vo.erp.ERPSti;
import com.fursys.mobilecm.vo.tms.reponse.TmsVehicleListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("/v1/areamaster")
public class WebController {
	@Autowired ORMMapper oRMMapper;
	@Autowired STIMapper sTIMapper;
	@Autowired UserMapper userMapper;
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);		
		return "greeting";   // html name
	}

	@GetMapping("/test")
	public String test(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);		
		return "test";   // html name
	}	
	
	@GetMapping("/sign")
	public String sign(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);		
		return "sign";   // html name
	}	
	
	@GetMapping("/areamina")
	public String areamina(@AuthenticationPrincipal User user, @RequestParam(name="name", required=false, defaultValue="World") String name, Model model, HttpServletRequest request) {
		
		System.out.println(user.getUsername());
		System.out.println("========================2222222===============");
        model.addAttribute("name", name);	
        
        HashMap<String, Object> params = new HashMap<String, Object>();
    	params = new HashMap<String, Object>();
    	params.put("vnd_cd", user.getUsername());
    	Map<String, Object> etc = userMapper.getUserEtc(params);

    	model.addAttribute("com_scd", etc.get("COM_SCD").toString());
    	model.addAttribute("sti_cd", etc.get("STI_CD").toString());
    	model.addAttribute("k_sti_cd", etc.get("K_STI_CD").toString());
        
    	System.out.println(etc.get("COM_SCD").toString());
    	System.out.println(etc.get("STI_CD").toString());
    	System.out.println(etc.get("K_STI_CD").toString());
    	
		return "areamina";   // html name
	}	
	
	@GetMapping("/scheduling")
	public String scheduling(@AuthenticationPrincipal User user, @RequestParam(name="name", required=false, defaultValue="World") String name, Model model, HttpServletRequest request) {
		
		System.out.println(user.getUsername());
		System.out.println("========================2222222===============");
        model.addAttribute("name", name);	
        
        HashMap<String, Object> params = new HashMap<String, Object>();
    	params = new HashMap<String, Object>();
    	params.put("vnd_cd", user.getUsername());
    	Map<String, Object> etc = userMapper.getUserEtc(params);

    	model.addAttribute("com_scd", etc.get("COM_SCD").toString());
    	model.addAttribute("sti_cd", etc.get("STI_CD").toString());
    	model.addAttribute("k_sti_cd", etc.get("K_STI_CD").toString());
        
    	System.out.println(etc.get("COM_SCD").toString());
    	System.out.println(etc.get("STI_CD").toString());
    	System.out.println(etc.get("K_STI_CD").toString());
    	
		return "scheduling";   // html name
	}	
	
	TmsVehicleListResponse tmsVehicleListResponse = null;
	BaseResponse tmpAreaesponse = new BaseResponse();
	@GetMapping("/area")
	public String area(@RequestParam(name="date", required=false) String date ,Model model) {
		Gson gson = new Gson();
        model.addAttribute("appKey", TMapInfo.appKey);	
        
        String baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/vehicleListUpdateFromERP";
        
		RestService.post(baseUrl,"", new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				System.out.println("result : " + result);
				tmpAreaesponse = gson.fromJson(result, BaseResponse.class);
			}
		});
		
		if (!tmpAreaesponse.getResultCode().equals("200")) {
			
		}
		
		baseUrl = ServerInfo.SERVER_URL + "/v1/api/tms/vehicleList";
        
		RestService.get(baseUrl, new RestServiceCallBack() {
			@Override
			public void onResult(String result) {
				tmsVehicleListResponse = gson.fromJson(result, TmsVehicleListResponse.class);
			}
		});
		
		model.addAttribute("vehicleList", tmsVehicleListResponse);
        
		return "area";  
	}
	
    @GetMapping("/vndBanpumStatus")
    public String vndBanpumStatus(@AuthenticationPrincipal User user) {
		return "vndBanpumStatus";
    	
    }
    @GetMapping("/migyeolReport")
    public String migyeolReport(@AuthenticationPrincipal User user) {
		return "migyeolReport";
    	
    }
    @GetMapping("/defectInfo")
    public String defectInfo(@AuthenticationPrincipal User user) {
		return "defectInfo";
    	
    }
    @GetMapping("/stimember")
    public String stimember(@AuthenticationPrincipal User user) {
		return "stimember";
    	
    }
    @GetMapping("/stiResultStatus")
    public String stiResultStatus(@AuthenticationPrincipal User user) {
		return "stiResultStatus";
    	
    }
}
