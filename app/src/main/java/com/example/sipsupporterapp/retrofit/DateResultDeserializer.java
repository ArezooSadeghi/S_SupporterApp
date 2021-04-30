package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.DateResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DateResultDeserializer implements JsonDeserializer<DateResult> {
    @Override
    public DateResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        DateResult dateResult = gson.fromJson(bodyObject.toString(), DateResult.class);

        return dateResult;
    }
}
