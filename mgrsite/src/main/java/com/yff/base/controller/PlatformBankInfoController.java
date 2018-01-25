package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.CompanyBankInfo;
import com.yff.base.domain.UserFile;
import com.yff.base.query.PlatformBankInfoQueryObject;
import com.yff.base.service.ICompanyBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 平台账户管理。注意这里是p2p平台自己的账户。用户充值后（线上要借助第三方充值平台）
 * 钱应该存入到p2p平台的账户。该账户维护了用户的钱。
 */
@Controller
public class PlatformBankInfoController {

    @Autowired
    ICompanyBankInfoService companyBankInfoService;


    /**
     * 平台賬戶列表
     *
     * @param model
     * @return
     */
    @RequestMapping("companyBank_list")
    public String companyBankList(@ModelAttribute("qo") PlatformBankInfoQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //1.查询所有的待审核的实名认证申请
        List<CompanyBankInfo> list = companyBankInfoService.queryByPageCondition(qo);
        PageInfo<CompanyBankInfo> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "platformbankinfo/list";
    }

    /**
     * 修改或者保存品台银行账户
     */
    @RequestMapping("companyBank_update")
    @ResponseBody
    public JSONResult companyBankUpdate(CompanyBankInfo companyBankInfo) {
        this.companyBankInfoService.saveOrUpdate(companyBankInfo);
        return new JSONResult();
    }
}
