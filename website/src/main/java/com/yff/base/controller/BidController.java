package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.service.IBidRequestService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 投标
 */
@Controller
public class BidController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 投标
     *
     * @param bidRequestId:标的id
     * @param amount:投标金额
     * @return
     */
    @LoginPermission
    @RequestMapping("borrow_bid")
    @ResponseBody
    public JSONResult borrowBid(Long bidRequestId, BigDecimal amount) {
        JSONResult result = new JSONResult();
        try {
            bidRequestService.bid(bidRequestId,amount);
        } catch (RuntimeException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
