package com.candao.spas.convert.sdk.utils;

import com.candao.spas.convert.sdk.node.Jsont;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public final class JsonCovertUtils {

    private volatile static JsonCovertUtils instance = null;

    private JsonCovertUtils() {
        this.transform = new Jsont();
        this.parser = new JsonParser();
    }

    final Jsont transform;
    final JsonParser parser;

    // java中使用双重检查锁定机制
    public static JsonCovertUtils getInstance() {
        if (instance == null) {
            synchronized (JsonCovertUtils.class) {
                if (instance == null) {
                    instance = new JsonCovertUtils();
                }
            }
        }
        return instance;
    }

    private JsonObject parse(String data){
        return parser.parse(data).getAsJsonObject();
    }

    /**
     * @param sourceJsonData  转换json对象
     * @param convertProtocol 转换协议
     *
     * @return String json数据
     * */
    public static String convert(String sourceJsonData, String convertProtocol){
        if (sourceJsonData == null || convertProtocol == null)
            return null;

        try {
            JsonObject inObj = getInstance().parse(sourceJsonData).getAsJsonObject();
            JsonObject specObj = getInstance().parse(convertProtocol).getAsJsonObject();
            getInstance().transform.loadCfg(specObj);
            JsonElement result = getInstance().transform.convert(inObj);
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}