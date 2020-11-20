package com.securitytest.security.authentication;

import com.alibaba.fastjson.JSON;
import com.securitytest.base.result.JsonResult;
import com.securitytest.security.properties.LoginResponseType;
import com.securitytest.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器
 * 1.决定响应json还是跳转页面，或者认证成功后进行其他处理
 */
@Component("customAuthenticationSuccessHandler")
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SecurityProperties securityProperties;
	@Autowired(required = false) //容易中可以不需要有接口的实现，如果有则自动注入
	private AuthenticationSuccessListener authenticationSuccessListener;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
			, Authentication authentication) throws IOException, ServletException {
		if (authenticationSuccessListener != null) {
			//当认证成功之后调用此监听进行后续的处理，比如加载用户的权限菜单
			authenticationSuccessListener.successListener(httpServletRequest, httpServletResponse, authentication);
		}
		if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
			//认证成功后，响应JSON字符串
			JsonResult result = JsonResult.ok("认证成功");
			String resultJson = result.toJsonString();
			httpServletResponse.setContentType("application/json;charset=utf-8");
			httpServletResponse.getWriter().write(resultJson);
		}else {
			//重定向到上次请求的地址，引发跳转认证页面的地址
			logger.info("authentication:"+ JSON.toJSONString(authentication.toString()));
			super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
		}
		
	}
}
