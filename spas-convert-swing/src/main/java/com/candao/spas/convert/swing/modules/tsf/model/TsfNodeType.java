package com.candao.spas.convert.swing.modules.tsf.model;


import com.candao.spas.convert.swing.base.constants.Icons;

/**
 * 协议节点值类型
 *
 */
public enum TsfNodeType {

	raw("· 原始", Icons.raw),
	obj("{} 对象", Icons.obj),
	arr("[] 数组", Icons.arr);

	public String cnName;
	public String icon;

	private TsfNodeType(String cnName, String icon) {
		this.cnName = cnName;
		this.icon = icon;
	}

	@Override
	public String toString() {
		return cnName;
	}

}