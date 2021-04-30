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
import com.example.sipsupporterapp.databinding.FragmentSuccessfulAttachDepositAmountsDialogBinding;
import com.example.sipsupporterapp.viewmodel.GalleryViewModel;

public class SuccessfulAttachDepositAmountsDialogFragment extends DialogFragment {
    private FragmentSuccessfulAttachDepositAmountsDialogBinding binding;
    /* private DepositAmountsViewModel viewModel;*/
    private GalleryViewModel viewModel;

    public static final String TAG = SuccessfulAttachDepositAmountsDialogFragment.class.getSimpleName();

    public static SuccessfulAttachDepositAmountsDialogFragment newInstance() {
        SuccessfulAttachDepositAmountsDialogFragment fragment = new SuccessfulAttachDepositAmountsDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);*/
        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_successful_attach_deposit_amounts_dialog,
                null,
                false);

        setListener();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
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
                viewModel.getDialogDismissedSingleLiveEvent().setValue(true);
                dismiss();
            }
        });
    }
}