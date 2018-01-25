package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.domain.IpLog;
import com.yff.base.query.IpLogQueryObject;
import com.yff.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IpLogController {

    @Autowired
    private IIpLogService ipLogService;

    /**
     * 后台查询登陆日志
     */
    @RequestMapping("ipLog")
    public String ipLogList(@ModelAttribute("qo") IpLogQueryObject qo, Model model) {
        //1.设置分页查询的起始页
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //2.查询List<IpLog>数据
        List<IpLog> ipLogList = ipLogService.getIpLogList(qo);
        PageInfo<IpLog> pageInfo = new PageInfo<>(ipLogList);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "iplog/list";
    }
}
