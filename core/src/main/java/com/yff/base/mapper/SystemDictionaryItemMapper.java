package com.yff.base.mapper;

import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface SystemDictionaryItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemDictionaryItem record);

    int insertSelective(SystemDictionaryItem record);

    SystemDictionaryItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemDictionaryItem record);

    int updateByPrimaryKey(SystemDictionaryItem record);

    List<SystemDictionaryItem> selectSystemDicItems(SystemDictionaryQueryObject qo);

    List<SystemDictionaryItem> selectByParentSn(String sn);
}