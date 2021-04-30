package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.GalleryAdapterItemBinding;
import com.example.sipsupporterapp.model.AttachInfo;
import com.example.sipsupporterapp.viewmodel.GalleryViewModel;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {
    private Context context;
    private List<AttachInfo> attachInfoList;
    private GalleryViewModel viewModel;

    public GalleryAdapter(Context context, List<AttachInfo> attachInfoList, GalleryViewModel viewModel) {
        this.context = context;
        this.attachInfoList = attachInfoList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.gallery_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        holder.bindAttachInfo(attachInfoList.get(position));

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.getPhotoClickedSingleLiveEvent().setValue(attachInfoList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachInfoList == null ? 0 : attachInfoList.size();
    }

    public class GalleryHolder extends RecyclerView.ViewHolder {
        private GalleryAdapterItemBinding binding;

        public GalleryHolder(GalleryAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindAttachInfo(AttachInfo attachInfo) {
            byte[] decodedString = Base64.decode(attachInfo.getFileData().getBytes(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            binding.imgAttachmentFile.setImageBitmap(decodedByte);
           /* Glide.with(context)
                    .load(decodedString)
                    .centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imgAttachmentFile);*/

        }
    }
}
