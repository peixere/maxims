package cn.gotom.gateway.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.gotom.commons.Response;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserSetPassword;
import cn.gotom.commons.json.JSON;
import cn.gotom.commons.utils.CaptchaUtils;
import cn.gotom.commons.utils.IOUtils;
import cn.gotom.commons.utils.ValidatorUtils;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.commons.webflux.SimpleExchange;
import cn.gotom.commons.webflux.WebAbsContext;
import cn.gotom.gateway.m.SecurityUserManager;
import cn.gotom.gateway.m.cache.CacheService;
import cn.gotom.gateway.v.form.ForgetPassword;
import cn.gotom.gateway.v.form.ForgetPasswordSet;
import cn.gotom.security.LoginForm;
import cn.gotom.security.SecurityConfig;
import cn.gotom.ws.EchoHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

/**
 * 取验证码, 用户注册, 取回密码, 忘记帐户, 激活帐户
 * 
 * @author Administrator
 *
 */
@Api(tags = "100.不登录可访问(取验证码, 用户注册, 重置密码)")
@RestController
@RequestMapping(name = "不登录可访问", value = SecurityConfig.NONE)
@Slf4j
public class NoneController extends WebAbsContext {

	@Autowired
	private SecurityUserManager userManager;

	@Autowired
	private CacheService cacheManager;

	@Autowired
	private EchoHandler echoHandler;

	@ApiOperation(value = "用户注册")
	@PostMapping(name = "用户注册", value = "/register")
	public Mono<Response<User>> register(@RequestBody LoginForm form) {
		if (StringUtils.isNotBlank(form.getAccount()) && StringUtils.isNotBlank(form.getPassword())) {
			return exchange(exchange -> userManager.register(exchange, form));
		} else {
			return ResponseMono.bq("请输入用户名和密码");
		}
	}

	@ApiOperation(value = "重置密码-取验证码")
	@PostMapping(name = "重置密码-取验证码", value = "/forget-password")
	public Mono<Response<ForgetPassword>> forgetPassword(@RequestBody ForgetPassword form) {
		form.setId(User.nextId());
		if (ValidatorUtils.isEmail(form.getEmailOrMobile())) {
			return userManager.findByEmail(form.getEmailOrMobile()).flatMap(user -> {
				form.setCaptcha(CaptchaUtils.randomNumber(6));
				cacheManager.setForget(form);
				return ResponseMono.ok(form, "邮件已经发送至[" + form.getEmailOrMobile() + "]，验证码有效期10分钟，请注意查收！");
			}).defaultIfEmpty(Response.bq("邮箱地址[" + form.getEmailOrMobile() + "]未绑定用户！"));
		} else if (ValidatorUtils.isMobile(form.getEmailOrMobile())) {
			return userManager.findByMobile(form.getEmailOrMobile()).flatMap(user -> {
				form.setCaptcha(CaptchaUtils.randomNumber(6));
				cacheManager.setForget(form);
				return ResponseMono.ok(form, "短信已经发送，验证码有效期10分钟，请注意查收！");
			}).defaultIfEmpty(Response.bq("请输入正确的手机号码！"));
		} else {
			return ResponseMono.bq("请输入正确邮箱或手机号码");
		}
	}

	@ApiOperation(value = "重置密码")
	@PostMapping(name = "重置密码", value = "/forget-password-set")
	public Mono<Response<String>> forgetPasswordSet(@RequestBody ForgetPasswordSet form) {
		ValidatorUtils.validatorAssert(form);
		ForgetPassword forget = cacheManager.getForget(form.getId());
		if (forget == null) {
			return Mono.just(Response.bq("验证码已过期，请重新取验证码！"));
		} else if (!forget.getCaptcha().equals(form.getCaptcha())) {
			return Mono.just(Response.bq("验证码不正确，请重新取验证码！"));
		} else {
			return userManager.findByAccount(forget.getEmailOrMobile()).flatMap(user -> {
				UserSetPassword setPassword = new UserSetPassword();
				setPassword.setId(user.getId());
				setPassword.setAccount(user.getAccount());
				setPassword.setNewpassword(form.getPassword());
				return userManager.setPassword(user, setPassword).flatMap(u -> {
					return Mono.just(Response.ok("密码重置完成"));
				}).defaultIfEmpty(Response.bq("密码重置失败"));
			}).defaultIfEmpty(Response.bq("密码重置失败，帐户不存在！"));
		}
	}

	@ApiOperation(value = "取验证码")
	@GetMapping(name = "取验证码", value = "/captcha")
	public Mono<Void> captcha() {
		String code = CaptchaUtils.random(4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			CaptchaUtils.outputImage(400, 80, baos, code);
			byte[] bytes = baos.toByteArray();
			return exchange(exchange -> {
				String id = SimpleExchange.getCookieId(exchange);
				cacheManager.setCaptcha(id, code);
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(MediaType.IMAGE_JPEG);
				response.setStatusCode(HttpStatus.OK);
				return exchange.getResponse()
						.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(bytes))));
			});
		} catch (IOException e) {
			return exchange(exchange -> {
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(MediaType.TEXT_HTML);
				response.setStatusCode(HttpStatus.OK);
				return exchange.getResponse().writeAndFlushWith(
						Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(e.getMessage().getBytes()))));
			});
		} finally {
			IOUtils.close(baos);
		}
	}

	@ApiOperation(value = "发送WS数据给用户")
	@PostMapping(name = "发送WS数据给用户", value = "/ws/send")
	public Mono<Response<String>> wsSend(String message) {
		echoHandler.send(message);
		return ResponseMono.ok(message);
	}
	

	@PostMapping(value = "/wx/login")
	public Mono<Response<String>> wxLogin(Map<String,Object> data) {
		log.info(JSON.format(data));
		return ResponseMono.ok("");
	}
}
