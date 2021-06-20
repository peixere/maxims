package cn.gotom.data;

import cn.gotom.commons.entities.LogOperator;
import reactor.core.publisher.Mono;

public interface LogOperatorManager {
	Mono<LogOperator> insert(LogOperator entity);
}
