package com.example.sipsupporterapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.FragmentGallerySupportsBinding;

public class GallerySupportsFragment extends Fragment {
    private FragmentGallerySupportsBinding mBinding;

    public static GallerySupportsFragment newInstance() {
        GallerySupportsFragment fragment = new GallerySupportsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_gallery_supports, container, false);

        return mBinding.getRoot();
    }
}