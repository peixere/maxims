package cn.gotom.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@EntityScan({ "com.zhnaste.commons", "com.zhnaste.ems" })
@Configuration
public class GatewayConfig {

	public static final String PATH = "/gw";

	@Bean
	RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(1, 2);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowCredentials(true);
		// cfg.addAllowedOrigin("*");
		cfg.addAllowedOriginPattern("*");
		cfg.addAllowedMethod("*");
		cfg.addAllowedHeader("*");
		cfg.addExposedHeader("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cfg);
		return source;
	}

	@Bean
	public CorsWebFilter CorsWebFilter(@Autowired CorsConfigurationSource corsConfigurationSource) {
		return new CorsWebFilter(corsConfigurationSource);
	}
}
