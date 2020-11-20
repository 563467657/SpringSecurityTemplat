package com.securitytest.security.authentication.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送短信验证码，第三方的短信服务接口
 */
public class SmsCodeSender implements SmsSend{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 *
	 * @param mobile 手机号
	 * @param content 发送内容,接收的是验证码
	 * @return
	 */
	@Override
	public boolean sendSms(String mobile, String content) {
		String sendContent = String.format("欢迎登陆XX系统，验证码%s，请勿泄露", content);
		//调用第三方发送功能的sdk
		logger.info("向手机号："+mobile+"发送的短信为："+sendContent);
		
		return false;
	}
}
