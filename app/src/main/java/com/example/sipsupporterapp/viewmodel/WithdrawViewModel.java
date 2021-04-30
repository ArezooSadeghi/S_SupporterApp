package com.example.sipsupporterapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.PaymentInfo;
import com.example.sipsupporterapp.model.PaymentResult;
import com.example.sipsupporterapp.model.PaymentSubjectResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.HashMap;

public class WithdrawViewModel extends AndroidViewModel {
    private SipSupportRepository repository;

    private SingleLiveEvent<BankAccountResult> bankAccountResultSingleLiveEvent;
    private SingleLiveEvent<String> errorBankAccountResultSingleLiveEvent;

    private SingleLiveEvent<PaymentResult> paymentResultPaymentsListByBankAccountSingleLiveEvent;
    private SingleLiveEvent<String> errorPaymentResultPaymentsListByBankAccountSingleLiveEvent;

    private SingleLiveEvent<PaymentInfo> editClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<PaymentInfo> deleteClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<PaymentInfo> seeDocumentsClickedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<PaymentResult> paymentResultPaymentsEditSingleLiveEvent;
    private SingleLiveEvent<String> errorPaymentResultPaymentsEditSingleLiveEvent;

    private SingleLiveEvent<PaymentResult> paymentResultPaymentsDeleteSingleLiveEvent;
    private SingleLiveEvent<String> errorPaymentResultPaymentsDeleteSingleLiveEvent;

    private SingleLiveEvent<Boolean> yesDeleteSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> updateListSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<PaymentSubjectResult> paymentSubjectResultPaymentSubjectsListSingleLiveEvent;
    private SingleLiveEvent<String> errorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent;

    private SingleLiveEvent<BankAccountResult> addPaymentDialogBankAccountResultSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<PaymentResult> paymentResultPaymentsAddSingleLiveEvent;
    private SingleLiveEvent<String> errorPaymentResultPaymentsAddSingleLiveEvent;

    private SingleLiveEvent<Boolean> successDialogDismissSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Integer> updatingSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<HashMap<Integer, String>> subjectSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;

    public WithdrawViewModel(@NonNull Application application) {
        super(application);

        repository = SipSupportRepository.getInstance(getApplication());

        bankAccountResultSingleLiveEvent = repository.getBankAccountResultSingleLiveEvent();
        errorBankAccountResultSingleLiveEvent = repository.getErrorBankAccountResultSingleLiveEvent();

        paymentResultPaymentsListByBankAccountSingleLiveEvent = repository.getPaymentResultPaymentsListByBankAccountSingleLiveEvent();
        errorPaymentResultPaymentsListByBankAccountSingleLiveEvent = repository.getErrorPaymentResultPaymentsListByBankAccountSingleLiveEvent();

        paymentResultPaymentsEditSingleLiveEvent = repository.getPaymentResultPaymentsEditSingleLiveEvent();
        errorPaymentResultPaymentsEditSingleLiveEvent = repository.getErrorPaymentResultPaymentsEditSingleLiveEvent();

        paymentResultPaymentsDeleteSingleLiveEvent = repository.getPaymentResultPaymentsDeleteSingleLiveEvent();
        errorPaymentResultPaymentsDeleteSingleLiveEvent = repository.getErrorPaymentResultPaymentsDeleteSingleLiveEvent();

        paymentSubjectResultPaymentSubjectsListSingleLiveEvent = repository.getPaymentSubjectResultPaymentSubjectsListSingleLiveEvent();
        errorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent = repository.getErrorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent();

        paymentResultPaymentsAddSingleLiveEvent = repository.getPaymentResultPaymentsAddSingleLiveEvent();
        errorPaymentResultPaymentsAddSingleLiveEvent = repository.getErrorPaymentResultPaymentsAddSingleLiveEvent();

        noConnection = repository.getNoConnection();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
    }

    public SingleLiveEvent<BankAccountResult> getBankAccountResultSingleLiveEvent() {
        return bankAccountResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorBankAccountResultSingleLiveEvent() {
        return errorBankAccountResultSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentResult> getPaymentResultPaymentsListByBankAccountSingleLiveEvent() {
        return paymentResultPaymentsListByBankAccountSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPaymentResultPaymentsListByBankAccountSingleLiveEvent() {
        return errorPaymentResultPaymentsListByBankAccountSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentInfo> getEditClickedSingleLiveEvent() {
        return editClickedSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentInfo> getDeleteClickedSingleLiveEvent() {
        return deleteClickedSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentInfo> getSeeDocumentsClickedSingleLiveEvent() {
        return seeDocumentsClickedSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentResult> getPaymentResultPaymentsEditSingleLiveEvent() {
        return paymentResultPaymentsEditSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPaymentResultPaymentsEditSingleLiveEvent() {
        return errorPaymentResultPaymentsEditSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentResult> getPaymentResultPaymentsDeleteSingleLiveEvent() {
        return paymentResultPaymentsDeleteSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPaymentResultPaymentsDeleteSingleLiveEvent() {
        return errorPaymentResultPaymentsDeleteSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesDeleteSingleLiveEvent() {
        return yesDeleteSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getUpdateListSingleLiveEvent() {
        return updateListSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentSubjectResult> getPaymentSubjectResultPaymentSubjectsListSingleLiveEvent() {
        return paymentSubjectResultPaymentSubjectsListSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent() {
        return errorPaymentSubjectResultPaymentSubjectsListSingleLiveEvent;
    }

    public SingleLiveEvent<BankAccountResult> getAddPaymentDialogBankAccountResultSingleLiveEvent() {
        return addPaymentDialogBankAccountResultSingleLiveEvent;
    }

    public SingleLiveEvent<PaymentResult> getPaymentResultPaymentsAddSingleLiveEvent() {
        return paymentResultPaymentsAddSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorPaymentResultPaymentsAddSingleLiveEvent() {
        return errorPaymentResultPaymentsAddSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getSuccessDialogDismissSingleLiveEvent() {
        return successDialogDismissSingleLiveEvent;
    }

    public SingleLiveEvent<Integer> getUpdatingSingleLiveEvent() {
        return updatingSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<HashMap<Integer, String>> getSubjectSingleLiveEvent() {
        return subjectSingleLiveEvent;
    }

    public void getSipSupportServiceGetBankAccountResult(String baseUrl) {
        repository.getSipSupportServiceGetBankAccountResult(baseUrl);
    }

    public void getSipSupportServicePaymentsListByBankAccount(String baseUrl) {
        repository.getSipSupportServicePaymentsListByBankAccount(baseUrl);
    }

    public void getSipSupportServicePaymentsEdit(String baseUrl) {
        repository.getSipSupportServicePaymentsEdit(baseUrl);
    }

    public void getSipSupportServicePaymentsDelete(String baseUrl) {
        repository.getSipSupportServicePaymentsDelete(baseUrl);
    }

    public void getSipSupportServicePaymentSubjectsList(String userLoginKey) {
        repository.getSipSupportServicePaymentSubjectsList(userLoginKey);
    }

    public void getSipSupportServicePaymentsAdd(String baseUrl) {
        repository.getSipSupportServicePaymentsAdd(baseUrl);
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void fetchBankAccounts(String userLoginKey) {
        repository.fetchBankAccounts(userLoginKey);
    }

    public void fetchPaymentsListByBankAccounts(String userLoginKey, int bankAccountID) {
        repository.fetchPaymentsListByBankAccounts(userLoginKey, bankAccountID);
    }

    public void fetchPaymentSubjectsList(String baseUrl) {
        repository.fetchPaymentSubjectsList(baseUrl);
    }

    public void paymentsDelete(String userLoginKey, int paymentID) {
        repository.paymentsDelete(userLoginKey, paymentID);
    }

    public void paymentEdit(String userLoginKey, PaymentInfo paymentInfo) {
        repository.paymentsEdit(userLoginKey, paymentInfo);
    }

    public void paymentsAdd(String userLoginKey, PaymentInfo paymentInfo) {
        repository.paymentsAdd(userLoginKey, paymentInfo);
    }
}
