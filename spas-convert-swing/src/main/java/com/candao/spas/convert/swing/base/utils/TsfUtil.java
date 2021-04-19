package com.candao.spas.convert.swing.base.utils;

import com.candao.spas.convert.common.utils.FileUtil;
import com.candao.spas.convert.sdk.condition.ArgParser;
import com.candao.spas.convert.swing.base.constants.Errors;
import com.candao.spas.convert.swing.modules.tsf.model.TsfNode;
import com.candao.spas.convert.swing.modules.tsf.model.TsfNodeType;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class TsfUtil {

	private static Gson gson = new GsonBuilder()
		.setPrettyPrinting()
		.disableHtmlEscaping()
		.create();

	private static final Pattern arrChildNamePattern = Pattern.compile("\\*|[0-9]+");

	public static boolean isArrChildName(String name) {
		return arrChildNamePattern.matcher(name).matches();
	}

	// *************************************************************************

	/**
	 * 从文件中读出协议节点树
	 */
	public static TsfNode readTree(File file) throws Exception {
		JsonElement json = FileUtil.readJson(file);
		if (json == null) {
			json = new JsonObject();
		}
		TsfNode root = new TsfNode();
		root.parseRoot(json);
		return root;
	}

	/**
	 * 将协议节点树写到文件中
	 */
	public static void writeTree(TsfNode root, File oFile) {
		JsonElement json = root.genDefValue();
		String text = gson.toJson(json);
		try {
			FileUtil.writeTextFile(oFile, text);
		} catch (IOException e) {
			Errors.dispatch("writeTree exception", e);
		}
	}

	// *************************************************************************

	/**
	 * 根据原始数据文件生成规则树
	 */
	public static TsfNode genTree(File file) throws Exception {
		if (!file.isFile()) {
			return null;
		}

		JsonElement json = FileUtil.readJson(file);
		if (json == null || json.isJsonNull() || json.isJsonPrimitive()) {
			return null;
		}

		TsfNode root = new TsfNode();
		root.srcName = "";
		genNode(root, json);
		return root;
	}

	private static void genNode(TsfNode node, JsonElement json) {
		if (json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			node.type = TsfNodeType.obj;
			for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
				TsfNode childNode = new TsfNode();
				childNode.srcName = entry.getKey();
				node.addChild(childNode);
				genNode(childNode, entry.getValue());
			}

		} else if (json.isJsonArray()) {
			JsonArray arr = json.getAsJsonArray();
			node.type = TsfNodeType.arr;

			TsfNode childNode = new TsfNode();
			childNode.srcName = "*";
			node.addChild(childNode);

			if (arr.size() == 0) {
				childNode.type = TsfNodeType.raw;
			} else {
				JsonElement e = arr.get(0);
				genNode(childNode, e);
			}

		} else {
			node.type = TsfNodeType.raw;
		}
	}

	// *************************************************************************

	public static void genDstPath(TsfNode iRoot, TsfNode oRoot) {
		if (iRoot.children != null) {
			genDstPathForChildren("", iRoot, oRoot);
		}
	}

	private static void genDstPathForChildren(String pathPrefix, TsfNode iNode, TsfNode oRoot) {
		for (TsfNode node : iNode.children) {
			String path = pathPrefix + node.srcName;
			if (node.type == TsfNodeType.raw || node.children == null) {
				TsfNode dstNode = oRoot.findNodeByPath(path);
				if (dstNode != null) {
					node.dstPath = path;
				} else {
					node.dstPath = "";
				}
			} else {
				node.dstPath = "";
				genDstPathForChildren(path + ".", node, oRoot);
			}
		}
	}

	// *************************************************************************

	/**
	 * 根据转换规则树生成输出结果树
	 */
	public static TsfNode genDstTree(TsfNode iRoot) {
		TsfNode oRoot = new TsfNode();
		addOPaths(oRoot, iRoot);
		return oRoot;
	}

	private static void addOPaths(TsfNode oRoot, TsfNode iNode) {
		if (iNode.hasDstPath()) {
			addOPath(oRoot, iNode);
		}

		if (iNode.children != null) {
			for (TsfNode node : iNode.children) {
				addOPaths(oRoot, node);
			}
		}
	}

	private static void addOPath(TsfNode oRoot, TsfNode iNode) {
		String path = iNode.dstPath;
		ArgParser pathTokenizer = new ArgParser(path, ".");
		TsfNode parent = oRoot;
		while (pathTokenizer.hasMoreArgs()) {
			String name = pathTokenizer.nextArg();
			TsfNode node = parent.getChildBySrcName(name);
			if (node == null) {
				node = new TsfNode();
				node.srcName = name;
				node.type = TsfNodeType.raw;
				parent.addChild(node);
			}

			if (isArrChildName(name)) {
				parent.type = TsfNodeType.arr;
			} else {
				parent.type = TsfNodeType.obj;
			}

			parent = node;
		}
	}

}
