package com.yff.base.service.impl;


import com.yff.base.domain.IpLog;
import com.yff.base.mapper.IpLogMapper;
import com.yff.base.query.IpLogQueryObject;
import com.yff.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpLogServiceImpl implements IIpLogService {

    @Autowired
    private IpLogMapper ipLogMapper;

    @Override
    public List<IpLog> getIpLogList(IpLogQueryObject qo) {
        return ipLogMapper.queryByPageCondition(qo);
    }
}
