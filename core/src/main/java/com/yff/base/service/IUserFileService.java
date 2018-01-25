package com.yff.base.service;

import com.yff.base.domain.UserFile;
import com.yff.base.query.UserFileQueryObject;

import java.util.List;

public interface IUserFileService {

    /**
     * 上传一个风控资料
     * @param fileName
     */
    void apply(String fileName);

    /**
     * 查询某一个用户所有的风控文件
     * @Param hasType:是否已经选中了风控材料类型
     * @param loginInfoId
     * @return
     */
    List<UserFile> getUserFilesByFileType(Long loginInfoId,Boolean hasType);

    /**
     * 保存用户提交的风控材料
     * @param ids:风控材料id列表
     * @param fileTypes:该风控材料对应的类型id列表
     */
    void saveUserFiles(Long[] ids, Long[] fileTypes);

    List<UserFile> queryByPageCondition(UserFileQueryObject qo);

    /**
     * 风控材料审核
     * @param id:风控材料id
     * @param score:评审得分
     * @param remark:备注
     * @param state:状态（拒绝或者通过）
     */
    void audit(Long id,int score,String remark,int state);
}
