package com.candao.spas.convert.swing.main;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.app.control.AppControl;
import com.candao.spas.convert.swing.modules.app.model.AppData;

import java.awt.EventQueue;
import java.awt.Font;

/**
 * json转换编辑器程序入口
 */
public class Jsonte {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UiUtil.setDefaultFont(new Font("微软雅黑", Font.PLAIN, 12));
					AppData.ins.init();
					AppControl.ins.init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}