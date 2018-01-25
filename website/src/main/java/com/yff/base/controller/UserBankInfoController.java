package com.yff.base.controller;

import com.yff.base.aop.LoginPermission;
import com.yff.base.common.JSONResult;
import com.yff.base.common.UserContext;
import com.yff.base.domain.LoginInfo;
import com.yff.base.domain.UserBankInfo;
import com.yff.base.domain.UserInfo;
import com.yff.base.service.IAccountService;
import com.yff.base.service.IUserBankInfoService;
import com.yff.base.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户绑定银行卡
 */
@Controller
public class UserBankInfoController {

    @Autowired
    private IUserBankInfoService userBankInfoService;
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 导向绑定银行卡界面
     */
    @LoginPermission
    @RequestMapping("bankInfo")
    public String bankInfo(Model model) {
        //1.查询用户是否有绑定银行卡
        LoginInfo current = UserContext.getCurrent();
        UserInfo currentUser = this.userInfoService.getUserInfo(current.getId());
        if (currentUser != null && !currentUser.getIsBindBank()) {
            //2.没有绑定银行卡
            model.addAttribute("userInfo", currentUser);
            return "bankInfo";
        } else {
            model.addAttribute("bankInfo", this.userBankInfoService.getByUserId(currentUser.getId()));
            return "bankInfo_result";
        }
    }

    /**
     * 执行银行绑定
     *
     * @param bankInfo
     * @return
     */
    @LoginPermission
    @RequestMapping("bankInfo_save")
    public String bankInfoSave(UserBankInfo bankInfo) {
        try {
            this.userBankInfoService.bindBankInfo(bankInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/bankInfo";
    }
}
