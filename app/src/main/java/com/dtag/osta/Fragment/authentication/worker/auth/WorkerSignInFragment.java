package com.dtag.osta.Fragment.authentication.worker.auth;

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
import com.dtag.osta.Fragment.ViewModel.authentication.worker.auth.WorkerSignInViewModel;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ForgetPasswordDialogBinding;
import com.dtag.osta.databinding.WorkerSignInFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.common.ResetPassword;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Utility;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerSignInFragment extends Fragment {

    private WorkerSignInViewModel mViewModel;
    WorkerSignInFragmentBinding workerSignInFragmentBinding;
    private ForgetPasswordDialogBinding forgetPasswordDialogBinding;
    ResetPassword resetPassword = new ResetPassword();

    public static WorkerSignInFragment newInstance() {
        return new WorkerSignInFragment();
    }

    private Api apiInterface = RetrofitClient.getInstance().getApi();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        workerSignInFragmentBinding = WorkerSignInFragmentBinding.inflate(inflater, container, false);
        forgetPasswordDialogBinding = ForgetPasswordDialogBinding.inflate(inflater, container, false);
        return workerSignInFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).openDrawer();
        workerSignInFragmentBinding.forgetpassword.setOnClickListener(view -> {
            showDialogConfirmation(getContext());
        });

        mViewModel = ViewModelProviders.of(this).get(WorkerSignInViewModel.class);
        mViewModel.Init(workerSignInFragmentBinding, getContext());
    }

    public void progress() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
        dialog.setContentView(R.layout.progress_dialog);
        dialog.show();
    }

//    public void dialogSendCode(Context activity) {
//        View view = confirmCodeDialogBinding.getRoot();
//        final Dialog dialog2 = new Dialog(activity);
//        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog2.setCancelable(false);
//        dialog2.setContentView(view);
//        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        confirmCodeDialogBinding.confirmButton1.setOnClickListener(v -> {
//            if (inputsValid()) {
//                resetPassword.setCode(confirmCodeDialogBinding.codeSend.getText().toString());
//                resetPassword.setPassword(confirmCodeDialogBinding.newpass.getText().toString());
//                resetPassword.setPassword_confirmation(confirmCodeDialogBinding.newpass2.getText().toString());
//                apiInterface.agentSetResetPassword(resetPassword).enqueue(new Callback<ApiResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
//                        if (response.body() != null && response.isSuccessful()) {
//                            if (response.body().getStatus()) {
//                                Toast.makeText(getContext(), R.string.changepassworddone, Toast.LENGTH_SHORT).show();
//                                dialog2.dismiss();
//                            } else {
//                                String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getCode().toString())};
//                                StringBuilder builder = new StringBuilder();
//                                for (String i : myArray) {
//                                    builder.append("" + i + " ");
//                                }
//                                Toast.makeText(getContext(), builder, Toast.LENGTH_LONG).show();
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
//
//        confirmCodeDialogBinding.cancelButton.setOnClickListener(v -> dialog2.dismiss());
//        dialog2.show();
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
            apiInterface.agentForgetPassword(getEamilConfirmation.getText().toString()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_LONG).show();
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

    private boolean inputsValid() {
        return codeValid() && passwordIsValid();
    }

    private boolean passwordIsValid() {
//        String firstPass = confirmCodeDialogBinding.newpass.getText().toString();
//        String confirmPass = confirmCodeDialogBinding.newpass2.getText().toString();
//        if (firstPass.isEmpty() || firstPass.length() < 6) {
//            confirmCodeDialogBinding.newpass.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
//            confirmCodeDialogBinding.newpass2.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
//            return false;
//        } else if (!firstPass.equals(confirmPass)) {
//            Toast.makeText(getContext(), getContext().getResources().getString(R.string.passwordsdoesntmatch), Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//
            return true;
        }
//    }


    private boolean codeValid() {
//        String code = confirmCodeDialogBinding.codeSend.getText().toString();
//        if (!code.isEmpty() && code.length() >= 6) {
//            return true;
//        }
//        confirmCodeDialogBinding.codeSend.setError(getContext().getResources().getString(R.string.invalidcodee));
        return false;
    }

}
