package com.dtag.osta.Fragment.ViewModel.authentication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SignInPhoneNumberFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.PhoneNumber;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInPhoneNumberViewModel extends ViewModel {
    SignInPhoneNumberFragmentBinding signInPhoneNumberFragmentBinding;
    private static final String TAG = "SignInPhoneNumber";
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private PhoneNumber phoneNumber = new PhoneNumber();
    Context context;

    public void Init(SignInPhoneNumberFragmentBinding signInPhoneNumberFragmentBinding, Context context) {
        this.context = context;
        this.signInPhoneNumberFragmentBinding = signInPhoneNumberFragmentBinding;
        signInPhoneNumberFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.signinfragment);
            }
        });
        signInPhoneNumberFragmentBinding.enter.setOnClickListener(view -> {
            if (phoneNumberValid()) {
                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                if (dialog != null) {
                    int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                }
                dialog.setContentView(R.layout.progress_dialog);
                dialog.show();
                String phoneNumberString = signInPhoneNumberFragmentBinding.phonenumber.getText().toString();
                if (Sal7haSharedPreference.getRole(context).equals("user")) {
                    apiInterface.sendPhoneNumber(phoneNumberString).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    dialog.dismiss();
                                    Log.i("CODDDE", response.body().getMessage() + "?");
//                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phoneNumber", signInPhoneNumberFragmentBinding.phonenumber.getText().toString());
                                    Navigation.findNavController(view).navigate(R.id.verificationCodeFragment, bundle);
                                } else {
                                    dialog.dismiss();
                                    String[] myArray = new String[]{response.body().getErrors().getEmail().toString()};

                                    StringBuilder builder = new StringBuilder();
                                    for (String i : myArray) {
                                        builder.append("" + i + " ");
                                    }
                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    apiInterface.agentSendPhoneNumber(phoneNumberString).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    dialog.dismiss();
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phoneNumber", signInPhoneNumberFragmentBinding.phonenumber.getText().toString());
                                    Navigation.findNavController(view).navigate(R.id.verificationCodeFragment, bundle);
                                } else {
                                    dialog.dismiss();
                                    String[] myArray = new String[]{response.body().getErrors().getEmail().toString()};
                                    StringBuilder builder = new StringBuilder();
                                    for (String i : myArray) {
                                        builder.append("" + i + " ");
                                    }
                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean phoneNumberValid() {
        if (!signInPhoneNumberFragmentBinding.phonenumber.getText().toString().isEmpty()) {
            return true;
        } else {
            signInPhoneNumberFragmentBinding.phonenumber.setError(context.getResources().getString(R.string.entervalidemail));
            return false;
        }
    }

}
