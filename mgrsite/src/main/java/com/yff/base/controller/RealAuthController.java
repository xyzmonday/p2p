package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.RealAuth;
import com.yff.base.query.RealAuthQueryObject;
import com.yff.base.service.IRealAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RealAuthController {

    @Autowired
    private IRealAuthService realAuthService;
    @Value("${mail.hostUrl}")
    private String host;


    @RequestMapping("realAuth")
    public String realAuth(@ModelAttribute("qo") RealAuthQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //1.查询所有的待审核的实名认证申请
        List<RealAuth> list = realAuthService.getAppliedRealAuthList(qo);
        for (RealAuth item : list) {
            item.setImage1(host + item.getImage1());
            item.setImage2(host + item.getImage2());
        }
        PageInfo<RealAuth> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "realAuth/list";
    }

    @RequestMapping("realAuth_audit")
    @ResponseBody
    public JSONResult realAuthAudit(Long id, String remark, int state) {
        JSONResult result = new JSONResult();
        this.realAuthService.audit(id, remark, state);
        return result;
    }


}
