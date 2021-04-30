package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.List;

public class SupportHistoryViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<CustomerSupportResult> customerSupportResult;
    private SingleLiveEvent<String> errorCustomerSupportResult;
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;
    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;

    private SingleLiveEvent<CustomerSupportInfo> attachFileClicked = new SingleLiveEvent<>();

    private SingleLiveEvent<String> fileDataSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> requestPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> allowPermissionSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> attachResultSingleLiveEvent;
    private SingleLiveEvent<String> errorAttachResultSingleLiveEvent;

    private SingleLiveEvent<Boolean> dismissClickedSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> yesAgain = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> noAgain = new SingleLiveEvent<>();

    private SingleLiveEvent<CustomerSupportInfo> mCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent = new SingleLiveEvent<>();

    public SupportHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        customerSupportResult = repository.getCustomerSupportResult();
        errorCustomerSupportResult = repository.getErrorCustomerSupportResult();
        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
        noConnection = repository.getNoConnection();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
        attachResultSingleLiveEvent = repository.getAttachResultSingleLiveEvent();
        errorAttachResultSingleLiveEvent = repository.getErrorAttachResultSingleLiveEvent();
    }

    public SingleLiveEvent<CustomerSupportResult> getCustomerSupportResult() {
        return customerSupportResult;
    }

    public SingleLiveEvent<String> getErrorCustomerSupportResult() {
        return errorCustomerSupportResult;
    }

    public void getCustomerSupportResult(String userLoginKey, int customerID) {
        repository.getCustomerSupportResult(userLoginKey, customerID);
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void getSipSupportServiceCustomerSupportResult(String baseUrl) {
        repository.getSipSupportServiceCustomerSupportResult(baseUrl);
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerSupportInfo> getAttachFileClicked() {
        return attachFileClicked;
    }

    public void deleteServerData(ServerData serverData) {
        repository.deleteServerData(serverData);
    }

    public List<ServerData> getServerDataList() {
        return repository.getServerDataList();
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

    public void getSipSupportServiceAttach(String baseUrl) {
        repository.getSipSupportServiceAttach(baseUrl);
    }

    public void attach(String userLoginKey, AttachInfo attachInfo) {
        repository.attach(userLoginKey, attachInfo);
    }

    public SingleLiveEvent<AttachResult> getAttachResultSingleLiveEvent() {
        return attachResultSingleLiveEvent;
    }

    public void setAttachResultSingleLiveEvent(SingleLiveEvent<AttachResult> attachResultSingleLiveEvent) {
        this.attachResultSingleLiveEvent = attachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultSingleLiveEvent() {
        return errorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<CustomerSupportInfo> getCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent() {
        return mCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent;
    }

    public void setErrorAttachResultSingleLiveEvent(SingleLiveEvent<String> errorAttachResultSingleLiveEvent) {
        this.errorAttachResultSingleLiveEvent = errorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDismissClickedSingleLiveEvent() {
        return dismissClickedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesAgain() {
        return yesAgain;
    }

    public void setYesAgain(SingleLiveEvent<Boolean> yesAgain) {
        this.yesAgain = yesAgain;
    }

    public SingleLiveEvent<Boolean> getNoAgain() {
        return noAgain;
    }

    public void setNoAgain(SingleLiveEvent<Boolean> noAgain) {
        this.noAgain = noAgain;
    }
}
