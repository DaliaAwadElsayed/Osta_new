package com.dtag.osta.Fragment.ViewModel.authentication.user.auth;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SignInFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.network.ResponseModel.wrapper.SetToken;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.vipulasri.timelineview.TimelineView.TAG;

public class SignInViewModel extends ViewModel {
    SignInFragmentBinding signInFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    LoginRequest loginRequest = new LoginRequest();
    SetToken setToken = new SetToken();

    public void Init(SignInFragmentBinding signInFragmentBinding, Context context) {
        this.signInFragmentBinding = signInFragmentBinding;
        this.context = context;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        signInFragmentBinding.enter.setOnClickListener(view -> {

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
                loginRequest.setEmail(signInFragmentBinding.phonenumber.getText().toString());
                loginRequest.setPassword(signInFragmentBinding.password.getText().toString());
                loginRequest.setLogin_as("user");
                apiInterface.logIn(loginRequest).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                dialog.dismiss();
                                Sal7haSharedPreference.saveUserData(context, response.body().getData().getUser(), response.body().getData().getUser().getId());
                                getDeviceToken(Sal7haSharedPreference.getToken(context));
                                Bundle bundle = new Bundle();
//                                        //bundle.putString("type", loginRequest.getLogin_as());
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.profileFragment);

                             } else if (response.body().getErrors().getAccount() !=null){
                                dialog.dismiss();
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                                String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getAccount().toString())};
                                StringBuilder builder = new StringBuilder();
                                for (String i : myArray) {
                                    builder.append("" + i + " ");
                                }
                                Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(context, R.string.pendedaccount, Toast.LENGTH_SHORT).show();
//                                String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getUn_active_account())};
//                                StringBuilder builder = new StringBuilder();
//                                for (String i : myArray) {
//                                    builder.append("" + i + " ");
//                                }
//                                Toast.makeText(context, builder, Toast.LENGTH_LONG).show();


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


        signInFragmentBinding.registertextviewcustomer.setOnClickListener(view -> {
            Sal7haSharedPreference.setRole(context, Sal7haSharedPreference.Role.USER.Role);
            Navigation.findNavController(view).navigate(R.id.signInPhoneNumberFragment);
        });
        signInFragmentBinding.image.setOnClickListener(view -> {
            Sal7haSharedPreference.setRole(context, Sal7haSharedPreference.Role.AGENT.Role);
            Navigation.findNavController(view).navigate(R.id.workerSignInFragment);
        });
    }


    private boolean inputValid() {
        return emailValid() && passwordIsValid();
    }

    private boolean passwordIsValid() {
        String firstPass = signInFragmentBinding.password.getText().toString();
        if (firstPass.length() < 6) {
            signInFragmentBinding.password.setError(context.getResources().getString(R.string.lessthan6letters));
            return false;
        } else {
            return true;
        }
    }

    private void getDeviceToken(String userToken) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.i(TAG, "device token" + token);
                    setToken.setDevice_token(token);
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
                            Log.e(TAG, "onFailure: ", t);
                        }
                    });


                });
    }

    private boolean emailValid() {
        if (!signInFragmentBinding.phonenumber.getText().toString().isEmpty()) {
            return true;
        } else {
            signInFragmentBinding.phonenumber.setError(context.getResources().getString(R.string.entervalidemail));
            return false;
        }
    }

}
