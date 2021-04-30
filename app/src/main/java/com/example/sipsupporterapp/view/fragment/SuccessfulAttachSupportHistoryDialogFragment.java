package com.example.sipsupporterapp.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentSuccessfulAttachSupportHistoryDialogBinding;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;

public class SuccessfulAttachSupportHistoryDialogFragment extends DialogFragment {
    private FragmentSuccessfulAttachSupportHistoryDialogBinding binding;
    private SupportHistoryViewModel viewModel;

    public static final String TAG = SuccessfulAttachSupportHistoryDialogFragment.class.getSimpleName();


    public static SuccessfulAttachSupportHistoryDialogFragment newInstance() {
        SuccessfulAttachSupportHistoryDialogFragment fragment = new SuccessfulAttachSupportHistoryDialogFragment();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_successful_attach_support_history_dialog, null, false);

        binding.imgCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getDismissClickedSingleLiveEvent().setValue(true);
                dismiss();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(binding.getRoot()).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        return dialog;
    }
}