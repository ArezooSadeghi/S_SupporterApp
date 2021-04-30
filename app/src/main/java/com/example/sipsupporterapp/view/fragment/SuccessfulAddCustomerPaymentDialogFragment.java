package com.example.sipsupporterapp.view.fragment;

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
import com.example.sipsupporterapp.databinding.FragmentSuccessfulAddCustomerPaymentDialogBinding;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;

public class SuccessfulAddCustomerPaymentDialogFragment extends DialogFragment {
    private FragmentSuccessfulAddCustomerPaymentDialogBinding binding;
    private DepositAmountsViewModel viewModel;

    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    public static final String TAG = SuccessfulAddCustomerPaymentDialogFragment.class.getSimpleName();

    public static SuccessfulAddCustomerPaymentDialogFragment newInstance() {
        SuccessfulAddCustomerPaymentDialogFragment fragment = new SuccessfulAddCustomerPaymentDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_successful_add_customer_payment_dialog,
                null,
                false);

        setListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void setListener() {
        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getDismissSuccessfulAddCustomerPaymentsSingleLiveEvent().setValue(true);
                viewModel.getUpdateListAddCustomerPaymentSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}