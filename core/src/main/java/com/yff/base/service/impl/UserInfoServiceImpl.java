package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.BitStatesUtils;
import com.yff.base.common.DateUtil;
import com.yff.base.common.UserContext;
import com.yff.base.domain.EmailActive;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.EmailActiveMapper;
import com.yff.base.mapper.UserInfoMapper;
import com.yff.base.service.IUserInfoService;
import com.yff.base.service.IVerifyCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private EmailActiveMapper emailActiveMapper;

    @Override
    public void update(UserInfo userinfo) {
        int ret = this.userInfoMapper.updateByPrimaryKey(userinfo);
        if (ret == 0) {
            throw new RuntimeException("乐观锁失败,Userinfo:" + userinfo.getId());
        }

    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }

    @Override
    public UserInfo getCurrent() {
        LoginInfo loginInfo = UserContext.getCurrent();
        UserInfo userInfo = this.getUserInfo(loginInfo.getId());
        return userInfo;
    }

    /**
     * 绑定手机号
     *
     * @param phoneNumber
     * @param verifyCode
     */
    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //1.查询用户是否绑定了手机
        UserInfo userInfo = this.getCurrent();
        if (!userInfo.getIsBindPhone()) {
            //2.验证验证证码的合法性
            boolean validate = verifyCodeService.verify(phoneNumber, verifyCode);
            if (validate) {
                //3.如果验证成功
                userInfo.addState(BitStatesUtils.OP_BIND_PHONE);
                userInfo.setPhoneNumber(phoneNumber);
                this.update(userInfo);
            } else {
                throw new RuntimeException("绑定手机失败");
            }
        }
    }

    /**
     * 绑定邮件
     *
     * @param uuid
     */
    @Override
    public void bindEmail(String uuid) {
        //1.获取发送邮件的日志
        EmailActive ma = emailActiveMapper.selectByUuid(uuid);
        if (ma != null) {
            //2.验证邮件
            UserInfo userInfo = this.getUserInfo(ma.getLoginInfoId());
            if (userInfo != null
                    && DateUtil.getBetweenSecond(new Date(), ma.getSendDate()) <= BidConst.VERIFYEMAIL_VALIDATE_DAY * 60 * 60 * 24) {
                //3.更新用户绑定邮件的信息
                userInfo.addState(BitStatesUtils.OP_BIND_EMAIL);
                userInfo.setEmail(ma.getEmail());
                this.update(userInfo);
                return;
            }
        }
        throw new RuntimeException("绑定邮箱失败");
    }

    /**
     * 更新用户基本信息。注意这里必须使用当前用户登录的id来更新用户基本信息
     *
     * @param userinfo
     */
    @Override
    public void updateBasicInfo(UserInfo userinfo) {
        UserInfo old = this.getCurrent();
        old.setIncomeGrade(userinfo.getIncomeGrade());
        old.setKidCount(userinfo.getKidCount());
        old.setEducationBackground(userinfo.getEducationBackground());
        old.setHouseCondition(userinfo.getHouseCondition());
        old.setMarriage(userinfo.getMarriage());

        if (!old.getIsBasicInfo()) {
            old.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        this.update(old);
    }

    @Override
    public List<Map<String, Object>> queryAutoCompleteList(String keyword) {
        return userInfoMapper.autoComplete(keyword);
    }

}
