package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.aop.LoginPermission;
import com.yff.base.domain.IpLog;
import com.yff.base.query.IpLogQueryObject;
import com.yff.base.service.IIpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IpLogController {

    @Autowired
    private IIpLogService ipLogService;

    //@ModelAttribute注解如果在方法的上，那么会将该对象加入到Model中
    @LoginPermission
    @RequestMapping("ipLog")
    public String ipLogList(@ModelAttribute("qo") IpLogQueryObject qo, Model model) {
        //1.设置分页查询的起始页
        PageHelper.startPage(qo.getPageNum(),qo.getPageSize());
        //2.查询List<IpLog>数据
        List<IpLog> ipLogList = ipLogService.getIpLogList(qo);
        PageInfo<IpLog> pageInfo = new PageInfo<>(ipLogList);
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
        return "iplog_list";
    }
}
