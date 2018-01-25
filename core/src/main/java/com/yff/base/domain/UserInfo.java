package com.yff.base.domain;

import com.yff.base.common.BitStatesUtils;
import org.springframework.util.StringUtils;

public class UserInfo extends BaseDomain{
    private Integer version;
    private Long bitState = 0L;
    //用户实名认证  判断是否有对应的实名认证对象  冗余数据
    private String realName;
    private String idNumber;
    private String phoneNumber;
    //实名认证实体对象主键。当用户处于实名审核状态的时候，那么realAuthId不为空
    //如果实名认证审核拒绝那么realAuthId为空
    private Long realAuthId;
    private String email;
    // 用户的风控总分数
    private Integer authScore = 0;
/*    //收入等级
    private Long incomeGradeId;
    //婚姻状况
    private Long marriageId;
    //子女情况
    private Long kidCountId;
    //教育背景
    private Long educationBackgroundId;
    //住房条件
    private Long houseConditionId;*/

    private SystemDictionaryItem incomeGrade; // 收入
    private SystemDictionaryItem marriage; // 婚姻情况
    private SystemDictionaryItem kidCount; // 子女情况
    private SystemDictionaryItem educationBackground; // 学历
    private SystemDictionaryItem houseCondition; // 住房条件


    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @return
     */
    public String getAnonymousIdNumber() {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }


    // 判断是否已经绑定了手机
    public boolean getIsBindPhone() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_BIND_PHONE);
    }

    // 判断是否已经绑定看了银行卡
    public boolean getIsBindBank() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_HAS_BIND_BANK);
    }

    // 判断是否已经绑定了邮箱
    public boolean getIsBindEmail() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_BIND_EMAIL);
    }

    // 添加绑定的状态码
    public void addState(Long state) {
        bitState = BitStatesUtils.addState(this.bitState, state);
    }

    // 移除状态码
    public void removeState(Long state) {
        bitState = BitStatesUtils.removeState(this.bitState, state);
    }

    // 判断用户是否已经填写了基本资料
    public boolean getIsBasicInfo() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_BASIC_INFO);
    }

    // 判断用户是否已经实名认证
    public boolean getIsRealAuth() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_REAL_AUTH);
    }

    // 判断用户是否已经视频认证
    public boolean getIsVedioAuth() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_VEDIO_AUTH);
    }

    // 判断用户是否已经有一个借款在审核流程中
    public boolean getHasBidRequestInProcess() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
    }

    // 判断用户是否已经有一个提现在审核流程中
    public boolean getHasWithdrawInProcess() {
        return BitStatesUtils.hasState(this.bitState,
                BitStatesUtils.OP_HAS_WITHDRAW_PROCESS);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getBitState() {
        return bitState;
    }

    public void setBitState(Long bitState) {
        this.bitState = bitState;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Integer getAuthScore() {
        return authScore;
    }

    public void setAuthScore(Integer authScore) {
        this.authScore = authScore;
    }

    public Long getRealAuthId() {
        return realAuthId;
    }

    public void setRealAuthId(Long realAuthId) {
        this.realAuthId = realAuthId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public SystemDictionaryItem getIncomeGrade() {
        return incomeGrade;
    }

    public void setIncomeGrade(SystemDictionaryItem incomeGrade) {
        this.incomeGrade = incomeGrade;
    }

    public SystemDictionaryItem getMarriage() {
        return marriage;
    }

    public void setMarriage(SystemDictionaryItem marriage) {
        this.marriage = marriage;
    }

    public SystemDictionaryItem getKidCount() {
        return kidCount;
    }

    public void setKidCount(SystemDictionaryItem kidCount) {
        this.kidCount = kidCount;
    }

    public SystemDictionaryItem getEducationBackground() {
        return educationBackground;
    }

    public void setEducationBackground(SystemDictionaryItem educationBackground) {
        this.educationBackground = educationBackground;
    }

    public SystemDictionaryItem getHouseCondition() {
        return houseCondition;
    }

    public void setHouseCondition(SystemDictionaryItem houseCondition) {
        this.houseCondition = houseCondition;
    }
}