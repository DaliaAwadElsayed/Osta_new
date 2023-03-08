package com.dtag.osta.Adapter.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.databinding.NotificationItemBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.Data;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Data> store = new ArrayList<>();
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View viewItem, String type, String id);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public NotificationAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(NotificationItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(store.get(position));
    }

    @Override
    public int getItemCount() {
        return store == null ? 0 : store.size();
    }

    public void setStores(List<Data> store) {
        this.store.addAll(store);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private NotificationItemBinding storeGrideItemBinding;

        HomePageViewHolder(@NonNull NotificationItemBinding storeGrideItemBinding) {
            super(storeGrideItemBinding.getRoot());
            this.storeGrideItemBinding = storeGrideItemBinding;
        }

        private void bindRestaurant(Data store) {
            if (store.getViewed()==0){
                storeGrideItemBinding.showId.setVisibility(View.VISIBLE);
            }else {
                storeGrideItemBinding.showId.setVisibility(View.GONE);

            }
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                storeGrideItemBinding.titleId.setText(Utility.fixNullString(String.valueOf(store.getBody())));
            } else {
                storeGrideItemBinding.titleId.setText(Utility.fixNullString(String.valueOf(store.getBody_en())));
            }
            storeGrideItemBinding.notificationItemId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    readNotification(getAdapterPosition());
                    //TODO NAVIGATION BY TYPES
                }
            });


        }

        private void readNotification(int position) {
            apiInterface.readNotification(Sal7haSharedPreference.getToken(context), store.get(position).getId()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200 && response.body() != null) {
                            storeGrideItemBinding.showId.setVisibility(View.GONE);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        }


    }
}

