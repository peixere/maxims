package cn.gotom.gateway.controller;

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
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.Tenant;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserSetPassword;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.commons.webflux.SimpleExchange;
import cn.gotom.commons.webflux.SimpleExchangeContext;
import cn.gotom.commons.webflux.WebAbsContext;
import cn.gotom.data.AspectController.Ignore;
import cn.gotom.gateway.m.SecurityUserManager;
import cn.gotom.gateway.m.StructureManager;
import cn.gotom.gateway.m.TenantManager;
import cn.gotom.gateway.v.form.Permission;
import cn.gotom.security.SecurityConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@Api(tags = "100.登录可访问")
@ApiModel("(修改密码，菜单或按钮权限判断，取菜单树，取功能树)")
@RestController
@RequestMapping(name = "公用功能", value = SecurityConfig.GUEST)
@Ignore
public class GuestController extends WebAbsContext {

	@Autowired
	private SecurityUserManager userManager;

	@Autowired
	private TenantManager tenantManager;

	@Autowired
	private StructureManager structureManager;

	@Autowired
	private SecurityUserManager securityUserManager;

	@ApiOperation(value = "修改密码")
	@PostMapping(name = "修改密码", value = "/set-password")
	public Mono<Response<User>> setPassword(@RequestBody UserSetPassword form) {
		return token(token -> userManager.setPassword(token, form));
	}

	@ApiOperation(value = "菜单或按钮权限判断")
	@PostMapping(name = "权限判断", value = "/permissions")
	public Mono<Response<List<Permission>>> permissions(@RequestBody List<Permission> permissions) {
		return token(token -> userManager.permissions(token, permissions));
	}

	@ApiOperation(value = "我的菜单树")
	@GetMapping(name = "我的菜单树", value = "/tree-menu")
	public Mono<Response<List<Right>>> treeMenu() {
		return token(token -> {
			return userManager.treeMenu(token).flatMap(res -> removeTop(res));
		});
	}

	private Mono<Response<List<Right>>> removeTop(Response<List<Right>> res) {
//		List<Right> rightlist = res.getData();
//		if (rightlist.size() == 1 && RightCategoryEnum.DIR.equals(rightlist.get(0).getCategory())) {
//			if (!CollectionUtils.isEmpty(rightlist.get(0).getChildren())) {
//				res.setData(rightlist.get(0).getChildren());
//			}
//		}
		return Mono.just(res);
	}

	@ApiOperation(value = "我的功能树")
	@GetMapping(name = "我的功能树", value = "/tree-right")
	public Mono<Response<List<Right>>> treeRight() {
		return token(token -> userManager.treeRight(token).flatMap(res -> removeTop(res)));
	}

	@ApiOperation(value = "我的信息")
	@GetMapping(name = "我的信息", value = "/my-info")
	public Mono<Response<User>> myInfo() {
		return token(token -> {
			return userManager.findResponse(token.getUserId()).flatMap(resp -> {
				if (resp.ok()) {
					return tenantManager.findById(token.getTenantId()).flatMap(tenant -> {
						resp.getData().setCurrentTenant(tenant);
						return Mono.just(resp);
					}).defaultIfEmpty(resp);
				} else {
					return Mono.just(resp);
				}
			});
		});
	}

	@ApiOperation(value = "修改我的信息")
	@PostMapping(name = "修改我的信息", value = "/my-info")
	public Mono<Response<User>> myInfo(@RequestBody User form) {
		return context(context -> myInfo(form, context));
	}

	private Mono<Response<User>> myInfo(User form, SimpleExchangeContext context) {
		Token token = context.getToken();
		if (StringUtils.isBlank(form.getId())) {
			form.setId(token.getUserId());
		}
		if (!token.getUserId().equals(form.getId())) {
			return ResponseMono.bq("不要修改他人信息");
		}
		form.setTenantId(token.getTenantId());
		return userManager.update(form).flatMap(user -> {
			context.setToken(context.getToken().authed(user));
			SimpleExchange.setCookie(context.getExchange(), context.getToken());
			return ResponseMono.ok(user);
		}).defaultIfEmpty(Response.bq("修改我的信息失败"));
	}

	@ApiOperation(value = "我的租户")
	@GetMapping(name = "我的租户", value = "/tenant")
	public Mono<Response<List<Tenant>>> tenant() {
		return token(token -> tenantManager.find8UserId(token.getUserId()));
	}

	@ApiOperation(value = "切换租户")
	@GetMapping(name = "切换租户", value = "/tenant/{tenantId}")
	public Mono<Response<User>> tenant(@PathVariable String tenantId) {
		return context(context -> {
			return tenantManager.tenant(tenantId, context).flatMap(res -> {
				if (res.ok()) {
					context.setToken(context.getToken().authed(res.getData()));
					SimpleExchange.setCookie(context.getExchange(), context.getToken());
					securityUserManager.logLogin(context.getExchange(), context.getToken(), "切换租户", null);
				}
				return Mono.just(res);
			});
		});
	}

	@ApiOperation(value = "可见组织")
	@GetMapping(name = "可见组织", value = "/structure")
	public Mono<Response<List<Structure>>> structure() {
		return token(token -> structureManager.find8Token(token).flatMap(data -> ResponseMono.ok(data)));
	}

	@ApiOperation(value = "可见组织树")
	@GetMapping(name = "可见组织树", value = "/structure/tree")
	public Mono<Response<List<Structure>>> structureTree() {
		return token(token -> structureManager.tree8Token(token));
	}
}
