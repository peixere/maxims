package cn.gotom.gateway.m.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.model.Token;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.gateway.StructureUtils;
import cn.gotom.gateway.m.dao.StructureDao;
import reactor.core.publisher.Mono;

@Service
public class StructureService extends GenericServiceImpl<Structure, String> {

	public StructureService(StructureDao dao) {
		super(dao);
	}

	@Override
	protected StructureDao access() {
		return (StructureDao) super.access();
	}

	/**
	 * 用户所在组织
	 * 
	 * @param token
	 * @param roles
	 * @return
	 */
	public Mono<List<Structure>> find8TenantIdAndUserId(String tenantId, String userId) {
		return access().find8TenantIdAndUserId(tenantId, userId);
	}

	public Mono<List<Structure>> find8ParentId(String tenantId, String parentId) {
		return access().find8ParentId(tenantId, parentId);
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
		return access().find(form);
	}

	public Mono<List<User>> findUser(Token token, String structureId) {
		return access().findUser(token, structureId);
	}

	/**
	 * 可见角色ID
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<String>> findStructureId8Token(Token token) {
		return find8Token(token).flatMap(structureList -> {
			final List<String> structureIdList = new ArrayList<>();
			structureList.forEach(structure -> structureIdList.add(structure.getId()));
			structureIdList.add(Role.WILDCARD);
			return Mono.just(structureIdList);
		});
	}

	/**
	 * 
	 * @see 用户可见组织
	 * 
	 * @see 查询当前用户所在组织和下级组织
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Structure>> find8Token(Token token) {
		return findTree8Token(token).flatMap(treeList -> {
			return Mono.just(StructureUtils.tree2List(treeList));
		});
	}

	/**
	 * @see 用户可见组织树
	 * 
	 * @see 查询当前用户所在组织和下级组织
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Structure>> findTree8Token(Token token) {
		return find8Tenant(token.getTenantId()).flatMap(structureList -> {
			final List<Structure> sortList = structureList.stream()//
					.sorted(Comparator.comparing(Structure::getSort))// 排序
					.collect(Collectors.toList());
			if (token.getSuperAdmin()) {
				return Mono.just(StructureUtils.tree(sortList));
			} else {
				return find8TenantIdAndUserId(token.getTenantId(), token.getUserId()).flatMap(list -> {
					List<Structure> topList = new ArrayList<>();
					for (Structure top : list) {
						List<Structure> tmpList = sortList.stream()//
								.filter(r -> top.getId().equals(r.getId()))// 过虑
								.collect(Collectors.toList());
						if (tmpList.size() > 0) {
							top.setChildren(StructureUtils.tree(sortList, top));
							topList.add(top);
						}
					}
					return Mono.just(topList);
				});
			}
		});
	}

//	/**
//	 * 查用户关联的组织
//	 * 
//	 * @param token
//	 * @return
//	 */
//	public Mono<List<Structure>> findLink8Token(Token token) {
//		if (token.getSuperAdmin()) {
//			return find8Tenant(token.getTenantId());
//		} else {
//			return find8TenantIdAndUserId(token.getTenantId(), token.getUserId());
//		}
//	}

}
