package com.dtag.osta.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.ChangePasswordFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.ChangePassword;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {
    ChangePasswordFragmentBinding changePasswordFragmentBinding;
    Context context;
    ChangePassword changePassword = new ChangePassword();
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public void init(ChangePasswordFragmentBinding changePasswordFragmentBinding, Context context) {
        this.changePasswordFragmentBinding = changePasswordFragmentBinding;
        this.context = context;
        changePasswordFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "user");
                Navigation.findNavController(changePasswordFragmentBinding.getRoot()).navigate(R.id.profileFragment, bundle);

            }
        });
        changePasswordFragmentBinding.cancelButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "user");
                Navigation.findNavController(changePasswordFragmentBinding.getRoot()).navigate(R.id.profileFragment, bundle);

            }
        });
        changePasswordFragmentBinding.confirmId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordInputValid()) {
                    changePassword();
                }
            }
        });
    }

    private void changePassword() {
        changePasswordFragmentBinding.progressId.setVisibility(View.VISIBLE);
        changePassword.setOldPassword(changePasswordFragmentBinding.oldPasswordDialog.getText().toString());
        changePassword.setNewPassword(changePasswordFragmentBinding.newpassDialog.getText().toString());
        changePassword.setNewPasswordConfirmation(changePasswordFragmentBinding.newpassConfirmId.getText().toString());
        apiInterface.changePassword(Sal7haSharedPreference.getToken(context), changePassword).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        changePasswordFragmentBinding.progressId.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "user");
                        Navigation.findNavController(changePasswordFragmentBinding.getRoot()).navigate(R.id.profileFragment, bundle);
                        Toast.makeText(context, R.string.changepassworddone, Toast.LENGTH_LONG).show();
                    } else {
                        changePasswordFragmentBinding.progressId.setVisibility(View.GONE);
                        Toast.makeText(context, R.string.oldpasswordisincorret, Toast.LENGTH_LONG).show();
                    }

                } else {
                    changePasswordFragmentBinding.progressId.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                changePasswordFragmentBinding.progressId.setVisibility(View.GONE);
            }

        });
    }

    private boolean passwordInputValid() {
        return oldPasswordEditText() && newPasswordEditText() && confirmPasswordEditText() && equalPasswordEditText();
    }

    private boolean equalPasswordEditText() {
        String newPasstext = changePasswordFragmentBinding.newpassDialog.getText().toString();
        String confirmPasstext = changePasswordFragmentBinding.newpassConfirmId.getText().toString();
        if (newPasstext.equals(confirmPasstext)) {
            return true;
        } else {
            changePasswordFragmentBinding.newpassConfirmId.setError(context.getResources().getString(R.string.passwordsdoesntmatch));
            changePasswordFragmentBinding.newpassConfirmId.requestFocus();
            return false;
        }
    }

    private boolean confirmPasswordEditText() {
        if (changePasswordFragmentBinding.newpassConfirmId.getText().toString().matches("")) {
            changePasswordFragmentBinding.newpassConfirmId.setError(context.getResources().getString(R.string.enterpassword));
            changePasswordFragmentBinding.newpassConfirmId.requestFocus();

            return false;
        }
        return true;
    }

    private boolean newPasswordEditText() {
        String name = changePasswordFragmentBinding.newpassDialog.getText().toString();
        if (name.isEmpty()) {
            changePasswordFragmentBinding.newpassDialog.setError(context.getResources().getString(R.string.enterpassword));
            changePasswordFragmentBinding.newpassDialog.requestFocus();
            return false;
        } else if (!name.isEmpty() && name.length() < 8) {
            changePasswordFragmentBinding.newpassDialog.setError(context.getResources().getString(R.string.lessthan6letters));
            changePasswordFragmentBinding.newpassDialog.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private boolean oldPasswordEditText() {
        if (changePasswordFragmentBinding.oldPasswordDialog.getText().toString().matches("")) {
            changePasswordFragmentBinding.oldPasswordDialog.setError(context.getResources().getString(R.string.enterpassword));
            changePasswordFragmentBinding.oldPasswordDialog.requestFocus();
            return false;
        }
        return true;
    }
}