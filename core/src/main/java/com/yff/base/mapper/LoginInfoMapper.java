package com.yff.base.mapper;

import com.yff.base.domain.LoginInfo;
import org.apache.ibatis.annotations.Param;

public interface LoginInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LoginInfo record);

    int insertSelective(LoginInfo record);

    LoginInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LoginInfo record);

    int updateByPrimaryKey(LoginInfo record);
    int getCountByUsername(String username);

    LoginInfo login(@Param("username") String username,
                                          @Param("password") String password,
                                          @Param("userType") int userType);

    int getCountByUserType(int userType);
}