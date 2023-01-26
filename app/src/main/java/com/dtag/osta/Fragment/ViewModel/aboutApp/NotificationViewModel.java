package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.Adapter.notification.NotificationAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.NotificationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    NotificationFragmentBinding notificationFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    NotificationAdapter notificationAdapter;
    private int currentPage = 1;
    int lastPage;

    public void init(NotificationFragmentBinding notificationFragmentBinding, Context context) {
        this.notificationFragmentBinding = notificationFragmentBinding;
        this.context = context;
        notificationAdapter = new NotificationAdapter(context);
        notificationFragmentBinding.homeRecyclerView.setAdapter(notificationAdapter);
        getNotification(1);
        notificationFragmentBinding.idNesteddSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == view.getChildAt(0).getMeasuredHeight() - view.getMeasuredHeight()) {
                    currentPage++;
                    Log.i("currentPageScrolling", String.valueOf(currentPage));
                    Log.i("LastPageScrolling", String.valueOf(lastPage));
                    if (currentPage <= lastPage) {
                        Log.i("currentPageIf", String.valueOf(currentPage));
                        getNotification(currentPage);
                    }
                }
            }
        });

        notificationFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.offersFragment);
            }
        });
    }

    private void getNotification(int page) {
        notificationFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.getNotifications(Sal7haSharedPreference.getToken(context), page).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (!response.body().getData().getNotifications().getData().isEmpty()) {
                            lastPage = response.body().getData().getNotifications().getLastPage();
                            notificationFragmentBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                            notificationFragmentBinding.noDataId.setVisibility(View.GONE);
                            notificationFragmentBinding.progress.setVisibility(View.GONE);
                            notificationAdapter.setStores(response.body().getData().getNotifications().getData());
                        } else {
                            notificationFragmentBinding.homeRecyclerView.setVisibility(View.GONE);
                            notificationFragmentBinding.progress.setVisibility(View.GONE);
                            notificationFragmentBinding.noDataId.setVisibility(View.VISIBLE);
                        }
                    } else {
                        notificationFragmentBinding.progress.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    notificationFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("onFailure", t.toString());
                notificationFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

