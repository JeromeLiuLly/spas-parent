package com.candao.spas.convert.swing.modules.tsf.view;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.model.TsfNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


/**
 * 协议元素弹出菜单
 *
 */
public class TsfNodeMenu {

	public static TsfNodeMenu ins = new TsfNodeMenu();

	private JPopupMenu popupMenu;

	private TsfNode node;

	private TsfNode srcNode;
	private boolean srcCut;

	private JMenuItem createChildMni = UiUtil.createMenuItem("添加子项", (ActionEvent e) -> {
		TsfData.ins.createChildNode(node);
	});

	private JMenuItem createBroMni = UiUtil.createMenuItem("添加同级项", (ActionEvent e) -> {
		TsfData.ins.createBroNode(node);
	});

	private JMenuItem repeatMni = UiUtil.createMenuItem("重复本项", (ActionEvent e) -> {
		TsfData.ins.repeatNode(node);
	});

	private JMenuItem copyMni = UiUtil.createMenuItem("复制", (ActionEvent e) -> {
		srcNode = node;
		srcCut = false;
	});

	private JMenuItem cutMni = UiUtil.createMenuItem("剪切", (ActionEvent e) -> {
		srcNode = node;
		srcCut = true;
	});

	private JMenuItem pasteAsChildMni = UiUtil.createMenuItem("", new PasteActionListener(TsfData.ins::copyNodeToChild));

	private JMenuItem pasteAsNextBroMni = UiUtil.createMenuItem("", new PasteActionListener(TsfData.ins::copyNodeToNextBro));
	private JMenuItem pasteAsPrevBroMni = UiUtil.createMenuItem("", new PasteActionListener(TsfData.ins::copyNodeToPrevBro));

	private JMenuItem removeConnectMni = UiUtil.createMenuItem("移除连接", (ActionEvent e) -> {
		TsfData.ins.removeConnect(node);
	});

	private JMenuItem clearChildrenMni = UiUtil.createMenuItem("清空子项", (ActionEvent e) -> {
		TsfData.ins.clearChildren(node);
	});

	private JMenuItem deleteMni = UiUtil.createMenuItem("删除", (ActionEvent e) -> {
		TsfData.ins.deleteNode(node);
	});

	public TsfNodeMenu() {
		popupMenu = new JPopupMenu();
		popupMenu.add(createChildMni);
		popupMenu.add(createBroMni);
		popupMenu.addSeparator();
		popupMenu.add(repeatMni);
		popupMenu.add(copyMni);
		popupMenu.add(cutMni);
		popupMenu.add(pasteAsChildMni);
		popupMenu.add(pasteAsNextBroMni);
		popupMenu.add(pasteAsPrevBroMni);
		popupMenu.addSeparator();
		popupMenu.add(removeConnectMni);
		popupMenu.add(clearChildrenMni);
		popupMenu.add(deleteMni);
	}

	public void show(TsfNodeView nodeView, MouseEvent e) {
		node = nodeView.node;

		repeatMni.setVisible(node.parent != null);
		cutMni.setVisible(node.parent != null);
		deleteMni.setVisible(node.parent != null);
		pasteAsChildMni.setVisible(srcNode != null && !(srcCut && srcNode == node));
		pasteAsNextBroMni.setVisible(srcNode != null && node.parent != null);
		pasteAsPrevBroMni.setVisible(pasteAsNextBroMni.isVisible());
		clearChildrenMni.setVisible(!node.isLeaf());

		String copyTip = null;
		if (srcNode != null) {
			copyTip = srcCut ? "剪切" : "复制";
			copyTip = "(" + copyTip + "来自：" + srcNode.srcPath() + ")";
		}

		if (pasteAsChildMni.isVisible()) {
			pasteAsChildMni.setText("粘贴到子项" + copyTip);
		}
		if (pasteAsNextBroMni.isVisible()) {
			pasteAsNextBroMni.setText("粘贴到同级下方" + copyTip);
			pasteAsPrevBroMni.setText("粘贴到同级上方" + copyTip);
		}

		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private static interface PasteHandler {
		void handle(TsfNode srcNode, TsfNode dstNode, boolean isCut);
	}

	private class PasteActionListener implements ActionListener {
		private PasteHandler handler;

		public PasteActionListener(PasteHandler handler) {
			this.handler = handler;
		}

		public void actionPerformed(ActionEvent e) {
			if (srcNode == null) {
				return;
			}
			boolean isCut = srcCut;
			srcCut = false;
			handler.handle(srcNode, node, isCut);
			if (isCut) {
				srcNode = null;
			}
		}
	}

}