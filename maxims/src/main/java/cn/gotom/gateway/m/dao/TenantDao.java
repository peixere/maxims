package cn.gotom.gateway.m.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.gotom.commons.entities.Tenant;
import cn.gotom.data.dao.GenericDaoImpl;
import reactor.core.publisher.Mono;

@Repository
public class TenantDao extends GenericDaoImpl<Tenant, String> {

	public TenantDao() {
		super();
	}

	private static final String FIND8USERID = "SELECT base_tenant.* FROM base_tenant\r\n"//
			+ "INNER JOIN base_user_tenant ON base_user_tenant.tenant_id = base_tenant.id\r\n"//
			+ "WHERE base_user_tenant.user_id = :userId \r\n"//
			+ "AND base_tenant.deleted = :deleted\r\n"//
			+ "AND base_tenant.disabled = :disabled";//

	/**
	 * 用户所在组织
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Tenant>> find8UserId(String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("deleted", false);
		params.put("disabled", false);
		params.put("userId", userId);
		return nativeList(FIND8USERID, params, Tenant.class);
	}

}
