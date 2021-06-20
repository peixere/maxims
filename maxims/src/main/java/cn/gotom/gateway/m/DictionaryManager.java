package cn.gotom.gateway.m;

import org.springframework.stereotype.Component;

import cn.gotom.commons.entities.Dictionary;
import cn.gotom.data.GenericManagerImpl;

@Component
public class DictionaryManager extends GenericManagerImpl<Dictionary, String> {

	public DictionaryManager() {
		super();
	}

}
