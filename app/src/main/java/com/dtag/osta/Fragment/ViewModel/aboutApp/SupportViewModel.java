package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SupportFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportViewModel extends ViewModel {
    SupportFragmentBinding supportFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    String whatsapp, phoneNumber, twitter, email;

    public void Init(SupportFragmentBinding supportFragmentBinding, Context context) {
        this.context = context;
        this.supportFragmentBinding = supportFragmentBinding;
        supportFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.offersFragment);
            }
        });
        supportFragmentBinding.technicalsupport.setOnClickListener(view -> {
            apiInterface.support().enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + response.body().getData().getSettings().get(2).getValue()));
                            context.startActivity(intent);
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
        supportFragmentBinding.whatsappclick.setOnClickListener(view ->
                {
                    apiInterface.support().enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    String number = response.body().getData().getSettings().get(1).getValue();
                                    number = number.replace(" ", "").replace("+", "");
                                 sendWhatsapp(number);
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
                }
        );
        supportFragmentBinding.email.setOnClickListener(view -> {
            apiInterface.support().enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("vnd.android.cursor.item/email");
                            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            String[] TO = {response.body().getData().getSettings().get(3).getValue()};
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                            context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
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
        supportFragmentBinding.twitter.setOnClickListener(view -> {
            apiInterface.support().enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            phoneNumber = response.body().getData().getSettings().get(0).getValue();
                            Uri uri = Uri.parse(phoneNumber);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
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
    }
    private void sendWhatsapp(String number) {
        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_VIEW);
            sendIntent.setPackage("com.whatsapp");
            String url = "https://api.whatsapp.com/send?phone=" + number ;
            sendIntent.setData(Uri.parse(url));
            context.startActivity(sendIntent);

        } catch (Exception e) {
            Log.i("EXCEPTIONn", e.toString());
            Toast.makeText(context, "WhatsApp Not Install", Toast.LENGTH_SHORT).show();
        }


    }
    private void technicalSupport() {
       /* apiInterface.support().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        whatsapp = response.body().getData().getSettings().get(1).getValue();
                        twitter = response.body().getData().getSettings().get(0).getValue();
                        phoneNumber = response.body().getData().getSettings().get(2).getValue();
                        email = response.body().getData().getSettings().get(3).getValue();
                    } else {
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });*/

    }
}
