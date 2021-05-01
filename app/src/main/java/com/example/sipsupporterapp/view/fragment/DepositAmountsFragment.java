package com.example.sipsupporterapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.sipsupporterapp.adapter.CustomerPaymentInfoAdapter;
import com.example.sipsupporterapp.databinding.FragmentDepositAmountsBinding;
import com.example.sipsupporterapp.model.CustomerPaymentInfo;
import com.example.sipsupporterapp.model.CustomerPaymentResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ImageListContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.AddEditCustomerPaymentDialogFragment;
import com.example.sipsupporterapp.view.dialog.DeleteQuestionCustomerPaymentsDialogFragment;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.SuccessfulDeleteCustomerPaymentDialogFragment;
import com.example.sipsupporterapp.viewmodel.DepositAmountsViewModel;

import java.util.ArrayList;
import java.util.List;

public class DepositAmountsFragment extends Fragment {
    private FragmentDepositAmountsBinding binding;
    private DepositAmountsViewModel viewModel;

    private int customerID, customerPaymentID;

    private static final String ARGS_CUSTOMER_ID = "customerID";


    public static DepositAmountsFragment newInstance(int customerID) {
        DepositAmountsFragment fragment = new DepositAmountsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceCustomerPaymentResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchCustomerPaymentResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_deposit_amounts,
                null,
                false);

        String customerName = Converter.convert(SipSupportSharedPreferences.getCustomerName(getContext()));
        String userName = Converter.convert(SipSupportSharedPreferences.getUserFullName(getContext()));
        binding.txtCustomerName.setText(customerName);
        binding.txtUserName.setText(userName);

        initRecyclerView();
        setListener();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void initRecyclerView() {
        binding.recyclerViewDepositAmounts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewDepositAmounts.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewDepositAmounts.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setListener() {
        binding.fabAddNewCustomerPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditCustomerPaymentDialogFragment fragment =
                        AddEditCustomerPaymentDialogFragment.newInstance(
                                "",
                                "",
                                0,
                                0,
                                customerID,
                                true,
                                0);
                fragment.show(getParentFragmentManager(), AddEditCustomerPaymentDialogFragment.TAG);
            }
        });
    }


    private void setObserver() {
        viewModel.getCustomerPaymentResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerPaymentResult>() {
            @Override
            public void onChanged(CustomerPaymentResult customerPaymentResult) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewDepositAmounts.setVisibility(View.VISIBLE);
                setupAdapter(customerPaymentResult.getCustomerPayments());
            }
        });

        viewModel.getErrorCustomerPaymentResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
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

        viewModel.getSeeDocumentsClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerPaymentInfo>() {
            @Override
            public void onChanged(CustomerPaymentInfo customerPaymentInfo) {
                Intent intent = ImageListContainerActivity.newIntent(
                        getContext(),
                        customerPaymentInfo.getCustomerID(),
                        0,
                        0,
                        customerPaymentInfo.getCustomerPaymentID());
                startActivity(intent);
            }
        });

        viewModel.getDeleteCustomerPaymentsClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerPaymentInfo>() {
            @Override
            public void onChanged(CustomerPaymentInfo customerPaymentInfo) {
                customerPaymentID = customerPaymentInfo.getCustomerPaymentID();
                DeleteQuestionCustomerPaymentsDialogFragment fragment = DeleteQuestionCustomerPaymentsDialogFragment.newInstance(customerPaymentID);
                fragment.show(getParentFragmentManager(), DeleteQuestionCustomerPaymentsDialogFragment.TAG);
            }
        });

        viewModel.getYesDeleteCustomerPaymentsSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean yesDeleteCustomerPayments) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceDeleteCustomerPayments(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.deleteCustomerPayments(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentID);
            }
        });

        viewModel.getDeleteCustomerPaymentsSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerPaymentResult>() {
            @Override
            public void onChanged(CustomerPaymentResult customerPaymentResult) {
                SuccessfulDeleteCustomerPaymentDialogFragment fragment = SuccessfulDeleteCustomerPaymentDialogFragment.newInstance("برداشتی با موفقیت ثبت شد");
                fragment.show(getParentFragmentManager(), SuccessfulDeleteCustomerPaymentDialogFragment.TAG);
            }
        });

        viewModel.getErrorDeleteCustomerPaymentSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getEditCustomerPaymentsClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerPaymentInfo>() {
            @Override
            public void onChanged(CustomerPaymentInfo customerPaymentInfo) {
                AddEditCustomerPaymentDialogFragment fragment = AddEditCustomerPaymentDialogFragment
                        .newInstance(
                                customerPaymentInfo.getBankAccountName(),
                                customerPaymentInfo.getDescription(),
                                customerPaymentInfo.getPrice(),
                                customerPaymentInfo.getDatePayment(),
                                customerID,
                                false,
                                customerPaymentInfo.getCustomerPaymentID());
                fragment.show(getParentFragmentManager(), AddEditCustomerPaymentDialogFragment.TAG);
            }
        });

        viewModel.getUpdateListAddCustomerPaymentSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceCustomerPaymentResult(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchCustomerPaymentResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
            }
        });

        viewModel.getUpdateListDeleteCustomerPaymentSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceCustomerPaymentResult(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchCustomerPaymentResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
            }
        });
    }


    private void setupAdapter(CustomerPaymentInfo[] customerPaymentInfoArray) {
        List<CustomerPaymentInfo> customerPaymentInfoList = new ArrayList<>();
        for (CustomerPaymentInfo customerPaymentInfo : customerPaymentInfoArray) {
            customerPaymentInfoList.add(customerPaymentInfo);
        }
        CustomerPaymentInfoAdapter adapter = new CustomerPaymentInfoAdapter(getContext(), customerPaymentInfoList, viewModel);
        binding.recyclerViewDepositAmounts.setAdapter(adapter);
    }
}