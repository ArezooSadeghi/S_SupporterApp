package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentRegisterSupportDialogBinding;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.model.SupportEventResult;
import com.example.sipsupporterapp.model.SupportEvents;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.RegisterSupportDialogViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterSupportDialogFragment extends DialogFragment {

    private FragmentRegisterSupportDialogBinding binding;
    private RegisterSupportDialogViewModel viewModel;
    private int customerID;
    private String value;
    private Map<String, Integer> map = new HashMap<>();

    public static final String TAG = RegisterSupportDialogFragment.class.getSimpleName();
    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static RegisterSupportDialogFragment newInstance(int customerID) {
        RegisterSupportDialogFragment fragment = new RegisterSupportDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterSupportDialogViewModel.class);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceSupportEventResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchSupportEventResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));

        setObservable();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_register_support_dialog,
                null,
                false);

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        Log.d("Arezoo", getChildFragmentManager() + " :RegisterSupportDialogFragment");

        setListener();
        setItemSelected();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(false);

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
                if (binding.edTextAnswer.getText().toString().isEmpty() || binding.edTextQuestion.getText().toString().isEmpty()) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("لطفا موارد خواسته شده را پر کنید");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    int supportEventID = map.get(value);
                    int customerUserID = SipSupportSharedPreferences.getCustomerUserId(getContext());
                    String question = binding.edTextQuestion.getText().toString();
                    String answer = binding.edTextAnswer.getText().toString();
                    CustomerSupportInfo customerSupportInfo = new CustomerSupportInfo(supportEventID, customerID, customerUserID, question, answer);

                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServicePostCustomerSupportResult(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.postCustomerSupportInfo(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerSupportInfo);
                }
            }
        });
    }


    private void setItemSelected() {
        binding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;
            }
        });
    }


    private void setObservable() {
        viewModel.getSupportEventResultSingleLiveEvent().observe(this, new Observer<SupportEventResult>() {
            @Override
            public void onChanged(SupportEventResult supportEventResult) {
                for (SupportEvents supportEvents : supportEventResult.getSupportEvents()) {
                    map.put(supportEvents.getSupportEvent(), supportEvents.getSupportEventID());
                }
                setupSpinner(supportEventResult.getSupportEvents());
            }
        });

        viewModel.getErrorSupportEventResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getCustomerSupportResultSingleLiveEvent().observe(this, new Observer<CustomerSupportResult>() {
            @Override
            public void onChanged(CustomerSupportResult customerSupportResult) {
                SuccessfulRegisterDialogFragment fragment = SuccessfulRegisterDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), "ok");
                dismiss();
            }
        });

        viewModel.getErrorCustomerSupportResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String noConnection) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(noConnection);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }

    private void setupSpinner(SupportEvents[] supportEvents) {
        List<String> supportEventList = new ArrayList<>();
        for (SupportEvents supportEvents1 : supportEvents) {
            if (supportEvents1.getSupportEvent().equals("خدمات تلفني")) {
                supportEventList.add(0, supportEvents1.getSupportEvent());
            } else {
                supportEventList.add(supportEvents1.getSupportEvent());
            }
        }
        binding.spinner.setItems(supportEventList);
        value = "خدمات تلفني";
    }
}