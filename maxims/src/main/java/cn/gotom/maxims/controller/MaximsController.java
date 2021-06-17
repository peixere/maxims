package cn.gotom.maxims.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.maxims.redis.StringTemplate;
import reactor.core.publisher.Mono;

@RestController
public class MaximsController {

	@Autowired
	private StringTemplate stringTemplate;

	private final static String MAXIMS = "maxims";

	@GetMapping("/")
	Mono<String> home() {
		String maxims = stringTemplate.get(MAXIMS);
		if (StringUtils.isBlank(maxims)) {
			stringTemplate.set(MAXIMS, "Maxims");
		}
		return Mono.just("Hello " + maxims);
	}

	@PostMapping("/")
	Mono<String> index() {
		return Mono.just("Hello Maxims!");
	}
}
