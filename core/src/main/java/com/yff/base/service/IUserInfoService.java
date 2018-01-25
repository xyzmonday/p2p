package com.yff.base.service;

import com.yff.base.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface IUserInfoService {
    void update(UserInfo userinfo);

    void addUserInfo(UserInfo userInfo);

    UserInfo getUserInfo(Long userId);

    UserInfo getCurrent();

    void bindPhone(String phoneNumber, String verifyCode);

    void bindEmail(String uuid);

    /**
     * 更新用户基本信息
     *
     * @param userinfo
     */
    void updateBasicInfo(UserInfo userinfo);

    /**
     * 用户显示用户自动的显示
     *
     * @param keyword
     * @return Map<String       ,       Object> {id:xxx,username:xxx}
     */
    List<Map<String, Object>> queryAutoCompleteList(String keyword);


}
