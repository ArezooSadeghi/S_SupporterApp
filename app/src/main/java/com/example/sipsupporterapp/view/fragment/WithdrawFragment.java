package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.PaymentAdapter;
import com.example.sipsupporterapp.databinding.FragmentWithdrawBinding;
import com.example.sipsupporterapp.model.BankAccountInfo;
import com.example.sipsupporterapp.model.BankAccountResult;
import com.example.sipsupporterapp.model.PaymentInfo;
import com.example.sipsupporterapp.model.PaymentResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ImageGalleyContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.AddPaymentDialogFragment;
import com.example.sipsupporterapp.view.dialog.DeletePaymentQuestionDialogFragment;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.SuccessDeletePaymentDialogFragment;
import com.example.sipsupporterapp.viewmodel.WithdrawViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithdrawFragment extends Fragment {
    private FragmentWithdrawBinding binding;
    private WithdrawViewModel viewModel;

    private int customerID, paymentID;
    private String lastValueSpinner, bankAccountName;
    private Map<String, Integer> map = new HashMap<>();
    private BankAccountInfo[] bankAccountInfos;

    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static WithdrawFragment newInstance(int customerID) {
        WithdrawFragment fragment = new WithdrawFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Arezoo", "onCreateWithdraw");

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(WithdrawViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetBankAccountResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_withdraw,
                container,
                false);

        initViews();
        setItemSelectedListener();
        handleClicked();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void initViews() {
        String userFullName = Converter.convert(SipSupportSharedPreferences.getUserFullName(getContext()));
        binding.txtUserFullName.setText(userFullName);
        binding.recyclerViewWithDraws.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewWithDraws.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewWithDraws.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setItemSelectedListener() {
        binding.spinnerBankAccounts.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                lastValueSpinner = (String) item;

                int bankAccountID = map.get(lastValueSpinner);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePaymentsListByBankAccount(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchPaymentsListByBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()), bankAccountID);

            }
        });
    }


    private void handleClicked() {
        binding.fabAddNewWithDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPaymentDialogFragment fragment = AddPaymentDialogFragment.newInstance(0, "", 0, 0, lastValueSpinner, 0, true);
                fragment.show(getParentFragmentManager(), AddPaymentDialogFragment.TAG);
            }
        });
    }


    private void setObserver() {
        viewModel.getBankAccountResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<BankAccountResult>() {
            @Override
            public void onChanged(BankAccountResult bankAccountResult) {
                viewModel.getAddPaymentDialogBankAccountResultSingleLiveEvent().setValue(bankAccountResult);
                for (BankAccountInfo bankAccountInfo : bankAccountResult.getBankAccounts()) {
                    map.put(bankAccountInfo.getBankAccountName(), bankAccountInfo.getBankAccountID());
                }
                setupSpinner(bankAccountResult.getBankAccounts());

                int bankAccountID = map.get(lastValueSpinner);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePaymentsListByBankAccount(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchPaymentsListByBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()), bankAccountID);
            }
        });

        viewModel.getErrorBankAccountResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getPaymentResultPaymentsListByBankAccountSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PaymentResult>() {
            @Override
            public void onChanged(PaymentResult paymentResult) {
                Log.d("Arezoo", "hi I'm here");
                if (paymentResult.getPayments().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                }
                StringBuilder stringBuilder = new StringBuilder();
                String listSize = String.valueOf(paymentResult.getPayments().length);

                for (int i = 0; i < listSize.length(); i++) {
                    stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                }

                binding.txtCountWithDraw.setText("تعداد برداشت ها: " + stringBuilder.toString());
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewWithDraws.setVisibility(View.VISIBLE);
                setupAdapter(paymentResult.getPayments());
            }
        });

        viewModel.getErrorPaymentResultPaymentsListByBankAccountSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getEditClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PaymentInfo>() {
            @Override
            public void onChanged(PaymentInfo paymentInfo) {
                AddPaymentDialogFragment fragment = AddPaymentDialogFragment.newInstance(paymentInfo.getPaymentID(), paymentInfo.getDescription(), paymentInfo.getDatePayment(), paymentInfo.getPrice(), lastValueSpinner, paymentInfo.getPaymentSubjectID(), false);
                fragment.show(getParentFragmentManager(), AddPaymentDialogFragment.TAG);
            }
        });

        viewModel.getDeleteClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PaymentInfo>() {
            @Override
            public void onChanged(PaymentInfo paymentInfo) {
                paymentID = paymentInfo.getPaymentID();
                DeletePaymentQuestionDialogFragment fragment = DeletePaymentQuestionDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), DeletePaymentQuestionDialogFragment.TAG);
            }
        });

        viewModel.getYesDeleteSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean yesDelete) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePaymentsDelete(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.paymentsDelete(SipSupportSharedPreferences.getUserLoginKey(getContext()), paymentID);
            }
        });

        viewModel.getPaymentResultPaymentsDeleteSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PaymentResult>() {
            @Override
            public void onChanged(PaymentResult paymentResult) {
                SuccessDeletePaymentDialogFragment fragment = SuccessDeletePaymentDialogFragment.newInstance("پرداختی با موفقیت حذف شد");
                fragment.show(getParentFragmentManager(), SuccessDeletePaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorPaymentResultPaymentsDeleteSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getUpdateListSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean updateList) {
                int bankAccountID = map.get(lastValueSpinner);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePaymentsListByBankAccount(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchPaymentsListByBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()), bankAccountID);
            }
        });

        viewModel.getSeeDocumentsClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<PaymentInfo>() {
            @Override
            public void onChanged(PaymentInfo paymentInfo) {
                /*Intent intent = ImageListContainerActivity.newIntent(getContext(), customerID, 0, 0, paymentInfo.getPaymentID());
                startActivity(intent);*/
                Intent intent = ImageGalleyContainerActivity.newIntent(getContext(), customerID, 0, 0, paymentInfo.getPaymentID());
                startActivity(intent);
            }
        });

        viewModel.getUpdatingSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer bankAccountID) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    if (entry.getValue().equals(bankAccountID)) {
                        bankAccountName = entry.getKey();
                    }
                }

                setupSpinner(bankAccountInfos);

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServicePaymentsListByBankAccount(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchPaymentsListByBankAccounts(SipSupportSharedPreferences.getUserLoginKey(getContext()), bankAccountID);
            }
        });

        viewModel.getNoConnection().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
        bankAccountInfos = bankAccountInfoArray;
        List<String> bankAccountNameList = new ArrayList<>();
        for (BankAccountInfo bankAccountInfo : bankAccountInfoArray) {
            bankAccountNameList.add(bankAccountInfo.getBankAccountName());
        }

        if (bankAccountName == null || bankAccountName.isEmpty()) {
            binding.spinnerBankAccounts.setItems(bankAccountNameList);
            lastValueSpinner = (String) binding.spinnerBankAccounts.getItems().get(0);
        } else {
            List<String> newBankAccountNameList = new ArrayList<>();
            for (String bankAccountN : bankAccountNameList) {
                if (!bankAccountN.equals(bankAccountName)) {
                    newBankAccountNameList.add(bankAccountN);
                }
            }

            newBankAccountNameList.add(0, bankAccountName);
            lastValueSpinner = bankAccountName;
            binding.spinnerBankAccounts.setItems(newBankAccountNameList);
        }
    }


    private void setupAdapter(PaymentInfo[] paymentInfoArray) {
        List<PaymentInfo> paymentInfoList = new ArrayList<>();
        for (PaymentInfo paymentInfo : paymentInfoArray) {
            paymentInfoList.add(paymentInfo);
        }
        PaymentAdapter adapter = new PaymentAdapter(getContext(), paymentInfoList, viewModel);
        binding.recyclerViewWithDraws.setAdapter(adapter);
    }
}