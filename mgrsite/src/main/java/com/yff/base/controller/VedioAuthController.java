package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.domain.VedioAuth;
import com.yff.base.query.VedioAuthQueryObject;
import com.yff.base.service.IUserInfoService;
import com.yff.base.service.IVedioAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 视频认证
 */
@Controller
public class VedioAuthController {

    @Autowired
    private IVedioAuthService vedioAuthService;
    @Autowired
    private IUserInfoService userInfoService;

    @RequestMapping("vedioAuth")
    public String vedioAuth(@ModelAttribute("qo") VedioAuthQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        List<VedioAuth> list = vedioAuthService.getAppliedVedioAuthList(qo);
        PageInfo<VedioAuth> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "vedioAuth/list";
    }

    @RequestMapping("vedioAuth_audit")
    @ResponseBody
    public JSONResult vedioAuthAudit(Long loginInfoValue, String remark, int state) {
        vedioAuthService.audit(loginInfoValue, remark, state);
        return new JSONResult();
    }

    @RequestMapping("vedioAuth_autocomplate")
    @ResponseBody
    public List<Map<String,Object>> vedioAuthAutocomplate(String keyword) {
        return userInfoService.queryAutoCompleteList(keyword);
    }
}
