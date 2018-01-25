package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.Account;
import com.yff.base.domain.IpLog;
import com.yff.base.domain.PaymentSchedule;
import com.yff.base.query.PaymentScheduleQueryObject;
import com.yff.base.service.IAccountService;
import com.yff.base.service.IBidRequestService;
import com.yff.base.service.IPaymentScheduleService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.json.JsonReadContext;

import java.util.List;


/**
 * 还款相关控制器
 */
@Controller
public class ReturnMoneyController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IPaymentScheduleService paymentScheduleService;

    @LoginPermission
    @RequestMapping("borrowBidReturn_list")
    public String borrowBidReturnList(@ModelAttribute("qo") PaymentScheduleQueryObject qo, Model model) {
        Account account = this.accountService.getAccountById(UserContext.getCurrent().getId());
        //查询还款明细
        qo.setLoginInfoId(UserContext.getCurrent().getId());
        PageHelper.startPage(qo.getPageNum(),qo.getPageSize());
        List<PaymentSchedule> list = paymentScheduleService.queryByPageCondition(qo);
        PageInfo<PaymentSchedule> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        //注意这里需要考虑分页插件的startPage和totalPage，由于totalPage必须大于等于startPage
        //而且都不能小于等于1
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult",pageInfo);
        model.addAttribute("account", account);
        return "returnmoney_list";
    }

    @RequestMapping("returnMoney")
    @ResponseBody
    public JSONResult returnMoney(Long id) {
        JSONResult json = new JSONResult();
        try {
            this.bidRequestService.returnMoney(id);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }


}
