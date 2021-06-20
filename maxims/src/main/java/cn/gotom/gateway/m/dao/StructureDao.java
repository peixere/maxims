package cn.gotom.gateway.m.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.model.Token;
import cn.gotom.data.dao.GenericDaoImpl;
import reactor.core.publisher.Mono;

@Repository
public class StructureDao extends GenericDaoImpl<Structure, String> {

	public StructureDao() {
		super();
	}

	private static final String FIND8TENANTIDANDUSERID = ""//
			+ "SELECT base_structure.* FROM base_structure\r\n" //
			+ "INNER JOIN base_structure_user\r\n"//
			+ " ON base_structure_user.structure_id = base_structure.id\r\n"//
			+ "WHERE base_structure_user.user_id = :userId\r\n"//
			+ "AND base_structure.tenant_id = :tenantId";

	/**
	 * 用户所在组织
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Structure>> find8TenantIdAndUserId(String tenantId, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId == null ? "" : tenantId);
		params.put("userId", userId);
		return nativeList(FIND8TENANTIDANDUSERID, params, Structure.class);
	}

	private static final String FIND8_PARENT_ID = ""//
			+ "SELECT base_structure.* FROM base_structure \r\n"//
			+ " WHERE base_structure.parent_id = :parentId \r\n"//
			+ " AND base_structure.tenant_id = :tenantId \r\n"//
			+ " AND base_structure.deleted = :deleted \r\n";

	private static final String FIND8_PARENT_ID_IS_NULL = ""//
			+ "SELECT base_structure.* FROM base_structure \r\n"//
			+ " WHERE base_structure.tenant_id = :tenantId \r\n"
			+ " AND (base_structure.parent_id IS NULL or base_structure.parent_id = '')\r\n"
			+ " AND base_structure.deleted = :deleted \r\n";

	public Mono<List<Structure>> find8ParentId(String tenantId, String parentId) {
		final String sql;
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("deleted", false);
		if (parentId != null) {
			params.put("parentId", parentId);
			sql = FIND8_PARENT_ID;
		} else {
			sql = FIND8_PARENT_ID_IS_NULL;
		}
		return nativeList(sql, params, Structure.class);
	}

	public Mono<List<Structure>> findTop(String tenantId) {
		return this.find8ParentId(tenantId, null);
	}

	/**
	 * 租户下的所有架构
	 * 
	 * @param tenantId
	 * @return
	 */
	public Mono<List<Structure>> find8Tenant(String tenantId) {
		Structure form = new Structure();
		form.setTenantId(tenantId);
		return find(form);
	}

	private static final String FIND_USER = ""//
			+ "SELECT base_user.* FROM base_user "//
			+ " INNER JOIN base_structure_user ON base_structure_user.user_id = base_user.id"//
			+ " WHERE base_structure_user.structure_id = :structureId"//
			+ " AND base_structure_user.tenant_id = :tenantId";

	public Mono<List<User>> findUser(Token token, String structureId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", token.getTenantId() == null ? "" : token.getTenantId());
		params.put("structureId", structureId);
		return nativeList(FIND_USER, params, User.class);
	}
}
