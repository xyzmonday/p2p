package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.BidConst;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.*;
import com.yff.base.query.BidRequestQueryObject;
import com.yff.base.query.UserFileQueryObject;
import com.yff.base.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 借款审核
 */
@Controller
public class BidRequestController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IBidRequestHistoryService bidRequestHistoryService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;


    /**
     * 发标前待审核列表
     *
     * @return
     */
    @RequestMapping("bidrequest_publishaudit_list")
    public String bidRequestPublishAuditList(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        //1.设置分页查询的起始页
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //2.查询List<IpLog>数据
        List<BidRequest> bidRequests = bidRequestService.queryByPageCondition(qo);
        PageInfo<BidRequest> pageInfo = new PageInfo<>(bidRequests);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "bidRequest/publish_audit";
    }

    /**
     * 发标前审核
     */
    @RequestMapping("bidrequest_publishaudit")
    @ResponseBody
    public JSONResult bidRequestPublishAudit(Long id, int state, String remark) {
        this.bidRequestService.publishAudit(id, state, remark);
        return new JSONResult();
    }

    /**
     * 后台查看借款详情
     *
     * @param id:表示该借款的id
     */
    @RequestMapping("borrow_info")
    public String borrowInfo(Long id, Model model) {
        //bidRequest
        BidRequest bidRequest = bidRequestService.getBidRequestById(id);
        model.addAttribute("bidRequest", bidRequest);
        //userInfo
        UserInfo userInfo = userInfoService.getUserInfo(bidRequest.getCreateUser().getId());
        model.addAttribute("userInfo", userInfo);

        //当前用户是否是借款人自己。如果是自己，那么只能撤销，如果不是并且有账户那么可以投标

        //audits历史（审核历史）
        List<BidRequestAuditHistory> audits = bidRequestHistoryService.getHistorysByBidRequestId(id);
        model.addAttribute("audits", audits);
        //realAuth借款人实名信息
        RealAuth realAuth = realAuthService.getById(userInfo.getRealAuthId());
        model.addAttribute("realAuth", realAuth);
        //userFile借款人风控资料
        UserFileQueryObject qo = new UserFileQueryObject();
        //查看该用户已经审核通过的风控资料
        qo.setApplierId(userInfo.getId());
        qo.setState(BidConst.AUDIT_STATE_AUTH);
        List<UserFile> userFiles = userFileService.queryByPageCondition(qo);
        model.addAttribute("userFiles", userFiles);
        return "bidRequest/borrow_info";
    }


    /**
     * 查看满标一审列表
     */
    @RequestMapping("bidrequest_audit1_list")
    public String bidRequestAudit1List(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        //1.设置分页查询的起始页
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //2.查询List<IpLog>数据
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
        List<BidRequest> ipLogList = bidRequestService.queryByPageCondition(qo);
        PageInfo<BidRequest> pageInfo = new PageInfo<>(ipLogList);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "bidRequest/audit1";
    }

    /**
     * 查看满标二审列表
     *
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("bidrequest_audit2_list")
    public String bidRequestAudit2List(@ModelAttribute("qo") BidRequestQueryObject qo, Model model) {
        //1.设置分页查询的起始页
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        //2.查询List<IpLog>数据
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
        List<BidRequest> ipLogList = bidRequestService.queryByPageCondition(qo);
        PageInfo<BidRequest> pageInfo = new PageInfo<>(ipLogList);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "bidRequest/audit2";
    }


    /**
     * 满标一审
     *
     * @param id
     * @param state
     * @param remark
     * @return
     */
    @RequestMapping("bidrequest_audit1")
    @ResponseBody
    public JSONResult bidRequestAudit1(Long id, int state, String remark) {
        JSONResult result = new JSONResult();
        try {
            bidRequestService.fullAudit1(id, state, remark);
        } catch (RuntimeException e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 满标二审
     *
     * @param id
     * @param state
     * @param remark
     * @return
     */
    @RequestMapping("bidrequest_audit2")
    @ResponseBody
    public JSONResult bidRequestAudit2(Long id, int state, String remark) {
        JSONResult result = new JSONResult();
        try {
            bidRequestService.fullAudit2(id, state, remark);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}


