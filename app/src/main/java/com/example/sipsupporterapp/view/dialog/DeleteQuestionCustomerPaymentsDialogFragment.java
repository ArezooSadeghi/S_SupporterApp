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
import com.example.sipsupporterapp.databinding.FragmentDeleteQuestionCustomerPaymentsDialogBinding;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;

public class DeleteQuestionCustomerPaymentsDialogFragment extends DialogFragment {
    private DepositAmountsViewModel viewModel;
    private FragmentDeleteQuestionCustomerPaymentsDialogBinding binding;
    private int customerPaymentID;

    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    public static final String TAG = DeleteQuestionCustomerPaymentsDialogFragment.class.getSimpleName();

    public static DeleteQuestionCustomerPaymentsDialogFragment newInstance(int customerPaymentID) {
        DeleteQuestionCustomerPaymentsDialogFragment fragment = new DeleteQuestionCustomerPaymentsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_delete_question_customer_payments_dialog,
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
        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getYesDeleteCustomerPaymentsSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}