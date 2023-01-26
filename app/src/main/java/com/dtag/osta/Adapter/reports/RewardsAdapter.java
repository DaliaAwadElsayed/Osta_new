package com.dtag.osta.Adapter.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.dtag.osta.databinding.RewardWalletItemBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.Data;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.HomePageViewHolder> {
    private LayoutInflater inflater;
    private List<Data> categories = new ArrayList<>();
    Context context;
    int subId;
    double amount;
    private int selectedPosition = -1;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public RewardsAdapter(Context context) {
        this.context = context;
    }

    public void addCategory(List<Data> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public List<Data> getCategories() {
        return categories;
    }


    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new HomePageViewHolder(RewardWalletItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageViewHolder holder, int position) {
        holder.bindRestaurant(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public void setCategories(List<Data> categories) {
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    class HomePageViewHolder extends RecyclerView.ViewHolder {
        private RewardWalletItemBinding partitionRecyclerItemBinding;

        HomePageViewHolder(@NonNull RewardWalletItemBinding partitionRecyclerItemBinding) {
            super(partitionRecyclerItemBinding.getRoot());
            this.partitionRecyclerItemBinding = partitionRecyclerItemBinding;
        }

        private void bindRestaurant(Data category) {
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 0) {
                //0 for english
                partitionRecyclerItemBinding.rewardId.setText(Utility.fixNullString(category.getBody()));
            } else {
                partitionRecyclerItemBinding.rewardId.setText(Utility.fixNullString(category.getBody()));
            }
            partitionRecyclerItemBinding.restaurantTimeId.setText(Utility.fixNullString(category.getCreatedAt()));
            partitionRecyclerItemBinding.amountId.setText(Utility.fixNullString(String.valueOf(category.getReward_value())) + " " + context.getResources().getString(R.string.sar));


        }


    }
}

