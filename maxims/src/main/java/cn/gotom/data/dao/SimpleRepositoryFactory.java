package cn.gotom.data.dao;

import java.io.Serializable;

import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

public interface SimpleRepositoryFactory {

	SimpleRepository<?, Serializable> get();

	<T> SimpleRepository<T, Serializable> get(Class<T> clazz);

	<R> SimpleRepository<R, Serializable> get(R entity);

	DefaultReactiveDataAccessStrategy getDataAccessStrategy();

	R2dbcEntityTemplate getEntityTemplate();

}