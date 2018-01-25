package com.yff.base.mapper;

import com.yff.base.domain.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<Map<String,Object>> autoComplete(String keyword);
}