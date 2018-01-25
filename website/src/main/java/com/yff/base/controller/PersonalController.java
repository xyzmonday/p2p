package com.yff.base.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.Account;
import com.yff.base.domain.IpLog;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.query.IpLogQueryObject;
import com.yff.base.service.IAccountService;
import com.yff.base.service.IIpLogService;
import com.yff.base.service.IUserInfoService;
import com.yff.base.service.IVerifyEmailService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 个人中心
 */
@Controller
public class PersonalController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;


    @LoginPermission
    @RequestMapping("personal")
    public String personal(Model model) {
        //1.取出登陆信息
        LoginInfo loginInfo = UserContext.getCurrent();
        //2.如果没有登陆那么强制用户登陆
        UserInfo userInfo = userInfoService.getUserInfo(loginInfo.getId());
        Account account = accountService.getAccountById(loginInfo.getId());
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("account", account);
        return "personal";
    }


    @LoginPermission
    @RequestMapping("bindPhone")
    @ResponseBody
    public JSONResult bindPhone(String phoneNumber, String verifyCode) {
        JSONResult result = new JSONResult();
        result.setSuccess(true);
        result.setMsg("绑定成功");
        try {
            if (StringUtils.isEmpty(phoneNumber)) {
                result.setMsg("绑定手机号为空");
                result.setSuccess(false);
                return result;
            }
            if (StringUtils.isEmpty(verifyCode)) {
                result.setMsg("验证码为空");
                result.setSuccess(false);
                return result;
            }
            userInfoService.bindPhone(phoneNumber, verifyCode);
        } catch (RuntimeException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @GetMapping("bindEmail")
    public String bindEmail(String uuid,Model model) {
        try {
            this.userInfoService.bindEmail(uuid);
            model.addAttribute("success",true);
        } catch (Exception e) {
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";
    }

}
