package cn.gotom.gateway.controller.fliter;

import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import cn.gotom.commons.webflux.SimpleExchangeContext;
import reactor.core.publisher.Mono;

@Component
public class SimpleExchangeContextFilter implements OrderedWebFilter {

	public SimpleExchangeContextFilter() {

	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange).contextWrite(SimpleExchangeContext.of(exchange));
	}

}
