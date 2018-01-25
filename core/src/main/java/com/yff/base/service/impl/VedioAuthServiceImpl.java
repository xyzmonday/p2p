package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.BitStatesUtils;
import com.yff.base.common.UserContext;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.domain.VedioAuth;
import com.yff.base.mapper.UserInfoMapper;
import com.yff.base.mapper.VedioAuthMapper;
import com.yff.base.query.VedioAuthQueryObject;
import com.yff.base.service.IUserInfoService;
import com.yff.base.service.IVedioAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VedioAuthServiceImpl implements IVedioAuthService {

    @Autowired
    private VedioAuthMapper vedioAuthMapper;
    @Autowired
    private IUserInfoService userInfoService;


    @Override
    public void audit(Long id, String remark, int state) {
        //1.查询当前用户是否已经视频认证了
        UserInfo applier = userInfoService.getUserInfo(id);
        if (applier != null && !applier.getIsVedioAuth()) {
            //没有视频审核
            VedioAuth va = new VedioAuth();
            LoginInfo li = new LoginInfo();
            li.setId(id);
            va.setApplier(li);
            va.setApplyTime(new Date());
            va.setRemark(remark);
            va.setState((byte) state);
            va.setAuditor(UserContext.getCurrent());
            va.setAuditTime(new Date());

            if (state == BidConst.AUDIT_STATE_AUTH) {
                //审核通过
                applier.addState(BitStatesUtils.OP_VEDIO_AUTH);
                this.userInfoService.update(applier);
            }
            //插入审核信息
            vedioAuthMapper.insertSelective(va);
        }
    }

    @Override
    public List<VedioAuth> getAppliedVedioAuthList(VedioAuthQueryObject qo) {
        return vedioAuthMapper.queryByPageCondition(qo);
    }
}
