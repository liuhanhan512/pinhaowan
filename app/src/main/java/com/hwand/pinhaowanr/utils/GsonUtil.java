package com.hwand.pinhaowanr.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hanhanliu on 15/12/4.
 */
public class GsonUtil {

    public static <T> List<T> arrayHomePageModelFromData(String str) {

        Type listType = new TypeToken<List<T>>() {}.getType();

        return new Gson().fromJson(str, listType);
    }
}
