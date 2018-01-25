package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.DateUtil;
import com.yff.base.common.UserContext;
import com.yff.base.service.IVerifyCodeService;
import com.yff.base.vo.VerifyCodeVO;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {


    @Override
    public void sendVerifyCode(String phoneNumber) {
        //1.判断是否能够发送短信。获取最后一次发送短信的时间
        VerifyCodeVO verifyCode = UserContext.getVerifyCode();
        if (verifyCode == null || DateUtil.getBetweenSecond(new Date(), verifyCode.getSendTime()) > 90) {
            //可以发送
            verifyCode = new VerifyCodeVO();
            String code = UUID.randomUUID().toString().substring(0, 4);
            System.out.println("手机验证码 = " + code);
            verifyCode.setCode(code);
            verifyCode.setPhoneNumber(phoneNumber);
            verifyCode.setSendTime(new Date());
            UserContext.putVerifyCode(verifyCode);
        } else {
            throw new RuntimeException("发送过于频繁");
        }

    }

    @Override
    public boolean verify(String phoneNumber, String verifyCode) {
        VerifyCodeVO vo = UserContext.getVerifyCode();
        //必须保证手机一致，验证码一致，以及必须在有效期内
        if (vo != null && vo.getPhoneNumber().equals(phoneNumber)
                && vo.getCode().equals(verifyCode)
                && DateUtil.getBetweenSecond(new Date(), vo.getSendTime()) <= BidConst.VERIFYCODE_VALIDATE_TIME) {
            return true;
        }
        return false;
    }
}
