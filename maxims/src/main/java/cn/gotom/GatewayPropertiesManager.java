package cn.gotom;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GatewayPropertiesManager implements ApplicationEventPublisherAware {

	@Autowired
	private GatewayProperties gatewayProperties;

	private ApplicationEventPublisher publisher;

	public GatewayPropertiesManager() {
	}

	public GatewayProperties getGatewayProperties() {
		return gatewayProperties;
	}

	/**
	 * 增加路由
	 * 
	 * @param definition
	 * @return
	 */
	public String add(RouteDefinition definition) {
		delete(definition.getId());
		gatewayProperties.getRoutes().add(definition);
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
		return "success";
	}

	/**
	 * 更新路由
	 * 
	 * @param definition
	 * @return
	 */
	public String update(RouteDefinition definition) {
		try {
			List<RouteDefinition> list = gatewayProperties.getRoutes().stream()
					.filter(e -> e.getId().equals(definition.getId())).collect(Collectors.toList());
			gatewayProperties.getRoutes().removeAll(list);
			gatewayProperties.getRoutes().add(definition);
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "success";
		} catch (Exception e) {
			return "update route  fail";
		}
	}

	public boolean refresh(List<RouteDefinition> definitions) {
		try {
			gatewayProperties.setRoutes(definitions);
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return true;
		} catch (Exception e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	/**
	 * 删除路由
	 * 
	 * @param id
	 * @return
	 */
	public String delete(String id) {
		try {
			List<RouteDefinition> list = gatewayProperties.getRoutes().stream().filter(e -> e.getId().equals(id))
					.collect(Collectors.toList());
			gatewayProperties.getRoutes().removeAll(list);
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "delete success";
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "delete fail";
		}

	}

	public List<RouteDefinition> getRouteDefinitions() {
		return gatewayProperties.getRoutes();

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

}
