package com.candao.spas.convert.swing.modules.tsf.view;

import com.candao.spas.convert.common.utils.UiUtil;
import com.candao.spas.convert.swing.modules.tsf.model.Tsf;
import com.candao.spas.convert.swing.modules.tsf.model.TsfData;
import com.candao.spas.convert.swing.modules.tsf.model.TsfEvent;
import com.candao.spas.convert.swing.modules.tsf.model.TsfNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * 协议编辑视图
 *
 */
public class TsfEditView extends JPanel {

	private static final long serialVersionUID = -1301329499745413755L;

	public static TsfEditView ins;

	private TsfNodeView iTree;
	private TsfNodeView oTree;

	private TsfNodeView focasingNodeView;
	public TsfNodeView connectingNodeView;

	private Graphics graphics;

	private TsfData data = TsfData.ins;
	private Tsf tsf;

	public TsfEditView() {
		ins = this;

		setLayout(new BorderLayout(0, 0));

		iTree = new TsfNodeView(true);
		iTree.setVisible(false);
		add(iTree, BorderLayout.WEST);

		oTree = new TsfNodeView(false);
		oTree.setVisible(false);
		add(oTree, BorderLayout.EAST);

		new TsfNodeEditView();

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMove(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				onMousePressed(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseClicked(MouseEvent e) {}
		});

		data.on(TsfEvent.treeChange, this::setTreeData);
		data.on(TsfEvent.nodeAdd, this::onNodeAdd);
		data.on(TsfEvent.childRemove, this::onNodeChildRemove);
		data.on(TsfEvent.childrenClear, this::onNodeChildrenClear);
		data.on(TsfEvent.connectChange, this::onConnectChange);
	}

	private void onMouseMove(MouseEvent e) {
		if (connectingNodeView != null) {
			repaint();
		}
	}

	private void onMousePressed(MouseEvent e) {
		endConnect();
		TsfNodeEditView.ins.endEdit();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (tsf == null) {
			return;
		}

		graphics = g;
		drawConnectedLines();
		drawConnectingLine();
	}

	private void drawConnectedLines() {
		drawConnectedLines(iTree);
	}

	private void drawConnectedLines(TsfNodeView iNode) {
		if (iNode.node.hasDstPath()) {
			drawConnectedLine(iNode);
		} else if (iNode.children != null) {
			for (TsfNodeView node : iNode.children) {
				drawConnectedLines(node);
			}
		}
	}

	private void drawConnectedLine(TsfNodeView iNode) {
		TsfNodeView oNode = oTree.findNodeViewByPath(iNode.node.dstPath);
		if (oNode == null) {
			return;
		}

		Rectangle b1 = UiUtil.getBounds(iNode.infoContainer, this);
		Rectangle b2 = UiUtil.getBounds(oNode.infoContainer, this);
		if (b1 == null || b2 == null) {
			return;
		}
		graphics.setColor(Color.green);
		graphics.drawLine(b1.x + b1.width, b1.y + b1.height / 2, b2.x, b2.y + b2.height / 2);
	}

	private void drawConnectingLine() {
		if (connectingNodeView != null) {
			Point pos = getMousePosition(true);
			if (pos != null) {
				Rectangle b1 = UiUtil.getBounds(connectingNodeView.infoContainer, this);
				if (b1 != null) {
					graphics.setColor(Color.blue);
					graphics.drawLine(b1.x + b1.width, b1.y + b1.height / 2, pos.x, pos.y);
				}
			}
		}
	}

	public void beginEdit() {
		this.tsf = data.tsf;

		setTreeData(null);

		iTree.setVisible(true);
		oTree.setVisible(true);

		repaint();
	}

	public void endEdit() {
		if (tsf != null) {
			iTree.setVisible(false);
			oTree.setVisible(false);
			iTree.setData(null);
			oTree.setData(null);
			tsf = null;
		}
	}

	private void setTreeData(Object[] args) {
		iTree.setData(data.iRoot);
		oTree.setData(data.oRoot);
		iTree.repaint();
		UiUtil.repaint(oTree);
	}

	private TsfNodeView getTree(TsfNode node) {
		TsfNode root = node.getRoot();
		if (root == iTree.node) {
			return iTree;
		} else if (root == oTree.node) {
			return oTree;
		}
		return null;
	}

	private void onNodeAdd(Object[] args) {
		TsfNode node = (TsfNode) args[0];

		TsfNodeView tree = getTree(node);

		if (tree != null) {
			String path = node.parent.srcPath();
			TsfNodeView parentView = tree.findNodeViewByPath(path);
			parentView.onNodeAdd(node);
		}
	}

	private void onNodeChildRemove(Object[] args) {
		TsfNode node = (TsfNode) args[0];
		TsfNode child = (TsfNode) args[1];

		TsfNodeView tree = getTree(node);

		if (tree != null) {
			String path = node.srcPath();
			TsfNodeView view = tree.findNodeViewByPath(path);
			view.onNodeRemove(child);
		}
	}

	private void onNodeChildrenClear(Object[] args) {
		TsfNode node = (TsfNode) args[0];

		TsfNodeView tree = getTree(node);

		if (tree != null) {
			String path = node.srcPath();
			TsfNodeView view = tree.findNodeViewByPath(path);
			view.onChildrenClear();
		}
	}

	public void beginConnect(TsfNodeView nodeView) {
		connectingNodeView = nodeView;
		repaint();
	}

	public void endConnect() {
		connectingNodeView = null;
		repaint();
	}

	private void onConnectChange(Object[] args) {
		TsfNode iNode = (TsfNode) args[0];
		TsfNodeView nodeView = iTree.findNodeViewByPath(iNode.srcPath());
		if (nodeView != null) {
			nodeView.refreshDstPath();
		}
		repaint();
	}

	public void beginFocasNode(TsfNodeView nodeView) {
		if (focasingNodeView != nodeView) {
			endFocasNode();

			focasingNodeView = nodeView;

			if (nodeView != null) {
				focasingNodeView.onFocasBegin();
			}
		}
	}

	public void endFocasNode() {
		if (focasingNodeView != null) {
			focasingNodeView.onFocasEnd();
			focasingNodeView = null;
		}
	}

}
