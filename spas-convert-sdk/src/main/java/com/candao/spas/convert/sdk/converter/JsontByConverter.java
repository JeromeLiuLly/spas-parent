package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.candao.spas.convert.sdk.converter.impl.ObjectConverter;

/**
 * json数据结构转换
 *
 */
public class JsontByConverter {

	private ObjectConverter converter;

	public void loadCfg(String text) throws Exception {
		loadCfg(new JsonParser().parse(text).getAsJsonObject());
	}

	public void loadCfg(JsonObject cfg) throws Exception {
		converter = new ObjectConverter(cfg);
	}

	public JsonObject convert(JsonObject src) {
		return (JsonObject) converter.convert(src);
	}

}
