package com.yff.base.service.impl;

import com.yff.base.common.BidConst;
import com.yff.base.common.UserContext;
import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.domain.UserFile;
import com.yff.base.domain.UserInfo;
import com.yff.base.mapper.UserFileMapper;
import com.yff.base.mapper.UserInfoMapper;
import com.yff.base.query.UserFileQueryObject;
import com.yff.base.service.IUserFileService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserFileServiceImpl implements IUserFileService {

    @Autowired
    private UserFileMapper userFileMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Override
    public void apply(String fileName) {
        UserFile userFile = new UserFile();
        userFile.setApplier(UserContext.getCurrent());
        userFile.setApplyTime(new Date());
        userFile.setImage(fileName);
        userFile.setFile(fileName);
        userFile.setState((byte) BidConst.AUDIT_STATE_NORMAL);
        userFile.setScore((byte) 0);
        userFileMapper.insertSelective(userFile);
    }

    @Override
    public List<UserFile> getUserFilesByFileType(Long loginInfoId, Boolean hasType) {
        return userFileMapper.queryUserFilesByFileType(loginInfoId, hasType);
    }

    @Override
    public void saveUserFiles(Long[] ids, Long[] fileTypes) {
        for (int i = 0; i < ids.length; i++) {
            UserFile userFile = this.userFileMapper.selectByPrimaryKey(ids[i]);
            SystemDictionaryItem item = new SystemDictionaryItem();
            item.setId(fileTypes[i]);
            userFile.setFileType(item);
            this.userFileMapper.updateByPrimaryKey(userFile);
        }
    }

    @Override
    public List<UserFile> queryByPageCondition(UserFileQueryObject qo) {
        return userFileMapper.queryByPageCondition(qo);
    }

    @Override
    public void audit(Long id, int score, String remark, int state) {
        //1.查询该风控材料
        UserFile uf = this.userFileMapper.selectByPrimaryKey(id);
        if (uf != null && uf.getState() == BidConst.AUDIT_STATE_NORMAL) {
            //2.说明该风控材料在审核状态
            uf.setAuditTime(new Date());
            uf.setAuditor(UserContext.getCurrent());
            uf.setRemark(remark);
            uf.setState((byte) state);

            if (state == BidConst.AUDIT_STATE_AUTH) {
                //3.审核通过
                uf.setScore((byte) score);
                UserInfo current = this.userInfoService.getUserInfo(uf.getApplier().getId());
                current.setAuthScore(current.getAuthScore() == null ? 0 : current.getAuthScore() + score);
                this.userInfoService.update(current);
            }
            this.userFileMapper.updateByPrimaryKey(uf);
        }
    }
}
