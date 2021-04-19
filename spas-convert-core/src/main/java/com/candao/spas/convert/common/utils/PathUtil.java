package com.candao.spas.convert.common.utils;

import java.io.File;
import java.io.IOException;

public class PathUtil {

	/**
	 * 获取绝对唯一路径
	 */
	public static String getCanonicalPath(File file) throws IOException {
		return file.getCanonicalPath();
	}

}