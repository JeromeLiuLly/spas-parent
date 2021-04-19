package com.candao.spas.convert.swing.modules.tsf.view;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.base.constants.Icons;
import com.candao.spas.convert.swing.base.ui.NTreeNode;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.model.TsfNode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;


/**
 * 协议元素视图
 *
 */
public class TsfNodeView extends NTreeNode<TsfNodeView> {

	private static final long serialVersionUID = 1895645113255307982L;

	private static final ImageIcon[] expandBtnIcons = new ImageIcon[] {
		UiUtil.createIcon(Icons.empty),
		UiUtil.createIcon(Icons.right),
		UiUtil.createIcon(Icons.down),
	};

	private JLabel condTxt;
	private JLabel cvtTxt;
	private JButton connectBtn;

	public TsfNode node;
	public boolean isInput;

	public TsfNodeView(boolean isInput) {
		super(expandBtnIcons);

		this.isInput = isInput;

		condTxt = UiUtil.createLabelWithBgColor(Color.green);
		infoView.add(condTxt);

		cvtTxt = UiUtil.createLabelWithBgColor(Color.yellow);
		infoView.add(cvtTxt);

		connectBtn = UiUtil.createIconBtn(Icons.caret_right);
		connectBtn.addActionListener(this::onConnect);
		infoView.add(connectBtn);

		UiUtil.onMouseClick(infoView, this::onInfoViewClick);
	}

	private void onInfoViewClick(MouseEvent e) {
		if (UiUtil.isClick(e)) {
			TsfEditView.ins.beginFocasNode(this);

		} else if (UiUtil.isRightClick(e)) {
			TsfEditView.ins.beginFocasNode(this);
			TsfNodeMenu.ins.show(this, e);

		} else if (UiUtil.isDoubleClick(e)) {
			TsfNodeEditView.ins.beginEdit(this);
		}
	}

	public void onEditBegin(TsfNodeEditView editView) {
		infoView.setVisible(false);
		infoContainer.add(editView);
	}

	public void onEditEnd(TsfNodeEditView editView) {
		infoView.setVisible(true);
		infoContainer.remove(editView);
		refreshInfo();
		TsfEditView.ins.repaint();
	}

	public void refreshDstPath() {
		if (TsfNodeEditView.ins.nodeView == this) {
			TsfNodeEditView.ins.refreshDstPath();
		}
		TsfEditView.ins.repaint();
	}

	public void onFocasBegin() {
		infoView.setBackground(Color.orange);
	}

	public void onFocasEnd() {
		infoView.setBackground(normalBg);
	}

	private void onConnect(ActionEvent e) {
		TsfNodeView srcNodeView = TsfEditView.ins.connectingNodeView;

		if (srcNodeView != null) {
			TsfNodeView srcRoot = srcNodeView.getRootView();
			TsfNodeView dstRoot = getRootView();
			if (srcRoot != dstRoot) {
				TsfData.ins.connect(srcRoot.node, srcNodeView.node, dstRoot.node, this.node);
				TsfEditView.ins.endConnect();
				return;
			}
		}

		TsfEditView.ins.beginConnect(this);
	}

	private void refreshInfo() {
		typeIcon.setIcon(UiUtil.createIcon(node.type.icon));
		nameTxt.setText(node.srcName);

		if (node.cond != null) {
			condTxt.setText(" " + node.cond.toViewString());
		} else {
			condTxt.setText("");
		}

		if (node.cvt != null) {
			cvtTxt.setText(" " + node.cvt.toViewString());
		} else {
			cvtTxt.setText("");
		}

		connectBtn.setVisible(node.isLeaf());

		setAllowChild(!node.isLeaf());
	}

	public void setData(TsfNode node) {
		clearData();

		this.node = node;

		if (node == null) {
			return;
		}

		refreshInfo();

		if (node.children != null) {
			for (TsfNode childNode : node.children) {
				createChild(childNode);
			}
		}
	}

	private TsfNodeView createChild(TsfNode childNode) {
		int index = childNode.parent.getChildIndex(childNode);

		TsfNodeView childView = new TsfNodeView(isInput);
		childView.setData(childNode);

		addChild(childView, index);

		return childView;
	}

	private void clearData() {
		if (node != null) {
			node = null;
			clearChildren();
		}
	}

	public void onNodeAdd(TsfNode childNode) {
		createChild(childNode);
		if (!allowChild) {
			refreshInfo();
		}
		UiUtil.repaint(this);
	}

	public void onNodeRemove(TsfNode childNode) {
		for (TsfNodeView view : children) {
			if (view.node == childNode) {
				removeChild(view);
				if (node.isLeaf()) {
					refreshInfo();
				}
				UiUtil.repaint(this);
				break;
			}
		}
	}

	public void onChildrenClear() {
		clearChildren();
		refreshInfo();
		UiUtil.repaint(this);
	}

}
