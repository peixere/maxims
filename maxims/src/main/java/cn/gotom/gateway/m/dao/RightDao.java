package cn.gotom.gateway.m.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.gotom.commons.entities.Right;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.data.dao.GenericDaoImpl;
import reactor.core.publisher.Mono;

@Repository
public class RightDao extends GenericDaoImpl<Right, String> {
	
	public RightDao() {
		super();
	}

	private static final String FIND8ROLES = ""//
			+ "SELECT base_right.* FROM base_right \r\n"//
			+ " INNER JOIN base_role_right ON base_role_right.right_id = base_right.id \r\n"//
			+ " INNER JOIN base_right_tenant ON base_right_tenant.right_id = base_right.id \r\n"//
			+ " WHERE base_right_tenant.tenant_id = :tenantId \r\n"//
			+ "	AND base_right.type = :platform \r\n"//
			+ "	AND base_role_right.role_id in (:roleIds)";

	/**
	 * 角色拥有的功能权限
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Right>> find8Roles(String tenantId, PlatformEnum platform, List<String> roleIds) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId == null ? "" : tenantId);
		params.put("platform", platform == null ? "" : platform.name());
		params.put("roleIds", roleIds);
		return nativeList(FIND8ROLES, params, Right.class);
	}

	private static final String FIND8_PARENT_ID = ""//
			+ "SELECT base_right.* FROM base_right "//
			+ "WHERE base_right.parent_id = :parentId";

	private static final String FIND8_PARENT_ID_IS_NULL = ""//
			+ "SELECT base_right.* FROM base_right "//
			+ "WHERE base_right.parent_id IS NULL or base_right.parent_id = ''";

	public Mono<List<Right>> find8ParentId(String parentId) {
		final String sql;
		Map<String, Object> params = new HashMap<>();
		if (parentId != null) {
			params.put("parentId", parentId);
			sql = FIND8_PARENT_ID;
		} else {
			sql = FIND8_PARENT_ID_IS_NULL;
		}
		return nativeList(sql, params, Right.class);
	}

	private static final String FIND8_TEANANT = "SELECT base_right.* FROM base_right\r\n"//
			+ "	INNER JOIN base_right_tenant ON base_right_tenant.right_id = base_right.id \r\n"//
			+ "WHERE base_right_tenant.tenant_id = :tenantId";//

	/**
	 * 租户下的所有功能
	 * 
	 * @param tenantId
	 * @return
	 */
	public Mono<List<Right>> find8Tenant(String tenantId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		return nativeList(FIND8_TEANANT, params, Right.class);
	}

}
