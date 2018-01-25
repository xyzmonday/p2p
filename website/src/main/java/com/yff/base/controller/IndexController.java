package com.yff.base.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.BidConst;
import com.yff.base.domain.BidRequest;
import com.yff.base.domain.IpLog;
import com.yff.base.query.BidRequestQueryObject;
import com.yff.base.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 首页
 */
@Controller
public class IndexController {

    @Autowired
    private IBidRequestService bidRequestService;

    @RequestMapping("index")
    public String index(Model model) {
        //查询正在投标中，还款中，已经还清的标
        BidRequestQueryObject qo = new BidRequestQueryObject();
        int[] bidRequestStates = {BidConst.BIDREQUEST_STATE_BIDDING, BidConst.BIDREQUEST_STATE_PAYING_BACK, BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK};
        qo.setBidRequestStates(bidRequestStates);
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize(),"bid_request_state ASC");
        List<BidRequest> bidRequests = bidRequestService.queryByPageCondition(qo);
        model.addAttribute("bidRequests",bidRequests);
        return "main";
    }

    /**
     * 我要投资
     * @return
     */
    @RequestMapping("invest")
    public String invest() {
        return "invest";
    }

    /**
     * 查看投资明细。注意这里返回的是一个页面，invest页面发出ajax请求，成功过后
     * 将该页面嵌入到tbody中。注意invet_list已经将通过model里面的数据自动进行渲染
     */
    @RequestMapping("invest_list")
    public String investList(BidRequestQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(),qo.getPageSize());
        List<BidRequest> bidRequests = this.bidRequestService.queryByPageCondition(qo);
        PageInfo<BidRequest> pageInfo = new PageInfo<>(bidRequests);
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
        return "invest_list";
    }
}
