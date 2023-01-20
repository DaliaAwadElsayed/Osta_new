package com.dtag.osta.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dtag.osta.R;
import com.dtag.osta.databinding.BannerLayoutBinding;
import com.dtag.osta.network.ResponseModel.Model.orderList.UploadedImage;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageBannerAdapter extends PagerAdapter {
    private List<UploadedImage> images;
    private static final String TAG = "adapter";

    public void setImages(List<UploadedImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        BannerLayoutBinding layoutBinding = BannerLayoutBinding.inflate(LayoutInflater.from(container.getContext()), container, false);

        container.addView(layoutBinding.getRoot());
        Picasso.get().load(RetrofitClient.BASE_URL + '/' + images.get(position).getSrc()).error(R.drawable.ic_baseline_cancel_24).into(layoutBinding.img);

        return layoutBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((MaterialCardView) object);
    }

}
