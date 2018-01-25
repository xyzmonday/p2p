package com.yff.base.controller;

import com.yff.base.common.JSONResult;
import com.yff.base.service.IVerifyCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 验证码相关的controller
 */
@Controller
public class VerifyCodeController {

    @Autowired
    private IVerifyCodeService verifyCodeService;


    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public JSONResult sendVerifyCode(String phoneNumber) {
        JSONResult result = new JSONResult();
        try {
            if (StringUtils.isEmpty(phoneNumber)) {
                result.setSuccess(false);
                result.setMsg("请输入手机号");
                return result;
            }
            verifyCodeService.sendVerifyCode(phoneNumber);
        } catch (RuntimeException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
