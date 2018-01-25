package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.RealAuth;
import com.yff.base.domain.UserFile;
import com.yff.base.query.UserFileQueryObject;
import com.yff.base.service.IUserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 风控材料
 */
@Controller
public class UserFileController {

    @Autowired
    private IUserFileService userFileService;

    @Value("${mail.hostUrl}")
    private String host;

    @RequestMapping("userFileAuth")
    public String userFileAuth(@ModelAttribute("qo") UserFileQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //1.查询所有的待审核的实名认证申请
        List<UserFile> list = userFileService.queryByPageCondition(qo);
        for (UserFile item : list) {
            item.setImage(host + item.getImage());
            item.setFile(host + item.getFile());
        }
        PageInfo<UserFile> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "userFileAuth/list";
    }

    @RequestMapping("userFile_audit")
    @ResponseBody
    public JSONResult userFileAudit(Long id,int score,String remark,int state) {
        this.userFileService.audit(id,score,remark,state);
        return new JSONResult();
    }

}
