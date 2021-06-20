package cn.gotom.gateway.m.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.UserRole;
import cn.gotom.commons.model.Token;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.gateway.m.dao.RoleDao;
import reactor.core.publisher.Mono;

@Service
public class RoleService extends GenericServiceImpl<Role, String> {

	public RoleService(RoleDao dao) {
		super(dao);
	}

	@Override
	protected RoleDao access() {
		return (RoleDao) super.access();
	}

	/**
	 * 角色拥有的权限
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Role>> find8TenantIdAndUserId(String tenantId, String userId) {
		return access().find8TenantIdAndUserId(tenantId, userId);
	}

	/**
	 * 用户拥有的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public Mono<List<UserRole>> find8TenantAndUser(String tenantId, String userId) {
		return access().find8TenantAndUser(tenantId, userId);
	}

	/**
	 * 角色拥有的功能权限
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Right>> find8TenantIdRoles(String tenantId, List<Role> roles) {
		return access().find8TenantIdRoles(tenantId, roles);
	}

	public Mono<Response<List<Role>>> page8Token(Role form, Token token) {
		return access().page8Token(form, token);
	}
}
