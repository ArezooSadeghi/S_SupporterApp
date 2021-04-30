package com.example.sipsupporterapp.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.GalleryAdapter;
import com.example.sipsupporterapp.databinding.FragmentGalleryBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private GalleryViewModel viewModel;

    private int customerID, customerPaymentID;

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 0;

    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";
    private static final String ARGS_CUSTOMER_ID = "customerID";

    public static GalleryFragment newInstance(int customerID, int customerPaymentID) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);
        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
        viewModel.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.getAttachmentFilesViaCustomerPaymentID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentID, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false);

        binding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));

        binding.fabAddNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttachDepositAmountsDialogFragment fragment = AttachDepositAmountsDialogFragment.newInstance(customerID, customerPaymentID);
                fragment.show(getParentFragmentManager(), AttachDepositAmountsDialogFragment.TAG);
            }
        });

        initRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PERMISSION:
                if (grantResults == null || grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getAllowPermissionSingleLiveEvent().setValue(true);
                }
        }
    }

    private void setObserver() {
        viewModel.getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                StringBuilder stringBuilder = new StringBuilder();
                String listSize = String.valueOf(attachResult.getAttachs().length);

                for (int i = 0; i < listSize.length(); i++) {
                    stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
                }

                binding.txtCountAttachmentFiles.setText("تعداد فایل ها: " + stringBuilder.toString());
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.recyclerViewAttachmentFiles.setVisibility(View.VISIBLE);
                setupAdapter(attachResult.getAttachs());
            }
        });

        viewModel.getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        viewModel.getNoConnection().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
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
                Intent intent = LoginContainerActivity.newIntent(getContext());
                startActivity(intent);
                getActivity().finish();
            }
        });

        viewModel.getRequestPermissionSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRequestPermission) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            }
        });

        viewModel.getAttachOkSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isOkAttached) {
                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.getAttachmentFilesViaCustomerPaymentID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentID, true);
            }
        });

        viewModel.getPhotoClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachInfo>() {
            @Override
            public void onChanged(AttachInfo attachInfo) {
                FullScreenImageDialogFragment fragment = FullScreenImageDialogFragment.newInstance(attachInfo.getFileData());
                fragment.show(getParentFragmentManager(), FullScreenImageDialogFragment.TAG);
            }
        });
    }

    private void initRecyclerView() {
        binding.recyclerViewAttachmentFiles.setLayoutManager(new GridLayoutManager(getContext(), 3));

        binding.recyclerViewAttachmentFiles.addItemDecoration(new DividerItemDecoration(
                binding.recyclerViewAttachmentFiles.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void setupAdapter(AttachInfo[] attachInfoArray) {
        List<AttachInfo> attachInfoList = new ArrayList<>();
        for (AttachInfo attachInfo : attachInfoArray) {
            attachInfoList.add(attachInfo);
        }

        GalleryAdapter adapter = new GalleryAdapter(getContext(), attachInfoList, viewModel);
        binding.recyclerViewAttachmentFiles.setAdapter(adapter);
    }
}