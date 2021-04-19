package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * json值转换器

 */
public abstract class Converter {

	public abstract void parseArg(JsonElement arg) throws Exception;

	public JsonElement convert(JsonElement srcValue) {
		return srcValue;
	}

	public boolean isDispatcher() {
		return false;
	}

	public void dispatch(JsonObject dstParent, JsonElement srcValue) {}

}