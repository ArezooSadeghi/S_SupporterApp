package com.example.sipsupporterapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sipsupporterapp.R;
import com.example.sipsupporterapp.databinding.AttachmentAdapterItemBinding;
import com.example.sipsupporterapp.diffutils.BitmapDiffUtils;
import com.example.sipsupporterapp.viewmodel.AttachmentViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentHolder> {
    private Context context;
    private List<Bitmap> bitmaps;
    private AttachmentViewModel viewModel;
    private Map<Bitmap, String> mapBitmap;
    private Map<Uri, String> mapUri;

    public AttachmentAdapter(Context context, List<Bitmap> bitmaps, AttachmentViewModel viewModel) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.viewModel = viewModel;
    }

    public void setMapBitmap(Map<Bitmap, String> mapBitmap) {
        this.mapBitmap = mapBitmap;
    }

    public void setMapUri(Map<Uri, String> mapUri) {
        this.mapUri = mapUri;
    }

    @NonNull
    @Override
    public AttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachmentHolder(DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.attachment_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder holder, int position) {
        holder.bindBitmap(bitmaps.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = bitmaps.get(position);
                String imageName = mapBitmap.get(bitmap);
                Uri uri = null;
                for (Map.Entry<Uri, String> entry : mapUri.entrySet()) {
                    if (entry.getValue().equals(imageName)) {
                        uri = entry.getKey();
                    }
                }
                Map<Uri, String> mapUri = new HashMap<>();
                mapUri.put(uri, imageName);
                viewModel.getShowFullScreenImage().setValue(mapUri);
            }
        });
    }

    public void updateBitmaps(List<Bitmap> newBitmaps) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BitmapDiffUtils(bitmaps, newBitmaps));
        diffResult.dispatchUpdatesTo(this);
        bitmaps.clear();
        bitmaps.addAll(newBitmaps);
    }

    @Override
    public int getItemCount() {
        return bitmaps == null ? 0 : bitmaps.size();
    }

    public class AttachmentHolder extends RecyclerView.ViewHolder {
        private AttachmentAdapterItemBinding binding;

        public AttachmentHolder(AttachmentAdapterItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindBitmap(Bitmap bitmap) {
            Glide.with(context).asBitmap().load(bitmap).into(binding.imgView);
        }
    }
}
