package com.srmn.xwork.androidlib.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XYN on 2015/7/21.
 */
public class GsonUtil {

    public static Gson getGson() {
        GsonBuilder gsonb = new GsonBuilder();
        DateDeserializer ds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, ds);
        gsonb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonb.create();
        return gson;
    }

    public static <T> T DeserializerSingleDataResult(String json, Type objectType) {
        Gson gson = GsonUtil.getGson();
        T dataResult = gson.fromJson(json, objectType);
        return dataResult;
    }

    public static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

            String value = json.getAsJsonPrimitive().getAsString();

            SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();

            Date date = new Date();

            try {
                date = df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
    }


//    public static <T> ListDataResult<T> DeserializerListDataResult(String json) {
//        Gson gson = GsonUtil.getGson();
//        Type projectobjectType = new TypeToken<ListDataResult<T>>() {}.getType();
//        ListDataResult<T> listDataResult = gson.fromJson(json, projectobjectType);
//        return listDataResult;
//    }
}
