package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.CompanyBankInfo;
import com.yff.base.domain.IpLog;
import com.yff.base.domain.RechargeOffline;
import com.yff.base.query.RechargeOfflineQueryObject;
import com.yff.base.service.ICompanyBankInfoService;
import com.yff.base.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 前端线下充值
 */
@Controller
public class RechargeOfflineController {

    @Autowired
    private ICompanyBankInfoService companyBankInfoService;
    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    /**
     * 导向导充值界面
     *
     * @return
     */
    @LoginPermission
    @RequestMapping("recharge")
    public String recharge(Model model) {
        List<CompanyBankInfo> banks = this.companyBankInfoService.queryByPageCondition(null);
        model.addAttribute("banks", banks);
        return "recharge";
    }


    /**
     * 保存充值
     */
    @LoginPermission
    @RequestMapping("recharge_save")
    @ResponseBody
    public JSONResult rechargeSave(RechargeOffline rechargeOffline) {
        JSONResult jsonResult = new JSONResult();
        try {
            this.rechargeOfflineService.apply(rechargeOffline);
        } catch (RuntimeException e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 查看充值明细
     */
    @LoginPermission
    @RequestMapping("/recharge_list")
    public String rechargeList(@ModelAttribute("qo")RechargeOfflineQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(),qo.getPageSize());
        qo.setApplierId(UserContext.getCurrent().getId());
        List<RechargeOffline> ipLogList = this.rechargeOfflineService.queryByPageCondition(qo);
        PageInfo<RechargeOffline> pageInfo = new PageInfo<>(ipLogList);
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
        return "recharge_list";
    }
}
