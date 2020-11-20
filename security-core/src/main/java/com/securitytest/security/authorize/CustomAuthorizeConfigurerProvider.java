package com.securitytest.security.authorize;

import com.securitytest.security.properties.AuthenticationProperties;
import com.securitytest.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 身份认证相关的授权配置
 */
@Component
@Order//值越小加载越优先，值越大加载越靠后，默认为int最大值
public class CustomAuthorizeConfigurerProvider implements AuthorizeConfigurerProvider {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		AuthenticationProperties authentication = securityProperties.getAuthentication();
		config.antMatchers(authentication.getLoginPage(),
//						"/code/image", "/mobile/page", "/code/mobile"
				authentication.getImageCodeUrl(),
				authentication.getMobilePage(),
				authentication.getMobileCodeUrl()
		).permitAll()
				.anyRequest().authenticated();   //所用访问该应用的http请求都要通过身份认证才可以访问//放行登录页不需要认证
	}
}
