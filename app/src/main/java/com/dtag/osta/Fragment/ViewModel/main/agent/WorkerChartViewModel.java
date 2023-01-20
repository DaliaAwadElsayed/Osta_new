package com.dtag.osta.Fragment.ViewModel.main.agent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.WorkerChartFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerChartViewModel extends ViewModel {
    WorkerChartFragmentBinding workerChartFragmentBinding;
    Context context;
    int orderId;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Order order = new Order();
    Boolean isNotification;

    public void Init(WorkerChartFragmentBinding workerChartFragmentBinding, Context context, int orderId, Boolean isNotification) {
        this.context = context;
        this.orderId = orderId;
        this.isNotification = isNotification;
        this.workerChartFragmentBinding = workerChartFragmentBinding;
        isAvailable();
        if (isNotification) {
            workerAcceptRefusedDialog();
        }
        workerChartFragmentBinding.active.setOnClickListener(view -> {
            changeActiveStatus();
//            workerChartFragmentBinding.active.setText(R.string.inactive);
//            workerChartFragmentBinding.active.setBackground(context.getDrawable(R.drawable.red_oval_button));
//            active = 1;
//            workerChartFragmentBinding.active.setOnClickListener(view1 -> {
//                if (active == 1) {
//                    active = 0;
//                    workerChartFragmentBinding.active.setText(R.string.active);
//                    workerChartFragmentBinding.active.setBackground(context.getDrawable(R.drawable.oval_button));
//                } else {
//                    workerChartFragmentBinding.active.setText(R.string.inactive);
//                    workerChartFragmentBinding.active.setBackground(context.getDrawable(R.drawable.red_oval_button));
//                    active = 1;
//                }
//
//            });
        });

        ArrayList NoOfTrips = new ArrayList();
        ArrayList day = new ArrayList();
        workerChartFragmentBinding.lastrequest.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.requestsFragment));
        apiInterface.orderStatistics(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        if (response.body().getData().getOrderStat().isEmpty()) {
                            workerChartFragmentBinding.noChart.setVisibility(View.VISIBLE);
                            workerChartFragmentBinding.barchart.setVisibility(View.GONE);
                            workerChartFragmentBinding.lastrequest.setVisibility(View.GONE);
                        } else {
                            int size = response.body().getData().getOrderStat().size();
                            workerChartFragmentBinding.noChart.setVisibility(View.GONE);
                            workerChartFragmentBinding.barchart.setVisibility(View.VISIBLE);
                            workerChartFragmentBinding.lastrequest.setVisibility(View.VISIBLE);
                            if (size == 1) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add("");
                                day.add("");
                                day.add("");
                                day.add("");
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(0f, 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(0f, 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(0f, 6));

                            } else if (size == 2) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add("");
                                day.add("");
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(0f, 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(0f, 6));

                            } else if (size == 3) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(2).getDateNow()));
                                day.add("");
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(2).getTotal(), 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(0f, 6));

                            } else if (size == 4) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(2).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(3).getDateNow()));
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(2).getTotal(), 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(3).getTotal(), 6));
                                NoOfTrips.add(new BarEntry(0f, 7));

                            } else if (size == 5) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(2).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(3).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(4).getDateNow()));
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(2).getTotal(), 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(3).getTotal(), 6));
                                NoOfTrips.add(new BarEntry(0f, 7));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(4).getTotal(), 8));
                                NoOfTrips.add(new BarEntry(0f, 9));

                            } else if (size == 6) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(2).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(3).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(4).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(5).getDateNow()));
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(2).getTotal(), 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(3).getTotal(), 6));
                                NoOfTrips.add(new BarEntry(0f, 7));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(4).getTotal(), 8));
                                NoOfTrips.add(new BarEntry(0f, 9));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(5).getTotal(), 10));
                                NoOfTrips.add(new BarEntry(0f, 11));

                            } else if (size == 7) {
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(0).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(1).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(2).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(3).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(4).getDateNow()));
                                day.add("");
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(5).getDateNow()));
                                day.add(Utility.fixNullString(response.body().getData().getOrderStat().get(6).getDateNow()));
                                day.add("");
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(0).getTotal(), 0));
                                NoOfTrips.add(new BarEntry(0f, 1));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(1).getTotal(), 2));
                                NoOfTrips.add(new BarEntry(0f, 3));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(2).getTotal(), 4));
                                NoOfTrips.add(new BarEntry(0f, 5));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(3).getTotal(), 6));
                                NoOfTrips.add(new BarEntry(0f, 7));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(4).getTotal(), 8));
                                NoOfTrips.add(new BarEntry(0f, 9));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(5).getTotal(), 10));
                                NoOfTrips.add(new BarEntry(0f, 11));
                                NoOfTrips.add(new BarEntry(response.body().getData().getOrderStat().get(6).getTotal(), 12));
                                NoOfTrips.add(new BarEntry(0f, 13));

                            }
                            BarDataSet bardataset = new BarDataSet(NoOfTrips, context.getResources().getString(R.string.weekrepoert));
                            workerChartFragmentBinding.barchart.animateY(5000);
                            workerChartFragmentBinding.barchart.setDrawValueAboveBar(false);
                            workerChartFragmentBinding.barchart.setBorderWidth(5.0f);
                            BarData data = new BarData(day, bardataset);
                            bardataset.setColors(ColorTemplate.createColors(new int[]{context.getResources().getColor(R.color.basicColor)}));
                            workerChartFragmentBinding.barchart.setData(data);
                        }

                    } else {
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

//        workerChartFragmentBinding.active.setOnClickListener(view -> {
//            if (workerChartFragmentBinding.active.getText().equals(R.string.active)) {
//                workerChartFragmentBinding.active.setText(R.string.inactive);
//                workerChartFragmentBinding.active.setBackgroundColor(context.getResources().getColor(R.color.errorcolor));
//            }
//            else {
//                workerChartFragmentBinding.active.setText(R.string.active);
//                workerChartFragmentBinding.active.setBackgroundColor(context.getResources().getColor(R.color.basicColor));
//
//            }
//        });
    }

    private void changeActiveStatus() {
        workerChartFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.agentAvailable(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getStatus() && response.isSuccessful()) {
                    workerChartFragmentBinding.progress.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.successfulyupdate, Toast.LENGTH_SHORT).show();
                    isAvailable();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void isAvailable() {
        apiInterface.getAgentProfile(Sal7haSharedPreference.getToken(context)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        int isAvailable = response.body().getData().getAgent().getIs_available();
                        if (isAvailable == 1) {
                            workerChartFragmentBinding.active.setText(R.string.active);
                            workerChartFragmentBinding.active.setBackground(context.getDrawable(R.drawable.oval_button));
                        } else if (isAvailable == 0) {
                            workerChartFragmentBinding.active.setText(R.string.inactive);
                            workerChartFragmentBinding.active.setBackground(context.getDrawable(R.drawable.red_oval_button));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void workerAcceptRefusedDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        dialog.setContentView(R.layout.worker_accept_refuse_dialog);
        Button accept, refuse;
        accept = dialog.findViewById(R.id.acceptdialogId);
        refuse = dialog.findViewById(R.id.refuseddialogId);
        refuse.setOnClickListener(view -> {
            final Dialog dialog1 = new Dialog(context);
            dialog1.setCancelable(false);
            if (dialog1 != null) {
                int width = ViewGroup.LayoutParams.WRAP_CONTENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog1.getWindow().setLayout(width, height);
            }
            dialog1.setContentView(R.layout.progress_dialog);
            dialog1.show();
            order.setOrder_id(orderId);
            apiInterface.agentAcceptRefuseRequest(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, R.string.requesthasbeenrejected, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            dialog1.dismiss();
                        } else {
                            dialog1.dismiss();
                          Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        dialog1.dismiss();
                        Toast.makeText(context, "not suc"+order.getOrder_id(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog1.dismiss();
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        });
        accept.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(context, R.string.theofferwassuccessfullyapproved, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putInt("order_id", orderId);
            Navigation.findNavController(workerChartFragmentBinding.getRoot()).navigate(R.id.orderDetailsFragment, bundle);
        });

        dialog.show();
    }

}
