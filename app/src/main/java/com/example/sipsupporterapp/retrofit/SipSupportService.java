package com.example.sipsupporterapp.retrofit;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.model.CustomerPaymentResult;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.CustomerResult;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.CustomerUserResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.PaymentInfo;
import com.example.sipsupporterapp.model.PaymentResult;
import com.example.sipsupporterapp.model.PaymentSubjectResult;
import com.example.sipsupporterapp.model.ProductInfo;
import com.example.sipsupporterapp.model.ProductResult;
import com.example.sipsupporterapp.model.SupportEventResult;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SipSupportService {

    @POST(".")
    Call<UserResult> postUserLoginParameter(@Body UserLoginParameter userLoginParameter);

    @GET(".")
    Call<CustomerResult> getCustomers(@Header("userLoginKey") String userLoginKey, @Query("customerName") String customerName);

    @GET(".")
    Call<CustomerResult> getCustomerInfo(@Header("userLoginKey") String userLoginKey, int customerID);

    @POST(".")
    Call<UserResult> changePassword(@Header("userLoginKey") String userLoginKey, @Body String newPassword);

    @GET(".")
    Call<CustomerSupportResult> getCustomerSupportResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @GET(".")
    Call<CustomerUserResult> getCustomerUserResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @GET(".")
    Call<SupportEventResult> getSupportEventResult(@Header("userLoginKey") String userLoginKey);

    @POST(".")
    Call<CustomerSupportResult> postCustomerSupportInfo(@Header("userLoginKey") String userLoginKey, @Body CustomerSupportInfo customerSupportInfo);

    @GET(".")
    Call<DateResult> getDateResult(@Header("userLoginKey") String userLoginKey);

    @GET(".")
    Call<CustomerProductResult> getProductResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @POST(".")
    Call<ProductResult> postProductInfo(@Header("userLoginKey") String userLoginKey, @Body ProductInfo productInfo);

    @GET(".")
    Call<ProductResult> getProductResult(@Header("userLoginKey") String userLoginKey);

    @POST(".")
    Call<CustomerProductResult> postCustomerProducts(@Header("userLoginKey") String userLoginKey, @Body CustomerProducts customerProducts);

    @GET(".")
    Call<ProductResult> getProductInfo(@Header("userLoginKey") String userLoginKey, @Query("productID") int productID);

    @DELETE(".")
    Call<CustomerProductResult> deleteCustomerProduct(@Header("userLoginKey") String userLoginKey, @Query("customerProductID") int customerProductID);

    @PUT(".")
    Call<CustomerProductResult> editCustomerProduct(@Header("userLoginKey") String userLoginKey, @Body CustomerProducts customerProducts);

    @POST(".")
    Call<AttachResult> attach(@Header("userLoginKey") String userLoginKey, @Body AttachInfo attachInfo);

    @GET(".")
    Call<CustomerPaymentResult> getCustomerPaymentResult(@Header("userLoginKey") String userLoginKey, @Query("customerID") int customerID);

    @GET(".")
    Call<AttachResult> getAttachmentFilesViaCustomerPaymentID(@Header("userLoginKey") String userLoginKey, @Query("customerPaymentID") int paymentID, @Query("LoadFileData") boolean LoadFileData);

    @GET(".")
    Call<AttachResult> getAttachmentFilesViaCustomerProductID(@Header("userLoginKey") String userLoginKey, @Query("customerProductID") int customerProductID, @Query("LoadFileData") boolean LoadFileData);

    @GET(".")
    Call<AttachResult> getAttachmentFilesViaCustomerSupportID(@Header("userLoginKey") String userLoginKey, @Query("customerSupportID") int customerSupportID, @Query("LoadFileData") boolean LoadFileData);

    @GET(".")
    Call<AttachResult> getAttachmentFileViaAttachID(@Header("userLoginKey") String userLoginKey, @Query("attachID") int attachID, @Query("LoadFileData") boolean LoadFileData);

    @PUT(".")
    Call<CustomerPaymentResult> editCustomerPaymentsResult(@Header("userLoginKey") String userLoginKey, @Body CustomerPaymentInfo customerPaymentInfo);

    @POST(".")
    Call<CustomerPaymentResult> addCustomerPaymentsResult(@Header("userLoginKey") String userLoginKey, @Body CustomerPaymentInfo customerPaymentInfo);

    @DELETE(".")
    Call<CustomerPaymentResult> deleteCustomerPayments(@Header("userLoginKey") String userLoginKey, @Query("customerPaymentID") int customerPaymentID);

    @GET(".")
    Call<BankAccountResult> getBankAccountResult(@Header("userLoginKey") String userLoginKey);

    @GET(".")
    Call<PaymentResult> paymentsListAllBankAccount(@Header("userLoginKey") String userLoginKey);

    @GET(".")
    Call<PaymentResult> paymentsListByBankAccount(@Header("userLoginKey") String userLoginKey, @Query("bankAccountID") int bankAccountID);

    @PUT(".")
    Call<PaymentResult> paymentsEdit(@Header("userLoginKey") String userLoginKey, @Body PaymentInfo paymentInfo);

    @DELETE(".")
    Call<PaymentResult> paymentsDelete(@Header("userLoginKey") String userLoginKey, @Query("paymentID") int paymentID);

    @GET(".")
    Call<PaymentSubjectResult> paymentSubjectsList(@Header("userLoginKey") String userLoginKey);

    @POST(".")
    Call<PaymentResult> paymentsAdd(@Header("userLoginKey") String userLoginKey, @Body PaymentInfo paymentInfo);

    @DELETE(".")
    Call<AttachResult> deleteAttach(@Header("userLoginKey") String userLoginKey, @Query("attachID") int attachID);
}
