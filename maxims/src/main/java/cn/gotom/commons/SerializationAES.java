package cn.gotom.commons;

import org.springframework.util.StringUtils;

import cn.gotom.commons.crypt.AES;
import cn.gotom.commons.json.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
@Note("消息编码解码器")
public class SerializationAES implements Serialization {

	@Override
	public String encode(final Message msg, String key) {
		try {
			String text = JSON.toJSONString(msg);
			if (StringUtils.hasText(key)) {
				text = AES.encrypt(text, key);
			}
			return text;
		} catch (Throwable e) {
			log.error(msg.toString());
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public Message decode(final String text, String key) {
		Message msg;
		try {
			if (StringUtils.hasText(key)) {
				String json = AES.decrypt(text, key);
				if (json.startsWith("\"")) {
					json = json.substring(1, json.length());
				}
				if (json.endsWith("\"")) {
					json = json.substring(0, json.length() - 1);
				}
				msg = JSON.parseObject(json, Message.class);
			} else {
				msg = JSON.parseObject(text, Message.class);
			}
		} catch (Throwable e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return msg;
	}

	@Override
	public String getAlgorithm() {
		return "AES";
	}

}