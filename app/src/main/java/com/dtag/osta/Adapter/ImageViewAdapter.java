package com.dtag.osta.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {
    private LayoutInflater inflater;
    private List<Uri> imagesEncodedList;
    Context context;

    public ImageViewAdapter(List<Uri> imagesEncodedList, Context context) {
        this.imagesEncodedList = imagesEncodedList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new ImageViewHolder(inflater.inflate(R.layout.image_gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.setModelImage(imagesEncodedList.get(position));
    }

    @Override
    public int getItemCount() {
        return imagesEncodedList == null ? 0 : imagesEncodedList.size();
    }

    public void imageList(List<Uri> imagesEncodedList) {
        this.imagesEncodedList = imagesEncodedList;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView modelImage;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.modelImage = itemView.findViewById(R.id.gallery2);
        }

        void setModelImage(Uri uri) {
            Picasso.get().load(uri).into(this.modelImage);
        }

    }
}
