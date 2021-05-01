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
import com.example.sipsupporterapp.adapter.CustomerUsersAdapter;
import com.example.sipsupporterapp.databinding.FragmentCustomerUsersBinding;
import com.example.sipsupporterapp.model.CustomerUserResult;
import com.example.sipsupporterapp.model.CustomerUsers;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.CustomerContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.RegisterSupportDialogFragment;
import com.example.sipsupporterapp.viewmodel.CustomerUsersViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerUsersFragment extends Fragment {
    private FragmentCustomerUsersBinding binding;
    private CustomerUsersViewModel viewModel;

    private int customerID;
    private String date;

    private static final String ARGS_CUSTOMER_ID = "customerID";


    public static CustomerUsersFragment newInstance(int customerID) {
        CustomerUsersFragment fragment = new CustomerUsersFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CustomerUsersViewModel.class);
        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetDateResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchDateResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customer_users,
                container,
                false);

        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));

        initRecyclerView();

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceCustomerUserResult(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchCustomerUserResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void initRecyclerView() {
        binding.recyclerViewCustomerUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustomerUsers.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewCustomerUsers.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setObserver() {
        viewModel.getCustomerUserResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<CustomerUserResult>() {
            @Override
            public void onChanged(CustomerUserResult customerUserResult) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerViewCustomerUsers.setVisibility(View.VISIBLE);

                String str = (customerUserResult.getCustomerUsers().length) + "";
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < str.length(); i++) {
                    stringBuilder.append((char) ((int) str.charAt(i) - 48 + 1632));
                }
                binding.txtCount.setText("تعداد کاربران: " + stringBuilder.toString());
                setupAdapter(customerUserResult.getCustomerUsers());
            }
        });

        viewModel.getErrorCustomerUserResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
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

        viewModel.getItemClicked().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer customerID) {
                RegisterSupportDialogFragment fragment = RegisterSupportDialogFragment.newInstance(customerID);
                fragment.show(getActivity().getSupportFragmentManager(), RegisterSupportDialogFragment.TAG);
            }
        });

        viewModel.getDateResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<DateResult>() {
            @Override
            public void onChanged(DateResult dateResult) {
                date = dateResult.getDate();
            }
        });

        viewModel.getSuccessfulRegisterCustomerUsersSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccessfulRegister) {
                Intent intent = CustomerContainerActivity.newIntent(getContext());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setupAdapter(CustomerUsers[] customerUsersArray) {
        List<CustomerUsers> customerUsersList = new ArrayList<>();
        for (CustomerUsers customerUsers1 : customerUsersArray) {
            customerUsersList.add(customerUsers1);
        }

        CustomerUsersAdapter adapter = new CustomerUsersAdapter(getContext(), customerUsersList, viewModel, date);
        binding.recyclerViewCustomerUsers.setAdapter(adapter);
    }
}