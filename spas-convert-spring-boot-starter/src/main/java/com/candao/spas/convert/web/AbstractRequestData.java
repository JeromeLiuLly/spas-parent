package com.candao.spas.convert.web;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractRequestData<T> implements Serializable {
	 /**
     * 请求业务具体内容-json串对象
     */
    protected T data;
}