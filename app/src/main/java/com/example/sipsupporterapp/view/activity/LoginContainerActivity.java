package com.example.sipsupporterapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.fragment.LoginFragment;

public class LoginContainerActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SipSupportSharedPreferences.getUserLoginKey(this) != null) {
            Intent intent = CustomerContainerActivity.newIntent(this);
            startActivity(intent);
            finish();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginContainerActivity.class);
    }
}