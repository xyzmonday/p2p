package com.yff.base.listener;

import com.yff.base.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 第一次创建系统平台的账户
 *
 */
@Component
public class InitSystemAccountListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ISystemAccountService systemAccountService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.systemAccountService.initSystemAccount();
	}

	
}