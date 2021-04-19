package com.candao.spas.convert.swing.modules.tsf.model;

/**
 * 协议节点值转换器类型
 *
 */
public enum TsfCvtType {

	numToDateStr("日期数字转字符串"),
	dateStrToNum("日期字符串转数字"),
	constant("常量"),
	copy("复制");

	public String cnName;

	private TsfCvtType(String cnName) {
		this.cnName = cnName;
	}

	@Override
	public String toString() {
		return cnName;
	}

}
