package com.example.sipsupporterapp.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EnterIPAddressDialogFragment extends DialogFragment {

    public static final String TAG = EnterIPAddressDialogFragment.class.getSimpleName();


    public static EnterIPAddressDialogFragment newInstance() {
        EnterIPAddressDialogFragment fragment = new EnterIPAddressDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setMessage("لطفا آدرس ip خود را وارد کنید")
                .setPositiveButton("تایید", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddAndEditIPAddressDialogFragment fragment = AddAndEditIPAddressDialogFragment.newInstance("", "", "");
                        fragment.setCancelable(false);
                        fragment.show(getActivity().getSupportFragmentManager(), AddAndEditIPAddressDialogFragment.TAG);
                        dismiss();
                    }
                })
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }
}