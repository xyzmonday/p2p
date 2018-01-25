package com.yff.base.service;

import com.yff.base.domain.RealAuth;
import com.yff.base.domain.VedioAuth;
import com.yff.base.query.VedioAuthQueryObject;

import java.util.List;

public interface IVedioAuthService {

    /**
     * 视频认证
     * @param id:本次视频认证申请人的id,注意视频认证和实名认证，风控材料认证等不同。
     *          这里的id是后台通过查询登陆用户，由后台指定本次需要对那个用户进行审核
     * @param remark:审核备注
     * @param state:拒绝或者审核通过
     */
    void audit(Long id,String remark,int state);


    /**
     * 按条件查询提交了的视频审核列表
     * @param qo
     * @return
     */
    List<VedioAuth> getAppliedVedioAuthList(VedioAuthQueryObject qo);
}
