package com.yff.base.common;

import com.yff.base.domain.LoginInfo;
import com.yff.base.vo.VerifyCodeVO;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserContext {
    public static final String USER_IN_SESSION = "loginInfo";
    public static final String VERIFYCODE_IN_SESSION = "verifycode";

    public static void putCurrent(LoginInfo loginInfo) {
        getSession().setAttribute(USER_IN_SESSION, loginInfo);
    }

    public static void invalidate() {
        getSession().invalidate();
    }

    public static LoginInfo getCurrent() {
        return (LoginInfo) getSession().getAttribute(USER_IN_SESSION);
    }

    public static VerifyCodeVO getVerifyCode() {
        return (VerifyCodeVO) getSession().getAttribute(VERIFYCODE_IN_SESSION);
    }

    public static void putVerifyCode(VerifyCodeVO verifyCodeVO) {
        getSession().setAttribute(VERIFYCODE_IN_SESSION, verifyCodeVO);
    }


    private static HttpSession getSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        return session;
    }
}
