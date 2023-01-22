package com.dtag.osta.Fragment.main.user.scaning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.WorkerScaningQrResultFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.main.user.scaning.WorkerScaningQrResultViewModel;

public class WorkerScaningQrResultFragment extends Fragment {

    private WorkerScaningQrResultViewModel mViewModel;
    WorkerScaningQrResultFragmentBinding workerScaningQrResultFragmentBinding;

    public static WorkerScaningQrResultFragment newInstance() {
        return new WorkerScaningQrResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        workerScaningQrResultFragmentBinding = WorkerScaningQrResultFragmentBinding.inflate(inflater, container, false);

        return workerScaningQrResultFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();

        mViewModel = ViewModelProviders.of(this).get(WorkerScaningQrResultViewModel.class);
mViewModel.Init(workerScaningQrResultFragmentBinding,getContext(),getArguments().getString("agentName"),getArguments().getString("agentPhone"),getArguments().getString("agentCategory"),getArguments().getString("agentImage"),getArguments().getInt("order_id_result"));
    }

}
