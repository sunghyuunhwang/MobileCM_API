package com.fursys.mobilecm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fursys.mobilecm.dto.MemberDto;
import com.fursys.mobilecm.mapper.STIMapper;
import com.fursys.mobilecm.mapper.UserMapper;
import com.fursys.mobilecm.role.Role;
import com.fursys.mobilecm.vo.FursysUser;

@Service
public class UserService  implements UserDetailsService {
	@Autowired
	UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("login_id", userId);
    	FursysUser user = userMapper.getUserInfo(params);
  
    	if (user == null){
    		throw new UsernameNotFoundException("login fail");
    	}
    	
    	Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        
        System.out.println(userId);
        
        return new User(user.getVnd_cd(), user.getUsr_pwd(), grantedAuthorities);
    }
}
