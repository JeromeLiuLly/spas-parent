package com.candao.spas.convert.sdk.converter;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * json值转换器链
 *
 */
class ConverterChain extends Converter {

	public List<Converter> converters;

	public ConverterChain(List<Converter> converters) {
		this.converters = converters;
	}

	@Override
	public void parseArg(JsonElement arg) throws Exception {}

	@Override
	public JsonElement convert(JsonElement srcValue) {
		JsonElement dstValue = srcValue;
		for (Converter converter : converters) {
			dstValue = converter.convert(dstValue);
		}
		return dstValue;
	}

	@Override
	public boolean isDispatcher() {
		for (Converter converter : converters) {
			if (converter.isDispatcher()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void dispatch(JsonObject dstParent, JsonElement srcValue) {
		for (Converter converter : converters) {
			if (converter.isDispatcher()) {
				converter.dispatch(dstParent, srcValue);
			}
		}
	}

}