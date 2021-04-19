package com.candao.spas.convert.sdk.condition.impl;

import com.candao.spas.convert.sdk.condition.Cvt;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Constant extends Cvt {

	public JsonPrimitive value;

	@Override
	public void parseArg(String arg) {
		value = new JsonPrimitive(arg);
	}

	@Override
	public JsonElement convert(JsonElement e) {
		return value;
	}
}