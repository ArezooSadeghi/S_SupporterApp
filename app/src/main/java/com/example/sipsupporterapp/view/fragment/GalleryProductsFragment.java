package com.example.sipsupporterapp.view.fragment;

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

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentGalleryProductsBinding;
import com.example.sipsupporterapp.model.CustomerProducts;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

public class GalleryProductsFragment extends Fragment {
    private FragmentGalleryProductsBinding mBinding;
    private RegisterProductViewModel mViewModel;

    private int mCustomerID, mCustomerProductID;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";

    public static GalleryProductsFragment newInstance(int customerID, int customerProductID) {
        GalleryProductsFragment fragment = new GalleryProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCustomerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        mCustomerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);

        mViewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_gallery_products, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}