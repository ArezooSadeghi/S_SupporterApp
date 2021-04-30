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
import com.example.sipsupporterapp.databinding.FragmentSuccessfulDeleteDialogBinding;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

public class SuccessfulDeleteDialogFragment extends DialogFragment {
    private FragmentSuccessfulDeleteDialogBinding binding;
    private RegisterProductViewModel viewModel;

    public static final String TAG = SuccessfulDeleteDialogFragment.class.getSimpleName();

    public static SuccessfulDeleteDialogFragment newInstance() {
        SuccessfulDeleteDialogFragment fragment = new SuccessfulDeleteDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_successful_delete_dialog,
                null,
                false);

        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getDismissSuccessfulDeleteDialogSingleLiveEvent().setValue(true);
                dismiss();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }
}