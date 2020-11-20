package com.securitytest.security.authentication.session;

import com.securitytest.base.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当session失效后的处理逻辑
 */
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {
	
	private SessionRegistry sessionRegistry;
	
	public CustomInvalidSessionStrategy(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}
	
	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//不可以使用request.getSession().getId()，因为默认session超时失效后session已经清除，getsession相当于重新获得了一个新的session
		//request.getRequestedSessionId()相当于获取浏览器端session中的id，因为还没清除浏览器端cookie，所以使用这个方法
		sessionRegistry.removeSessionInformation(request.getRequestedSessionId());
		//要将浏览器当中的jsessionid删除
		cancelCookie(request, response);
		JsonResult jsonResult = JsonResult.build(HttpStatus.UNAUTHORIZED.value(), "登录已超时，请重新登录");
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(jsonResult.toJsonString());
	}
	
	protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setMaxAge(0);
		cookie.setPath(getCookiePath(request));
		response.addCookie(cookie);
	}
	
	private String getCookiePath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return contextPath.length() > 0 ? contextPath : "/";
	}
}
