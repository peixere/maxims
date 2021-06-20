package cn.gotom.gateway.m.cache;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gotom.commons.json.JSON;
import cn.gotom.data.redis.StringTemplate;
import cn.gotom.gateway.v.form.ForgetPassword;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CacheService {

	private static final String CAPTCHA = "gw:captcha:";

	private static final String FORGET = "gw:forget:";
	private static final String NEXTSTRUCTURENO = "structure:no";

	@Autowired
	private StringTemplate stringTemplate;

	@Autowired
	private RedissonClient redissonClient;

//	@Autowired
//	private RedissonClient redissonClient;

	public String getCaptcha(String key) {
		String captcha = stringTemplate.opsForValue().get(CAPTCHA + key);
		stringTemplate.delete(CAPTCHA + key);
		return captcha;
	}

	/**
	 * 6分钟后过期
	 * 
	 * @param key
	 * @param value
	 */
	public void setCaptcha(String key, String value) {
		setCaptcha(CAPTCHA + key, value, Duration.ofMinutes(6));
	}

	public void setCaptcha(String key, String value, Duration duration) {
		stringTemplate.opsForValue().set(CAPTCHA + key, value, duration);
	}

	public void setForget(ForgetPassword form) {
		final String key = FORGET + form.getId();
		stringTemplate.opsForValue().set(key, JSON.format(form), Duration.ofMinutes(10));
		form.setCaptcha(null);
	}

	public ForgetPassword getForget(String key) {
		String json = stringTemplate.opsForValue().get(FORGET + key);
		stringTemplate.delete(FORGET + key);
		return JSON.parseObject(json, ForgetPassword.class);
	}

	public int nextStructureNo(String tenantId) {
		String key = NEXTSTRUCTURENO + tenantId;
		int num = stringTemplate.increment(key).intValue();
		if (num < 1000000) {
			num = 1000000;
			RLock lock = redissonClient.getFairLock("fairlock:" + key);
			try {
				// 尝试加锁，最多等待100秒，上锁以后5秒自动解锁
				boolean isLock = lock.tryLock(100, 5, TimeUnit.SECONDS);
				if (isLock) {
					stringTemplate.opsForValue().set(key, Integer.toString(num));
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				lock.unlock();
			}
		}
		return num;
	}
}
