package com.yff.base.utils;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.UserContext;
import com.yff.base.domain.LoginInfo;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆检查的拦截器
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //判断是否有后台管理员登陆
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            LoginPermission methodAnnotation = handlerMethod.getMethodAnnotation(LoginPermission.class);

            if (methodAnnotation != null && UserContext.getCurrent() == null) {
                //需要登陆
                response.sendRedirect("/login.html");
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }


}
