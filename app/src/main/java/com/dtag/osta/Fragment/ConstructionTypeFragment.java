package com.dtag.osta.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.databinding.ConstructionTypeFragmentBinding;

public class ConstructionTypeFragment extends Fragment {

    private ConstructionTypeViewModel mViewModel;
    ConstructionTypeFragmentBinding constructionTypeFragmentBinding;

    public static ConstructionTypeFragment newInstance() {
        return new ConstructionTypeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        constructionTypeFragmentBinding = ConstructionTypeFragmentBinding.inflate(inflater, container, false);
        return constructionTypeFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConstructionTypeViewModel.class);
        mViewModel.init(constructionTypeFragmentBinding, getContext());
    }

}