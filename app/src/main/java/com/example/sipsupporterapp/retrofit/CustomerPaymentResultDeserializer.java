package com.example.sipsupporterapp.retrofit;

import android.util.Log;

import com.example.sipsupporterapp.model.CustomerPaymentResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CustomerPaymentResultDeserializer implements JsonDeserializer<CustomerPaymentResult> {
    @Override
    public CustomerPaymentResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        CustomerPaymentResult customerPaymentResult = gson.fromJson(bodyObject.toString(), CustomerPaymentResult.class);

        return customerPaymentResult;
    }
}
