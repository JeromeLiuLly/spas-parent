package com.candao.spas.convert.swing.modules.project.view;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.project.control.ProjectControl;
import com.candao.spas.convert.swing.modules.project.model.ProjectNode;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * 项目节点弹出菜单
 *
 */
public class ProjectNodeMenu {

	public static ProjectNodeMenu ins = new ProjectNodeMenu();

	private JPopupMenu popupMenu;

	private ProjectNode node;

	private JMenuItem createChildMni = UiUtil.createMenuItem("打开所在文件夹", (ActionEvent e) -> {
		ProjectControl.ins.exploreDir(node);
	});

	public ProjectNodeMenu() {
		popupMenu = new JPopupMenu();
		popupMenu.add(createChildMni);
	}

	public void show(ProjectNode node, MouseEvent e) {
		this.node = node;
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

}