package com.candao.spas.convert.swing.modules.app.view;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.app.model.AppData;
import com.candao.spas.convert.swing.modules.app.model.Setting;
import com.candao.spas.convert.swing.modules.app.model.SettingEvent;
import com.candao.spas.convert.swing.modules.project.control.ProjectControl;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.model.TsfEvent;

/**
 * 所有菜单
 *
 */
public class Menus {

	public static final Menus ins = new Menus();

	private JMenu recentProjectsMn;

	void create(JMenuBar menuBar) {
		JMenu fileMn = new JMenu("文件(F)");
		fileMn.setMnemonic(KeyEvent.VK_F);
		createFileMnis(fileMn);
		menuBar.add(fileMn);

		JMenu editMn = new JMenu("编辑(E)");
		editMn.setMnemonic(KeyEvent.VK_E);
		createEditMnis(editMn);
		menuBar.add(editMn);
	}

	private void createFileMnis(JMenu menu) {
		JMenuItem saveMni = UiUtil.createMenuItem("保存", (ActionEvent e) -> {
			TsfData.ins.save();
		});
		saveMni.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		TsfData.ins.on(TsfEvent.modifiedChange, (Object[] args) -> {
			saveMni.setEnabled(TsfData.ins.modified);
		});
		saveMni.setEnabled(false);
		menu.add(saveMni);

		JMenuItem openProjectMni = UiUtil.createMenuItem("打开项目", (ActionEvent e) -> {
			ProjectControl.ins.chooseOpenProject();
		});
		openProjectMni.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		menu.add(openProjectMni);

		recentProjectsMn = new JMenu("最近项目");
		AppData.ins.on(SettingEvent.recentProjectsChange, this::refreshRecentProjects);
		refreshRecentProjects(null);
		menu.add(recentProjectsMn);
	}

	private void createEditMnis(JMenu menu) {
		JMenuItem genTsfMni = UiUtil.createMenuItem("根据输入输出json自动生成", (ActionEvent e) -> {
			TsfData.ins.genTsf();
		});
		TsfData.ins.on(TsfEvent.editBegin, (Object[] args) -> {
			genTsfMni.setEnabled(true);
		});
		TsfData.ins.on(TsfEvent.editEnd, (Object[] args) -> {
			genTsfMni.setEnabled(false);
		});
		genTsfMni.setEnabled(false);
		menu.add(genTsfMni);
	}

	private void refreshRecentProjects(Object[] args) {
		recentProjectsMn.removeAll();
		for (String path : Setting.ins.recentProjects) {
			recentProjectsMn.add(UiUtil.createMenuItem(path, (ActionEvent e) -> {
				ProjectControl.ins.openProject(new File(e.getActionCommand()));
			}));
		}
	}

}
