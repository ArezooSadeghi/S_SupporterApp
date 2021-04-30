package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.UserLoginParameter;
import com.example.sipsupporterapp.model.UserResult;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.List;

public class SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel extends AndroidViewModel {
    private SipSupportRepository repository;

    private SingleLiveEvent<String> wrongIpAddressSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> insertSpinnerSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> insertIPAddressListSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ServerData> deleteIPAddressListSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ServerData> deleteSpinnerSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> updateSpinnerSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<ServerData> updateIPAddressListSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<UserResult> userResultSingleLiveEvent;
    private SingleLiveEvent<String> errorUserResult;
    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;

    public SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        wrongIpAddressSingleLiveEvent = repository.getWrongIpAddressSingleLiveEvent();
        userResultSingleLiveEvent = repository.getUserResultSingleLiveEvent();
        errorUserResult = repository.getErrorUserResult();
        noConnection = repository.getNoConnection();
        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
    }

    public SingleLiveEvent<Boolean> getInsertSpinnerSingleLiveEvent() {
        return insertSpinnerSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getInsertIPAddressListSingleLiveEvent() {
        return insertIPAddressListSingleLiveEvent;
    }

    public void insertServerData(ServerData serverData) {
        repository.insertServerData(serverData);
    }

    public SingleLiveEvent<ServerData> getDeleteIPAddressListSingleLiveEvent() {
        return deleteIPAddressListSingleLiveEvent;
    }

    public SingleLiveEvent<ServerData> getDeleteSpinnerSingleLiveEvent() {
        return deleteSpinnerSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getUpdateSpinnerSingleLiveEvent() {
        return updateSpinnerSingleLiveEvent;
    }

    public SingleLiveEvent<ServerData> getUpdateIPAddressListSingleLiveEvent() {
        return updateIPAddressListSingleLiveEvent;
    }

    public SingleLiveEvent<String> getWrongIpAddressSingleLiveEvent() {
        return wrongIpAddressSingleLiveEvent;
    }

    public SingleLiveEvent<UserResult> getUserResultSingleLiveEvent() {
        return userResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorUserResult() {
        return errorUserResult;
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public void fetchUserResult(UserLoginParameter userLoginParameter) {
        repository.fetchUserResult(userLoginParameter);
    }

    public List<ServerData> getServerDataList() {
        return repository.getServerDataList();
    }

    public void deleteServerData(ServerData serverData) {
        repository.deleteServerData(serverData);
    }

    public void getSipSupportServicePostUserLoginParameter(String baseUrl) {
        repository.getSipSupportServicePostUserLoginParameter(baseUrl);
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }
}
