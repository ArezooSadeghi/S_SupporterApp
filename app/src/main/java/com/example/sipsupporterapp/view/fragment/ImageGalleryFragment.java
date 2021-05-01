package com.example.sipsupporterapp.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.sipsupporterapp.databinding.FragmentImageGalleryBinding;
import com.example.sipsupporterapp.eventbus.UpdateEvent;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.FullScreenImageContainerActivity;
import com.example.sipsupporterapp.view.dialog.AttachmentDialogFragment;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageGalleryFragment extends Fragment {
    private FragmentImageGalleryBinding binding;
    private AttachmentViewModel viewModel;

    private int customerID, customerSupportID, customerProductID, customerPaymentID, index;
    private List<AttachInfo> attachInfoList = new ArrayList<>();
    private List<Bitmap> oldBitmapList = new ArrayList<>();
    private List<Bitmap> newBitmapList = new ArrayList<>();
    private Map<Bitmap, String> mapBitmap = new HashMap<>();
    private Map<Uri, String> mapUri = new HashMap<>();
    private AttachmentAdapter adapter;
    private boolean flag;

    private static final int PHONE_SPAN_COUNT = 3;
    private static final int TABLET_SPAN_COUNT = 4;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_SUPPORT_ID = "customerSupportID";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    private static final String TAG = ImageGalleryFragment.class.getSimpleName();

    public static ImageGalleryFragment newInstance(int customerID, int customerSupportID, int customerProductID, int customerPaymentID) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_SUPPORT_ID, customerSupportID);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void update(UpdateEvent event) {

        Bitmap bitmap = null;
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File f : files) {
                    if (f.getName().contains(String.valueOf(event.getAttachID()))) {
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        f.delete();
                    }
                }
            }
        }

        List<Bitmap> bitmaps = new ArrayList<>();
        for (Bitmap b : newBitmapList) {
            if (!b.sameAs(bitmap)) {
                bitmaps.add(b);
            }
        }

        adapter.updateBitmaps(bitmaps);
        binding.recyclerViewAttachmentFile.setAdapter(adapter);
        newBitmapList.clear();
        newBitmapList.addAll(bitmaps);

        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        customerSupportID = getArguments().getInt(ARGS_CUSTOMER_SUPPORT_ID);
        customerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);
        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);

        if (hasWriteExternalStoragePermission()) {
            if (customerSupportID != 0) {
                getCustomerSupportAttachmentList();
            } else if (customerProductID != 0) {
                getCustomerProductAttachmentList();
            } else if (customerPaymentID != 0) {
                getCustomerPaymentAttachmentList();
            }
        } else {
            requestWriteExternalStoragePermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_image_gallery,
                container,
                false);

        initViews();
        handleClicked();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
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
                } else {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اجازه دسترسی به دوربین داده نشد");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                }
                break;
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults == null || grantResults.length == 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (customerSupportID != 0) {
                        getCustomerSupportAttachmentList();
                    } else if (customerProductID != 0) {
                        getCustomerProductAttachmentList();
                    } else if (customerPaymentID != 0) {
                        getCustomerPaymentAttachmentList();
                    }
                } else {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اجازه دسترسی به حافظه داده نشد");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                }
                break;
        }
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    private boolean hasWriteExternalStoragePermission() {
        return (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestWriteExternalStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    private void getCustomerSupportAttachmentList() {
        String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getSipSupportServiceGetAttachmentFilesViaCustomerSupportID(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchFileWithCustomerSupportID(userLoginKey, customerSupportID, false);
    }

    private void getCustomerProductAttachmentList() {
        String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getSipSupportServiceGetAttachmentFilesViaCustomerProductID(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchFileWithCustomerProductID(userLoginKey, customerProductID, false);
    }

    private void getCustomerPaymentAttachmentList() {
        String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getSipSupportServiceGetAttachmentFilesViaCustomerPaymentID(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.getAttachmentFilesViaCustomerPaymentID(userLoginKey, customerPaymentID, false);
    }

    private void setupObserver() {
        viewModel.getRequestPermission().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRequestPermission) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            }
        });

        viewModel.getGetAttachmentFilesViaCustomerSupportIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs() != null & attachResult.getAttachs().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < attachResult.getAttachs().length; i++) {
                        Bitmap bitmap = readFromStorage(attachResult.getAttachs()[i]);
                        if (bitmap != null) {
                            binding.progressBarLoading.setVisibility(View.GONE);
                            binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                            setupAdapter();
                        }
                    }

                    if (index < attachInfoList.size()) {
                        getAttachInfo(attachInfoList.get(index).getAttachID());
                    }
                }
            }
        });

        viewModel.getGetErrorAttachmentFilesViaCustomerSupportIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getAttachResultViaAttachIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                ImageGalleryAsyncTask imageGalleryAsyncTask = new ImageGalleryAsyncTask();
                imageGalleryAsyncTask.execute(attachResult.getAttachs()[0]);
            }
        });

        viewModel.getErrorAttachResultViaAttachIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectionSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getGetAttachmentFilesViaCustomerProductIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs() != null & attachResult.getAttachs().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < attachResult.getAttachs().length; i++) {
                        Bitmap bitmap = readFromStorage(attachResult.getAttachs()[i]);
                        if (bitmap != null) {
                            binding.progressBarLoading.setVisibility(View.GONE);
                            binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                            setupAdapter();
                        }
                    }

                    if (index < attachInfoList.size()) {
                        getAttachInfo(attachInfoList.get(index).getAttachID());
                    }
                }
            }
        });

        viewModel.getGetErrorAttachmentFilesViaCustomerProductIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getGetAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (attachResult.getAttachs() != null & attachResult.getAttachs().length == 0) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < attachResult.getAttachs().length; i++) {
                        Bitmap bitmap = readFromStorage(attachResult.getAttachs()[i]);
                        if (bitmap != null) {
                            binding.progressBarLoading.setVisibility(View.GONE);
                            binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                            setupAdapter();
                        }
                    }

                    if (index < attachInfoList.size()) {
                        getAttachInfo(attachInfoList.get(index).getAttachID());
                    }
                }
            }
        });

        viewModel.getGetErrorAttachmentFilesViaCustomerPaymentIDSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getUpdateImageListSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                getAttachInfo(attachResult.getAttachs()[0].getAttachID());
            }
        });

        viewModel.getShowFullScreenImage().observe(getViewLifecycleOwner(), new Observer<Map<Uri, String>>() {
            @Override
            public void onChanged(Map<Uri, String> map) {
                Uri uri = null;
                String imageName = "";
                for (Map.Entry<Uri, String> entry : map.entrySet()) {
                    uri = entry.getKey();
                    imageName = entry.getValue();
                }
                imageName = imageName.replace(".jpg", "");
                int attachID = Integer.valueOf(imageName);

                Intent intent = FullScreenImageContainerActivity.newIntent(getContext(), uri, attachID);
                startActivity(intent);
            }
        });
    }

    private void getAttachInfo(int attachID) {
        String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
        String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
        ServerData serverData = viewModel.getServerData(centerName);
        viewModel.getSipSupportServiceGetAttachmentFileViaAttachIDRetrofitInstance(serverData.getIpAddress() + ":" + serverData.getPort());
        viewModel.fetchWithAttachID(userLoginKey, attachID, true);
    }

    private Bitmap readFromStorage(AttachInfo attachInfo) {
        Bitmap bitmap = null;
        File file = new File(Environment.getExternalStorageDirectory(), "Attachments");
        if (file.exists()) {
            File[] files = file.listFiles();

            if (files.length == 0) {
                if (!hasAttachInfo(attachInfo.getAttachID())) {
                    attachInfoList.add(attachInfo);
                }
            } else {
                for (File f : files) {
                    if (f.getName().equals(attachInfo.getAttachID() + ".jpg")) {
                        flag = true;
                        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        if (adapter == null) {
                            oldBitmapList.add(bitmap);
                            newBitmapList.addAll(oldBitmapList);
                        } else {
                            newBitmapList.add(bitmap);
                        }
                        mapBitmap.put(bitmap, f.getName());
                        mapUri.put(Uri.fromFile(f), f.getName());
                        break;
                    }
                }
                if (!flag) {
                    if (!hasAttachInfo(attachInfo.getAttachID())) {
                        attachInfoList.add(attachInfo);
                    }
                } else {
                    flag = false;
                }
            }
        } else {
            if (!hasAttachInfo(attachInfo.getAttachID())) {
                attachInfoList.add(attachInfo);
            }
        }
        return bitmap;
    }

    private boolean hasAttachInfo(int attachID) {
        for (AttachInfo attachInfo : attachInfoList) {
            if (attachInfo.getAttachID() == attachID) {
                return true;
            }
        }
        return false;
    }

    private AttachInfo writeToStorage(AttachInfo attachInfo) throws IOException {
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
        return attachInfo;
    }

    private void setupAdapter() {
        if (adapter == null) {
            adapter = new AttachmentAdapter(getContext(), oldBitmapList, viewModel);
        } else {
            adapter.updateBitmaps(newBitmapList);
        }
        adapter.setMapBitmap(mapBitmap);
        adapter.setMapUri(mapUri);
        binding.recyclerViewAttachmentFile.setAdapter(adapter);
    }

    private class ImageGalleryAsyncTask extends AsyncTask<AttachInfo, Void, AttachInfo> {

        @Override
        protected AttachInfo doInBackground(AttachInfo... attachInfoArray) {
            AttachInfo attachInfo = null;
            try {
                attachInfo = writeToStorage(attachInfoArray[0]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return attachInfo;
        }

        @Override
        protected void onPostExecute(AttachInfo attachInfo) {
            if (attachInfo != null) {
                Bitmap bitmap = readFromStorage(attachInfo);
                if (bitmap != null) {
                    binding.progressBarLoading.setVisibility(View.GONE);
                    binding.recyclerViewAttachmentFile.setVisibility(View.VISIBLE);
                    setupAdapter();
                }
            }
            index++;
            if (index < attachInfoList.size()) {
                getAttachInfo(attachInfoList.get(index).getAttachID());
            } else {
                binding.progressBarLoading.setVisibility(View.GONE);
            }
        }
    }

    private void initViews() {
        if (isTablet()) {
            binding.recyclerViewAttachmentFile.setLayoutManager(new GridLayoutManager(getContext(), TABLET_SPAN_COUNT));
        } else {
            binding.recyclerViewAttachmentFile.setLayoutManager(new GridLayoutManager(getContext(), PHONE_SPAN_COUNT));
        }
    }

    private void handleClicked() {
        binding.fabAddNewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmentDialogFragment fragment = AttachmentDialogFragment.newInstance(customerID, customerSupportID, customerProductID, customerPaymentID);
                fragment.show(getParentFragmentManager(), AttachmentDialogFragment.TAG);
            }
        });
    }

    public boolean isTablet() {
        boolean xlarge = ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}