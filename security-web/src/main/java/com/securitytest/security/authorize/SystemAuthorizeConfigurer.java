package com.securitytest.security.authorize;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 关于系统管理模块的安全配置
 */
@Component
public class SystemAuthorizeConfigurer implements AuthorizeConfigurerProvider {
	//在此处配置为整体逻辑，后面代码以方法级别控制权限为主，故先注释
	@Override
	public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		//有sys:user 权限的可以访问任意请求方式的/user
		/*config.antMatchers("/user").hasAuthority("sys:user")
				//有sys:role 权限的可以访问get方式的/role
				.antMatchers(HttpMethod.GET, "/role").hasAuthority("sys:role")
				.antMatchers(HttpMethod.GET, "/permission")
				//ADMIN 注意角色会在前面加上前缀ROLE_ ,也就是完整的是ROLE_ADMIN,ROLE_ROOT
				.access("hasAuthority('sys:permission') or hasAnyRole('ADMIN','ROOT')");*/
	}
}
