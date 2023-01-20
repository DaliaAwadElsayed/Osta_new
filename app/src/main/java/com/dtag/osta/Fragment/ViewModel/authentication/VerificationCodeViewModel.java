package com.dtag.osta.Fragment.ViewModel.authentication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.VerificationCodeFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.Interface.OnReciveCode;
import com.dtag.osta.network.ResponseModel.Model.PhoneNumber;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeViewModel extends ViewModel {
    VerificationCodeFragmentBinding verificationCodeFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private PhoneNumber phoneNumber = new PhoneNumber();
    Context context;
    private static final String TAG = "VERFICATIONCODE";
    String phoneNumberArgument;

    public void Init(VerificationCodeFragmentBinding verificationCodeFragmentBinding, Context context, String phoneNumberArgument) {
        this.verificationCodeFragmentBinding = verificationCodeFragmentBinding;
        this.context = context;
        this.phoneNumberArgument = phoneNumberArgument;
        verificationCodeFragmentBinding.sendagainlinear.setAlpha(0.5f);
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                verificationCodeFragmentBinding.number.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                verificationCodeFragmentBinding.sendagainlinear.setAlpha(1f);
                verificationCodeFragmentBinding.sendagainlinear.setOnClickListener(view -> {

                    apiInterface.sendPhoneNumber(phoneNumberArgument).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    verificationCodeFragmentBinding.enter.setOnClickListener(view1 -> {
                                        if (codeValid()) {
                                            final Dialog dialog = new Dialog(context);
                                            dialog.setCancelable(false);
                                            if (dialog != null) {
                                                int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                                dialog.getWindow().setLayout(width, height);
                                            }
                                            dialog.setContentView(R.layout.progress_dialog);
                                            dialog.show();
                                            if (Sal7haSharedPreference.getRole(context).equals(Sal7haSharedPreference.Role.USER)) {
                                                codeValidation(new OnReciveCode() {
                                                    @Override
                                                    public void onSuccess(String code) {
                                                        codeSucess();
                                                        dialog.dismiss();
                                                        Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("phoneNumber", phoneNumberArgument);
                                                        Navigation.findNavController(view).navigate(R.id.registerationFragment, bundle);
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        codeFail();
                                                        dialog.dismiss();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else if (Sal7haSharedPreference.getRole(context).equals(Sal7haSharedPreference.Role.AGENT)) {
                                                codeValidation(new OnReciveCode() {
                                                    @Override
                                                    public void onSuccess(String code) {
                                                        codeSucess();
                                                        dialog.dismiss();
                                                        Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("phoneNumber", phoneNumberArgument);
                                                        Navigation.findNavController(view).navigate(R.id.workerRegisterationFragment, bundle);
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        codeFail();
                                                        dialog.dismiss();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }

                                    });
                                } else {
                                    String[] myArray = new String[]{response.body().getErrors().getEmail().toString()};
                                    StringBuilder builder = new StringBuilder();
                                    for (String i : myArray) {
                                        builder.append("" + i + " ");
                                    }
                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                    //  Toast.makeText(context, response.body().getErrors().getPhone(), Toast.LENGTH_SHORT).show();
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
        }.start();

        verificationCodeFragmentBinding.enter.setOnClickListener(view -> {
            if (codeValid()) {
                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                if (dialog != null) {
                    int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setLayout(width, height);
                }
                dialog.setContentView(R.layout.progress_dialog);
                dialog.show();
                if (Sal7haSharedPreference.getRole(context).equals("user")) {
                    codeValidation(new OnReciveCode() {
                        @Override
                        public void onSuccess(String code) {
                            codeSucess();
                            dialog.dismiss();
                            Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("phoneNumber", phoneNumberArgument);
                            bundle.putString("code", verificationCodeFragmentBinding.phonenumber.getText().toString());
                            Navigation.findNavController(view).navigate(R.id.registerationFragment, bundle);
                        }

                        @Override
                        public void onError(String error) {
                            codeFail();
                            dialog.dismiss();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (Sal7haSharedPreference.getRole(context).equals("agent")) {
                    codeValidation(new OnReciveCode() {
                        @Override
                        public void onSuccess(String code) {
                            codeSucess();
                            dialog.dismiss();
                            Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("code", verificationCodeFragmentBinding.phonenumber.getText().toString());
                            bundle.putString("phoneNumber", phoneNumberArgument);
                            Navigation.findNavController(view).navigate(R.id.workerRegisterationFragment, bundle);
                        }

                        @Override
                        public void onError(String error) {
                            codeFail();
                            dialog.dismiss();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void codeSucess() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.code_sucess_dialog);
        Button done;
        done = dialog.findViewById(R.id.done);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        done.setOnClickListener(v -> {
            dialog.dismiss();
            verificationCodeFragmentBinding.linearLayout2.setAlpha(1f);

        });
        dialog.show();

    }

    public void codeFail() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.code_sucess_dialog);
        ImageView imageView;
        TextView textView;
        Button done;
        imageView = dialog.findViewById(R.id.imagedialog);
        textView = dialog.findViewById(R.id.textdialog);
        done = dialog.findViewById(R.id.done);
//        imageView.setImageResource(R.drawable.key);/////
        textView.setText(context.getResources().getString(R.string.invalidcodee));
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        done.setOnClickListener(v -> {
            dialog.dismiss();
            verificationCodeFragmentBinding.linearLayout2.setAlpha(1f);

        });
        dialog.show();

    }

    private void codeValidation(OnReciveCode onReciveCode) {
        String codeString = verificationCodeFragmentBinding.phonenumber.getText().toString();
        phoneNumber.setCode(codeString);
        apiInterface.verifyCode(phoneNumber).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        onReciveCode.onSuccess(response.body().getMessage());

                    } else {
                        String[] myArray = new String[]{response.body().getErrors().getCode().toString()};
                        StringBuilder builder = new StringBuilder();
                        for (String i : myArray) {
                            builder.append("" + i + " ");
                        }
                        onReciveCode.onError(builder.toString());
                        //Toast.makeText(context, builder, Toast.LENGTH_SHORT).show();
                    }
                }
            }
           /*
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        onReciveCode.onSuccess(response.body().getMessage());
                    } else {
                        String errorInPass = response.body().getErrors().getCode().get(0);
                        onReciveCode.onError(errorInPass);//////////////////////////////////////////////////
                        Toast.makeText(context, errorInPass, Toast.LENGTH_LONG).show();


                        String[] myArray = new String[]{response.body().getErrors().getCode().toString()};
                        StringBuilder builder = new StringBuilder();
                        for (String i : myArray) {
                            builder.append("" + i + " ");
                        }
                        onReciveCode.onError(builder.toString());


                        //Toast.makeText(context, builder, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            */

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                onReciveCode.onError(context.getResources().getString(R.string.internetconnection));
                //Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean codeValid() {
        if (!verificationCodeFragmentBinding.phonenumber.getText().toString().isEmpty()) {
            return true;
        } else {
            verificationCodeFragmentBinding.phonenumber.setError(context.getResources().getString(R.string.entercode));
            verificationCodeFragmentBinding.phonenumber.requestFocus();
            return false;
        }


    }
}
