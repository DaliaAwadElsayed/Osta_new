package com.dtag.osta.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SaleItemBinding;
import com.dtag.osta.network.ResponseModel.Model.user.sales.Sales;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {
    private LayoutInflater inflater;
    private List<Sales> sales;
    Context context;

    public SalesAdapter(Context context) {
        this.context = context;
    }

    public void setSales(List<Sales> sales) {
        this.sales = sales;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new SalesViewHolder(SaleItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        holder.bindSales(sales.get(position));

    }

    @Override
    public int getItemCount() {
        return sales == null ? 0 : sales.size();
    }

    class SalesViewHolder extends RecyclerView.ViewHolder {
        private SaleItemBinding saleItemBinding;

        SalesViewHolder(@NonNull SaleItemBinding saleItemBinding) {
            super(saleItemBinding.getRoot());
            this.saleItemBinding = saleItemBinding;
        }

        private void bindSales(Sales sales) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", sales.getId());
            Picasso.get().load(RetrofitClient.BASE_URL + '/' + sales.getImage()).error(R.drawable.promote).placeholder(R.drawable.promote).into(saleItemBinding.imageView);
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                saleItemBinding.salename.setText(sales.getNameAr());
            } else {
                saleItemBinding.salename.setText(sales.getNameEn());
            }

            itemView.setOnClickListener(view -> {
                //   bundle.putInt("categoryId", services.getId());
                Navigation.findNavController(view).navigate(R.id.salesDetailsFragment, bundle);

            });

        }
    }


}
