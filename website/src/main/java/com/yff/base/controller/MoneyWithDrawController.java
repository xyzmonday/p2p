package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.UserInfo;
import com.yff.base.service.IAccountService;
import com.yff.base.service.IMoneyWithDrawService;
import com.yff.base.service.IUserBankInfoService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 提现
 */
@Controller
public class MoneyWithDrawController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IMoneyWithDrawService moneyWithDrawService;
    @Autowired
    private IUserBankInfoService userBankInfoService;
    @Autowired
    private IAccountService accountService;

    @LoginPermission
    @RequestMapping("moneyWithdraw")
    public String moneyWithdraw(Model model) {
        UserInfo current = this.userInfoService.getCurrent();
        model.addAttribute("haveProcessing", current.getHasWithdrawInProcess());
        model.addAttribute("bankInfo", this.userBankInfoService.getByUserId(current.getId()));
        model.addAttribute("account", this.accountService.getAccountById(current.getId()));
        return "moneyWithdraw_apply";
    }

    /**
     * 提现申请
     */
    @LoginPermission
    @RequestMapping("moneyWithdraw_apply")
    @ResponseBody
    public JSONResult moneyWithdrawApply(BigDecimal moneyAmount) {

        JSONResult result = new JSONResult();
        try {
            this.moneyWithDrawService.apply(moneyAmount);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
