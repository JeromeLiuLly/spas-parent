package com.candao.spas.convert.sdk.node;

import com.candao.spas.convert.common.utils.JsontUtil;
import com.candao.spas.convert.sdk.condition.ArgParser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 输出节点
 */
@Getter
@Setter
public class DstNode {

	public String name;
	public List<DstNode> children;
	public DstNode parent;
	public boolean isArr = false;

	public DstNode createNode(String path) throws Exception {
		if (!path.contains(".")) {
			return createChildNode(path);
		} else {
			DstNode node = this;
			ArgParser pathParser = new ArgParser(path, ".");
			while (pathParser.hasMoreArgs()) {
				String name = pathParser.nextArg();
				node = node.createChildNode(name);
			}
			return node;
		}
	}

	private DstNode createChildNode(String name) throws Exception {
		if (children != null) {
			for (DstNode node : children) {
				if (node.name.equals(name)) {
					return node;
				}
			}
		}
		DstNode node = new DstNode();
		node.name = name;
		addChild(node);
		return node;
	}

	private void addChild(DstNode node) throws Exception {
		boolean isArr = JsontUtil.isArrElementName(node.name);
		if (children != null) {
			if (isArr != this.isArr) {
				throw new Exception("既有下标，又有属性，无法判断容器类型");
			}
		}

		node.parent = this;

		if (children == null) {
			children = new ArrayList<DstNode>();
			this.isArr = isArr;

		}
		children.add(node);
	}

	public boolean containsOrEqual(DstNode node) {
		return node == this || contains(node);
	}

	public boolean contains(DstNode node) {
		while (node.parent != null) {
			if (node.parent == this) {
				return true;
			}
			node = node.parent;
		}
		return false;
	}

}
