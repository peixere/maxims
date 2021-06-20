package cn.gotom.gateway.m.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.gotom.commons.entities.Tenant;
import cn.gotom.data.service.GenericServiceImpl;
import cn.gotom.gateway.m.dao.TenantDao;
import reactor.core.publisher.Mono;

@Service
public class TenantService extends GenericServiceImpl<Tenant, String> {

	public TenantService(TenantDao dao) {
		super(dao);
	}

	@Override
	protected TenantDao access() {
		return (TenantDao) super.access();
	}

	public Mono<List<Tenant>> find8UserId(String userId) {
		return access().find8UserId(userId);
	}
}
