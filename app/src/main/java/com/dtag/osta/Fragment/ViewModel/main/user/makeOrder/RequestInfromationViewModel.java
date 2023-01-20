package com.dtag.osta.Fragment.ViewModel.main.user.makeOrder;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.bsimagepicker.BSImagePicker;
import com.asksira.bsimagepicker.Utils;
import com.dtag.osta.Adapter.ImageViewAdapter;
import com.dtag.osta.Fragment.maps.MapFragment;
import com.dtag.osta.R;
import com.dtag.osta.databinding.RequestInfromationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.Interface.OnInputSelected;
import com.dtag.osta.network.ResponseModel.Model.Area;
import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.Services;
import com.dtag.osta.network.ResponseModel.Model.common.LoginRequest;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.DatePickerFragment;
import com.dtag.osta.utility.OnMediaClickListener;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dtag.osta.utility.Sal7haSharedPreference.getActivity;

public class RequestInfromationViewModel extends ViewModel
        implements DatePickerFragment.OnDatePicked,
        OnInputSelected, OnMediaClickListener {
    Context context;
    public MapFragment dialog;
    FragmentActivity fragmentActivity;
    RequestInfromationFragmentBinding requestInfromationFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    List<String> areas;
    int area_id;
    List<String> cities;
    private static final int PICTURE_RC = 1001;
    EditText phone, password;
    Button enter;
    private static final int PICK_IMAGE_RC = 1003;
    LoginRequest loginRequest = new LoginRequest();
    private static final String TAG = "RequestInfromation";
    String paymentMethod = "cash";
    //image list to upload
    String imageEncoded;
    List<MultipartBody.Part> imagesEncodedList = new ArrayList<>();
    List<String> category;
    int selectedItemIdselected;
    ImageViewAdapter imageViewAdapter;

    public RequestInfromationViewModel() {
        dialog = new MapFragment();
    }

    public void Init(RequestInfromationFragmentBinding requestInfromationFragmentBinding, Context context, int catId, int catPostion, FragmentActivity fragmentActivity, String lat, String lang, String address) {
        this.requestInfromationFragmentBinding = requestInfromationFragmentBinding;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        selectedItemIdselected = catId;

        imageViewAdapter = new ImageViewAdapter(new ArrayList<>(), context);
        Log.i(TAG, "SELECTION" + selectedItemIdselected + "" + catPostion);
        ///////
        apiInterface.serviceCategories().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                category = new ArrayList<String>();
                ArrayList<Integer> catIdList = new ArrayList<Integer>();
                for (Services services : response.body().getData().getServicesList()) {
                    catIdList.add(services.getId());
                    if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                        category.add(services.getName());
                    } else {
                        category.add(services.getNameEn());
                    }
                    requestInfromationFragmentBinding.servicetype.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            requestInfromationFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    String item = String.valueOf(requestInfromationFragmentBinding.servicetype.getItemAtPosition(i));
                                    Log.i(TAG, "xxx" + catIdList + adapterView.getItemAtPosition(i) + services.getId());
                                    selectedItemIdselected = catIdList.get(i);
                                    Log.i(TAG,"NEW ID"+selectedItemIdselected);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            return false;
                        }
                    });
//                    requestInfromationFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                            Log.i(TAG, "xxx" + services.getId());
//                            selectedItemIdselected = services.getId();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
                }
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, category);
                requestInfromationFragmentBinding.servicetype.setAdapter(adapter);
                requestInfromationFragmentBinding.servicetype.setSelection(catPostion);

                //  int pos = adapter.getItem(catPostion);
                // requestInfromationFragmentBinding.servicetype.setSelection(selectedCatId);
                //    requestInfromationFragmentBinding.servicetype.setSelection(catId);
//                if (catId != -1) {
//                    int spinnerPosition = adapter.getPosition(catId);
//                    requestInfromationFragmentBinding.servicetype.setSelection(spinnerPosition);
//                }
            /*  requestInfromationFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) parent.getChildAt(0);
                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        catId = response.body().getData().getServicesList().get(position).getId();
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
/////
//        getCategory();
        requestInfromationFragmentBinding.lat.setText(lat);
        requestInfromationFragmentBinding.lang.setText(lang);
//        requestInfromationFragmentBinding.address.setText(address);
//        requestInfromationFragmentBinding.address.setText(Sal7haSharedPreference.getAddress(context));
        int areaId = Sal7haSharedPreference.getArea(context);
        int cityId = Sal7haSharedPreference.getCity(context);
        requestInfromationFragmentBinding.area.setSelection(areaId);
        requestInfromationFragmentBinding.city.setSelection(cityId);


//        requestInfromationFragmentBinding.progress.setVisibility(View.VISIBLE);
        getAreaa();
        requestInfromationFragmentBinding.verfyId.setOnClickListener(view -> {
            checkCopoun();
        });
        requestInfromationFragmentBinding.radiogrp.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            if (null != rb) {
                // Toast.makeText(context, rb.getText(), Toast.LENGTH_SHORT).show();
                if (rb.getText().equals("Credit Card") || rb.getText().equals("البطاقه الائتمانيه")) {
                    paymentMethod = "visa";
                } else if (rb.getText().equals("Cash") || rb.getText().equals("كاش")) {
                    paymentMethod = "cash";
                }
                // Toast.makeText(context, paymentMethod, Toast.LENGTH_SHORT).show();
            }
        });

        requestInfromationFragmentBinding.pictureRv.setLayoutManager(new
                LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        requestInfromationFragmentBinding.pictureRv.setAdapter(imageViewAdapter);

        if (Sal7haSharedPreference.isLoggedIn(context)) {
            if (Sal7haSharedPreference.getRole(context).equals("agent")) {
                requestInfromationFragmentBinding.createOrder.setEnabled(false);
            } else if (Sal7haSharedPreference.getRole(context).equals("user")) {
                requestInfromationFragmentBinding.createOrder.setEnabled(true);
                requestInfromationFragmentBinding.createOrder.setOnClickListener(view -> {

//                    if (BuildConfig.DEBUG) {
//                        requestInfromationFragmentBinding.lat.setText("33.2222");
//                        requestInfromationFragmentBinding.lang.setText("33.2222");
//
//                    }

                    if (inputValid()) {
                        requestInfromationFragmentBinding.progressMakeOrder.setVisibility(View.VISIBLE);
//                    MultipartBody.Part[] images = (MultipartBody.Part[]) imagesEncodedList.toArray();
                        requestInfromationFragmentBinding.createOrder.setEnabled(false);
                        Log.i(TAG, "selectedcat" + selectedItemIdselected);

                        apiInterface.makeOrder(Sal7haSharedPreference.getToken(context),

                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(selectedItemIdselected)),
//                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.address.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(cityId)),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.copounid.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.lat.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.lang.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.dateBtn.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.timeBtn.getText().toString()),
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), requestInfromationFragmentBinding.descrption.getText().toString()),
                                imagesEncodedList,
                                RequestBody.create(okhttp3.MediaType.parse("text/plain"), paymentMethod))
                                .enqueue(new Callback<ApiResponse>() {

                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.body() != null && response.isSuccessful()) {
                                            if (response.body().getStatus()) {
                                                requestInfromationFragmentBinding.createOrder.setEnabled(true);
                                                requestInfromationFragmentBinding.progressMakeOrder.setVisibility(View.GONE);
                                                Log.i(TAG, "IMAGEENCODEDLIST" + imagesEncodedList.toString());
                                                Navigation.findNavController(view).navigate(R.id.offersFragment);
                                                Toast.makeText(context, R.string.therequestwassuccessfullycompleted, Toast.LENGTH_SHORT).show();
                                            } else {
                                                requestInfromationFragmentBinding.createOrder.setEnabled(true);
                                                requestInfromationFragmentBinding.progressMakeOrder.setVisibility(View.GONE);

                                                if (response.body().getErrors().getDate() != null) {
                                                    String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getDate().toString())};
                                                    StringBuilder builder = new StringBuilder();
                                                    for (String i : myArray) {
                                                        builder.append("" + i + " ");
                                                    }
                                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                                } else if (
                                                        response.body().getErrors().getTime() != null) {
                                                    String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getTime().toString())};
                                                    StringBuilder builder = new StringBuilder();
                                                    for (String i : myArray) {
                                                        builder.append("" + i + " ");
                                                    }
                                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                                } else if (response.body().getErrors().getLatitude() != null) {
                                                    String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getLatitude().toString())};
                                                    StringBuilder builder = new StringBuilder();
                                                    for (String i : myArray) {
                                                        builder.append("" + i + " ");
                                                    }
                                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                                } else if (response.body().getErrors().getLongitude() != null) {
                                                    String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getLatitude().toString())};
                                                    StringBuilder builder = new StringBuilder();
                                                    for (String i : myArray) {
                                                        builder.append("" + i + " ");
                                                    }
                                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                                } else if (response.body().getErrors().getCoupon() != null) {
                                                    String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getCoupon().toString())};
                                                    StringBuilder builder = new StringBuilder();
                                                    for (String i : myArray) {
                                                        builder.append("" + i + " ");
                                                    }
                                                    Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                                                } else if (response.body().getErrors().getAgent_id() != null) {
                                                    unAvailable();
                                                }

                                                // Toast.makeText(context, "status false", Toast.LENGTH_SHORT).show();
                                                //  Toast.makeText(context, order.getDescription() + order.getCategoryId() + order.getAddress() + order.getCityId() + order.getLatitude() + order.getLongitude() + order.getTime() + order.getDate(), Toast.LENGTH_LONG).show();
                                                // Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(context, R.string.somethinghappenswrong, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        requestInfromationFragmentBinding.createOrder.setEnabled(true);
                                        requestInfromationFragmentBinding.progressMakeOrder.setVisibility(View.GONE);
                                        Log.e(TAG, "Onfailure" + t);
                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }

        } else {
            //   Toast.makeText(context, R.string.youhavetosignin, Toast.LENGTH_SHORT).show();
            logIn();
        }
    }

    private void checkCopoun() {
        apiInterface.checkCopoun(requestInfromationFragmentBinding.copounid.getText().toString()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, R.string.codevalid, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.codeinvalid, Toast.LENGTH_SHORT).show();
                        requestInfromationFragmentBinding.copounid.setError(context.getResources().getString(R.string.codevalid));
                        requestInfromationFragmentBinding.copounid.findFocus();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private boolean inputValid() {
        return date() && time() && addValid() && latValid() && longValid() && descrptionValid();
    }

    private boolean time() {

        String name = requestInfromationFragmentBinding.timeBtn.getText().toString();
        if (!name.isEmpty()) {
            return true;
        }
        requestInfromationFragmentBinding.timeBtn.setError(context.getResources().getString(R.string.choosetime));
        requestInfromationFragmentBinding.timeBtn.requestFocus();
        return false;
    }

    private boolean date() {
        String name = requestInfromationFragmentBinding.dateBtn.getText().toString();
        if (!name.isEmpty()) {
            return true;
        }
        requestInfromationFragmentBinding.dateBtn.setError(context.getResources().getString(R.string.youmustchecktheightdate));
        requestInfromationFragmentBinding.dateBtn.requestFocus();

        return false;
    }

    private boolean descrptionValid() {
        String descrption = requestInfromationFragmentBinding.descrption.getText().toString();
        if (!descrption.isEmpty() && descrption.length() >= 15) {
            return true;
        }
        requestInfromationFragmentBinding.descrption.setError(context.getResources().getString(R.string.descrptionmustbegreaer));
        requestInfromationFragmentBinding.descrption.requestFocus();

        return false;
    }

    private boolean longValid() {
        String lan = requestInfromationFragmentBinding.lang.getText().toString();
        if (!lan.isEmpty()) {
            return true;
        }
        requestInfromationFragmentBinding.lang.setError(context.getResources().getString(R.string.required));
        requestInfromationFragmentBinding.lang.requestFocus();
        return false;
    }

    private boolean latValid() {
        String latstring = requestInfromationFragmentBinding.lat.getText().toString();
        if (!latstring.isEmpty()) {
            return true;
        }
        requestInfromationFragmentBinding.lat.setError(context.getResources().getString(R.string.required));
        requestInfromationFragmentBinding.lat.requestFocus();
        return false;
    }

    private boolean addValid() {
//        String addstring = requestInfromationFragmentBinding.address.getText().toString();
//        if (!addstring.isEmpty()) {
//            return true;
//        }
//        requestInfromationFragmentBinding.address.setError(context.getResources().getString(R.string.required));
//        requestInfromationFragmentBinding.address.requestFocus();
        return false;
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
                requestInfromationFragmentBinding.city.setAdapter(adapter);
                requestInfromationFragmentBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int area = response.body().getData().getCities().get(position).getId();
                        area_id = response.body().getData().getCities().get(position).getId();
                        apiInterface.getCity(area).enqueue(new Callback<ApiResponse>() {
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
                                requestInfromationFragmentBinding.area.setAdapter(adapter1);
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
                });

//                requestInfromationFragmentBinding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File saveBitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outputStream = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");
        }
        try {
            outputStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private String getImagePath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null, null);
        cursor.moveToNext();
        String documentId = cursor.getString(0);

        documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null, null
        );
        cursor.moveToNext();
        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return imagePath;
    }

    @Override
    public void onDateSetDone(String dateText) {
        requestInfromationFragmentBinding.dateBtn.setText(dateText);
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
                //  pickUpImage();
            }
        }
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fragmentActivity.startActivityForResult(cameraIntent, PICK_IMAGE_RC);
    }

    public void pickImageFromGallery() {
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.dtag.osta.fileprovider")
                .setTag("makeOrder")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
                .useFrontCamera()
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(10)
                .build();
        pickerDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "picker");
    }

    public void pickImageFromCamera() {
        BSImagePicker singleSelectionPicker = new BSImagePicker.Builder("com.dtag.osta.fileprovider")
                .setMaximumDisplayingImages(24) //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                .hideGalleryTile() //Default: show. Set this if you don't want to further let user select from a gallery app. In such case, I suggest you to set maximum displaying images to Integer.MAX_VALUE.
                .setTag("makeOrder") //Default: null. Set this if you need to identify which picker is calling back your fragment / activity.
                .build();
//        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.dtag.sal7ha.fileprovider")
//                .setTag("makeOrder")
//                .build();
        singleSelectionPicker.show(((AppCompatActivity) context).getSupportFragmentManager(), "picker");
    }

    public void logIn() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.login_dialog);
        ImageView imageView;
        TextView register;
        ProgressBar progressBar;
        imageView = dialog.findViewById(R.id.imgcanceldialog);
        phone = dialog.findViewById(R.id.phoneNumberDailog);
        password = dialog.findViewById(R.id.passwordDialog);
        enter = dialog.findViewById(R.id.logindialog);
        register = dialog.findViewById(R.id.registerIdDialog);
        progressBar = dialog.findViewById(R.id.progressdialog);

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.FILL_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        register.setOnClickListener(view -> {
            dialog.dismiss();
            Navigation.findNavController(requestInfromationFragmentBinding.getRoot()).navigate(R.id.signInPhoneNumberFragment);
        });
        enter.setOnClickListener(view -> {
            if (inputValidLogin()) {
                progressBar.setVisibility(View.VISIBLE);
                loginRequest.setEmail(phone.getText().toString());
                loginRequest.setPassword(password.getText().toString());
                loginRequest.setLogin_as("user");
                loginRequest.setDevice_token("xyz");
                apiInterface.logIn(loginRequest).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                requestInfromationFragmentBinding.createOrder.setEnabled(true);
                                Sal7haSharedPreference.saveUserData(context, response.body().getData().getUser(), response.body().getData().getUser().getId());
                                progressBar.setVisibility(View.GONE);
                                dialog.dismiss();
//                                        //bundle.putString("type", loginRequest.getLogin_as());
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                            } else if (response.body().getErrors().getAccount() != null) {
                                progressBar.setVisibility(View.GONE);
                                requestInfromationFragmentBinding.createOrder.setEnabled(false);
                                // Toast.makeText(context, response.body().getStatus().toString(), Toast.LENGTH_SHORT).show();
                                String[] myArray = new String[]{Utility.fixNullString(response.body().getErrors().getAccount().toString())};
                                StringBuilder builder = new StringBuilder();
                                for (String i : myArray) {
                                    builder.append("" + i + " ");
                                }
                                Toast.makeText(context, builder, Toast.LENGTH_LONG).show();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, R.string.pendedaccount, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
        dialog.show();
        imageView.setOnClickListener(view -> {
            dialog.dismiss();
        });

    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    }

    private boolean inputValidLogin() {
        return emailValid() && passwordIsValid();
    }

    private boolean passwordIsValid() {
        String firstPass = password.getText().toString();
        if (firstPass.length() < 6) {
            password.setError(context.getResources().getString(R.string.lessthan6letters));
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValid() {
        if (!phone.getText().toString().isEmpty()) {
            return true;
        } else {
            phone.setError(context.getResources().getString(R.string.entervalidemail));
            return false;
        }
    }

    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        imagesEncodedList = Utility.compressImageArray(context, uriList, "images");
        imageViewAdapter.imageList(uriList);
        Log.i("IMAGE_LIST",imagesEncodedList.toString());
    }

    public void onCancelled(boolean isMultiSelecting, String tag) {

    }

    public void onSingleImageSelected(Uri uri, String tag) {
        imagesEncodedList = Collections.singletonList(Utility.compressImage(context, uri, "images[0]"));
        imageViewAdapter.imageList(Collections.singletonList(uri));
        Log.i("IMAGE_LIST",imagesEncodedList.toString());
    }


    public void loadImage(Uri imageUri, ImageView ivImagoe) {

    }

    public void unAvailable() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        dialog.setContentView(R.layout.unavailable_dialog);
        Button cancel;
        cancel = dialog.findViewById(R.id.okDialog);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void sendInput(String lat, String lang, String address) {
        requestInfromationFragmentBinding.lat.setText(lat);
        requestInfromationFragmentBinding.lang.setText(lang);
//        requestInfromationFragmentBinding.address.setText(address);
    }

    private void getCategory() {
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
                requestInfromationFragmentBinding.servicetype.setAdapter(adapter);
                //    requestInfromationFragmentBinding.servicetype.setSelection(catId);
//                if (catId != -1) {
//                    int spinnerPosition = adapter.getPosition(catId);
//                    requestInfromationFragmentBinding.servicetype.setSelection(spinnerPosition);
//                }
            /*  requestInfromationFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView textView = (TextView) parent.getChildAt(0);
                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        catId = response.body().getData().getServicesList().get(position).getId();
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

    @Override
    public void openMedia(View view, String mediaUri) {

    }

    @Override
    public void removeMedia(int position) {
//        imagesEncodedList.remove(imagesEncodedList.get(position));
//        imageViewAdapter.notifyItemRemoved(position);

    }

    @Override
    public void removeVideo(int position) {

    }
}