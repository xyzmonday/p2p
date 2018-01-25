package com.yff.base.listener;

import com.yff.base.service.ILoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/**
 * 初始化超级管理员监听器
 *
 *1.在spring中 实现了ApplicationListener接口的类就可以作为spring的监听器来监听spring中的特殊事件
 *2.在spring中ApplicationEvent这个类相当于所有的事件,如果监听器继承了这个ApplicationListener<ApplicationEvent> ,
 *相当于 这个监听器见日那个的是spring里面的所有事件
 *3.这里只需要监听spring容器启动完成的事件即可 只需要监听ContextRefreshedEvent事件就可以了
 */
@Component
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 当上下文refresh完成后，回到该方法
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loginInfoService.initAdmin();
    }
}
