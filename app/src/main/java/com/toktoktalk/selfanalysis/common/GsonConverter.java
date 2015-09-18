package com.toktoktalk.selfanalysis.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by seogangmin on 2015. 9. 17..
 */
public class GsonConverter {

    public static final String toJson(Object obj){
        return new Gson().toJson(obj);
    }

    public static final Object fromJson(String json, Class cls){

        return (new Gson().fromJson(json, cls));
    }

    public static final List fromJsonArray(String jsonArray, Class cls){
        Type type = new ListParameterizedType(cls);
        return (List) new Gson().fromJson(jsonArray, type);
    }


    private static class ListParameterizedType implements ParameterizedType {

        private Type type;

        private ListParameterizedType(Type type) {
            this.type = type;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] {type};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        // implement equals method too! (as per javadoc)
    }



}
