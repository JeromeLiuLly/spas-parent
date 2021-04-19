package com.candao.spas.convert.web;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
/**
 * 基本请求消息
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequestData<T> extends AbstractRequestData<T> implements Serializable {
    /**
     * 语言类型 ：0=简体；1=繁体，2=英语
     */
    private Integer langType;
    /**
     * 时间戳,精确到秒
     */
    private Long timestamp;
    /**
     * 浏览器信息(如:Firefox 65.0)
     */
    private String browser;
    /**
     * 操作系统(如:Windows 7)
     */
    private String os;
    /**
     * token
     */
    private String token;
    /**
     * 客户端ip
     */
    private String ip;
}