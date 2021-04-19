package com.candao.spas.convert.sdk.context;

import com.candao.spas.convert.sdk.node.DstNode;
import com.candao.spas.convert.sdk.node.SrcNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Context {

	private JsonElement root;

	private DstNode lastParent;
	private JsonElement lastParentOut;
	private Map<DstNode, JsonElement> arrElementOutMap = new HashMap<DstNode, JsonElement>();
	private Map<DstNode, Integer> arrElementRefCountMap = new HashMap<DstNode, Integer>();

	private Map<SrcNode, JsonElement> srcMap;

	public void beginGroup(DstNode dstNode) {
		Integer count = arrElementRefCountMap.get(dstNode);
		if (count == null) {
			arrElementRefCountMap.put(dstNode, 1);
		} else {
			arrElementRefCountMap.put(dstNode, count + 1);
		}
	}

	public void endGroup(DstNode dstNode) {
		Integer count = arrElementRefCountMap.get(dstNode);
		if (count != null) {
			if (count <= 1) {
				arrElementRefCountMap.remove(dstNode);
				arrElementOutMap.remove(dstNode);
				if (lastParent == dstNode) {
					lastParent = null;
					lastParentOut = null;
				}
			} else {
				arrElementRefCountMap.put(dstNode, count - 1);
			}
		}
	}

	public void addOut(JsonElement out, DstNode dstNode) {
		DstNode parent = dstNode.getParent();

		if (parent != lastParent) {
			lastParentOut = createOrFindParentOut(parent);
			lastParent = parent;
		}

		if (parent.isArr()) {
			((JsonArray) lastParentOut).add(out);
		} else {
			((JsonObject) lastParentOut).add(dstNode.getName(), out);
		}
	}

	private JsonElement createOrFindParentOut(DstNode parent) {
		if (parent.getParent() == null) {
			if (root == null) {
				root = createOut(parent);
			}
			return root;

		} else {
			List<DstNode> path = new ArrayList<DstNode>();
			while (parent.getParent() != null) {
				path.add(parent);
				parent = parent.getParent();
			}

			if (root == null) {
				root = createOut(parent);
			}

			JsonElement parentOut = root;

			for (int i = path.size() - 1; i >= 0; --i) {
				DstNode node = path.get(i);
				JsonElement out;
				if (parent.isArr()) {
					out = arrElementOutMap.get(node);
					if (out == null) {
						out = createOut(node);
						((JsonArray) parentOut).add(out);
						arrElementOutMap.put(node, out);
					}
				} else {
					out = ((JsonObject) parentOut).get(node.getName());
					if (out == null) {
						out = createOut(node);
						((JsonObject) parentOut).add(node.getName(), out);
					}
				}
				parent = node;
				parentOut = out;
			}

			return parentOut;
		}
	}

	private JsonElement createOut(DstNode node) {
		if (node.isArr()) {
			return new JsonArray();
		} else {
			return new JsonObject();
		}
	}

	// *************************************************************************

	public void keepSrc(SrcNode node, JsonElement src) {
		if (srcMap == null) {
			srcMap = new HashMap<SrcNode, JsonElement>();
		}
		srcMap.put(node, src);
	}

	public void unkeepSrc(SrcNode node) {
		if (srcMap != null) {
			srcMap.remove(node);
		}
	}

	public JsonElement getKeptSrc(SrcNode node) {
		if (srcMap != null) {
			return srcMap.get(node);
		}
		return null;
	}

}
