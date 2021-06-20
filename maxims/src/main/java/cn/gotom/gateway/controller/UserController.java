package cn.gotom.gateway.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.StructureManager;
import cn.gotom.gateway.m.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@Api(tags = "102.用户管理")
@RestController
@RequestMapping(name = "用户管理", value = GatewayConfig.PATH + "/user")
public class UserController extends AbsGenericController<User, String> {

	@Autowired
	private UserManager userManager;

	@Autowired
	private StructureManager structureManager;

	@Override
	public Mono<Response<Integer>> deleteById(@PathVariable String id) {
		return userManager.findById(id).flatMap(user -> {
			if (user.getSuperAdmin()) {
				return ResponseMono.ok(0, "请不要删除初始化数据");
			} else {
				// return userManager.deleteById(id);
				return token(token -> userManager.removeUserTenant(token.getTenantId(), Arrays.asList(id)));
			}
		}).defaultIfEmpty(Response.bq("找不到要删除的数据"));
	}

	@ApiOperation(value = "添加用户", notes = "添加后请分配角色")
	@Override
	public Mono<Response<User>> insert(@RequestBody User form) {
		form.setId(Role.nextId());
		return token(token -> userManager.save(true, token, form).defaultIfEmpty(Response.er("添加用户失败")));
	}

	@ApiOperation(value = "更新用户", notes = "更新后请分配角色")
	@Override
	public Mono<Response<User>> update(@RequestBody User form) {
		if (StringUtils.isBlank(form.getId())) {
			return ResponseMono.bq("更新数据ID不允许为空");
		}
		return token(token -> {
			return userManager.exists(token.getTenantId(), form.getId()).flatMap(bool -> {
				if (bool) {
					return userManager.save(false, token, form).defaultIfEmpty(Response.er("修改用户失败"));
				} else {
					return ResponseMono.bq("用户未关联租户或用户不存在");
				}
			});
		});
	}

	@Override
	public Mono<Response<User>> find(@PathVariable String id) {
		return token(token -> userManager.findDetail8Id(token.getTenantId(), id));
	}

	@ApiOperation(value = "可选角色")
	@PostMapping(name = "可选角色", value = "/roles")
	public Mono<Response<List<Role>>> findRoles(@RequestBody List<String> structureIdList) {
		return token(token -> userManager.findRoles(token, structureIdList)).flatMap(data -> ResponseMono.ok(data));
	}

	@ApiOperation(value = "可选组织树")
	@GetMapping(name = "可选组织树", value = "/structure-tree")
	public Mono<Response<List<Structure>>> structureTreeAll() {
		return token(token -> structureManager.tree8Token(token));
	}

	@Override
	public Mono<Response<List<User>>> find(@RequestBody User form) {
		return token(token -> userManager.find(form, token));
	}

	@Override
	public Mono<Response<List<User>>> findAll() {
		return token(token -> userManager.find8TenantId(token));
	}

	@Override
	public Mono<Response<List<User>>> page(@RequestBody User form) {
		return token(token -> userManager.page(form, token));
	}
}
