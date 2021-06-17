package cn.gotom.commons.config;

import cn.gotom.commons.listener.GenericListener;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Getter
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class ConfigListener {

	private final @NonNull String dataId;
	private final @NonNull String group;
	private final @NonNull GenericListener<String> listener;

	private String configInfo;

	public static ConfigListener of(GenericListener<String> listener, String dataId, String group) {
		return new ConfigListener(dataId, group, listener);
	}

	public void receiveConfigInfo(String config) {
		try {
			log.info(String.format("receive [group=%s, dataId=%s] %s", group, dataId, config));
			if (config != null && config.equals(configInfo)) {
				return;
			}
			configInfo = config;
			listener.event(config);
		} catch (Exception e) {
			log.error(String.format("receive [group=%s, dataId=%s]", group, dataId), e);
		}
	}
}
