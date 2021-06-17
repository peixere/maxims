package cn.gotom.commons.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import cn.gotom.commons.listener.GenericListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConfigManager {

	private final Environment env;

	private final String appName;

	private final String envName;

	public ConfigManager(@Autowired Environment env) {
		this.env = env;
		this.appName = this.env.getProperty("spring.application.name");
		String[] profiles = env.getActiveProfiles();
		if (profiles != null && profiles.length > 0) {
			this.envName = profiles[0];
		} else {
			this.envName = "";
		}
		log.info("appName [{}] envName [{}]", appName, envName);
	}

	public void global(final String id, GenericListener<String> listener) {
		register(id, true, listener);
	}

	public void register(final String id, GenericListener<String> listener) {
		register(id, false, listener);
	}

	private void register(final String id, boolean global, GenericListener<String> listener) {
		String dataId = global ? id : (appName + "-" + id);
		ConfigListener configListener = null;
		if (StringUtils.isBlank(envName)) {
			configListener = ConfigListener.of(listener, dataId, "");
		} else {
			configListener = ConfigListener.of(listener, dataId, envName + "-");
		}
		configListener.receiveConfigInfo(null);
	}

}
