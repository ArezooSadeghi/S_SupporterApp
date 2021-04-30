package com.example.sipsupporterapp.view.fragment;

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
import com.example.sipsupporterapp.databinding.FragmentAddPaymentDialogBinding;
import com.example.sipsupporterapp.model.BankAccountInfo;
import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.PaymentInfo;
import com.example.sipsupporterapp.model.PaymentResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPaymentDialogFragment extends DialogFragment {
    private FragmentAddPaymentDialogBinding binding;
    private WithdrawViewModel viewModel;

    private String lastValueSpinner;
    private Map<String, Integer> map = new HashMap<>();
    private String paymentSubject;

    private int paymentID, datePayment, paymentSubjectID;
    private String description, bankAccountName;
    private long price;
    private boolean isAdd;

    private static final String ARGS_PAYMENT_ID = "paymentID";
    private static final String ARGS_DESCRIPTION = "description";
    private static final String ARGS_DATE_PAYMENT = "datePayment";
    private static final String ARGS_PRICE = "price";
    private static final String ARGS_BANK_ACCOUNT_NAME = "bankAccountName";
    private static final String ARGS_PAYMENT_SUBJECT_ID = "paymentSubjectID";
    private static final String ARGS_IS_ADD = "isAdd";

    public static final String TAG = AddPaymentDialogFragment.class.getSimpleName();

    public static AddPaymentDialogFragment newInstance(int paymentID, String description, int datePayment, long price, String bankAccountName, int paymentSubjectID, boolean isAdd) {
        AddPaymentDialogFragment fragment = new AddPaymentDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PAYMENT_ID, paymentID);
        args.putString(ARGS_DESCRIPTION, description);
        args.putInt(ARGS_DATE_PAYMENT, datePayment);
        args.putLong(ARGS_PRICE, price);
        args.putString(ARGS_BANK_ACCOUNT_NAME, bankAccountName);
        args.putInt(ARGS_PAYMENT_SUBJECT_ID, paymentSubjectID);
        args.putBoolean(ARGS_IS_ADD, isAdd);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paymentID = getArguments().getInt(ARGS_PAYMENT_ID);
        description = getArguments().getString(ARGS_DESCRIPTION);
        datePayment = getArguments().getInt(ARGS_DATE_PAYMENT);
        price = getArguments().getLong(ARGS_PRICE);
        bankAccountName = getArguments().getString(ARGS_BANK_ACCOUNT_NAME);
        paymentSubjectID = getArguments().getInt(ARGS_PAYMENT_SUBJECT_ID);
        isAdd = getArguments().getBoolean(ARGS_IS_ADD);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);

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
                R.layout.fragment_add_payment_dialog,
                null,
                false);

        handleClicked();
        initViews();
        setItemSelectedListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    private void setObserver() {
        viewModel.getAddPaymentDialogBankAccountResultSingleLiveEvent().observe(this, new Observer<BankAccountResult>() {
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

        viewModel.getPaymentResultPaymentsAddSingleLiveEvent().observe(this, new Observer<PaymentResult>() {
            @Override
            public void onChanged(PaymentResult paymentResult1) {
                SuccessAddPaymentDialogFragment fragment = SuccessAddPaymentDialogFragment.newInstance(paymentResult1.getPayments()[0].getBankAccountID());
                fragment.show(getParentFragmentManager(), SuccessAddPaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorPaymentResultPaymentsAddSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getSuccessDialogDismissSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDismiss) {
                dismiss();
            }
        });

        viewModel.getPaymentResultPaymentsEditSingleLiveEvent().observe(this, new Observer<PaymentResult>() {
            @Override
            public void onChanged(PaymentResult paymentResult) {
                SuccessAddPaymentDialogFragment fragment = SuccessAddPaymentDialogFragment.newInstance(paymentResult.getPayments()[0].getBankAccountID());
                fragment.show(getParentFragmentManager(), SuccessAddPaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorPaymentResultPaymentsEditSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getSubjectSingleLiveEvent().observe(this, new Observer<HashMap<Integer, String>>() {
            @Override
            public void onChanged(HashMap<Integer, String> hashMap) {
                Collection<String> subjectPayment = hashMap.values();
                List<String> collectionToList = new ArrayList<>(subjectPayment);

                binding.btnWhat.setText(collectionToList.get(0));
                paymentSubject = collectionToList.get(0);
                for (Integer key : hashMap.keySet()) {
                    paymentSubjectID = key;
                    break;
                }
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
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });
    }


    private void setupSpinner(BankAccountInfo[] bankAccountInfoArray) {
        List<String> bankAccountNameList = new ArrayList<>();
        for (BankAccountInfo bankAccountInfo : bankAccountInfoArray) {
            bankAccountNameList.add(bankAccountInfo.getBankAccountName());
        }

        if (!bankAccountName.isEmpty()) {
            lastValueSpinner = bankAccountName;
            bankAccountNameList.remove(bankAccountName);
            bankAccountNameList.add(0, bankAccountName);
            binding.spinnerBankNames.setItems(bankAccountNameList);
        } else {
            lastValueSpinner = bankAccountNameList.get(0);
            binding.spinnerBankNames.setItems(bankAccountNameList);
        }
    }


    private void handleClicked() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentInfo paymentInfo = new PaymentInfo();

                String bankAccountName = lastValueSpinner;
                paymentInfo.setBankAccountName(bankAccountName);

                int bankAccountID = map.get(lastValueSpinner);
                paymentInfo.setBankAccountID(bankAccountID);

                String description = binding.edTextDescription.getText().toString();
                paymentInfo.setDescription(description);

                String paymentSubject = AddPaymentDialogFragment.this.paymentSubject;
                paymentInfo.setPaymentSubject(paymentSubject);

                String price = binding.edTextInvoicePrice.getText().toString().replaceAll(",", "");
                paymentInfo.setPrice(Long.valueOf(price));

                paymentInfo.setPaymentID(paymentID);
                paymentInfo.setPaymentSubjectID(paymentSubjectID);

                String date = binding.btnDatePayment.getText().toString().replaceAll("/", "");
                paymentInfo.setDatePayment(Integer.valueOf(date));

                if (isAdd) {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServicePaymentsAdd(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.paymentsAdd(SipSupportSharedPreferences.getUserLoginKey(getContext()), paymentInfo);
                } else {
                    ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                    viewModel.getSipSupportServicePaymentsEdit(serverData.getIpAddress() + ":" + serverData.getPort());
                    viewModel.paymentEdit(SipSupportSharedPreferences.getUserLoginKey(getContext()), paymentInfo);
                }
            }
        });

        binding.btnDatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                if (String.valueOf(monthOfYear + 1).length() == 1 & String.valueOf(dayOfMonth).length() == 1) {
                                    binding.btnDatePayment.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else if (String.valueOf(monthOfYear + 1).length() == 1) {
                                    binding.btnDatePayment.setText(year + "/" + "0" + (monthOfYear + 1) + "/" + dayOfMonth);
                                } else if (String.valueOf(dayOfMonth).length() == 1) {
                                    binding.btnDatePayment.setText(year + "/" + (monthOfYear + 1) + "/" + "0" + dayOfMonth);
                                } else {
                                    binding.btnDatePayment.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                }
                            }
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay());
                datePickerDialog.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        binding.btnShowSubjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectListDialogFragment fragment = SubjectListDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), SubjectListDialogFragment.TAG);
            }
        });

        binding.edTextInvoicePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.edTextInvoicePrice.removeTextChangedListener(this);
                try {
                    String text = editable.toString();
                    Long textToLong;
                    if (text.contains(",")) {
                        text = text.replaceAll(",", "");
                    }
                    textToLong = Long.parseLong(text);
                    NumberFormat numberFormat = new DecimalFormat("#,###");
                    String currencyFormat = numberFormat.format(textToLong);
                    binding.edTextInvoicePrice.setText(currencyFormat);
                    binding.edTextInvoicePrice.setSelection(binding.edTextInvoicePrice.getText().length());
                } catch (NumberFormatException exception) {
                    Log.e(TAG, exception.getMessage());
                }
                binding.edTextInvoicePrice.addTextChangedListener(this);
            }
        });
    }


    private void initViews() {
        binding.edTextDescription.setText(description);
        binding.edTextDescription.setSelection(binding.edTextDescription.getText().length());

        if (datePayment == 0) {
            binding.btnDatePayment.setText(SipSupportSharedPreferences.getDate(getContext()));
        } else {
            String date = String.valueOf(datePayment);
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6);
            String dateFormat = year + "/" + month + "/" + day;
            binding.btnDatePayment.setText(dateFormat);
        }

        binding.edTextInvoicePrice.setText(String.valueOf(price));
        binding.edTextInvoicePrice.setSelection(binding.edTextInvoicePrice.getText().length());
    }


    private void setItemSelectedListener() {
        binding.spinnerBankNames.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                lastValueSpinner = (String) item;
            }
        });
    }
}