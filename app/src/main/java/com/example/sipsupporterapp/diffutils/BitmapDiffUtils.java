package com.example.sipsupporterapp.diffutils;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class BitmapDiffUtils extends DiffUtil.Callback {

    private List<Bitmap> oldBitmaps = new ArrayList<>();
    private List<Bitmap> newBitmaps = new ArrayList<>();

    public BitmapDiffUtils(List<Bitmap> oldBitmaps, List<Bitmap> newBitmaps) {
        this.oldBitmaps = oldBitmaps;
        this.newBitmaps = newBitmaps;
    }

    @Override
    public int getOldListSize() {
        return oldBitmaps != null ? oldBitmaps.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newBitmaps != null ? newBitmaps.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBitmaps.get(oldItemPosition) == newBitmaps.get(newItemPosition);
    }
}
