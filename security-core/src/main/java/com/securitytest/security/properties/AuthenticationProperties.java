package com.securitytest.security.properties;

import lombok.Data;

/**
 * 认证相关动态配置
 */
@Data
public class AuthenticationProperties {
	//设置默认值防止application.yml没有配置
	private String loginPage = "/login/page";
    private String loginProcessingUrl = "/login/form";
    private String usernameParameter = "name";
    private String passwordParameter = "pwd";
    private String[] staticPaths = {"/dist/**", "/modules/**", "/plugins/**"};
    private LoginResponseType loginType = LoginResponseType.REDIRECT;
    private String imageCodeUrl = "/code/image";
	private String mobileCodeUrl = "/code/mobile";
	private String mobilePage = "/mobile/page";
	private Integer tokenValiditySeconds = 604800;
	

}
