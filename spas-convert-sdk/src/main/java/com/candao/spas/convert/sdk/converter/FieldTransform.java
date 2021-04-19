package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * json对象字段传送与转换器
 */
public class FieldTransform {

	public static final String ANY_KEY = "*";

	public String dstKey;
	public String srcKey;
	public Converter converter;

	public FieldTransform(String keyCfg, JsonElement cvtCfg) throws Exception {
		if (keyCfg.contains("=")) {
			String[] keys = keyCfg.split("=");
			dstKey = keys[0];
			srcKey = keys[1];
		} else {
			dstKey = null;
			srcKey = keyCfg;
		}

		if (srcKey.equals(ANY_KEY)) {
			srcKey = null;
		}

		if (!(cvtCfg.isJsonPrimitive() && cvtCfg.getAsString().isEmpty())) {
			converter = Converters.create(cvtCfg);
		}
	}

	public boolean matchSrcKey(String key) {
		return srcKey == null || key.equals(srcKey);
	}

	public String getDstKey(String srcKey) {
		return dstKey == null ? srcKey : dstKey;
	}

	public void transform(JsonObject dstObj, String srcKey, JsonElement srcValue) {
		JsonElement dstValue = convert(srcValue);
		if (dstValue != null) {
			if (converter != null && converter.isDispatcher()) {
				converter.dispatch(dstObj, dstValue);
			} else {
				String dstKey = getDstKey(srcKey);
				dstObj.add(dstKey, dstValue);
			}
		}
	}

	public JsonElement convert(JsonElement fieldValue) {
		if (converter == null) {
			return fieldValue.deepCopy();
		} else {
			return converter.convert(fieldValue);
		}
	}

}
