package cn.gotom.data.redis;

import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import cn.gotom.commons.config.ConfigManager;

@Configuration
@ConditionalOnClass({ RedisClient.class, RedisOperations.class })
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties({ RedissonProperties.class, RedisProperties.class })
public class RedissonAutoConfiguration {

	@Autowired
	private RedissonProperties redissonProperties;

	@Autowired
	private RedisProperties redisProperties;

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean({ RedisClient.class, RedissonClient.class })
	public RedisClient redisson(ConfigManager configManager) {
		return new RedisClient(redissonProperties, redisProperties, configManager);
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisClient redisson) {
		return redisson.redisTemplate();
	}

	@Bean
	@ConditionalOnMissingBean({ StringRedisTemplate.class, StringTemplate.class })
	public StringTemplate stringRedisTemplate(RedisClient redisson) {
		return redisson.stringRedisTemplate();
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveStringRedisTemplate.class)
	public ReactiveStringRedisTemplate reactiveStringRedisTemplate(RedisClient redisson) {
		return redisson.reactiveStringRedisTemplate();
	}

//	@Bean
//	@ConditionalOnMissingBean(RedisConnectionFactory.class)
//	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
//		return new RedissonConnectionFactory(redisson);
//	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactive(RedisClient redisson) {
		return redisson.reactive();
	}

}
