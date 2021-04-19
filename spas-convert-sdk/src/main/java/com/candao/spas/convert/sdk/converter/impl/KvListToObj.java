package com.candao.spas.convert.sdk.converter.impl;

import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将(key,value)数组转成对象，源数组的一个元素 对应 输出对象的一个字段。<br>
 * 可以额外指定 源数组元素key值 与 输出对象字段名 之间的映射关系，如果不指定映射关系，则表示字段名与key值相等。
 *
 * <pre>
 如果指定key映射，则配置为：
 {
	 "kvListToObj": {
		"kv": ["源数组元素key字段名", "源数组元素value字段名"],
		"keyMap": [
			["输出对象字段名1", "源数组元素key字段值1"],
			["输出对象字段名2", "源数组元素key字段值2"]
		]
	 }
 }

 如果不指定key映射，则配置为：
 { "kvListToObj": ["源数组元素key字段名", "源数组元素value字段名"] }
 * </pre>
 */
public class KvListToObj extends Converter {

	public String kFieldName;
	public String vFieldName;
	public Map<String, String> keyMap;

	@Override
	public void parseArg(JsonElement arg) {
		if (arg.isJsonArray()) {
			parseKvName(arg.getAsJsonArray());
		} else {
			JsonObject cfg = arg.getAsJsonObject();

			JsonElement kvCfg = cfg.get("kv");
			parseKvName(kvCfg.getAsJsonArray());

			JsonElement keyMapCfg = cfg.get("keyMap");
			if (keyMapCfg != null) {
				parseKeyMap(keyMapCfg.getAsJsonArray());
			}
		}
	}

	private void parseKvName(JsonArray args) {
		kFieldName = args.get(0).getAsString();
		vFieldName = args.get(1).getAsString();
	}

	private void parseKeyMap(JsonArray args) {
		keyMap = new LinkedHashMap<String, String>();
		for (JsonElement line : args) {
			JsonArray pair = line.getAsJsonArray();
			String dstKey = pair.get(0).getAsString();
			String srcKey = pair.get(1).getAsString();
			keyMap.put(srcKey, dstKey);
		}
	}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		JsonArray srcArr = (JsonArray) srcValue;
		JsonObject dstObj = new JsonObject();

		for (JsonElement element : srcArr) {
			JsonObject kv = (JsonObject) element;
			String k = kv.get(kFieldName).getAsString();
			String dstKey = keyMap == null ? k : keyMap.get(k);
			if (dstKey != null) {
				JsonElement v = kv.get(vFieldName);
				dstObj.add(dstKey, v.deepCopy());
			}
		}

		return dstObj;
	}

}