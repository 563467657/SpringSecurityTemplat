package com.securitytest.security.config;

import com.securitytest.security.authentication.mobile.SmsCodeSender;
import com.securitytest.security.authentication.mobile.SmsSend;
import com.securitytest.security.authentication.session.CustomInvalidSessionStrategy;
import com.securitytest.security.authentication.session.CustomSessionInformationExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 主要为容器中添加bean实例
 */
@Configuration
public class SecurityConfigBean {
	
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Bean
	@ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
		return new CustomSessionInformationExpiredStrategy();
	}
	
	@Bean
	@ConditionalOnMissingBean(InvalidSessionStrategy.class)
	public InvalidSessionStrategy invalidSessionStrategy() {
		return new CustomInvalidSessionStrategy(sessionRegistry);
	}
	
	/**
	 * 默认情况下，采用的是SmsCodeSender实例，
	 * 但是如果容器当中有其他的SmsSend类型的实例，
	 * 那当前的这个SmsSender就失效了。
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SmsSend.class)
	public SmsSend smsSend() {
		return new SmsCodeSender();
	}
	
	
}
