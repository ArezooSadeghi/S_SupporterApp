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
import com.example.sipsupporterapp.databinding.FragmentAttachAgainSupportHistoryDialogBinding;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;

public class AttachAgainSupportHistoryDialogFragment extends DialogFragment {
    private FragmentAttachAgainSupportHistoryDialogBinding binding;
    private SupportHistoryViewModel viewModel;

    public static final String TAG = AttachAgainSupportHistoryDialogFragment.class.getSimpleName();


    public static AttachAgainSupportHistoryDialogFragment newInstance() {
        AttachAgainSupportHistoryDialogFragment fragment = new AttachAgainSupportHistoryDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SupportHistoryViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_attach_again_support_history_dialog, null, false);

        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getNoAgain().setValue(true);
                dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getYesAgain().setValue(true);
                dismiss();
            }
        });


        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}