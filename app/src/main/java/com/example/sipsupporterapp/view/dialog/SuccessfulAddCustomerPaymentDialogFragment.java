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
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;

public class SuccessfulAddCustomerPaymentDialogFragment extends DialogFragment {
    private FragmentSuccessDialogBinding binding;
    private DepositAmountsViewModel viewModel;

    private static final String ARGS_MESSAGE = "message";

    public static final String TAG = SuccessfulAddCustomerPaymentDialogFragment.class.getSimpleName();

    public static SuccessfulAddCustomerPaymentDialogFragment newInstance(String message) {
        SuccessfulAddCustomerPaymentDialogFragment fragment = new SuccessfulAddCustomerPaymentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_success_dialog,
                null,
                false);

        initViews();
        handleEvents();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);
    }

    private void initViews() {
        String message = getArguments().getString(ARGS_MESSAGE);
        binding.txtMessage.setText(message);
    }

    private void handleEvents() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getDismissSuccessfulAddCustomerPaymentsSingleLiveEvent().setValue(true);
                viewModel.getUpdateListAddCustomerPaymentSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}