package cn.gotom.wechat;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.json.JSON;
import cn.gotom.commons.utils.HttpUtils;
import cn.gotom.maxims.redis.StringTemplate;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping()
public class LoginController {

	@Autowired
	private StringTemplate stringTemplate;

	@ApiOperation(value = "1.登入接口", httpMethod = "POST")
	@PostMapping(value = "/wechat")
	public Map<String, Object> login(@Validated @RequestBody LoginForm form) throws Exception {
		final int hashCode = form.getCode().hashCode();
		stringTemplate.set("wechat:" + hashCode, JSON.format(form));
		stringTemplate.set("rawdata:" + hashCode, JSON.parseObject(form.getRawData(), RawData.class));
		return jscode2session(hashCode, form.getCode());
	}

	private final static String APPID = "wx3b627d9750d1a30b";

	private final static String APPSECRET = "186b3863725d2043460e57235c5d6d25";

	public Map<String, Object> jscode2session(final int hashCode, String code) {
		String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("appid", APPID);
		parameters.put("secret", APPSECRET);
		parameters.put("js_code", code);
		parameters.put("grant_type", "authorization_code");// 默认参数
		String json = HttpUtils.post(requestUrl, parameters);
		stringTemplate.set("wechat:jscode2session:" + hashCode, json);
		return JSON.parseMap(json);
	}
}
