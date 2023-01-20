package com.dtag.osta.Fragment.ViewModel.sales;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.dtag.osta.Adapter.SalesAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.SalesDetailsFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesDetailsViewModel extends ViewModel {
    SalesDetailsFragmentBinding salesDetailsFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private SalesAdapter salesAdapter;
    Context context;
    int id;

    public void Init(SalesDetailsFragmentBinding salesDetailsFragmentBinding, Context context, int id) {
        this.context = context;
        this.salesDetailsFragmentBinding = salesDetailsFragmentBinding;
        this.id = id;
        showSaleDetails();

    }

    private void showSaleDetails() {
        salesDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.salesDetails(id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        salesDetailsFragmentBinding.progress.setVisibility(View.GONE);
                        salesDetailsFragmentBinding.fromId.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getStartDate()) + "\n" + context.getResources().getString(R.string.to) + "\n" + response.body().getData().getSalesDetails().getEndDate());
                        salesDetailsFragmentBinding.couponId.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getCoupon()));

                        if (response.body().getData().getSalesDetails().getType().equals("amount")) {
                            salesDetailsFragmentBinding.percentage.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getDiscount()) + " " + context.getResources().getString(R.string.sar));

                        } else {
                            salesDetailsFragmentBinding.percentage.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getDiscount()) + " " + "%");

                        }
                        if (response.body().getData().getSalesDetails().getStatus().equals("deactivated")) {
                            salesDetailsFragmentBinding.status.setText(R.string.deactive);
                        } else {
                            salesDetailsFragmentBinding.status.setText(R.string.activec);
                        }

                        if (Sal7haSharedPreference.getSelectedLanguageValue(context).equals("ar")) {
                            salesDetailsFragmentBinding.offername.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getNameAr()));
                            salesDetailsFragmentBinding.descrption.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getTextAr()));
                        } else {
                            salesDetailsFragmentBinding.offername.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getNameEn()));
                            salesDetailsFragmentBinding.descrption.setText(Utility.fixNullString(response.body().getData().getSalesDetails().getTextEn()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }


}
