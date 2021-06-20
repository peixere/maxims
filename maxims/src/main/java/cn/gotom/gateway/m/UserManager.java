package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.gotom.commons.Response;
import cn.gotom.commons.data.EntityMetaData;
import cn.gotom.commons.entities.RightTargetEnum;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.StructureUser;
import cn.gotom.commons.entities.Tenant;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserRole;
import cn.gotom.commons.entities.UserTenant;
import cn.gotom.commons.entities.UserTenantJoin;
import cn.gotom.commons.json.JSON;
import cn.gotom.commons.model.TenantEntity;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.utils.ValidatorUtils;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.commons.webflux.SimpleExchangeContext;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.gateway.m.service.RightService;
import cn.gotom.gateway.m.service.RoleService;
import cn.gotom.gateway.m.service.StructureService;
import cn.gotom.gateway.m.service.UserService;
import cn.gotom.security.UserPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserManager extends GenericManagerImpl<User, String> {

	@Autowired
	private UserPasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RightService rightService;

	@Autowired
	private UserService userService;

	@Autowired
	private StructureService structureService;

	public UserManager() {
		super();
	}

	@Override
	protected void init() {
		rightService.updateIfNull("target", RightTargetEnum.OLD.name()).subscribe();
		userService.updateIfNull("deleted", false).subscribe();
		structureService.updateIfNull("deleted", false).subscribe();
		User admin = User.admin();
		EntityMetaData meta = EntityMetaData.get(User.class);
		admin.page().put(meta.getDeleted().getColumn(), null);
		find(admin).flatMap(userList -> {
			if (userList.size() > 0) {
				BeanUtils.copyProperties(userList.get(0), admin);
				return Mono.just(admin);
			} else {
				return initUser();
			}
		}).contextWrite(SimpleExchangeContext.of(null, admin)).subscribe();
		log.info("init");
	}

	private Mono<User> initUser() {
		log.info("initUser");
		User admin = User.admin();
		return find(admin).flatMap(userList -> {
			if (userList.size() > 0) {
				BeanUtils.copyProperties(userList.get(0), admin);
				return Mono.just(admin);
			} else {
				Tenant tenant = Tenant.admin();
				return access().find(tenant).flatMap(tenantList -> {
					if (tenantList.size() > 0) {
						BeanUtils.copyProperties(tenantList.get(0), admin);
					} else {
						tenant.setId(Tenant.nextId());
					}
					User user = User.admin();
					user.setId(User.nextId());
					user.setPassword(User.DEFAULT_PASSWORD);
					user.setName("超级管理员");
					user.setMemo("系统初始化的超级管理员角色,请不要删除");
					user.setTenantId(tenant.getId());
					user.setEmail("peixere@qq.com");
					user.setCreater(user.getAccount());
					user.setUpdater(user.getAccount());
					user.setMobile("13888888888");
					user.setDeviceInfo("设备信息");
					user.setMemo("备注");
					user.setRfid("rfid卡号");
					user.setIcon("/logo.png");
					passwordEncoder.setDefault(user);
					return super.insert(user).flatMap(u -> initTenant(user));
				});
			}
		});
	}

	private Mono<User> initTenant(User user) {
		Tenant e = Tenant.admin();
		if (StringUtils.isBlank(e.getId())) {
			e.setId(Tenant.nextId());
		}
		e.setName("澳门美心");
		e.setTitle(e.getName());
		e.setCompany(e.getTitle());
		e.setAddress("地址");
		e.setBoard("board");
		e.setLogoId("logoId");
		e.setMain("主页信息");
		e.setPosition("坐 标 点");
		e.setMobile("联系电话");
		e.setUserId(user.getId());
		e.setCreater(user.getAccount());
		e.setUpdater(user.getAccount());
		UserTenant ut = new UserTenant();
		ut.setUserId(user.getId());
		ut.setTenantId(e.getId());
		ut.setCreater(user.getAccount());
		ut.setUpdater(user.getAccount());
		ut.setJoined(UserTenantJoin.JOINED);
		List<Object> list = new ArrayList<>();
		list.add(e);
		list.add(ut);
		return insertList(list).flatMap(t -> {
			log.info(JSON.format(t));
			return Mono.just(user);
		});
	}

	public Mono<Response<User>> findDetail8Id(String tenantId, String userId) {
		return findUserTenants(tenantId, userId).flatMap(userTenants -> {
			if (userTenants.size() > 0) {
				return findDetail(userTenants.get(0));
			} else {
				return ResponseMono.bq("租户下找不到用户信息");
			}
		});
	}

	private Mono<Response<User>> findDetail(UserTenant userTenant) {
		String tenantId = userTenant.getTenantId();
		String userId = userTenant.getUserId();
		return findById(userId)//
				.flatMap(user -> {
					user.setJoined(userTenant.getJoined());
					return roleService.find8TenantIdAndUserId(tenantId, userId).flatMap(roles -> {
						user.setRoleList(roles);
						Set<String> roleSet = new HashSet<>();
						for (Role role : roles) {
							roleSet.add(role.getName());
						}
						user.setRole(roleSet.toString());
						return findStructures(tenantId, userId).flatMap(structures -> {
							user.setStructureList(structures);
							Set<String> structureSet = new HashSet<>();
							for (Structure str : structures) {
								structureSet.add(str.getName());
							}
							user.setStructure(structureSet.toString());
							return ResponseMono.ok(user);
						});
					});
				}).defaultIfEmpty(Response.bq("找不到数据"));
	}

	private Mono<List<Structure>> findStructures(String tenantId, String userId) {
		return structureService.find8TenantIdAndUserId(tenantId, userId);
	}

	public Mono<Boolean> exists(String tenantId, String userId) {
		return findUserTenants(tenantId, userId).flatMap(list -> {
			return Mono.just(list.size() > 0);
		});
	}

	private Mono<List<UserTenant>> findUserTenants(String tenantId, String userId) {
		UserTenant form = new UserTenant();
		form.setUserId(userId);
		form.setTenantId(tenantId);
		return access().find(form);
	}

	public Mono<Response<Integer>> removeUserTenant(String tenantId, List<String> userIds) {
		for (String userId : userIds) {
			UserTenant form = new UserTenant();
			form.setUserId(userId);
			form.setTenantId(tenantId);
		}
		Map<String, Object> whereMap = new HashMap<>();
		whereMap.put("tenant_id", tenantId);
		whereMap.put("user_id", userIds);
		return access().find8Map(UserTenant.class, whereMap)//
				.flatMap(entitysToDelete -> removeList(entitysToDelete).flatMap(size -> ResponseMono.ok(size)));
	}

	public Mono<Response<List<User>>> find(User form, Token token) {
		return structureService.findStructureId8Token(token).flatMap(structureIdList -> {
			form.page().put("structureId", structureIdList);
			return userService.find(form, token);
		});
	}

	public Mono<Response<List<User>>> find8TenantId(Token token) {
		return userService.find8TenantId(token);
	}

	public Mono<Response<List<User>>> page(User form, Token token) {
		return structureService.findStructureId8Token(token).flatMap(structureIdList -> {
			form.page().put("structureId", structureIdList);
			return userService.page(form, token);
		});
	}

	public Mono<Response<User>> save(boolean insert, Token token, User form) {
		if (insert) {
			passwordEncoder.setDefault(form);
		}
		if (StringUtils.isBlank(form.getEmail())) {
			form.setEmail(null);
		}
		if (StringUtils.isBlank(form.getMobile())) {
			form.setMobile(null);
		}
		ValidatorUtils.validatorAssert(form);
		List<String> struIdList = form.getStructureIdList();
		if (CollectionUtils.isEmpty(struIdList) && form.getStructureList() != null) {
			struIdList = new ArrayList<>();
			for (Structure str : form.getStructureList()) {
				if (StringUtils.isNotBlank(str.getId())) {
					struIdList.add(str.getId());
				}
			}
		}
		if (CollectionUtils.isEmpty(struIdList)) {
			return ResponseMono.bq("请选择组织机构");
		}
		List<String> roleIds = form.getRoleIdList();
		if (CollectionUtils.isEmpty(roleIds) && form.getRoleList() != null) {
			roleIds = new ArrayList<>();
			for (Role role : form.getRoleList()) {
				if (StringUtils.isNotBlank(role.getId())) {
					roleIds.add(role.getId());
				}
			}
		}
		if (CollectionUtils.isEmpty(roleIds)) {
			return ResponseMono.bq("请选择用户角色");
		}
		final List<String> structureIdList = new ArrayList<>(struIdList);
		final List<String> roleIdList = new ArrayList<>(roleIds);
		return structureService.find8Token(token).flatMap(structures -> {
			List<Structure> tmplist = structures.stream()//
					.filter(r -> structureIdList.contains(r.getId()))// 过虑
					.collect(Collectors.toList());
			if (tmplist.size() != structureIdList.size()) {
				return ResponseMono.bq("请选择正确组织机构");
			} else {
				return findRoles(token, structureIdList).flatMap(roles -> {
					List<Role> roleList = roles.stream()//
							.filter(r -> roleIdList.contains(r.getId()))// 过虑
							.collect(Collectors.toList());
					if (roleList.size() != roleIdList.size()) {
						return ResponseMono.bq("请选择正确用户角色");
					} else {
						form.setRoleIdList(roleIdList);
						form.setStructureIdList(structureIdList);
						return insertOrUpdate(insert, token, form);
					}
				});
			}
		});
	}

	private Mono<Response<User>> insertOrUpdate(boolean insert, Token token, User form) {
		List<Object> entitysToSave = new ArrayList<>();
		entitysToSave.add(form);
		List<String> structureIdList = form.getStructureIdList();
		for (String structureId : structureIdList) {
			StructureUser structureUser = new StructureUser();
			structureUser.setId(StructureUser.nextId());
			structureUser.setUserId(form.getId());
			structureUser.setStructureId(structureId);
			structureUser.setTenantId(token.getTenantId());
			entitysToSave.add(structureUser);
		}

		List<String> roleIdList = form.getRoleIdList();
		for (String roleId : roleIdList) {
			UserRole userRole = new UserRole();
			userRole.setId(StructureUser.nextId());
			userRole.setUserId(form.getId());
			userRole.setRoleId(roleId);
			userRole.setTenantId(token.getTenantId());
			entitysToSave.add(userRole);
		}

		if (insert) {
			form.setTenantId(token.getTenantId());
			UserTenant ut = new UserTenant();
			ut.setId(UserTenant.nextId());
			ut.setJoined(UserTenantJoin.JOINED);
			ut.setTenantId(token.getTenantId());
			ut.setUserId(form.getId());
			entitysToSave.add(ut);
			return access().insertList(entitysToSave).flatMap(list -> ResponseMono.ok(form));
		} else {
			return findUserRole(token.getTenantId(), form.getId()).flatMap(userRoleList -> {
				entitysToSave.removeAll(userRoleList);// 从新选的数据中删除原来已选的
				List<UserRole> roleSelected = userRoleList.stream()//
						.filter(rr -> roleIdList.contains(rr.getRoleId()))//
						.collect(Collectors.toList());
				userRoleList.removeAll(roleSelected);// 从原来的数据中删除本次选的
				List<Object> deleteList = new ArrayList<>();
				deleteList.addAll(userRoleList);
				return findStructureUser(token.getTenantId(), form.getId()).flatMap(structureUsers -> {
					entitysToSave.removeAll(structureUsers);// 从新选的数据中删除原来已选的
					List<StructureUser> selected = structureUsers.stream()//
							.filter(rr -> structureIdList.contains(rr.getStructureId()))//
							.collect(Collectors.toList());
					structureUsers.removeAll(selected);// 从原来的数据中删除本次选的
					deleteList.addAll(structureUsers);
					return removeThenSave(deleteList, entitysToSave).flatMap(o -> ResponseMono.ok(form));
				});
			});
		}
	}

	/**
	 * 用户拥有的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	private Mono<List<UserRole>> findUserRole(String tenantId, String userId) {
		UserRole params = new UserRole();
		params.setTenantId(tenantId);
		params.setUserId(userId);
		return find(params);
	}

	/**
	 * 用户拥有的角色
	 * 
	 * @param tenantId
	 * @param userId
	 * @return
	 */
	private Mono<List<StructureUser>> findStructureUser(String tenantId, String userId) {
		StructureUser params = new StructureUser();
		params.setTenantId(tenantId);
		params.setUserId(userId);
		return access().find(params);
	}

	private Mono<List<Structure>> findStructure8Token(Token token) {
		if (token.getSuperAdmin()) {
			return structureService.find8Tenant(token.getTenantId());
		} else {
			return structureService.find8TenantIdAndUserId(token.getTenantId(), token.getUserId());
		}
	}

	private Mono<List<String>> findStructureId8Token(Token token) {
		return findStructure8Token(token).flatMap(structureList -> {
			final List<String> structureIdList = new ArrayList<>();
			structureList.forEach(structure -> structureIdList.add(structure.getId()));
			return Mono.just(structureIdList);
		});
	}

	/**
	 * 用户可选的角色
	 * 
	 * @param token
	 * @param structureIdList
	 * @return
	 */
	public Mono<List<Role>> findRoles(Token token, List<String> structureIdList) {
		return find8Token(token).flatMap(roles -> {
			List<Role> roleList = roles.stream()//
					.filter(r -> structureIdList.contains(r.getStructureId())//
							|| Role.WILDCARD.equals(r.getStructureId()))// 过虑
					.collect(Collectors.toList());
			return Mono.just(roleList);
		});
	}

	/**
	 * 用户可选的角色
	 * 
	 * @param token
	 * @return
	 */
	private Mono<List<Role>> find8Token(Token token) {
		return findStructureId8Token(token).flatMap(structureIdList -> {
			structureIdList.add(Structure.WILDCARD);
			Map<String, Object> params = new HashMap<>();
			params.put(TenantEntity.TENANTID_COLUMN, token.getTenantId());
			params.put("structure_id", structureIdList);
			return find8Map(Role.class, params);
		});
	}

}
