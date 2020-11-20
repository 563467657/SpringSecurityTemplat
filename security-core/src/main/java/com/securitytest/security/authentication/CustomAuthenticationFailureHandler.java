package com.securitytest.security.authentication;

import com.securitytest.base.result.JsonResult;
import com.securitytest.security.properties.LoginResponseType;
import com.securitytest.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component("customAuthenticationFailureHandler")
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param e                   认证失败时抛出的异常
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
			, AuthenticationException e) throws IOException, ServletException {
		if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
			//认证失败响应json字符串
			JsonResult result = JsonResult.build(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
			httpServletResponse.setContentType("application/json;charset=utf-8");
			httpServletResponse.getWriter().write(result.toJsonString());
		}else {
			//重定向回登录页面，注意要加上?error
			//super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage()+"?error");
			//获取上一次请求路径
			String referer = httpServletRequest.getHeader("Referer");
			logger.info("referer:" + referer);
			//如果下面有值，则认为是多端登录，直接返回一个登录地址
			Object toAuthentication = httpServletRequest.getAttribute("toAuthentication");
			String lastUrl = toAuthentication != null ? securityProperties.getAuthentication().getLoginPage()
					: StringUtils.substringBefore(referer, "?");
			logger.info("上一次请求的路径:" + referer);
			super.setDefaultFailureUrl(lastUrl + "?error");
			super.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
		}
		
	}
}
