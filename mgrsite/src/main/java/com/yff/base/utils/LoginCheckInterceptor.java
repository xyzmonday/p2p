package com.yff.base.utils;

import com.yff.base.common.UserContext;
import com.yff.base.domain.LoginInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台登陆拦截器
 *
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		//判断是否有后台管理员登陆
		LoginInfo current = UserContext.getCurrent() ;
		if (current == null ) {
			response.sendRedirect("/login.html");
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	
}
