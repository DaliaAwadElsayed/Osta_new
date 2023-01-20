package com.dtag.osta.Fragment.ViewModel.orders;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.dtag.osta.Adapter.RequestAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.RequestsFragmentBinding;
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

public class RequestsViewModel extends ViewModel {
    RequestsFragmentBinding requestsFragmentBinding;
    private static final String TAG = "RequestsFragment";
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    List<Order> orders = new ArrayList<>();
    private RequestAdapter requestAdapter;
    Context context;

    public void Init(RequestsFragmentBinding requestsFragmentBinding, Context context) {
        this.requestsFragmentBinding = requestsFragmentBinding;
        this.context = context;
        List<String> categories = new ArrayList();
        categories.add(context.getResources().getString(R.string.newstatus));
        categories.add(context.getResources().getString(R.string.completedorder));
        categories.add(context.getResources().getString(R.string.rated));
        categories.add(context.getResources().getString(R.string.cancelledorder));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter(context, R.layout.spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestsFragmentBinding.spinnerStatus.setAdapter(dataAdapter);
        requestsFragmentBinding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                requestsFragmentBinding.progress.setVisibility(View.VISIBLE);
                requestsFragmentBinding.noResultLinear.setVisibility(View.INVISIBLE);
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
                requestAdapter = new RequestAdapter(context);
                requestsFragmentBinding.homeRecyclerView.setAdapter(requestAdapter);
                requestsFragmentBinding.progress.setVisibility(View.VISIBLE);

                RequestAdapter.OnModelClickListener listener = new RequestAdapter.OnModelClickListener() {
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
                    apiInterface.userRequestsList(Sal7haSharedPreference.getToken(context), tmp).enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                                    requestAdapter.setRequests(response.body().getData().getOrders());
                                    if (response.body().getData().getOrders().isEmpty()) {
                                        requestsFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                                    }
                                    Log.i(TAG, "List" + response.body().getData().getOrders());
                                } else {
                                    Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "NoSuccess");

                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
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
                                    requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                                    orders = response.body().getData().getOrders();
                                    requestAdapter.setRequests(response.body().getData().getOrders());
                                    if (response.body().getData().getOrders().isEmpty()) {
                                        requestsFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                                    }
                                    Log.i(TAG, "List" + response.body().getData().getOrders());
                                } else {
//                                    Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "NoSuccess");

                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
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
            apiInterface.userRequestsList(Sal7haSharedPreference.getToken(context), tmp).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            requestAdapter.setRequests(response.body().getData().getOrders());
                            if (response.body().getData().getOrders().isEmpty()) {
                                requestsFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                            }
                            Log.i(TAG, "List" + response.body().getData().getOrders());
                        } else {
                            Toast.makeText(context, "falsestatus", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "NoSuccess");

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
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
                            requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                            orders = response.body().getData().getOrders();
                            requestAdapter.setRequests(response.body().getData().getOrders());
                            if (response.body().getData().getOrders().isEmpty()) {
                                requestsFragmentBinding.noResultLinear.setVisibility(View.VISIBLE);
                            }
                            Log.i(TAG, "List" + response.body().getData().getOrders());
                        } else {
                        }
                    } else {
                        Log.i(TAG, "NoSuccess");

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    requestsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

}


