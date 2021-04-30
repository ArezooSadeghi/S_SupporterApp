package com.example.sipsupporterapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sipsupporterapp.repository.SipSupportRepository;

public class SuccessViewModel extends AndroidViewModel {
    private SipSupportRepository repository;

    public SuccessViewModel(@NonNull Application application) {
        super(application);

        repository = SipSupportRepository.getInstance(getApplication());
    }
}
