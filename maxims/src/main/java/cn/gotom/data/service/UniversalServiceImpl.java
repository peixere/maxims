
package cn.gotom.data.service;

import org.springframework.transaction.annotation.Transactional;

import cn.gotom.data.dao.UniversalDao;

@Transactional(readOnly = true)
public class UniversalServiceImpl extends UniversalAbsServiceImpl implements UniversalService {

	private UniversalDao universalDao;

	public UniversalServiceImpl(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	@Override
	protected UniversalDao access() {
		return universalDao;
	}

}
