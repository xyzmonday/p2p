package com.yff.base.service.impl;

import com.yff.base.domain.SystemDictionary;
import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.mapper.SystemDictionaryItemMapper;
import com.yff.base.mapper.SystemDictionaryMapper;
import com.yff.base.query.SystemDictionaryQueryObject;
import com.yff.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {

    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public List<SystemDictionary> getSystemDics(SystemDictionaryQueryObject qo) {
        return systemDictionaryMapper.selectSystemDics(qo);
    }

    @Override
    public List<SystemDictionary> getSystemDicAll() {
        return systemDictionaryMapper.selectAll();
    }

    @Override
    public void saveOrUpdateSysDic(SystemDictionary record) {
        if (record.getId() == null) {
            //新增
            systemDictionaryMapper.insert(record);
        } else {
            //更新
            systemDictionaryMapper.updateByPrimaryKey(record);
        }
    }

    @Override
    public List<SystemDictionaryItem> getSystemDicsBySn(String sn) {
        return systemDictionaryItemMapper.selectByParentSn(sn);
    }
}
