package com.dtag.osta.Fragment.ViewModel.main.user.scaning;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;import com.dtag.osta.databinding.WorkerScaningQrResultFragmentBinding;
import com.dtag.osta.network.ResponseModel.wrapper.RetrofitClient;
import com.squareup.picasso.Picasso;

public class WorkerScaningQrResultViewModel extends ViewModel {
    WorkerScaningQrResultFragmentBinding workerScaningQrResultFragmentBinding;
    Context context;

    public void Init(WorkerScaningQrResultFragmentBinding workerScaningQrResultFragmentBinding, Context context, String name, String phone, String category, String img,int id) {
        this.context = context;
        this.workerScaningQrResultFragmentBinding = workerScaningQrResultFragmentBinding;
        workerScaningQrResultFragmentBinding.scanbtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.requestsFragment);
        });
        workerScaningQrResultFragmentBinding.category.setText(category);
        workerScaningQrResultFragmentBinding.name.setText(name);
        workerScaningQrResultFragmentBinding.phone.setText(phone);
        Picasso.get().load(RetrofitClient.BASE_URL + '/' + img).error(R.drawable.ic_baseline_cancel_24).into(workerScaningQrResultFragmentBinding.img);
    }
}
