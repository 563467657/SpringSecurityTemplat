package com.securitytest.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 手机认证处理提供者
 */
public class MobileAuthenticationProvider implements AuthenticationProvider {
	
	private UserDetailsService userDetailService;
	
	public void setUserDetailService(UserDetailsService userDetailService) {
		this.userDetailService = userDetailService;
	}
	
	/**
	 * 认证处理:
	 * 1.通过手机号码 查询用户信息（UserDetailsService实现）
	 * 2.当查询到用户信息，则认为认证通过，封装Authentication对象
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		//1.通过手机号码 查询用户信息（UserDetailsService实现）
		MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken)authentication;
		String mobile = (String) mobileAuthenticationToken.getPrincipal();
		//2.当查询到用户信息，则认为认证通过，封装Authentication对象
		UserDetails userDetails = userDetailService.loadUserByUsername(mobile);
		//未查询到用户信息
		if (userDetails == null) {
			throw new AuthenticationServiceException("该手机号未注册");
		}
		//认证通过
		//封装到MobileAuthenticationToken里面
		MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
		return authenticationToken;
	}
	
	/**
	 * 通过这个方法，来选择对应的Provider，即选择MobileAuthenticationProvider
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return MobileAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
