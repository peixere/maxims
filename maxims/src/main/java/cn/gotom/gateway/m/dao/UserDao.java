package cn.gotom.gateway.m.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.model.Page;
import cn.gotom.data.dao.GenericDaoImpl;
import cn.gotom.data.dao.SQLUtils;
import reactor.core.publisher.Mono;

@Service
public class UserDao extends GenericDaoImpl<User, String> {

	public UserDao() {
		super();
	}

	private static final String FIND = "SELECT base_user.*,base_user_tenant.joined\r\n"//
			+ " FROM base_user\r\n"//
			+ " INNER JOIN base_user_tenant ON base_user_tenant.user_id = base_user.id\r\n"//
			+ " INNER JOIN base_structure_user ON base_structure_user.user_id = base_user.id\r\n"//
			+ " WHERE base_user_tenant.tenant_id = :tenantId\r\n"//
			+ " AND base_user.deleted = false\r\n"//
			+ " AND base_structure_user.structure_id in(:structureId)";

	public Mono<List<User>> find8TenantId(String tenantId, User form) {
		form.setTenantId(null);
		Object structureId = form.page().getParams().remove("structureId");
		StringBuilder sql = new StringBuilder(FIND);
		Map<String, Object> params = SQLUtils.toSqlParams(form);
		sql.append(SQLUtils.where("base_user", params));
		params.put("tenantId", tenantId);
		params.put("structureId", structureId);
		return nativeList(sql.toString(), params, User.class);
	}

	public Mono<Response<List<User>>> page8TenantId(String tenantId, User form) {
		form.setTenantId(null);
		Object structureId = form.page().getParams().remove("structureId");
		StringBuilder sql = new StringBuilder(FIND);
		Map<String, Object> params = SQLUtils.toSqlParams(form);
		sql.append(SQLUtils.where("base_user", params));
		params.put("tenantId", tenantId);
		params.put("structureId", structureId);
		Page page = form.page();
		page.setParams(params);
		return nativePage(sql.toString(), page, User.class);
	}

	private static final String FIND8TENANTID = "SELECT base_user.*,base_user_tenant.joined\r\n"//
			+ " FROM base_user\r\n"//
			+ " INNER JOIN base_user_tenant ON base_user_tenant.user_id = base_user.id\r\n"//
			+ " WHERE base_user_tenant.tenant_id = :tenantId\r\n"//
			+ " AND base_user.deleted = false\r\n";

	public Mono<List<User>> find8TenantId(String tenantId) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		return nativeList(FIND8TENANTID, params, User.class);
	}

	private static final String FIND_USER_STRUCTURE = ""//
			+ "SELECT base_structure_user.user_id as id, base_structure.`name`\r\n" //
			+ "FROM base_structure\r\n"//
			+ "INNER JOIN base_structure_user ON base_structure_user.structure_id = base_structure.id\r\n"//
			+ "WHERE base_structure.tenant_id = :tenantId\r\n"//
			+ "AND base_structure_user.user_id in(:userId)\r\n";

	/**
	 * 查询用户所在组织
	 * 
	 * @param userIdList
	 * @return
	 */
	public Mono<Map<String, String>> findUserStructure(String tenantId, List<String> userIdList) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("userId", userIdList);
		return nativeList(FIND_USER_STRUCTURE, params, Structure.class).flatMap(list -> {
			Map<String, String> map = new HashMap<>();
			for (Structure structure : list) {
				if (map.containsKey(structure.getId())) {
					map.put(structure.getId(), map.get(structure.getId()) + "," + structure.getName());
				} else {
					map.put(structure.getId(), structure.getName());
				}
			}
			return Mono.just(map);
		});
	}

	private static final String FIND_USER_ROLE = ""//
			+ "SELECT base_user_role.user_id as id, base_role.`name`\r\n" //
			+ "FROM base_role\r\n"//
			+ "INNER JOIN base_user_role ON base_user_role.role_id = base_role.id\r\n"//
			+ "WHERE base_role.tenant_id = :tenantId\r\n"//
			+ "AND base_user_role.user_id in(:userId)\r\n";

	/**
	 * 查询用户角色
	 * 
	 * @param userIdList
	 * @return
	 */
	public Mono<Map<String, String>> findUserRole(String tenantId, List<String> userIdList) {
		Map<String, Object> params = new HashMap<>();
		params.put("tenantId", tenantId);
		params.put("userId", userIdList);
		return nativeList(FIND_USER_ROLE, params, Role.class).flatMap(list -> {
			Map<String, String> map = new HashMap<>();
			for (Role role : list) {
				if (map.containsKey(role.getId())) {
					map.put(role.getId(), map.get(role.getId()) + "," + role.getName());
				} else {
					map.put(role.getId(), role.getName());
				}
			}
			return Mono.just(map);
		});
	}
}
