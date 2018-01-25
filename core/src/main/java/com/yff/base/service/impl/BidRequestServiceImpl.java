package com.yff.base.service.impl;

import com.yff.base.common.*;
import com.yff.base.domain.*;
import com.yff.base.mapper.*;
import com.yff.base.query.BidRequestQueryObject;
import com.yff.base.query.PaymentScheduleQueryObject;
import com.yff.base.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BidRequestServiceImpl implements IBidRequestService {

    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    /**
     * 乐观锁的控制
     */
    @Override
    public void update(BidRequest bidRequest) {
        int count = this.bidRequestMapper.updateByPrimaryKey(bidRequest);
        if (count <= 0) {
            throw new RuntimeException("乐观锁错误bidRequest:" + bidRequest.getId());
        }
    }

    @Override
    public boolean canApplyBidRequest(Long loginInfoId) {
        //1.获取当前用户信息，
        UserInfo userInfo = userInfoService.getUserInfo(loginInfoId);
        //2.判断是否通过1.基本资料；2.实名认证；3.视频认证；4.风控资料；5，没有借款申请
        boolean isCan = userInfo != null
                && userInfo.getIsBasicInfo()
                && userInfo.getIsRealAuth()
                && userInfo.getIsVedioAuth()
                && userInfo.getAuthScore() >= BidConst.CREDIT_BORROW_SCORE;
        return isCan;
    }

    /**
     * 借款申请
     *
     * @param bidRequest
     */
    @Override
    public void applyBidRequest(BidRequest bidRequest) {
        //1.得到当前用户和当前账户信息
        UserInfo userInfo = this.userInfoService.getUserInfo(UserContext.getCurrent().getId());
        Account account = this.accountService.getAccountById(UserContext.getCurrent().getId());
        //2.判断该借款申请是否满足要求（因为这里审核有多个状态，所以每次申请借款需要再次判断状态）
        boolean isCanApply =
                //可以申请借款
                this.canApplyBidRequest(userInfo.getId())
                        //没有申请处于审核流程
                        && !userInfo.getHasBidRequestInProcess()
                        //借款申请金额
                        && bidRequest.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT) >= 0 //大于等于最小借款金额
                        && bidRequest.getBidRequestAmount().compareTo(account.getRemainBorrowLimit()) <= 0 //小于等于账户最大借款限额
                        && bidRequest.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE) <= 0//系统最低年化率<= 年化率 <= 系统最高年化率
                        && bidRequest.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT) >= 0; //系统最小投标 <= 最小投标金额

        if (isCanApply) {
            BidRequest br = new BidRequest();
            //借款金额
            br.setBidRequestAmount(bidRequest.getBidRequestAmount());
            //借款年化利率
            br.setCurrentRate(bidRequest.getCurrentRate());
            //借款描述
            br.setDescription(bidRequest.getDescription());
            //招标天数
            br.setDisableDays(bidRequest.getDisableDays());
            //最新投标金额
            br.setMinBidAmount(bidRequest.getMinBidAmount());
            //还款类型
            br.setReturnType(bidRequest.getReturnType());
            //借款标题
            br.setTitle(bidRequest.getTitle());
            //借款期限
            br.setMonthes2Return(bidRequest.getMonthes2Return());
            //借款申请时间
            br.setApplyTime(new Date());
            //借款申请人
            br.setCreateUser(UserContext.getCurrent());
            //借款类型
            br.setBidRequestType((byte) BidConst.BIDREQUEST_TYPE_NORMAL);
            //设置当前标的状态 .待发布
            br.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
            //设置总的回报金额(总利息)    工具类中计算
            br.setTotalRewardAmount(CalculatetUtil.calTotalInterest(
                    bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
                    bidRequest.getCurrentRate(), bidRequest.getMonthes2Return()));

            //3.如果满足要求，那么保存该借款申请
            this.bidRequestMapper.insert(br);

            //4.更改用户信息（该字段用来判断该用户是否有标在招标中，满标一审，满标二审状态。如果在这些状态那么不允许申请借款）
            userInfo.addState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            this.userInfoService.update(userInfo);
        }
    }

    @Override
    public List<BidRequest> queryByPageCondition(BidRequestQueryObject qo) {
        return bidRequestMapper.queryByPageCondition(qo);
    }

    /**
     * 借款发布审核。借款的状态从待发布->投标中
     *
     * @param id:需要审核的标的id
     * @param state:审核状态（通过或者拒绝）
     * @param remark:审核备注
     */
    @Override
    public void publishAudit(Long id, int state, String remark) {
        //1.获取需要审核的标
        BidRequest br = this.bidRequestMapper.selectByPrimaryKey(id);
        //2.判断标是否处于待审核状态
        if (br != null && br.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING) {
            //3.该标处于待发布审核状态,创建审核历史
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            //发标前审核
            history.setBidRequestId(br.getId());
            //设置借款申请人
            history.setApplier(br.getCreateUser());
            history.setApplyTime(br.getApplyTime());
            //设置审核信息
            history.setAuditType((byte) BidRequestAuditHistory.PUBLISH_AUDIT);
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            //审核备注
            history.setRemark(remark);
            //审核状态（通过或者拒绝）
            history.setState((byte) state);
            this.bidRequestAuditHistoryMapper.insert(history);
            //4.需要标的状态（通过或者拒绝）
            //风控意见
            br.setNote(remark);
            if (state == BidConst.AUDIT_STATE_AUTH) {
                //审核通过
                //发布时间
                br.setPublishTime(new Date());
                //该标的截止时间（从投标截止时间）
                br.setDisableDate(DateUtils.addDays(new Date(), br.getDisableDays()));
                //设置该标的状态（招标中）
                br.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_BIDDING);
            } else {
                //审核拒绝
                br.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                UserInfo userInfo = this.userInfoService.getUserInfo(br.getCreateUser().getId());
                userInfo.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                this.userInfoService.update(userInfo);
            }
            this.update(br);
        }
    }

    @Override
    public BidRequest getBidRequestById(Long id) {
        return this.bidRequestMapper.selectByPrimaryKey(id);
    }

    /**
     * 投标
     *
     * @param bidRequestId:标的id
     * @param amount:投资金额
     */
    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        BidRequest br = this.bidRequestMapper.selectByPrimaryKey(bidRequestId);
        //当前投资人的账户
        Account currentAccount = this.accountService.getAccountById(UserContext.getCurrent().getId());
        //1.借款存在
        if (br != null && currentAccount != null
                //2.借款处于投标中
                && br.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING
                //3.当前投资用户不是借款用户
                && !br.getCreateUser().getId().equals(UserContext.getCurrent().getId())
                //4.投标金额大于最小投标金额
                && amount.compareTo(br.getMinBidAmount()) >= 0
                //5.投标金额小于剩余投标金额
                && amount.compareTo(br.getRemainAmount()) <= 0
                //5.当前用户的可用金额大于投标金额
                && currentAccount.getUsableAmount().compareTo(amount) >= 0) {

            //计算该借款剩余还未投满的金额
            if (br.getRemainAmount().compareTo(br.getMinBidAmount()) < 0) {
                //如果剩余还未投满的金额小于最小投标金额，那么只能投资将剩余的所有投标金额
                throw new RuntimeException("投标金额" + amount + "小于剩余投标金额，请一次性投" + br.getRemainAmount());
            }
            //5.执行投标
            Bid bid = new Bid();
            bid.setActualRate(br.getCurrentRate());
            //投资金额
            bid.setAvailableAmount(amount);
            bid.setBidRequestId(bidRequestId);
            bid.setBidTime(new Date());
            bid.setBidRequestTitle(br.getTitle());
            bid.setBidUser(UserContext.getCurrent());
            //  标的状态
            bid.setBidRequestState(br.getBidRequestState());
            this.bidMapper.insertSelective(bid);
            //6.得到投标人，修改账户信息
            currentAccount.setUsableAmount(currentAccount.getUsableAmount().subtract(amount));
            currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().add(amount));
            //注意一定要修改了账户金额之后才去生成流水
            //6.生成一条投标流水
            this.accountFlowService.bid(bid, currentAccount);
            //7.修改借款的信息
            br.setBidCount(br.getBidCount() + 1);
            //修改已经投标的金额
            br.setCurrentSum(br.getCurrentSum().add(amount));


            //8.判断当前标是否投满，如果投满立刻进入满标审核状态
            if (br.getCurrentSum().equals(br.getBidRequestAmount())) {
                br.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                //当借款的状态发生改变的时候,需要修改所有投标的状态
                this.bidMapper.updateBidState(bidRequestId, (byte) BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
            }
            this.accountService.updateAccount(currentAccount);
            this.bidRequestMapper.updateByPrimaryKey(br);
        }
    }

    @Override
    public void fullAudit1(Long bidRequestId, int state, String remark) {
        //1.获取需要审核的标
        BidRequest bidRequest = this.bidRequestMapper.selectByPrimaryKey(bidRequestId);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1) {
            //2.创建审核流程对象
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            history.setApplier(bidRequest.getCreateUser());
            history.setApplyTime(new Date());
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            history.setRemark(remark);
            history.setBidRequestId(bidRequestId);
            history.setState((byte) state);
            history.setAuditType(BidRequestAuditHistory.FULL_AUDIT1);
            this.bidRequestAuditHistoryMapper.insert(history);
            //3.审核通过。满标二审
            if (state == BidConst.AUDIT_STATE_AUTH) {
                //修改借款状态
                bidRequest.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                //修改标的状态
                this.bidMapper.updateBidState(bidRequestId, (byte) BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            } else {
                //4.如果审核不通过。审核失败,修改标的状态和投标状态
                bidRequest.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_REJECTED);
                this.bidMapper.updateBidState(bidRequest.getId(), (byte) BidConst.BIDREQUEST_STATE_REJECTED);
                //退钱
                returnMoney(bidRequest, 1);
                //修改借款人的状态，使其可以再次借款
                UserInfo borrowUser = this.userInfoService.getUserInfo(bidRequest.getCreateUser().getId());
                borrowUser.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                this.userInfoService.update(borrowUser);
            }
        }
        this.update(bidRequest);
    }

    @Override
    public void fullAudit2(Long bidRequestId, int state, String remark) {
        //1.获取需要审核的标
        BidRequest bidRequest = this.bidRequestMapper.selectByPrimaryKey(bidRequestId);
        if (bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            //2.创建审核流程对象
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            history.setApplier(bidRequest.getCreateUser());
            history.setApplyTime(new Date());
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            history.setRemark(remark);
            history.setBidRequestId(bidRequestId);
            history.setState((byte) state);
            history.setAuditType(BidRequestAuditHistory.FULL_AUDIT2);
            this.bidRequestAuditHistoryMapper.insert(history);
            //3.审核通过。满标二审
            if (state == BidConst.AUDIT_STATE_AUTH) {
                //满标二审对借款。修改借款状态（还款中）
                bidRequest.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_PAYING_BACK);//*1修改借款的状态
                this.bidMapper.updateBidState(bidRequestId, (byte) BidConst.BIDREQUEST_STATE_PAYING_BACK);    //*2修改投标的状态
                //满标二审对借款人。收款操作（账户余额增加，生成收款流水，生成带还本息，减少可用信用额度，移除借款人借款进行中的状态，支付借款手续费，
                //2从借款人角度
                //*2.1借款人账户余额增加 , 生成同一条账户流水
                Account borrowAccount = this.accountService.getAccountById(bidRequest.getCreateUser().getId());
                borrowAccount.setUsableAmount(borrowAccount.getUsableAmount().add(bidRequest.getBidRequestAmount()));
                this.accountFlowService.borrowSuccess(bidRequest, borrowAccount);
                //*2.2生成带还本息
                borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount().add(bidRequest.getBidRequestAmount())
                        .add(bidRequest.getTotalRewardAmount()));
                //*2.3减少剩余借款额度
                borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                //*2.4 支付借款收学费流水，平台收取借款手续费，平台账户流水）
                BigDecimal borrowChargeFee = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                //生成手续费流水
                this.accountFlowService.borrowChargeFee(borrowChargeFee, borrowAccount, bidRequest);  // 生成的手续费流水
                //*2.5平台收取手续费(两个对象 --> 账户和账户流水)
                this.systemAccountService.chargeBorrowFee(borrowChargeFee, bidRequest);
                //这里手续费从哪里来
                this.accountService.updateAccount(borrowAccount);
                //3满标二审对投资人。（遍历投标根据标减少投资人的冻结金额，生成成功投资流水，计算待收利息和待收本金）
                //*3.1.遍历投标
                Map<Long, Account> updates = new HashMap<>();
                for (Bid bid : bidRequest.getBids()) {
                    //*3.2减少账户的冻结金额,增加投标成功的流水
                    Long bidAccountId = bid.getBidUser().getId();
                    Account bidAccount = updates.get(bidAccountId);
                    if (bidAccount == null) {
                        bidAccount = this.accountService.getAccountById(bidAccountId);
                        updates.put(bidAccountId, bidAccount);
                    }
                    bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    this.accountFlowService.bidSuccess(bid, bidAccount);
                }
                //4满标二审之后的流程。生成还款对象和回款对象，以后借款人和投资人查看还款和回款明细
                //还款明细记录的是本期还款每一个人应该得到的回款
                //生成针对这个借款的还款信息和回款信息
                //*4.1创建还款计划
                List<PaymentSchedule> paymentSchedules = createPaymentSchedule(bidRequest);
                //*3增加待收利息和待收本金
                //遍历还款对象和回款对象
                for (PaymentSchedule ps : paymentSchedules) {
                    for (PaymentScheduleDetail psd : ps.getPaymentScheduleDetails()) {
                        //得到收款人的账户
                        Account bidAccount = updates.get(psd.getToLoginInfoId());
                        //待收本金
                        bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().add(psd.getPrincipal()));
                        bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(psd.getInterest()));
                    }
                }
                for (Account account : updates.values()) {
                    this.accountService.updateAccount(account);
                }

            } else {
                //5.如果审核不通过。审核失败,修改标的状态和投标状态
                bidRequest.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_REJECTED);
                this.bidMapper.updateBidState(bidRequest.getId(), (byte) BidConst.BIDREQUEST_STATE_REJECTED);
                //退钱。遍历投标,每个投标人的账户可用金额增加 冻结金额减少 , 创建一条取消投标流水
                returnMoney(bidRequest, 2);

            }
            //6.对于借款人  移除对应的状态码(满标二审不管成功还是失败都要移除状态码)
            UserInfo borrowUser = this.userInfoService.getUserInfo(bidRequest.getCreateUser().getId());
            borrowUser.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            this.userInfoService.update(borrowUser);
            this.update(bidRequest);
        }
    }

    @Override
    public void returnMoney(Long id) {
        //得到还款对象  判断状态
        PaymentSchedule ps = this.paymentScheduleMapper.selectByPrimaryKey(id);
        //1处于待还款  ,  并且当前是为自己的借款还款
        if (ps.getState() == BidConst.PAYMENT_STATE_NORMAL
                && ps.getBorrowUser().getId().equals(UserContext.getCurrent().getId())) {
            //2借款人账户余额大于还款金额
            Account returnAccount = this.accountService.getAccountById(ps.getBorrowUser().getId());
            if (returnAccount.getUsableAmount().compareTo(ps.getTotalAmount()) >= 0) {
                //执行还款
                //1对于还款对象,修改状态
                ps.setState((byte) BidConst.PAYMENT_STATE_DONE);
                ps.setPayDate(new Date());
                this.paymentScheduleMapper.updateByPrimaryKey(ps);
                //2对于借款人
                //**2.1可用金额件少生成还款流水
                returnAccount.setUsableAmount(returnAccount.getUsableAmount().subtract(ps.getTotalAmount()));
                this.accountFlowService.returnMoney(returnAccount, ps);
                //**2.2待还金额减少 剩余信用额度增加
                returnAccount.setUnReturnAmount(returnAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));
                returnAccount.setRemainBorrowLimit(returnAccount.getRemainBorrowLimit().add(ps.getPrincipal()));
                //3对于投资人
                //**3.1遍历还款明细对象
                Map<Long, Account> updates = new HashMap<>();
                for (PaymentScheduleDetail psd : ps.getPaymentScheduleDetails()) {
                    Long bidAccountId = psd.getToLoginInfoId();
                    Account bidAccount = updates.get(bidAccountId);
                    if (bidAccount == null) {
                        bidAccount = this.accountService.getAccountById(bidAccountId);
                        updates.put(bidAccountId, bidAccount);
                    }
                    //**3.2的到投资人对象,增加账户的可用余额.生成收款流水
                    bidAccount.setUsableAmount(bidAccount.getUsableAmount().add(psd.getTotalAmount()));
                    this.accountFlowService.receiveMoney(bidAccount, psd);
                    //**3.3减少待收本金和待收利息
                    bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                    bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                    //**3.4支付利息管理 费,生成的支付利息管理费流水
                    BigDecimal interestChargeFee = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                    bidAccount.setUsableAmount(bidAccount.getUsableAmount().subtract(interestChargeFee));
                    this.accountFlowService.interestChargeFee(bidAccount, interestChargeFee, psd);
                    //**3.5系统账户收取利息管理费
                    this.systemAccountService.chargeInterest(interestChargeFee, psd);
                    //修改每一期回款
                    psd.setPayDate(new Date());
                    this.paymentScheduleDetailMapper.updateByPrimaryKey(psd);
                }
                for (Account account : updates.values()) {
                    this.accountService.updateAccount(account);
                }
                //4对于借款,如果当前还的是最后一期,借款状态变成已完成状态 ,修改投标信息
                PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
                qo.setBidRequestId(ps.getBidRequestId());
                qo.setPageSize(-1);
                List<PaymentSchedule> psss = this.paymentScheduleMapper.queryByPageCondition(qo);
                System.out.println(psss);  //空
                //遍历查询是否还有没有还款的还款计划
                boolean unReturn = false;
                for (PaymentSchedule temp : psss) {
                    if (temp.getState() == BidConst.PAYMENT_STATE_NORMAL
                            || temp.getState() == BidConst.PAYMENT_STATE_OVERDUE) {
                        unReturn = true;
                        break;
                    }
                }
                if (!unReturn) { //说明是最后一期
                    BidRequest bidRequest = this.getBidRequestById(ps.getBidRequestId());
                    bidRequest.setBidRequestState((byte) BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                    this.update(bidRequest);
                    this.bidMapper.updateBidState(bidRequest.getId(), (byte) BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                }
                this.accountService.updateAccount(returnAccount);
            }
        }
    }

    /**
     * 创建针对这个借款的还款信息和汇款信息
     */
    private List<PaymentSchedule> createPaymentSchedule(BidRequest bidRequest) {
        Date now = new Date();
        List<PaymentSchedule> ret = new ArrayList<>();
        //用于累加本金
        BigDecimal totalPrincipal = BidConst.ZERO;
        //用于累加利息
        BigDecimal totalInterest = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getMonthes2Return(); i++) {
            //每一期创建一个还款计划
            PaymentSchedule ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setBorrowUser(bidRequest.getCreateUser());
            ps.setDeadLine(DateUtils.addDays(now, i + 1));
            ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
            ps.setReturnType(bidRequest.getReturnType());
            ps.setMonthIndex((byte) (i + 1));
            //带还状态
            ps.setState((byte) BidConst.PAYMENT_STATE_NORMAL);
            if (i < bidRequest.getMonthes2Return() - 1) {
                //计算这一期的利息
                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                ps.setMonthIndex((byte) (i + 1));
                //计算每一期的总还款金额
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(), bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                totalPrincipal = totalPrincipal.add(ps.getPrincipal());
                totalInterest = totalInterest.add(ps.getInterest());
            } else {
                //最后一期
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(totalInterest));
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(totalPrincipal));
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }
            this.paymentScheduleMapper.insert(ps);
            //创建每期还款对象对于的汇款明细
            createPaymentScheduleDetail(ps, bidRequest);
            ret.add(ps);
        }
        return ret;
    }

    /**
     * 创建针对每一期还款的回款对象
     *
     * @param ps
     * @param bidRequest
     */
    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {
        //用于 累加 本期还款本金
        BigDecimal totalPrincipal = BidConst.ZERO;
        //用于累加 总金额 (本金+利息)
        BigDecimal totalAmount = BidConst.ZERO;
        for (int i = 0; i < bidRequest.getBids().size(); i++) {
            Bid bid = bidRequest.getBids().get(i);
            //针对每一个投标创建已个回款对象
            PaymentScheduleDetail psd = new PaymentScheduleDetail();
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidRequestId(bidRequest.getId());
            psd.setBidId(bid.getId());
            psd.setDeadLine(ps.getDeadLine());
            psd.setFromLoginInfo(bidRequest.getCreateUser());
            psd.setMonthIndex((byte) (i + 1));
            psd.setToLoginInfoId(bid.getBidUser().getId());
            psd.setPaymentScheduleId(ps.getId());
            psd.setReturnType(bidRequest.getReturnType());
            if (i < bidRequest.getBids().size() - 1) {
                // 回款本金 = 投标金额 / 借款金额 * 本期还款本金
                psd.setPrincipal(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),
                        BidConst.CAL_SCALE, RoundingMode.HALF_UP).multiply(ps.getPrincipal()
                        .setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP)));
                // 汇款利息 = 投标金额/ 借款金额 * 本期还款利息
                psd.setInterest(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(),
                        BidConst.CAL_SCALE, RoundingMode.HALF_UP).multiply(ps.getInterest()
                        .setScale(BidConst.CAL_SCALE, RoundingMode.HALF_UP)));
                psd.setTotalAmount(psd.getInterest().add(psd.getPrincipal()));

                totalPrincipal = totalPrincipal.add(psd.getPrincipal());   //本金
                totalAmount = totalAmount.add(psd.getTotalAmount());        //本息

            } else {
                //最后一个回款明细
                psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal)); //本期的剩余本金
                psd.setTotalAmount(ps.getTotalAmount().subtract(totalAmount)); //本期的剩余本息
                psd.setInterest(psd.getTotalAmount().subtract(psd.getPrincipal()));   //剩余的利息
            }
            this.paymentScheduleDetailMapper.insert(psd);
            ps.getPaymentScheduleDetails().add(psd);
        }
    }


    /**
     * 退钱。这里考虑审核期类，投资的利息。该利息有品台支付.
     * 这里需要考虑如果一个用户进行了多次投标，那么这里可以考虑将所有投标的投资一次性修改。
     * 而且对于每一个投标都需要生成一个退款流水
     *
     * @param bidRequest
     */
    private void returnMoney(BidRequest bidRequest, int state) {
        List<Bid> bids = bidRequest.getBids();
        Map<Long, Account> updates = new HashMap<>();
        //1.遍历投标列表
        for (Bid bid : bids) {
            //2.找到投标的人的账户
            Long accountId = bid.getBidUser().getId();
            Account account = updates.get(accountId);
            if (account == null) {
                account = this.accountService.getAccountById(bid.getBidUser().getId());
                updates.put(accountId, account);
            }
            //3.账户可用余额增加，冻结金额减少
            account.setUsableAmount(account.getUsableAmount().add(bid.getAvailableAmount()));
            account.setFreezedAmount(account.getFreezedAmount().subtract(bid.getAvailableAmount()));
            //4.生成退款流水（某一个投标都需要一个退款流水）
            this.accountFlowService.returnMoney(bid, account, state);
        }
        //统一更新可用余额和冻结金额
        for (Account account : updates.values()) {
            this.accountService.updateAccount(account);
        }
    }
}
