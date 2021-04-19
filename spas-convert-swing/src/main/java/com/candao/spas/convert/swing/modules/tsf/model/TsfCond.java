package com.candao.spas.convert.swing.modules.tsf.model;

/**
 * 协议节点源条件
 *
 */
public class TsfCond {

	public boolean isMulti;
	public String op;
	public String fieldName;
	public String compareValue;

	public TsfCond(boolean isMulti, String def) {
		String[] args = def.split(" ");
		this.isMulti = isMulti;
		fieldName = args[0];
		op = args[1];
		compareValue = args[2];
	}

	public void toDefArgs(StringBuilder sb) {
		sb.append(isMulti ? "1" : "0")
			.append(",")
			.append(toEditString());
	}

	public String toEditString() {
		return fieldName + " " + op + " " + compareValue;
	}

	public String toViewString() {
		return "(" + (isMulti ? "多选" : "单选") + toEditString() + ")";
	}

	@Override
	public String toString() {
		return toViewString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TsfCond)) {
			return false;
		}
		TsfCond o = (TsfCond) obj;
		return isMulti == o.isMulti && op.equals(o.op) && fieldName.equals(o.fieldName) && compareValue.equals(o.compareValue);
	}

}