package com.dtag.osta.Fragment.ViewModel.orders;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.dtag.osta.Adapter.ImageBannerAdapter;
import com.dtag.osta.R;
import com.dtag.osta.databinding.OrderDetailsFragmentBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.Model.user.CancelOrder;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.rd.animation.type.AnimationType;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsViewModel extends ViewModel implements ViewPager.OnPageChangeListener {
    OrderDetailsFragmentBinding orderDetailsFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    int orderId;
    ImageBannerAdapter imageBannerAdapter = new ImageBannerAdapter();
    Order order = new Order();
    CancelOrder cancelOrder = new CancelOrder();
    ImageView cancel;
    EditText preview, cost;
    Button sendPreview, accept, refuse;
    String choiceReason;
    Boolean isNotification;
    String body;

    public void Init(OrderDetailsFragmentBinding orderDetailsFragmentBinding, Context context, int orderId, Boolean isNotification, String body) {
        this.context = context;
        this.orderDetailsFragmentBinding = orderDetailsFragmentBinding;
        this.orderId = orderId;
        this.body = body;
        this.isNotification = isNotification;

        orderDetailsFragmentBinding.sendpreview.setOnClickListener(view -> {
            sendPreviewDialog();
        });
        if (Sal7haSharedPreference.getRole(context).equals("user")) {
            userOrderDetails();
        } else {
            agentOrderDetails();
        }
        orderDetailsFragmentBinding.viewPager.setAdapter(imageBannerAdapter);
        orderDetailsFragmentBinding.viewPager.addOnPageChangeListener(this);
        orderDetailsFragmentBinding.pageIndicatorView.setAnimationType(AnimationType.WORM);
    }

    private void userOrderDetails() {
        orderDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.userOrderDetails(Sal7haSharedPreference.getToken(context), orderId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null) {
                    orderDetailsFragmentBinding.progress.setVisibility(View.GONE);
                    Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getOrder().getAgent().getImage()).error(R.drawable.ic_baseline_cancel_24).into(orderDetailsFragmentBinding.img);
                    imageBannerAdapter.setImages(response.body().getData().getOrder().getUploadedImages());

                    if (response.body().getStatus() && response.isSuccessful()) {
                        if (Sal7haSharedPreference.getSelectedLanguageValue(context).equals("ar")) {
                            orderDetailsFragmentBinding.servicename.setText(Utility.fixNullString(response.body().getData().getOrder().getCategory().getName()));
                        } else {
                            orderDetailsFragmentBinding.servicename.setText(Utility.fixNullString(response.body().getData().getOrder().getCategory().getNameEn()));
                        }
                        orderDetailsFragmentBinding.status.setText(Utility.fixNullString(response.body().getData().getOrder().getStatus()));
                        orderDetailsFragmentBinding.name.setText(Utility.fixNullString(response.body().getData().getOrder().getAgent().getName()));
                        orderDetailsFragmentBinding.phoneNumber.setText(Utility.fixNullString(response.body().getData().getOrder().getAgent().getPhone()));
                        orderDetailsFragmentBinding.date.setText(Utility.fixNullString(response.body().getData().getOrder().getDate()));
                        orderDetailsFragmentBinding.time.setText(Utility.fixNullString(response.body().getData().getOrder().getTime()));
                        orderDetailsFragmentBinding.descrption.setText(Utility.fixNullString(response.body().getData().getOrder().getDescription()));
                        orderDetailsFragmentBinding.address.setText(Utility.fixNullString(response.body().getData().getOrder().getAddress()));
                        orderDetailsFragmentBinding.visitfee.setText(Utility.fixNullString("" + response.body().getData().getOrder().getVisitFees()));
                        orderDetailsFragmentBinding.requestnumber.setText(Utility.fixNullString("" + response.body().getData().getOrder().getId()));
                        if (response.body().getData().getOrder().getStatus().equals("new")) {
                            orderDetailsFragmentBinding.status.setText(R.string.neww);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(1);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                        }
                        if (response.body().getData().getOrder().getStatus().equals("on_way")) {
                            orderDetailsFragmentBinding.status.setText(R.string.onway);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(2);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                        } else if (response.body().getData().getOrder().getStatus().equals("arrived")) {
                            /////TODO
                            orderDetailsFragmentBinding.status.setText(R.string.arrived);
                            if (isNotification && body.contains("&")) {
                                userAcceptRefusedDialog();
                            }
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);
                        } else if (response.body().getData().getOrder().getStatus().equals("approved")) {
                            orderDetailsFragmentBinding.status.setText(R.string.approved);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);
                        } else if (response.body().getData().getOrder().getStatus().equals("cancelled")) {
                            orderDetailsFragmentBinding.relativegone.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.status.setText(R.string.cancelledd);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.visitfee.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.visitfeetext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setText(Utility.fixNullString(response.body().getData().getOrder().getCancelReason()));
                        } else if (response.body().getData().getOrder().getStatus().equals("completed")) {
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(5);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentDrawable(context.getDrawable(R.drawable.ic_done_black_24dp));
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentStatusZoom(0);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCircleFillColorCurrent(context.getResources().getColor(R.color.basicColor));
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setText(Utility.fixNullString("" + response.body().getData().getOrder().getCost()));
                            orderDetailsFragmentBinding.preview.setText(Utility.fixNullString(response.body().getData().getOrder().getAgentDescription()));
                            double cost = response.body().getData().getOrder().getCost();
                            double visitFee = response.body().getData().getOrder().getVisitFees();
                            double totalCost = cost + visitFee;
                            orderDetailsFragmentBinding.totalcost.setText("" + totalCost);
                        } else if (response.body().getData().getOrder().getStatus().equals("rated")) {
//                            orderDetailsFragmentBinding.stepView.go(2, true);
//                            orderDetailsFragmentBinding.stepView.done(true);
                            orderDetailsFragmentBinding.status.setText(R.string.rated);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(5);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentDrawable(context.getDrawable(R.drawable.ic_done_black_24dp));
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentStatusZoom(0);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCircleFillColorCurrent(context.getResources().getColor(R.color.basicColor));
                            orderDetailsFragmentBinding.rate.setRating(response.body().getData().getOrder().getRate());
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.rate.setVisibility(View.VISIBLE);
                            orderDetailsFragmentBinding.cost.setText(Utility.fixNullString("" + response.body().getData().getOrder().getCost()));
                            orderDetailsFragmentBinding.preview.setText(Utility.fixNullString(response.body().getData().getOrder().getAgentDescription()));
                            double cost = response.body().getData().getOrder().getCost();
                            double visitFee = response.body().getData().getOrder().getVisitFees();
                            double totalCost = cost + visitFee;
                            orderDetailsFragmentBinding.totalcost.setText("" + totalCost);
                        }


                    } else {
                        //  Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                orderDetailsFragmentBinding.progress.setVisibility(View.GONE);
            }
        });
    }

    private void agentOrderDetails() {
        orderDetailsFragmentBinding.nametext.setText(R.string.customername);
        orderDetailsFragmentBinding.progress.setVisibility(View.VISIBLE);
        apiInterface.agentOrderDetails(Sal7haSharedPreference.getToken(context), orderId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() && response.body() != null) {
                        if (response.body().getData().getOrder().getCategory() != null) {
                            orderDetailsFragmentBinding.progress.setVisibility(View.GONE);
                            if (response.body().getData().getOrder().getAgent() != null) {
                                Picasso.get().load(RetrofitClient.BASE_URL + '/' + response.body().getData().getOrder().getAgent().getImage()).error(R.drawable.ic_baseline_cancel_24).into(orderDetailsFragmentBinding.img);
                            } else {
                                orderDetailsFragmentBinding.img.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_cancel_24));
                            }
                            imageBannerAdapter.setImages(response.body().getData().getOrder().getUploadedImages());

                            if (Sal7haSharedPreference.getSelectedLanguageValue(context).equals("ar")) {
                                orderDetailsFragmentBinding.servicename.setText(Utility.fixNullString(response.body().getData().getOrder().getCategory().getName()));
                            } else {
                                orderDetailsFragmentBinding.servicename.setText(Utility.fixNullString(response.body().getData().getOrder().getCategory().getNameEn()));
                            }

                        } else {
                            orderDetailsFragmentBinding.servicename.setText("");
                        }
                        if (response.body().getData().getOrder().getUser() != null) {
                            orderDetailsFragmentBinding.name.setText(Utility.fixNullString(response.body().getData().getOrder().getUser().getName()));
                            orderDetailsFragmentBinding.phoneNumber.setText(Utility.fixNullString(response.body().getData().getOrder().getUser().getPhone()));
                            orderDetailsFragmentBinding.address.setText(Utility.fixNullString(response.body().getData().getOrder().getAddress()));
                        } else {
                            orderDetailsFragmentBinding.name.setText("");
                            orderDetailsFragmentBinding.phoneNumber.setText("");
                            orderDetailsFragmentBinding.address.setText("");
                        }
                        orderDetailsFragmentBinding.status.setText(Utility.fixNullString(response.body().getData().getOrder().getStatus()));
                        orderDetailsFragmentBinding.date.setText(Utility.fixNullString(response.body().getData().getOrder().getDate()));
                        orderDetailsFragmentBinding.time.setText(Utility.fixNullString(response.body().getData().getOrder().getTime()));
                        orderDetailsFragmentBinding.descrption.setText(Utility.fixNullString(response.body().getData().getOrder().getDescription()));
                        orderDetailsFragmentBinding.visitfee.setText(Utility.fixNullString("" + response.body().getData().getOrder().getVisitFees()));
                        orderDetailsFragmentBinding.requestnumber.setText(Utility.fixNullString("" + response.body().getData().getOrder().getId()));
                        if (response.body().getData().getOrder().getStatus().equals("new")) {
                            orderDetailsFragmentBinding.status.setText(R.string.neww);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(1);
                            orderDetailsFragmentBinding.linear.setOnClickListener(view -> {
                                order.setStatus("on_way");
                                order.setOrder_id(response.body().getData().getOrder().getId());
                                apiInterface.agentChangeStatus(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getStatus() && response.isSuccessful()) {
                                                agentOrderDetails();
                                                orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(2);
                                                Toast.makeText(context, R.string.statuschangesucc, Toast.LENGTH_SHORT).show();
                                                orderDetailsFragmentBinding.status.setText(R.string.onway);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                        } else if (response.body().getData().getOrder().getStatus().equals("on_way")) {
                            orderDetailsFragmentBinding.status.setText(R.string.onway);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(2);
                            orderDetailsFragmentBinding.linear.setOnClickListener(view -> {
                                order.setStatus("arrived");
                                order.setOrder_id(response.body().getData().getOrder().getId());
                                apiInterface.agentChangeStatus(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getStatus() && response.isSuccessful()) {
                                                //   agentOrderDetails();
                                                orderDetailsFragmentBinding.status.setText(R.string.arrived);
                                                orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);
                                                Toast.makeText(context, R.string.statuschangesucc, Toast.LENGTH_SHORT).show();
                                                orderDetailsFragmentBinding.qrImg.setVisibility(View.VISIBLE);
                                                orderDetailsFragmentBinding.sendpreview.setVisibility(View.VISIBLE);
                                                orderDetailsFragmentBinding.qrImg.setVisibility(View.VISIBLE);
                                                order.setOrder_id(response.body().getData().getOrder().getId());
                                                // orderDetailsFragmentBinding.progressImg.setVisibility(View.INVISIBLE);
                                                apiInterface.qrCode(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                                    @Override
                                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            if (response.body().getStatus()) {
                                                                //   orderDetailsFragmentBinding.progressImg.setVisibility(View.GONE);
                                                                Picasso.get().load(response.body().getMessage()).error(R.drawable.ic_baseline_cancel_24).into(orderDetailsFragmentBinding.imgscan);
                                                            } else {
                                                                Toast.makeText(context, response.body().getStatus().toString() + order.getId(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });

                        } else if (response.body().getData().getOrder().getStatus().equals("approved")) {
                            orderDetailsFragmentBinding.status.setText(R.string.approved);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);
                            orderDetailsFragmentBinding.linear.setOnClickListener(view -> {
                                order.setStatus("completed");
                                order.setOrder_id(response.body().getData().getOrder().getId());
                                apiInterface.agentChangeStatus(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getStatus() && response.isSuccessful()) {
                                                agentOrderDetails();
                                                orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(4);
                                                Toast.makeText(context, R.string.statuschangesucc, Toast.LENGTH_SHORT).show();
                                                orderDetailsFragmentBinding.status.setText(R.string.completed);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });

                        } else if (response.body().getData().getOrder().getStatus().equals("arrived") || orderDetailsFragmentBinding.status.equals(context.getResources().getString(R.string.arrived))) {
                            // agentOrderDetails();
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.status.setText(R.string.arrived);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);
                            orderDetailsFragmentBinding.qrImg.setVisibility(View.VISIBLE);
                            orderDetailsFragmentBinding.sendpreview.setVisibility(View.VISIBLE);

                            order.setOrder_id(response.body().getData().getOrder().getId());
                            //  orderDetailsFragmentBinding.progressImg.setVisibility(View.VISIBLE);
                            apiInterface.qrCode(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getStatus()) {
                                            //         agentOrderDetails();
                                            //           orderDetailsFragmentBinding.progressImg.setVisibility(View.GONE);
//                                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            Picasso.get().load(response.body().getMessage()).error(R.drawable.ic_baseline_cancel_24).into(orderDetailsFragmentBinding.imgscan);
                                        } else {
                                            Toast.makeText(context, response.body().getStatus().toString() + order.getId(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else if (response.body().getData().getOrder().getStatus().equals("cancelled")) {
                            orderDetailsFragmentBinding.relativegone.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcost.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.status.setText(R.string.cancelledd);
                            orderDetailsFragmentBinding.preview.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.visitfee.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.visitfeetext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.costtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.totalcosttext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.previewtext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cancelreason.setText(Utility.fixNullString(response.body().getData().getOrder().getCancelReason()));
                        } else if (response.body().getData().getOrder().getStatus().equals("completed")) {
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(4);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentDrawable(context.getDrawable(R.drawable.ic_done_black_24dp));
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentStatusZoom(0);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCircleFillColorCurrent(context.getResources().getColor(R.color.basicColor));
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.cost.setText(Utility.fixNullString("" + response.body().getData().getOrder().getCost()));
                            orderDetailsFragmentBinding.preview.setText(Utility.fixNullString(response.body().getData().getOrder().getAgentDescription()));
                            double cost = response.body().getData().getOrder().getCost();
                            double visitFee = response.body().getData().getOrder().getVisitFees();
                            double totalCost = cost + visitFee;
                            orderDetailsFragmentBinding.totalcost.setText("" + totalCost);
                        } else if (response.body().getData().getOrder().getStatus().equals("rated")) {
                            orderDetailsFragmentBinding.status.setText(R.string.rated);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(5);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentDrawable(context.getDrawable(R.drawable.ic_done_black_24dp));
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentStatusZoom(0);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCircleFillColorCurrent(context.getResources().getColor(R.color.basicColor));
                            orderDetailsFragmentBinding.rate.setRating(response.body().getData().getOrder().getRate());
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.GONE);
                            orderDetailsFragmentBinding.rate.setVisibility(View.VISIBLE);
                            orderDetailsFragmentBinding.cost.setText(Utility.fixNullString("" + response.body().getData().getOrder().getCost()));
                            orderDetailsFragmentBinding.preview.setText(Utility.fixNullString(response.body().getData().getOrder().getAgentDescription()));
                            double cost = response.body().getData().getOrder().getCost();
                            double visitFee = response.body().getData().getOrder().getVisitFees();
                            double totalCost = cost + visitFee;
                            orderDetailsFragmentBinding.totalcost.setText("" + totalCost);
                        } else if (response.body().getData().getOrder().getStatus().equals("rejected")) {
                            orderDetailsFragmentBinding.linear.setOnClickListener(view -> {
                                order.setStatus("completed");
                                order.setOrder_id(response.body().getData().getOrder().getId());
                                apiInterface.agentChangeStatus(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.body() != null) {
                                            if (response.body().getStatus() && response.isSuccessful()) {
                                                agentOrderDetails();
                                                orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(4);
                                                Toast.makeText(context, R.string.statuschangesucc, Toast.LENGTH_SHORT).show();
                                                orderDetailsFragmentBinding.status.setText(R.string.completed);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                            orderDetailsFragmentBinding.status.setText(R.string.cancelledd);
                            orderDetailsFragmentBinding.canceltext.setVisibility(View.VISIBLE);
                            orderDetailsFragmentBinding.cancelreason.setVisibility(View.VISIBLE);
                            orderDetailsFragmentBinding.statusViewScroller.getStatusView().setCurrentCount(3);

                        }


                    } else {
                        Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                        orderDetailsFragmentBinding.progress.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                orderDetailsFragmentBinding.progress.setVisibility(View.GONE);
            }
        });
    }

    /////dalia
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        orderDetailsFragmentBinding.pageIndicatorView.setSelection(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void sendPreviewDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        dialog.setContentView(R.layout.send_preview_dialog);
        preview = dialog.findViewById(R.id.previewDialog);
        cost = dialog.findViewById(R.id.costDialog);
        sendPreview = dialog.findViewById(R.id.sendPreviewDialogId);
        sendPreview.setOnClickListener(view -> {
            if (inputValid()) {
                order.setAgentDescription(preview.getText().toString());
                order.setCost(Double.parseDouble(cost.getText().toString()));
                order.setOrder_id(Integer.valueOf(orderDetailsFragmentBinding.requestnumber.getText().toString()));
                apiInterface.agentChangeStatus(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus() && response.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(context, R.string.previewsentsuccessfullypleasewaitforcustomerresponse, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        cancel = dialog.findViewById(R.id.sendcanceldialog);
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void refusedReasons() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.cancel_dialog);
        TextView reason1, reason2, reason3, reason4, reason5, reason6, reason7;
        Button cancel, keep;
        reason1 = dialog.findViewById(R.id.reason1);
        reason2 = dialog.findViewById(R.id.reason2);
        reason3 = dialog.findViewById(R.id.reason3);
        reason4 = dialog.findViewById(R.id.reason4);
        reason5 = dialog.findViewById(R.id.reason5);
        reason6 = dialog.findViewById(R.id.reason6);
        reason7 = dialog.findViewById(R.id.reason7);
        cancel = dialog.findViewById(R.id.cancelId);
        keep = dialog.findViewById(R.id.keepId);
        reason1.setOnClickListener(v -> {
            choiceReason = reason1.getText().toString();
            reason1.setBackgroundColor(context.getResources().getColor(R.color.blue));
        });
        reason2.setOnClickListener(v -> {
            choiceReason = reason2.getText().toString();
            reason2.setBackgroundColor(context.getResources().getColor(R.color.blue));
        });
        reason3.setOnClickListener(v -> {
            choiceReason = reason3.getText().toString();
            reason3.setBackgroundColor(context.getResources().getColor(R.color.blue));

        });
        reason4.setOnClickListener(v -> {
            choiceReason = reason4.getText().toString();
            reason4.setBackgroundColor(context.getResources().getColor(R.color.blue));

        });
        reason5.setOnClickListener(v -> {
            choiceReason = reason5.getText().toString();
            reason5.setBackgroundColor(context.getResources().getColor(R.color.blue));

        });
        reason6.setOnClickListener(v -> {
            choiceReason = reason6.getText().toString();
            reason6.setBackgroundColor(context.getResources().getColor(R.color.blue));

        });
        reason7.setOnClickListener(v -> {
            choiceReason = reason7.getText().toString();
            reason7.setBackgroundColor(context.getResources().getColor(R.color.blue));

        });
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        keep.setOnClickListener(v ->
        {
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> {
            cancelOrder.setOrderId(Integer.valueOf(orderDetailsFragmentBinding.requestnumber.getText().toString()));
            order.setStatus("rejected");
            order.setCancelReason(choiceReason);
            apiInterface.userCancelOrder(Sal7haSharedPreference.getToken(context), cancelOrder).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            dialog.dismiss();
                            Toast.makeText(context, R.string.requesthasbeenrejected, Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();

    }

    public void userAcceptRefusedDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
        dialog.setContentView(R.layout.user_accept_refuse_dialog);
        TextView previewText, previewDetailsText;
        TextView costText, costDetailsText;
        //   costText = dialog.findViewById(R.id.costTextDialogId);
        // previewText = dialog.findViewById(R.id.previewTextDialogId);
        previewDetailsText = dialog.findViewById(R.id.previewDetailsTextDialogId);
        costDetailsText = dialog.findViewById(R.id.costDetailsTextDialogId);
        accept = dialog.findViewById(R.id.acceptdialogId);
        refuse = dialog.findViewById(R.id.refuseddialogId);
        String currentString = body;
        String[] separated = currentString.split("&");
        String previewSplit1 = separated[0];
        String previewSplit2 = separated[1];
//        String[] separatedPreview = previewSplit1.split(":");
//        previewText.setText(" " + separatedPreview[0] + " ");
//        previewDetailsText.setText("  " + separatedPreview[1] + " ");
//        String[] separatedPreview2 = previewSplit2.split(":");
//        costText.setText(" " + separatedPreview2[1] + " ");
//        costDetailsText.setText(" " + separatedPreview2[0] + " ");
        previewDetailsText.setText(previewSplit1);
        costDetailsText.setText(previewSplit2);
        accept.setOnClickListener(view -> {
            cancelOrder.setOrderId(Integer.valueOf(orderDetailsFragmentBinding.requestnumber.getText().toString()));
            cancelOrder.setStatus("approved");
            apiInterface.userCancelOrder(Sal7haSharedPreference.getToken(context), cancelOrder).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            dialog.dismiss();

                            Toast.makeText(context, R.string.theofferwassuccessfullyapproved, Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                }
            });
        });
        refuse.setOnClickListener(view -> {
            dialog.dismiss();
            refusedReasons();
        });

        dialog.show();
    }


    private boolean inputValid() {
        return previewValid() && costValid();
    }

    private boolean previewValid() {
        String previewText = preview.getText().toString();
        if (!previewText.isEmpty()) {
            return true;
        }
        preview.setError(context.getResources().getString(R.string.enterpreview));
        return false;
    }

    private boolean costValid() {
        String costText = cost.getText().toString();
        if (!costText.isEmpty()) {
            return true;
        }
        cost.setError(context.getResources().getString(R.string.entercost));
        return false;
    }

}



