package cn.gotom.data;

import java.io.Serializable;
import java.util.List;

import cn.gotom.commons.Response;
import reactor.core.publisher.Mono;

interface ResponseManager extends UniversalAccess {

//	Mono<Boolean> insertVerify(Object insertOrUpdate);
//
//	Mono<Boolean> updateVerify(Object insertOrUpdate);

	Mono<Response<Integer>> deleteById(Class<?> clazz, String id);

	<T> Mono<Response<Integer>> deleteByIds(Class<T> clazz, List<Serializable> ids);

	<T> Mono<Response<List<T>>> findAllResponse(Class<T> clazz);

	<T> Mono<Response<T>> findResponse(Class<T> clazz, String id);

	<T> Mono<Response<List<T>>> findResponse(T entity);

	<T> Mono<Response<T>> insertResponse(T entity);

	<T> Mono<Response<T>> updateResponse(T entity);

}
