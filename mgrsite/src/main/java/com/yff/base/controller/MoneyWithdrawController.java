package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.IpLog;
import com.yff.base.domain.MoneyWithDraw;
import com.yff.base.query.MoneyWithdrawQueryObject;
import com.yff.base.service.IMoneyWithDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.json.JsonReadContext;

import java.util.List;


/**
 * 后台提现的审核相关 控制器
 */
@Controller
public class MoneyWithdrawController {

    @Autowired
    private IMoneyWithDrawService moneyWithdrawService;


    @RequestMapping("moneyWithdraw")
    public String moneyWithdrawList(@ModelAttribute("qo") MoneyWithdrawQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        List<MoneyWithDraw> ipLogList = this.moneyWithdrawService.queryByPageCondition(qo);
        PageInfo<MoneyWithDraw> pageInfo = new PageInfo<>(ipLogList);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "moneywithdraw/list";

    }

    @RequestMapping("moneyWithdraw_audit")
    @ResponseBody
    public JSONResult audit(Long id, String remark, int state) {
        JSONResult json = new JSONResult();
        try {
            this.moneyWithdrawService.audit(id, remark, state);
        } catch (Exception e) {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }
}
