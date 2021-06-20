package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.entities.LogOperator;
import cn.gotom.data.AbsGenericController;
import cn.gotom.data.AspectController.Ignore;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "103.操作日志")
@RestController
@RequestMapping(name = "操作日志", value = GatewayConfig.PATH + "/log/operator")
@Ignore
public class LogOperatorController extends AbsGenericController<LogOperator, String> {

}
