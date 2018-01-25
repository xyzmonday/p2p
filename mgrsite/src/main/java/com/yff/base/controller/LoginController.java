package com.yff.base.controller;

import com.yff.base.common.BidConst;
import com.yff.base.common.JSONResult;
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
public class LoginController {

    @Autowired
    private ILoginInfoService loginInfoService;

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
                BidConst.USER_MANAGER);
        if (loginInfo != null) {
            result.setMsg("登陆成功");
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
            result.setMsg("登陆失败,用户名或者密码错误!");
        }
        return result;
    }

    /**
     * 后台首页
     * @return
     */
    @GetMapping("/index")
    public String index() {
        return "main";
    }

}
