package com.example.sipsupporterapp.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.adapter.AttachmentAdapter;
import com.example.sipsupporterapp.databinding.FragmentImageListBinding;
import com.example.sipsupporterapp.eventbus.UpdateEvent;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.view.dialog.AttachmentDialogFragment;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.FullScreenImageDialogFragment;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {
    private FragmentImageListBinding mBinding;
    private AttachmentViewModel mViewModel;

    private int customerID, customerSupportID, customerProductID, customerPaymentID;

    private static final int SPAN_COUNT = 3;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 0;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_SUPPORT_ID = "customerSupportID";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    public static final String TAG = ImageListFragment.class.getSimpleName();

    private List<Bitmap> oldBitmapList = new ArrayList<>();
    private List<Bitmap> newBitmapList = new ArrayList<>();
    private List<AttachInfo> attachInfoList = new ArrayList<>();

    private boolean flag = false;

    private AttachmentAdapter adapter;

    private int index = 0;

    public static ImageListFragment newInstance(int customerID, int customerSupportID, int customerProductID, int customerPaymentID) {
        ImageListFragment fragment = new ImageListFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_SUPPORT_ID, customerSupportID);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        customerSupportID = getArguments().getInt(ARGS_CUSTOMER_SUPPORT_ID);
        customerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);
        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);

        mViewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_image_list, container, false);

        initViews();
        setListener();

        if (checkPermission()) {
            if (customerSupportID != 0) {
                ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerSupportID(serverData.getIpAddress() + ":" + serverData.getPort());
                mViewModel.fetchFileWithCustomerSupportID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerSupportID, false);
            } else if (customerProductID != 0) {
                ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerProductID(serverData.getIpAddress() + ":" + serverData.getPort());
                mViewModel.fetchFileWithCustomerProductID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProductID, false);
            } else if (customerPaymentID != 0) {
                ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(serverData.getIpAddress() + ":" + serverData.getPort());
                mViewModel.getAttachmentFilesViaCustomerPaymentID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentID, false);
            }
        } else {
            requestPermission();
        }

        return mBinding.getRoot();
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
                    mViewModel.getAllowPermissionSingleLiveEvent().setValue(true);
                }
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults == null || grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (customerSupportID != 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerSupportID(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchFileWithCustomerSupportID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerSupportID, false);
                    } else if (customerProductID != 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerProductID(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchFileWithCustomerProductID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerProductID, false);
                    } else if (customerPaymentID != 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.getAttachmentFilesViaCustomerPaymentID(SipSupportSharedPreferences.getUserLoginKey(getContext()), customerPaymentID, false);
                    }
                } else {
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اجازه دسترسی داده نشد");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                }
        }
    }

    private void initViews() {
        mBinding.txtCustomerName.setText(SipSupportSharedPreferences.getCustomerName(getContext()));
        mBinding.recyclerViewAttachmentFiles.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    private void setListener() {
        mBinding.fabAddNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttachmentDialogFragment fragment = AttachmentDialogFragment.newInstance(customerID, customerSupportID, customerProductID, customerPaymentID);
                fragment.show(getParentFragmentManager(), AttachmentDialogFragment.TAG);
            }
        });
    }

    private void setObserver() {
        mViewModel.getRequestPermission().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRequestPermission) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            }
        });

        mViewModel.getGetAttachmentFilesViaCustomerSupportIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs().length == 0) {
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    readFile(attachResult.getAttachs());
                    if (oldBitmapList.size() != 0) {
                        mBinding.progressBarLoading.setVisibility(View.GONE);
                        mBinding.recyclerViewAttachmentFiles.setVisibility(View.VISIBLE);
                        String count = convertEnDigitToPer(String.valueOf(oldBitmapList.size()));
                        mBinding.txtCountAttachmentFiles.setText("تعداد فایل ها: " + count);
                        setupAdapter(oldBitmapList);
                    }
                    if (attachInfoList.size() != 0 & index == 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfoList.get(index).getAttachID(), true);
                    }
                }
            }
        });

        mViewModel.getGetErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getGetAttachmentFilesViaCustomerProductIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs().length == 0) {
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    readFile(attachResult.getAttachs());
                    if (oldBitmapList.size() != 0) {
                        mBinding.progressBarLoading.setVisibility(View.GONE);
                        mBinding.recyclerViewAttachmentFiles.setVisibility(View.VISIBLE);
                        String count = convertEnDigitToPer(String.valueOf(oldBitmapList.size()));
                        mBinding.txtCountAttachmentFiles.setText("تعداد فایل ها: " + count);
                        setupAdapter(oldBitmapList);
                    }
                    if (attachInfoList.size() != 0 & index == 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfoList.get(index).getAttachID(), true);
                    }
                }
            }
        });

        mViewModel.getGetErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs().length == 0) {
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    readFile(attachResult.getAttachs());
                    if (oldBitmapList.size() != 0) {
                        mBinding.progressBarLoading.setVisibility(View.GONE);
                        mBinding.recyclerViewAttachmentFiles.setVisibility(View.VISIBLE);
                        String count = convertEnDigitToPer(String.valueOf(oldBitmapList.size()));
                        mBinding.txtCountAttachmentFiles.setText("تعداد فایل ها: " + count);
                        setupAdapter(oldBitmapList);
                    }
                    if (attachInfoList.size() != 0 & index == 0) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfoList.get(index).getAttachID(), true);
                    }
                }
            }
        });

        mViewModel.getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getAttachResultViaAttachIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (checkPermission()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (attachResult.getAttachs()[0].getFileData() != null) {
                                    mViewModel.getDoneWriteFilesSingleLiveEvent().postValue(writeFile(attachResult.getAttachs()));
                                } else {
                                    index++;
                                    if (index < attachInfoList.size()) {
                                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                                        mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                                        mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfoList.get(index).getAttachID(), true);
                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mBinding.progressBarLoading.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage(), e);
                            }
                        }
                    }).start();
                } else {
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.getErrorAttachResultViaAttachIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getDoneWriteFilesSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachInfo[]>() {
            @Override
            public void onChanged(AttachInfo[] attachInfoArray) {
                if (attachInfoArray[0].getFileData() != null) {
                    if (newBitmapList.size() == 0) {
                        oldBitmapList.add(read(attachInfoArray));
                        newBitmapList.addAll(oldBitmapList);
                    } else {
                        oldBitmapList.addAll(newBitmapList);
                        newBitmapList.add(read(attachInfoArray));
                    }
                    String count = convertEnDigitToPer(String.valueOf(newBitmapList.size()));
                    mBinding.txtCountAttachmentFiles.setText("تعداد فایل ها: " + count);
                    mBinding.progressBarLoading.setVisibility(View.GONE);
                    mBinding.recyclerViewAttachmentFiles.setVisibility(View.VISIBLE);
                    setupAdapter(oldBitmapList);
                    index++;
                    if (index < attachInfoList.size()) {
                        ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                        mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                        mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfoList.get(index).getAttachID(), true);
                    }
                }
            }
        });

        mViewModel.getAttachResultForImageListSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mViewModel.getDoneWriteFilesSingleLiveEvent().postValue(writeFile(attachResult.getAttachs()));
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }).start();
            }
        });

        mViewModel.getNoConnectionSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                mBinding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        mViewModel.getDangerousUserSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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

        mViewModel.getAttachmentAdapterItemClickedSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mViewModel.getShowFullScreenSingleLiveEvent().postValue(encoded);
                    }
                }).start();
            }
        });

        mViewModel.getShowFullScreenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String fileData) {
                FullScreenImageDialogFragment fragment = FullScreenImageDialogFragment.newInstance(fileData);
                fragment.show(getParentFragmentManager(), FullScreenImageDialogFragment.TAG);
            }
        });

        mViewModel.getUpdateImageListSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                ServerData serverData = mViewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                mViewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
                mViewModel.fetchWithAttachID(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachResult.getAttachs()[0].getAttachID(), true);
            }
        });
    }

    private AttachInfo[] writeFile(AttachInfo[] attachInfoArray) throws IOException {
        for (AttachInfo attachInfo : attachInfoArray) {
            File fileDir = new File(Environment.getExternalStorageDirectory(), "Attachments");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File file = new File(fileDir, attachInfo.getAttachID() + ".jpg");

            if (attachInfo.getFileData() != null) {
                byte[] stringAsBytes = Base64.decode(attachInfo.getFileData(), 0);
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(stringAsBytes);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        return attachInfoArray;
    }

    private void setupAdapter(List<Bitmap> bitmaps) {
        if (adapter == null) {
            adapter = new AttachmentAdapter(getContext(), bitmaps, mViewModel);
            mBinding.recyclerViewAttachmentFiles.setAdapter(adapter);
        } else {
            adapter.updateBitmaps(newBitmapList);
            mBinding.recyclerViewAttachmentFiles.setAdapter(adapter);
        }
    }

    private String convertEnDigitToPer(String enDigit) {
        StringBuilder stringBuilder = new StringBuilder();
        String listSize = String.valueOf(enDigit);

        for (int i = 0; i < listSize.length(); i++) {
            stringBuilder.append((char) ((int) listSize.charAt(i) - 48 + 1632));
        }

        return stringBuilder.toString();
    }

    private List<Bitmap> readFile(AttachInfo[] attachInfoArray) {
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (file.exists()) {
            File[] files = file.listFiles();

            if (files.length == 0) {
                for (AttachInfo attachInfo : attachInfoArray) {
                    attachInfoList.add(attachInfo);
                }
            } else {
                for (AttachInfo attachInfo : attachInfoArray) {
                    for (File f : files) {
                        if (f.getName().equals(attachInfo.getAttachID() + ".jpg")) {
                            flag = true;
                            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                            oldBitmapList.add(bitmap);
                            mViewModel.getBitmapListSingleLiveEvent().setValue(oldBitmapList);
                            break;
                        }
                    }
                    if (!flag) {
                        attachInfoList.add(attachInfo);
                    } else {
                        flag = false;
                    }
                }
            }
        } else {
            for (AttachInfo attachInfo : attachInfoArray) {
                attachInfoList.add(attachInfo);
            }
        }
        return oldBitmapList;
    }

    private Bitmap read(AttachInfo[] attachInfoArray) {
        AttachInfo attachInfo = attachInfoArray[0];
        Bitmap bitmap = null;
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        File[] files = file.listFiles();

        for (File f : files) {
            if (f.getName().equals(attachInfo.getAttachID() + ".jpg")) {
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                break;
            }
        }
        return bitmap;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
    }
}