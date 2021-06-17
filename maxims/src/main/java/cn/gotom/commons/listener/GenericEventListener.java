package cn.gotom.commons.listener;

import java.util.EventListener;

public interface GenericEventListener<T, E> extends EventListener {
	void event(T object, E event);
}
