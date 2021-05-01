package com.example.sipsupporterapp.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentFullScreenImageDialogBinding;
import com.example.sipsupporterapp.viewmodel.GalleryViewModel;

public class FullScreenImageDialogFragment extends DialogFragment {
    private FragmentFullScreenImageDialogBinding binding;
    private String fileData;
    private GalleryViewModel viewModel;

    private static final String ARGS_FILE_DATA = "fileData";

    public static final String TAG = FullScreenImageDialogFragment.class.getSimpleName();

    public static FullScreenImageDialogFragment newInstance(String fileData) {
        FullScreenImageDialogFragment fragment = new FullScreenImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_FILE_DATA, fileData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fileData = getArguments().getString(ARGS_FILE_DATA);

        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                R.layout.fragment_full_screen_image_dialog,
                null,
                false);

        byte[] decodedString = Base64.decode(fileData.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        binding.imageView.setImage(ImageSource.bitmap(decodedByte));

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(binding.getRoot())
                .create();
        return dialog;
    }
}