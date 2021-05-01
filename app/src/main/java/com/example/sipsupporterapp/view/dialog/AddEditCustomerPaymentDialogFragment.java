package com.example.sipsupporterapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentAddEditCustomerPaymentDialogBinding;
import com.example.sipsupporterapp.model.BankAccountInfo;
import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.model.CustomerPaymentResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditCustomerPaymentDialogFragment extends DialogFragment {
    private FragmentAddEditCustomerPaymentDialogBinding binding;
    private DepositAmountsViewModel viewModel;

    private String bankName, description, value;
    private long price;
    private int datePayment, customerID, customerPaymentID;
    private Map<String, Integer> map = new HashMap<>();
    private boolean isAdd;

    private static final String ARGS_BANK_NAME = "bankName";
    private static final String ARGS_DESCRIPTION = "description";
    private static final String ARGS_PRICE = "price";
    private static final String ARGS_DATE_PAYMENT = "datePayment";
    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_IS_ADD = "isAdd";
    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    public static final String TAG = AddEditCustomerPaymentDialogFragment.class.getSimpleName();

    public static AddEditCustomerPaymentDialogFragment newInstance(
            String bankName,
            String description,
            long price,
            int datePayment,
            int customerID,
            boolean isAdd,
            int customerPaymentID) {
        AddEditCustomerPaymentDialogFragment fragment = new AddEditCustomerPaymentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_BANK_NAME, bankName);
        args.putString(ARGS_DESCRIPTION, description);
        args.putLong(ARGS_PRICE, price);
        args.putInt(ARGS_DATE_PAYMENT, datePayment);
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putBoolean(ARGS_IS_ADD, isAdd);
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);

        bankName = getArguments().getString(ARGS_BANK_NAME);
        description = getArguments().getString(ARGS_DESCRIPTION);
        price = getArguments().getLong(ARGS_PRICE);
        datePayment = getArguments().getInt(ARGS_DATE_PAYMENT);
        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        isAdd = getArguments().getBoolean(ARGS_IS_ADD);
        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetBankAccountResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()));

        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_add_edit_customer_payment_dialog,
                null,
                false);

        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));
        binding.edTextDescription.setText(description);

        binding.btnDepositDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                if (String.valueOf(monthOfYear + 1).length() == 1 & String.valueOf(dayOfMonth).length() == 1) {
                                    datePayment = Integer.valueOf(year + "0" + (monthOfYear + 1) + "0" + dayOfMonth);
                                    binding.btnDepositDate.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else if (String.valueOf(monthOfYear + 1).length() == 1) {
                                    datePayment = Integer.valueOf(year + "0" + (monthOfYear + 1) + dayOfMonth);
                                    binding.btnDepositDate.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + dayOfMonth);
                                } else if (String.valueOf(dayOfMonth).length() == 1) {
                                    datePayment = Integer.valueOf(year + (monthOfYear + 1) + "0" + dayOfMonth);
                                    binding.btnDepositDate.setText(year + "/" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else {
                                    datePayment = Integer.valueOf(String.valueOf(year + (monthOfYear + 1) + dayOfMonth));
                                    binding.btnDepositDate.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                }
                            }
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay());
                datePickerDialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        binding.edTextDepositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.edTextDepositAmount.removeTextChangedListener(this);
                try {
                    String text = editable.toString();
                    Long textToLong;
                    if (text.contains(",")) {
                        text = text.replaceAll(",", "");
                    }
                    textToLong = Long.parseLong(text);
                    NumberFormat numberFormat = new DecimalFormat("#,###");
                    String currencyFormat = numberFormat.format(textToLong);
                    binding.edTextDepositAmount.setText(currencyFormat);
                    binding.edTextDepositAmount.setSelection(binding.edTextDepositAmount.getText().length());
                } catch (NumberFormatException exception) {
                    Log.e(TAG, exception.getMessage());
                }
                binding.edTextDepositAmount.addTextChangedListener(this);
            }
        });

        binding.edTextDepositAmount.setText(String.valueOf(price));


        binding.spinnerBankName.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                value = (String) item;
            }
        });

        if (datePayment != 0) {
            String date = String.valueOf(datePayment);
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6);
            String dateFormat = year + "/" + month + "/" + day;
            binding.btnDepositDate.setText(dateFormat);
        } else {
            binding.btnDepositDate.setText(SipSupportSharedPreferences.getDate(getContext()));
        }

        setListener();

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }

    private void setListener() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerPaymentInfo customerPaymentInfo = new CustomerPaymentInfo();
                customerPaymentInfo.setDescription(binding.edTextDescription.getText().toString());

                String text = binding.edTextDepositAmount.getText().toString();
                String newText = text.replace(",", "");
                long price = Long.valueOf(newText);
                customerPaymentInfo.setPrice(price);

                int bankAccountID = map.get(value);
                customerPaymentInfo.setBankAccountID(bankAccountID);

                String bankAccountName = value;
                customerPaymentInfo.setBankAccountName(bankAccountName);

                if (datePayment == 0) {
                    String date = binding.btnDepositDate.getText().toString();
                    date = date.replaceAll("/", "");
                    customerPaymentInfo.setDatePayment(Integer.valueOf(date));
                } else {
                    customerPaymentInfo.setDatePayment(datePayment);
                }

                customerPaymentInfo.setCustomerID(customerID);
                customerPaymentInfo.setCustomerPaymentID(customerPaymentID);

                if (isAdd) {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServiceAddCustomerPayments(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.addCustomerPaymentsResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentInfo);
                } else {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServiceEditCustomerPayments(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.editCustomerPaymentsResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentInfo);
                }
            }
        });
    }

    private void setObserver() {
        viewModel.getBankAccountResultSingleLiveEvent().observe(this, new Observer<BankAccountResult>() {
            @Override
            public void onChanged(BankAccountResult bankAccountResult) {
                for (BankAccountInfo bankAccountInfo : bankAccountResult.getBankAccounts()) {
                    map.put(bankAccountInfo.getBankAccountName(), bankAccountInfo.getBankAccountID());
                }
                setupSpinner(bankAccountResult.getBankAccounts());
            }
        });

        viewModel.getErrorBankAccountResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnection().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDangerousUser) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getAddCustomerPaymentsSingleLiveEvent().observe(this, new Observer<CustomerPaymentResult>() {
            @Override
            public void onChanged(CustomerPaymentResult customerPaymentResult) {
                SuccessfulAddCustomerPaymentDialogFragment fragment = SuccessfulAddCustomerPaymentDialogFragment.newInstance("پرداختی با موفقیت ثبت شد");
                fragment.show(getParentFragmentManager(), SuccessfulAddCustomerPaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorAddCustomerPaymentSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDismissSuccessfulAddCustomerPaymentsSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                dismiss();
            }
        });

        viewModel.getEditCustomerPaymentsSingleLiveEvent().observe(this, new Observer<CustomerPaymentResult>() {
            @Override
            public void onChanged(CustomerPaymentResult customerPaymentResult) {
                SuccessfulAddCustomerPaymentDialogFragment fragment = SuccessfulAddCustomerPaymentDialogFragment.newInstance("پرداختی با موفقیت ثبت شد");
                fragment.show(getParentFragmentManager(), SuccessfulAddCustomerPaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorEditCustomerPaymentSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }

    private void setupSpinner(BankAccountInfo[] bankAccountInfoArray) {
        List<String> bankAccountNameList = new ArrayList<>();
        for (BankAccountInfo bankAccountInfo : bankAccountInfoArray) {
            bankAccountNameList.add(bankAccountInfo.getBankAccountName());
        }
        if (!bankName.isEmpty()) {
            value = bankName;
            bankAccountNameList.remove(bankName);
            bankAccountNameList.add(0, bankName);
            binding.spinnerBankName.setItems(bankAccountNameList);
        } else {
            value = bankAccountNameList.get(0);
            binding.spinnerBankName.setItems(bankAccountNameList);
        }
    }
}