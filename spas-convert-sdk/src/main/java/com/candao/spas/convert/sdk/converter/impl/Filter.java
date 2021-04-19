package com.candao.spas.convert.sdk.converter.impl;

import com.candao.spas.convert.sdk.converter.Condition;
import com.candao.spas.convert.sdk.converter.Converter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Filter extends Converter {

		public Condition condition;

		@Override
		public void parseArg(JsonElement arg) throws Exception {
			condition = Condition.create(arg);
		}

		@Override
		public JsonElement convert(JsonElement srcValue) {
			JsonArray srcArr = (JsonArray) srcValue;
			JsonArray dstArr = null;

			for (JsonElement element : srcArr) {
				if (condition.accept(element)) {
					if (dstArr == null) {
						dstArr = new JsonArray();
					}
					dstArr.add(element.deepCopy());
				}
			}

			return dstArr;
		}

	}