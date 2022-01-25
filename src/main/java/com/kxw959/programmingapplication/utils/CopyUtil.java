package com.kxw959.programmingapplication.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class CopyUtil {
    public static <T>T copyObject(Object object){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(object).getAsJsonObject();
        return gson.fromJson(jsonObject,(Type) object.getClass());
    }
}
