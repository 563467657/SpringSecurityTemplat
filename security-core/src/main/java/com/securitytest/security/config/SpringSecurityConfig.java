package com.securitytest.security.config;

import com.securitytest.security.authentication.code.ImageCodeValidateFilter;
import com.securitytest.security.authentication.mobile.MobileAuthenticationConfig;
import com.securitytest.security.authentication.mobile.MobileValidateFilter;
import com.securitytest.security.authentication.session.CustomLogoutHandler;
import com.securitytest.security.authorize.AuthorizeConfigurerManager;
import com.securitytest.security.properties.AuthenticationProperties;
import com.securitytest.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

@EnableWebSecurity  //开启springsecurity过滤器链
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启注解方法级别权限控制
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//    public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(customUserDetailsService);
//		authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密方式
//		return authenticationProvider;
//	}
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private UserDetailsService customUserDetailsService;
	
	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	private ImageCodeValidateFilter imageCodeValidateFilter;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private MobileValidateFilter mobileValidateFilter;
	
	@Autowired
	private MobileAuthenticationConfig mobileAuthenticationConfig;
	
	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;
	
	//当同个用户session数量超过指定值之后，会调用这个实现类
	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
	
	@Autowired
	private AuthorizeConfigurerManager authorizeConfigurerManager;
	
	
	/**
	 * 为了解决退出重新登录的问题
	 *
	 * @return
	 */
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	/**
	 * 退出清除缓存
	 */
	@Autowired
	private CustomLogoutHandler customLogoutHandler;
	
	@Bean
	public JdbcTokenRepositoryImpl jdbcTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		// 是否启动时自动创建表，第一次启动创建就行，后面启动把这个注释掉,不然报错已存在表
		//jdbcTokenRepository.setCreateTableOnStartup(true);
		return jdbcTokenRepository;
	}
	
	/**
	 * 认证管理器
	 * 1、认证信息（用户名、密码）
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//数据库存储的密码必须是加密后的
		/*String password = passwordEncoder().encode("111111");
		logger.info("加密之后存储的密码：" + password);
		auth.inMemoryAuthentication().withUser("winter")
				.password(password)
				.authorities("ADMIN");*/
		auth.userDetailsService(customUserDetailsService);
		//auth.authenticationProvider(authenticationProvider());
	}
	
	/**
	 * 资源权限配置
	 * 1、被拦截的资源
	 * <p>
	 * 当你认证成功后，springsecurity会重定向到你上一次请求上
	 *
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.httpBasic()    //采用httpBasic认证方式
		AuthenticationProperties authentication = securityProperties.getAuthentication();
		http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin()    //表单登录方式
				.loginPage(authentication.getLoginPage())
				.loginProcessingUrl(authentication.getLoginProcessingUrl())  //登录表单提交处理url，默认是/login
				.usernameParameter(authentication.getUsernameParameter())  //默认的是username
				.passwordParameter(authentication.getPasswordParameter())   //默认的是password
				.successHandler(customAuthenticationSuccessHandler)
				.failureHandler(customAuthenticationFailureHandler)
				//在这里处理还是在CustomAuthenticationFailureHandler中处理都一样
//				.failureUrl(securityProperties.getAuthentication().getLoginPage()+"?error")
				
				/*.and()
				.authorizeRequests()    //认证请求
				.antMatchers(authentication.getLoginPage(),
//						"/code/image", "/mobile/page", "/code/mobile"
						authentication.getImageCodeUrl(),
						authentication.getMobilePage(),
						authentication.getMobileCodeUrl()
				).permitAll() //放行登录页不需要认证

				//有sys:user 权限的可以访问任意请求方式的/user
				.antMatchers("/user").hasAuthority("sys:user")
				//有sys:role 权限的可以访问get方式的/role
				.antMatchers(HttpMethod.GET, "/role").hasAuthority("sys:role")
				.antMatchers(HttpMethod.GET,"/permission")
				.access("hasAuthority('sys:permission') or hasAnyRole('ADMIN','ROOT')")//ADMIN 注意角色会在前面加上前缀ROLE_ ,也就是完整的是ROLE_ADMIN,ROLE_ROOT
				.anyRequest().authenticated()   //所用访问该应用的http请求都要通过身份认证才可以访问*/
				
				.and()
				
				.rememberMe() //记住我
				.tokenRepository(jdbcTokenRepository()) // 保存登录信息
				.tokenValiditySeconds(authentication.getTokenValiditySeconds()) // 记住我有效时长
				.and()
				.sessionManagement() //session管理
				.invalidSessionStrategy(invalidSessionStrategy) //当session失效后的处理类，可自定义失效后返回json,注释之后返回登录页
				.maximumSessions(1) //每个用户在系统中最多可以有多少个session
				.expiredSessionStrategy(sessionInformationExpiredStrategy)  //超过最大数执行这个实现类
				.maxSessionsPreventsLogin(true) //当一个用户达到最大session数，则不允许后面再登录（会和remember-me功能产生冲突，二选一）
				.sessionRegistry(sessionRegistry())
				.and().and()
				.logout()
				.addLogoutHandler(customLogoutHandler)//退出清除缓存
				//.logoutUrl("/user/logout")//退出请求路径，和前台页面保持一致即可，默认为/logout
				//.logoutSuccessUrl("/mobile/page")//退出成功后跳转的地址
				.deleteCookies("JSESSIONID")//退出后删除什么cookie值,删除cookie之后退出会直接返回登录页，否则返回session过期请重新登录
				.and().csrf().disable() //关闭跨站请求伪造
		;
		
		//将手机认证添加到过滤器链上
		http.apply(mobileAuthenticationConfig);
		//将所有的授权配置统一的管理起来
		authorizeConfigurerManager.configure(http.authorizeRequests());
	}
	
	/**
	 * 一般是针对静态资源放行
	 *
	 * @param web
	 * @throws Exception
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
	}
	
}
