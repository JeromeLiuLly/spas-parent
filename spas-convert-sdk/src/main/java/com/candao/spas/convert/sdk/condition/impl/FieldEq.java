package com.candao.spas.convert.sdk.condition.impl;

import com.candao.spas.convert.sdk.condition.Cond;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FieldEq extends Cond {

	@Override
	public boolean accept(JsonElement src) {
		return getCompareValue().equals(((JsonObject) src).get(getFieldName()).getAsString());
	}

}