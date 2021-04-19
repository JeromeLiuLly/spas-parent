package com.candao.spas.convert.common.utils;

public class ObjectUtil {

	public static boolean equal(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}
}