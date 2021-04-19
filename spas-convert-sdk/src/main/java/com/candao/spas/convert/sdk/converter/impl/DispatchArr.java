package com.candao.spas.convert.sdk.converter.impl;

import com.candao.spas.convert.sdk.converter.Condition;
import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DispatchArr extends Converter {

	public Map<String, Condition> conditionMap;

	public void parseArg(JsonElement arg) throws Exception {
		JsonObject cfg = (JsonObject) arg;
		conditionMap = new LinkedHashMap<String, Condition>();
		for (Map.Entry<String, JsonElement> e : cfg.entrySet()) {
			conditionMap.put(e.getKey(), Condition.create(e.getValue()));
		}
	}

	@Override
	public boolean isDispatcher() {
		return true;
	}

	@Override
	public void dispatch(JsonObject dstParent, JsonElement srcValue) {
		JsonArray srcArr = (JsonArray) srcValue;
		Map<String, JsonArray> dstArrMap = new HashMap<String, JsonArray>();

		for (JsonElement element : srcArr) {
			for (Map.Entry<String, Condition> e : conditionMap.entrySet()) {
				if (e.getValue().accept(element)) {
					String key = e.getKey();
					JsonArray dstArr = dstArrMap.get(key);
					if (dstArr == null) {
						dstArr = new JsonArray();
						dstArrMap.put(key, dstArr);
					}
					dstArr.add(element.deepCopy());
					break;
				}
			}
		}

		for (Map.Entry<String, Condition> e : conditionMap.entrySet()) {
			String key = e.getKey();
			JsonArray dstArr = dstArrMap.get(key);
			if (dstArr != null) {
				dstParent.add(key, dstArr);
			}
		}
	}

}