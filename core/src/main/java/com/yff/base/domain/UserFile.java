package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserFile extends BaseAuditDomain {

    //风控图片
    private String image;
    //评审后得分
    private Byte score = 0;
    //风控材料
    private String file;
    //风控材料类型
    private SystemDictionaryItem fileType;

    public String getJsonString(){
        Map<String,Object> json = new HashMap<>();
        json.put("id", id);
        json.put("applier", this.applier.getUsername());
        json.put("fileType", this.fileType.getTitle());
        json.put("image", image);
        json.put("file", file);
        return JSONObject.toJSONString(json);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Byte getScore() {
        return score;
    }

    public void setScore(Byte score) {
        this.score = score;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public SystemDictionaryItem getFileType() {
        return fileType;
    }

    public void setFileType(SystemDictionaryItem fileType) {
        this.fileType = fileType;
    }
}