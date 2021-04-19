package com.candao.spas.convert.sdk.converter;

import com.google.gson.JsonElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * json转换上下文
 */
@Data
@Setter
@Getter
public class ConvertContext {

	private JsonElement dstParent;
	private JsonElement srcValue;

}