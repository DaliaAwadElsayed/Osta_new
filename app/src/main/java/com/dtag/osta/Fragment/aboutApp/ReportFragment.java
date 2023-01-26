package com.dtag.osta.Fragment.aboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.aboutApp.ReportViewModel;
import com.dtag.osta.databinding.ReportFragmentBinding;

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    ReportFragmentBinding reportFragmentBinding;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        reportFragmentBinding = ReportFragmentBinding.inflate(inflater, container, false);
        return reportFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel.init(reportFragmentBinding, getContext());
    }

}