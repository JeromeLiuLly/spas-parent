package com.candao.spas.convert.swing.modules.tsf.model;

import com.candao.spas.convert.sdk.condition.ArgParser;
import com.candao.spas.convert.swing.base.utils.TsfUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.*;

/**
 * 协议中的一个节点
 *
 */
public class TsfNode {

	public String srcName;
	public TsfNodeType type;
	public TsfCond cond;
	public TsfCvt cvt;
	public String dstPath = "";
	public TsfNode parent;
	public List<TsfNode> children;

	public TsfNode connectedNode;

	public Map<TsfNodeField, String> validateErrorMap = new HashMap<TsfNodeField, String>();

	public TsfNode deepCopy() {
		TsfNode o = new TsfNode();
		o.srcName = srcName;
		o.type = type;
		o.cond = cond;
		o.cvt = cvt;
		if (children != null) {
			for (TsfNode node : children) {
				o.addChild(node.deepCopy());
			}
		}
		return o;
	}

	/**
	 * 清理输入树节点与输出树节点不同的信息
	 */
	public void clearExtraData(boolean srcIsFromITree) {
		if (srcIsFromITree) {
			cond = null;
			cvt = null;
			dstPath = "";
		}

		connectedNode = null;

		if (children != null) {
			for (TsfNode node : children) {
				node.clearExtraData(srcIsFromITree);
			}
		}
	}

	public boolean buildConnection(TsfNode oRoot) {
		boolean good = true;

		if (hasDstPath()) {
			TsfNode oNode = oRoot.findNodeByPath(dstPath);
			if (oNode == null || (oNode.connectedNode != null && oNode.connectedNode != this)) {
				good = false;
				dstPath = "";
			} else {
				connectedNode = oNode;
				oNode.connectedNode = this;
			}

		} else if (children != null) {
			for (TsfNode node : children) {
				if (!node.buildConnection(oRoot)) {
					good = false;
				}
			}
		}

		return good;
	}

	public void cleanConnection(TsfNode iRoot) {
		if (connectedNode != null) {
			if (connectedNode.getRoot() != iRoot || !connectedNode.dstPath.equals(srcPath())) {
				connectedNode = null;
			}
		}

		if (children != null) {
			for (TsfNode node : children) {
				node.cleanConnection(iRoot);
			}
		}
	}

	private void addValidateError(List<String> errors, TsfNodeField field, String msg) {
		String curMsg = validateErrorMap.get(field);
		if (curMsg == null) {
			curMsg = msg;
		} else {
			curMsg += "\n" + msg;
		}
		validateErrorMap.put(field, curMsg);

		errors.add(srcPath() + ": " + msg);
	}

	public void validate(List<String> errors, TsfNode oRoot) {
		validateErrorMap.clear();

		if (srcName.isEmpty() && parent != null) {
			addValidateError(errors, TsfNodeField.srcName, "字段名不能为空");
		}

		if (hasDstPath() && children != null && !children.isEmpty()) {
			addValidateError(errors, TsfNodeField.dstPath, "拥有子项，则不能指定输出");
		}

		if (hasDstPath() && oRoot != null) {
			TsfNode dstNode = oRoot.findNodeByPath(dstPath);
			if (dstNode == null) {
				addValidateError(errors, TsfNodeField.dstPath, "输出目标不存在");
			} else if (!dstNode.isLeaf()) {
				addValidateError(errors, TsfNodeField.dstPath, "输出目标不是叶子项");
			}
		}

		if (children != null && !children.isEmpty()) {
			Set<String> keys = new HashSet<String>();
			for (TsfNode node : children) {
				node.validate(errors, oRoot);
			}
			for (TsfNode node : children) {
				String key = node.genDefKey();
				if (!keys.add(key)) {
					node.addValidateError(errors, TsfNodeField.srcName, "键重复");
				}
			}
		}
	}

	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}

	public boolean hasDstPath() {
		return !dstPath.isEmpty();
	}

	public String srcPath() {
		if (parent == null || parent.parent == null) {
			return srcName;
		} else {
			return parent.srcPath() + "." + srcName;
		}
	}

	public TsfNode getChildBySrcName(String srcName) {
		if (children != null) {
			for (TsfNode node : children) {
				if (srcName.equals(node.srcName)) {
					return node;
				}
			}
		}
		return null;
	}

	public TsfNode getRoot() {
		TsfNode v = this;
		while (v.parent != null) {
			v = v.parent;
		}
		return v;
	}

	public TsfNode findNodeByPath(String path) {
		return findNodeByPath(new ArgParser(path, "."));
	}

	public TsfNode findNodeByPath(ArgParser path) {
		if (children == null) {
			return null;
		}

		String name = path.nextArg();
		for (TsfNode node : children) {
			if (name.equals(node.srcName)) {
				if (!path.hasMoreArgs()) {
					return node;
				} else {
					return node.findNodeByPath(path);
				}
			}
		}
		return null;
	}

	private void ensureChildren() {
		if (children == null) {
			children = new ArrayList<TsfNode>();
		}
	}

	public void addChild(TsfNode node) {
		ensureChildren();
		node.parent = this;
		children.add(node);
	}

	public void addChildAt(TsfNode node, int index) {
		ensureChildren();
		node.parent = this;
		children.add(index, node);
	}

	public boolean removeChild(TsfNode node) {
		if (node.parent != this) {
			return false;
		}
		if (children != null && children.remove(node)) {
			node.parent = null;
			return true;
		}
		return false;
	}

	public void clearChildren() {
		if (!isLeaf()) {
			for (TsfNode node : children) {
				node.parent = null;
			}
			children.clear();
		}
	}

	public int getChildIndex(TsfNode node) {
		if (children == null) {
			return -1;
		}
		return children.indexOf(node);
	}

	public void parseRoot(JsonElement json) {
		srcName = "";
		parseChildren(json);
	}

	private String genDefKey() {
		if (cond == null && cvt == null) {
			return srcName;
		}

		StringBuilder sb = new StringBuilder(srcName);
		if (cond != null) {
			sb.append(",cond,");
			cond.toDefArgs(sb);
		}
		if (cvt != null) {
			sb.append(",cvt,");
			cvt.toDefArgs(sb);
		}
		return sb.toString();
	}

	private void parseDefKey(String key) {
		if (!key.contains(",")) {
			srcName = key;

		} else {
			ArgParser args = new ArgParser(key, ",");
			srcName = args.nextArg();

			while (args.hasMoreArgs()) {
				String argType = args.nextArg();
				switch (argType) {
				case "cond":
					cond = new TsfCond(args.nextArg().equals("1"), args.nextArg());
					break;
				case "cvt":
					cvt = new TsfCvt(args);
					break;
				default:
					throw new IllegalArgumentException("invalid key: " + key);
				}
			}
		}
	}

	public JsonElement genDefValue() {
		JsonElement element;
		if (children != null && !children.isEmpty()) {
			JsonObject jsonObject = new JsonObject();
			for (TsfNode node : children) {
				jsonObject.add(node.genDefKey(), node.genDefValue());
			}
			element = jsonObject;
		} else {
			element = new JsonPrimitive(dstPath);
		}
		return element;
	}

	private void parseDefValue(JsonElement value) {
		if (value.isJsonPrimitive()) {
			type = TsfNodeType.raw;
			dstPath = value.getAsString();

		} else if (value.isJsonObject()) {
			parseChildren(value);

		} else {
			throw new IllegalArgumentException("invalid value: " + value);
		}
	}

	private void parseChildren(JsonElement childrenJson) {
		type = TsfNodeType.obj;

		children = new ArrayList<TsfNode>();
		for (Map.Entry<String, JsonElement> entry : childrenJson.getAsJsonObject().entrySet()) {
			TsfNode node = new TsfNode();
			node.parseDefKey(entry.getKey());
			node.parseDefValue(entry.getValue());
			addChild(node);

			if (TsfUtil.isArrChildName(node.srcName)) {
				type = TsfNodeType.arr;
			}
		}
	}

	@Override
	public String toString() {
		return genDefKey() + ":" + genDefValue();
	}

}
