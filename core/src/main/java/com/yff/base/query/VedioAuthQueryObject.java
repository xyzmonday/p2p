package com.yff.base.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 视频认证的高级查询对象
 *
 * @author Linv999
 */
public class VedioAuthQueryObject extends AuditQueryObject {

    private String keyword;

    public String getKeyword() {
        return StringUtils.hasLength(keyword) ? keyword : null;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
