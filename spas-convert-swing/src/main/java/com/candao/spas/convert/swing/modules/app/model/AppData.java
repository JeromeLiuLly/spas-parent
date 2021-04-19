package com.candao.spas.convert.swing.modules.app.model;

import com.candao.spas.convert.swing.base.event.EventDispatcher;

/**
 * 程序模块数据
 *
 */
public class AppData extends EventDispatcher {

	public static final AppData ins = new AppData();

	public void init() throws Exception {
		Setting.load();
	}

}
