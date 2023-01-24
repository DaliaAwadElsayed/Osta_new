package com.dtag.osta.Fragment.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.sales.SalesViewModel;
import com.dtag.osta.databinding.SalesFragmentBinding;

public class SalesFragment extends Fragment {

    private SalesViewModel mViewModel;
    SalesFragmentBinding salesFragmentBinding;

    public static SalesFragment newInstance() {
        return new SalesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        salesFragmentBinding = SalesFragmentBinding.inflate(inflater, container, false);

        return salesFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel =new ViewModelProvider(this).get(SalesViewModel.class);
        mViewModel.Init(salesFragmentBinding, getContext());
    }

}
