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
import com.example.sipsupporterapp.adapter.CustomerSupportInfoAdapter;
import com.example.sipsupporterapp.databinding.FragmentSupportHistoryBinding;
import com.example.sipsupporterapp.model.CustomerSupportInfo;
import com.example.sipsupporterapp.model.CustomerSupportResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ImageGalleyContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class SupportHistoryFragment extends Fragment {
    private FragmentSupportHistoryBinding binding;
    private SupportHistoryViewModel viewModel;

    private int customerID;

    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static SupportHistoryFragment newInstance(int customerID) {
        SupportHistoryFragment fragment = new SupportHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(SupportHistoryViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_support_history,
                container,
                false);

        initView();

        ServerData serverData = viewModel
                .getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceCustomerSupportResult(
                serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.getCustomerSupportResult(
                SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void initView() {
        String customerName = Converter.convert(SipSupportSharedPreferences.getCustomerName(getContext()));
        binding.txtUserFullName.setText(customerName);
        binding.recyclerViewSupportHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSupportHistory.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewSupportHistory.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setObserver() {
        viewModel.getCustomerSupportResult()
                .observe(getViewLifecycleOwner(), new Observer<CustomerSupportResult>() {
                    @Override
                    public void onChanged(CustomerSupportResult customerSupportResult) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.recyclerViewSupportHistory.setVisibility(View.VISIBLE);

                        StringBuilder stringBuilder = new StringBuilder();
                        String listSize = String.valueOf(customerSupportResult.getCustomerSupports().length);

                        for (int i = 0; i < listSize.length(); i++) {
                            stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                        }


                        binding.txtCount.setText("تعداد پشتیبانی ها: " + stringBuilder.toString());
                        setupAdapter(customerSupportResult.getCustomerSupports());
                    }
                });

        viewModel.getErrorCustomerSupportResult()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String error) {
                        binding.progressBar.setVisibility(View.GONE);
                        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getNoConnection().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isTimeOutExceptionHappen) {
                        binding.progressBar.setVisibility(View.GONE);
                        ErrorDialogFragment fragment = ErrorDialogFragment
                                .newInstance("اتصال به اینترنت با خطا مواجه شد");
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getDangerousUserSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        viewModel.getCustomerSupportInfoAdapterSeeDocumentClickedSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerSupportInfo>() {
                    @Override
                    public void onChanged(CustomerSupportInfo customerSupportInfo) {
                       /* Intent intent = ImageListContainerActivity.newIntent(
                                getContext(),
                                customerSupportInfo.getCustomerID(),
                                customerSupportInfo.getCustomerSupportID(),
                                0,
                                0);

                        startActivity(intent);*/
                        Intent intent = ImageGalleyContainerActivity.newIntent(getContext(), customerSupportInfo.getCustomerID(), customerSupportInfo.getCustomerSupportID(), 0, 0);
                        startActivity(intent);
                    }
                });
    }


    private void setupAdapter(CustomerSupportInfo[] customerSupportInfoArray) {
        List<CustomerSupportInfo> customerSupportInfoList = new ArrayList<>();
        for (CustomerSupportInfo customerSupportInfo : customerSupportInfoArray) {
            customerSupportInfoList.add(customerSupportInfo);
        }

        CustomerSupportInfoAdapter adapter = new CustomerSupportInfoAdapter(
                getContext(), customerSupportInfoList, viewModel);
        binding.recyclerViewSupportHistory.setAdapter(adapter);
    }
}