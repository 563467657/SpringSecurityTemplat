package com.securitytest.security.authentication.mobile;

import com.securitytest.security.authentication.CustomAuthenticationFailureHandler;
import com.securitytest.security.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

/**
 * 用于组合其他关于手机登录的组件
 */
@Component
public class MobileAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	private UserDetailsService mobileUserDetailsService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
		//获取容器中已经存在的AuthenticationManager对象,并传入MobileAuthenticationFilter里面
		mobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		
		//指定记住我功能
		mobileAuthenticationFilter.setRememberMeServices(http.getSharedObject(RememberMeServices.class));
		//session重复登录 管理
		mobileAuthenticationFilter.setSessionAuthenticationStrategy(http.getSharedObject(SessionAuthenticationStrategy.class));
		//传入 失败与成功处理器
		mobileAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
		mobileAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
		
		MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
		mobileAuthenticationProvider.setUserDetailService(mobileUserDetailsService);
		
		//将provide绑定到HttpSecurity之上，并将mobileAuthenticationFilter绑定到UsernamePasswordAuthenticationFilter之后
		http.authenticationProvider(mobileAuthenticationProvider)
				.addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
		
		;
	}
}
