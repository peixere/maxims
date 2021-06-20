package cn.gotom.gateway.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.RightManager;
import cn.gotom.gateway.m.RoleManager;
import cn.gotom.gateway.m.StructureManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@Api(tags = "102.角色管理")
@RestController
@RequestMapping(name = "角色管理", value = GatewayConfig.PATH + "/role")
public class RoleController extends AbsGenericController<Role, String> {

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private StructureManager structureManager;

	@ApiOperation(value = "可选功能树")
	@GetMapping(name = "可选功能树", value = "/right-tree")
	public Mono<Response<List<Right>>> rightTree() {
		return token(token -> roleManager.findRight(token)//
				.flatMap(rights -> RightManager.monoRight(rights)));
	}

	@ApiOperation(value = "可选组织树")
	@GetMapping(name = "可选组织树", value = "/structure-tree")
	public Mono<Response<List<Structure>>> structureTree() {
		return token(token -> structureManager.tree8Token(token).flatMap(response -> {
			List<Structure> strList = response.getData();
			boolean wildcard = strList.isEmpty();
			for (Structure structure : strList) {
				if (StringUtils.isBlank(structure.getParentId())) {
					wildcard = true;
				}
			}
			if (wildcard) {
				// 顶级机架用户可添加公共角色
				Structure common = new Structure();
				common.setId(Role.WILDCARD);
				common.setName("通用角色");
				strList.add(common);
			}
			return Mono.just(response);
		}));
	}

	@Override
	public Mono<Response<Integer>> deleteById(@PathVariable String id) {
		return roleManager.findById(id).flatMap(role -> {
			return roleManager.delete(role);
		}).defaultIfEmpty(Response.bq("找不到要删除的数据"));
	}

	@Override
	public Mono<Response<Role>> insert(@RequestBody Role role) {
		role.setId(Role.nextId());
		return token(token -> insertOrUpdate(token, role))//
				.flatMap(res -> Mono.just(res))//
				.defaultIfEmpty(Response.er("添加角色失败"));
	}

	@Override
	public Mono<Response<Role>> update(@RequestBody Role role) {
		if (StringUtils.isBlank(role.getId())) {
			return ResponseMono.bq("更新数据ID不允许为空");
		}
		return token(token -> insertOrUpdate(token, role))//
				.flatMap(res -> Mono.just(res))//
				.defaultIfEmpty(Response.er("修改角色失败"));
	}

	@ApiOperation(value = "验证组织机构")
	private Mono<Response<Role>> insertOrUpdate(Token token, Role role) {
		return structureManager.find8Token(token).flatMap(list -> {
			return roleManager.insertOrUpdate(token, role, list);
		});
	}

	@Override
	public Mono<Response<Role>> find(@PathVariable String id) {
		return super.find(id).flatMap(res -> roleManager.findRight(res));
	}

	@Override
	public Mono<Response<List<Role>>> find(@RequestBody Role entity) {
		return token(token -> roleManager.find(entity, token));
	}

	@Override
	public Mono<Response<List<Role>>> findAll() {
		return token(token -> roleManager.find8Token(token))//
				.flatMap(data -> ResponseMono.ok(data));
	}

	@Override
	public Mono<Response<List<Role>>> page(@RequestBody Role entity) {
		return token(token -> roleManager.page(entity, token));
	}
}
