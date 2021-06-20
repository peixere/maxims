package cn.gotom.data.r2dbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RoutingAspect implements Ordered {

	private final RoutingConnectionFactory connectionFactory;

	/**
	 * {@link org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager}
	 * @see 事务也在service层
	 * @see 要在事务执行前先执行路由
	 */
	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

	public RoutingAspect(RoutingConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	@Pointcut("execution(* cn.gotom..service..*(..))")
	public void pointcut() {

	}

	@Around("pointcut()")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		return connectionFactory.around(point);
	}

}
