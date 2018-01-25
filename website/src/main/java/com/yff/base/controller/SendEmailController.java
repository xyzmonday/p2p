package com.yff.base.controller;

import com.yff.base.common.JSONResult;
import com.yff.base.service.IVerifyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 发送邮箱相关服务
 */
@Controller
public class SendEmailController {

    @Autowired
    private IVerifyEmailService verifyEmailService;

    @RequestMapping("sendEmail")
    @ResponseBody
    public JSONResult sendEmail(String email) {
        JSONResult result = new JSONResult();
        result.setMsg("发送邮件成功");
        result.setSuccess(true);
        try {
            this.verifyEmailService.sendVerifyEmail(email);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
