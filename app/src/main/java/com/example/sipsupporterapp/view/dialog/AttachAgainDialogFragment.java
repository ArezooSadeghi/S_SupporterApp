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
import com.example.sipsupporterapp.databinding.FragmentAttachAgainDialogBinding;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

public class AttachAgainDialogFragment extends DialogFragment {
    public static final String TAG = AttachAgainDialogFragment.class.getSimpleName();
    private RegisterProductViewModel viewModel;
    private FragmentAttachAgainDialogBinding binding;

    public static AttachAgainDialogFragment newInstance() {
        AttachAgainDialogFragment fragment = new AttachAgainDialogFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_attach_again_dialog, null, false);

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getYesAttachAgainSuccessfulDialogSingleLiveEvent().setValue(true);
                viewModel.getYesAttachAgainProductFragmentSingleLiveEvent().setValue(true);
                dismiss();

            }
        });

        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getNoAttachAgainProductFragmentSingleLiveEvent().setValue(true);
                viewModel.getNoAttachAgainSuccessfulDialogSingleLiveEvent().setValue(true);
                dismiss();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(binding.getRoot()).create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}