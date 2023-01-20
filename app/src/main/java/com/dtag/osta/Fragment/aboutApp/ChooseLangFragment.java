package com.dtag.osta.Fragment.aboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.Fragment.ViewModel.aboutApp.ChooseLangViewModel;
import com.dtag.osta.databinding.ChooseLangFragmentBinding;

public class ChooseLangFragment extends Fragment {

    private ChooseLangViewModel mViewModel;
    ChooseLangFragmentBinding chooseLangFragmentBinding;

    public static ChooseLangFragment newInstance() {
        return new ChooseLangFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        chooseLangFragmentBinding = ChooseLangFragmentBinding.inflate(inflater, container, false);
        return chooseLangFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChooseLangViewModel.class);
        mViewModel.init(chooseLangFragmentBinding, getContext());
    }

}