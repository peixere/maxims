package cn.gotom;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;

@Api(tags = "100.动态路由")
@RestController
@RequestMapping(name = "动态路由")
@RefreshScope
public class GatewayRouteController {

	@Autowired
	private GatewayPropertiesManager gatewayPropertiesManager;

	@GetMapping(name = "首页")
	public Mono<List<RouteDefinition>> index() {
		return Mono.just(gatewayPropertiesManager.getRouteDefinitions());
	}

	/**
	 * 增加路由
	 * 
	 * @param gwdefinition
	 * @return
	 */
	@PostMapping("/route/add")
	public Mono<String> add(@RequestBody RouteDefinition definition) {
		return Mono.just(gatewayPropertiesManager.add(definition));
	}

	@GetMapping("/route/delete/{id}")
	public Mono<String> delete(@PathVariable String id) {
		return Mono.just(gatewayPropertiesManager.delete(id));
	}

	@PostMapping("/route/update")
	public Mono<String> update(@RequestBody RouteDefinition definition) {
		return Mono.just(gatewayPropertiesManager.update(definition));
	}

	@GetMapping("/route")
	public Mono<List<RouteDefinition>> route() {
		return Mono.just(gatewayPropertiesManager.getRouteDefinitions());
	}
}
