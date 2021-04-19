package com.candao.spas.convert.common.utils;

public class JsontUtil {

	public static boolean isArrElementName(String name) {
		return name.startsWith("*") || name.endsWith("*") || name.indexOf("*") > 0;
	}

}