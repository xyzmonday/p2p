package com.yff.base.service;

import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface ISystemDictionaryItemService {

    List<SystemDictionaryItem> getSystemDicItems(SystemDictionaryQueryObject qo);

    void saveOrUpdateSysDicItem(SystemDictionaryItem record);
}
