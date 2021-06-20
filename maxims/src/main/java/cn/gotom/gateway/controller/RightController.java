package cn.gotom.gateway.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.CustomException;
import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.RightCategoryEnum;
import cn.gotom.commons.entities.RightTenant;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.RightManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import reactor.core.publisher.Mono;

@Api(tags = "101.功能管理")
@RestController
@RequestMapping(name = "功能管理", value = GatewayConfig.PATH + "/right")
public class RightController extends AbsGenericController<Right, String> {

	@Autowired
	private RightManager rightManager;

	@ApiOperation(value = "取功能树")
	@GetMapping(name = "取功能树", value = "/tree/{platform}")
	public Mono<Response<List<Right>>> tree(//
			@ApiParam(value = "平台类型", required = false) //
			@PathVariable(required = false) //
			String platform) {
		Right form = new Right();
		form.setType(PlatformEnum.of(platform));
		return rightManager.find(form).flatMap(rights -> RightManager.monoRight(rights));
	}

	@ApiOperation(value = "取菜单树")
	@GetMapping(name = "取菜单树", value = "/menu/{platform}")
	public Mono<Response<List<Right>>> menu(//
			@ApiParam(name = "platform", value = "平台类型", required = false) //
			@PathVariable(name = "platform", required = false) //
			String platform) {
		Right form = new Right();
		form.setType(PlatformEnum.of(platform));
		return rightManager.find(form).flatMap(rights -> RightManager.monoMenu(rights));
	}

	@ApiOperation(value = "可选上级")
	@GetMapping(name = "可选上级", value = "/menu/{platform}/{id}")
	public Mono<Response<List<Right>>> menup(//
			@ApiParam(name = "platform", value = "平台类型", required = false) //
			@PathVariable(name = "platform", required = false) //
			String platform, //
			@ApiParam(name = "id", value = "编辑的ID", required = false) //
			@PathVariable(name = "id", required = false) //
			String id) {
		Right form = new Right();
		form.setType(PlatformEnum.of(platform));
		return rightManager.find(form).flatMap(rights -> RightManager.treeParentMenu(rights, id));
	}

	@Override
	public Mono<Response<Right>> find(@PathVariable String id) {
		return rightManager.findById(id).flatMap(right -> {
			if (!right.getCategory().equals(RightCategoryEnum.DIR)) {
				return rightManager.find8ParentId(id).flatMap(children -> {
					right.setChildren(children);
					return ResponseMono.ok(right);
				});
			} else {
				return ResponseMono.ok(right);
			}
		});
	}

	@Override
	public Mono<Response<Integer>> deleteById(@PathVariable String id) {
		return rightManager.findById(id).flatMap(right -> {
			return rightManager.find8ParentId(id).flatMap(children -> {
				if (children.size() > 0 && right.getCategory().equals(RightCategoryEnum.DIR)) {
					return Mono.error(CustomException.bq("请先删除下级数据"));
				} else {
					children.add(right);
					return rightManager.removeList(children).flatMap(r -> {
						if (r > 0) {
							return ResponseMono.ok(r, "删除成功");
						} else {
							return Mono.error(CustomException.er("删除失败"));
						}
					});
				}
			});
		}).defaultIfEmpty(Response.bq("数据不存在或已删除"));

	}

	@Override
	public Mono<Response<Right>> insert(@RequestBody Right right) {
		if (StringUtils.isBlank(right.getName())) {
			return ResponseMono.bq("请输入正确的功能名称");
		}
		if (StringUtils.isBlank(right.getCode())) {
			right.setCode(Right.nextId());
		}
		if (StringUtils.isBlank(right.getParentId())) {
			if (right.getCategory().equals(RightCategoryEnum.BTN)) {
				return ResponseMono.bq("请选择" + RightCategoryEnum.URL.memo());
			}
			return token(token -> insert(token, right));
		} else {
			if (right.getParentId().equals(right.getId())) {
				return ResponseMono.bq("本身不能当上级");
			}
			return rightManager.findById(right.getParentId())//
					.flatMap(parent -> {
						CustomException er = validation(right, parent);
						if (er != null) {
							return Mono.error(er);
						}
						return token(token -> insert(token, right));
					}).defaultIfEmpty(Response.bq("上级菜单无法找到！"));
		}
	}

	private Mono<Response<Right>> insert(Token token, Right right) {
		List<Right> rightList = new ArrayList<>();
		right.setId(Right.nextId());
		if (StringUtils.isBlank(right.getWebPage())) {
			right.setWebPage("#");
		}
		rightList.add(right);
		if (right.getCategory().equals(RightCategoryEnum.URL)) {
			List<Right> children = right.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				for (Right child : children) {
					child.setId(Right.nextId());
					child.setParentId(right.getId());
					child.setCategory(RightCategoryEnum.BTN);
				}
				rightList.addAll(children);
			}
		}
		List<Object> entitysToSave = new ArrayList<>();
		for (Right r : rightList) {
			entitysToSave.add(r);
			entitysToSave.add(of(token, r));
		}
		return rightManager.saveList(entitysToSave).flatMap(list -> ResponseMono.ok(right));
	}

	private RightTenant of(Token token, Right right) {
		RightTenant rt = new RightTenant();
		rt.setId(RightTenant.nextId());
		rt.setRightId(right.getId());
		rt.setTenantId(token.getTenantId());
		return rt;
	}

	@Override
	public Mono<Response<Right>> update(@RequestBody Right right) {
		if (StringUtils.isBlank(right.getId())) {
			return ResponseMono.bq("修改时，ID不允许为空");
		}
		if (right.getId().equals(right.getParentId())) {
			return ResponseMono.bq("本身不能当上级");
		}
		if (StringUtils.isBlank(right.getName())) {
			return ResponseMono.bq("请输入正确的功能名称");
		}
		if (StringUtils.isBlank(right.getCode())) {
			right.setCode(Right.nextId());
		}
		if (StringUtils.isBlank(right.getParentId())) {
			if (right.getCategory().equals(RightCategoryEnum.BTN)) {
				return ResponseMono.bq("请选择" + RightCategoryEnum.URL.memo());
			}
			return token(token -> update(token, right));
		} else {
			return rightManager.findById(right.getParentId())//
					.flatMap(parent -> {
						CustomException er = validation(right, parent);
						if (er != null) {
							return Mono.error(er);
						}
						return token(token -> update(token, right));
					}).defaultIfEmpty(Response.bq("上级菜单无法找到！"));
		}
	}

	private Mono<Response<Right>> update(Token token, Right right) {
		if (StringUtils.isBlank(right.getWebPage())) {
			right.setWebPage("#");
		}
		return rightManager.find8ParentId(right.getId()).flatMap(childList -> update(token, right, childList));
	}

	private Mono<Response<Right>> update(Token token, Right right, List<Right> childList) {
		List<Object> entitysToSave = new ArrayList<>();
		entitysToSave.add(right);
		List<Right> children = right.getChildren();
		if (right.getCategory().equals(RightCategoryEnum.URL) && !CollectionUtils.isEmpty(children)) {
			for (Right child : children) {
				child.setId(Right.nextId());
				child.setParentId(right.getId());
				child.setCategory(RightCategoryEnum.BTN);
				boolean insert = true;
				for (Right old : childList) {
					if (old.getName().equals(child.getName())) {
						old.setUrlPatterns(child.getUrlPatterns());
						BeanUtils.copyProperties(old, child);
						insert = false;
					}
				}
				if (insert) {
					entitysToSave.add(of(token, child));
				}
			}
			childList.removeAll(children);
			entitysToSave.addAll(1, children);
		}
		if (right.getCategory().equals(RightCategoryEnum.DIR) || right.getCategory().equals(RightCategoryEnum.BTN)) {
			childList.clear();
		}
		return rightManager.removeThenSave(childList, entitysToSave).flatMap(list -> ResponseMono.ok(right));
	}

	private CustomException validation(Right right, Right parent) {
		if (parent.getCategory().equals(RightCategoryEnum.BTN)) {
			return CustomException.bq(RightCategoryEnum.BTN.memo() + "下不允许有下级");
		} else if (parent.getCategory().equals(RightCategoryEnum.URL)) {
			if (right.getCategory().equals(RightCategoryEnum.URL)//
					|| right.getCategory().equals(RightCategoryEnum.DIR)) {
				return CustomException.bq(RightCategoryEnum.URL.memo() + "下不允许为" + RightCategoryEnum.DIR.memo() + "或"
						+ RightCategoryEnum.URL.memo());
			}
		}
		return null;
	}

	@ApiOperation(value = "初始化菜单(保留最后一次初始化的)")
	@PostMapping(name = "初始化菜单", value = "/init/{platform}")
	public Mono<Response<String>> init(@PathVariable String platform, @RequestBody List<Right> rightList) {
		return token(token -> rightManager.init(token, PlatformEnum.of(platform), rightList));
	}
}
