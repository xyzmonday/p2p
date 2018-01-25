package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.UserContext;
import com.yff.base.domain.EmailActive;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.EmailActiveMapper;
import com.yff.base.service.IUserInfoService;
import com.yff.base.service.IVerifyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

@Service
public class VerifyEmailServiceImpl implements IVerifyEmailService {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private EmailActiveMapper emailActiveMapper;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.hostUrl}")
    private String mailHostUrl;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    @Override
    public void sendVerifyEmail(String email) {
        //1.获取是否已经绑定过邮箱
        LoginInfo loginInfo = UserContext.getCurrent();
        UserInfo userInfo = userInfoService.getUserInfo(loginInfo.getId());
        if (!userInfo.getIsBindEmail()) {
            //2.创建邮件内容
            try {
                String uuid = UUID.randomUUID().toString();
                StringBuilder content = new StringBuilder(100)
                        .append("这是验证邮件,点击<a href='").append(this.mailHostUrl)
                        .append("/bindEmail?uuid=").append(uuid)
                        .append("'>这里</a>,有效期是").append(BidConst.VERIFYEMAIL_VALIDATE_DAY).append("天!");
                System.out.println("发送邮件的内容:" + content);
                sendMail(email,content.toString());
                //3.记录邮件发送日志
                EmailActive ma = new EmailActive();
                ma.setEmail(email);
                ma.setUuid(uuid);
                ma.setLoginInfoId(userInfo.getId());
                ma.setSendDate(new Date());
                emailActiveMapper.insert(ma);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("发送验证邮件失败!");
            }
        }
    }

    //发送邮件方法 的抽取
    private void sendMail(String email, String content)
            throws MessagingException {
        //创建一个邮件消息
        MimeMessage msg = mailSender.createMimeMessage() ;
        MimeMessageHelper msgHelper = new MimeMessageHelper(msg, "utf-8");
        //设置收件人 寄件人 等邮件相关
        msgHelper.setTo(email);
        //设置邮件发送的来源
        msgHelper.setFrom(username);
        //设置邮件的主题
        msgHelper.setSubject("绑定邮箱验证邮件");
        //设置邮件内容
        msgHelper.setText(content,true); //true 表示启动html格式的邮件
        //发送邮件
        mailSender.send(msg);
    }


}
