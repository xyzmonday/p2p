package com.yff.base.service;

import com.yff.base.domain.RealAuth;

public interface ISmsService {

    void onRealAuthSendSms(RealAuth realAuth);
}
