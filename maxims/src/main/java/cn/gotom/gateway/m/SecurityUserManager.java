package cn.gotom.gateway.m;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import cn.gotom.commons.Response;
import cn.gotom.commons.UserAgent;
import cn.gotom.commons.entities.Conf;
import cn.gotom.commons.entities.LogLogin;
import cn.gotom.commons.entities.Right;
import cn.gotom.commons.entities.Role;
import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.UserSetPassword;
import cn.gotom.commons.entities.WechatUser;
import cn.gotom.commons.json.JSON;
import cn.gotom.commons.model.Token;
import cn.gotom.commons.utils.HttpUtils;
import cn.gotom.commons.utils.ValidatorUtils;
import cn.gotom.commons.webflux.ResponseMono;
import cn.gotom.commons.webflux.SimpleExchange;
import cn.gotom.commons.webflux.SimpleExchangeContext;
import cn.gotom.data.GenericManagerImpl;
import cn.gotom.data.redis.StringTemplate;
import cn.gotom.gateway.m.service.RightService;
import cn.gotom.gateway.m.service.RoleService;
import cn.gotom.gateway.m.service.WechatUserService;
import cn.gotom.gateway.v.form.Permission;
import cn.gotom.security.LoginForm;
import cn.gotom.security.SecurityAuthenticationManager;
import cn.gotom.security.UserPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityUserManager extends GenericManagerImpl<User, String> implements SecurityAuthenticationManager {

	@Autowired
	private UserPasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RightService rightService;

	@Autowired
	private ScheduledExecutorService executor;

	@Autowired
	private WechatUserService wechatUserService;

	@Autowired
	private StringTemplate stringTemplate;

	private String wechatAppid = "wx3b627d9750d1a30b";

	private String wechatAappsecret = "186b3863725d2043460e57235c5d6d25";

	public SecurityUserManager() {
		super();
	}

	@Override
	protected void init() {
		wechatAppid = stringTemplate.get("_:wechat:appid");
		wechatAappsecret = stringTemplate.get("_:wechat:aappsecret");
	}

	@Override
	public Mono<User> login(Token form) {
		switch (form.getPlatform()) {
		case APPLET:
			return wechat(form);
		default:
			return findByAccount(form.getAccount());
		}
	}

	public Mono<User> wechat(final Token form) {
		WechatUser user = wechatCode2session(form);
		stringTemplate.set("wechat:" + user.getCode(), user);
		return wechatUserService.save(user);
	}

//    account: res.code, //临时登录凭证
//    password: userProfile.rawData, //用户非敏感信息
//    access: userProfile.signature, //签名
//    refresh: userProfile.encryptedData, //用户敏感信息
//    captcha: userProfile.iv, //解密算法的向量
//    platform: 'APPLET'
	private WechatUser wechatCode2session(final Token form) {
		form.setRememberMe(true);
		WechatUser user = JSON.parseObject(form.getPassword(), WechatUser.class);
		user.setCode(form.getAccount());
		user.setRawData(form.getPassword());
		user.setSignature(form.getAccess());
		user.setEncryptedData(form.getRefresh());
		String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("appid", wechatAppid);
		parameters.put("secret", wechatAappsecret);
		parameters.put("js_code", user.getCode());
		parameters.put("grant_type", "authorization_code");// 默认参数
		String json = HttpUtils.post(requestUrl, parameters);
		Map<String, Object> map = JSON.parseMap(json);
		if (map != null) {
			Object openid = map.get("openid");
			Object sessionKey = map.get("session_key");
			user.setOpenid("" + openid);
			user.setSessionKey("" + sessionKey);
		}
		return user;
	}

	public Mono<User> findAuthorities(Token token) {
		return findById(token.getUserId()).flatMap(user -> {
			return roleService.find8TenantIdAndUserId(token.getTenantId(), token.getUserId())//
					.flatMap(roleList -> findAuthorities(roleList, token, user));
		}).defaultIfEmpty(User.empty());
	}

	private Mono<User> findAuthorities(List<Role> roleList, Token token, User user) {
		return findAuthorities(roleList, token)//
				.flatMap(authorities -> {
					user.setAuthorPatterns(authorities);
					return Mono.just(user);
				});
	}

	private Mono<List<String>> findAuthorities(List<Role> roleList, Token token) {
		List<String> authorities = new ArrayList<>();
		roleList.forEach(e -> {
			authorities.add(e.getAuthorities());
		});
		return findRightList(token, roleList).flatMap(rList -> {
			rList.forEach(right -> authorities.addAll(right.authorities()));
			return Mono.just(authorities);
		});
	}

	public Mono<Response<List<Permission>>> permissions(Token token, List<Permission> permissions) {
		return findRightList(token).flatMap(rightList -> {
			List<String> codes = new ArrayList<>();
			rightList.forEach(r -> codes.add(r.getCode()));
			permissions.forEach(p -> p.setAllow(codes.contains(p.getCode())));
			return ResponseMono.ok(permissions);
		});
	}

	public Mono<List<Right>> findRightList(Token token) {
		return roleService.find8TenantIdAndUserId(token.getTenantId(), token.getUserId())//
				.flatMap(roleList -> findRightList(token, roleList));
	}

	private Mono<List<Right>> findRightList(Token token, List<Role> roleList) {
		if (BooleanUtils.isTrue(token.getSuperAdmin())) {
			return rightService.find8Tenant(token.getTenantId()).flatMap(rights -> {
				final List<Right> rightList = rights.stream()//
						.filter(r -> token.getPlatform().equals(r.getType()))// 过虑
						.sorted(Comparator.comparing(Right::getSort))// 排序
						.collect(Collectors.toList());
				return Mono.just(rightList);
			});
		} else {
			return rightService.find8TokenRoles(token, roleList).flatMap(list -> {
				return Mono.just(list);
			});
		}
	}

	public Mono<Response<List<Right>>> treeMenu(Token token) {
		return findRightList(token).flatMap(rights -> RightManager.monoMenu(rights));
	}

	public Mono<Response<List<Right>>> treeRight(Token token) {
		return findRightList(token).flatMap(rights -> RightManager.monoRight(rights));
	}

	public Mono<User> findByAccount(String account) {
		User user = new User();
		if (ValidatorUtils.isEmail(account)) {
			user.setEmail(account);
		} else if (ValidatorUtils.isMobile(account)) {
			user.setMobile(account);
		} else {
			user.setAccount(account);
		}
		return findOne(user);
	}

	public Mono<User> findByUsername(String account) {
		User user = new User();
		user.setAccount(account);
		return findOne(user);
	}

	public Mono<User> findByEmail(String email) {
		User user = new User();
		user.setEmail(email);
		return findOne(user);
	}

	public Mono<User> findByMobile(String mobile) {
		User user = new User();
		user.setMobile(mobile);
		return findOne(user);
	}

	/**
	 * 设置密码
	 * 
	 * @param token
	 * @param form
	 * @return
	 */
	public Mono<Response<User>> setPassword(Token token, UserSetPassword form) {
		return findByUsername(token.getAccount()).flatMap(user -> setPassword(form, user));
	}

	private Mono<Response<User>> setPassword(UserSetPassword form, User user) {
		if (passwordEncoder.matches(user, form.getPassword())) {
			return setPassword(user, form).flatMap(u -> {
				return SimpleExchangeContext.exchange(exchange -> {
					SimpleExchange.clearCookie(exchange);
					user.setUpdated(form.getUpdated());
					return ResponseMono.ok(user, "修改成功");
				});
			}).defaultIfEmpty(Response.bq("修改失败"));
		}
		return ResponseMono.bq("原密码不正确");
	}

	/**
	 * 取回密码重置
	 * 
	 * @param user
	 * @param form
	 * @return
	 */
	public Mono<UserSetPassword> setPassword(User user, UserSetPassword form) {
		ValidatorUtils.validatorAssert(form);
		if (StringUtils.isNotBlank(user.getSalt())) {
			user.setSalt(passwordEncoder.nextSalt());
		}
		user.setPassword(form.getNewpassword());
		passwordEncoder.encode(user);
		form.setPassword(user.getPassword());
		form.setId(user.getId());
		form.setSalt(user.getSalt());
		return access().update(form);
	}

	/**
	 * 用户注册
	 * 
	 * @param exchange
	 * @param form
	 * @return
	 */
	public Mono<Response<User>> register(ServerWebExchange exchange, LoginForm form) {
		User user = new User();
		user.setAccount(form.getAccount());
		return exists(user).flatMap(bool -> {
			if (bool) {
				exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
				return Mono.just(Response.bq("用户已经存在！"));
			} else {
				user.setPassword(form.getPassword());
				user.setSalt(passwordEncoder.nextSalt());
				return register(exchange, user);
			}
		});
	}

	private Mono<Response<User>> register(ServerWebExchange exchange, User user) {
		UserAgent ua = UserAgent.parse(exchange.getRequest().getHeaders().getFirst(UserAgent.NAME));
		user.setUserAgent(ua);
		user.setDeviceInfo(ua.getDeviceInfo());
		return insert(user).doOnError(error -> {
			log.error("用户注册异常", error);
		}).flatMap(u -> {
			logLogin(exchange, u, "注册成功", null);
			return ResponseMono.ok(user);
		});
	}

	/**
	 * 登录/退出/注册日志
	 * 
	 * @param exchange
	 * @param token
	 * @return
	 */
	public void logLogin(ServerWebExchange exchange, Token token, String memo, Throwable error) {
		try {
			LogLogin logLogin = new LogLogin();
			logLogin.setUserId(token.getId());
			logLogin.setCreater(token.getAccount());
			logLogin.setUpdater(token.getAccount());
			logLogin.setTenantId(token.getTenantId());
			logLogin.setPlatform(token.getPlatform());
			logLogin.setSuccess(true);
			logLogin.setMemo(memo);
			if (error != null) {
				logLogin.setMemo(error.getMessage());
			}
			logLogin.setAddressIp(SimpleExchange.getRemoteIP(exchange.getRequest()));
			logLogin.setForwardedIp(SimpleExchange.getForwardedIP(exchange.getRequest()));
			if (token.getUserAgent() == null) {
				UserAgent ua = UserAgent.parse(exchange.getRequest().getHeaders().getFirst(UserAgent.NAME));
				token.setUserAgent(ua);
				token.setDeviceInfo(ua.getDeviceInfo());
			}
			BeanUtils.copyProperties(token.getUserAgent(), logLogin);
			List<Object> entitysToSave = new ArrayList<>();
			if (StringUtils.isNotBlank(token.getTokenId())) {
				User user = new User();
				user.setId(token.getUserId());
				user.setTokenId(token.getTokenId());
				user.setPlatform(token.getPlatform());
				entitysToSave.add(user);
			}
			entitysToSave.add(logLogin);
			executor.execute(() -> logLoginSave(entitysToSave, exchange, token));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	private void logLoginSave(List<Object> entitysToSave, ServerWebExchange exchange, Token token) {
		access().saveList(entitysToSave)//
				.contextWrite(SimpleExchangeContext.of(exchange, token)).subscribe();
	}

	@Override
	public Mono<List<Conf>> findConf(Conf form) {
		return access().find(form);
	}

	@Override
	public Mono<Conf> insert(Conf conf) {
		return access().insert(conf);
	}

}