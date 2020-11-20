package com.securitytest.security.authentication.mobile;

/**
 * 短信发送统一接口
 */
public interface SmsSend {
	
	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @param content 发送内容,接收的是验证码
	 * @return
	 */
	boolean sendSms(String mobile,String content);
}
