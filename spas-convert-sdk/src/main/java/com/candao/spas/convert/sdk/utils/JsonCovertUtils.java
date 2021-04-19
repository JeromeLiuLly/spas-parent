package com.candao.spas.convert.sdk.utils;

import com.candao.spas.convert.sdk.node.Jsont;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    public static void main(String[] args) throws Exception {
        String out = readText("/Users/jeromeliu/Downloads/json-c/candao-jsont/src/test/resources/out.json");;
        String in = readText("/Users/jeromeliu/Downloads/json-c/candao-jsont/src/test/resources/in.json");;
        String spec = readText("/Users/jeromeliu/Downloads/json-c/candao-jsont/src/test/resources/json_convert.json");
        Map result = JsonCovertUtils.convert(in,spec);
        System.out.println("输入:\n"+in);
        System.out.println("期望:\n"+out);
        System.out.println("转换:\n"+spec);
        System.out.println("输出:\n"+new Gson().toJson(result));
    }

    public static String readText(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()){
            throw new Exception("文件不存在");
        }
        InputStream stream = new FileInputStream(file);
        int size = stream.available();
        byte[] buf = new byte[size];
        stream.read(buf);
        return new String(buf, "utf-8");
    }
}