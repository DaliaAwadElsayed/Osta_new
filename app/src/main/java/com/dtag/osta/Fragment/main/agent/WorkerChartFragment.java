package com.dtag.osta.Fragment.main.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.osta.Fragment.ViewModel.main.agent.WorkerChartViewModel;
import com.dtag.osta.databinding.WorkerChartFragmentBinding;


public class WorkerChartFragment extends Fragment {
    WorkerChartFragmentBinding workerChartFragmentBinding;
    private WorkerChartViewModel mViewModel;

    public static WorkerChartFragment newInstance() {
        return new WorkerChartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        workerChartFragmentBinding = WorkerChartFragmentBinding.inflate(inflater, container, false);

        return workerChartFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WorkerChartViewModel.class);
        if (getArguments() != null) {
            mViewModel.Init(workerChartFragmentBinding, getContext(), getArguments().getInt("order_id"), getArguments().getBoolean("isNotification", false));
        } else {
            mViewModel.Init(workerChartFragmentBinding, getContext(),1,false);
        }
    }
}
