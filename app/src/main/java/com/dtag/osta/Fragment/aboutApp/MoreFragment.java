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
import com.dtag.osta.Fragment.ViewModel.aboutApp.MoreViewModel;
import com.dtag.osta.databinding.MoreFragmentBinding;

public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;
    MoreFragmentBinding moreFragmentBinding;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        moreFragmentBinding = MoreFragmentBinding.inflate(inflater, container, false);
        return moreFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel.init(moreFragmentBinding, getContext());
    }

}