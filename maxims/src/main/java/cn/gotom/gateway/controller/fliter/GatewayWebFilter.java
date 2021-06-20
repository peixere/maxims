package cn.gotom.gateway.controller.fliter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;

import cn.gotom.commons.json.JSON;
import cn.gotom.commons.utils.TextUtils;
import cn.gotom.commons.webflux.SimpleExchangeFilter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GatewayWebFilter extends SimpleExchangeFilter {

	private final String API_DOCS = "/*/v3/api-docs";
	private final String GW_API_DOCS = "/v3/api-docs";
	private PathMatcher pathMatcher = new AntPathMatcher();

	public GatewayWebFilter() {
		super();
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	/**
	 * 处理swagger集成到网关，服务名丢失问题
	 */
	@Override
	protected Mono<DataBuffer> writeWith(ServerWebExchange exchange, byte[] responseBody) {
		//解决IFrame拒绝的问题
		exchange.getResponse().getHeaders().set("X-Frame-Options", "SAMEORIGIN");
		String path = exchange.getRequest().getPath().value();
		if (pathMatcher.match(GW_API_DOCS, path)) {
			Map<String, Object> map = JSON.parseMap(responseBody);
			responseBody = TextUtils.toBytes(JSON.format(map));
			exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			exchange.getResponse().getHeaders().setContentLength(responseBody.length);

		} else if (pathMatcher.match(API_DOCS, path)) {
			try {
				String serviceId = path.replace(GW_API_DOCS, "");
				Map<String, Object> map = JSON.parseMap(responseBody);
				Map<String, Object> newpaths = new LinkedHashMap<>();
				@SuppressWarnings("unchecked")
				Map<String, Object> paths = (Map<String, Object>) map.get("paths");
				for (String key : paths.keySet()) {
					newpaths.put(serviceId + key, paths.get(key));
				}
				map.put("paths", newpaths);
				responseBody = TextUtils.toBytes(JSON.format(map));
				exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
				exchange.getResponse().getHeaders().setContentLength(responseBody.length);
			} catch (Throwable ex) {
				log.info("{} {} {}", path, exchange.getResponse().getHeaders().getContentType(), ex.toString());
			}
		}
		return super.writeWith(exchange, responseBody);
	}
}
