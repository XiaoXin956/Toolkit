package com.xiaoxin.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author: Admin
 * @date: 2021-12-02
 */
public class GsonUtils {

    static Gson gson = new GsonBuilder().create();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T toObject(String resJson) {
        T data = gson.fromJson(resJson,new TypeToken<T>(){}.getType());
        return data;
    }

    public static <T> List<T> toListObject(String resJson) {
        return gson.fromJson(resJson,new TypeToken<List<T>>(){}.getType());
    }

}
