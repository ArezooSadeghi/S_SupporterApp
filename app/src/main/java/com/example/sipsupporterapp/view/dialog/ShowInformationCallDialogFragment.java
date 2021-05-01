package com.example.sipsupporterapp.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentShowInformationCallDialogBinding;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;

public class ShowInformationCallDialogFragment extends DialogFragment {
    private FragmentShowInformationCallDialogBinding binding;

    public static final String TAG = ShowInformationCallDialogFragment.class.getSimpleName();

    public static ShowInformationCallDialogFragment newInstance() {
        ShowInformationCallDialogFragment fragment = new ShowInformationCallDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_show_information_call_dialog,
                null,
                false);

        if (SipSupportSharedPreferences.getCustomerTel(getContext()) != null & !SipSupportSharedPreferences.getCustomerTel(getContext()).isEmpty()) {

            binding.txtCallInformation.setText(SipSupportSharedPreferences.getCustomerTel(getContext()));
            setListener();

            AlertDialog dialog = new AlertDialog
                    .Builder(getContext())
                    .setView(binding.getRoot())
                    .create();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        } else {

            binding.txtCallInformation.setText("اطلاعات تماس درج نشده است");
            setListener();

            AlertDialog dialog = new AlertDialog
                    .Builder(getContext())
                    .setView(binding.getRoot())
                    .create();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

    }


    private void setListener() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}