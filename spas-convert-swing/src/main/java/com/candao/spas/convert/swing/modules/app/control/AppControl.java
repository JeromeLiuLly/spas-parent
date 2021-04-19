package com.candao.spas.convert.swing.modules.app.control;

import com.candao.spas.convert.common.utils.ExceptionUtil;
import com.candao.spas.convert.swing.base.constants.Errors;
import com.candao.spas.convert.swing.modules.app.model.Setting;
import com.candao.spas.convert.swing.modules.app.view.AppFrame;
import com.candao.spas.convert.swing.modules.project.control.ProjectControl;
import com.candao.spas.convert.swing.modules.project.model.ProjectData;

import javax.swing.*;
import java.io.File;

public class AppControl {

	public static final AppControl ins = new AppControl();

	public AppFrame appFrame;

	public void init() {
		appFrame = new AppFrame();
		appFrame.setVisible(true);

		Errors.dispatcher.on(Errors.ERROR, this::onError);

		if (Setting.ins.curProject != null && new File(Setting.ins.curProject).isDirectory()) {
			ProjectData.ins.openProject(Setting.ins.curProject);
		}
	}

	private void onError(Object[] args) {
		String msg = (String) args[0];
		Exception exception = (Exception) args[1];

		String title;
		String message;
		if (exception == null) {
			title = "错误";
			message = msg;
		} else {
			title = "异常";
			message = msg + "\n" + ExceptionUtil.getStackTrace(exception);
		}
		JOptionPane.showMessageDialog(AppFrame.ins, message, title, JOptionPane.ERROR_MESSAGE);
	}

	public boolean checkModifiedAndPrompt() {
		return ProjectControl.ins.checkModifiedAndPrompt();
	}

	/**
	 * 关闭程序
	 */
	public void closeWindow() {
		if (checkModifiedAndPrompt()) {
			System.exit(0);
		}
	}

}
