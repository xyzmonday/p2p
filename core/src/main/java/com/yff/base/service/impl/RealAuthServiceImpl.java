package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.BitStatesUtils;
import com.yff.base.common.UserContext;
import com.yff.base.domain.RealAuth;
import com.yff.base.domain.UserInfo;
import com.yff.base.event.RealAuthSuccessEvent;
import com.yff.base.mapper.RealAuthMapper;
import com.yff.base.mapper.UserInfoMapper;
import com.yff.base.query.RealAuthQueryObject;
import com.yff.base.service.IRealAuthService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 实名认证
 */
@Service
public class RealAuthServiceImpl implements IRealAuthService {

    @Autowired
    private RealAuthMapper realAuthMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public RealAuth getById(Long id) {
        return realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public void apply(RealAuth realAuth) {
        //1.判断当前用户是否还没有实名认证，而且不处于待审核状态
        UserInfo current = userInfoService.getCurrent();
        if (!current.getIsRealAuth() && current.getRealAuthId() == null) {
            //2.创建实名认证实体(注意这里每次申请都必须生成一个新的对象)
            //表示当前用户没有提交过  可以保存realAuth对象
            RealAuth ra = new RealAuth();
            ra.setRealName(realAuth.getRealName());
            ra.setSex(realAuth.getSex());
            ra.setIdNumber(realAuth.getIdNumber());
            ra.setBirthDate(realAuth.getBirthDate());
            ra.setAddress(realAuth.getAddress());
            ra.setImage1(realAuth.getImage1());
            ra.setImage2(realAuth.getImage2());

            ra.setState((byte) BidConst.AUDIT_STATE_NORMAL);
            ra.setApplier(UserContext.getCurrent());
            ra.setApplyTime(new Date());
            realAuthMapper.insertSelective(ra);
            //3.更新当前用户
            current.setRealAuthId(ra.getId());
            this.userInfoService.update(current);
        }
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //1.通过id查询需要审核的信息
        RealAuth ra = this.realAuthMapper.selectByPrimaryKey(id);
        if (ra != null && ra.getState() == BidConst.AUDIT_STATE_NORMAL) {
            //2.设置审核的属性
            ra.setAuditor(UserContext.getCurrent());
            ra.setAuditTime(new Date());
            ra.setRemark(remark);
            ra.setState((byte) state);
            //设置用户realAuthId
            UserInfo applier = this.userInfoService.getUserInfo(ra.getApplier().getId());

            if (state == BidConst.AUDIT_STATE_AUTH) {
                if (!applier.getIsRealAuth()) {
                    //如果没有实名认证
                    //审核通过,注意这里如果审核通过了，realAuthId依然在。因为此时isRealAuth=true
                    applier.addState(BitStatesUtils.OP_REAL_AUTH);
                    applier.setRealName(ra.getRealName());
                    applier.setIdNumber(ra.getIdNumber());
                    //发送一个实名认证成功的消息
                    applicationContext.publishEvent(new RealAuthSuccessEvent(this, ra));
                }
            } else if (state == BidConst.AUDIT_STATE_REJECT) {
                //审核拒绝
                applier.setRealAuthId(null);
            }
            this.userInfoService.update(applier);
            this.realAuthMapper.updateByPrimaryKey(ra);
        }
    }

    @Override
    public List<RealAuth> getAppliedRealAuthList(RealAuthQueryObject qo) {
        return realAuthMapper.queryByPageCondition(qo);
    }
}
