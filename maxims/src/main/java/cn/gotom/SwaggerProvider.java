package cn.gotom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;

/**
 * 解决网关集成API DOCS路径问题
 * 
 * @author Administrator
 *
 */
@Component
@Primary
public class SwaggerProvider extends InMemorySwaggerResourcesProvider {

	public static final String API_URI = "/v3/api-docs";

	@Autowired
	private GatewayProperties gatewayProperties;

	public SwaggerProvider(@Autowired Environment environment, //
			@Autowired DocumentationCache documentationCache, //
			@Autowired DocumentationPluginsManager pluginsManager) {
		super(environment, documentationCache, pluginsManager);
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		resources.addAll(super.get());
		List<RouteDefinition> routeDefs = gatewayProperties.getRoutes();
		for (RouteDefinition route : routeDefs) {
			for (PredicateDefinition pd : route.getPredicates()) {
				resources.add(swaggerResource(route.getId(),
						pd.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", API_URI)));
			}
		}
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("3.0.0");
		return swaggerResource;
	}

}
