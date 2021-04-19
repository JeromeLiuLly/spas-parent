package com.candao.spas.convert.sdk.condition.impl;

import com.candao.spas.convert.sdk.condition.Cvt;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Getter
@Setter
public class NumToDateStr extends Cvt {

	private SimpleDateFormat dateFormat;

	@Override
	public void parseArg(String arg) {
		dateFormat = new SimpleDateFormat(arg);
	}

	@Override
	public JsonElement convert(JsonElement e) {
		return new JsonPrimitive(dateFormat.format(new Date(e.getAsLong())));
	}


}