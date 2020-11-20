package com.securitytest.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "web.security")

public class SecurityProperties {
	
	// 将application.yml 中的 web.security.authentication 下面的值绑定到此对象中
	private AuthenticationProperties authentication;
	
	public AuthenticationProperties getAuthentication() {
		return authentication;
	}
	
	public void setAuthentication(AuthenticationProperties authentication) {
		this.authentication = authentication;
	}
}
