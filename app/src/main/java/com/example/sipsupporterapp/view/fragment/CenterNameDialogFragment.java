package com.example.sipsupporterapp.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentCenterNameDialogBinding;
import com.example.sipsupporterapp.model.CustomerParameter;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedCenterNameDialogAndCustomerViewModel;

import java.util.List;

public class CenterNameDialogFragment extends DialogFragment {
    private FragmentCenterNameDialogBinding binding;
    private SharedCenterNameDialogAndCustomerViewModel viewModel;
    public static final String TAG = CenterNameDialogFragment.class.getSimpleName();


    public static CenterNameDialogFragment newInstance() {
        CenterNameDialogFragment fragment = new CenterNameDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedCenterNameDialogAndCustomerViewModel.class);

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_center_name_dialog,
                null,
                false);

     /*   binding.edTextCustomerName.setText(SipSupportSharedPreferences.getLastSearchQuery(getContext()));*/

        binding.edTextCustomerName.setSelection(binding.edTextCustomerName.getText().length());

        setListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }


    private void setListener() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int customerID = 0;
                String customerName = binding.edTextCustomerName.getText().toString();
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), customerName);
                CustomerParameter customerParameter = new CustomerParameter(
                        customerID, customerName);

                SipSupportSharedPreferences.setLastSearchQuery(getContext(), customerName);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));

                viewModel.getSupportServicePostCustomerParameter(serverData.getIpAddress() + ":" + serverData.getPort());

                viewModel.fetchCustomerResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerName);
                viewModel.getShowProgressBarSingleLiveEvent().setValue(true);

                dismiss();
            }
        });
    }
}