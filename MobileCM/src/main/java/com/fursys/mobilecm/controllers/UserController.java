package com.fursys.mobilecm.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fursys.mobilecm.dto.MemberDto;
import com.fursys.mobilecm.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserMapper userMapper;
	
	@Autowired private PlatformTransactionManager txManager;

    @GetMapping("/login")
    public String login(HttpSession session) {
        return "user/login";
    }
    
    @GetMapping("/login/result")
    public String result(@AuthenticationPrincipal User user) {
    	
    	TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
    	int res = 0;
    	
    	try {
        	//로그인 이력 추가, 20211025, hong
    		res = userMapper.insertLoginHistory(user.getUsername()); 
    		
    		if (res < 1)
    		{
        		txManager.rollback(status);
        		return "redirect:/v1/areamaster/scheduling";
     			 
    		}
    		
    	} catch (Exception e) {
			txManager.rollback(status);			
			return "redirect:/v1/areamaster/scheduling";
		}
		
		txManager.commit(status);
		return "redirect:/v1/areamaster/scheduling";

//        return "redirect:/v1/areamaster/areamina";
    }

    @GetMapping("/denied")
    public String dispDenied() {
        return "denied";
    }
}
