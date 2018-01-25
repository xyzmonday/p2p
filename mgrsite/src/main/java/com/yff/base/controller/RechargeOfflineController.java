package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
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
 * 线下充值审核
 */
@Controller
public class RechargeOfflineController {

    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    @Autowired
    private ICompanyBankInfoService companyBankInfoService;

    @RequestMapping("rechargeOffline")
    public String rechargeOffline(@ModelAttribute("qo") RechargeOfflineQueryObject qo, Model model) {
        model.addAttribute("banks", this.companyBankInfoService.queryByPageCondition(null));
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
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
        model.addAttribute("pageResult", pageInfo);
        return "rechargeOffline/list";
    }

    /**
     * 充值审核
     *
     * @param id
     * @param state
     * @param remark
     * @return
     */
    @RequestMapping("rechargeOffline_audit")
    @ResponseBody
    public JSONResult rechargeOfflineAudit(Long id, int state, String remark) {
        rechargeOfflineService.audit(id, state, remark);
        return new JSONResult();
    }
}
