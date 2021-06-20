package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import cn.gotom.ThreadExecutor;
import cn.gotom.commons.Note;
import cn.gotom.commons.cache.Cache;
import cn.gotom.commons.entities.Conf;
import cn.gotom.data.GenericManagerImpl;
import reactor.core.publisher.Mono;

@Component
public class ConfManager extends GenericManagerImpl<Conf, String> {

	public final static String MULTI_LOGIN = "MULTI_LOGIN";
	public final static String AUTHORITIES_CACHE_TIME = "AUTHORITIES_CACHE_TIME";

	@Note("系统参数[conf]缓存：Cache<code, value>")
	private final Cache<String, String> confCache = new Cache<>(60000L);

	public ConfManager() {
		super();
		confCache.setLoading((token, defaultValue) -> load(token, defaultValue));
	}

	private Mono<String> load(String code, String defaultValue) {
		Conf form = new Conf();
		form.setCode(code);
		return find(form).flatMap(confList -> {
			if (confList.size() > 0) {
				return Mono.just(confList.get(0).getValue());
			} else {
				Conf conf = new Conf();
				conf.setCode(code);
				conf.setName(code);
				conf.setValue(defaultValue);
				ThreadExecutor.execute(() -> insert(conf).subscribe());
				return Mono.just(conf.getValue());
			}
		});
	}

	public Mono<String> getCache(String code) {
		return confCache.get(code);
	}

	public Mono<String> getCache(String code, String defaultValue) {
		return confCache.get(code, defaultValue);
	}

	@Note("同时只允许一个登录")
	public Mono<Boolean> singleLogin() {
		return getCache(ConfManager.MULTI_LOGIN, "Yes")//
				.flatMap(bool -> Mono.just(!BooleanUtils.toBoolean(bool)));
	}

	@Note("权限数据缓存时长(豪秒)")
	public Mono<Long> getGuthoritiesCacheTime() {
		return getCache(ConfManager.AUTHORITIES_CACHE_TIME, "600000")//
				.flatMap(number -> Mono.just(NumberUtils.toLong(number)));
	}
	
	@Note("登录可访问的权限，不需要鉴权")
	public Mono<List<String>> findAuthorizedAuthorities() {
		List<String> authorities = new ArrayList<>();
		return Mono.just(authorities);
	}
}
