package com.dtag.osta.Fragment.aboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.SettingFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.aboutApp.SettingViewModel;

public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;
    SettingFragmentBinding settingFragmentBinding;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        settingFragmentBinding = SettingFragmentBinding.inflate(inflater, container, false);
        return settingFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();

        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        mViewModel.Init(settingFragmentBinding, getContext());
    }

}
