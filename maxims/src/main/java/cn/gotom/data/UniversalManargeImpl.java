package cn.gotom.data;

import cn.gotom.data.service.UniversalAbsService;
import cn.gotom.data.service.UniversalService;

public final class UniversalManargeImpl extends UniversalAccessImpl implements UniversalManager {

	private UniversalService access;

	public UniversalManargeImpl(UniversalService access) {
		this.access = access;
	}

	@Override
	protected UniversalAbsService access() {
		return access;
	}

}
