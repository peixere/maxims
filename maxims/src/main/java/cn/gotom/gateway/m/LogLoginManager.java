package cn.gotom.gateway.m;

import org.springframework.stereotype.Component;

import cn.gotom.commons.entities.LogLogin;
import cn.gotom.data.GenericManagerImpl;

@Component
public class LogLoginManager extends GenericManagerImpl<LogLogin, String> {

	public LogLoginManager() {
		super();
	}
}
