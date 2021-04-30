package com.example.sipsupporterapp.view.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.fragment.CustomerFragment;

public class CustomerContainerActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return CustomerFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CustomerContainerActivity.class);
    }

    @Override
    protected void onDestroy() {
        SipSupportSharedPreferences.setLastSearchQuery(this, null);
        super.onDestroy();
    }
}