package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.model.CustomerUserResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.repository.SipSupportRepository;

import java.util.List;

public class CustomerUsersViewModel extends AndroidViewModel {
    private SipSupportRepository repository;
    private SingleLiveEvent<CustomerUserResult> customerUserResultSingleLiveEvent;
    private SingleLiveEvent<String> errorCustomerUserResultSingleLiveEvent;
    private SingleLiveEvent<Integer> itemClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> dangerousUserSingleLiveEvent;
    private SingleLiveEvent<DateResult> dateResultSingleLiveEvent;
    private SingleLiveEvent<String> noConnection;
    private SingleLiveEvent<Boolean> timeoutExceptionHappenSingleLiveEvent;
    private SingleLiveEvent<Boolean> successfulRegisterItemClickedSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> successfulRegisterCustomerUsersSingleLiveEvent = new SingleLiveEvent<>();

    public CustomerUsersViewModel(@NonNull Application application) {
        super(application);
        repository = SipSupportRepository.getInstance(getApplication());
        customerUserResultSingleLiveEvent = repository.getCustomerUserResultSingleLiveEvent();
        errorCustomerUserResultSingleLiveEvent = repository.getErrorCustomerUserResultSingleLiveEvent();
        dangerousUserSingleLiveEvent = repository.getDangerousUserSingleLiveEvent();
        dateResultSingleLiveEvent = repository.getDateResultSingleLiveEvent();
        noConnection = repository.getNoConnection();
        timeoutExceptionHappenSingleLiveEvent = repository.getTimeoutExceptionHappenSingleLiveEvent();
    }

    public SingleLiveEvent<CustomerUserResult> getCustomerUserResultSingleLiveEvent() {
        return customerUserResultSingleLiveEvent;
    }

    public SingleLiveEvent<String> getErrorCustomerUserResultSingleLiveEvent() {
        return errorCustomerUserResultSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDangerousUserSingleLiveEvent() {
        return dangerousUserSingleLiveEvent;
    }

    public SingleLiveEvent<Integer> getItemClicked() {
        return itemClicked;
    }

    public ServerData getServerData(String centerName) {
        return repository.getServerData(centerName);
    }

    public void getSipSupportServiceCustomerUserResult(String baseUrl) {
        repository.getSipSupportServiceCustomerUserResult(baseUrl);
    }

    public SingleLiveEvent<DateResult> getDateResultSingleLiveEvent() {
        return dateResultSingleLiveEvent;
    }

    public void fetchCustomerUserResult(String userLoginKey, int customerID) {
        repository.fetchCustomerUserResult(userLoginKey, customerID);
    }

    public SingleLiveEvent<Boolean> getTimeoutExceptionHappenSingleLiveEvent() {
        return timeoutExceptionHappenSingleLiveEvent;
    }

    public void deleteServerData(ServerData serverData) {
        repository.deleteServerData(serverData);
    }

    public List<ServerData> getServerDataList() {
        return repository.getServerDataList();
    }

    public void getSipSupportServiceGetDateResult(String baseUrl) {
        repository.getSipSupportServiceGetDateResult(baseUrl);
    }

    public SingleLiveEvent<String> getNoConnection() {
        return noConnection;
    }

    public SingleLiveEvent<Boolean> getSuccessfulRegisterItemClickedSingleLiveEvent() {
        return successfulRegisterItemClickedSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getSuccessfulRegisterCustomerUsersSingleLiveEvent() {
        return successfulRegisterCustomerUsersSingleLiveEvent;
    }

    public void fetchDateResult(String userLoginKey) {
        repository.fetchDateResult(userLoginKey);
    }
}
