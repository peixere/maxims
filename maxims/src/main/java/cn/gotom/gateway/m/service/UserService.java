package cn.gotom.gateway.m.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserSetTenant;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.gateway.m.dao.UserDao;
import reactor.core.publisher.Mono;

@Service
public class UserService extends GenericServiceImpl<User, String> {

	public UserService(UserDao access) {
		super(access);
	}

	@Override
	protected UserDao access() {
		return (UserDao) super.access();
	}

	public Mono<Response<List<User>>> find(User form, Token token) {
		return access().find8TenantId(token.getTenantId(), form).flatMap(data -> ResponseMono.ok(data));
	}

	public Mono<Response<List<User>>> find8TenantId(Token token) {
		return access().find8TenantId(token.getTenantId()).flatMap(data -> ResponseMono.ok(data));
	}

	public Mono<Response<List<User>>> page(User form, Token token) {
		form.setTenantId(token.getTenantId());
		return access().page8TenantId(token.getTenantId(), form).flatMap(res -> {
			if (res.ok() && res.getData().size() > 0) {// 查询用户明细，当前租户名称、角色、所以组织
				List<User> userList = res.getData();
				List<String> userIdList = new ArrayList<>();
				userList.forEach(u -> userIdList.add(u.getId()));
				return findUserStructure(token, userList, userIdList)//
						.flatMap(list -> findUserRole(token, userList, userIdList))//
						.flatMap(list -> Mono.just(res));
			} else {
				return Mono.just(res);
			}
		});
	}

	private Mono<List<User>> findUserStructure(Token token, List<User> userList, List<String> userIdList) {
		return access().findUserStructure(token.getTenantId(), userIdList).flatMap(map -> {
			for (User user : userList) {
				user.setStructure(map.get(user.getId()));
			}
			return Mono.just(userList);
		});
	}

	private Mono<List<User>> findUserRole(Token token, List<User> userList, List<String> userIdList) {
		return access().findUserRole(token.getTenantId(), userIdList).flatMap(map -> {
			for (User user : userList) {
				user.setRole(map.get(user.getId()));
			}
			return Mono.just(userList);
		});
	}

	public Mono<User> updateTenant(UserSetTenant form) {
		return access().update(form).flatMap(userSetTenant -> {
			return findById(form.getId());
		});
	}
}
