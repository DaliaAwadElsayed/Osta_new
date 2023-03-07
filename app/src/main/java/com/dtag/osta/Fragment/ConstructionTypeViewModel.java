package com.dtag.osta.Fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.dtag.osta.Adapter.ConstructionOrderAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.ConstructionTypeFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConstructionTypeViewModel extends ViewModel {
    ConstructionTypeFragmentBinding constructionTypeFragmentBinding;
    Context context;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    List<Order> orders = new ArrayList<>();
    private ConstructionOrderAdapter requestAdapter;

    public void init(ConstructionTypeFragmentBinding constructionTypeFragmentBinding, Context context) {
        this.constructionTypeFragmentBinding = constructionTypeFragmentBinding;
        this.context = context;
        List<String> categories = new ArrayList();
        categories.add(context.getResources().getString(R.string.newpreview));
        categories.add(context.getResources().getString(R.string.completedpreview));
        categories.add(context.getResources().getString(R.string.ratedpreview));
        categories.add(context.getResources().getString(R.string.cancelledpreview));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(context, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        constructionTypeFragmentBinding.spinnerStatus.setAdapter(dataAdapter);
        constructionTypeFragmentBinding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                constructionTypeFragmentBinding.progress.setVisibility(View.VISIBLE);
                constructionTypeFragmentBinding.noResultLinear.setVisibility(View.INVISIBLE);
                ArrayList<String> type1;
                type1 = new ArrayList<String>();
                if (position == 0) {
                    type1.add("new");
                    type1.add("on_way");
                    type1.add("arrived");
                    type1.add("approved");
                } else if (position == 1) {
                    type1.add("completed");
                } else if (position == 2) {
                    type1.add("rated");
                } else if (position == 3) {
                    type1.add("cancelled");
                }
                requestAdapter = new ConstructionOrderAdapter(context);
                constructionTypeFragmentBinding.homeRecyclerView.setAdapter(requestAdapter);
                constructionTypeFragmentBinding.progress.setVisibility(View.VISIBLE);

                ConstructionOrderAdapter.OnModelClickListener listener = new ConstructionOrderAdapter.OnModelClickListener() {
                    @Override
                    public void sendModelId(int id) {
                        requestList(type1);
                        requestAdapter.notifyDataSetChanged();
                    }
                };
                requestAdapter.setOrderListener(listener);

                if (Sal7haSharedPreference.getRole(context).equals("user")) {
                    String[] tmp = new String[type1.size()];
                    type1.toArray(tmp);
                    apiInterface.getConstList(Sal7haSharedPreference.getToken(context), tmp.toString()).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                                    requestAdapter.setRequests(response.body().getData().getConstruction());
                                    if (response.body().getData().getConstruction().isEmpty()) {
                                        constructionTypeFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                                    }
                                    Log.i("TAG", "List" + response.body().getData().getConstruction());
                                } else {
                                    Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "NoSuccess");

                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String[] tmp = new String[type1.size()];
                    type1.toArray(tmp);
                    apiInterface.agentRequestsList(Sal7haSharedPreference.getToken(context), tmp).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                                    orders = response.body().getData().getConstruction();
                                    requestAdapter.setRequests(response.body().getData().getConstruction());
                                    if (response.body().getData().getConstruction().isEmpty()) {
                                        constructionTypeFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                                    }
                                    Log.i("TAG", "List" + response.body().getData().getConstruction());
                                } else {
//                                    Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                                Log.i("TAG", "NoSuccess");

                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void requestList(ArrayList<String> type1) {
        if (Sal7haSharedPreference.getRole(context).equals("user")) {
            String[] tmp = new String[type1.size()];
            type1.toArray(tmp);
            apiInterface.getConstList(Sal7haSharedPreference.getToken(context), tmp.toString()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            requestAdapter.setRequests(response.body().getData().getConstruction());
                            if (response.body().getData().getConstruction().isEmpty()) {
                                constructionTypeFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                            }
                            Log.i("TAG", "List" + response.body().getData().getConstruction());
                        } else {
                            Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "NoSuccess");

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String[] tmp = new String[type1.size()];
            type1.toArray(tmp);
            apiInterface.agentRequestsList(Sal7haSharedPreference.getToken(context), tmp).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            orders = response.body().getData().getConstruction();
                            requestAdapter.setRequests(response.body().getData().getConstruction());
                            if (response.body().getData().getConstruction().isEmpty()) {
                                constructionTypeFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                            }
                            Log.i("TAG", "List" + response.body().getData().getConstruction());
                        } else {
                        }
                    } else {
                        Log.i("TAG", "NoSuccess");

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    constructionTypeFragmentBinding.progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}