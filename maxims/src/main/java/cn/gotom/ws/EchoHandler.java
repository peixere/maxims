
package cn.gotom.ws;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;

import cn.gotom.commons.webflux.WebAbsContext;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class EchoHandler extends WebAbsContext implements WebSocketHandler {

	Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

	/**
	 * 任务执行器
	 */
	@Autowired
	ScheduledExecutorService executor;

	@Override
	public Mono<Void> handle(final WebSocketSession session) {
		log.info(session.toString());
		return token(token -> {
			sessionMap.put(token.getId(), session);
			session.send(Mono.just(session.textMessage("你好"))).subscribe();
			return session.receive().doOnTerminate(() -> {
				log.info("doOnTerminate " + session.toString());
				sessionMap.remove(token.getId());
			}).doOnNext(message -> {
				switch (message.getType()) {
				case BINARY:
				case TEXT:
				default:
					log.info(message.getPayloadAsText());
					String msg = "收到： " + token.getAccount() + message.getPayloadAsText();
					session.send(Mono.just(session.textMessage(msg))).subscribe();
					break;
				case PING:
					session.send(Mono.just(session.pongMessage(s -> s.wrap(new byte[256]))));
					break;
				case PONG:
					executor.schedule(
							() -> session.send(Flux.just(session.pingMessage(DataBufferFactory::allocateBuffer)))//
									.subscribe(),
							3, TimeUnit.SECONDS);
					break;
				}
			}).then();
		});

	}

	public void send(String msg) {
		for (WebSocketSession session : sessionMap.values()) {
			session.send(Mono.just(session.textMessage(msg))).subscribe();
		}
	}

}
