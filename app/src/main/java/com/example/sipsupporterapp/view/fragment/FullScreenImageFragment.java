package com.example.sipsupporterapp.view.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentFullScreenImageBinding;
import com.example.sipsupporterapp.eventbus.UpdateEvent;
import com.example.sipsupporterapp.model.AttachResult;
import com.example.sipsupporterapp.model.ServerData;
import com.example.sipsupporterapp.utils.SipSupportSharedPreferences;
import com.example.sipsupporterapp.view.dialog.ErrorDialogFragment;
import com.example.sipsupporterapp.view.dialog.QuestionDialogFragment;
import com.example.sipsupporterapp.view.dialog.SuccessDeleteFileDialogFragment;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class FullScreenImageFragment extends Fragment {
    private FragmentFullScreenImageBinding binding;
    private AttachmentViewModel viewModel;

    private static final String ARGS_PHOTO = "photo";
    private static final String ARGS_ATTACH_ID = "attachID";

    private static final String TAG = FullScreenImageFragment.class.getSimpleName();

    public static FullScreenImageFragment newInstance(Uri photo, int attachID) {
        FullScreenImageFragment fragment = new FullScreenImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_PHOTO, photo);
        args.putInt(ARGS_ATTACH_ID, attachID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_full_screen_image,
                null,
                false);

        initViews();
        handleEvents();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AttachmentViewModel.class);
    }

    private void initViews() {
        Uri photo = getArguments().getParcelable(ARGS_PHOTO);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photo);
        } catch (IOException exception) {
            Log.e(TAG, exception.getMessage());
        }
        binding.imgViewFullScreen.setImage(ImageSource.bitmap(bitmap));
    }

    private void handleEvents() {
        binding.imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDialogFragment fragment = QuestionDialogFragment.newInstance("آیا می خواهید فایل مربوطه را حذف کنید؟");
                fragment.show(getParentFragmentManager(), QuestionDialogFragment.TAG);
            }
        });
    }

    private void setupObserver() {
        viewModel.getYesDelete().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean yesDelete) {
                String centerName = SipSupportSharedPreferences.getLastValueSpinner(getContext());
                String userLoginKey = SipSupportSharedPreferences.getUserLoginKey(getContext());
                int attachID = getArguments().getInt(ARGS_ATTACH_ID);
                ServerData serverData = viewModel.getServerData(centerName);
                viewModel.getSipSupportServiceDeleteAttach(serverData.getIpAddress() + ":" + serverData.getPort());
                viewModel.deleteAttach(userLoginKey, attachID);
            }
        });

        viewModel.getDeleteAttachResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<AttachResult>() {
            @Override
            public void onChanged(AttachResult attachResult) {
                EventBus.getDefault().postSticky(new UpdateEvent(attachResult.getAttachs()[0].getAttachID()));
                SuccessDeleteFileDialogFragment fragment = SuccessDeleteFileDialogFragment.newInstance("فایل با موفقیت حذف شد");
                fragment.show(getParentFragmentManager(), SuccessDeleteFileDialogFragment.TAG);
            }
        });

        viewModel.getErrorDeleteAttachResultSingleLiveEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(message);
                fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
            }
        });
    }
}