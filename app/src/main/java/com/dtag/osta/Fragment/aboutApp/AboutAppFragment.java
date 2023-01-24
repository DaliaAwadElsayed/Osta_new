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
import com.dtag.osta.Fragment.ViewModel.aboutApp.AboutAppViewModel;
import com.dtag.osta.databinding.AboutAppFragmentBinding;

public class AboutAppFragment extends Fragment {

    private AboutAppViewModel mViewModel;
    private static final String TAG = "AboutAppFragment";

    public static AboutAppFragment newInstance() {
        return new AboutAppFragment();
    }

    AboutAppFragmentBinding aboutAppFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        aboutAppFragmentBinding = AboutAppFragmentBinding.inflate(inflater, container, false);

        return aboutAppFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).showBottomMenu();
        mViewModel = new ViewModelProvider(this).get(AboutAppViewModel.class);
        mViewModel.Init(aboutAppFragmentBinding, getContext());
    }


}
