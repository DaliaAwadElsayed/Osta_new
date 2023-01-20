package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.AboutAppFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.RateApp;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAppViewModel extends ViewModel {
    AboutAppFragmentBinding aboutAppFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    RateApp rateApp = new RateApp();

    public void Init(AboutAppFragmentBinding aboutAppFragmentBinding, Context context) {
        this.aboutAppFragmentBinding = aboutAppFragmentBinding;
        this.context = context;
        aboutAppFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.offersFragment);
            }
        });
        aboutAppFragmentBinding.shareopinion.setOnClickListener(view -> {
            if (Sal7haSharedPreference.isLoggedIn(context)) {
                rateAppDialog();
            } else {
                Toast.makeText(context, R.string.youhavetologin, Toast.LENGTH_SHORT).show();
            }

        });
        aboutAppFragmentBinding.aboutsal7ha.setOnClickListener(view -> {
            if (aboutAppFragmentBinding.aboutsal7haexpand.getVisibility() == View.GONE) {
                aboutApp("about");
                aboutAppFragmentBinding.progress1.setVisibility(View.VISIBLE);
                aboutAppFragmentBinding.aboutsal7haexpand.setVisibility(View.VISIBLE);
            } else {
                aboutAppFragmentBinding.aboutsal7haexpand.setVisibility(View.GONE);
            }
        });

        aboutAppFragmentBinding.usageway.setOnClickListener(view -> {
            if (aboutAppFragmentBinding.usageexpand.getVisibility() == View.GONE) {
                aboutAppFragmentBinding.progress2.setVisibility(View.VISIBLE);
                aboutApp("usage");
                aboutAppFragmentBinding.usageexpand.setVisibility(View.VISIBLE);
            } else {
                aboutAppFragmentBinding.usageexpand.setVisibility(View.GONE);
            }
        });
        aboutAppFragmentBinding.policy.setOnClickListener(view -> {
            if (aboutAppFragmentBinding.policyexpand.getVisibility() == View.GONE) {
                aboutAppFragmentBinding.progress3.setVisibility(View.VISIBLE);
                aboutApp("terms");
                aboutAppFragmentBinding.policyexpand.setVisibility(View.VISIBLE);
            } else {
                aboutAppFragmentBinding.policyexpand.setVisibility(View.GONE);
            }
        });
    }

    private void aboutApp(String name) {
        apiInterface.aboutApp(name).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        //      Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        if (name.equals("usage")) {
                            aboutAppFragmentBinding.progress2.setVisibility(View.GONE);
                            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                                aboutAppFragmentBinding.usageexpand.setText(response.body().getData().getContentAr());
                            } else {
                                aboutAppFragmentBinding.usageexpand.setText(response.body().getData().getContentEn());

                            }
                        }
                        if (name.equals("about")) {
                            aboutAppFragmentBinding.progress1.setVisibility(View.GONE);
                            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                                aboutAppFragmentBinding.aboutsal7haexpand.setText(response.body().getData().getContentAr());
                            } else {
                                aboutAppFragmentBinding.aboutsal7haexpand.setText(response.body().getData().getContentEn());

                            }
                        }

                        if (name.equals("terms")) {
                            aboutAppFragmentBinding.progress3.setVisibility(View.GONE);
                            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                                aboutAppFragmentBinding.policyexpand.setText(response.body().getData().getContentAr());
                            } else {
                                aboutAppFragmentBinding.policyexpand.setText(response.body().getData().getContentEn());

                            }
                        }
                    }
                    //      Toast.makeText(context, "nosuccess", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void rateAppDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setContentView(R.layout.rate_app_dialog);
        Button send, later;
        TextView comment;
        RatingBar ratingBar;
        ratingBar = dialog.findViewById(R.id.rateappdialog);
        comment = dialog.findViewById(R.id.rateAppComment);
        later = dialog.findViewById(R.id.laterAppComment);
        send = dialog.findViewById(R.id.sendAppComment);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
        send.setOnClickListener(view -> {
            if (comment.getText().toString().matches("")) {
                rateApp.setRate(ratingBar.getRating());
                rateApp.setComment(context.getResources().getString(R.string.prettyapp));
            } else {
                rateApp.setComment(comment.getText().toString());
                rateApp.setRate(ratingBar.getRating());
            }
            apiInterface.rateApp(Sal7haSharedPreference.getToken(context), rateApp).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            dialog.dismiss();
                            Toast.makeText(context, R.string.succesfulyratedapp, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        });
        later.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }
}
