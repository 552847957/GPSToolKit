package com.srmn.xwork.androidlib.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by XYN on 2015/7/21.
 */
public class GsonUtil {

    public static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

            String value = json.getAsJsonPrimitive().getAsString();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (value.contains("T")) {
                value = value.replace("T", " ");
            }

            if (value.contains(".")) {
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
            }


            Date date = new Date();

            try {
                date = df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
    }


    public static Gson getGson() {
        GsonBuilder gsonb = new GsonBuilder();
        DateDeserializer ds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, ds);
        gsonb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }

    public static Gson getGsonExcludeFieldsWithoutExposeAnnotation() {
        GsonBuilder gsonb = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        DateDeserializer ds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, ds);
        gsonb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson;
    }

    public static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);

        ArrayList<T> listOfT = new ArrayList<>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(new Gson().fromJson(jsonObj, classOfT));
        }

        return listOfT;
    }



    public static <T> T DeserializerSingleDataResult(String json, Type objectType) {
        Gson gson = GsonUtil.getGson();
        T dataResult = gson.fromJson(json, objectType);
        return dataResult;
    }

////    public static <T> ListDataResult<T> DeserializerListDataResult(String json) {
////        Gson gson = GsonUtil.getGson();
////        Type projectobjectType = new TypeToken<ListDataResult<T>>() {}.getType();
////        ListDataResult<T> listDataResult = gson.fromJson(json, projectobjectType);
////        return listDataResult;
////    }
}
