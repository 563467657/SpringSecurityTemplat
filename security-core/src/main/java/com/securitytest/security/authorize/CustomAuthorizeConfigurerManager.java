package com.securitytest.security.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将所有的授权配置统一的管理起来
 */
@Component
public class CustomAuthorizeConfigurerManager implements AuthorizeConfigurerManager {
	
	@Autowired
	List<AuthorizeConfigurerProvider> authorizeConfigurerProviders;
	
	/**
	 * 将一个个AuthorizeConfigurerProvider的实现类，传入配置的参数ExpressionInterceptUrlRegistry
	 * @param config
	 */
	@Override
	public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		for (AuthorizeConfigurerProvider authorizeConfigurerProvider : authorizeConfigurerProviders) {
			authorizeConfigurerProvider.configure(config);
		}
	}
}
