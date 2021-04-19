package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 * json数组元素传送与转换器

 */
public class ArrayElementTransform extends FieldTransform {

	public int dstIndex;
	public int srcIndex;

	public ArrayElementTransform(String keyCfg, JsonElement cvtCfg) throws Exception {
		super(keyCfg, cvtCfg);

		dstIndex = convertIndex(dstKey);
		srcIndex = convertIndex(srcKey);
	}

	private static int convertIndex(String key) {
		return key == null ? -1 : Integer.parseInt(key);
	}

	public boolean matchSrcIndex(int index) {
		return index == srcIndex || srcIndex < 0;
	}

	public int getDstIndex(int srcIndex) {
		return dstIndex < 0 ? srcIndex : dstIndex;
	}

	public void transform(JsonArray dstArr, int srcIndex, JsonElement srcValue) {
		JsonElement dstValue = convert(srcValue);
		if (dstValue != null) {
			int dstIndex = getDstIndex(srcIndex);
			safeSet(dstArr, dstIndex, dstValue);
		}
	}

	private void safeSet(JsonArray arr, int index, JsonElement element) {
		while (arr.size() < index) {
			arr.add(JsonNull.INSTANCE);
		}
		if (arr.size() == index) {
			arr.add(element);
		} else {
			arr.set(index, element);
		}
	}

}
