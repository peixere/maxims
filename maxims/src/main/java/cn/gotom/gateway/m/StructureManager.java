package cn.gotom.gateway.m;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.gateway.StructureUtils;
import cn.gotom.gateway.m.cache.CacheService;
import cn.gotom.gateway.m.service.StructureService;
import reactor.core.publisher.Mono;

@Component
public class StructureManager extends GenericManagerImpl<Structure, String> {

	@Autowired
	private StructureService structureService;

	@Autowired
	private CacheService cacheService;

	public StructureManager() {
		super();
	}

	public Mono<List<Structure>> find8ParentId(String tenantId, String parentId) {
		return structureService.find8ParentId(tenantId, parentId);
	}

	/**
	 * 用户可见组织
	 * 
	 * @param token
	 * @return
	 */
	public Mono<List<Structure>> find8Token(Token token) {
		return structureService.find8Token(token);
	}

	public Mono<Response<List<Structure>>> tree8Token(Token token, String removeId) {
		return structureService.findTree8Token(token).flatMap(data -> {
			StructureUtils.removeNode(data, removeId);
			return ResponseMono.ok(data);
		});
	}

	/**
	 * 用户可见组织树
	 * 
	 * @param token
	 * @return
	 */
	public Mono<Response<List<Structure>>> tree8Token(Token token) {
		return structureService.findTree8Token(token).flatMap(data -> ResponseMono.ok(data));
	}

	private Mono<List<Structure>> find8Name(String name, String tenantId) {
		Structure form = new Structure();
		form.setTenantId(tenantId);
		form.setName(name);
		return access().find(form);
	}

	private Mono<List<Structure>> find8No(Integer no, String tenantId) {
		Structure form = new Structure();
		form.setTenantId(tenantId);
		form.setNo(no);
		return access().existsFind(form);
	}

	public Mono<Response<Structure>> insert(Structure structure, Token token) {
		if (structure.getNo() == null || structure.getNo() <= 0) {
			structure.setNo(cacheService.nextStructureNo(token.getTenantId()));
		}
		structure.setId(Role.nextId());
		return find8Name(structure.getName(), token.getTenantId()).flatMap(list -> {
			if (list.size() > 0) {
				return ResponseMono.bq("组织名称已经存在");
			}
			return find8No(structure.getNo(), token.getTenantId()).flatMap(nolist -> {
				if (nolist.size() > 0) {
					return ResponseMono.bq("组织编号已经存在");
				}
				return structureService.insert(structure)//
						.flatMap(r -> ResponseMono.ok(structure))//
						.defaultIfEmpty(Response.er("添加组织架构失败"));
			});
		});
	}

	public Mono<Response<Structure>> update(Structure structure, Token token) {
		if (StringUtils.isBlank(structure.getId())) {
			return ResponseMono.bq("更新数据ID不允许为空");
		}
		if (structure.getNo() == null || structure.getNo() <= 0) {
			structure.setNo(cacheService.nextStructureNo(token.getTenantId()));
		}
		return find8Name(structure.getName(), token.getTenantId()).flatMap(list -> {
			if (list.size() > 0 && !list.get(0).getId().equals(structure.getId())) {
				return ResponseMono.bq("组织名称已经存在");
			}
			return find8No(structure.getNo(), token.getTenantId()).flatMap(nolist -> {
				if (nolist.size() > 0 && !nolist.get(0).getId().equals(structure.getId())) {
					return ResponseMono.bq("组织编号已经存在");
				}
				return structureService.update(structure)//
						.flatMap(r -> ResponseMono.ok(structure))//
						.defaultIfEmpty(Response.er("更新组织架构失败"));
			});
		});
	}

	public Mono<Response<List<User>>> findUser(Token token, String structureId) {
		return structureService.findUser(token, structureId).flatMap(list -> ResponseMono.ok(list));
	}
}
