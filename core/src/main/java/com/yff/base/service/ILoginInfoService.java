package com.yff.base.service;

import com.yff.base.domain.LoginInfo;

/**
 * 用户登录
 */
public interface ILoginInfoService {
    /**
     * 用户注册
     * @param username
     * @param password
     */
    void register(String username,String password);

    /**
     * 用户注册时检查用户名是否存在
     * @param username
     * @return 返回true表示用户名存在，返回false表示用户名不存在
     */
    Boolean checkUsername(String username);


    LoginInfo login(String username, String password,String ip, int userType);

    /**
     * 初始化第一个管理员
     */
    void initAdmin();
}
