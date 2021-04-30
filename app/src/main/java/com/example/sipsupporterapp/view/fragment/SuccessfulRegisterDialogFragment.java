package com.example.sipsupporterapp.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentSuccessfullRegisterDialogBinding;
import com.example.sipsupporterapp.viewmodel.CustomerUsersViewModel;

public class SuccessfulRegisterDialogFragment extends DialogFragment {
    private FragmentSuccessfullRegisterDialogBinding binding;
    private CustomerUsersViewModel viewModel;

    public static SuccessfulRegisterDialogFragment newInstance() {
        SuccessfulRegisterDialogFragment fragment = new SuccessfulRegisterDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CustomerUsersViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_successfull_register_dialog,
                null,
                false);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getSuccessfulRegisterItemClickedSingleLiveEvent().setValue(true);
                viewModel.getSuccessfulRegisterCustomerUsersSingleLiveEvent().setValue(true);
                dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}