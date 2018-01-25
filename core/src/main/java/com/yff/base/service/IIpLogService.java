package com.yff.base.service;

import com.yff.base.domain.IpLog;
import com.yff.base.query.IpLogQueryObject;

import java.util.List;

public interface IIpLogService {

    List<IpLog> getIpLogList(IpLogQueryObject qo);

}
