package com.yff.base;

import com.yff.base.domain.BidRequest;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.RechargeOffline;
import com.yff.base.query.PaymentScheduleQueryObject;
import com.yff.base.query.RechargeOfflineQueryObject;
import com.yff.base.service.IBidRequestService;
import com.yff.base.service.ILoginInfoService;
import com.yff.base.service.IPaymentScheduleService;
import com.yff.base.service.IRechargeOfflineService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-*.xml")
public class Test {


    @Autowired
    private ILoginInfoService loginInfoService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @Autowired
    private IPaymentScheduleService paymentScheduleService;


    @org.junit.Test
    public void testLogin() {
       /* LoginInfo loginInfo = loginInfoService.login("xyzmonday", "123456", "127.0.0.0", 0);
        RechargeOfflineQueryObject qo = new RechargeOfflineQueryObject();
        qo.setApplierId(20L);
        List<RechargeOffline> rechargeOfflines = rechargeOfflineService.queryByPageCondition(qo);
        System.out.println(rechargeOfflines);*/


        PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
        qo.setLoginInfoId(20L);
        paymentScheduleService.queryByPageCondition(qo);
    }



}
