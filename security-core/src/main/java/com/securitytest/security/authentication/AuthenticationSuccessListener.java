package com.securitytest.security.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 这个接口是用来监听认证成功之后的处理，也就是说认证成功之后让成功处理器调用此接口方法 successListener
 */
public interface AuthenticationSuccessListener {
    void successListener(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
            , Authentication authentication);
}
