package com.candao.spas.convert.sdk.condition.impl;

import com.candao.spas.convert.sdk.condition.Cvt;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@Data
public class DateStrToNum extends Cvt {

	private SimpleDateFormat dateFormat;

	@Override
	public void parseArg(String arg) {
		dateFormat = new SimpleDateFormat(arg);
	}

	@Override
	public JsonElement convert(JsonElement e) throws Exception {
		Date date = dateFormat.parse(e.getAsString());
		return new JsonPrimitive(date.getTime());
	}
}