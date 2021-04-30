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
import com.example.sipsupporterapp.adapter.ProductsAdapter;
import com.example.sipsupporterapp.databinding.FragmentProductsBinding;
import com.example.sipsupporterapp.model.CustomerProductResult;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.Converter;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.ImageListContainerActivity;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private FragmentProductsBinding binding;
    private RegisterProductViewModel viewModel;

    private int customerID, customerProductID;

    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static ProductsFragment newInstance(int customerID) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);

        ServerData serverData = viewModel
                .getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetCustomerProductResult(
                serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchProductResult(
                SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_products,
                container,
                false);

        String customerName = Converter.convert(SipSupportSharedPreferences.getCustomerName(getContext()));
        binding.txtCustomerName.setText(customerName);
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
        binding.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewProducts.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewProducts.getContext(),
                DividerItemDecoration.VERTICAL));
    }


    private void setListener() {
        binding.fabAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterProductFragment fragment = RegisterProductFragment.newInstance(
                        customerID,
                        "",
                        "",
                        0,
                        false,
                        false, true, 0, 0);
                fragment.show(getParentFragmentManager(), RegisterProductFragment.TAG);
            }
        });
    }


    private void setObserver() {
        viewModel.getCustomerProductResultSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerProductResult>() {
                    @Override
                    public void onChanged(CustomerProductResult productResult) {
                        binding.progressBarLoading.setVisibility(View.GONE);
                        binding.recyclerViewProducts.setVisibility(View.VISIBLE);

                        StringBuilder stringBuilder = new StringBuilder();
                        String listSize = String.valueOf(productResult.getCustomerProducts().length);

                        for (int i = 0; i < listSize.length(); i++) {
                            stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                        }

                        binding.txtCountProducts.setText("تعداد محصولات: " + stringBuilder.toString());
                        setupAdapter(productResult.getCustomerProducts());
                    }
                });

        viewModel.getErrorCustomerProductResultSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
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

        viewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDangerousUser) {
                SipSupportSharedPreferences.setUserLoginKey(getContext(), null);
                SipSupportSharedPreferences.setUserFullName(getContext(), null);
                SipSupportSharedPreferences.setCustomerUserId(getContext(), 0);
                SipSupportSharedPreferences.setCustomerName(getContext(), null);
                SipSupportSharedPreferences.setLastSearchQuery(getContext(), null);
                SipSupportSharedPreferences.setCustomerTel(getContext(), null);
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getDeleteClickedSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer customerProductID1) {
                        customerProductID = customerProductID1;
                        DeleteQuestionDialogFragment fragment = DeleteQuestionDialogFragment.newInstance();
                        fragment.show(getActivity().getSupportFragmentManager(), DeleteQuestionDialogFragment.TAG);
                    }
                });

        viewModel.getYesDeleteSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean yesDelete) {
                        ServerData serverData = viewModel
                                .getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        viewModel.getSipSupportServiceForDeleteCustomerProduct(
                                serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.deleteCustomerProduct(
                                SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProductID);
                    }
                });

        viewModel.getDeleteCustomerProductSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerProductResult>() {
                    @Override
                    public void onChanged(CustomerProductResult customerProductResult) {
                        SuccessfulDeleteDialogFragment fragment = SuccessfulDeleteDialogFragment.newInstance();
                        fragment.show(getActivity().getSupportFragmentManager(), SuccessfulDeleteDialogFragment.TAG);
                    }
                });

        viewModel.getErrorDeleteCustomerProductSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String error) {
                        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                        fragment.show(getActivity().getSupportFragmentManager(), ErrorDialogFragment.TAG);
                    }
                });

        viewModel.getDismissSuccessfulDeleteDialogSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean dismissed) {
                        ServerData serverData = viewModel.getServerData(
                                SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        viewModel.getSipSupportServiceGetCustomerProductResult(
                                serverData.getIpAddress() + ":" + serverData.getPort());
                        viewModel.fetchProductResult(
                                SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
                    }
                });

        viewModel.getEditClickedSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerProducts>() {
                    @Override
                    public void onChanged(CustomerProducts customerProducts) {
                        RegisterProductFragment fragment = RegisterProductFragment.newInstance(
                                customerID,
                                customerProducts.getProductName(),
                                customerProducts.getDescription(),
                                customerProducts.getInvoicePrice(),
                                customerProducts.isInvoicePayment(),
                                customerProducts.isFinish(),
                                false,
                                customerProducts.getCustomerProductID(),
                                customerProducts.getExpireDate());
                        fragment.show(getActivity().getSupportFragmentManager(), RegisterProductFragment.TAG);
                    }
                });

        viewModel.getProductsFragmentDialogDismissSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dialogDismissed) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceGetCustomerProductResult(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.fetchProductResult(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerID);
            }
        });

        viewModel.getProductAdapterSeeDocumentsClickedSingleLiveEvent()
                .observe(getViewLifecycleOwner(), new Observer<CustomerProducts>() {
                    @Override
                    public void onChanged(CustomerProducts customerProducts) {
                        Intent intent = ImageListContainerActivity.newIntent(
                                getContext(),
                                customerProducts.getCustomerID(),
                                0,
                                customerProducts.getCustomerProductID(), 0);
                        startActivity(intent);
                    }
                });
    }

    private void setupAdapter(CustomerProducts[] customerProductsArray) {
        List<CustomerProducts> customerProductsList = new ArrayList<>();
        for (CustomerProducts products : customerProductsArray) {
            customerProductsList.add(products);
        }

        ProductsAdapter adapter = new ProductsAdapter(getContext(), customerProductsList, viewModel);
        binding.recyclerViewProducts.setAdapter(adapter);
    }
}