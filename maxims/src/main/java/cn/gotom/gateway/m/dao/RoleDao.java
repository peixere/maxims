package cn.gotom.gateway.m.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.gotom.commons.Response;
import cn.gotom.commons.data.EntityMetaData;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.UserRole;
import cn.gotom.commons.model.Page;
import cn.gotom.commons.model.Token;
import cn.gotom.data.dao.GenericDaoImpl;
import cn.gotom.data.dao.SQLUtils;
import reactor.core.publisher.Mono;

@Repository
public class RoleDao extends GenericDaoImpl<Role, String> {

	public RoleDao() {
		super();
	}

	private static final String FIND8TENANTIDANDUSERID = ""//
			+ "SELECT base_role.* FROM base_role \r\n"//
			+ " INNER JOIN base_user_role ON base_user_role.role_id = base_role.id \r\n"//
			+ " 	AND base_role.tenant_id = :tenantId \r\n"//
			+ " WHERE base_user_role.user_id = :userId";

	/**
	 * 角色拥有的权限
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Role>> find8TenantIdAndUserId(String tenantId, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId == null ? "" : tenantId);
		params.put("userId", userId);
		return nativeList(FIND8TENANTIDANDUSERID, params, Role.class);
	}

	private static final String FIND8TENANT_USER = "SELECT base_role.structure_id,\r\n"//
			+ "	base_user_role.*\r\n"//
			+ "	FROM base_user_role\r\n"//
			+ "	INNER JOIN base_role ON base_role.id = base_user_role.role_id\r\n"//
			+ "	WHERE base_user_role.user_id = :userId\r\n"//
			+ "	AND base_user_role.tenant_id = :tenantId";

	/**
	 * 用户拥有的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public Mono<List<UserRole>> find8TenantAndUser(String tenantId, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("userId", userId);
		return nativeList(FIND8TENANT_USER, params, UserRole.class);
	}

	private static final String FIND8_TENANTID_ROLES = ""//
			+ "SELECT base_right.* FROM base_right \r\n"//
			+ " INNER JOIN base_role_right ON base_role_right.right_id = base_right.id \r\n"//
			+ " INNER JOIN base_right_tenant ON base_right_tenant.right_id = base_right.id \r\n"//
			+ " WHERE base_right_tenant.tenant_id = :tenantId \r\n"//
			+ "	AND base_role_right.role_id in (:roleIds)";

	/**
	 * 角色拥有的功能权限
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Right>> find8TenantIdRoles(String tenantId, List<Role> roles) {
		if (roles.isEmpty()) {
			return Mono.just(Collections.emptyList());
		}
		List<String> roleIds = new ArrayList<>();
		roles.forEach(e -> {
			roleIds.add(e.getId());
		});
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("roleIds", roleIds);
		return nativeList(FIND8_TENANTID_ROLES, params, Right.class);
	}

	private static final String FIND = "SELECT base_structure.`name` as structure, base_role.*\r\n"//
			+ " FROM base_role \r\n"//
			+ " LEFT JOIN base_structure ON base_structure.id = base_role.structure_id"//
			+ "	WHERE 1 = 1\r\n";

	/**
	 * 用户拥有的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	public Mono<Response<List<Role>>> page8Token(Role form, Token token) {
		form.setTenantId(token.getTenantId());
		StringBuilder sql = new StringBuilder(FIND);
		Map<String, Object> params = SQLUtils.toSqlParams(form);
		EntityMetaData meta = EntityMetaData.get(form.getClass());
		sql.append(SQLUtils.where("base_role", params));
		sql.append(" ORDER BY sort ASC," + meta.getCreated().getColumn() + " DESC");
		Page page = form.page();
		page.setParams(params);
		return nativePage(sql.toString(), page, Role.class);
	}
}
