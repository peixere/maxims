package cn.gotom.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Tenant;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.AbsGenericController;
import cn.gotom.data.dao.SQLUtils;
import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.RightManager;
import cn.gotom.gateway.m.TenantManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import reactor.core.publisher.Mono;

@Api(tags = "101.租户管理")
@RestController
@RequestMapping(name = "租户管理", value = GatewayConfig.PATH + "/tenant")
public class TenantController extends AbsGenericController<Tenant, String> {

	@Autowired
	private TenantManager tenantManager;

	@ApiOperation(value = "可选功能树")
	@GetMapping(name = "可选功能树", value = "/right/tree")
	public Mono<Response<List<Right>>> treeAll() {
		return this.treeAll("");
	}

	@ApiOperation(value = "可选功能树")
	@GetMapping(name = "可选功能树", value = "/right/tree/{platform}")
	public Mono<Response<List<Right>>> treeAll(//
			@ApiParam(value = "平台类型", required = false) //
			@PathVariable(required = false) //
			String platform) {
		PlatformEnum type = PlatformEnum.of(platform);
		return token(token -> tenantManager.findRight(token)//
				.flatMap(rightList -> {
					List<Right> rights = rightList.stream()//
							.filter(r -> BooleanUtils.isNotTrue(r.getSuperAdmin()))//
							.collect(Collectors.toList());
					if (type != null) {
						List<Right> menus = rights.stream()//
								.filter(r -> r.getType().equals(type))//
								.collect(Collectors.toList());
						return RightManager.monoRight(menus);
					} else {
						return RightManager.monoRight(rights);
					}
				}));
	}

	@Override
	public Mono<Response<Integer>> deleteById(@PathVariable String id) {
		return tenantManager.findById(id).flatMap(tenant -> {
			if (tenant.getSuperAdmin()) {
				return ResponseMono.ok(0, "请不要删除初始化数据");
			} else {
				return super.deleteById(id);
			}
		}).defaultIfEmpty(Response.bq("找不到要删除的数据"));
	}

	@Override
	public Mono<Response<Tenant>> find(@PathVariable String id) {
		return super.find(id).flatMap(res -> tenantManager.findRight(res));
	}

	@Override
	public Mono<Response<Tenant>> insert(@RequestBody Tenant tenant) {
		return token(token -> tenantManager.insert(tenant, token));
	}

	@Override
	public Mono<Response<Tenant>> update(@RequestBody Tenant tenant) {
		return token(token -> tenantManager.update(tenant, token));
	}

	@Override
	public Mono<Response<List<Tenant>>> find(@RequestBody Tenant tenant) {
		if (tenant.getName() != null) {
			tenant.setName(SQLUtils.isLike(tenant.getName()));
		}
		return tenantManager.findResponse(tenant);
	}

	@Override
	public Mono<Response<List<Tenant>>> page(@RequestBody Tenant tenant) {
		if (tenant.getName() != null) {
			tenant.setName(SQLUtils.isLike(tenant.getName()));
		}
		return tenantManager.page(tenant);
	}
}
