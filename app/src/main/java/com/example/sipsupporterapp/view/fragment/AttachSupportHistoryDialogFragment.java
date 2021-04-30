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
import com.example.sipsupporterapp.databinding.FragmentAttachSupportHistoryDialogBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.SupportHistoryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AttachSupportHistoryDialogFragment extends DialogFragment {
    private FragmentAttachSupportHistoryDialogBinding binding;
    private SupportHistoryViewModel viewModel;
    private Uri uri;
    private Bitmap bitmap;
    private String fileName;
    private File file;
    private int customerID, customerSupportID;
    private int numberOfRotate = 0;

    private static final int SELECT_PICTURE_REQUEST_CODE = 0;
    private static final int TAKE_PICTURE_REQUEST_CODE = 1;

    private static final String AUTHORITY = "com.example.sipsupporterapp.fileProvider";

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_SUPPORT_ID = "customerSupportID";

    public static final String TAG = AttachSupportHistoryDialogFragment.class.getSimpleName();

    public static AttachSupportHistoryDialogFragment newInstance(int customerID, int customerSupportID) {
        AttachSupportHistoryDialogFragment fragment = new AttachSupportHistoryDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_SUPPORT_ID, customerSupportID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        customerSupportID = getArguments().getInt(ARGS_CUSTOMER_SUPPORT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(SupportHistoryViewModel.class);

        viewModel.getAllowPermissionSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean allowedPermission) {
                openCamera();
            }
        });

        viewModel.getFileDataSingleLiveEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String fileData) {
                File file = new File(uri.getPath());
                fileName = file.getName();

                AttachInfo attachInfo = new AttachInfo();
                attachInfo.setCustomerID(customerID);
                attachInfo.setCustomerSupportID(customerSupportID);
                attachInfo.setFileData(fileData);
                attachInfo.setFileName(fileName);
                attachInfo.setDescription(binding.edTextDescription.getText().toString());

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceAttach(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.attach(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfo);
            }
        });

        viewModel.getAttachResultSingleLiveEvent().observe(this, new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                binding.progressBar.setVisibility(View.GONE);
                SuccessfulAttachSupportHistoryDialogFragment fragment = SuccessfulAttachSupportHistoryDialogFragment.newInstance().newInstance();
                fragment.show(getActivity().getSupportFragmentManager(), SuccessfulAttachSupportHistoryDialogFragment.TAG);
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

        viewModel.getDismissClickedSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                AttachAgainSupportHistoryDialogFragment fragment = AttachAgainSupportHistoryDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), AttachAgainSupportHistoryDialogFragment.TAG);
            }
        });

        viewModel.getNoAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                dismiss();
            }
        });

        viewModel.getYesAgain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                uri = null;
                binding.img.setImageResource(0);
                binding.edTextDescription.setText("");
            }
        });

        viewModel.getTimeoutExceptionHappenSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.progressBar.setVisibility(View.GONE);
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance("اتصال به اینترنت با خطا مواجه شد");
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_attach_support_history_dialog,
                null,
                false);

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

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

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