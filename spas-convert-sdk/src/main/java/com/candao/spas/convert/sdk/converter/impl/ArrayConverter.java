package com.candao.spas.convert.sdk.converter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.candao.spas.convert.sdk.converter.ArrayElementTransform;
import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * json数组转换器
 */
public class ArrayConverter extends Converter {

	private List<ArrayElementTransform> elementTransforms;

	public ArrayConverter() {}

	public ArrayConverter(JsonObject cfg) throws Exception {
		parseArg(cfg);
	}

	@Override
	public void parseArg(JsonElement arg) throws Exception {
		JsonObject cfg = (JsonObject) arg;
		elementTransforms = new ArrayList<ArrayElementTransform>();
		for (Entry<String, JsonElement> entry : cfg.entrySet()) {
			elementTransforms.add(new ArrayElementTransform(entry.getKey(), entry.getValue()));
		}
	}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		JsonArray srcArr = (JsonArray) srcValue;
		JsonArray dstArr = new JsonArray();

		int srcIndex = 0;
		for (JsonElement e : srcArr) {
			for (ArrayElementTransform t : elementTransforms) {
				if (t.matchSrcIndex(srcIndex)) {
					t.transform(dstArr, srcIndex, e);
					break;
				}
			}
			srcIndex++;
		}

		return dstArr;
	}

}