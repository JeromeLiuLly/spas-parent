package com.candao.spas.convert.sdk.converter.impl;

import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将数字转成时间字符串
 *
 * <pre>
 参数为SimpleDateFormat格式，示例：{ "numToDateStr": "yyyy-MM-dd HH:mm:ss" }
 如果不指定参数，则默认使用"yyyy-MM-dd HH:mm:ss"，配置为："numToDateStr"
 * </pre>
 */
public class NumToDateStr extends Converter {

	private static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SimpleDateFormat dateFormat;

	@Override
	public void parseArg(JsonElement arg) {
		if (arg == null) {
			dateFormat = defaultDateFormat;
		} else {
			dateFormat = new SimpleDateFormat(arg.getAsString());
		}
	}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		long time = ((JsonPrimitive) srcValue).getAsLong();
		String str = dateFormat.format(new Date(time));
		return new JsonPrimitive(str);
	}

}