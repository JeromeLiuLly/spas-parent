package com.candao.spas.convert.sdk.condition.impl;

import com.candao.spas.convert.sdk.condition.Cvt;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Copy extends Cvt {

	private String arg;

	@Override
	public void parseArg(String arg) {
		this.arg = arg;
	}

	@Override
	public JsonElement convert(JsonElement e) {
		return e.deepCopy();
	}
}