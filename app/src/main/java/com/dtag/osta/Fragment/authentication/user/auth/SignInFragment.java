package com.dtag.osta.Fragment.authentication.user.auth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.authentication.user.auth.SignInViewModel;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ForgetPasswordDialogBinding;
import com.dtag.osta.databinding.SignInFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.ResetPassword;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Utility;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInFragment extends Fragment {

    private SignInViewModel mViewModel;
    SignInFragmentBinding signInFragmentBinding;
    ResetPassword resetPassword = new ResetPassword();
    private ForgetPasswordDialogBinding forgetPasswordDialogBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        signInFragmentBinding = SignInFragmentBinding.inflate(inflater, container, false);
        forgetPasswordDialogBinding = ForgetPasswordDialogBinding.inflate(inflater, container, false);
        return signInFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).hideBottomMenu();

        signInFragmentBinding.forgetpassword.setOnClickListener(view -> {
            showDialogConfirmation(getContext());
        });

        mViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        mViewModel.Init(signInFragmentBinding, getContext());
        signInFragmentBinding.googleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).signIn();

            }
        });
        signInFragmentBinding.faceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).faceBookLogin();

            }
        });
    }


//    public void dialogSendCode(Context activity) {
//        AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                .setView(view)
//                .show();
//        Button confirmBtn = view.findViewById(R.id.confirm_button_1);
//        confirmBtn.setOnClickListener(v -> {
//
//            if (inputsValid(view)) {
//
//                EditText code = view.findViewById(R.id.code_send);
//                EditText newPass = view.findViewById(R.id.newpass);
//                EditText confirmNewPass = view.findViewById(R.id.newpass2);
//
//                resetPassword.setCode(/*confirmCodeDialogBinding.codeSend.getText().toString()*/
//                        code.getText().toString());
//                resetPassword.setPassword(/*confirmCodeDialogBinding.newpass.getText().toString()*/ newPass.getText().toString());
//                resetPassword.setPassword_confirmation(/*confirmCodeDialogBinding.newpass2.getText().toString()*/confirmNewPass.getText().toString());
//                apiInterface.userSetResetPassword(resetPassword).enqueue(new Callback<ApiResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
//                        if (response.body() != null && response.isSuccessful()) {
//                            if (response.body().getStatus()) {
//                                alertDialog.dismiss();
//                                Toast.makeText(getContext(), R.string.changepassworddone, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getContext(), response.body().getErrors().getCode().get(0), Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
//                        Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//            }
//
//        });
//        Button cancelBtn = view.findViewById(R.id.cancel_button);
//        cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
//
//    }

    public void showDialogConfirmation(Context activity) {
        final Dialog dialog1 = new Dialog(activity);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.forget_password_dialog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button cancel, confirm;
        final EditText getEamilConfirmation;
        cancel = dialog1.findViewById(R.id.cancel_button);
        confirm = dialog1.findViewById(R.id.confirm_button);
        getEamilConfirmation = dialog1.findViewById(R.id.confirm_email_forget);
        confirm.setOnClickListener(v -> {
            apiInterface.userForgetPassword(getEamilConfirmation.getText().toString()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(activity, R.string.donesuccessfully, Toast.LENGTH_LONG).show();
//                            dialogSendCode(getContext());
                            dialog1.dismiss();
                        } else {
                            String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getEmail().toString())};
                            StringBuilder builder = new StringBuilder();
                            for (String i : myArray) {
                                builder.append("" + i + " ");
                            }
                            Toast.makeText(getContext(), builder, Toast.LENGTH_LONG).show();
                            // Toast.makeText(activity, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                    Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });


        });

        cancel.setOnClickListener(v -> dialog1.dismiss());
        dialog1.show();

    }


//    private boolean inputsValid(View view) {
//        return codeValid(view) && passwordIsValid(view);
//    }

//    private boolean passwordIsValid(View view) {
//        EditText newPass = view.findViewById(R.id.newpass);
//        EditText confirmNewPass = view.findViewById(R.id.newpass2);
//
//        String firstPass = newPass.getText().toString();
//        String confirmPass = confirmNewPass.getText().toString();
//
//        if (firstPass.isEmpty() || firstPass.length() < 6) {
//            newPass.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
//            confirmNewPass.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
//            return false;
//        } else if (!firstPass.equals(confirmPass)) {
//            Toast.makeText(getContext(), getContext().getResources().getString(R.string.passwordsdoesntmatch), Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//
//            return true;
//        }
//    }


    private boolean codeValid(View view) {
        EditText codeEdt = view.findViewById(R.id.code_send);
        String code = codeEdt.getText().toString();
        if (!code.isEmpty() && code.length() >= 6) {
            return true;
        }
        codeEdt.setError(getContext().getResources().getString(R.string.invalidcodee));
        return false;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (confirmCodeDialogBinding != null) {
//            ViewGroup parentViewGroup = (ViewGroup) confirmCodeDialogBinding.getRoot().getParent();
//            if (parentViewGroup != null) {
//                parentViewGroup.removeAllViews();
//            }
//        }
//    }
}

