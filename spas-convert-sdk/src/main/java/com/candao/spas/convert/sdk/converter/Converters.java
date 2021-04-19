package com.candao.spas.convert.sdk.converter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.candao.spas.convert.sdk.converter.impl.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 转换器汇总
 *
 */
class Converters {

	public static Map<String, Class<? extends Converter>> converterClassMap = new HashMap<String, Class<? extends Converter>>();
	static {
		converterClassMap.put("obj", ObjectConverter.class);
		converterClassMap.put("arr", ArrayConverter.class);

		converterClassMap.put("numToDateStr", NumToDateStr.class);
		converterClassMap.put("kvListToObj", KvListToObj.class);
		converterClassMap.put("ungroup", Ungroup.class);
		converterClassMap.put("filter", Filter.class);

		converterClassMap.put("dispatchArr", DispatchArr.class);
	}

	public static Converter create(JsonElement cfg) throws Exception {
		if (cfg.isJsonArray()) {
			JsonArray childCfgs = (JsonArray) cfg;
			List<Converter> converters = new ArrayList<Converter>(childCfgs.size());
			for (JsonElement c : childCfgs) {
				converters.add(create(c));
			}
			return new ConverterChain(converters);
		}

		if (cfg.isJsonPrimitive()) {
			return create(cfg.getAsString(), null);
		}

		JsonObject cfgObj = cfg.getAsJsonObject();
		for (Entry<String, JsonElement> e : cfgObj.entrySet()) {
			// 直接return，不要奇怪，这里只会有一个属性
			return create(e.getKey(), e.getValue());
		}

		throw new Exception("无法解析转换器配置：" + cfg);
	}

	private static Converter create(String name, JsonElement arg) throws Exception {
		Class<? extends Converter> converterClass = converterClassMap.get(name);
		Converter converter = converterClass.newInstance();
		converter.parseArg(arg);
		return converter;
	}

}