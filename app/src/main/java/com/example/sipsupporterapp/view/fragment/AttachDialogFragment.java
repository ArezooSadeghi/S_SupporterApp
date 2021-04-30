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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentAttachDialogBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.viewmodel.RegisterProductViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class AttachDialogFragment extends DialogFragment {
    public static final int TAKE_PICTURE_REQUEST_CODE = 2;
    private FragmentAttachDialogBinding binding;
    private RegisterProductViewModel viewModel;

    private int customerID, customerProductID;
    private String fileData, fileName;
    private File file, pictureFile;
    private Uri uri;
    private int numberOfRotate = 0;

    private Bitmap bitmap, newBitmap;
    private AttachSuccessfulDialogFragment fragment;

    private static final int SELECT_PICTURE_REQUEST_CODE = 0;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 1;

    private static final String ARGS_CUSTOMER_ID = "customerID";
    private static final String ARGS_CUSTOMER_PRODUCT_ID = "customerProductID";

    public static final String AUTHORITY = "com.example.sipsupporterapp.fileProvider";
    public static final String TAG = AttachDialogFragment.class.getSimpleName();


    public static AttachDialogFragment newInstance(int customerID, int customerProductID) {
        AttachDialogFragment fragment = new AttachDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_CUSTOMER_ID, customerID);
        args.putInt(ARGS_CUSTOMER_PRODUCT_ID, customerProductID);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = AttachSuccessfulDialogFragment.newInstance();

        customerID = getArguments().getInt(ARGS_CUSTOMER_ID);
        customerProductID = getArguments().getInt(ARGS_CUSTOMER_PRODUCT_ID);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterProductViewModel.class);

        viewModel.getAttachResultSingleLiveEvent().observe(this, new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                if (fragment == null) {
                    fragment = AttachSuccessfulDialogFragment.newInstance();
                    fragment.show(getActivity().getSupportFragmentManager(), AttachSuccessfulDialogFragment.TAG);
                } else {
                    fragment.show(getActivity().getSupportFragmentManager(), AttachSuccessfulDialogFragment.TAG);
                }

                binding.progressBar.setVisibility(View.GONE);
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

        viewModel.getDismissAttachSuccessfulDialogSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dismissAttachSuccessfulDialog) {
                AttachAgainDialogFragment fragment = AttachAgainDialogFragment.newInstance();
                fragment.show(getParentFragmentManager(), AttachAgainDialogFragment.TAG);
            }
        });

        viewModel.getAllowPermission().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
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
                attachInfo.setCustomerProductID(customerProductID);
                attachInfo.setFileData(fileData);
                attachInfo.setFileName(fileName);
                attachInfo.setDescription(binding.edTextDescription.getText().toString());

                ServerData serverData = viewModel.getServerData(SipSupportSharedPreferences.getLastValueSpinner(getContext()));
                viewModel.getSipSupportServiceAttach(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.attach(SipSupportSharedPreferences.getUserLoginKey(getContext()), attachInfo);
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

        viewModel.getNoAttachAgainProductFragmentSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                dismiss();
            }
        });

        viewModel.getYesAttachAgainProductFragmentSingleLiveEvent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                uri = null;
                binding.img.setImageResource(0);
                fragment = null;
                binding.edTextDescription.setText("");
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_attach_dialog,
                null,
                false);

        setListener();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.img.setImageBitmap(bitmap);

            } else if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
                uri = FileProvider.getUriForFile(
                        getActivity(), AUTHORITY, file);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
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
                    viewModel.getRequestPermission().setValue(true);
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

    public String getBytes(Uri uri) throws IOException {
        InputStream iStream = getActivity().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        System.gc();

        return Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        System.gc();

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
}