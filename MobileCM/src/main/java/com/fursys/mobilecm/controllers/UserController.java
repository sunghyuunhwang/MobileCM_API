package com.fursys.mobilecm.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fursys.mobilecm.dto.MemberDto;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        return "user/login";
    }
    
    @GetMapping("/login/result")
    public String result() {

    	return "redirect:/v1/areamaster/scheduling";
//        return "redirect:/v1/areamaster/areamina";
    }

    @GetMapping("/denied")
    public String dispDenied() {
        return "denied";
    }
}
