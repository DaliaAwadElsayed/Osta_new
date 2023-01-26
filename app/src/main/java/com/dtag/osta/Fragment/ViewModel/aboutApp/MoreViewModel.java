package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.R;
import com.dtag.osta.databinding.MoreFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreViewModel extends ViewModel {
    MoreFragmentBinding moreFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    String type;
    Activity activity;

    public void init(MoreFragmentBinding moreFragmentBinding, Context context, Activity activity) {
        this.context = context;
        this.moreFragmentBinding = moreFragmentBinding;
        this.activity = activity;
        moreFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.offersFragment);
            }
        });
        clicks();
    }

    private void clicks() {
        apiInterface.getCustomerProfile(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        String name = response.body().getData().getUser().getName();
                        moreFragmentBinding.nameId.setText(name + "");
                        moreFragmentBinding.electronicId.setText(context.getResources().getString(R.string.electronic_value) +
                                " " + response.body().getData().getUser().getTotal_wallet() + context.getResources().getString(R.string.sar));
                        Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getUser().getImage()).error(R.drawable.ic_profile).placeholder(R.drawable.ic_profile)
                                .into(moreFragmentBinding.profileImage);

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
        moreFragmentBinding.homeId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.offersFragment));
        moreFragmentBinding.offersId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.salesFragment));
        moreFragmentBinding.profileId.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("type", "user");
            Navigation.findNavController(v).navigate(R.id.profileFragment, bundle);
        });
        moreFragmentBinding.contractId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.constructionFragment));
        moreFragmentBinding.orderId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.requestsFragment));
        moreFragmentBinding.reportId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.reportFragment));
        moreFragmentBinding.supportId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.supportFragment));
        moreFragmentBinding.langId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.settingFragment));
        moreFragmentBinding.infoId.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.aboutAppFragment));
        moreFragmentBinding.logoutId.setOnClickListener(v -> {
            if (type.equals("google")) {
                ((MainActivity) activity).googleLogOut();
            }
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.logo);
            builder.setMessage(R.string.logoutQuestion)
                    .setCancelable(false)

                    .setPositiveButton(R.string.yes, (dialog, id) -> {
                        Sal7haSharedPreference.clearSharedPreference(context);
                        Log.i("TAG", "TOKEN" + Sal7haSharedPreference.getToken(context));
                        Navigation.findNavController(v).navigate(R.id.signinfragment);
                    })
                    .setNeutralButton(R.string.no, (dialog, id) -> dialog.cancel())
                    .show();
        });

    }
}