package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;

import com.dtag.osta.Adapter.reports.RequestReportAdapter;
import com.dtag.osta.Adapter.reports.RewardsAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ReportFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends ViewModel {
    ReportFragmentBinding reportFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    RewardsAdapter rewardsAdapter;
    RequestReportAdapter requestReportAdapter;
    private int currentPage = 1;
    int lastPage;

    public void init(ReportFragmentBinding reportFragmentBinding, Context context) {
        this.context = context;
        this.reportFragmentBinding = reportFragmentBinding;
        requestReportAdapter = new RequestReportAdapter(context);
        rewardsAdapter = new RewardsAdapter(context);
        reportFragmentBinding.returnOrderRecyclerId.setAdapter(requestReportAdapter);
        reportFragmentBinding.rewardRecyclerId.setAdapter(rewardsAdapter);
        reportFragmentBinding.idOrderNesteddSV.setVisibility(View.GONE);
        getRewards(1);
        reportFragmentBinding.idOrderNesteddSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                    currentPage++;
                    Log.i("currentPageScrolling", String.valueOf(currentPage));
                    Log.i("LastPageScrolling", String.valueOf(lastPage));
                    if (currentPage <= lastPage) {
                        Log.i("currentPageIf", String.valueOf(currentPage));
                        getOrders(currentPage);
                    }
                }
            }
        });
        reportFragmentBinding.idRewardNesteddSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                    currentPage++;
                    Log.i("currentPageScrolling", String.valueOf(currentPage));
                    Log.i("LastPageScrolling", String.valueOf(lastPage));
                    if (currentPage <= lastPage) {
                        Log.i("currentPageIf", String.valueOf(currentPage));
                        getRewards(currentPage);
                    }
                }
            }
        });
        reportFragmentBinding.rewardClickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRewards(1);
                reportFragmentBinding.idOrderNesteddSV.setVisibility(View.GONE);
                reportFragmentBinding.idRewardNesteddSV.setVisibility(View.VISIBLE);

            }
        });
        reportFragmentBinding.ordersClickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFragmentBinding.idOrderNesteddSV.setVisibility(View.VISIBLE);
                reportFragmentBinding.idRewardNesteddSV.setVisibility(View.GONE);
                getOrders(1);
            }
        });

    }

    private void getOrders(int page) {
        reportFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getNotifications(Sal7haSharedPreference.getToken(context), page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (!response.body().getData().getNotifications().getData().isEmpty()) {
                            lastPage = response.body().getData().getNotifications().getLastPage();
                            reportFragmentBinding.returnOrderRecyclerId.setVisibility(View.VISIBLE);
                            reportFragmentBinding.noResultTvv.setVisibility(View.GONE);
                            reportFragmentBinding.progress.setVisibility(View.GONE);
                            requestReportAdapter.setRequests(response.body().getData().getNotifications().getData());
                        } else {
                            reportFragmentBinding.returnOrderRecyclerId.setVisibility(View.GONE);
                            reportFragmentBinding.progress.setVisibility(View.GONE);
                            reportFragmentBinding.noResultTvv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        reportFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    reportFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                reportFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getRewards(int page) {
        reportFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getNotifications(Sal7haSharedPreference.getToken(context), page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (!response.body().getData().getNotifications().getData().isEmpty()) {
                            lastPage = response.body().getData().getNotifications().getLastPage();
                            reportFragmentBinding.rewardRecyclerId.setVisibility(View.VISIBLE);
                            reportFragmentBinding.noResultTvv.setVisibility(View.GONE);
                            reportFragmentBinding.progress.setVisibility(View.GONE);
                            rewardsAdapter.setCategories(response.body().getData().getNotifications().getData());
                        } else {
                            reportFragmentBinding.rewardRecyclerId.setVisibility(View.GONE);
                            reportFragmentBinding.progress.setVisibility(View.GONE);
                            reportFragmentBinding.noResultTvv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        reportFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    reportFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                reportFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

}