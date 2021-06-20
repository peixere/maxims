package cn.gotom.gateway.m.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Tenant;
import cn.gotom.commons.model.Token;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.gateway.m.dao.RightDao;
import reactor.core.publisher.Mono;

@Service
public class RightService extends GenericServiceImpl<Right, String> {

	public RightService(RightDao rightDao) {
		super(rightDao);
	}

	@Override
	protected RightDao access() {
		return (RightDao) super.access();
	}

	/**
	 * 角色拥有的功能权限
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Right>> find8TokenRoles(Token token, List<Role> roles) {
		if (roles.isEmpty()) {
			return Mono.just(Collections.emptyList());
		}
		List<String> roleIds = new ArrayList<>();
		roles.forEach(e -> {
			roleIds.add(e.getId());
		});
		return access().find8Roles(token.getTenantId(), token.getPlatform(), roleIds);
	}

	public Mono<List<Right>> find8ParentId(String parentId) {
		return access().find8ParentId(parentId);
	}

	/**
	 * 租户下的所有功能
	 * 
	 * @param tenantId
	 * @return
	 */
	public Mono<List<Right>> find8Tenant(String tenantId) {
		return access().findById(Tenant.class, tenantId).flatMap(tenant -> {
			if (tenant.getSuperAdmin()) {
				return access().findAll();
			} else {
				return access().find8Tenant(tenantId);
			}
		}).defaultIfEmpty(Collections.emptyList());
	}

}
