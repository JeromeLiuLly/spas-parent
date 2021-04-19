package com.candao.spas.convert.swing.base.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件监听器队列
 *
 */
public class EventListenerQueue {

	private List<EventListener> listeners = new ArrayList<EventListener>();
	private int dispatchDepth = 0;

	/**
	 * 添加监听
	 */
	public void on(EventListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * 移除监听
	 */
	public void off(EventListener listener) {
		if (dispatchDepth == 0) {
			listeners.remove(listener);
		} else {
			int index = listeners.indexOf(listener);
			if (index != -1) {
				listeners.set(index, null);
			}
		}
	}

	/**
	 * 分发事件
	 */
	public void dispatch(Object[] args) {
		if (!listeners.isEmpty()) {
			++dispatchDepth;
			try {
				int size = listeners.size(); // 以当前size为准，一旦开始分发，里面就有可能on/off，size就变了
				for (int i = 0; i < size; i++) {
					EventListener listener = listeners.get(i);
					if (listener != null) {
						listener.onEvent(args);
					}
				}
			} finally {
				--dispatchDepth;
				if (dispatchDepth == 0) {
					compact();
				}
			}
		}
	}

	/**
	 * 压缩存储空间
	 */
	private void compact() {
		int oldSize = listeners.size();
		int newSize = 0;

		for (int i = 0; i < oldSize; i++) {
			EventListener listener = listeners.get(i);
			if (listener != null) {
				if (newSize != i) {
					listeners.set(newSize, listener);
				}
				++newSize;
			}
		}

		if (newSize != oldSize) {
			listeners = listeners.subList(0, newSize);
		}
	}

}
