package com.dtag.osta.Fragment.ViewModel.main.services;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.Adapter.ServiceAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.OffersFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

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
        this.offersFragmentBinding = offersFragmentBinding;
        serviceAdapter = new ServiceAdapter(context);
        notification();
        offersFragmentBinding.searchId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("WORD?",offersFragmentBinding.searchId.getText().toString());
                getSearch(offersFragmentBinding.searchId.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getAll();
        offersFragmentBinding.contactUsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.supportFragment);
            }
        });
        offersFragmentBinding.notificationId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.notificationFragment);
            }
        });

    }

    private void getAll() {
        offersFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.serviceCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.i(TAG, "SuccessHAHAH");
                        offersFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                        offersFragmentBinding.searchRecyclerView.setVisibility(View.GONE);
                        offersFragmentBinding.homeRecyclerView.setAdapter(serviceAdapter);
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

    }

    private void getSearch(String name) {
        offersFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.searchCategories(name).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.i(TAG, "SuccessSearch");
                        offersFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                        offersFragmentBinding.searchRecyclerView.setVisibility(View.VISIBLE);
                        offersFragmentBinding.searchRecyclerView.setAdapter(serviceAdapter);
                        offersFragmentBinding.progress.setVisibility(View.GONE);
                        serviceAdapter.setServices(response.body().getData().getServicesList());
                        Log.i(TAG, "List" + response.body().getData().getServicesList());
                    }
                } else {
                    Log.i(TAG, "NoSuccessSearch");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                offersFragmentBinding.progress.setVisibility(View.GONE);
                offersFragmentBinding.noResultTv.setText(R.string.internetconnection);
            }
        });
    }

    private void notification() {
        apiInterface.getNotifications(Sal7haSharedPreference.getToken(context), 0).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (!response.body().getData().getNotifications().getData().isEmpty()) {
                            int size = response.body().getData().getNotifications().getTotal();
                            offersFragmentBinding.notificationBadge.setText(size + "");
                            offersFragmentBinding.notificationBadge.setVisibility(View.VISIBLE);
                            Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
                            offersFragmentBinding.notificationId.startAnimation(shake);
                            offersFragmentBinding.notificationBadge.setVisibility(View.VISIBLE);
                        } else {
                            offersFragmentBinding.notificationBadge.setVisibility(View.GONE);

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
