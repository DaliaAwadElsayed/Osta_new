package com.dtag.osta.Adapter.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.dtag.osta.databinding.RequestReportItemBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.user.RateService;
import com.dtag.osta.network.ResponseModel.wrapper.Data;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class RequestReportAdapter extends RecyclerView.Adapter<RequestReportAdapter.ServicesViewHolder>
        implements View.OnClickListener {
    private LayoutInflater inflater;
    private List<Data> orders;
    RateService rateService = new RateService();
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    private OnModelClickListener onModelClickListener;


    public void setOrderListener(OnModelClickListener onModelClickListener) {
        this.onModelClickListener = onModelClickListener;
    }
    public RequestReportAdapter(Context context) {
        this.context = context;
    }

    public void setRequests(List<Data> orders) {
        this.orders = orders;
        Collections.reverse(orders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new ServicesViewHolder(RequestReportItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.bindRequest(orders.get(position));

    }


    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    @Override
    public void onClick(View view) {

    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {
        private RequestReportItemBinding requestItemBinding;

        ServicesViewHolder(@NonNull RequestReportItemBinding requestItemBinding) {
            super(requestItemBinding.getRoot());
            this.requestItemBinding = requestItemBinding;
        }

        private void bindRequest(Data orders) {
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                if (orders.getCategory() != null)
                    requestItemBinding.servicename.setText(Utility.fixNullString(orders.getCategory().getName()));
            } else {
                if (orders.getCategory() != null)
                    requestItemBinding.servicename.setText(Utility.fixNullString(orders.getCategory().getNameEn()));
            }

//            requestItemBinding.visitdate.setText(Utility.fixNullString(orders.getDate()));
//            requestItemBinding.visittime.setText(Utility.fixNullString(orders.getTime()));
            requestItemBinding.requststats.setText(R.string.cancelledd);
            if (orders.getCategory() != null) {
                if (!orders.getCategory().getIcon().isEmpty()) {
                    Picasso.get().load(RetrofitClient.BASE_URL + '/' + orders.getCategory().getIcon()).into(requestItemBinding.img);
                } else {
                    requestItemBinding.img.setImageResource(R.drawable.smile);
                }
            } else {
                requestItemBinding.img.setImageResource(R.drawable.smile);
            }

        }


    }

    public interface OnModelClickListener {
        void sendModelId(int id);
    }

}
