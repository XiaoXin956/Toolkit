package com.xiaoxin.pay.alipay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ObjectUtils {

    /**
     * 字符转map
     *
     * @param mapJson
     * @return
     */
    public static Map<String, Object> jsonToMap(String mapJson) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map2 = gson.fromJson(mapJson, type);
        return map2;
    }


    /**
     * MD5加密
     * @param data
     * @return
     */
    public static String MD5(String data) {
        StringBuilder sb=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 数据签名
     * @param data
     * @param key
     * @return
     */
    public static String generateSignature(final Map<String, String> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if ("".equals(k)||null==data.get(k)) {
                continue;
            }
            String value = data.get(k).trim();
            if (value.length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(value).append("&");
        }
        sb.append("key=").append(key);
        return MD5(sb.toString());

    }

}
