package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLockReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.gotom.CustomException;
import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.RightCategoryEnum;
import cn.gotom.commons.entities.RightTenant;
import cn.gotom.commons.entities.RoleRight;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.gateway.m.service.RightService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RightManager extends GenericManagerImpl<Right, String> {

	@Autowired
	private RightService rightService;

	@Autowired
	private RedissonReactiveClient redissonReactiveClient;

	public RightManager() {
		super();
	}

	public static Mono<Response<List<Right>>> treeParentMenu(List<Right> rights, String removeId) {
		final List<Right> sortList = rights.stream()//
				.filter(r -> !r.getId().equals(removeId))// 过虑
				.sorted(Comparator.comparing(Right::getSort))// 排序
				.collect(Collectors.toList());
		return ResponseMono.ok(treeMenu(sortList, null));
	}

	public static Mono<Response<List<Right>>> monoMenu(List<Right> rights) {
		return ResponseMono.ok(treeMenu(rights, null));
	}

	public static Mono<Response<List<Right>>> monoRight(List<Right> rightList) {
		return ResponseMono.ok(treeAll(rightList, null));
	}

	public static List<Right> treeMenu(List<Right> rights) {
		return treeMenu(rights, null);
	}

	public static List<Right> treeAll(List<Right> rightList) {
		return treeAll(rightList, null);
	}

	private static List<Right> treeMenu(List<Right> rights, Right top) {
		List<Right> menus = rights.stream()//
				.filter(r -> (RightCategoryEnum.DIR.equals(r.getCategory())//
						|| RightCategoryEnum.URL.equals(r.getCategory()) //
						|| r.getCategory() == null))
				.collect(Collectors.toList());
		List<Right> tree = treeAll(menus, top);
		return tree;
	}

	private static List<Right> treeAll(final List<Right> rightList, Right top) {
		final List<Right> rights = rightList.stream().sorted(Comparator.comparing(Right::getSort))// 排序
				.collect(Collectors.toList());
		List<Right> topList;
		if (top != null) {
			topList = rights.stream()//
					.filter(right -> top.getId().equals(right.getParentId()))//
					.collect(Collectors.toList());
		} else {
			topList = rights.stream()//
					.filter(right -> StringUtils.isBlank(right.getParentId()))//
					.collect(Collectors.toList());
		}
		rights.removeAll(topList);
		topList.forEach(right -> {
			treeCallback(right, rights);
		});
		return topList;
	}

	private static void treeCallback(Right parent, List<Right> menus) {
		List<Right> children = menus.stream().filter(r -> parent.getId().equals(r.getParentId()))// 过虑
				.collect(Collectors.toList());
		menus.removeAll(children);
		parent.setChildren(children);
		children.forEach(child -> {
			treeCallback(child, menus);
		});
	}

	public Mono<Integer> delete(Right right) {
		RightTenant rt = new RightTenant();
		rt.setRightId(right.getId());
		RoleRight rr = new RoleRight();
		rr.setRightId(right.getId());
		Mono<List<RightTenant>> rtlist = access().find(rt);
		Mono<List<RoleRight>> rrlist = access().find(rr);
		return Mono.zip(rtlist, rrlist).flatMap(tuple -> {
			List<Object> entitysToDelete = new ArrayList<>();
			entitysToDelete.add(right);
			tuple.getT1().forEach(value -> entitysToDelete.add(value));
			tuple.getT2().forEach(value -> entitysToDelete.add(value));
			return removeList(entitysToDelete);
		});
	}

	public Mono<List<Right>> find8ParentId(String parentId) {
		return rightService.find8ParentId(parentId);
	}

	public Mono<List<Right>> findTop() {
		return find8ParentId(null);
	}

	public Mono<Response<Integer>> delete8Id(String id) {
		return findById(id).flatMap(right -> {
			return delete(right).flatMap(r -> {
				if (r > 0) {
					return ResponseMono.ok(r, "删除成功");
				} else {
					return Mono.error(CustomException.er("删除失败"));
				}
			});
		}).defaultIfEmpty(Response.bq("找不到要删除的数据"));
	}

	public Mono<Response<String>> init(Token token, PlatformEnum platform, List<Right> treeList) {
		RLockReactive rlock = redissonReactiveClient.getFairLock("fairlock:right.init");
		return rlock.tryLock(3, 5, TimeUnit.SECONDS).flatMap(luck -> {
			if (luck) {
				return initLock(token, platform, treeList);
			} else {
				log.info("初始化菜菜单正在执行");
				return ResponseMono.bq("初始化菜菜单正在执行");
			}
		});
	}

	private Mono<Response<String>> initLock(Token token, PlatformEnum platform, List<Right> treeList) {
		log.info("初始化菜菜单");
		if (!BooleanUtils.isTrue(token.getSuperAdmin())) {
			return ResponseMono.bq("无权限初始化数据");
		}
		if (platform == null) {
			return ResponseMono.bq("请提交要初始化的平台");
		}
		if (CollectionUtils.isEmpty(treeList)) {
			return ResponseMono.bq("初始化数据不允许为空");
		}
		List<Right> rightList = new ArrayList<>();
		for (Right right : treeList) {
			right.setInitName(right.getName());
			rightList.add(right);
			initCallback(rightList, right);
		}
		Map<String, Right> rightMap = new HashMap<>();
		int sort = 1;
		for (Right right : rightList) {
			if (StringUtils.isBlank(right.getWebPage())) {
				right.setWebPage("#");
			}
			right.setSort(sort++);
			rightMap.put(right.getInitName(), right);
		}
		Right form = new Right();
		form.setType(platform);
		return find(form).flatMap(deleteList -> {
			List<Right> keepList = deleteList.stream()//
					.filter(o -> rightMap.containsKey(o.getInitName()))//
					.collect(Collectors.toList());
			deleteList.removeAll(keepList);
			for (Right keep : keepList) {
				Right p = rightMap.get(keep.getInitName());
				p.setId(keep.getId());
			}
			for (Right p : rightList) {
				if (!CollectionUtils.isEmpty(p.getChildren())) {
					for (Right child : p.getChildren()) {
						child.setParentId(p.getId());
					}
				}
			}
			List<Object> entitysToSave = new ArrayList<>();
			entitysToSave.addAll(rightList);
			rightList.removeAll(keepList);
			for (Right p : rightList) {// 新菜单关联租户
				RightTenant rt = new RightTenant();
				rt.setTenantId(token.getTenantId());
				rt.setRightId(p.getId());
				entitysToSave.add(rt);
			}
			return access().removeThenSave(deleteList, entitysToSave)
					.flatMap(list -> ResponseMono.ok("初始化成功", "初始化成功"));
		});
	}

	private void initCallback(List<Right> rightList, Right p) {
		if (StringUtils.isBlank(p.getId())) {
			p.setId(Right.nextId());
		}
		if (!CollectionUtils.isEmpty(p.getChildren())) {
			for (Right child : p.getChildren()) {
				child.setParentId(p.getId());
				child.setInitName(p.getInitName() + "-" + child.getName());
				rightList.add(child);
				initCallback(rightList, child);
			}
		}
	}
}
