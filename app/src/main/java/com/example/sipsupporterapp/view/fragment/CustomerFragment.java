package com.example.sipsupporterapp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.CustomerAdapter;
import com.example.sipsupporterapp.databinding.FragmentCustomerBinding;
import com.example.sipsupporterapp.model.CustomerInfo;
import com.example.sipsupporterapp.model.CustomerParameter;
import com.example.sipsupporterapp.model.CustomerResult;
import com.example.sipsupporterapp.model.DateResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ItemClickedContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.SharedCenterNameDialogAndCustomerViewModel;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {
    private FragmentCustomerBinding binding;
    private SharedCenterNameDialogAndCustomerViewModel viewModel;


    public static CustomerFragment newInstance() {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedCenterNameDialogAndCustomerViewModel.class);

        ServerData serverData = viewModel
                .getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetDateResult(
                serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchDateResult(SipSupportSharedPreferences.getUserLoginKey(getContext()));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_customer,
                container,
                false);


        initViews();
        setListener();

        if (SipSupportSharedPreferences.getLastSearchQuery(getContext()) != null) {
            binding.progressBarLoading.setVisibility(View.VISIBLE);
            ServerData serverData = viewModel
                    .getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
            viewModel.getSupportServicePostCustomerParameter(
                    serverData.getIpAddress() + ":" + serverData.getPort());
            viewModel.fetchCustomerResult(
                    SipSupportSharedPreferences.getUserLoginKey(getContext()), SipSupportSharedPreferences.getLastSearchQuery(getContext()));
        }

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }


    private void setObserver() {
        viewModel.getCustomerResultSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerResult>() {
                    @Override
                    public void onChanged(CustomerResult customerResult) {

                        StringBuilder stringBuilder = new StringBuilder();
                        String listSize = String.valueOf(customerResult.getCustomers().length);

                        for (int i = 0; i < listSize.length(); i++) {
                            stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                        }

                        binding.txtCount.setText("تعداد مراکز: " + stringBuilder.toString());
                        binding.progressBarLoading.setVisibility(View.GONE);
                        binding.recyclerViewCustomerList.setVisibility(View.VISIBLE);
                        List<CustomerInfo> customerInfoList = new ArrayList<>();
                        for (CustomerInfo customerInfo : customerResult.getCustomers()) {
                            customerInfoList.add(customerInfo);
                        }
                        setupAdapter(customerInfoList);
                    }
                });

        viewModel.getErrorSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        viewModel.getTimeoutExceptionHappenSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isTimeOutExceptionHappen) {
                        binding.progressBarLoading.setVisibility(View.GONE);
                        ErrorDialogFragment fragment = ErrorDialogFragment
                                .newInstance("اتصال به اینترنت با خطا مواجه شد");
                        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getDangerousUserSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        viewModel.getItemClickedSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer customerID) {
                        Intent intent = ItemClickedContainerActivity.newIntent(getContext(), customerID);
                        startActivity(intent);
                    }
                });

        viewModel.getDateResultSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<DateResult>() {
                    @Override
                    public void onChanged(DateResult dateResult) {
                        SipSupportSharedPreferences.setDate(getContext(), dateResult.getDate());
                    }
                });
        viewModel.getShowProgressBarSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.progressBarLoading.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initViews() {
        String userName = Converter.convert(SipSupportSharedPreferences.getUserFullName(getContext()));
        binding.txtUserName.setText(userName);
        binding.recyclerViewCustomerList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustomerList.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewCustomerList.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setListener() {
        binding.imgViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(getContext())
                        .addItem(new PowerMenuItem("تغییر رمز عبور"))
                        .addItem(new PowerMenuItem("خروج از حساب کاربری"))
                        .setTextColor(Color.parseColor("#000000"))
                        .setTextGravity(Gravity.RIGHT)
                        .build();

                powerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        switch (position) {
                            case 0:
                                ChangePasswordDialogFragment changePasswordDialogFragment =
                                        ChangePasswordDialogFragment.newInstance();
                                changePasswordDialogFragment.show(
                                        getActivity().getSupportFragmentManager(), ChangePasswordDialogFragment.TAG);
                                powerMenu.dismiss();
                                return;
                            case 1:
                                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                                Intent intent = LoginContainerActivity.newIntent(getContext());
                                startActivity(intent);
                                getActivity().finish();
                                powerMenu.dismiss();
                                return;
                        }
                    }
                });
                powerMenu.showAsDropDown(view);
            }
        });

        int searchPlateId = binding.searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) binding.searchView.findViewById(searchPlateId);
        searchPlate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchPlate.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    if (v.getText().toString().isEmpty()) {
                        int customerID = 0;
                        SipSupportSharedPreferences.setLastSearchQuery(getContext(), "");
                        CustomerParameter customerParameter = new CustomerParameter(
                                customerID, "");
                        binding.progressBarLoading.setVisibility(View.VISIBLE);

                        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        viewModel.getSupportServicePostCustomerParameter(serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.fetchCustomerResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), "");
                    } else {
                        int customerID = 0;
                        SipSupportSharedPreferences.setLastSearchQuery(getContext(), v.getText().toString());
                        CustomerParameter customerParameter = new CustomerParameter(
                                customerID, v.getText().toString());

                        binding.progressBarLoading.setVisibility(View.VISIBLE);

                        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        viewModel.getSupportServicePostCustomerParameter(serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.fetchCustomerResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), v.getText().toString());
                    }
                }
                return false;
            }
        });
    }


    private void setupAdapter(List<CustomerInfo> customerInfoList) {
        CustomerAdapter adapter = new CustomerAdapter(
                getContext(),
                customerInfoList, viewModel, SipSupportSharedPreferences.getDate(getContext()));
        binding.recyclerViewCustomerList.setAdapter(adapter);
    }
}