package com.candao.spas.convert.sdk.utils;

import com.candao.spas.convert.sdk.node.Jsont;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;

public final class JsonCovertUtils {

    private volatile static JsonCovertUtils instance = null;

    private JsonCovertUtils() { this.transform = new Jsont(); }

    final Jsont transform;

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

    /**
     * @param sourceJsonData  转换json对象
     * @param convertProtocol 转换协议
     * */
    public static Map convert(String sourceJsonData, String convertProtocol){
        if (sourceJsonData == null || convertProtocol == null)
            return null;

        try {
            JsonObject inObj = new JsonParser().parse(sourceJsonData).getAsJsonObject();
            JsonObject specObj = new JsonParser().parse(convertProtocol).getAsJsonObject();
            getInstance().transform.loadCfg(specObj);
            JsonElement result = getInstance().transform.convert(inObj);
            return new Gson().fromJson(result,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}