package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.entities.LogLogin;
import cn.gotom.data.AbsGenericController;
import cn.gotom.data.AspectController.Ignore;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "103.登录日志")
@RestController
@RequestMapping(name = "登录日志", value = GatewayConfig.PATH + "/log/login")
@Ignore
public class LogLoginController extends AbsGenericController<LogLogin, String> {

}
