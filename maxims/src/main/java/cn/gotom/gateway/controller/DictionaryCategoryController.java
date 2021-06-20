package cn.gotom.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.entities.DictionaryCategory;
import cn.gotom.data.AbsGenericController;
import cn.gotom.gateway.GatewayConfig;
import io.swagger.annotations.Api;

@Api(tags = "103.字典类别")
@RestController
@RequestMapping(name = "字典类别", value = GatewayConfig.PATH + "/dictionary-category")
public class DictionaryCategoryController extends AbsGenericController<DictionaryCategory, String> {

}
