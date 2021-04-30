package com.example.sipsupporterapp.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.List;

public class AttachmentViewModel extends AndroidViewModel {
    private SipSupportRepository mRepository;

    private SingleLiveEvent<Boolean> mRequestPermissionSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mAllowPermissionSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> mAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> mErrorAttachResultSingleLiveEvent;
    private SingleLiveEvent<String> mNoConnectionSingleLiveEvent;
    private SingleLiveEvent<Boolean> mTimeOutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<Boolean> mDangerousUserSingleLiveEvent;

    private SingleLiveEvent<String> mBitmapAsStringSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> mIsAttachAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mYesAgainSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> mNoAgainSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    private SingleLiveEvent<String> getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;

    private SingleLiveEvent<AttachInfo[]> doneWriteFilesSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> attachResultForImageListSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> getAttachmentFilesViaCustomerProductIDSingleLiveEvent;
    private SingleLiveEvent<String> getErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent;

    private SingleLiveEvent<AttachResult> getAttachmentFilesViaCustomerSupportIDSingleLiveEvent;
    private SingleLiveEvent<String> getErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent;

    private SingleLiveEvent<Bitmap> attachmentAdapterItemClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> showFullScreenSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<AttachResult> attachResultViaAttachIDSingleLiveEvent;
    private SingleLiveEvent<String> errorAttachResultViaAttachIDSingleLiveEvent;

    private SingleLiveEvent<AttachResult> updateImageListSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<Boolean> bitmapIsSetSingleLiveEvent = new SingleLiveEvent<>();

    private SingleLiveEvent<List<Bitmap>> bitmapListSingleLiveEvent = new SingleLiveEvent<>();

    public AttachmentViewModel(@NonNull Application application) {
        super(application);

        mRepository = SipSupportRepository.getInstance(getApplication());

        mAttachResultSingleLiveEvent = mRepository.getAttachResultSingleLiveEvent();
        mErrorAttachResultSingleLiveEvent = mRepository.getErrorAttachResultSingleLiveEvent();
        mNoConnectionSingleLiveEvent = mRepository.getNoConnection();
        mTimeOutExceptionHappenSingleLiveEvent = mRepository.getTimeoutExceptionHappenSingleLiveEvent();
        mDangerousUserSingleLiveEvent = mRepository.getDangerousUserSingleLiveEvent();

        getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent = mRepository.getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent();
        getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent = mRepository.getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent();

        getAttachmentFilesViaCustomerProductIDSingleLiveEvent = mRepository.getGetAttachmentFilesViaCustomerProductIDSingleLiveEvent();
        getErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent = mRepository.getGetErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent();

        getAttachmentFilesViaCustomerSupportIDSingleLiveEvent = mRepository.getGetAttachmentFilesViaCustomerSupportIDSingleLiveEvent();
        getErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent = mRepository.getGetErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent();

        attachResultViaAttachIDSingleLiveEvent = mRepository.getAttachResultViaAttachIDSingleLiveEvent();
        errorAttachResultViaAttachIDSingleLiveEvent = mRepository.getErrorAttachResultViaAttachIDSingleLiveEvent();

    }

    public SingleLiveEvent<Boolean> getRequestPermissionSingleLiveEvent() {
        return mRequestPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getAllowPermissionSingleLiveEvent() {
        return mAllowPermissionSingleLiveEvent;
    }

    public SingleLiveEvent<String> getBitmapAsStringSingleLiveEvent() {
        return mBitmapAsStringSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultSingleLiveEvent() {
        return mAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultSingleLiveEvent() {
        return mErrorAttachResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getNoConnectionSingleLiveEvent() {
        return mNoConnectionSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeOutExceptionHappenSingleLiveEvent() {
        return mTimeOutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return mDangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getIsAttachAgainSingleLiveEvent() {
        return mIsAttachAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getYesAgainSingleLiveEvent() {
        return mYesAgainSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getNoAgainSingleLiveEvent() {
        return mNoAgainSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent() {
        return getAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent() {
        return getErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent;
    }

    public SingleLiveEvent<AttachInfo[]> getDoneWriteFilesSingleLiveEvent() {
        return doneWriteFilesSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultForImageListSingleLiveEvent() {
        return attachResultForImageListSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getGetAttachmentFilesViaCustomerProductIDSingleLiveEvent() {
        return getAttachmentFilesViaCustomerProductIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent() {
        return getErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getGetAttachmentFilesViaCustomerSupportIDSingleLiveEvent() {
        return getAttachmentFilesViaCustomerSupportIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getGetErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent() {
        return getErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent;
    }

    public SingleLiveEvent<Bitmap> getAttachmentAdapterItemClickedSingleLiveEvent() {
        return attachmentAdapterItemClickedSingleLiveEvent;
    }

    public SingleLiveEvent<String> getShowFullScreenSingleLiveEvent() {
        return showFullScreenSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getAttachResultViaAttachIDSingleLiveEvent() {
        return attachResultViaAttachIDSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorAttachResultViaAttachIDSingleLiveEvent() {
        return errorAttachResultViaAttachIDSingleLiveEvent;
    }

    public SingleLiveEvent<AttachResult> getUpdateImageListSingleLiveEvent() {
        return updateImageListSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getBitmapIsSetSingleLiveEvent() {
        return bitmapIsSetSingleLiveEvent;
    }

    public SingleLiveEvent<List<Bitmap>> getBitmapListSingleLiveEvent() {
        return bitmapListSingleLiveEvent;
    }

    public ServerData getServerData(String centerName) {
        return mRepository.getServerData(centerName);
    }

    public void getSipSupportServiceAttach(String baseUrl) {
        mRepository.getSipSupportServiceAttach(baseUrl);
    }

    public void getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(String baseUrl) {
        mRepository.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(baseUrl);
    }

    public void getSipSupportServiceGetAttachmentFilesViaCustomerProductID(String baseUrl) {
        mRepository.getSipSupportServiceGetAttachmentFilesViaCustomerProductID(baseUrl);
    }

    public void getSipSupportServiceGetAttachmentFilesViaCustomerSupportID(String baseUrl) {
        mRepository.getSipSupportServiceGetAttachmentFilesViaCustomerSupportID(baseUrl);
    }

    public void getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(String baseUrl) {
        mRepository.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(baseUrl);
    }

    public void attach(String userLoginKey, AttachInfo attachInfo) {
        mRepository.attach(userLoginKey, attachInfo);
    }

    public void getAttachmentFilesViaCustomerPaymentID(String userLoginKey, int customerPaymentID, boolean LoadFileData) {
        mRepository.getAttachmentFilesViaCustomerPaymentID(userLoginKey, customerPaymentID, LoadFileData);
    }

    public void fetchFileWithCustomerProductID(String userLoginKey, int customerProductID, boolean LoadFileData) {
        mRepository.fetchFileWithCustomerProductID(userLoginKey, customerProductID, LoadFileData);
    }

    public void fetchFileWithCustomerSupportID(String userLoginKey, int customerSupportID, boolean LoadFileData) {
        mRepository.fetchFileWithCustomerSupportID(userLoginKey, customerSupportID, LoadFileData);
    }

    public void fetchWithAttachID(String userLoginKey, int attachID, boolean loadFileData) {
        mRepository.fetchWithAttachID(userLoginKey, attachID, loadFileData);
    }

}
