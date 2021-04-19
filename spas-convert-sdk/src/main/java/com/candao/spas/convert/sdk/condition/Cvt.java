package com.candao.spas.convert.sdk.condition;

import com.candao.spas.convert.sdk.condition.impl.Constant;
import com.candao.spas.convert.sdk.condition.impl.Copy;
import com.candao.spas.convert.sdk.condition.impl.DateStrToNum;
import com.candao.spas.convert.sdk.condition.impl.NumToDateStr;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public abstract class Cvt {

	public static final Map<String, Class<? extends Cvt>> cvtClassMap = new HashMap<String, Class<? extends Cvt>>();

	static {
		cvtClassMap.put("numToDateStr", NumToDateStr.class);
		cvtClassMap.put("dateStrToNum", DateStrToNum.class);
		cvtClassMap.put("constant", Constant.class);
		cvtClassMap.put("copy", Copy.class);
	}

	public static Cvt create(String name, String arg) throws Exception {
		Class<? extends Cvt> cvtClass = cvtClassMap.get(name);
		Cvt cvt = cvtClass.newInstance();
		cvt.parseArg(arg);
		return cvt;
	}

	// *************************************************************************

	public abstract void parseArg(String arg);

	public abstract JsonElement convert(JsonElement src) throws Exception;
}
