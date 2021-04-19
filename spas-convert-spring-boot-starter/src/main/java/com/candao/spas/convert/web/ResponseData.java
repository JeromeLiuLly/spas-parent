package com.candao.spas.convert.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
public class ResponseData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2486046848414220167L;
	/**
	 * 响应状态
	 */
	@JsonProperty(index = 1)
	private Integer status;
	/**
	 * 响应状态描述
	 */
	@JsonProperty(index = 2)
	private String msg;
	/**
	 * 日志id
	 */
	@JsonProperty(index = 3)
	private String logId;
	/**
	 * 服务器当前时间
	 */
	@JsonProperty(index = 4)
	private Long serverTime;
	/**
	 * 响应具体数据
	 */
	@JsonProperty(index = 100)
	private Object data;
	public ResponseData() {
		this.serverTime = System.currentTimeMillis();
	}
	/**
	 * 设置响应状态（使用默认描述）
	 *
	 * @param responseStatus-{link RspStatus}
	 */
	public void setResponseStatus(ResponseStatus responseStatus) {
		status = responseStatus.getStatus();
		msg = responseStatus.getMsg();
	}
	/**
	 * 设置响应状态（使用自定义描述）
	 *
	 * @param responseStatus-{link RspStatus}
	 * @param msg-自定义描述
	 */
	public void setResponseStatus(ResponseStatus responseStatus, String msg) {
		status = responseStatus.getStatus();
		this.msg = msg;
	}
	// **********************************http常用数据返回方法***************************************
	public static ResponseData generateSuccess(Object data) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.SUCCESS);
		responseData.data = data;
		return responseData;
	}
	/**
	 * 返回一个无具体数据的成功的RspData<br/>
	 *
	 * @return
	 */
	public static ResponseData generateSuccess() {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.SUCCESS);
		return responseData;
	}
	/**
	 * 返回一个无具体数据的成功的RspData<br/>
	 *
	 * @return
	 */
	public static ResponseData generateSuccess(String msg) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.SUCCESS, msg);
		return responseData;
	}
	/**
	 * 返回一个失败的RspData<br/>
	 * 默认为RspStatus.FAIL失败类型
	 *
	 * @param msg-自定义失败信息
	 * @return
	 */
	public static ResponseData generateFail(String msg) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.FAIL, msg);
		return responseData;
	}
	/**
	 * 返回一个失败的RspData<br/>
	 *
	 * @param status-自定义状态
	 * @param msg-自定义信息
	 * @return
	 */
	public static ResponseData generateFail(ResponseStatus status, String msg) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(status, msg);
		return responseData;
	}
	/**
	 * 返回一个失败的RspData<br/>
	 *
	 * @param status-自定义状态
	 * @param msg-自定义信息
	 * @return
	 */
	public static ResponseData generateFail(int status, String msg) {
		ResponseData responseData = new ResponseData();
		responseData.status = status;
		responseData.msg = msg;
		return responseData;
	}
	/**
	 * 返回一个参数错误的RspData<br/>
	 *
	 * @param paramName 错误的参数名
	 * @return
	 */
	public static ResponseData generateParamFail(String paramName) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.PARAM_ERR);
		responseData.msg += "，参数名：" + paramName;
		return responseData;
	}

	/**
	 * 返回一个失败的RspData<br/>
	 *
	 * @param responseStatus-{link RspStatus}
	 * @return
	 */
	public static ResponseData generateTokenFail(ResponseStatus responseStatus) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(responseStatus);
		return responseData;
	}

	/**
	 * 返回一个失败的RspData<br/>
	 *
	 * @param responseStatus-{link RspStatus}
	 * @return
	 */
	public static ResponseData generateFail(ResponseStatus responseStatus) {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(responseStatus);
		return responseData;
	}
	/**
	 * 返回一个自定义的RspData
	 *
	 * @param status-响应状态
	 * @param msg-响应信息
	 * @param data-具体响应数据
	 * @return
	 */
	public static ResponseData generate(int status, String msg, Object data) {
		ResponseData responseData = new ResponseData();
		responseData.status = status;
		responseData.msg = msg;
		responseData.data = data;
		return responseData;
	}
	/**
	 * 返回一个失败的RspData<br/>
	 *
	 * @return
	 */
	public static ResponseData generateFailException() {
		ResponseData responseData = new ResponseData();
		responseData.setResponseStatus(ResponseStatus.FAIL);
		responseData.setMsg("【logId=】服务器异常，请将logId反馈给技术人员");
		return responseData;
	}

	/**
	 * 用于简单构建返回对象
	 */
	public static class SimpleResponseData extends LinkedHashMap<String, Object> implements Serializable {
		public static SimpleResponseData newInstance() {
			return new SimpleResponseData();
		}
		public static SimpleResponseData newInstance(String key, Object value) {
			SimpleResponseData simpleResponseData = newInstance();
			simpleResponseData.put(key, value);
			return simpleResponseData;
		}
		public SimpleResponseData result(String key, Object value) {
			this.put(key, value);
			return this;
		}
	}
}