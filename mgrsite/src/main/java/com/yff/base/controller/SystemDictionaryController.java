package com.yff.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yff.base.common.JSONResult;
import com.yff.base.domain.SystemDictionary;
import com.yff.base.domain.SystemDictionaryItem;
import com.yff.base.query.SystemDictionaryQueryObject;
import com.yff.base.service.ISystemDictionaryItemService;
import com.yff.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据字典相关的控制器
 */
@Controller
public class SystemDictionaryController {

    @Autowired
    private ISystemDictionaryService systemDictionaryService;
    @Autowired
    private ISystemDictionaryItemService systemDictionaryItemService;

    @RequestMapping("systemDictionary_list")
    public String getSystemDictionaryList(@ModelAttribute("qo") SystemDictionaryQueryObject qo, Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        List<SystemDictionary> list = systemDictionaryService.getSystemDics(qo);
        PageInfo<SystemDictionary> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        return "systemdic/systemDictionary_list";
    }

    @RequestMapping("systemDictionary_update")
    @ResponseBody
    public JSONResult systemDictionaryUpdate(SystemDictionary record) {
        JSONResult result = new JSONResult();
        try {
            systemDictionaryService.saveOrUpdateSysDic(record);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping("systemDictionaryItem_list")
    public String systemDictionaryItemList(@ModelAttribute("qo") SystemDictionaryQueryObject qo,Model model) {
        PageHelper.startPage(qo.getPageNum(), qo.getPageSize());
        List<SystemDictionaryItem> list = systemDictionaryItemService.getSystemDicItems(qo);
        PageInfo<SystemDictionaryItem> pageInfo = new PageInfo<>(list);
        pageInfo.setNavigatePages(5);
        int pages = pageInfo.getPages();
        pages = pages == 0 ? 1 : pages;
        pageInfo.setPages(pages);
        int pageNum = pageInfo.getPageNum();
        pageNum = pageNum == 0 ? 1 : pageNum;
        pageInfo.setPageNum(pageNum);
        model.addAttribute("pageResult", pageInfo);
        //所有的字典明细
        model.addAttribute("systemDictionaryGroups",this.systemDictionaryService.getSystemDicAll());
        return "systemdic/systemDictionaryItem_list";
    }


    @RequestMapping("systemDictionaryItem_update")
    @ResponseBody
    public JSONResult systemDictionaryItemUpdate(SystemDictionaryItem record) {
        JSONResult result = new JSONResult();
        try {
            systemDictionaryItemService.saveOrUpdateSysDicItem(record);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
