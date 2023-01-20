package com.dtag.osta.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.dtag.osta.databinding.OfferItemBinding;
import com.dtag.osta.network.ResponseModel.Model.Services;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServicesViewHolder> {
    private LayoutInflater inflater;
    private List<Services> services;
    Context context;

    public ServiceAdapter(Context context) {
        this.context = context;
    }

    public void setServices(List<Services> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new ServicesViewHolder(OfferItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.bindService(services.get(position));

    }


    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {
        private OfferItemBinding offerItemBinding;

        ServicesViewHolder(@NonNull OfferItemBinding offerItemBinding) {
            super(offerItemBinding.getRoot());
            this.offerItemBinding = offerItemBinding;
        }

        private void bindService(Services services) {
            Bundle bundle = new Bundle();
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                offerItemBinding.itemRecycleTextId.setText(services.getName());
                bundle.putString("categoryName", services.getName());


            } else {
                offerItemBinding.itemRecycleTextId.setText(services.getNameEn());
                bundle.putString("categoryName", services.getNameEn());
            }
            Picasso.get().load(RetrofitClient.BASE_URL + '/' + services.getIcon()).into(offerItemBinding.img);
            itemView.setOnClickListener(view -> {
                bundle.putInt("catPositon",getAdapterPosition());
                bundle.putInt("categoryId", services.getId());
                Navigation.findNavController(view).navigate(R.id.requestInfromationFragment, bundle);

            });

        }
    }


}
