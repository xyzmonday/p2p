package com.yff.base.controller;

import com.yff.base.common.BidConst;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.LoginInfo;
import com.yff.base.service.ILoginInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    @Autowired
    private ILoginInfoService loginInfoService;

    @PostMapping("/register")
    @ResponseBody
    public JSONResult register(String username, String password) {
        JSONResult result = new JSONResult();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            result.setMsg("用户名或者密码为空");
            result.setSuccess(false);
            return result;
        }
        try {
            loginInfoService.register(username, password);
        } catch (RuntimeException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    @PostMapping("/checkUsername")
    @ResponseBody
    public Boolean checkUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return Boolean.FALSE;
        }
        //注意这里如果不存在返回true,如果存在返回false
        return !loginInfoService.checkUsername(username);
    }


    @PostMapping("/login")
    @ResponseBody
    public JSONResult login(String username, String password, HttpServletRequest request) {
        JSONResult result = new JSONResult();
        if (StringUtils.isEmpty(username)) {
            result.setSuccess(false);
            result.setMsg("用户名为空");
            return result;
        }
        if (StringUtils.isEmpty(password)) {
            result.setSuccess(false);
            result.setMsg("用户密码为空");
            return result;
        }
        LoginInfo loginInfo = loginInfoService.login(username, password, request.getRemoteAddr(),
                BidConst.USER_CLIENT);
        if (loginInfo != null) {
            result.setMsg("登陆成功");
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMsg("登陆失败,用户名或者密码错误!");
        }
        return result;
    }


    @RequestMapping("/logout")
    public String logout() {
        UserContext.invalidate();
        return "redirect:/index";
    }
}