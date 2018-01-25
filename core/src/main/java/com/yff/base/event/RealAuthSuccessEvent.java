package com.yff.base.event;

import com.yff.base.domain.RealAuth;
import org.springframework.context.ApplicationEvent;

/**
 * 实名认证成功消息
 */
public class RealAuthSuccessEvent extends ApplicationEvent {
    //事件关联对象
    public RealAuth realAuth;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source 事件源 the object on which the event initially occurred (never {@code null})
     */
    public RealAuthSuccessEvent(Object source, RealAuth realAut) {
        super(source);
        this.realAuth = realAut;
    }

    public RealAuth getRealAuth() {
        return realAuth;
    }

    public void setRealAuth(RealAuth realAuth) {
        this.realAuth = realAuth;
    }
}
