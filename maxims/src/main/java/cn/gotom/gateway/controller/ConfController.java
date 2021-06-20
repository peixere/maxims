package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.entities.Conf;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "103.系统参数")
@RestController
@RequestMapping(name = "系统参数", value = GatewayConfig.PATH + "/conf")
public class ConfController extends AbsGenericController<Conf, String> {

}
