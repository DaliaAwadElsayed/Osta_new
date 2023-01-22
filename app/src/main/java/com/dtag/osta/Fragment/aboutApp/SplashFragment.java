package com.dtag.osta.Fragment.aboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.aboutApp.SplashViewModel;
import com.dtag.osta.databinding.SplashFragmentBinding;
import com.dtag.osta.utility.Utility;

public class SplashFragment extends Fragment {

    private SplashViewModel mViewModel;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    SplashFragmentBinding splashFragmentBinding;
    private MainActivity main;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        splashFragmentBinding = SplashFragmentBinding.inflate(inflater, container, false);
        return splashFragmentBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).hideBottomMenu();

        Utility.deleteCache(getContext());
        mViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        mViewModel.Init(splashFragmentBinding, getContext());

    }

}
