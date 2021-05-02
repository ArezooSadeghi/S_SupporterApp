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
import com.example.sipsupporterapp.databinding.FragmentQuestionDialogBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;

public class DeleteServerDataDialogFragment extends DialogFragment {
    private FragmentQuestionDialogBinding binding;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;

    private static final String ARGS_MESSAGE = "message";
    private static final String ARGS_SERVER_DATA = "serverData";

    public static final String TAG = DeleteServerDataDialogFragment.class.getSimpleName();

    public static DeleteServerDataDialogFragment newInstance(String message, ServerData serverData) {
        DeleteServerDataDialogFragment fragment = new DeleteServerDataDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_MESSAGE, message);
        args.putSerializable(ARGS_SERVER_DATA, serverData);
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
                R.layout.fragment_question_dialog,
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
        viewModel = new ViewModelProvider(requireActivity()).get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);
    }

    private void initViews() {
        String message = getArguments().getString(ARGS_MESSAGE);
        binding.txtMessage.setText(message);
    }

    private void handleEvents() {
        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerData serverData = (ServerData) getArguments().getSerializable(ARGS_SERVER_DATA);
                viewModel.getYesDeleteIPAddressList().setValue(serverData);
            }
        });
    }
}