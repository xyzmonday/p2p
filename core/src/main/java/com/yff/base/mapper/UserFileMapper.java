package com.yff.base.mapper;

import com.yff.base.domain.RealAuth;
import com.yff.base.domain.UserFile;
import com.yff.base.query.RealAuthQueryObject;
import com.yff.base.query.UserFileQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFileMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserFile record);

    int insertSelective(UserFile record);

    UserFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFile record);

    int updateByPrimaryKey(UserFile record);

    List<UserFile> queryByPageCondition(UserFileQueryObject qo);

    List<UserFile> queryUserFilesByFileType(@Param("loginInfoId") Long loginInfoId,@Param("hasType") Boolean hasType);
}