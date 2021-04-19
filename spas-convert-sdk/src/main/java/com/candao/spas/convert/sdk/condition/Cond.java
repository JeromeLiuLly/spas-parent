package com.candao.spas.convert.sdk.condition;

import com.candao.spas.convert.sdk.condition.impl.FieldEq;
import com.google.gson.JsonElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Data
@ToString
public abstract class Cond {

	private boolean isMulti;
	private String op;
	private String fieldName;
	private String compareValue;

	public abstract boolean accept(JsonElement src);

	public static final Map<String, Class<? extends Cond>> condClassMap = new HashMap<String, Class<? extends Cond>>();
	static {
		condClassMap.put("=", FieldEq.class);
	}

	public static Cond create(boolean isMulti, String def) throws Exception {
		String[] args = def.split(" ");
		String op = args[1];
		Class<? extends Cond> condClass = condClassMap.get(op);
		Cond cond = condClass.newInstance();
		cond.isMulti = isMulti;
		cond.fieldName = args[0];
		cond.op = op;
		cond.compareValue = args[2];
		return cond;
	}

}