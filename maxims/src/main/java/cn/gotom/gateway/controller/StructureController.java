package cn.gotom.gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.CustomException;
import cn.gotom.commons.Response;
import cn.gotom.commons.entities.Structure;
import cn.gotom.commons.entities.User;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import cn.gotom.gateway.m.StructureManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Mono;

@Api(tags = "102.组织架构")
@RestController
@RequestMapping(name = "组织架构", value = GatewayConfig.PATH + "/structure")
public class StructureController extends AbsGenericController<Structure, String> {

	@Autowired
	private StructureManager structureManager;

	@Override
	public Mono<Response<Integer>> deleteById(@PathVariable String id) {
		return token(token -> structureManager.find8ParentId(token.getTenantId(), id).flatMap(children -> {
			if (children.size() > 0) {
				return Mono.error(CustomException.bq("请先删除下级数据"));
			} else {
				return super.deleteById(id);
			}
		}));
	}

	@Override
	public Mono<Response<Structure>> insert(@RequestBody Structure structure) {
		return token(token -> structureManager.insert(structure, token));
	}

	@Override
	public Mono<Response<Structure>> update(@RequestBody Structure structure) {
		return token(token -> structureManager.update(structure, token));
	}

	@ApiOperation(value = "上级架构", notes = "不允许选择当前编码的架构")
	@GetMapping(name = "上级架构", value = "/tree/{id}")
	public Mono<Response<List<Structure>>> tree(@PathVariable String id) {
		return token(token -> structureManager.tree8Token(token, id));
	}

	@ApiOperation(value = "组织架构树")
	@GetMapping(name = "组织架构树", value = "/tree")
	public Mono<Response<List<Structure>>> tree() {
		return token(token -> structureManager.tree8Token(token));
	}

	@ApiOperation(value = "组织关联用户")
	@GetMapping(name = "组织关联用户", value = "/user/{structureId}")
	public Mono<Response<List<User>>> user(@PathVariable String structureId) {
		return token(token -> structureManager.findUser(token, structureId));
	}
}
