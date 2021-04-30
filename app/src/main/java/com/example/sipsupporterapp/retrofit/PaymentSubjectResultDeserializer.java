package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.PaymentSubjectResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PaymentSubjectResultDeserializer implements JsonDeserializer<PaymentSubjectResult> {
    @Override
    public PaymentSubjectResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        PaymentSubjectResult paymentSubjectResult = gson.fromJson(bodyObject.toString(), PaymentSubjectResult.class);

        return paymentSubjectResult;
    }
}
