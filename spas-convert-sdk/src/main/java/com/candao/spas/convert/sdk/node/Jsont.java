package com.candao.spas.convert.sdk.node;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.candao.spas.convert.sdk.context.Context;

/**
 * json转换规则
 */
public class Jsont {

	public SrcNode srcRoot;
	public DstNode dstRoot;

	public JsonElement convert(JsonElement src) throws Exception {
		Context context = new Context();
		srcRoot.convert(src, context);
		return context.getRoot();
	}

	public void loadCfg(JsonObject cfg) throws Exception {
		srcRoot = new SrcNode();
		dstRoot = new DstNode();

		srcRoot.parseChildren(cfg, dstRoot);
		if (srcRoot.getChildren() == null) {
			throw new Exception("invalid cfg: " + cfg);
		}
	}

}
