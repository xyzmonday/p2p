package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;
import com.yff.base.common.BidConst;
import com.yff.base.common.DecimalFormatUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidRequest extends BaseDomain{

    private Integer version = 0;
    //还款类型
    private Byte returnType;
    //还款类型（等额等息）
    private Byte bidRequestType;
    //借款类型（信用标）
    private Byte bidRequestState;
    //借款总金额
    private BigDecimal bidRequestAmount;
    //年化利率
    private BigDecimal currentRate;
    //还款利率
    private Byte monthes2Return;
    //已经投标的次数
    private Integer bidCount = 0;
    //总回报金额（总利息）
    private BigDecimal totalRewardAmount;
    //当前已经投标的总金额
    private BigDecimal currentSum = BidConst.ZERO;
    //借款标题
    private String title;
    //借款描述
    private String description;
    //风控意见
    private String note;
    //投标截止日期
    private Date disableDate;
    //借款人
    private LoginInfo createUser;
    //招标天数
    private Byte disableDays;
    //最小借款金额
    private BigDecimal minBidAmount;
    //申请时间
    private Date applyTime;
    //发布时间
    private Date publishTime;

    private List<Bid> bids;


    //获取剩余还未投满的金额 (+:add  -:subtract * :multiply  / :divide)
    public BigDecimal  getRemainAmount(){
        return DecimalFormatUtil.formatBigDecimal(this.bidRequestAmount.subtract(this.currentSum),BidConst.DISPLAY_SCALE);
    }

    /**
     * 获取投标进度条
     * @return
     */
    public BigDecimal getPercent(){
        //当前已经投标的总金额/借款总金额
        return this.currentSum.divide(this.bidRequestAmount, BidConst.DISPLAY_SCALE, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

    }

    public String getBidRequestStateDisplay() {
        switch (this.bidRequestState) {
            case BidConst.BIDREQUEST_STATE_PUBLISH_PENDING:
                return "待发布";
            case BidConst.BIDREQUEST_STATE_BIDDING:
                return "招标中";
            case BidConst.BIDREQUEST_STATE_UNDO:
                return "已撤销";
            case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:
                return "流标";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:
                return "满标一审";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:
                return "满标二审";
            case BidConst.BIDREQUEST_STATE_REJECTED:
                return "满标审核被拒";
            case BidConst.BIDREQUEST_STATE_PAYING_BACK:
                return "还款中";
            case BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK:
                return "完成";
            case BidConst.BIDREQUEST_STATE_PAY_BACK_OVERDUE:
                return "逾期";
            case BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE:
                return "发标拒绝";
            default:
                return "";
        }
    }

    public String getReturnTypeDisplay() {
        return returnType == BidConst.RETURN_TYPE_MONTH_INTEREST ? "按月到期"
                : "等额本息";
    }


    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("username", this.createUser.getUsername());
        json.put("title", title);
        json.put("bidRequestAmount", bidRequestAmount);
        json.put("currentRate", currentRate);
        json.put("monthes2Return", monthes2Return);
        json.put("returnType", getReturnTypeDisplay());
        json.put("totalRewardAmount", totalRewardAmount);
        return JSONObject.toJSONString(json);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public Byte getReturnType() {
        return returnType;
    }

    public void setReturnType(Byte returnType) {
        this.returnType = returnType;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Byte getBidRequestType() {
        return bidRequestType;
    }

    public void setBidRequestType(Byte bidRequestType) {
        this.bidRequestType = bidRequestType;
    }

    public Byte getBidRequestState() {
        return bidRequestState;
    }

    public void setBidRequestState(Byte bidRequestState) {
        this.bidRequestState = bidRequestState;
    }

    public BigDecimal getBidRequestAmount() {
        return bidRequestAmount;
    }

    public void setBidRequestAmount(BigDecimal bidRequestAmount) {
        this.bidRequestAmount = bidRequestAmount;
    }

    public BigDecimal getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(BigDecimal currentRate) {
        this.currentRate = currentRate;
    }

    public Byte getMonthes2Return() {
        return monthes2Return;
    }

    public void setMonthes2Return(Byte monthes2Return) {
        this.monthes2Return = monthes2Return;
    }

    public Integer getBidCount() {
        return bidCount;
    }

    public void setBidCount(Integer bidCount) {
        this.bidCount = bidCount;
    }

    public BigDecimal getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(BigDecimal totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public BigDecimal getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(BigDecimal currentSum) {
        this.currentSum = currentSum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Date getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(Date disableDate) {
        this.disableDate = disableDate;
    }

    public LoginInfo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(LoginInfo createUser) {
        this.createUser = createUser;
    }

    public Byte getDisableDays() {
        return disableDays;
    }

    public void setDisableDays(Byte disableDays) {
        this.disableDays = disableDays;
    }

    public BigDecimal getMinBidAmount() {
        return minBidAmount;
    }

    public void setMinBidAmount(BigDecimal minBidAmount) {
        this.minBidAmount = minBidAmount;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
}