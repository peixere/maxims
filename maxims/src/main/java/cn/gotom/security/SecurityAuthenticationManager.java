package cn.gotom.security;

import java.util.List;

import org.springframework.web.server.ServerWebExchange;

import cn.gotom.commons.Note;
import cn.gotom.commons.entities.Conf;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.model.Token;
import reactor.core.publisher.Mono;

public interface SecurityAuthenticationManager {

	@Note("查询用户基本信息")
	Mono<User> findByAccount(String account);

	Mono<User> login(Token form);

	@Note("登录/退出/注册日志")
	void logLogin(ServerWebExchange exchange, Token token, String memo, Throwable error);

	@Note("取用户的权限数据")
	Mono<User> findAuthorities(Token token);

	Mono<List<Conf>> findConf(Conf form);

	Mono<Conf> insert(Conf conf);

}
