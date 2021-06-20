package cn.gotom.gateway.m;

import org.springframework.stereotype.Component;

import cn.gotom.commons.entities.LogOperator;
import cn.gotom.data.GenericManagerImpl;
import reactor.core.publisher.Mono;

@Component
public class LogOperatorManager extends GenericManagerImpl<LogOperator, String>
		implements cn.gotom.data.LogOperatorManager {

	public LogOperatorManager() {
		super();
	}

	@Override
	public Mono<LogOperator> insert(LogOperator entity) {
		return super.insert(entity);
	}

}
