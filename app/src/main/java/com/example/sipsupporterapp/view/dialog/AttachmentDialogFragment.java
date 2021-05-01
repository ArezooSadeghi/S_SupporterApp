package com.example.sipsupporterapp.view.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentAttachmentDialogBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.activity.LoginContainerActivity;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AttachmentDialogFragment extends DialogFragment {
    private FragmentAttachmentDialogBinding binding;
    private AttachmentViewModel viewModel;

    private File photoFile;
    private Uri uri;
    private Bitmap bitmap;
    private int numberOfRotate = 0;

    private int customerID, customerSupportID, customerProductID, customerPaymentID;

    private static final int REQUEST_CODE_PICK_PHOTO = 0;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_SUPPORT_ID = "customerSupportID";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";
    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";

    private static final String AUTHORITY = "com.example.sipsupporterapp.fileProvider";

    public static final String TAG = AttachmentDialogFragment.class.getSimpleName();

    public static AttachmentDialogFragment newInstance(int customerID, int customerSupportID, int customerProductID, int customerPaymentID) {
        AttachmentDialogFragment fragment = new AttachmentDialogFragment();
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

        createViewModel();

        setObserver();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_attachment_dialog,
                null,
                false);

        handleEvents();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                uri = data.getData();
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        binding.img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                uri = FileProvider.getUriForFile(
                        getActivity(), AUTHORITY, photoFile);
                if (uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        if (bitmap != null) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);

                            int imageHeight = options.outHeight;
                            int imageWidth = options.outWidth;

                            options.inJustDecodeBounds = false;

                            if (imageWidth > imageHeight) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                binding.img.setImageBitmap(bitmap);
                            } else {
                                binding.img.setImageBitmap(bitmap);
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    private void setObserver() {
        viewModel.getAllowPermissionSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAllowPermission) {
                openCamera();
            }
        });

      /*  viewModel.getBitmapAsStringSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String bitmapAsString) {
                File file = new File(uri.getPath());
                String fileName = file.getName();
                AttachInfo attachInfo = new AttachInfo();
                attachInfo.setCustomerID(customerID);
                attachInfo.setCustomerSupportID(customerSupportID);
                attachInfo.setCustomerProductID(customerProductID);
                attachInfo.setCustomerPaymentID(customerPaymentID);
                attachInfo.setFileData(bitmapAsString);
                attachInfo.setFileName(fileName);
                attachInfo.setDescription(binding.edTextDescription.getText().toString());

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceAttach(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.attach(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfo);
            }
        });*/

        viewModel.getAttachResultSingleLiveEvent().observe(this, new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                viewModel.getUpdateImageListSingleLiveEvent().setValue(attachResult);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.imgClose.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgRotate.setEnabled(true);
                binding.imgMore.setEnabled(true);
                SuccessAttachmentDialogFragment fragment = SuccessAttachmentDialogFragment.newInstance("ارسال فایل موفقیت آمیز بود");
                fragment.show(getParentFragmentManager(), SuccessAttachmentDialogFragment.TAG);
            }
        });

        viewModel.getErrorAttachResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.imgClose.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgRotate.setEnabled(true);
                binding.imgMore.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getNoConnectionSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.imgClose.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgRotate.setEnabled(true);
                binding.imgMore.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeOutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.progressBarLoading.setVisibility(View.GONE);
                binding.imgClose.setEnabled(true);
                binding.imgSend.setEnabled(true);
                binding.imgCamera.setEnabled(true);
                binding.edTextDescription.setEnabled(true);
                binding.imgRotate.setEnabled(true);
                binding.imgMore.setEnabled(true);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getDangerousUserSingleLiveEvent().observe(this, new Observer<Boolean>() {
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

        viewModel.getShowAttachAgainDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showAttachAgainDialog) {
                AttachmentAgainDialogFragment fragment = AttachmentAgainDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), AttachmentAgainDialogFragment.TAG);
            }
        });

        viewModel.getNoAttachAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNoAgain) {
                dismiss();
            }
        });

        viewModel.getYesAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isYesAgain) {
                uri = null;
                binding.img.setImageResource(0);
                binding.edTextDescription.setText("");
            }
        });
    }

    private void handleEvents() {
        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraPermission()) {
                    openCamera();
                } else {
                    viewModel.getRequestPermission().setValue(true);
                }
            }
        });

        binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "انتخاب تصویر"), REQUEST_CODE_PICK_PHOTO);
            }
        });

        binding.imgRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("هنوز فایلی انتخاب نشده است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    switch (numberOfRotate) {
                        case 0:
                            Matrix matrix = new Matrix();
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            binding.img.setImageBitmap(bitmap);
                            numberOfRotate++;
                            return;
                        case 1:
                            Matrix matrix1 = new Matrix();
                            matrix1.postRotate(180);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix1, true);
                            binding.img.setImageBitmap(bitmap);
                            numberOfRotate++;
                            return;
                        case 2:
                            Matrix matrix2 = new Matrix();
                            matrix2.postRotate(270);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, true);
                            binding.img.setImageBitmap(bitmap);
                            numberOfRotate = 0;
                            return;
                    }
                }
            }
        });

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("هنوز فایلی انتخاب نشده است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    AttachmentAsyncTask attachmentAsyncTask = new AttachmentAsyncTask();
                    attachmentAsyncTask.execute(bitmap);
                }
            }
        });
    }

    private boolean hasCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File filesDir = getActivity().getFilesDir();
            String fileName = "img_" + new Date().getTime() + ".jpg";
            photoFile = new File(filesDir, fileName);

            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(getContext(),
                        AUTHORITY,
                        photoFile);

                List<ResolveInfo> activities =
                        getActivity().getPackageManager()
                                .queryIntentActivities(
                                        intent,
                                        PackageManager.MATCH_DEFAULT_ONLY
                                );

                for (ResolveInfo activity : activities) {
                    getActivity().grantUriPermission(
                            activity.activityInfo.packageName,
                            uri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private String convertBitmapToStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        System.gc();

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public class AttachmentAsyncTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            binding.progressBarLoading.setVisibility(View.VISIBLE);
            binding.imgClose.setEnabled(false);
            binding.imgSend.setEnabled(false);
            binding.imgCamera.setEnabled(false);
            binding.edTextDescription.setEnabled(false);
            binding.imgRotate.setEnabled(false);
            binding.imgMore.setEnabled(false);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            String stringBase64 = convertBitmapToStringBase64(bitmap);
            return stringBase64;
        }

        @Override
        protected void onPostExecute(String bitmapAsString) {
            File file = new File(uri.getPath());
            String fileName = file.getName();
            AttachInfo attachInfo = new AttachInfo();
            attachInfo.setCustomerID(customerID);
            attachInfo.setCustomerSupportID(customerSupportID);
            attachInfo.setCustomerProductID(customerProductID);
            attachInfo.setCustomerPaymentID(customerPaymentID);
            attachInfo.setFileData(bitmapAsString);
            attachInfo.setFileName(fileName);
            attachInfo.setDescription(binding.edTextDescription.getText().toString());

            String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
            String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
            ServerData serverData = viewModel.getServerData(centerName);
            viewModel.getSipSupportServiceAttach(serverData.getIpAddress() + ":" + serverData.getPort());
            viewModel.attach(userLoginKey, attachInfo);
        }
    }
}
