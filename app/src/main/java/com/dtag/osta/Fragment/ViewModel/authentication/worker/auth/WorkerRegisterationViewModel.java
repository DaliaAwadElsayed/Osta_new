package com.dtag.osta.Fragment.ViewModel.authentication.worker.auth;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.dtag.osta.R;import com.dtag.osta.databinding.WorkerRegisterationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.Area;
import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.Services;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.LocationTrack;
import com.dtag.osta.utility.Sal7haSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerRegisterationViewModel extends ViewModel {
    WorkerRegisterationFragmentBinding workerRegisterationFragmentBinding;

    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    List<String> areas;
    public int City_areas_id;
    List<String> cities;
    List<String> category;
    public int category_id;
    String categoryName;
    String longitude, latitude;
    LocationTrack locationTrack;


    public void Init(WorkerRegisterationFragmentBinding workerRegisterationFragmentBinding, Context context) {

        this.context = context;
        this.workerRegisterationFragmentBinding = workerRegisterationFragmentBinding;
        workerRegisterationFragmentBinding.conditions.setOnClickListener(view -> {
            termsAndConditionsDialog();
        });
        getAreaa();
        getCategory();


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
                workerRegisterationFragmentBinding.city.setAdapter(adapter);
                workerRegisterationFragmentBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int area = response.body().getData().getCities().get(position).getId();
                        City_areas_id = response.body().getData().getCities().get(position).getId();
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
                                workerRegisterationFragmentBinding.area.setAdapter(adapter1);


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
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
            }
        });
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
                workerRegisterationFragmentBinding.servicetype.setAdapter(adapter);
                workerRegisterationFragmentBinding.servicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        workerRegisterationFragmentBinding.servicetype.setSelection(adapter.getPosition(categoryName));
                        TextView textView = (TextView) parent.getChildAt(0);
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
    }

    public void termsAndConditionsDialog() {
        final Dialog dialog = new Dialog(context);
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
                        if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
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
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

}
