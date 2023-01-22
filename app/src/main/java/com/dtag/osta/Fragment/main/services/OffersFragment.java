package com.dtag.osta.Fragment.main.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.OffersFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.main.services.OffersViewModel;

public class OffersFragment extends Fragment {
    private FragmentActivity activity;
    private OffersViewModel mViewModel;

    public static OffersFragment newInstance() {
        return new OffersFragment();
    }

    OffersFragmentBinding offersFragmentBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        offersFragmentBinding = OffersFragmentBinding.inflate(inflater, container, false);
        return offersFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).showBottomMenu();
        mViewModel = ViewModelProviders.of(this).get(OffersViewModel.class);
        mViewModel.Init(offersFragmentBinding,getContext());
    }



}
