package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.RoleRight;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.UserRole;
import cn.gotom.commons.model.TenantEntity;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.data.dao.SQLUtils;
import cn.gotom.gateway.m.service.RightService;
import cn.gotom.gateway.m.service.RoleService;
import cn.gotom.gateway.m.service.StructureService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RoleManager extends GenericManagerImpl<Role, String> {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RightService rightService;

	@Autowired
	private StructureService structureService;

	public RoleManager() {
		super();
	}

	@Override
	protected RoleService access() {
		return roleService;
	}

	/**
	 * 查角色拥有的功能
	 * 
	 * @param resp
	 * @return
	 */
	public Mono<Response<Role>> findRight(Response<Role> resp) {
		if (!resp.ok()) {
			return Mono.just(resp);
		}
		return findRoleRight(resp.getData()).flatMap(rrList -> {
			resp.getData().setRightIdList(new ArrayList<>());
			rrList.forEach(rr -> {
				resp.getData().getRightIdList().add(rr.getRightId());
			});
			return Mono.just(resp);
		});
	}

	/**
	 * 查角色拥有的功能
	 * 
	 * @param resp
	 * @return
	 */
	private Mono<List<RoleRight>> findRoleRight(Role role) {
		RoleRight params = new RoleRight();
		params.setRoleId(role.getId());
		return access().find(params);
	}

	/**
	 * 查当前用户可访问的功能
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Right>> findRight(Token token) {
		return roleService.find8TenantIdAndUserId(token.getTenantId(), token.getUserId())//
				.flatMap(roleList -> {
					if (token.getSuperAdmin()) {// 超级管理员
						return rightService.find8Tenant(token.getTenantId());
					}
					return roleService.find8TenantIdRoles(token.getTenantId(), roleList);
				});
	}

	public Mono<Response<Role>> insertOrUpdate(Token token, Role role, List<Structure> structureList) {
		role.setAuthorities(null);
		if (StringUtils.isBlank(role.getStructureId())) {
			role.setStructureId(Role.WILDCARD);
		}
		if (role.getStructureId().equals(Role.WILDCARD)) {
			if (structureList.isEmpty()) {
				// 选通用，用户未关联角色
				return insertOrUpdate(token, role);
			} else {
				// 选通用，用户已关联角色
				List<Structure> tmplist = structureList.stream()//
						.filter(structure -> (StringUtils.isBlank(structure.getParentId())))//
						.collect(Collectors.toList());
				if (tmplist.size() > 0) {
					// 顶级机构用户
					return insertOrUpdate(token, role);
				} else {
					return ResponseMono.bq("选择的组织不存在");
				}
			}
		} else {
			List<Structure> tmplist = structureList.stream()//
					.filter(structure -> role.getStructureId().equals(structure.getId()))//
					.collect(Collectors.toList());
			if (tmplist.size() == 0) {
				return ResponseMono.bq("选择的组织不存在");
			} else {
				return insertOrUpdate(token, role);
			}
		}
	}

	private Mono<Response<Role>> insertOrUpdate(Token token, Role role) {
		return findRight(token).flatMap(rightList -> insertOrUpdate(role, rightList));
	}

	private Mono<Response<Role>> insertOrUpdate(Role role, List<Right> canSelectRightList) {
		final List<String> canSelectRightIds;
		if (canSelectRightList == null) {// 超级管理员
			canSelectRightIds = role.getRightIdList();
		} else {
			canSelectRightIds = new ArrayList<>();
			canSelectRightList.forEach(right -> canSelectRightIds.add(right.getId()));
		}
		List<Object> entitysToSave = new ArrayList<>();
		List<String> rightIdList = role.getRightIdList();
		entitysToSave.add(role);
		if (rightIdList != null) {
			for (String rightId : rightIdList) {
				if (canSelectRightIds.contains(rightId)) {
					RoleRight rr = new RoleRight();
					rr.setId(Role.nextId());
					rr.setRightId(rightId);
					rr.setRoleId(role.getId());
					entitysToSave.add(rr);
				} else {
					log.warn("can't select rightId={}", rightId);
				}
			}
		}
		return findRoleRight(role).flatMap(rrList -> {
			List<RoleRight> selected = rrList.stream().filter(rr -> rightIdList.contains(rr.getRightId()))
					.collect(Collectors.toList());
			rrList.removeAll(selected);
			entitysToSave.removeAll(selected);
			return access().removeThenSave(rrList, entitysToSave).flatMap(o -> {
				return ResponseMono.ok(role);
			});
		});
	}

	public Mono<Response<Integer>> delete(Role role) {
		RoleRight rr = new RoleRight();
		rr.setRoleId(role.getId());
		UserRole ur = new UserRole();
		ur.setRoleId(role.getId());
		Mono<List<UserRole>> urlist = access().find(ur);
		Mono<List<RoleRight>> rrlist = access().find(rr);
		return Mono.zip(urlist, rrlist).flatMap(tuple -> {
			List<Object> entitysToDelete = new ArrayList<>();
			entitysToDelete.add(role);
			tuple.getT1().forEach(value -> entitysToDelete.add(value));
			tuple.getT2().forEach(value -> entitysToDelete.add(value));
			return removeList(entitysToDelete).flatMap(n -> {
				if (n > 0) {
					return ResponseMono.ok(n, "删除成功");
				}
				return ResponseMono.ok(n, "删除失败");
			});
		});
	}

	/**
	 * 可见角色ID
	 * 
	 * @param token
	 * @return
	 */
	private Mono<List<String>> findStructureId8Token(Token token) {
		return structureService.findStructureId8Token(token);
	}

	public Mono<Response<List<Role>>> find(Role role, Token token) {
		Map<String, Object> params = SQLUtils.toSqlParams(role);
		params.put(TenantEntity.TENANTID_COLUMN, token.getTenantId());
		if (StringUtils.isBlank(role.getStructureId())) {
			return findStructureId8Token(token).flatMap(structureIdList -> {
				params.put("structure_id", structureIdList);
				return find8Map(params).flatMap(data -> ResponseMono.ok(data));
			});
		} else {
			return find8Map(params).flatMap(data -> ResponseMono.ok(data));
		}
	}

	/**
	 * 用户拥有的角色
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Role>> find8Token(Token token) {
		return findStructureId8Token(token).flatMap(structureIdList -> {
			Map<String, Object> params = new HashMap<>();
			params.put(TenantEntity.TENANTID_COLUMN, token.getTenantId());
			params.put("structure_id", structureIdList);
			return find8Map(params);
		});
	}

	public Mono<Response<List<Role>>> page(Role role, Token token) {
		role.page().setParams(SQLUtils.toSqlParams(role));
		role.page().put(TenantEntity.TENANTID_COLUMN, token.getTenantId());
		if (StringUtils.isBlank(role.getStructureId())) {
			return findStructureId8Token(token).flatMap(structureIdList -> {
				role.page().put("structure_id", structureIdList);
				return roleService.page8Token(role, token);
			});
		} else {
			return roleService.page8Token(role, token);
		}
	}
}
