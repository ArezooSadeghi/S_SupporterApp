package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.CustomerProductResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ProductResultDeserializer implements JsonDeserializer<CustomerProductResult> {
    @Override
    public CustomerProductResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        CustomerProductResult customerProductResult = gson.fromJson(bodyObject.toString(), CustomerProductResult.class);

        return customerProductResult;
    }
}
