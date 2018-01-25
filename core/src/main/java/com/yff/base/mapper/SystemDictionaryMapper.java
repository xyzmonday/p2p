package com.yff.base.mapper;

import com.yff.base.domain.SystemDictionary;
import com.yff.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface SystemDictionaryMapper {


    int deleteByPrimaryKey(Long id);

    int insert(SystemDictionary record);

    int insertSelective(SystemDictionary record);

    SystemDictionary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemDictionary record);

    int updateByPrimaryKey(SystemDictionary record);

    List<SystemDictionary> selectSystemDics(SystemDictionaryQueryObject qo);

    List<SystemDictionary> selectAll();
}