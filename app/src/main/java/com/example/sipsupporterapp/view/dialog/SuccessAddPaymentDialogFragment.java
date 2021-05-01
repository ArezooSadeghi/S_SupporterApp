package com.example.sipsupporterapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentSuccessDialogBinding;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;

public class SuccessAddPaymentDialogFragment extends DialogFragment {
    private FragmentSuccessDialogBinding binding;
    private WithdrawViewModel viewModel;

    private int bankAccountID;

    private static final String ARGS_BANK_ACCOUNT_ID = "bankAccountID";

    public static final String TAG = SuccessAddPaymentDialogFragment.class.getSimpleName();

    public static SuccessAddPaymentDialogFragment newInstance(int bankAccountID) {
        SuccessAddPaymentDialogFragment fragment = new SuccessAddPaymentDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_BANK_ACCOUNT_ID, bankAccountID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bankAccountID = getArguments().getInt(ARGS_BANK_ACCOUNT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_success_dialog,
                null,
                false);

        handleClicked();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    private void handleClicked() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getSuccessDialogDismissSingleLiveEvent().setValue(true);
                viewModel.getUpdatingSingleLiveEvent().setValue(bankAccountID);
                dismiss();
            }
        });
    }
}