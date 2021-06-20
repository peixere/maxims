package cn.gotom.gateway.m;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import cn.gotom.commons.config.ConfigManager;
import cn.gotom.commons.json.JSON;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RouteServiceManager implements ApplicationEventPublisherAware {

	private String dataId = "route";

	@Autowired
	private RouteDefinitionRepository routeDefinitionRepository;

	@Autowired
	private ConfigManager configManage;

	private ApplicationEventPublisher publisher;

	public RouteServiceManager() {
	}

	@PostConstruct
	public String init() {
		configManage.register(dataId, config -> refresh(config));
		return "success";
	}

	public String getName() {
		return dataId;
	}

	/**
	 * 增加路由
	 * 
	 * @param definition
	 * @return
	 */
	public String add(RouteDefinition definition) {
		routeDefinitionRepository.save(Mono.just(definition)).subscribe();
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
			this.routeDefinitionRepository.delete(Mono.just(definition.getId()));
		} catch (Exception e) {
			return "update fail,not find route  routeId: " + definition.getId();
		}
		try {
			routeDefinitionRepository.save(Mono.just(definition)).subscribe();
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "success";
		} catch (Exception e) {
			return "update route  fail";
		}
	}

	public boolean refresh(String config) {
		try {
			List<RouteDefinition> definitions = JSON.parseList(config, RouteDefinition.class);
			if (definitions == null) {
				return false;
			}
			routeDefinitionRepository.getRouteDefinitions().subscribe(route -> {
				routeDefinitionRepository.delete(Mono.just(route.getId()));
			});
			for (RouteDefinition route : definitions) {
				routeDefinitionRepository.save(Mono.just(route)).subscribe();
			}
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
			this.routeDefinitionRepository.delete(Mono.just(id));
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			return "delete success";
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "delete fail";
		}

	}

	public Flux<RouteDefinition> getRouteDefinitions() {
		return routeDefinitionRepository.getRouteDefinitions();

	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

}
