package com.dtag.osta.Fragment.ViewModel.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.asksira.bsimagepicker.BSImagePicker;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ProfileFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.agent.Agent;
import com.dtag.osta.network.ResponseModel.Model.common.ChangePassword;
import com.dtag.osta.network.ResponseModel.Model.user.Registeration;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.DatePickerFragment;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel implements DatePickerFragment.OnDatePicked {
    ProfileFragmentBinding profileFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Agent agent = new Agent();
    Registeration registeration = new Registeration();
    List<String> areas;
    List<String> category;
    int category_id;
    String categoryName;
    int area_id;
    private static final int TAKE_PHOTO_RC = 1003;
    private int imagePickerChoice;
    private static final int PICTURE_RC = 1001;
    List<String> cities;
    String city;
    String area;
    int city_id;
    String type;
    EditText currentPassword, newPassword, confirmPassword;
    // List<MultipartBody.Part> imagesEncoded = new ArrayList<>();
    MultipartBody.Part imagesEncoded;


    private static String TAG = "ProfileViewModel";

    public void Init(ProfileFragmentBinding profileFragmentBinding, Context context, String type) {
        this.profileFragmentBinding = profileFragmentBinding;
        this.context = context;
        this.type = type;
        getAreaa();
        profileFragmentBinding.profileImage.setOnClickListener(view -> {
                    pickUpImage();
                }
        );
        if (type.equals("user") || Sal7haSharedPreference.getRole(context).equals("user")) {
            userProfile();
            profileFragmentBinding.enter.setOnClickListener(view -> {
                editCustomerProfile();
            });
        } else {
            agentProfile();
            profileFragmentBinding.changePassword.setOnClickListener(view -> {
//                changePasswordDialogAgent();
            });
            profileFragmentBinding.enter.setOnClickListener(view -> {
                editAgentProfile();
            });
        }
        profileFragmentBinding.brithdayId.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        profileFragmentBinding.brithdayId.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showDatePickerDialog();
            }
        });


    }

    private void showDatePickerDialog() {
        DatePickerFragment pickerFragment = new DatePickerFragment(this, context, false);
        pickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "date_picker");
    }

    private void agentProfile() {
        //getCategory();
        profileFragmentBinding.userpoints.setVisibility(View.GONE);
        //  profileFragmentBinding.categorylinear.setVisibility(View.VISIBLE);
        profileFragmentBinding.rating.setVisibility(View.VISIBLE);
        profileFragmentBinding.BDlinear.setVisibility(View.VISIBLE);
        profileFragmentBinding.category.setVisibility(View.VISIBLE);
        profileFragmentBinding.addresslinear.setVisibility(View.GONE);
        apiInterface.getAgentProfile(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        city_id = response.body().getData().getAgent().getCityId();
                        String basicName = response.body().getData().getAgent().getName();
                        String name = response.body().getData().getAgent().getName();
                        String email = response.body().getData().getAgent().getEmail();
                        String phone_number = response.body().getData().getAgent().getPhone();
                        String birthdate = response.body().getData().getAgent().getBirthday();
                        if (response.body().getData().getAgent().getCategory() != null) {
                            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                                categoryName = response.body().getData().getAgent().getCategory().getName();

                            } else {
                                categoryName = response.body().getData().getAgent().getCategory().getNameEn();
                            }
                        }
                        //     city = Utility.fixNullString(response.body().getData().getAgent().getArea().getCity().getNameEn());
                        //   area = Utility.fixNullString(response.body().getData().getAgent().getArea().getNameEn());
                        profileFragmentBinding.basicName.setText(basicName);
                        profileFragmentBinding.name.setText(name);
                        profileFragmentBinding.phonenumber.setText(phone_number);
                        profileFragmentBinding.email.setText(email);
                        profileFragmentBinding.category.setText(categoryName);
                        profileFragmentBinding.brithdayId.setText(birthdate);
                        profileFragmentBinding.rate.setRating(response.body().getData().getAgent().getRate());
                        Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getAgent().getImage()).error(R.drawable.ic_profile).placeholder(R.drawable.ic_profile)
                                .into(profileFragmentBinding.profileImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void editAgentProfile() {
        String name = profileFragmentBinding.name.getText().toString();
        String email = profileFragmentBinding.email.getText().toString();
        String phone = "" + profileFragmentBinding.phonenumber.getText().toString();
        String birthDate = profileFragmentBinding.brithdayId.getText().toString();
        agent.setName(name);
        agent.setEmail(email);
        agent.setPhone(phone);
        agent.setCityId(city_id);
        agent.setBirthday(birthDate);
        apiInterface.updateAgentProfile(Sal7haSharedPreference.getToken(context), agent).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();
                    } else {
                        //  Toast.makeText(context, agent.getName() + agent.getEmail() + agent.getPhone() + agent.getCityId() + agent.getBirthday() + agent.getCategoryId(), Toast.LENGTH_SHORT).show();
                        //    Toast.makeText(context, R.string.updatefailed, Toast.LENGTH_SHORT).show();
                        if (response.body().getErrors().getBirthday() != null) {
                            String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getBirthday().toString())};
                            StringBuilder builder = new StringBuilder();
                            for (String i : myArray) {
                                builder.append("" + i + " ");
                            }
                            Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getCategory_id() != null) {
                            String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getCategory_id().toString())};
                            StringBuilder builder = new StringBuilder();
                            for (String i : myArray) {
                                builder.append("" + i + " ");
                            }
                            Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                        } else if (response.body().getErrors().getCityId() != null) {
                            String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getCityId().toString())};
                            StringBuilder builder = new StringBuilder();
                            for (String i : myArray) {
                                builder.append("" + i + " ");
                            }
                            Toast.makeText(context, response.body().getErrors().getError(), Toast.LENGTH_LONG).show();
                        }


                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void userProfile() {
        profileFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLIIICKKS", "YES");
                Navigation.findNavController(v).navigate(R.id.off);
            }
        });
        profileFragmentBinding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLIIICKKS", "YES");
                Navigation.findNavController(v).navigate(R.id.changePasswordFragment);
            }
        });
        apiInterface.getCustomerProfile(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        city_id = response.body().getData().getUser().getCityId();
                        String basicName = response.body().getData().getUser().getName();
                        String name = response.body().getData().getUser().getName();
                        String address = response.body().getData().getUser().getAddress();
                        String email = response.body().getData().getUser().getEmail();
                        String phone_number = response.body().getData().getUser().getPhone();
                        Integer points = response.body().getData().getUser().getPoints();
                        Integer credits = response.body().getData().getUser().getCredits();
                        if (response.body().getData().getUser().getSocial_type() == null) {
                            profileFragmentBinding.changePassword.setVisibility(View.VISIBLE);
                        } else if (response.body().getData().getUser().getSocial_type().equals("google") || response.body().getData().getUser().getSocial_type().equals("facebook")) {
                            profileFragmentBinding.changePassword.setVisibility(View.GONE);
                        } else {
                            profileFragmentBinding.changePassword.setVisibility(View.VISIBLE);

                        }
//                        city = Utility.fixNullString(response.body().getData().getUser().getArea().getCity().getNameEn());
                        //                 area = Utility.fixNullString(response.body().getData().getUser().getArea().getNameEn());
                        profileFragmentBinding.basicName.setText(Utility.fixNullString(basicName));
                        profileFragmentBinding.name.setText(Utility.fixNullString(name));
                        profileFragmentBinding.phonenumber.setText(Utility.fixNullString(phone_number));
                        profileFragmentBinding.email.setText(Utility.fixNullString(email));
                        profileFragmentBinding.address.setText(Utility.fixNullString(address));
                        profileFragmentBinding.pointid.setText("" + points);
                        profileFragmentBinding.creditid.setText("" + credits);
                        Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getUser().getImage()).error(R.drawable.ic_profile).placeholder(R.drawable.ic_profile)
                                .into(profileFragmentBinding.profileImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void editCustomerProfile() {
        String name = profileFragmentBinding.name.getText().toString();
        String address = profileFragmentBinding.address.getText().toString();
        String email = profileFragmentBinding.email.getText().toString();
        String phone = "" + profileFragmentBinding.phonenumber.getText().toString();

        registeration.setName(name);
        registeration.setEmail(email);
        registeration.setPhone(phone);
        registeration.setCityId(city_id);
        registeration.setAddress(address);
        apiInterface.updateUserProfile(Sal7haSharedPreference.getToken(context), registeration).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, R.string.updatefailed, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAreaa() {
        apiInterface.getCities().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                areas = new ArrayList<String>();
                for (City city : response.body().getData().getCities()) {
                    if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                        areas.add(city.getName());
                    } else {
                        areas.add(city.getNameEn());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, areas);
                //    profileFragmentBinding.city.setAdapter(adapter);

             /*   profileFragmentBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) parent.getChildAt(0);
                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        area_id = response.body().getData().getCities().get(position).getId();
                        apiInterface.getCity(area_id).enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                cities = new ArrayList<String>();
                                for (Area area : response.body().getData().getCity().getAreas()) {
                                    if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                                        cities.add(area.getName());
                                    } else {
                                        cities.add(area.getNameEn());
                                    }
                                }
                                ArrayAdapter adapter1 = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, cities);
                                profileFragmentBinding.area.setAdapter(adapter1);
                                profileFragmentBinding.area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        TextView textView = (TextView) adapterView.getChildAt(0);
                                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });*/
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* private void getCategory() {
        apiInterface.serviceCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                category = new ArrayList<String>();
                for (Services services : response.body().getData().getServicesList()) {
                    if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                        category.add(services.getName());
                    } else {
                        category.add(services.getNameEn());
                    }
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, category);
              //  profileFragmentBinding.servicetype.setAdapter(adapter);
                ////////////////////////////////////
                profileFragmentBinding.servicetype.setAdapter(adapter);
                if (categoryName != null) {
                    int spinnerPosition = adapter.getPosition(categoryName);
                    profileFragmentBinding.servicetype.setSelection(spinnerPosition);
                }
                profileFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) parent.getChildAt(0);
                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        category_id = response.body().getData().getServicesList().get(position).getId();
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
    }*/


//    public void changePasswordDialogAgent() {
//        final Dialog dialog = new Dialog(context);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.change_password_dialog);
//        Button cancel, confirm;
//        currentPassword = dialog.findViewById(R.id.old_password_dialog);
//        newPassword = dialog.findViewById(R.id.newpass_dialog);
//        confirmPassword = dialog.findViewById(R.id.newpass_confirm_id);
//        confirm = dialog.findViewById(R.id.confirm_button_dialog_change);
//        cancel = dialog.findViewById(R.id.cancel_button_change_password);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        confirm.setOnClickListener(v -> {
//            if (passwordInputValid()) {
//                changePassword.setOldPassword(currentPassword.getText().toString());
//                changePassword.setNewPassword(newPassword.getText().toString());
//                changePassword.setNewPasswordConfirmation(confirmPassword.getText().toString());
//                apiInterface.changePasswordAgent(Sal7haSharedPreference.getToken(context), changePassword).enqueue(new Callback<ApiResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
//                        if (response.body() != null && response.isSuccessful()) {
//                            if (response.body().getStatus()) {
//                                Toast.makeText(context, R.string.changepassworddone, Toast.LENGTH_LONG).show();
//                                dialog.dismiss();
//                            } else {
//                                /*
//                                String[] oldPassword = new String[]{response.body().getErrors().getAccount().toString()};
//                                StringBuilder builder = new StringBuilder();
//                                for (String i : oldPassword) {
//                                    builder.append("" + i + " ");
//                                }
//                                */
//                                //Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
//                                // Toast.makeText(context, R.string.oldpasswordisincorret, Toast.LENGTH_SHORT).show();
//                                //  Toast.makeText(context, R.string.thereisanerrorinthedata, Toast.LENGTH_SHORT).show();
//                                String errorInPass = response.body().getErrors().getPassword().get(0);
//                                Toast.makeText(context, errorInPass, Toast.LENGTH_LONG).show();
//                            }
//
//                        } else {
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
//                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
//                    }
//
//                });
//            }
//
//        });
//        cancel.setOnClickListener(v -> dialog.dismiss());
//        dialog.show();
//    }

    @Override
    public void onDateSetDone(String dateText) {
        profileFragmentBinding.brithdayId.setText(dateText);
    }

    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        Picasso.get().load(uriList.get(0)).placeholder(R.drawable.ic_profile).into(profileFragmentBinding.profileImage);
        imagesEncoded = null;
        imagesEncoded = Utility.compressImage(context, uriList.get(0), "image");
        profileFragmentBinding.progressImage.setVisibility(View.VISIBLE);
        profileFragmentBinding.profileImage.setEnabled(false);
        if (Sal7haSharedPreference.getRole(context).equals("agent")) {
            apiInterface.agentupdateImage(Sal7haSharedPreference.getToken(context), imagesEncoded).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();
                            profileFragmentBinding.progressImage.setVisibility(View.GONE);
                            profileFragmentBinding.profileImage.setEnabled(true);
                        } else {
                            profileFragmentBinding.profileImage.setEnabled(true);
                            profileFragmentBinding.progressImage.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.updatefailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    profileFragmentBinding.profileImage.setEnabled(true);
                    profileFragmentBinding.progressImage.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, t.getMessage());

                }
            });
        } else {
            apiInterface.userupdateImage(Sal7haSharedPreference.getToken(context), imagesEncoded).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            profileFragmentBinding.progressImage.setVisibility(View.GONE);
                            profileFragmentBinding.profileImage.setEnabled(true);
                            Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();
                        } else {
                            profileFragmentBinding.progressImage.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.updatefailed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    profileFragmentBinding.progressImage.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public void onCancelled(boolean isMultiSelecting, String tag) {
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICTURE_RC) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, context.getResources().getString(R.string.cameraaccessdenied), Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, context.getResources().getString(R.string.accesstomemoryusagedenied), Toast.LENGTH_SHORT).show();
            }
            if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, context.getResources().getString(R.string.accesstomemoryusagedenied), Toast.LENGTH_SHORT).show();
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                pickUpImage();
            }
        }
    }

    public void pickUpImage() {
        pickImageFromGallery();
    }

    public void pickImageFromGallery() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.dalia.osta.fileprovider")
                .setTag("profile")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(1)
                .build();
        pickerDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "picker");
    }

    public void onSingleImageSelected(Uri uri, String tag) {
        File file = new File(Utility.getRealPathFromURI(context, uri));
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        //imagesEncoded = part;
    }

}
