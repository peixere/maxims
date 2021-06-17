package cn.gotom.commons.webflux;

import java.util.function.Function;

import org.springframework.web.server.ServerWebExchange;

import cn.gotom.commons.model.Token;
import reactor.core.publisher.Mono;

public abstract class WebAbsContext {

	protected final <R> Mono<R> context(Function<SimpleExchangeContext, Mono<R>> function) {
		return SimpleExchangeContext.get().flatMap(context -> function.apply(context));
	}

	protected final <R> Mono<R> exchange(Function<ServerWebExchange, Mono<R>> function) {
		return SimpleExchangeContext.get().flatMap(context -> function.apply(context.getExchange()));
	}

	protected final <R> Mono<R> token(Function<Token, Mono<R>> function) {
		return SimpleExchangeContext.get().flatMap(context -> function.apply(context.getToken()));
	}
}
