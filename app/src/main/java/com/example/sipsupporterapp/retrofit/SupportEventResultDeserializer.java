package com.example.sipsupporterapp.retrofit;

import android.util.Log;

import com.example.sipsupporterapp.model.SupportEventResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SupportEventResultDeserializer implements JsonDeserializer<SupportEventResult> {
    @Override
    public SupportEventResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        SupportEventResult supportEventResult = gson.fromJson(bodyObject.toString(), SupportEventResult.class);

        return supportEventResult;
    }
}
