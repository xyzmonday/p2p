package com.yff.base.service;

import com.yff.base.domain.SystemDictionary;
import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.query.SystemDictionaryQueryObject;

import java.util.List;

public interface ISystemDictionaryService {

    List<SystemDictionary> getSystemDics(SystemDictionaryQueryObject qo);

    List<SystemDictionary> getSystemDicAll();

    void saveOrUpdateSysDic(SystemDictionary record);

    /**
     * 通过分组类别查询该类别下的所有分组明细
     * @param sn
     * @return
     */
    List<SystemDictionaryItem> getSystemDicsBySn(String sn);

}
