package com.yff.base.service.impl;


import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.mapper.SystemDictionaryItemMapper;
import com.yff.base.query.SystemDictionaryQueryObject;
import com.yff.base.service.ISystemDictionaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemDictionaryItemServiceImpl implements ISystemDictionaryItemService {

    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public List<SystemDictionaryItem> getSystemDicItems(SystemDictionaryQueryObject qo) {
        return systemDictionaryItemMapper.selectSystemDicItems(qo);
    }


    @Override
    public void saveOrUpdateSysDicItem(SystemDictionaryItem record) {
        if (record.getId() != null) {
            //更新
            systemDictionaryItemMapper.updateByPrimaryKey(record);
        } else {
            //插入
            systemDictionaryItemMapper.insert(record);
        }
    }
}
