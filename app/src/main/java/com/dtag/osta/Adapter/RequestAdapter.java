package com.dtag.osta.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dtag.osta.R;
import com.dtag.osta.databinding.RequestItemBinding;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.Model.user.CancelOrder;
import com.dtag.osta.network.ResponseModel.Model.user.RateService;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ServicesViewHolder>
        implements View.OnClickListener {
    private LayoutInflater inflater;
    private List<Order> orders;
    RateService rateService = new RateService();
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Context context;
    String choiceReason;
    CancelOrder order = new CancelOrder();
    private OnModelClickListener onModelClickListener;


    public void setOrderListener(OnModelClickListener onModelClickListener) {
        this.onModelClickListener = onModelClickListener;
    }
    private static final String TAG = "REQUSTADAPTER";

    public RequestAdapter(Context context) {
        this.context = context;
    }

    public void setRequests(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new ServicesViewHolder(RequestItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.bindRequest(orders.get(position));
        holder.requestItemBinding.cancel.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(R.string.areyousureyouwanttocanceltheservice);
            alertDialogBuilder.setPositiveButton(R.string.yes,
                    (arg0, arg1) -> {
                        //    cancelReason();
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
                            reason1.setBackgroundColor(context.getResources().getColor(R.color.grey));
                        });
                        reason2.setOnClickListener(v -> {
                            choiceReason = reason2.getText().toString();
                            reason2.setBackgroundColor(context.getResources().getColor(R.color.grey));
                        });
                        reason3.setOnClickListener(v -> {
                            choiceReason = reason3.getText().toString();
                            reason3.setBackgroundColor(context.getResources().getColor(R.color.grey));

                        });
                        reason4.setOnClickListener(v -> {
                            choiceReason = reason4.getText().toString();
                            reason4.setBackgroundColor(context.getResources().getColor(R.color.grey));

                        });
                        reason5.setOnClickListener(v -> {
                            choiceReason = reason5.getText().toString();
                            reason5.setBackgroundColor(context.getResources().getColor(R.color.grey));

                        });
                        reason6.setOnClickListener(v -> {
                            choiceReason = reason6.getText().toString();
                            reason6.setBackgroundColor(context.getResources().getColor(R.color.grey));

                        });
                        reason7.setOnClickListener(v -> {
                            choiceReason = reason7.getText().toString();
                            reason7.setBackgroundColor(context.getResources().getColor(R.color.grey));

                        });
                        cancel.setOnClickListener(v -> {
                            order.setOrderId(orders.get(position).getId());
                            order.setStatus("cancelled");
                            order.setCancelReason(choiceReason);
                            dialog.dismiss();
                            apiInterface.userCancelOrder(Sal7haSharedPreference.getToken(context), order).enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    if (response.body() != null && response.isSuccessful()) {
                                        if (response.body().getStatus()) {
                                            holder.requestItemBinding.rateservice.setEnabled(false);
                                            holder.requestItemBinding.linearrate.setAlpha(0.5f);
                                            Toast.makeText(context, R.string.therequesthasbeensuccessfullycanceled, Toast.LENGTH_SHORT).show();
                                            onModelClickListener.sendModelId(orders.get(position).getId());
                                        } else {
                                            Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                        if (dialog != null) {
                            int width = ViewGroup.LayoutParams.MATCH_PARENT;
                            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            dialog.getWindow().setLayout(width, height);
                        }
                        keep.setOnClickListener(v ->
                        {
                            dialog.dismiss();
                        });
                        dialog.show();

                    });
            alertDialogBuilder.setNeutralButton(R.string.no, (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        holder.requestItemBinding.rateservice.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.rate_service_dialog);
            dialog.show();
            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
            RatingBar ratingBar;
            EditText editText;
            Button ok, cancel;
            ok = dialog.findViewById(R.id.doneDialogRateService);
            cancel = dialog.findViewById(R.id.cancelDialogRateService);
            ratingBar = dialog.findViewById(R.id.ratedialog);
            editText = dialog.findViewById(R.id.commentDialogRateService);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            float rate = ratingBar.getRating();
            ok.setOnClickListener(view1 -> {
                if (editText.getText().toString().matches("")) {
                    rateService.setRate(rate);
                    rateService.setRate_comment("no_review");
                    rateService.setId(orders.get(position).getId());
                } else {
                    rateService.setRate(rate);
                    rateService.setRate_comment(editText.getText().toString());
                    rateService.setId(orders.get(position).getId());

                }
                apiInterface.userRateService(Sal7haSharedPreference.getToken(context), rateService).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                holder.requestItemBinding.linearrate.setEnabled(false);
                                Toast.makeText(context, R.string.succesfulyrated, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                ///////////
                                final Dialog dialog1 = new Dialog(context);
                                dialog1.setCancelable(false);
                                dialog1.setContentView(R.layout.rate_worker_dialog);
                                dialog1.show();
                                if (dialog1 != null) {
                                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    dialog1.getWindow().setLayout(width, height);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                }
                                RatingBar ratingBar1;
                                //EditText editText;
                                Button ok, cancel;
                                ok = dialog1.findViewById(R.id.doneDialogRateWorker);
                                cancel = dialog1.findViewById(R.id.cancelDialogRateWorker);
                                ratingBar1 = dialog1.findViewById(R.id.rateworkerdialog);
                                // editText = dialog.findViewById(R.id.commentDialogRateService);
                                LayerDrawable stars = (LayerDrawable) ratingBar1.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
                                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                                stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.whiteColor), PorterDuff.Mode.SRC_ATOP);
                                float rate1 = ratingBar1.getRating();
                                ok.setOnClickListener(view2 -> {
                                    rateService.setRate(rate1);
                                    rateService.setUserId(orders.get(position).getAgentId());
                                    apiInterface.rateWorker(Sal7haSharedPreference.getToken(context), rateService).enqueue(new Callback<ApiResponse>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                            if (response.body() != null && response.isSuccessful()) {
                                                if (response.body().getStatus()) {
                                                    holder.requestItemBinding.linearrate.setEnabled(false);
                                                    Toast.makeText(context, R.string.succesfulyrated, Toast.LENGTH_SHORT).show();
                                                    dialog1.dismiss();
                                                } else {
                                                    Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                                            Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                });
                                cancel.setOnClickListener(view22 -> {
                                    dialog1.dismiss();
                                });
                                ///////////
                            } else {
                                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(context, R.string.internetconnection, Toast.LENGTH_SHORT).show();
                    }
                });
            });
            cancel.setOnClickListener(view12 -> {
                dialog.dismiss();
            });

        });

        holder.requestItemBinding.technicalsupport.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.supportFragment);
        });

    }


    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    @Override
    public void onClick(View view) {

    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {
        private RequestItemBinding requestItemBinding;

        ServicesViewHolder(@NonNull RequestItemBinding requestItemBinding) {
            super(requestItemBinding.getRoot());
            this.requestItemBinding = requestItemBinding;
        }

        private void bindRequest(Order orders) {
            if (Sal7haSharedPreference.getSelectedLanguage(context) == 1) {
                if (orders.getCategory() != null)
                    requestItemBinding.servicename.setText(Utility.fixNullString(orders.getCategory().getName()));
            } else {
                if (orders.getCategory() != null)
                    requestItemBinding.servicename.setText(Utility.fixNullString(orders.getCategory().getNameEn()));
            }
            if (Sal7haSharedPreference.getRole(context).equals("agent")) {
                requestItemBinding.verfy.setVisibility(View.GONE);
                requestItemBinding.threeitems.setVisibility(View.GONE);
            }
            requestItemBinding.visitdate.setText(Utility.fixNullString(orders.getDate()));
            requestItemBinding.visittime.setText(Utility.fixNullString(orders.getTime()));
            requestItemBinding.requststats.setText(Utility.fixNullString(orders.getStatus()));
            requestItemBinding.rate.setRating(orders.getRate());
            if (orders.getStatus().equals("on_way")) {
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.linearrate.setAlpha(0.5f);
                requestItemBinding.rateservice.setEnabled(false);
                requestItemBinding.requststats.setText(R.string.onway);
            }
            if (orders.getStatus().equals("approved")) {
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.linearrate.setAlpha(0.5f);
                requestItemBinding.rateservice.setEnabled(false);
                requestItemBinding.requststats.setText(R.string.approved);
            }
            if (orders.getStatus().equals("arrived")) {
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.requststats.setText(R.string.arrived);
            }
            if (Utility.fixNullString(orders.getStatus()).equals("cancelled")) {
                requestItemBinding.requststats.setText(R.string.cancelledd);
                requestItemBinding.verfy.setVisibility(View.GONE);
                requestItemBinding.threeitems.setVisibility(View.GONE);
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.requststats.setTextColor(context.getResources().getColor(R.color.errorcolor));
            } else if (Utility.fixNullString(orders.getStatus()).equals("completed")) {
                requestItemBinding.requststats.setText(R.string.completedd);
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.verfy.setVisibility(View.GONE);
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.linearcancel.setAlpha(0.5f);
                requestItemBinding.cancel.setEnabled(false);
                requestItemBinding.requststats.setTextColor(context.getResources().getColor(R.color.green));
            } else if (Utility.fixNullString(orders.getStatus()).equals("rated")) {
                requestItemBinding.requststats.setText(R.string.rated);
                requestItemBinding.rateservice.setText(context.getResources().getString(R.string.rated));
                requestItemBinding.view.setVisibility(View.GONE);
                requestItemBinding.requststats.setTextColor(context.getResources().getColor(R.color.goldstar));
                requestItemBinding.verfy.setVisibility(View.GONE);
                requestItemBinding.linearrate.setAlpha(0.5f);
                requestItemBinding.linearcancel.setAlpha(0.5f);
                requestItemBinding.rateservice.setEnabled(false);
                requestItemBinding.cancel.setEnabled(false);
            } else if (Utility.fixNullString(orders.getStatus()).equals("new")) {
                requestItemBinding.rate.setVisibility(View.GONE);
                requestItemBinding.view.setVisibility(View.GONE);

                requestItemBinding.rateservice.setEnabled(false);
                requestItemBinding.linearrate.setAlpha(0.5f);
                requestItemBinding.requststats.setText(R.string.neww);
                requestItemBinding.requststats.setTextColor(context.getResources().getColor(R.color.blue));
            }
            requestItemBinding.requestnumber.setText("" + orders.getId());
            requestItemBinding.verfy.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", orders.getId());
                Navigation.findNavController(view).navigate(R.id.qrFragment, bundle);
            });
            if (orders.getCategory() != null) {
                if (!orders.getCategory().getIcon().isEmpty()) {
                    Picasso.get().load(RetrofitClient.BASE_URL + '/' + orders.getCategory().getIcon()).into(requestItemBinding.img);
                } else {
                    requestItemBinding.img.setImageResource(R.drawable.smile);
                }
            } else {
                requestItemBinding.img.setImageResource(R.drawable.smile);
            }
            itemView.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putInt("order_id", orders.getId());
                Navigation.findNavController(view).navigate(R.id.orderDetailsFragment, bundle);
            });
        }


    }

    public interface OnModelClickListener {
        void sendModelId(int id);
    }

}
