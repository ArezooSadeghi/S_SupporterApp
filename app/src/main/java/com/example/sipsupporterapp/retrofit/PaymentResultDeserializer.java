package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.PaymentResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PaymentResultDeserializer implements JsonDeserializer<PaymentResult> {
    @Override
    public PaymentResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        PaymentResult paymentResult = gson.fromJson(bodyObject.toString(), PaymentResult.class);

        return paymentResult;
    }
}
