package com.example.sipsupporterapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentAddAndEditIPAddressDialogBinding;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel;

import java.util.List;

public class AddAndEditIPAddressDialogFragment extends DialogFragment {
    private FragmentAddAndEditIPAddressDialogBinding binding;
    private SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel viewModel;

    private String centerName, ipAddress, port;
    private boolean flag;

    private static final String ARGS_CENTER_NAME = "centerName";
    private static final String ARGS_IP_ADDRESS = "ipAddress";
    private static final String ARGS_PORT = "port";

    public static final String TAG = AddAndEditIPAddressDialogFragment.class.getSimpleName();


    public static AddAndEditIPAddressDialogFragment newInstance(
            String centerName, String ipAddress, String port) {
        AddAndEditIPAddressDialogFragment fragment = new AddAndEditIPAddressDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_CENTER_NAME, centerName);
        args.putString(ARGS_IP_ADDRESS, ipAddress);
        args.putString(ARGS_PORT, port);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        centerName = getArguments().getString(ARGS_CENTER_NAME);
        ipAddress = getArguments().getString(ARGS_IP_ADDRESS);
        port = getArguments().getString(ARGS_PORT);

        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedLoginAndAddAndEditIPAddressDialogAndIPAddressListDialogViewModel.class);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_add_and_edit_i_p_address_dialog,
                null,
                false);

        binding.edTextCenterName.setText(centerName);
        binding.edTextIpAddress.setText(ipAddress);
        binding.edTextPort.setText(port);

        binding.edTextCenterName.setSelection(binding.edTextCenterName.getText().length());
        binding.edTextIpAddress.setSelection(binding.edTextIpAddress.getText().length());
        binding.edTextPort.setSelection(binding.edTextPort.getText().length());

        setListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        return dialog;
    }


    private void setListener() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                String centerName = binding.edTextCenterName.getText().toString();
                String ipAddress = binding.edTextIpAddress.getText().toString();
                String port = binding.edTextPort.getText().toString();

                if (centerName.isEmpty() || ipAddress.isEmpty() || port.isEmpty()) {
                    ErrorDialogFragment fragment = ErrorDialogFragment
                            .newInstance("لطفا موارد خواسته شده را پر کنید");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else if (ipAddress.length() < 7 || !hasThreeDots(ipAddress)
                        || hasEnglishLetter(ipAddress) || hasEnglishLetter(port)) {
                    ErrorDialogFragment fragment = ErrorDialogFragment
                            .newInstance("فرمت آدرس ip نادرست است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    List<ServerData> serverDataList = viewModel.getServerDataList();
                    if (serverDataList.size() > 0) {
                        for (ServerData serverData : serverDataList) {
                            if (serverData.getCenterName().equals(centerName)) {
                                ErrorDialogFragment fragment = ErrorDialogFragment
                                        .newInstance("نام مرکز تکراری می باشد");
                                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                                flag = true;
                            }
                        }
                    }
                    if (!flag) {
                        String newIpAddress = convertPerToEn(ipAddress, "");
                        String newPort = convertPerToEn(port, "");

                        newIpAddress = newIpAddress.replaceAll(" ", "");
                        newPort = newPort.replaceAll(" ", "");

                        ServerData serverData = new ServerData(centerName, newIpAddress, newPort);

                        if (serverData.getCenterName().equals(SipSupportSharedPreferences
                                .getLastValueSpinner(getContext()))) {
                            SipSupportSharedPreferences
                                    .setLastValueSpinner(getContext(), serverData.getCenterName());
                        }

                        viewModel.insertServerData(serverData);

                        viewModel.getInsertSpinnerSingleLiveEvent().setValue(true);
                        viewModel.getInsertIPAddressListSingleLiveEvent().setValue(true);
                        dismiss();
                    }
                }
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.edTextCenterName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 0 || actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edTextIpAddress.requestFocus();
                }
                return false;
            }
        });

        binding.edTextIpAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 0 || actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edTextPort.requestFocus();
                }
                return false;
            }
        });
    }


    private String convertPerToEn(String ipAddress, String output) {
        for (int i = 0; i < ipAddress.length(); i++) {
            if (ipAddress.charAt(i) == '۱') {
                output += '1';
            } else if (ipAddress.charAt(i) == '۲') {
                output += '2';

            } else if (ipAddress.charAt(i) == '۳') {
                output += '3';

            } else if (ipAddress.charAt(i) == '۴') {
                output += '4';

            } else if (ipAddress.charAt(i) == '۵') {
                output += '5';

            } else if (ipAddress.charAt(i) == '۶') {
                output += '6';

            } else if (ipAddress.charAt(i) == '۷') {
                output += '7';

            } else if (ipAddress.charAt(i) == '۸') {
                output += '8';

            } else if (ipAddress.charAt(i) == '۹') {
                output += '9';

            } else if (ipAddress.charAt(i) == '۰') {
                output += '0';

            } else {
                output += ipAddress.charAt(i);
            }
        }
        return output;
    }


    private boolean hasThreeDots(String ipAddress) {
        int dotNumber = 0;
        char[] chars = ipAddress.toCharArray();
        for (Character character : chars) {
            if (String.valueOf(character).equals(".")) {
                dotNumber++;
            }
        }
        if (dotNumber == 3) {
            return true;
        } else {
            return false;
        }
    }


    private boolean hasEnglishLetter(String ipAddress) {
        String str = "";
        char[] chars = ipAddress.toCharArray();
        for (Character character : chars) {
            if (!String.valueOf(character).equals(".")) {
                str += String.valueOf(character);
            }
        }

        if (str.matches(".*[a-zA-Z]+.*")) {
            return true;
        } else {
            return false;
        }
    }
}