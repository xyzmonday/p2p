package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.MD5;
import com.yff.base.common.UserContext;
import com.yff.base.domain.Account;
import com.yff.base.domain.IpLog;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.IpLogMapper;
import com.yff.base.mapper.LoginInfoMapper;
import com.yff.base.service.IAccountService;
import com.yff.base.service.ILoginInfoService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("loginInfoService")
public class LoginInfoServiceImpl implements ILoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private IpLogMapper ipLogMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 前台注册
     *
     * @param username
     * @param password
     */
    @Override
    public void register(String username, String password) {
        //校验username是否存在
        int count = loginInfoMapper.getCountByUsername(username);

        //如果不存在那么保存LoginInfo
        if (count <= 0) {
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername(username);
            //密码加密
            loginInfo.setPassword(MD5.encode(password));
            loginInfo.setState((byte) BidConst.REG_STATE_NORMAL);
            loginInfo.setUserType((byte) BidConst.USER_CLIENT);
            loginInfoMapper.insert(loginInfo);

            //初始化账户信息
            Account account = new Account();
            account.setId(loginInfo.getId());
            account.setVersion(0);
            this.accountService.addAccount(account);
            //初始化用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setVersion(0);
            userInfo.setId(loginInfo.getId());
            this.userInfoService.addUserInfo(userInfo);
            return;
        }

        //如果存在那么抛出异常
        throw new RuntimeException("用户已经注册");
    }

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return true表示该用户名已经存在，false表示该用户名不存在
     */
    @Override
    public Boolean checkUsername(String username) {
        return loginInfoMapper.getCountByUsername(username) > 0;
    }

    /**
     * 前后端同时公用一个登陆用户。注意这里不管登陆是否成功都生成了ip记录
     *
     * @param username
     * @param password
     * @param ip
     * @param userType
     * @return
     */
    @Override
    public LoginInfo login(String username, String password, String ip, int userType) {
        LoginInfo loginInfo = loginInfoMapper.login(username, MD5.encode(password), userType);
        IpLog ipLog = new IpLog();
        ipLog.setIp(ip);
        ipLog.setLoginTime(new Date());
        ipLog.setLoginType((byte) userType);
        if (loginInfo != null) {
            //保存用户信息
            ipLog.setLoginInfoId(loginInfo.getId());
            ipLog.setUsername(loginInfo.getUsername());
            loginInfo.setLastLoginTime(new Date());
            UserContext.putCurrent(loginInfo);
            ipLog.setLoginState((byte) BidConst.IP_STATE_SUCCESS);

        } else {
            ipLog.setLoginState((byte) BidConst.IP_STATE_FAIL);
        }
        ipLogMapper.insert(ipLog);
        return loginInfo;

    }

    /**
     * 初始化第一个管理员
     */
    @Override
    public void initAdmin() {
        //查询是否有管理员
        int count = loginInfoMapper.getCountByUserType(BidConst.USER_MANAGER);
        if (count <= 0) {
            //如果还没有一个管理员，那么创建一个
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername(BidConst.DEFAULT_ADMIN_NAME);
            //密码加密
            loginInfo.setPassword(MD5.encode(BidConst.DEFAULT_ADMIN_PASSWORD));
            //loginInfo.setState((byte) BidConst.REG_STATE_NORMAL);
            loginInfo.setUserType((byte) BidConst.USER_MANAGER);
            loginInfoMapper.insert(loginInfo);
        }
    }
}
