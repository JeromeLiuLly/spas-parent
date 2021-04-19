package com.candao.spas.convert.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
	
	/**
	 * 1 操作成功
	 */
	SUCCESS(1, "操作成功"),
	
	/**
	 * 2 操作失败
	 */
	FAIL(2, "操作失败"),
	
	/**
	 * 3 参数错误
	 */
	PARAM_ERR(3, "参数错误"),
	
	/**
	 * 4 token失效
	 */
	TOKEN_ERROR(4, "您的账号已失效，请重新登陆"),

	/**
	 * 5 job key失效
	 */
	JOB_KEY_ERROR(5, "调度任务jobReqValidationKey有误"),

	/**
	 * 4 token失效
	 */
	ACCESS_ERROR(6, "数据越权"),
	
	/**
	 * 登出成功
	 */
	LOGOUT(100, "登出成功"),
	
	/**
	 * 300 连接超时
	 */
	TIME_OUT(300, "连接超时"),
	/**
	 * 404 资源不存在
	 */
	RESOURCE_404(404, "资源不存在"),
	
	/**
	 * 404 资源不存在
	 */
	RESOURCE_429(429, "访问过快，请稍候再试")
	;
	private final int status;
	private final String msg;
	public static ResponseStatus getRspByStatus(int status) {
		for (ResponseStatus rsp : values()) {
			if (rsp.status == status) {
				return rsp;
			}
		}
		return ResponseStatus.FAIL;
	}
}