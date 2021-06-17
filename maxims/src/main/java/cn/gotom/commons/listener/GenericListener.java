package cn.gotom.commons.listener;

import java.util.EventListener;

public interface GenericListener<T> extends EventListener {
	void event(T event);
}
