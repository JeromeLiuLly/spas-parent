package com.candao.spas.convert.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtil {

	/**
	 * 文本文件默认编码，默认为utf-8
	 */
	public static String defaultCharset = "utf-8";

	private static final Pattern BLANK_LINE_PATTERN = Pattern.compile("^\\s*$");

	/**
	 * 用默认编码{@link #defaultCharset}读取文本文件
	 */
	public static String readTextFile(File file) throws IOException {
		InputStream stream = new FileInputStream(file);
		int size = stream.available();
		byte[] buf = new byte[size];
		stream.read(buf);
		stream.close();
		return new String(buf, defaultCharset);
	}

	/**
	 * 用默认编码{@link #defaultCharset}读取文本文件，返回行列表
	 */
	public static List<String> readTextFileLines(File file, boolean ignoreBlankLine) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader streamReader = new InputStreamReader(stream, defaultCharset);
		BufferedReader reader = new BufferedReader(streamReader);
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			if (ignoreBlankLine && BLANK_LINE_PATTERN.matcher(line).matches()) {
				continue;
			}
			lines.add(line);
		}
		stream.close();
		return lines;
	}

	/**
	 * 用默认编码{@link #defaultCharset}写文本文件
	 */
	public static void writeTextFile(File file, String content) throws IOException {
		if (!file.isFile()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		OutputStream stream = new FileOutputStream(file);
		stream.write(content.getBytes(defaultCharset));
		stream.close();
	}

	/**
	 * 读json文件
	 */
	public static JsonElement readJson(File file) throws Exception {
		String text = readTextFile(file);
		if (text.isEmpty()) {
			return null;
		}
		return new JsonParser().parse(text);
	}

}