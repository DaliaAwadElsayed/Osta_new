package com.dtag.osta.Fragment.main.user.scaning;

import android.app.Dialog;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.dtag.osta.R;import com.dtag.osta.databinding.QrFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.main.user.scaning.QrViewModel;
import com.dtag.osta.network.Interface.Api;
import com.dtag.osta.network.ResponseModel.Model.orderList.Order;
import com.dtag.osta.network.ResponseModel.wrapper.ApiResponse;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.dtag.osta.utility.Sal7haSharedPreference;
import com.dtag.osta.utility.Utility;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrFragment extends Fragment implements BarcodeReader.BarcodeReaderListener {

    private QrViewModel mViewModel;
    QrFragmentBinding qrFragmentBinding;
    private Api apiInterface = RetrofitClient.getInstance().getApi();
    Order order = new Order();

    public static QrFragment newInstance() {
        return new QrFragment();
    }

    private BarcodeReader barcodeReader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        qrFragmentBinding = QrFragmentBinding.inflate(inflater, container, false);

        return qrFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).hideBottomMenu();

        barcodeReader = (BarcodeReader) getChildFragmentManager().findFragmentById(R.id.barcode_scanner);
        barcodeReader.setListener(this);
        mViewModel = ViewModelProviders.of(this).get(QrViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onScanned(final Barcode barcode) {
        barcodeReader.playBeep();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //     Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
                barcodeReader.playBeep();
                order.setOrder_id(getArguments().getInt("id"));
                order.setQrCode(barcode.displayValue);
                apiInterface.getResultQr(Sal7haSharedPreference.getToken(getContext()), order).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("agentName", Utility.fixNullString(response.body().getData().getAgent().getName()));
                                bundle.putString("agentPhone", Utility.fixNullString(response.body().getData().getAgent().getPhone()));
                                bundle.putString("agentImage", Utility.fixNullString(response.body().getData().getAgent().getImage()));
                                if (Sal7haSharedPreference.getSelectedLanguageValue(getContext()).equals("ar")) {
                                    bundle.putString("agentCategory", Utility.fixNullString(response.body().getData().getCategory().getName()));
                                } else {
                                    bundle.putString("agentCategory", Utility.fixNullString(response.body().getData().getCategory().getNameEn()));
                                }
                                bundle.putInt("order_id_result",getArguments().getInt("id"));
                                Navigation.findNavController(getView()).navigate(R.id.workerScaningQrResultFragment, bundle);
                                //Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                            } else {
                                codeFail();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.internetconnection, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void codeFail() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.invalid_qr);
        Button done;
        done = dialog.findViewById(R.id.done);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
        done.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    @Override
    public void onScanError(String errorMessage) {
    }

    @Override
    public void onCameraPermissionDenied() {

    }
}
