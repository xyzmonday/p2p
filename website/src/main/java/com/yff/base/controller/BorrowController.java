package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.BidConst;
import com.yff.base.common.UserContext;
import com.yff.base.domain.*;
import com.yff.base.query.UserFileQueryObject;
import com.yff.base.service.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BorrowController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IBidRequestHistoryService bidRequestHistoryService;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private IRealAuthService realAuthService;


    /**
     * 跳转到借款首页
     *
     * @return
     */
    @LoginPermission
    @RequestMapping("borrow")
    public String borrow(Model model) {
        if (UserContext.getCurrent() != null) {
            UserInfo userInfo = userInfoService.getUserInfo(UserContext.getCurrent().getId());
            Account account = accountService.getAccountById(userInfo.getId());
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("account", account);
            System.out.println(BidConst.CREDIT_BORROW_SCORE);
            model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
            return "borrow";
        } else {
            //如果没有登录 直接导向到静态页面中
            return "redirect:borrow.html";
        }
    }

    /**
     * 用户点击“申请贷款”，跳转到申请借款的页面。
     * 1.如果满足申请借款的条件，那么跳转到borrow_apply页面
     * 2.如果满足申请借款的条件，查看是否已经有借款在审核，跳转到borrow_apply_result页面，查看申请结果
     * 3.如果不满足申请借款的条件，那么跳转到borrow页面；
     *
     * @param model
     * @return
     */
    @LoginPermission
    @RequestMapping("borrowInfo")
    public String borrowInfo(Model model) {
        LoginInfo loginInfo = UserContext.getCurrent();
        if (this.bidRequestService.canApplyBidRequest(loginInfo.getId())) {
            //1.查看用户是否在申请借款流程中
            UserInfo userInfo = this.userInfoService.getUserInfo(loginInfo.getId());
            if (!userInfo.getHasBidRequestInProcess()) {
                //如果没有申请借款处于审核中,跳转到borrow_apply
                //账户信息（剩余最大借款）
                Account account = this.accountService.getAccountById(loginInfo.getId());
                model.addAttribute("account", account);
                //最小借款申请金额
                model.addAttribute("minBidRequestAmount", BidConst.SMALLEST_BID_AMOUNT);
                //最小投标金额
                model.addAttribute("minBidAmount", BidConst.SMALLEST_BID_AMOUNT);
                return "borrow_apply";
            } else {
                //查看借款审核结果
                return "borrow_apply_result";
            }
        } else {
            //返回申请借款首页
            return "redirect:/borrow";
        }
    }

    /**
     * 用户填写借款表单，开始申请借款。
     * 将数据保存到数据库后返回borrowInfo，系统自动判断是否可以在借款
     */
    @LoginPermission
    @RequestMapping("borrow_apply")
    public String borrowApply(BidRequest bidRequest) {
        this.bidRequestService.applyBidRequest(bidRequest);
        return "redirect:/borrowInfo";
    }

    /**
     * 前台投资时查看借款详情
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("borrow_info")
    public String borrowInfo(Long id, Model model) {
        //用户未登录能看到的所有信息
        //bidRequest
        BidRequest bidRequest = bidRequestService.getBidRequestById(id);
        if (bidRequest != null) {
            model.addAttribute("bidRequest", bidRequest);
            //userInfo
            UserInfo applier = userInfoService.getUserInfo(bidRequest.getCreateUser().getId());
            model.addAttribute("userInfo", applier);

            //audits历史（审核历史）
            List<BidRequestAuditHistory> audits = bidRequestHistoryService.getHistorysByBidRequestId(id);
            model.addAttribute("audits", audits);
            //realAuth借款人实名信息
            RealAuth realAuth = realAuthService.getById(applier.getRealAuthId());
            model.addAttribute("realAuth", realAuth);
            //userFile借款人风控资料
            UserFileQueryObject qo = new UserFileQueryObject();
            //查看该用户已经审核通过的风控资料
            qo.setApplierId(applier.getId());
            qo.setState(BidConst.AUDIT_STATE_AUTH);
            List<UserFile> userFiles = userFileService.queryByPageCondition(qo);
            model.addAttribute("userFiles", userFiles);

            //查看当前投资的登陆人和借款人是否是一个人
            model.addAttribute("self", false);
            if (UserContext.getCurrent() != null) {
                if (UserContext.getCurrent().getId() == applier.getId()) {
                    //是一个人，那么不允许投资
                    model.addAttribute("self", true);
                } else {
                    //查询当前登陆人的账户信息
                    Account account = accountService.getAccountById(UserContext.getCurrent().getId());
                    model.addAttribute("account", account);
                }
            }
        }
        return "borrow_info";
    }

}
