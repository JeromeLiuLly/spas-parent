package com.candao.spas.convert.swing.modules.tsf.model;

import com.candao.spas.convert.common.utils.ObjectUtil;
import com.candao.spas.convert.swing.base.constants.Errors;
import com.candao.spas.convert.swing.base.event.EventDispatcher;
import com.candao.spas.convert.swing.base.utils.TsfUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * tsf模块数据
 *
 */
public class TsfData extends EventDispatcher {

	public static final TsfData ins = new TsfData();

	public Tsf tsf;
	public TsfNode iRoot;
	public TsfNode oRoot;

	public boolean modified = false;

	public boolean beginEdit(Tsf tsf) {
		try {
			tsf.beginEdit();
		} catch (IOException e) {
			Errors.dispatch("beginEdit error", e);
			return false;
		}

		TsfNode iRoot;
		TsfNode oRoot;
		try {
			iRoot = TsfUtil.readTree(tsf.inSpecFile);
			oRoot = TsfUtil.readTree(tsf.outSpecFile);
		} catch (Exception e) {
			Errors.dispatch("readTree fail", e);
			return false;
		}

		this.tsf = tsf;
		this.iRoot = iRoot;
		this.oRoot = oRoot;
		modified = false;

		if (!iRoot.buildConnection(oRoot)) {
			modified = true;
		}

		dispatch(TsfEvent.modifiedChange);
		dispatch(TsfEvent.editBegin);

		return true;
	}

	public void endEdit() {
		if (tsf != null) {
			tsf = null;
			iRoot = null;
			oRoot = null;

			if (modified) {
				modified = false;
				dispatch(TsfEvent.modifiedChange);
			}

			dispatch(TsfEvent.editEnd);
		}
	}

	public boolean save() {
		if (tsf != null && modified) {
			List<String> iErrors = new ArrayList<String>();
			List<String> oErrors = new ArrayList<String>();

			iRoot.validate(iErrors, oRoot);
			oRoot.validate(oErrors, null);

			if (!iErrors.isEmpty() || !oErrors.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				appendMsgs(sb, "输入树错误：", iErrors);
				appendMsgs(sb, "输出树错误：", oErrors);
				Errors.dispatch(sb.toString(), null);
				return false;
			}

			TsfUtil.writeTree(iRoot, tsf.inSpecFile);
			TsfUtil.writeTree(oRoot, tsf.outSpecFile);
			modified = false;
			dispatch(TsfEvent.modifiedChange);
		}
		return true;
	}

	private void appendMsgs(StringBuilder sb, String label, List<String> msgs) {
		if (!msgs.isEmpty()) {
			sb.append(label)
				.append("\n");
			for (String msg : msgs) {
				sb.append(msg)
					.append("\n");
			}
		}
	}

	private void setModified() {
		if (!modified) {
			modified = true;
			dispatch(TsfEvent.modifiedChange);
		}
	}

	/**
	 * 连接节点
	 */
	public void connect(TsfNode root1, TsfNode node1, TsfNode root2, TsfNode node2) {
		if (root1 == iRoot) {
			connect(node1, node2);
		} else if (root2 == iRoot) {
			connect(node2, node1);
		}
	}

	/**
	 * 连接节点
	 */
	private void connect(TsfNode iNode, TsfNode oNode) {
		if (iNode.connectedNode != oNode) {
			if (oNode.connectedNode != null) {
				removeConnectOfINode(oNode.connectedNode);
			}
			iNode.dstPath = oNode.srcPath();
			iNode.connectedNode = oNode;
			oNode.connectedNode = iNode;
			dispatch(TsfEvent.connectChange, new Object[] { iNode });
			setModified();
		}
	}

	/**
	 * 移除连接
	 */
	public void removeConnect(TsfNode node) {
		TsfNode root = node.getRoot();
		if (root == iRoot) {
			removeConnectOfINode(node);
		} else if (root == oRoot) {
			if (node.connectedNode != null) {
				removeConnectOfINode(node.connectedNode);
			}
		}
	}

	private void removeConnectOfINode(TsfNode node) {
		if (node.connectedNode != null || node.hasDstPath()) {
			node.connectedNode.connectedNode = null;
			node.connectedNode = null;
			node.dstPath = "";
			dispatch(TsfEvent.connectChange, new Object[] { node });
		}
	}

	public void updateNode(TsfNode node, TsfNode newNode) {
		TsfNode root = node.getRoot();
		boolean modified = false;
		boolean nameModified = false;
		boolean dstPathModified = false;

		if (node.type != newNode.type) {
			node.type = newNode.type;
			modified = true;
		}

		if (!node.srcName.equals(newNode.srcName)) {
			node.srcName = newNode.srcName;
			nameModified = true;
			modified = true;
		}

		if (!ObjectUtil.equal(node.cond, newNode.cond)) {
			node.cond = newNode.cond;
			modified = true;
		}

		if (!ObjectUtil.equal(node.cvt, newNode.cvt)) {
			node.cvt = newNode.cvt;
			modified = true;
		}

		if (!ObjectUtil.equal(node.dstPath, newNode.dstPath)) {
			node.dstPath = newNode.dstPath;
			dstPathModified = true;
			modified = true;
		}

		if (modified) {
			if (dstPathModified && root == iRoot) {
				TsfNode oNode = null;
				if (node.hasDstPath()) {
					oNode = oRoot.findNodeByPath(node.dstPath);
				}
				if (oNode == null) {
					node.dstPath = "";
					removeConnectOfINode(node);
				} else {
					connect(node, oNode);
				}
			}

			if (nameModified && root == oRoot) {
				moveConnect(node);
			}

			setModified();
		}
	}

	private void moveConnect(TsfNode node) {
		if (node.connectedNode != null) {
			node.connectedNode.dstPath = node.srcPath();
			dispatch(TsfEvent.connectChange, new Object[] { node.connectedNode });
		} else if (node.children != null) {
			for (TsfNode child : node.children) {
				moveConnect(child);
			}
		}
	}

	/**
	 * 根据输入输出json自动生成
	 */
	public void genTsf() {
		if (tsf == null) {
			return;
		}

		TsfNode iTree;
		TsfNode oTree;
		try {
			iTree = TsfUtil.genTree(tsf.inFile);
			oTree = TsfUtil.genTree(tsf.outFile);
		} catch (Exception e) {
			Errors.dispatch("genTree fail", e);
			return;
		}

		if (iTree == null && oTree == null) {
			return;
		}

		if (iTree != null) {
			iRoot = iTree;
		}
		if (oTree != null) {
			oRoot = oTree;
		}

		TsfUtil.genDstPath(iRoot, oRoot);

		oRoot.cleanConnection(iRoot);
		iRoot.buildConnection(oRoot);

		dispatch(TsfEvent.treeChange);
		setModified();
	}

	/**
	 * 重复一个节点
	 */
	public void repeatNode(TsfNode node) {
		TsfNode oNode = node.deepCopy();
		int index = node.parent.getChildIndex(node);
		node.parent.addChildAt(oNode, index + 1);

		dispatch(TsfEvent.nodeAdd, new Object[] { oNode });
		setModified();
	}

	/**
	 * 删除一个节点
	 */
	public void deleteNode(TsfNode node) {
		TsfNode parent = node.parent;
		if (parent == null) {
			return;
		}
		if (!parent.removeChild(node)) {
			return;
		}
		dispatch(TsfEvent.childRemove, new Object[] { parent, node });
		setModified();
	}

	/**
	 * 清空子节点
	 */
	public void clearChildren(TsfNode node) {
		if (node.isLeaf()) {
			return;
		}
		node.clearChildren();
		dispatch(TsfEvent.childrenClear, new Object[] { node });
		setModified();
	}

	/**
	 * 复制节点为子项
	 */
	public void copyNodeToChild(TsfNode srcNode, TsfNode dstNode, boolean isCut) {
		TsfNode srcParent = srcNode.parent;
		TsfNode copiedNode = copyNode(srcNode, dstNode, isCut);

		addChildToNode(dstNode, copiedNode);

		if (isCut) {
			dispatch(TsfEvent.childRemove, new Object[] { srcParent, srcNode });
		}
		dispatch(TsfEvent.nodeAdd, new Object[] { copiedNode });
		setModified();
	}

	/**
	 * 复制节点为兄弟项（放在目标节点之前）
	 */
	public void copyNodeToPrevBro(TsfNode srcNode, TsfNode dstNode, boolean isCut) {
		copyNodeToBro(srcNode, dstNode, isCut, false);
	}

	/**
	 * 复制节点为兄弟项（放在目标节点之后）
	 */
	public void copyNodeToNextBro(TsfNode srcNode, TsfNode dstNode, boolean isCut) {
		copyNodeToBro(srcNode, dstNode, isCut, true);
	}

	/**
	 * 复制节点为兄弟项
	 */
	public void copyNodeToBro(TsfNode srcNode, TsfNode dstNode, boolean isCut, boolean isPutNext) {
		if (isCut && srcNode == dstNode) {
			return;
		}

		TsfNode srcParent = srcNode.parent;
		TsfNode dstParent = dstNode.parent;

		int index = dstParent.getChildIndex(dstNode);
		if (isCut && srcParent == dstParent) {
			int srcIndex = srcParent.getChildIndex(srcNode);
			if (srcIndex <= index) {
				--index;
			}
		}

		TsfNode copiedNode = copyNode(srcNode, dstNode, isCut);

		dstParent.addChildAt(copiedNode, (isPutNext ? index + 1 : index));

		if (isCut) {
			dispatch(TsfEvent.childRemove, new Object[] { srcParent, srcNode });
		}
		dispatch(TsfEvent.nodeAdd, new Object[] { copiedNode });
		setModified();
	}

	private TsfNode copyNode(TsfNode srcNode, TsfNode dstNode, boolean isCut) {
		TsfNode srcRoot = srcNode.getRoot();
		TsfNode dstRoot = dstNode.getRoot();
		boolean needClearExtraData = srcRoot != dstRoot;

		TsfNode copiedNode;
		if (isCut) {
			srcNode.parent.removeChild(srcNode);
			copiedNode = srcNode;
		} else {
			copiedNode = srcNode.deepCopy();
		}

		if (needClearExtraData) {
			copiedNode.clearExtraData(srcRoot == iRoot);
		}

		return copiedNode;
	}

	private void addChildToNode(TsfNode node, TsfNode childNode) {
		node.addChild(childNode);
		if (node.type == TsfNodeType.raw) {
			node.type = TsfUtil.isArrChildName(childNode.srcName) ? TsfNodeType.arr : TsfNodeType.obj;
		}
	}

	public void createChildNode(TsfNode node) {
		TsfNode childNode = createNode();
		addChildToNode(node, childNode);
		dispatch(TsfEvent.nodeAdd, new Object[] { childNode });
		setModified();
	}

	public void createBroNode(TsfNode node) {
		TsfNode parent = node.parent;
		int index = parent.getChildIndex(node);

		TsfNode childNode = createNode();
		parent.addChildAt(childNode, index + 1);

		dispatch(TsfEvent.nodeAdd, new Object[] { childNode });
		setModified();
	}

	private TsfNode createNode() {
		TsfNode newNode = new TsfNode();
		newNode.srcName = "";
		newNode.type = TsfNodeType.raw;
		return newNode;
	}

}
