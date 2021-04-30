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
import com.example.sipsupporterapp.databinding.FragmentSuccessDeletePaymentDialogBinding;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;

public class SuccessDeletePaymentDialogFragment extends DialogFragment {
    private FragmentSuccessDeletePaymentDialogBinding binding;
    private WithdrawViewModel viewModel;

    private String message;

    private static final String ARGS_MESSAGE = "message";

    public static final String TAG = SuccessDeletePaymentDialogFragment.class.getSimpleName();

    public static SuccessDeletePaymentDialogFragment newInstance(String message) {
        SuccessDeletePaymentDialogFragment fragment = new SuccessDeletePaymentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = getArguments().getString(ARGS_MESSAGE);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_success_delete_payment_dialog,
                null,
                false);

        initViews();
        handleClicked();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void initViews() {
        binding.txtMessage.setText(message);
    }

    private void handleClicked() {
        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getUpdateListSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}