package cn.gotom.gateway.controller;

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

import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.GatewayPropertiesManager;
import io.swagger.annotations.Api;

@Api(tags = "100.动态路由")
@RestController
@RequestMapping(name = "动态路由", value = GatewayConfig.PATH)
@RefreshScope
public class RouteController {

	@Autowired
	private GatewayPropertiesManager gatewayPropertiesManager;

	/**
	 * 增加路由
	 * 
	 * @param gwdefinition
	 * @return
	 */
	@PostMapping("/route/add")
	public String add(@RequestBody RouteDefinition definition) {
		return gatewayPropertiesManager.add(definition);
	}

	@GetMapping("/route/delete/{id}")
	public String delete(@PathVariable String id) {
		return gatewayPropertiesManager.delete(id);
	}

	@PostMapping("/route/update")
	public String update(@RequestBody RouteDefinition definition) {
		return gatewayPropertiesManager.update(definition);
	}

	@GetMapping("/route")
	public List<RouteDefinition> list() {
		return gatewayPropertiesManager.getRouteDefinitions();
	}
}
