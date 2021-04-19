package com.candao.spas.convert.swing.base.event;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件分发器
 *
 */
public class EventDispatcher {

	private Map<String, EventListenerQueue> queueMap = new HashMap<String, EventListenerQueue>();

	/**
	 * 添加监听
	 */
	public void on(String event, EventListener listener) {
		EventListenerQueue queue = queueMap.get(event);
		if (queue == null) {
			queue = new EventListenerQueue();
			queueMap.put(event, queue);
		}
		queue.on(listener);
	}

	/**
	 * 移除监听
	 */
	public void off(String event, EventListener listener) {
		EventListenerQueue queue = queueMap.get(event);
		if (queue != null) {
			queue.off(listener);
		}
	}

	/**
	 * 分发事件（参数为null）
	 */
	public void dispatch(String event) {
		dispatch(event, null);
	}

	/**
	 * 分发事件
	 */
	public void dispatch(String event, Object[] args) {
		EventListenerQueue queue = queueMap.get(event);
		if (queue != null) {
			queue.dispatch(args);
		}
	}

}