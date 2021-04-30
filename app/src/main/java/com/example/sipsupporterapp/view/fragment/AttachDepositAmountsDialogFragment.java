package com.example.sipsupporterapp.view.fragment;

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
import com.example.sipsupporterapp.databinding.FragmentAttachDepositAmountsDialogBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.GalleryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AttachDepositAmountsDialogFragment extends DialogFragment {
    private FragmentAttachDepositAmountsDialogBinding binding;
    private GalleryViewModel viewModel;
    /*private DepositAmountsViewModel viewModel;*/
    private int customerID, customerPaymentID;
    private Uri uri;
    private String fileName;
    private File file, mPictureFile;
    private int numberOfRotate = 0;
    private Bitmap bitmap;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_PAYMENT_ID = "customerPaymentID";
    private static final String AUTHORITY = "com.example.sipsupporterapp.fileProvider";

    private static final int SELECT_PICTURE_REQUEST_CODE = 0;
    private static final int TAKE_PICTURE_REQUEST_CODE = 1;

    public static final String TAG = AttachDepositAmountsDialogFragment.class.getSimpleName();

    public static AttachDepositAmountsDialogFragment newInstance(int customerID, int customerPaymentID) {
        AttachDepositAmountsDialogFragment fragment = new AttachDepositAmountsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_PAYMENT_ID, customerPaymentID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        customerPaymentID = getArguments().getInt(ARGS_CUSTOMER_PAYMENT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        /*viewModel = new ViewModelProvider(requireActivity()).get(DepositAmountsViewModel.class);*/

        setObserver();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_attach_deposit_amounts_dialog,
                null,
                false);

        setListener();


        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        return dialog;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                binding.img.setImageBitmap(bitmap);

            } else if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
                uri = FileProvider.getUriForFile(
                        getActivity(), AUTHORITY, file);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;

                if (imageWidth > imageHeight) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    binding.img.setImageBitmap(bitmap);
                } else {
                    binding.img.setImageBitmap(bitmap);
                }

                getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setObserver() {
        viewModel.getAllowPermissionSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAllowedPermission) {
                openCamera();
            }
        });

        viewModel.getNoConnection().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTimeOutExceptionHappen) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getErrorAttachResultSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(error);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });

        viewModel.getAttachResultSingleLiveEvent().observe(this, new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                viewModel.getAttachOkSingleLiveEvent().setValue(true);
                binding.progressBar.setVisibility(View.GONE);
                SuccessfulAttachDepositAmountsDialogFragment fragment = SuccessfulAttachDepositAmountsDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), SuccessfulAttachDepositAmountsDialogFragment.TAG);
            }
        });

        viewModel.getFileDataSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String fileData) {
                File file = new File(uri.getPath());
                fileName = file.getName();
                AttachInfo attachInfo = new AttachInfo();
                attachInfo.setCustomerID(customerID);
                attachInfo.setCustomerPaymentID(customerPaymentID);
                attachInfo.setFileData(fileData);
                attachInfo.setFileName(fileName);
                attachInfo.setDescription(binding.edTextDescription.getText().toString());

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceAttach(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.attach(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfo);
            }
        });

        viewModel.getDialogDismissedSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDismissed) {
                AttachAgainDepositAmountsDialogFragment fragment = AttachAgainDepositAmountsDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), AttachAgainDepositAmountsDialogFragment.TAG);
            }
        });

        viewModel.getNoAttachAgainSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNoAttachAgain) {
                dismiss();
            }
        });

        viewModel.getYesAttachAgainSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isYesAttachAgain) {
                uri = null;
                binding.img.setImageResource(0);
                binding.edTextDescription.setText("");
            }
        });
    }


    private void setListener() {
        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "انتخاب تصویر"), SELECT_PICTURE_REQUEST_CODE);
            }
        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("هنوز فایلی انتخاب نشده است");
                    fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.getFileDataSingleLiveEvent().postValue(convertBitmapToString(bitmap));
                        }
                    }).start();
                }
            }
        });

        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions()) {
                    openCamera();
                } else {
                    viewModel.getRequestPermissionSingleLiveEvent().setValue(true);
                }
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
    }


    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void openCamera() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File filesDir = getActivity().getFilesDir();
            fileName = "img_" + new Date().getTime() + ".jpg";
            file = new File(filesDir, fileName);

            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        AUTHORITY,
                        file);

                List<ResolveInfo> activities =
                        getActivity().getPackageManager()
                                .queryIntentActivities(
                                        takePhotoIntent,
                                        PackageManager.MATCH_DEFAULT_ONLY
                                );

                for (ResolveInfo activity : activities) {
                    getActivity().grantUriPermission(
                            activity.activityInfo.packageName,
                            photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                }

                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePhotoIntent, TAKE_PICTURE_REQUEST_CODE);
            }
        }
    }




    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        System.gc();

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
}