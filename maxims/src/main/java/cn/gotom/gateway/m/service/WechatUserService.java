package cn.gotom.gateway.m.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gotom.commons.entities.User;
import cn.gotom.commons.entities.WechatUser;
import cn.gotom.commons.model.PlatformEnum;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.security.UserPasswordEncoder;
import reactor.core.publisher.Mono;

@Service
public class WechatUserService extends GenericServiceImpl<WechatUser, String> {
	@Autowired
	private UserPasswordEncoder passwordEncoder;

	public WechatUserService() {
		super();
	}

	public Mono<User> save(WechatUser wxUser) {
		WechatUser form = new WechatUser();
		form.setOpenid(wxUser.getOpenid());
		return find(form).flatMap(wxList -> {
			if (wxList.size() > 0) {
				return findById(User.class, wxList.get(0).getUserId());
			} else {
				User user = new User();
				user.setId(User.nextId());
				user.setAccount(wxUser.getOpenid());
				user.setName(wxUser.getNickName());
				user.setIcon(wxUser.getAvatarUrl());
				user.setPlatform(PlatformEnum.APPLET);
				passwordEncoder.setDefault(user);
				List<Object> list = new ArrayList<>();
				list.add(user);
				list.add(wxUser);
				return saveList(list).flatMap(s -> Mono.just(user));
			}
		});
	}
}
