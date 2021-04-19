package com.candao.spas.convert.sdk.converter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.candao.spas.convert.sdk.converter.Converter;
import com.candao.spas.convert.sdk.converter.FieldTransform;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * json对象转换器
 *
 */
public class ObjectConverter extends Converter {

	private List<FieldTransform> fieldTransforms;

	public ObjectConverter() {}

	public ObjectConverter(JsonObject cfg) throws Exception {
		parseArg(cfg);
	}

	@Override
	public void parseArg(JsonElement arg) throws Exception {
		JsonObject cfg = (JsonObject) arg;
		fieldTransforms = new ArrayList<FieldTransform>();
		for (Entry<String, JsonElement> entry : cfg.entrySet()) {
			fieldTransforms.add(new FieldTransform(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		JsonObject srcObj = (JsonObject) srcValue;
		JsonObject dstObj = new JsonObject();

		for (Entry<String, JsonElement> e : srcObj.entrySet()) {
			String srcKey = e.getKey();
			for (FieldTransform t : fieldTransforms) {
				if (t.matchSrcKey(srcKey)) {
					t.transform(dstObj, srcKey, e.getValue());
					break;
				}
			}
		}

		return dstObj;
	}

}