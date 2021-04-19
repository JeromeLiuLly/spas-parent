package com.candao.spas.convert.swing.modules.tsf.model;

import com.candao.spas.convert.sdk.condition.ArgParser;

/**
 * 协议节点值转换器
 *
 */
public class TsfCvt {

	public TsfCvtType type;
	public String arg;

	public TsfCvt(ArgParser args) {
		type = TsfCvtType.valueOf(args.nextArg());
		arg = args.nextArg();
	}

	public TsfCvt(TsfCvtType type, String arg) {
		this.type = type;
		this.arg = arg;
	}

	public void toDefArgs(StringBuilder sb) {
		sb.append(type.name())
			.append(",")
			.append(arg);
	}

	public String toViewString() {
		return "{" + type.cnName + ": " + arg + "}";
	}

	@Override
	public String toString() {
		return toViewString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TsfCvt)) {
			return false;
		}
		TsfCvt o = (TsfCvt) obj;
		return type == o.type && arg.equals(o.arg);
	}

}
