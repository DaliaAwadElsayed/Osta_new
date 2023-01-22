package com.dtag.osta.Fragment.ViewModel.main.services;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.Adapter.ServiceAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.OffersFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersViewModel extends ViewModel {
    OffersFragmentBinding offersFragmentBinding;
    private static final String TAG = "OffersFragment";
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private ServiceAdapter serviceAdapter;
    Context context;

    public void Init(OffersFragmentBinding offersFragmentBinding, Context context) {
        this.context = context;
        serviceAdapter = new ServiceAdapter(context);
        offersFragmentBinding.homeRecyclerView.setAdapter(serviceAdapter);
        offersFragmentBinding.progress.setVisibility(View.VISIBLE);
        offersFragmentBinding.notificationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.notificationFragment);
            }
        });
        apiInterface.serviceCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.i(TAG, "SuccessHAHAH");
                        offersFragmentBinding.progress.setVisibility(View.GONE);
                        serviceAdapter.setServices(response.body().getData().getServicesList());
                        Log.i(TAG, "List" + response.body().getData().getServicesList());
                    }
                } else {
                    Log.i(TAG, "NoSuccess");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                offersFragmentBinding.progress.setVisibility(View.GONE);
                offersFragmentBinding.noResultTv.setText(R.string.internetconnection);
            }
        });
        this.offersFragmentBinding = offersFragmentBinding;
    }
}
