package com.example.sipsupporterapp.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static String BASE_URL = "";

    public static Retrofit getRetrofitInstanceForPostUserLoginParameter(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/users/Login/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceForPostCustomerParameter(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customers/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceChangePassword(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/users/changePassword/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceCustomerSupportResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerSupports/ListByCustomer/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceCustomerUserResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerUsers/userList/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceSupportEventResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/supportEvents/List/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstancePostCustomerSupportInfo(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerSupports/AddWithAnswer/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceGetDateResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/common/getDate/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceGetCustomerProductResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerProducts/List/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstancePostProductInfo(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/products/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceGetProductResult(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/products/List/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstancePostCustomerProducts(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerProducts/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceForGetProductInfo(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/products/Info/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceForDeleteCustomerProduct(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerProducts/Delete/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceForEditCustomerProduct(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerProducts/Edit/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getRetrofitInstanceForAttach(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getCustomerPaymentResultRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerPayments/ListByCustomer/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getAttachmentFilesViaCustomerPaymentIDRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/List_ByCustomerPayment/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getAttachmentFilesViaCustomerProductIDRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/List_ByCustomerProduct/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getAttachmentFilesViaCustomerSupportIDRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/List_ByCustomerSupport/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getAttachmentFileViaAttachIDRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/Info/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit addCustomerPaymentsRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerPayments/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit editCustomerPaymentsRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerPayments/Edit/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit deleteCustomerPaymentsRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/customerPayments/Delete/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit getBankAccountResultRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/bankAccounts/List/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentsListAllBankAccountRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/payments/ListAllBankAccount/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentsListByBankAccountRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/payments/ListByBankAccount/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentsEditRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/payments/Edit/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentsDeleteRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/payments/Delete/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentSubjectsListRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/paymentSubjects/List/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit paymentsAddRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/payments/Add/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Retrofit deleteAttachRetrofitInstance(Type type, Object typeAdapter, Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor(context))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://" + BASE_URL + "/api/v1/attach/Delete/")
                .addConverterFactory(createConverter(type, typeAdapter))
                .client(client)
                .build();
    }

    public static Converter.Factory createConverter(Type type, Object typeAdapter) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }

    public static void getNewBaseUrl(String newBaseUrl) {
        BASE_URL = newBaseUrl;
    }
}
