package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

public class GalleryViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<AttachResult> getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    private SingleLiveEvent<String> getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> allowPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> requestPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<AttachResult> attachResultSingleLiveEvent;
    private SingleLiveEvent<String> errorAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> fileDataSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> dialogDismissedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> noAttachAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> yesAttachAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> attachOkSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<AttachInfo> photoClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<AttachResult> getAttachmentFilesViaAttachIDSingleLiveEvent;
    private SingleLiveEvent<String> getErrorAttachmentFilesViaAttachIDSingleLiveEvent;

    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent = repository.getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent();
        getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent = repository.getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
        noConnection = repository.getNoConnection();
        attachResultSingleLiveEvent = repository.getAttachResultSingleLiveEvent();
        errorAttachResultSingleLiveEvent = repository.getErrorAttachResultSingleLiveEvent();
        getAttachmentFilesViaAttachIDSingleLiveEvent = repository.getGetAttachmentFilesViaAttachIDSingleLiveEvent();
        getErrorAttachmentFilesViaAttachIDSingleLiveEvent = repository.getGetErrorAttachmentFilesViaAttachIDSingleLiveEvent();

        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
    }

    public SingleLiveEvent<AttachResult> getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent() {
        return getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent() {
        return getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<Boolean> getAllowPermissionSingleLiveEvent() {
        return allowPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getRequestPermissionSingleLiveEvent() {
        return requestPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultSingleLiveEvent() {
        return attachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultSingleLiveEvent() {
        return errorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getFileDataSingleLiveEvent() {
        return fileDataSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDialogDismissedSingleLiveEvent() {
        return dialogDismissedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getNoAttachAgainSingleLiveEvent() {
        return noAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesAttachAgainSingleLiveEvent() {
        return yesAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getAttachOkSingleLiveEvent() {
        return attachOkSingleLiveEvent;
    }

    public SingleLiveEvent<AttachInfo> getPhotoClickedSingleLiveEvent() {
        return photoClickedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getGetAttachmentFilesViaAttachIDSingleLiveEvent() {
        return getAttachmentFilesViaAttachIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorAttachmentFilesViaAttachIDSingleLiveEvent() {
        return getErrorAttachmentFilesViaAttachIDSingleLiveEvent;
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(String baseUrl) {
        repository.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(baseUrl);
    }

    public void getAttachmentFilesViaCustomerPaymentID(String userLoginKey, int customerPaymentID, boolean LoadFileData) {
        repository.getAttachmentFilesViaCustomerPaymentID(userLoginKey, customerPaymentID, LoadFileData);
    }

    public void getSipSupportServiceAttach(String baseUrl) {
        repository.getSipSupportServiceAttach(baseUrl);
    }

    public void attach(String userLoginKey, AttachInfo attachInfo) {
        repository.attach(userLoginKey, attachInfo);
    }

    public void getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(String baseUrl) {
        repository.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(baseUrl);
    }

    public void getAttachmentFileViaAttachID(String userLoginKey, int attachID, boolean LoadFileData) {
        repository.getAttachmentFileViaAttachID(userLoginKey, attachID, LoadFileData);
    }
}
