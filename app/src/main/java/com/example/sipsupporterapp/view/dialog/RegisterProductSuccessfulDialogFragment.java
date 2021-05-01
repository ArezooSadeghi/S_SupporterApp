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
import com.example.sipsupporterapp.databinding.FragmentRegisterProductSuccessfulDialogBinding;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

public class RegisterProductSuccessfulDialogFragment extends DialogFragment {
    private FragmentRegisterProductSuccessfulDialogBinding binding;
    private RegisterProductViewModel viewModel;
    ;

    public static final String TAG = RegisterProductSuccessfulDialogFragment.class.getSimpleName();

    public static RegisterProductSuccessfulDialogFragment newInstance() {
        RegisterProductSuccessfulDialogFragment fragment = new RegisterProductSuccessfulDialogFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_register_product_successful_dialog, null, false);
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();

        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getDialogDismissSingleLiveEvent().setValue(true);
                viewModel.getProductsFragmentDialogDismissSingleLiveEvent().setValue(true);
                dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }
}