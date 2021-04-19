package com.candao.spas.convert.sdk.node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.candao.spas.convert.common.utils.JsontUtil;
import com.candao.spas.convert.sdk.condition.ArgParser;
import com.candao.spas.convert.sdk.condition.Cond;
import com.candao.spas.convert.sdk.condition.Cvt;
import com.candao.spas.convert.sdk.context.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;

/**
 * 输入节点
 */
@Setter
@Getter
public class SrcNode {

	private String name;

	// 类型转换条件
	private Cond cond;
	private Cvt cvt;
	private List<SrcNode> children;
	private DstNode dstNode;
	private List<DstNode> arrElementDstNodes;

	private boolean isArr = false;

	// 用于处理数组降维（二维变一维）
	private List<SrcNode> normalNodes; // 正常处理的子节点
	private List<SrcNode> beginDelayNodes; // 要延迟处理的子节点
	private List<SrcNode> endDelayNodes; // 之前别人要延迟处理的子节点，现在可以处理了

	// 用于处理数组增维（一维变二维）
	private List<SrcNode> innerArrNodes;

	/**
	 * 是否会匹配多个输入数据（例如*号，如果没有搭配单选条件的话，会匹配数组中的多个元素）
	 */
	public boolean isMulti() {
		return JsontUtil.isArrElementName(name) && (cond == null || cond.isMulti());
	}

	public void convert(JsonElement src, Context context) throws Exception {
		if (cond != null && !cond.accept(src)) {
			return;
		}

		// 断言当前源Json节点是否为空
		if (children == null) {
			JsonElement out;
			if (cvt != null) {
				out = cvt.convert(src);
			} else {
				out = src.deepCopy();
			}

			context.addOut(out,dstNode);
		// 创建源Json节点
		} else {
			convertChildren(src, context);
		}
	}

	public void convertChildren(JsonElement src, Context context) throws Exception {
		if (arrElementDstNodes != null) {
			for (DstNode arrElementDstNode : arrElementDstNodes) {
				context.beginGroup(arrElementDstNode);
			}
		}

		if (endDelayNodes != null) {
			for (SrcNode node : endDelayNodes) {
				JsonElement keptSrc = context.getKeptSrc(node);
				if (keptSrc != null) {
					node.convert(keptSrc, context);
				}
			}
		}

		if (isArr) {
			if (normalNodes != null) {
				JsonArray srcArr = (JsonArray) src;
				for (JsonElement childSrc : srcArr) {
					for (SrcNode node : normalNodes) {
						node.convert(childSrc, context);
					}
				}
			}
		} else {
			JsonObject srcObj = (JsonObject) src;
			if (beginDelayNodes != null) {
				for (SrcNode node : beginDelayNodes) {
					JsonElement childSrc = srcObj.get(node.name);
					if (childSrc != null) {
						context.keepSrc(node, childSrc);
					}
				}
			}
			if (normalNodes != null) {
				for (SrcNode node : normalNodes) {
					JsonElement childSrc = srcObj.get(node.name);
					if (childSrc != null) {
						node.convert(childSrc, context);
					}
				}
			}
			if (beginDelayNodes != null) {
				for (SrcNode node : beginDelayNodes) {
					context.unkeepSrc(node);
				}
			}
		}

		if (arrElementDstNodes != null) {
			for (DstNode arrElementDstNode : arrElementDstNodes) {
				context.endGroup(arrElementDstNode);
			}
		}
	}

	// *************************************************************************

	public void parseKeyDef(String key) throws Exception {
		if (!key.contains(",")) {
			name = key;
		} else {
			ArgParser args = new ArgParser(key, ",");
			name = args.nextArg();
			while (args.hasMoreArgs()) {
				String argType = args.nextArg();
				switch (argType) {
				case "cond":
					cond = Cond.create(args.nextArg().equals("1"), args.nextArg());
					break;
				case "cvt":
					cvt = Cvt.create(args.nextArg(), args.nextArg());
					break;
				default:
					throw new Exception("invalid key: " + key);
				}
			}
		}
	}

	public void parseValueDef(JsonElement value, DstNode dstRoot) throws Exception {
		if (value instanceof JsonPrimitive) {
			String dstPath = value.getAsString();
			if (!dstPath.isEmpty()) {
				dstNode = dstRoot.createNode(dstPath);
			}

		} else if (value instanceof JsonObject) {
			parseChildren((JsonObject) value, dstRoot);

		} else {
			throw new Exception("invalid value: " + value);
		}
	}

	public void parseChildren(JsonObject cfg, DstNode dstRoot) throws Exception {
		// 迭代转换协议的key节点信息
		for (Entry<String, JsonElement> entry : cfg.entrySet()) {
			JsonElement value = entry.getValue();

			// 断言是不是字符串key
			if (value.isJsonPrimitive() && value.getAsString().isEmpty()) {
				continue;
			}

			SrcNode srcNode = new SrcNode();
			srcNode.parseKeyDef(entry.getKey());
			srcNode.parseValueDef(value, dstRoot);

			if (srcNode.dstNode == null && srcNode.children == null) {
				continue;
			}

			addChild(srcNode);
		}

		if (children != null) {
			computeDelayNodes();
			computeArrElementDstNodes();
		}
	}

	private void addChild(SrcNode node) throws Exception {
		boolean isArr = JsontUtil.isArrElementName(node.name);
		if (children != null) {
			if (isArr != this.isArr) {
				throw new Exception("既有下标，又有属性，无法判断容器类型");
			}
		}

		DstNode newDstNode;
		if (!node.isMulti()) {
			newDstNode = node.dstNode;
		} else {
			newDstNode = findContainerForMultiNode(node.dstNode);
		}

		if (children == null) {
			children = new ArrayList<SrcNode>();
			this.isArr = isArr;

			dstNode = newDstNode;
		} else {
			if (!dstNode.containsOrEqual(newDstNode)) {
				if (newDstNode.contains(dstNode)) {
					dstNode = newDstNode;
				} else {
					dstNode = getCommonContainer(dstNode, newDstNode);
				}
			}
		}

		children.add(node);
	}

	// *************************************************************************

	private static DstNode findContainerForMultiNode(DstNode dstNode) {
		DstNode n = dstNode;
		while (n.getParent() != null) {
			n = n.getParent();
			if (n.isArr()) {
				return n;
			}
		}
		return n;
	}

	private static DstNode getCommonContainer(DstNode n1, DstNode n2) {
		while (n1.getParent() != null) {
			if (n1.contains(n2)) {
				return n1;
			}
			n1 = n1.getParent();
		}
		return n1;
	}

	private void computeDelayNodes() throws Exception {
		Set<SrcNode> delayNodes = null;

		for (SrcNode i : children) {
			if (i.dstNode.isArr()) {
				for (SrcNode j : children) {
					if (j != i && (delayNodes == null || !delayNodes.contains(j))) {
						if (i.dstNode.contains(j.dstNode)) {
							if (delayNodes == null) {
								delayNodes = new LinkedHashSet<SrcNode>();
							}
							delayNodes.add(j);
							if (!i.addEndDelayNode(j)) {
								throw new Exception("addEndDelayUnit fail");
							}
						}
					}
				}
			}
		}

		if (delayNodes == null) {
			normalNodes = children;
		} else {
			for (SrcNode i : children) {
				if (!delayNodes.contains(i)) {
					if (normalNodes == null) {
						normalNodes = new ArrayList<SrcNode>();
					}
					normalNodes.add(i);
				}
			}
			beginDelayNodes = new ArrayList<SrcNode>(delayNodes);
		}
	}

	private boolean addEndDelayNode(SrcNode endDelayNode) {
		if (dstNode == endDelayNode.dstNode.getParent()) {
			if (endDelayNodes == null) {
				endDelayNodes = new ArrayList<SrcNode>();
			}
			endDelayNodes.add(endDelayNode);
			computeArrElementDstNodesOfNode(endDelayNode);
			return true;
		}
		for (SrcNode node : normalNodes) {
			if (node.dstNode.contains(endDelayNode.dstNode)) {
				return node.addEndDelayNode(endDelayNode);
			}
		}
		return false;
	}

	// *************************************************************************

	private void computeArrElementDstNodes() {
		judgeAndAddArrElementDstNode(dstNode);

		if (normalNodes != null) {
			for (SrcNode node : normalNodes) {
				computeArrElementDstNodesOfNode(node);
			}
		}
	}

	private void computeArrElementDstNodesOfNode(SrcNode node) {
		DstNode dn = node.dstNode;
		if (dn != dstNode) {
			while (dn.getParent() != dstNode) {
				judgeAndAddArrElementDstNode(dn.getParent());
				dn = dn.getParent();
			}
		}
	}

	private void judgeAndAddArrElementDstNode(DstNode node) {
		if (node.getParent() != null && node.getParent().isArr()) {
			if (arrElementDstNodes == null) {
				arrElementDstNodes = new ArrayList<DstNode>();
			}
			if (!arrElementDstNodes.contains(node)) {
				arrElementDstNodes.add(node);
			}
		}
	}

}
