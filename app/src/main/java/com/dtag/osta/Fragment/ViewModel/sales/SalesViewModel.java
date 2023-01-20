package com.dtag.osta.Fragment.ViewModel.sales;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SalesFragmentBinding;
import com.dtag.osta.Adapter.SalesAdapter;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;

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
        salesFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.offersFragment);
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

}



