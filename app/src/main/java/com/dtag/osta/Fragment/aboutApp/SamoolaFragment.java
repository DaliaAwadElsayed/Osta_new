package com.dtag.osta.Fragment.aboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.databinding.SamoolaFragmentBinding;

public class SamoolaFragment extends Fragment {

    private SamoolaViewModel mViewModel;
    SamoolaFragmentBinding samoolaFragmentBinding;

    public static SamoolaFragment newInstance() {
        return new SamoolaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        samoolaFragmentBinding = SamoolaFragmentBinding.inflate(inflater, container, false);
        return samoolaFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SamoolaViewModel.class);
        mViewModel.init(samoolaFragmentBinding, getContext());
    }

}