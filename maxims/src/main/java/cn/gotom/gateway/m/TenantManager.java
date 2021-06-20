package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gotom.CustomException;
import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.RightTenant;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Tenant;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserSetTenant;
import cn.gotom.commons.entities.UserTenant;
import cn.gotom.commons.entities.UserTenantJoin;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.commons.webflux.SimpleExchangeContext;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.gateway.m.service.RightService;
import cn.gotom.gateway.m.service.RoleService;
import cn.gotom.gateway.m.service.TenantService;
import cn.gotom.gateway.m.service.UserService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TenantManager extends GenericManagerImpl<Tenant, String> {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RightService rightService;

	@Autowired
	private UserService userService;

	@Autowired
	private TenantService tenantService;

	public TenantManager() {
		super();
	}

	/**
	 * 可选功能
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Right>> findRight(Token token) {
		return roleService.find8TenantIdAndUserId(token.getTenantId(), token.getUserId())//
				.flatMap(roleList -> {
					if (token.getSuperAdmin()) {// 超级管理员
						return rightService.findAll();
					}
					return rightService.find8Tenant(token.getTenantId());
				});
	}

	private Mono<List<RightTenant>> findRightTenant(Tenant tenant) {
		return access().find(RightTenant.class, "tenant_id", tenant.getId());
	}

	public Mono<Response<Tenant>> findRight(Response<Tenant> resp) {
		if (!resp.ok()) {
			return Mono.just(resp);
		}
		return findRightTenant(resp.getData()).flatMap(rrList -> {
			resp.getData().setRightIdList(new ArrayList<>());
			rrList.forEach(rr -> {
				resp.getData().getRightIdList().add(rr.getRightId());
			});
			return Mono.just(resp);
		});
	}

	private Mono<Response<Tenant>> insertOrUpdate(Tenant tenant, UserTenant userTenant,
			List<Right> canSelectRightList) {
		CustomException.assertIsEmpty(tenant.getRightIdList(), "请选择租户功能");
		final List<String> canSelectRightIds;
		if (canSelectRightList == null) {// 超级管理员
			canSelectRightIds = tenant.getRightIdList();
		} else {
			canSelectRightIds = new ArrayList<>();
			canSelectRightList.forEach(right -> canSelectRightIds.add(right.getId()));
		}
		List<Object> entitysToSave = new ArrayList<>();
		List<String> rightIdList = tenant.getRightIdList();
		if (StringUtils.isBlank(tenant.getId())) {
			tenant.setId(Role.nextId());
		}
		entitysToSave.add(tenant);
		if (userTenant != null) {
			entitysToSave.add(userTenant);
		}
		for (String rightId : rightIdList) {
			if (canSelectRightIds.contains(rightId)) {
				RightTenant rightTenant = new RightTenant();
				rightTenant.setId(Role.nextId());
				rightTenant.setRightId(rightId);
				rightTenant.setTenantId(tenant.getId());
				entitysToSave.add(rightTenant);
			} else {
				log.warn("can't select rightId={}", rightId);
			}
		}
		return findRightTenant(tenant).flatMap(rtList -> {
			List<RightTenant> selected = rtList.stream().filter(rr -> rightIdList.contains(rr.getRightId()))
					.collect(Collectors.toList());
			rtList.removeAll(selected);
			entitysToSave.removeAll(selected);
			return removeThenSave(rtList, entitysToSave).flatMap(o -> {
				return ResponseMono.ok(tenant);
			});
		});
	}

	public Mono<Response<Tenant>> insert(Tenant tenant, Token token) {
		tenant.setId(Role.nextId());
		UserTenant ut = new UserTenant();
		ut.setUserId(token.getId());
		ut.setTenantId(tenant.getId());
		ut.setCreater(token.getAccount());
		ut.setUpdater(token.getAccount());
		ut.setJoined(UserTenantJoin.JOINED);
		return find("name", tenant.getName()).flatMap(list -> {
			if (list.size() > 0) {
				return ResponseMono.bq("租户名称已经存在");
			}
			return findRight(token).flatMap(rightList -> {
				if (tenant.getRightIdList() == null || tenant.getRightIdList().size() == 0) {
					tenant.setRightIdList(new ArrayList<>());
					for (Right right : rightList) {
						tenant.getRightIdList().add(right.getId());
					}
				}
				return insertOrUpdate(tenant, ut, rightList);
			});
		});
	}

	public Mono<Response<Tenant>> update(Tenant tenant, Token token) {
		if (StringUtils.isBlank(tenant.getId())) {
			return ResponseMono.bq("更新数据ID不允许为空");
		}
		return find("name", tenant.getName()).flatMap(list -> {
			if (list.size() > 0 && !list.get(0).getId().equals(tenant.getId())) {
				return ResponseMono.bq("租户名称已经存在");
			}
			return findRight(token).flatMap(rightList -> insertOrUpdate(tenant, null, rightList));
		});
	}

	public Mono<Response<List<Tenant>>> find8UserId(String userId) {
		return tenantService.find8UserId(userId).flatMap(lsit -> ResponseMono.ok(lsit));
	}

	public Mono<Response<User>> tenant(String tenantId, SimpleExchangeContext context) {
		Token token = context.getToken();
		if (token.getTenantId().equals(tenantId)) {
			return ResponseMono.bq("不要切换和当前租户相同");
		}
		return exists(token, tenantId).flatMap(bool -> {
			if (!bool) {
				return ResponseMono.bq("请选择你可切换的租户");
			}
			return updateTenant(tenantId, context);
		});
	}

	private Mono<Response<User>> updateTenant(String tenantId, SimpleExchangeContext context) {
		Token token = context.getToken();
		UserSetTenant form = new UserSetTenant();
		form.setId(token.getUserId());
		form.setTenantId(tenantId);
		return userService.updateTenant(form).flatMap(user -> {
			return ResponseMono.ok(user);
		}).defaultIfEmpty(Response.bq("切换租户失败"));
	}

	private Mono<Boolean> exists(Token token, String tenantId) {
		return tenantService.find8UserId(token.getUserId()).flatMap(tenants -> {
			for (Tenant tenant : tenants) {
				if (tenant.getId().equals(tenantId)) {
					return Mono.just(true);
				}
			}
			return Mono.just(false);
		});
	}
}
