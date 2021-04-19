package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.Map;

/**
 * 匹配条件，用于筛选和分发
 */
public abstract class Condition {

	public static Map<String, Class<? extends Condition>> conditionClassMap = new HashMap<String, Class<? extends Condition>>();
	static {
		conditionClassMap.put("fieldEq", FieldEq.class);
	}

	public static Condition create(JsonElement cfg) throws Exception {
		JsonArray args = cfg.getAsJsonArray();
		String name = args.get(0).getAsString();
		Class<? extends Condition> conditionClass = conditionClassMap.get(name);
		Condition condition = conditionClass.newInstance();
		condition.parseArgs(args);
		return condition;
	}

	// *************************************************************************

	public abstract void parseArgs(JsonArray args);

	public abstract boolean accept(JsonElement element);

	public static class FieldEq extends Condition {

		public String fieldName;
		public String compareValue;

		@Override
		public void parseArgs(JsonArray args) {
			fieldName = args.get(1).getAsString();
			compareValue = args.get(2).getAsString();
		}

		@Override
		public boolean accept(JsonElement element) {
			JsonObject obj = (JsonObject) element;
			JsonPrimitive fieldValue = obj.getAsJsonPrimitive(fieldName);
			return compareValue.equals(fieldValue.getAsString());
		}
	}

}
