package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.CustomerResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CustomerResultDeserializer implements JsonDeserializer<CustomerResult> {
    @Override
    public CustomerResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        CustomerResult customerResult = gson.fromJson(bodyObject.toString(), CustomerResult.class);

        return customerResult;
    }
}
