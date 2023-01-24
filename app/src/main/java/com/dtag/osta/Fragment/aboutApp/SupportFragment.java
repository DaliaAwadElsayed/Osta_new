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
import com.dtag.osta.Fragment.ViewModel.aboutApp.SupportViewModel;
import com.dtag.osta.databinding.SupportFragmentBinding;

public class SupportFragment extends Fragment {

    private SupportViewModel mViewModel;
    SupportFragmentBinding supportFragmentBinding;

    public static SupportFragment newInstance() {
        return new SupportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        supportFragmentBinding = SupportFragmentBinding.inflate(inflater, container, false);
        return supportFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        mViewModel.Init(supportFragmentBinding,getContext());
    }

}
