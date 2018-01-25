package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.UserFile;
import com.yff.base.service.ISystemDictionaryService;
import com.yff.base.service.IUserFileService;
import com.yff.base.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 风控材料
 */
@Controller
public class UserFileController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private IUserFileService userFileService;

    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    /**
     * 查询该用户的风控材料基本逻辑：
     * 1. 查询该用户是否有还未选择风控类型的文件。如果有那么跳转到页面为该用户的风控材料选择类型
     * 2. 如果该用户所有的风控材料都已经选择了风控类型，那么跳转到风控材料提交页面。
     * @param model
     * @param request
     * @return
     */
    @LoginPermission
    @RequestMapping("userFile")
    public String userFile(Model model, HttpServletRequest request) {
        List<UserFile> files = userFileService.getUserFilesByFileType(UserContext.getCurrent().getId(),false);
        if(files != null && files.size() > 0) {
            //说明有未选择风控材料类型的材料
            model.addAttribute("userFiles", files);
            model.addAttribute("fileTypes", this.systemDictionaryService.getSystemDicsBySn("userFileType")) ;
            return "userFiles_commit";
        } else {
            //如果没有未选择风控类型的文件，那么已经选择了类型风控材料查询并展示出来
            //sessionId 这里的这个方法是 在火狐中 上传的图片默认是false是另外一个浏览器 此时传进去的值无法带上id 该方法解决
            String sessionId = request.getSession().getId();
            List<UserFile> userFiles = userFileService.getUserFilesByFileType(UserContext.getCurrent().getId(),true);
            model.addAttribute("sessionId", sessionId);
            model.addAttribute("userFiles", userFiles);
            return "userFiles";
        }
    }

    /**
     * 用户风控文件上传
     */
    @LoginPermission
    @RequestMapping("userFileUpload")
    @ResponseBody
    public String userFileUpload(MultipartFile file) {
        String basePath = servletContext.getRealPath("/upload");
        String fileName = UploadUtil.upload(file, basePath);
        fileName = "/upload/" + fileName;
        System.out.println(fileName);
        userFileService.apply(fileName);
        return fileName;
    }

    /**
     * 保存用户提交选择风控类型的风控材料
     * @param id:风控材料对应的id
     * @param fileType:该风控材料对应的类型id
     * @return
     */
    @RequestMapping("userFile_selectType")
    @ResponseBody
    public JSONResult userFileSelectType(Long[] id,Long[] fileType) {
        JSONResult result = new JSONResult();
        if(id != null && fileType != null && id.length == fileType.length) {
            userFileService.saveUserFiles(id,fileType);
        }
        return result;
    }

}
