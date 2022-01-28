package com.fursys.mobilecm.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fursys.mobilecm.security.FursysPasswordEncoder;
import com.fursys.mobilecm.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private final UserService userDetailsService;
	
	@Value("${security.enable-csrf}")
    private boolean csrfEnabled;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/lib/**", "/templates/**","/v2/api-docs", "/configuration/ui",
                "/swagger-resources/**","/configuration/security","/swagger-ui.html", "/webjars/**","/swagger/**","/_sign/**");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
        		.antMatchers("/user/login").permitAll()
        		.antMatchers("/v1/api/**").permitAll()
        		.antMatchers("/v1/areamaster/**").hasRole("MEMBER")
		        .anyRequest()
		        .authenticated()
            .and() 
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/user/login/result")
            .and() 
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/user/login")
                .deleteCookies("JSESSIONID", "remember-me")
                .invalidateHttpSession(true)
             .and()
             	.rememberMe()
             	.alwaysRemember(true)
             	.userDetailsService(userDetailsService);
        if(!csrfEnabled) {
        	http.csrf().disable();
        }
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new FursysPasswordEncoder());
    }
}
