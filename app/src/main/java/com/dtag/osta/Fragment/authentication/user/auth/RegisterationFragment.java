package com.dtag.osta.Fragment.authentication.user.auth;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.authentication.user.auth.RegisterationViewModel;
import com.dtag.osta.Fragment.maps.MapFragment;
import com.dtag.osta.R;
import com.dtag.osta.databinding.RegisterationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.Interface.OnInputSelected;
import com.dtag.osta.network.ResponseModel.Model.Area;
import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.PhoneNumber;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.Model.user.Registeration;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.network.ResponseModel.wrapper.SetToken;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterationFragment extends Fragment implements OnInputSelected {
    RegisterationFragmentBinding registerationFragmentBinding;
    private RegisterationViewModel mViewModel;
    LoginRequest loginRequest = new LoginRequest();
    SetToken setToken = new SetToken();
    CountryCodePicker ccp;
    public OnInputSelected mOnInputSelected;

    public void setmOnInputSelected(OnInputSelected mOnInputSelected) {
        this.mOnInputSelected = mOnInputSelected;
    }

    public static RegisterationFragment newInstance() {
        return new RegisterationFragment();
    }

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private Registeration registeration = new Registeration();
    String TAG = "registeration";
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private PhoneNumber phoneNumber = new PhoneNumber();
    Context context;
    List<String> areas;
    int area_id;
    List<String> cities;
    String longitude, latitude;
    int PLACE_PICKER_REQUEST = 1;
    MapFragment dialog = new MapFragment();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerationFragmentBinding = RegisterationFragmentBinding.inflate(inflater, container, false);
        return registerationFragmentBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).openDrawer();

        ccp = (CountryCodePicker) registerationFragmentBinding.getRoot().findViewById(R.id.ccp);

        getAreaa();
        registerationFragmentBinding.conditions.setOnClickListener(view -> {
            termsAndConditionsDialog();
        });
        if (Sal7haSharedPreference.getSelectedLanguage(getContext()) == 0) {
            registerationFragmentBinding.firstname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.firstname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.phoneId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (phoneValid()) {
                        registerationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.passwordId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordStrongValid()) {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            registerationFragmentBinding.confirmId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordsIsValid()) {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                        registerationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
                    } else {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        registerationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            registerationFragmentBinding.selectLocationTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            registerationFragmentBinding.firstname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.firstname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        registerationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.secondname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.secondname.getText().toString().length() >= 1) {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            registerationFragmentBinding.phoneId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (registerationFragmentBinding.phoneId.getText().toString().length() >= 11) {
                        if (registerationFragmentBinding.phoneId.getText().charAt(0) == 0) {
                            String phoneNum = registerationFragmentBinding.phoneId.getText().toString().substring(1);
                            registerationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources()
                                    .getDrawable(R.drawable.done), null, null, null);
                        }
                    } else {
                        registerationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            registerationFragmentBinding.passwordId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordStrongValid()) {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            registerationFragmentBinding.confirmId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (passwordsIsValid()) {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                        registerationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.done), null, null, null);
                    } else {
                        registerationFragmentBinding.passwordId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        registerationFragmentBinding.confirmId.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }

                }
            });
            registerationFragmentBinding.selectLocationTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        registerationFragmentBinding.checkbox.setOnClickListener(view -> {
            if (registerationFragmentBinding.checkbox.isChecked()) {
                registerationFragmentBinding.confirmId.setEnabled(true);
                registerationFragmentBinding.confirmId.setOnClickListener(view1 -> {
                    if (inputValid()) {
                        userRegisteration();
                    }
                });

            } else {
                registerationFragmentBinding.confirmId.setEnabled(false);
            }
        });

//        registerationFragmentBinding.informationPasswordId.setOnClickListener(view -> {
//            final Dialog dialog = new Dialog(getContext());
//            dialog.setCancelable(false);
//            if (dialog != null) {
//                int width = ViewGroup.LayoutParams.FILL_PARENT;
//                int height = ViewGroup.LayoutParams.MATCH_PARENT;
//                dialog.getWindow().setLayout(width, height);
//            }
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.setContentView(R.layout.password_helper_dialog);
//            dialog.show();
//            Button done;
//            done = dialog.findViewById(R.id.doneInformationId);
//            done.setOnClickListener(view12 -> dialog.dismiss());
//        });
        registerationFragmentBinding.selectLocationTxt.setOnClickListener(view -> {
            dialog.setmOnInputSelected(this);
            dialog.setTargetFragment(RegisterationFragment.this, 1);
            dialog.show(getFragmentManager(), "MyCustomDialog");
        });
        Log.i(TAG, "code : " + getArguments().getString("code"));
        mViewModel = ViewModelProviders.of(this).get(RegisterationViewModel.class);
        mViewModel.Init(registerationFragmentBinding, getContext());
    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(String.valueOf(permissionsRejected.get(0)))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNeutralButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean inputValid() {
        return firstNameValid() && secondNameValid() && thirdNameValid() && phoneValid() && passwordStrongValid() && confirmPasswordStrongValid() && passwordsIsValid() ;
    }



    private boolean confirmPasswordStrongValid() {
        String name = registerationFragmentBinding.confirmId.getText().toString();
        if (name.isEmpty()) {
            registerationFragmentBinding.confirmId.setError(getContext().getResources().getString(R.string.enterpassword));
            registerationFragmentBinding.confirmId.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordStrongValid() {
        String passwordhere = registerationFragmentBinding.passwordId.getText().toString();
        List<String> errorList = new ArrayList<String>();
        if (!isValidPassword(passwordhere, errorList)) {
            //  System.out.println("The password entered here  is invalid");
            registerationFragmentBinding.passwordId.setError(getContext().getResources().getString(R.string.passwordmustbemorethan6characters));
            registerationFragmentBinding.passwordId.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String passwordhere, List<String> errorList) {
        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean flag = true;

        if (passwordhere.length() < 8) {
            errorList.add("Password lenght must have alleast 8 character !!");
            flag = false;
        }
        if (!specailCharPatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one specail character !!");
            flag = false;
        }
        if (!UpperCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one uppercase character !!");
            flag = false;
        }
        if (!lowerCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one lowercase character !!");
            flag = false;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            errorList.add("Password must have atleast one digit character !!");
            flag = false;
        }
        return flag;

    }


    private boolean passwordsIsValid() {
        String firstPass = registerationFragmentBinding.passwordId.getText().toString();
        String confirmPass = registerationFragmentBinding.confirmId.getText().toString();
        if (!firstPass.equals(confirmPass)) {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.passwordsdoesntmatch), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /*private boolean emailValid() {
        String email = registerationFragmentBinding.email.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            registerationFragmentBinding.email.setError(getContext().getResources().getString(R.string.entervalidemail));
            return false;
        }
    }
*/
    private boolean phoneValid() {
        String name = registerationFragmentBinding.phoneId.getText().toString();
        if (!name.isEmpty() && name.length() <= 12 && name.length() >= 4) {
            registerationFragmentBinding.phoneId.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
            return true;
        }
        registerationFragmentBinding.phoneId.setError(getContext().getResources().getString(R.string.required));
        registerationFragmentBinding.phoneId.requestFocus();
        return false;
    }

    private boolean firstNameValid() {
        String name = registerationFragmentBinding.firstname.getText().toString();
        if (!name.isEmpty()) {
            registerationFragmentBinding.firstname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
            return true;
        }
        registerationFragmentBinding.firstname.setError(getContext().getResources().getString(R.string.enterurname));
        registerationFragmentBinding.firstname.requestFocus();
        return false;
    }

    private boolean secondNameValid() {
        String name = registerationFragmentBinding.secondname.getText().toString();
        if (!name.isEmpty()) {
            registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
            return true;
        }
        registerationFragmentBinding.secondname.setError(getContext().getResources().getString(R.string.entersecondname));
        registerationFragmentBinding.secondname.requestFocus();
        return false;
    }

    private boolean thirdNameValid() {
        String name = registerationFragmentBinding.secondname.getText().toString();
        if (!name.isEmpty()) {
            registerationFragmentBinding.secondname.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.drawable.done), null);
            return true;
        }
        registerationFragmentBinding.secondname.setError(getContext().getResources().getString(R.string.enterlastname));
        registerationFragmentBinding.secondname.requestFocus();
        return false;
    }

    private void userRegisteration() {
        registerationFragmentBinding.progress.setVisibility(View.VISIBLE);
        registeration.setName(registerationFragmentBinding.firstname.getText().toString() + " " + registerationFragmentBinding.secondname.getText().toString() + " " + registerationFragmentBinding.secondname.getText().toString());
        //registeration.setEmail(registerationFragmentBinding.email.getText().toString());
        if (registerationFragmentBinding.phoneId.getText().toString().charAt(0) == 0) {
            registeration.setPhone(registerationFragmentBinding.ccp.getSelectedCountryCodeWithPlus() + registerationFragmentBinding.phoneId.getText().toString().substring(1));

        } else {
            registeration.setPhone(registerationFragmentBinding.ccp.getSelectedCountryCodeWithPlus() + registerationFragmentBinding.phoneId.getText().toString());
        }
        registeration.setPassword(registerationFragmentBinding.passwordId.getText().toString());
        registeration.setPassword_confirmation(registerationFragmentBinding.confirmId.getText().toString());
        registeration.setCityId(area_id);
//        registeration.setLatitude(registerationFragmentBinding.lat.getText().toString());
//        registeration.setLongitude(registerationFragmentBinding.lang.getText().toString());
        registeration.setCode(getArguments().getString("code"));
        apiInterface.userRegister(registeration).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        registerationFragmentBinding.progress.setVisibility(View.GONE);
                        //     Toast.makeText(getContext(), R.string.successregisteration, Toast.LENGTH_LONG).show();
                        loginRequest.setEmail(getArguments().getString("phoneNumber"));
                        loginRequest.setPassword(registerationFragmentBinding.passwordId.getText().toString());
                        loginRequest.setLogin_as("user");
                        apiInterface.logIn(loginRequest).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                                if (response.body() != null && response.isSuccessful()) {
                                    if (response.body().getStatus()) {
                                        dialog.dismiss();
                                        Sal7haSharedPreference.saveUserData(getContext(), response.body().getData().getUser(), response.body().getData().getUser().getId());
                                        getDeviceToken(Sal7haSharedPreference.getToken(getContext()));
                                        Navigation.findNavController(getView()).navigate(R.id.profileFragment);
                                    } else {
                                        // Toast.makeText(getContext(), "error"+getArguments().getString("phoneNumber")+"////"+registerationFragmentBinding.password.getText().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                            }
                        });


                        //     Toast.makeText(getContext(), response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        registerationFragmentBinding.progress.setVisibility(View.GONE);
                        if (response.body().getErrors().getPhone() != null) {
                            String[] email = new String[]{response.body().getErrors().getPhone().toString()};
                            StringBuilder builder = new StringBuilder();
                            for (String i : email) {
                                builder.append("" + i + " ");
                            }
                            Toast.makeText(getContext(), builder, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getCode() != null) {
                            String[] code = new String[]{response.body().getErrors().getCode().toString()};
                            StringBuilder builder1 = new StringBuilder();
                            for (String i : code) {
                                builder1.append("" + i + " ");
                            }
                            Toast.makeText(getContext(), builder1, Toast.LENGTH_LONG).show();
                        }

                        Log.i(TAG, "haa" + registeration.getAddress() + registeration.getCode() + registeration.getName() + registeration.getPassword() + registeration.getPassword_confirmation() + registeration.getLatitude() + registeration.getLongitude() + registeration.getEmail() + registeration.getCityId());
                        //      Toast.makeText(getContext(), response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                registerationFragmentBinding.progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAreaa() {
        apiInterface.getCities().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                areas = new ArrayList<String>();
                for (City city : response.body().getData().getCities()) {
                    areas.add(city.getNameEn());
                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, areas);
                registerationFragmentBinding.city.setAdapter(adapter);
                registerationFragmentBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int area = response.body().getData().getCities().get(position).getId();
                        apiInterface.getCity(area).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                cities = new ArrayList<String>();
                                for (Area area : response.body().getData().getCity().getAreas()) {
                                    cities.add(area.getNameEn());
                                }
                                ArrayAdapter adapter1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, cities);
                                registerationFragmentBinding.area.setAdapter(adapter1);
                                registerationFragmentBinding.area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        area_id = response.body().getData().getCity().getAreas().get(i).getId();
                                        //    Toast.makeText(getContext(), area_id, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void termsAndConditionsDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.FILL_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        dialog.setContentView(R.layout.condition_dialog);
        ImageView cancel;
        TextView terms;
        ProgressBar progressBar;
        progressBar = dialog.findViewById(R.id.progressConditionId);
        terms = dialog.findViewById(R.id.conditionIdDialog);
        cancel = dialog.findViewById(R.id.canceldialog);
        cancel.setOnClickListener(v -> dialog.dismiss());
        progressBar.setVisibility(View.VISIBLE);
        apiInterface.aboutApp("terms").enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        progressBar.setVisibility(View.GONE);
                        //      Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                        if (Sal7haSharedPreference.getSelectedLanguage(getContext()) == 1) {
                            terms.setText(response.body().getData().getContentAr());
                        } else {
                            terms.setText(response.body().getData().getContentEn());
                        }

                    }
                    //      Toast.makeText(context, "nosuccess", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void sendInput(String lat, String lang, String address) {
//        registerationFragmentBinding.lat.setText(lat);
//        registerationFragmentBinding.lang.setText(lang);
//        registerationFragmentBinding.address.setText(address);
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

}
