package cn.gotom.data.r2dbc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import cn.gotom.data.dao.SimpleRepository;
import cn.gotom.data.dao.SimpleRepositoryFactory;

public class SimpleRepositoryFactoryImpl extends R2dbcRepositoryFactory implements SimpleRepositoryFactory {

	private final DefaultReactiveDataAccessStrategy dataAccessStrategy;
	private final R2dbcEntityTemplate entityTemplate;
	private final Map<Class<?>, SimpleRepositoryImpl<?, Serializable>> repositoryMap = new HashMap<>();

	public SimpleRepositoryFactoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate,
			DefaultReactiveDataAccessStrategy dataAccessStrategy) {
		super(r2dbcEntityTemplate);
		this.dataAccessStrategy = dataAccessStrategy;
		this.entityTemplate = r2dbcEntityTemplate;
	}

	@Override
	public SimpleRepository<?, Serializable> get() {
		if (repositoryMap.keySet().isEmpty()) {
			return null;
		}
		return repositoryMap.get(repositoryMap.keySet().iterator().next());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> SimpleRepository<T, Serializable> get(Class<T> clazz) {
		if (repositoryMap.containsKey(clazz)) {
			return (SimpleRepository<T, Serializable>) repositoryMap.get(clazz);
		}
		SimpleRepositoryImpl<T, Serializable> repository = null;
		repository = new SimpleRepositoryImpl<>(this, getEntityInformation(clazz));
		Assert.notNull(repository, clazz.getName() + " must be entity!");
		repositoryMap.put(clazz, repository);
		return repository;
	}

	@Override
	public <R> SimpleRepository<R, Serializable> get(R entity) {
		@SuppressWarnings("unchecked")
		Class<R> clazz = (Class<R>) ClassUtils.getUserClass(entity);
		return get(clazz);
	}

	@Override
	public DefaultReactiveDataAccessStrategy getDataAccessStrategy() {
		return dataAccessStrategy;
	}

	@Override
	public R2dbcEntityTemplate getEntityTemplate() {
		return entityTemplate;
	}

}
