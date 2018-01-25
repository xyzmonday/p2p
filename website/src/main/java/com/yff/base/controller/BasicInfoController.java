package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.RealAuth;
import com.yff.base.domain.UserInfo;
import com.yff.base.service.ISystemDictionaryService;
import com.yff.base.service.IUserInfoService;
import com.yff.base.utils.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

/**
 * 用户基本信息
 */
@Controller
public class BasicInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    @LoginPermission
    @RequestMapping("basicInfo")
    public String basicInfo(Model model) {
        //1.将用户信息保存到模型中
        model.addAttribute("userInfo", this.userInfoService.getCurrent());
        //2.将字典信息保存到模型中
        model.addAttribute("educationBackgrounds", this.systemDictionaryService.getSystemDicsBySn("educationBackground"));
        model.addAttribute("incomeGrades", this.systemDictionaryService.getSystemDicsBySn("incomeGrade"));
        model.addAttribute("marriages", this.systemDictionaryService.getSystemDicsBySn("marriage"));
        model.addAttribute("kidCounts", this.systemDictionaryService.getSystemDicsBySn("kidCount"));
        model.addAttribute("houseConditions", this.systemDictionaryService.getSystemDicsBySn("houseCondition"));
        return "userInfo";
    }

    @LoginPermission
    @RequestMapping("basicInfo_save")
    @ResponseBody
    public JSONResult basicInfoSave(UserInfo userinfo) {
        JSONResult json = new JSONResult();
        try {
            this.userInfoService.updateBasicInfo(userinfo);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }
}
