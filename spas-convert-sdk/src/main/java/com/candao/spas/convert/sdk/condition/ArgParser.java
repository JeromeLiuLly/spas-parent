package com.candao.spas.convert.sdk.condition;

import java.util.NoSuchElementException;

public class ArgParser {

	private String str;
	private String sep;
	private int curPos;
	private int endPos;

	public ArgParser(String str, String sep) {
		if (sep.isEmpty()) {
			throw new IllegalArgumentException("sep不能为空");
		}
		this.str = str;
		this.sep = sep;
		curPos = 0;
		endPos = str.length();
	}

	public boolean hasMoreArgs() {
		return curPos <= endPos;
	}

	/**
	 * @throws NoSuchElementException - if there are no more args
	 */
	public String nextArg() {
		if (!hasMoreArgs()) {
			throw new NoSuchElementException();
		}
		int sepPos = str.indexOf(sep, curPos);
		if (sepPos == -1) {
			sepPos = endPos;
		}
		String arg = str.substring(curPos, sepPos);
		curPos = sepPos + sep.length();
		return arg;
	}
}