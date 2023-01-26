package com.dtag.osta.Fragment.ViewModel.sales;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SalesFragmentBinding;
import com.dtag.osta.Adapter.SalesAdapter;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesViewModel extends ViewModel {
    SalesFragmentBinding salesFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private SalesAdapter salesAdapter;
    Context context;

    public void Init(SalesFragmentBinding salesFragmentBinding, Context context) {
        this.context = context;
        salesAdapter = new SalesAdapter(context);
        notification();
        salesFragmentBinding.notificationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.notificationFragment);
            }
        });

        salesFragmentBinding.homeRecyclerView.setAdapter(salesAdapter);
        salesFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.sales().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        salesFragmentBinding.progress.setVisibility(View.GONE);
                        if (response.body().getData().getSales().isEmpty()) {
                            salesFragmentBinding.noResultTv.setText(R.string.thereisanerrorinthedata);
                        }
                        salesAdapter.setSales(response.body().getData().getSales());
                    }

                } else {
                    salesFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                salesFragmentBinding.progress.setVisibility(View.GONE);
                salesFragmentBinding.noResultTv.setText(R.string.internetconnection);
            }
        });
        this.salesFragmentBinding = salesFragmentBinding;
    }
    private void notification() {
        apiInterface.getNotifications(Sal7haSharedPreference.getToken(context),0).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (!response.body().getData().getNotifications().getData().isEmpty()) {
                            int size = response.body().getData().getNotifications().getTotal();
                            salesFragmentBinding.notificationBadge.setText(size + "");
                            salesFragmentBinding.notificationBadge.setVisibility(View.VISIBLE);
                            Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                            salesFragmentBinding.notificationId.startAnimation(shake);
                            salesFragmentBinding.notificationBadge.setVisibility(View.VISIBLE);
                        }
                        else {
                            salesFragmentBinding.notificationBadge.setVisibility(View.GONE);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {

            }
        });
    }

}



