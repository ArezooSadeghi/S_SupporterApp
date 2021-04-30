package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.model.CustomerPaymentResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

public class DepositAmountsViewModel extends AndroidViewModel {
    private SipSupportRepository repository;

    private SingleLiveEvent<CustomerPaymentResult> customerPaymentResultSingleLiveEvent;
    private SingleLiveEvent<String> errorCustomerPaymentResultSingleLiveEvent;

    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<String> noConnection;

    private SingleLiveEvent<String> fileDataSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> requestPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> allowPermissionSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> attachResultSingleLiveEvent;
    private SingleLiveEvent<String> errorAttachResultSingleLiveEvent;

    private SingleLiveEvent<CustomerPaymentInfo> addDocumentClickedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> dialogDismissedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> yesAttachAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> noAttachAgainSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentInfo> seeDocumentsClickedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> yesDeleteCustomerPaymentsSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentInfo> DeleteCustomerPaymentsClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<CustomerPaymentInfo> editCustomerPaymentsClickedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentResult> addEditDeleteCustomerPaymentsSingleLiveEvent;
    private SingleLiveEvent<String> errorAddEditDeleteCustomerResultSingleLiveEvent;

    private SingleLiveEvent<BankAccountResult> bankAccountResultSingleLiveEvent;
    private SingleLiveEvent<String> errorBankAccountResultSingleLiveEvent;

    private SingleLiveEvent<Boolean> dismissSuccessfulAddCustomerPaymentsSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentResult> addCustomerPaymentsSingleLiveEvent;
    private SingleLiveEvent<String> errorAddCustomerPaymentSingleLiveEvent;

    private SingleLiveEvent<Boolean> updateListAddCustomerPaymentSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentResult> deleteCustomerPaymentsSingleLiveEvent;
    private SingleLiveEvent<String> errorDeleteCustomerPaymentSingleLiveEvent;

    private SingleLiveEvent<Boolean> updateListDeleteCustomerPaymentSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerPaymentResult> editCustomerPaymentsSingleLiveEvent;
    private SingleLiveEvent<String> errorEditCustomerPaymentSingleLiveEvent;

    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;


    public DepositAmountsViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());

        customerPaymentResultSingleLiveEvent = repository.getCustomerPaymentResultSingleLiveEvent();
        errorCustomerPaymentResultSingleLiveEvent = repository.getErrorCustomerPaymentResultSingleLiveEvent();

        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
        noConnection = repository.getNoConnection();

        attachResultSingleLiveEvent = repository.getAttachResultSingleLiveEvent();
        errorAttachResultSingleLiveEvent = repository.getErrorAttachResultSingleLiveEvent();

        addEditDeleteCustomerPaymentsSingleLiveEvent = repository.getAddEditDeleteCustomerPaymentsSingleLiveEvent();
        errorAddEditDeleteCustomerResultSingleLiveEvent = repository.getErrorAddEditDeleteCustomerResultSingleLiveEvent();

        bankAccountResultSingleLiveEvent = repository.getBankAccountResultSingleLiveEvent();
        errorBankAccountResultSingleLiveEvent = repository.getErrorBankAccountResultSingleLiveEvent();

        addCustomerPaymentsSingleLiveEvent = repository.getAddCustomerPaymentsSingleLiveEvent();
        errorAddCustomerPaymentSingleLiveEvent = repository.getErrorAddCustomerPaymentSingleLiveEvent();

        deleteCustomerPaymentsSingleLiveEvent = repository.getDeleteCustomerPaymentsSingleLiveEvent();
        errorDeleteCustomerPaymentSingleLiveEvent = repository.getErrorDeleteCustomerPaymentSingleLiveEvent();

        editCustomerPaymentsSingleLiveEvent = repository.getEditCustomerPaymentsSingleLiveEvent();
        errorEditCustomerPaymentSingleLiveEvent = repository.getErrorEditCustomerPaymentSingleLiveEvent();

        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
    }

    public SingleLiveEvent<CustomerPaymentResult> getCustomerPaymentResultSingleLiveEvent() {
        return customerPaymentResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerPaymentResultSingleLiveEvent() {
        return errorCustomerPaymentResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<String> getFileDataSingleLiveEvent() {
        return fileDataSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getRequestPermissionSingleLiveEvent() {
        return requestPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getAllowPermissionSingleLiveEvent() {
        return allowPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultSingleLiveEvent() {
        return attachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultSingleLiveEvent() {
        return errorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentInfo> getAddDocumentClickedSingleLiveEvent() {
        return addDocumentClickedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDialogDismissedSingleLiveEvent() {
        return dialogDismissedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesAttachAgainSingleLiveEvent() {
        return yesAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getNoAttachAgainSingleLiveEvent() {
        return noAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentInfo> getSeeDocumentsClickedSingleLiveEvent() {
        return seeDocumentsClickedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesDeleteCustomerPaymentsSingleLiveEvent() {
        return yesDeleteCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDismissSuccessfulAddCustomerPaymentsSingleLiveEvent() {
        return dismissSuccessfulAddCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public SingleLiveEvent<CustomerPaymentInfo> getDeleteCustomerPaymentsClickedSingleLiveEvent() {
        return DeleteCustomerPaymentsClickedSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentResult> getAddEditDeleteCustomerPaymentsSingleLiveEvent() {
        return addEditDeleteCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAddEditDeleteCustomerResultSingleLiveEvent() {
        return errorAddEditDeleteCustomerResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentInfo> getEditCustomerPaymentsClickedSingleLiveEvent() {
        return editCustomerPaymentsClickedSingleLiveEvent;
    }

    public SingleLiveEvent<BankAccountResult> getBankAccountResultSingleLiveEvent() {
        return bankAccountResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorBankAccountResultSingleLiveEvent() {
        return errorBankAccountResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentResult> getAddCustomerPaymentsSingleLiveEvent() {
        return addCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAddCustomerPaymentSingleLiveEvent() {
        return errorAddCustomerPaymentSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getUpdateListAddCustomerPaymentSingleLiveEvent() {
        return updateListAddCustomerPaymentSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentResult> getDeleteCustomerPaymentsSingleLiveEvent() {
        return deleteCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorDeleteCustomerPaymentSingleLiveEvent() {
        return errorDeleteCustomerPaymentSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getUpdateListDeleteCustomerPaymentSingleLiveEvent() {
        return updateListDeleteCustomerPaymentSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerPaymentResult> getEditCustomerPaymentsSingleLiveEvent() {
        return editCustomerPaymentsSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorEditCustomerPaymentSingleLiveEvent() {
        return errorEditCustomerPaymentSingleLiveEvent;
    }

    public void getSipSupportServiceCustomerPaymentResult(String baseUrl) {
        repository.getSipSupportServiceCustomerPaymentResult(baseUrl);
    }

    public void fetchCustomerPaymentResult(String userLoginKey, int customerID) {
        repository.fetchCustomerPaymentResult(userLoginKey, customerID);
    }

    public void getSipSupportServiceAttach(String baseUrl) {
        repository.getSipSupportServiceAttach(baseUrl);
    }

    public void getSipSupportServiceAddCustomerPayments(String baseUrl) {
        repository.getSipSupportServiceAddCustomerPayments(baseUrl);
    }

    public void getSipSupportServiceEditCustomerPayments(String baseUrl) {
        repository.getSipSupportServiceEditCustomerPayments(baseUrl);
    }

    public void getSipSupportServiceDeleteCustomerPayments(String baseUrl) {
        repository.getSipSupportServiceDeleteCustomerPayments(baseUrl);
    }

    public void attach(String userLoginKey, AttachInfo attachInfo) {
        repository.attach(userLoginKey, attachInfo);
    }

    public void addCustomerPaymentsResult(String userLoginKey, CustomerPaymentInfo customerPaymentInfo) {
        repository.addCustomerPayments(userLoginKey, customerPaymentInfo);
    }

    public void editCustomerPaymentsResult(String userLoginKey, CustomerPaymentInfo customerPaymentInfo) {
        repository.editCustomerPayments(userLoginKey, customerPaymentInfo);
    }

    public void deleteCustomerPayments(String userLoginKey, int customerPaymentID) {
        repository.deleteCustomerPayments(userLoginKey, customerPaymentID);
    }

    public void getSipSupportServiceGetBankAccountResult(String baseUrl) {
        repository.getSipSupportServiceGetBankAccountResult(baseUrl);
    }

    public void fetchBankAccounts(String userLoginKey) {
        repository.fetchBankAccounts(userLoginKey);
    }
}
