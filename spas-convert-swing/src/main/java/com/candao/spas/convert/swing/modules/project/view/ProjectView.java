package com.candao.spas.convert.swing.modules.project.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import com.candao.spas.convert.common.utils.JTreeUtil;
import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.project.model.ProjectData;
import com.candao.spas.convert.swing.modules.project.model.ProjectEvent;
import com.candao.spas.convert.swing.modules.project.model.ProjectNode;
import com.candao.spas.convert.swing.modules.tsf.control.TsfControl;

/**
 * 项目视图（协议树）
 *
 */
public class ProjectView extends JTree {

	private static final long serialVersionUID = -1601041936645011941L;

	private ProjectData data = ProjectData.ins;

	public ProjectView() {
		super((TreeModel) null);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});

		data.on(ProjectEvent.afterProjectOpen, this::onAfterProjectOpen);
		data.on(ProjectEvent.beforeProjectClose, this::onBeforeProjectClose);
	}

	private void onAfterProjectOpen(Object[] args) {
		setModel(createTreeModel());
		JTreeUtil.expandTree(this, true);
	}

	private TreeModel createTreeModel() {
		ProjectNode pn = data.curProject.rootNode;
		DefaultMutableTreeNode tn = new DefaultMutableTreeNode(pn);
		createChildrenTreeNode(tn, pn);
		return new DefaultTreeModel(tn);
	}

	private void createChildrenTreeNode(DefaultMutableTreeNode parentTn, ProjectNode parentPn) {
		if (parentPn.children != null) {
			for (ProjectNode pn : parentPn.children) {
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode(pn);
				parentTn.add(tn);
				createChildrenTreeNode(tn, pn);
			}
		}
	}

	private void onBeforeProjectClose(Object[] args) {
		setModel(null);
	}

	public void onMouseClicked(MouseEvent e) {
		TreePath selPath = getPathForLocation(e.getX(), e.getY());
		if (selPath != null) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode) selPath.getLastPathComponent();
			ProjectNode pn = (ProjectNode) tn.getUserObject();

			if (UiUtil.isDoubleClick(e)) {
				if (pn.tsf != null) {
					TsfControl.ins.beginEdit(pn.tsf);
				}
			} else if (UiUtil.isRightClick(e)) {
				ProjectNodeMenu.ins.show(pn, e);
			}
		}
	}

}