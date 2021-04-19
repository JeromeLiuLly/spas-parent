package com.candao.spas.convert.swing.modules.tsf.control;

import com.candao.spas.convert.swing.modules.app.control.AppControl;
import com.candao.spas.convert.swing.modules.tsf.model.Tsf;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.view.TsfEditView;
import com.candao.spas.convert.swing.modules.tsf.view.TsfNodeEditView;

import javax.swing.*;

/**
 * tsf模块控制器
 *
 */
public class TsfControl {

	public static final TsfControl ins = new TsfControl();

	private TsfData data = TsfData.ins;

	/**
	 * 编辑协议
	 */
	public boolean beginEdit(Tsf tsf) {
		if (!endEdit()) {
			return false;
		}

		if (!data.beginEdit(tsf)) {
			return false;
		}

		TsfEditView.ins.beginEdit();
		return true;
	}

	public boolean endEdit() {
		if (data.tsf != null) {
			if (!checkModifiedAndPrompt()) {
				return false;
			}

			TsfEditView.ins.endEdit();
			data.endEdit();
		}
		return true;
	}

	public boolean checkModifiedAndPrompt() {
		TsfNodeEditView.ins.endEdit();

		if (data.modified) {
			if (!promptForSave()) {
				return false;
			}
		}

		return true;
	}

	private boolean promptForSave() {
		int result = JOptionPane.showConfirmDialog(AppControl.ins.appFrame, "文件已修改，是否保存？", "是否保存文件", JOptionPane.YES_NO_CANCEL_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			return data.save();
		}

		return result != JOptionPane.CANCEL_OPTION;
	}

}