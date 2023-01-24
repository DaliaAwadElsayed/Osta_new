package com.dtag.osta.Fragment.ViewModel.authentication.worker.auth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.WorkerSignInFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.network.ResponseModel.wrapper.SetToken;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkerSignInViewModel extends ViewModel {
    WorkerSignInFragmentBinding workerSignInFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    LoginRequest loginRequest = new LoginRequest();
    SetToken setToken = new SetToken();
    Boolean isNotification;

    public void Init(WorkerSignInFragmentBinding workerSignInFragmentBinding, Context context) {
        this.context = context;
        this.workerSignInFragmentBinding = workerSignInFragmentBinding;
        workerSignInFragmentBinding.registertextviewworker.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.signInPhoneNumberFragment);
        });
//        if (isNotification){
//            final Dialog dialog = new Dialog(context);
//            dialog.setCancelable(false);
//            if (dialog != null) {
//                int width = ViewGroup.LayoutParams.MATCH_PARENT;
//                int height = ViewGroup.LayoutParams.MATCH_PARENT;
//                dialog.getWindow().setLayout(width, height);
//            }
//            dialog.setContentView(R.layout.unactive_dialog);
//            Button cancel;
//            TextView text;
//            text = dialog.findViewById(R.id.textchange);
//            text.setText(R.string.activiation_notification);
//            cancel = dialog.findViewById(R.id.okDialog);
//            cancel.setOnClickListener(view -> dialog.dismiss());
//            dialog.show();
//        }
        workerSignInFragmentBinding.enter.setOnClickListener(view -> {
            if (inputValid()) {
                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                if (dialog != null) {
                    int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                }
                dialog.setContentView(R.layout.progress_dialog);
                dialog.show();
                loginRequest.setEmail(workerSignInFragmentBinding.phonenumber.getText().toString());
                loginRequest.setPassword(workerSignInFragmentBinding.password.getText().toString());
                loginRequest.setLogin_as("agent");
                apiInterface.logIn(loginRequest).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                dialog.dismiss();
                                Sal7haSharedPreference.saveUserData(context, response.body().getData().getUser(), response.body().getData().getUser().getId());
                                getDeviceToken(Sal7haSharedPreference.getToken(context));
                                Bundle bundle = new Bundle();
                                bundle.putString("type", loginRequest.getLogin_as());
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.profileFragment, bundle);
                            } else {
                                dialog.dismiss();
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                                if (response.body().getErrors().getAccount() != null) {
                                    String[] myArray = new String[]{response.body().getErrors().getAccount().toString()};
                                    StringBuilder builder = new StringBuilder();
                                    for (String i : myArray) {
                                        builder.append("" + i + " ");
                                    }
                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                } else {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.setCancelable(false);
                                    if (dialog != null) {
                                        int width = ViewGroup.LayoutParams.MATCH_PARENT;
                                        int height = ViewGroup.LayoutParams.MATCH_PARENT;
                                        dialog.getWindow().setLayout(width, height);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    }
                                    dialog.setContentView(R.layout.unactive_dialog);
                                    Button cancel;
                                    cancel = dialog.findViewById(R.id.okDialog);
                                    cancel.setOnClickListener(v -> dialog.dismiss());
                                    dialog.show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void getDeviceToken(String userToken) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String deviceToken = task.getResult();
                        setToken.setDevice_token(deviceToken);
                        setToken.setType("android");
                        apiInterface.UserSetToken(userToken, setToken).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                                if (response.body() != null && response.isSuccessful()) {
                                    if (response.body().getStatus()) {
                                        //  Toast.makeText(context, setToken.getDevice_token(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                                Log.e(Constraints.TAG, "onFailure: ", t);
                            }
                        });
                    }
                });

    }


    private boolean inputValid() {
        return emailValid() && passwordIsValid();
    }

    private boolean passwordIsValid() {
        String firstPass = workerSignInFragmentBinding.password.getText().toString();
        if (firstPass.length() < 6) {
            workerSignInFragmentBinding.password.setError(context.getResources().getString(R.string.lessthan6letters));
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValid() {
        if (!workerSignInFragmentBinding.phonenumber.getText().toString().isEmpty()) {
            return true;
        } else {
            workerSignInFragmentBinding.phonenumber.setError(context.getResources().getString(R.string.entervalidemail));
            return false;
        }
    }
}
