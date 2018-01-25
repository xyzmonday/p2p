package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.RealAuth;
import com.yff.base.domain.UserInfo;
import com.yff.base.service.IRealAuthService;
import com.yff.base.service.IUserInfoService;
import com.yff.base.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

@Controller
public class RealAuthController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private ServletContext servletContext;

    /**
     * 加载实名认证菜单。
     * 用户实名认证有三个状态
     * 1.未提交实名认证
     * 2.提交了实名认证，待审核
     * 3.已经实名认证
     * userInfo保存了用户实名认证的状态。使用isRealAuth判断是否已经实名认证，
     * realAuthId来判断用户是否提交了实名认证，处于待审核的状态。如果realAuthId为空那么说明一定不是待审核状态。
     *
     * isRealAuth=false:为实名认证;
     * isRealAuth=true:已经实名认证;
     * isRealAuth=false,realAuthId=null,没有实名认证;
     * isRealAuth=false,realAuthId!=null,没有实名认证，提交了实名认证，处于待审核状态
     *
     * @param model
     * @return
     */
    @LoginPermission
    @RequestMapping("realAuth")
    public String realAuthSave(Model model) {
        //1.得到当前用户
        UserInfo userInfo = this.userInfoService.getCurrent();
        //2.判断用户是否已经实名验证
        if(userInfo.getIsRealAuth()) {
            //已经实名认证
            //  根据userInfo里面的realAuthId获取到实名验证的实体对象
            //  并且将auditing=false
            model.addAttribute("realAuth",this.realAuthService.getById(userInfo.getRealAuthId()));
            //已经实名认证，那么跳转到realAuth_result页面，显示已经认证的信息
            model.addAttribute("auditing",false);
            return "realAuth_result";
        }

        //还没有实名认证
        if(!StringUtils.isEmpty(userInfo.getRealAuthId())) {
            //审核中
            //3.如果没有实名认证，而且userInfo的realAuthId不为空（已经提交实名认证申请），auditing=true并且跳转到realAuth_result
            model.addAttribute("auditing",true);
            return "realAuth_result";
        } else {
            //4.如果没有实名认证，而且userInfo的realAuthId为空，那么则跳转到realAuth
            return "realAuth";
        }
    }

    /**
     * 身份证图片上传，注意这里返回的是一个图片绝对路径的字符串
     * @param file
     * @param model
     * @return
     */
    @RequestMapping("realAuthUpload")
    @ResponseBody
    public String realAuthUpload(MultipartFile file, Model model) {
        //1.得到文件保存目录的绝对路径
        String basePath = servletContext.getRealPath("/upload");
        String fileName = UploadUtil.upload(file, basePath);
        //2.记录文件的相对路径
        return "/upload/" + fileName;
    }

    @LoginPermission
    @RequestMapping("realAuth_save")
    @ResponseBody
    public JSONResult realAuthSave(RealAuth realAuth) {
        realAuthService.apply(realAuth);
        return new JSONResult();
    }
}
