package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.data.AbsUniversalController;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "100.通用增删查改")
@RestController
@RequestMapping(name = "通用增删查改", value = GatewayConfig.PATH + "/u/{className}")
public class UniversalController extends AbsUniversalController {

}
