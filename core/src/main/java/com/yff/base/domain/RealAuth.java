package com.yff.base.domain;

import com.alibaba.fastjson.JSONObject;
import com.yff.base.common.BidConst;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RealAuth extends BaseAuditDomain {

    //认证的实名
    private String realName;
    //性别
    private Byte sex;
    //出身日期
    private String birthDate;
    //身份证
    private String idNumber;
    //地址
    private String address;
    //身份证正面图片
    private String image1;
    //身份证背面图片
    private String image2;


    public String getSexDisplay() {
        return sex == BidConst.SEX_MALE ? "男" : "女";
    }

    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("username", this.applier.getUsername());
        json.put("realName", realName);
        json.put("idNumber", idNumber);
        json.put("sex", getSexDisplay());
        json.put("birthDate", birthDate);
        json.put("address", address);
        json.put("image1", image1);
        json.put("image2", image2);
        return JSONObject.toJSONString(json);
    }

    /**
     * 获取用户真实名字的隐藏字符串，只显示姓氏
     *
     * @return
     */
    public String getAnonymousRealName() {
        if (StringUtils.hasLength(this.realName)) {
            int len = realName.length();
            String replace = "";
            replace += realName.charAt(0);
            for (int i = 1; i < len; i++) {
                replace += "*";
            }
            return replace;
        }
        return realName;
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @return
     */
    public String getAnonymousIdNumber() {
        if (StringUtils.hasLength(idNumber)) {
            int len = idNumber.length();
            String replace = "";
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > len - 5)) {
                    replace += "*";
                } else {
                    replace += idNumber.charAt(i);
                }
            }
            return replace;
        }
        return idNumber;
    }

    /**
     * 获取用户住址的隐藏字符串
     * <p>
     * 用户住址
     *
     * @return
     */
    public String getAnonymousAddress() {
        if (StringUtils.hasLength(address) && address.length() > 4) {
            String last = address.substring(address.length() - 4,
                    address.length());
            String stars = "";
            for (int i = 0; i < address.length() - 4; i++) {
                stars += "*";
            }
            return stars + last;
        }
        return address;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate == null ? null : birthDate.trim();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber == null ? null : idNumber.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1 == null ? null : image1.trim();
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2 == null ? null : image2.trim();
    }


}