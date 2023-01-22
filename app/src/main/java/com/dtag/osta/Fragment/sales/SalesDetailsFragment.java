package com.dtag.osta.Fragment.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.SalesDetailsFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.sales.SalesDetailsViewModel;

public class SalesDetailsFragment extends Fragment {
    SalesDetailsFragmentBinding salesDetailsFragmentBinding;
    private SalesDetailsViewModel mViewModel;

    public static SalesDetailsFragment newInstance() {
        return new SalesDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        salesDetailsFragmentBinding = SalesDetailsFragmentBinding.inflate(inflater, container, false);

        return salesDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel = ViewModelProviders.of(this).get(SalesDetailsViewModel.class);
        mViewModel.Init(salesDetailsFragmentBinding, getContext(),getArguments().getInt("id"));
    }

}
