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
import com.example.sipsupporterapp.databinding.FragmentSuccessfulDeleteCustomerPaymentDialogBinding;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;


public class SuccessfulDeleteCustomerPaymentDialogFragment extends DialogFragment {
    private FragmentSuccessfulDeleteCustomerPaymentDialogBinding binding;
    private DepositAmountsViewModel viewModel;

    public static final String TAG = SuccessfulDeleteCustomerPaymentDialogFragment.class.getSimpleName();

    public static SuccessfulDeleteCustomerPaymentDialogFragment newInstance() {
        SuccessfulDeleteCustomerPaymentDialogFragment fragment = new SuccessfulDeleteCustomerPaymentDialogFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_successful_delete_customer_payment_dialog, null, false);

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
        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getUpdateListDeleteCustomerPaymentSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}