package com.candao.spas.convert.sdk.converter.impl;

import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class Ungroup extends Converter {

	public String innerArrFieldName;

	@Override
	public void parseArg(JsonElement arg) {
		innerArrFieldName = arg.getAsString();
	}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		JsonArray dstArr = new JsonArray();

		JsonArray groups = (JsonArray) srcValue;
		for (JsonElement group : groups) {
			JsonObject groupObj = (JsonObject) group;
			JsonArray innerArr = (JsonArray) groupObj.get(innerArrFieldName);
			for (JsonElement element : innerArr) {
				JsonObject dstElement = new JsonObject();

				for (Map.Entry<String, JsonElement> groupField : groupObj.entrySet()) {
					if (!groupField.getKey().equals(innerArrFieldName)) {
						dstElement.add(groupField.getKey(), groupField.getValue().deepCopy());
					}
				}

				if (element instanceof JsonObject) {
					for (Map.Entry<String, JsonElement> elementField : ((JsonObject) element).entrySet()) {
						dstElement.add(elementField.getKey(), elementField.getValue().deepCopy());
					}
				} else {
					dstElement.add(innerArrFieldName, element.deepCopy());
				}

				dstArr.add(dstElement);
			}
		}

		return dstArr;
	}

}