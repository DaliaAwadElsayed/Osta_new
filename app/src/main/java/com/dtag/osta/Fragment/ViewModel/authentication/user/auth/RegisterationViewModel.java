package com.dtag.osta.Fragment.ViewModel.authentication.user.auth;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;import com.dtag.osta.databinding.RegisterationFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.Area;
import com.dtag.osta.network.ResponseModel.Model.City;
import com.dtag.osta.network.ResponseModel.Model.PhoneNumber;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterationViewModel extends ViewModel {
    RegisterationFragmentBinding registerationFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    private PhoneNumber phoneNumber = new PhoneNumber();
    Context context;
    List<String> areas;
    int area_id;
    List<String> cities;
    public void Init(RegisterationFragmentBinding registerationFragmentBinding, Context context) {
        getAreaa();
        this.context = context;
        this.registerationFragmentBinding = registerationFragmentBinding;
        registerationFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.signinfragment);
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
                registerationFragmentBinding.city.setAdapter(adapter);
                registerationFragmentBinding.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                registerationFragmentBinding.area.setAdapter(adapter1);
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

}
