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
import com.example.sipsupporterapp.databinding.FragmentDeleteQuestionCustomerPaymentsDialogBinding;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;

public class DeletePaymentQuestionDialogFragment extends DialogFragment {
    private FragmentDeleteQuestionCustomerPaymentsDialogBinding binding;
    private WithdrawViewModel viewModel;

    public static final String TAG = DeletePaymentQuestionDialogFragment.class.getSimpleName();

    public static DeletePaymentQuestionDialogFragment newInstance() {
        DeletePaymentQuestionDialogFragment fragment = new DeletePaymentQuestionDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_delete_question_customer_payments_dialog,
                null,
                false);

        handleClicked();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void handleClicked() {
        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getYesDeleteSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}