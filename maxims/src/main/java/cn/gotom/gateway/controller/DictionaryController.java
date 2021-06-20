package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.entities.Dictionary;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "103.字典管理")
@RestController
@RequestMapping(name = "字典管理", value = GatewayConfig.PATH + "/dictionary")
public class DictionaryController extends AbsGenericController<Dictionary, String> {

}
