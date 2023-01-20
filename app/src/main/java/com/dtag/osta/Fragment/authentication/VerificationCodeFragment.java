package com.dtag.osta.Fragment.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.VerificationCodeFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.authentication.VerificationCodeViewModel;

public class VerificationCodeFragment extends Fragment {

    private VerificationCodeViewModel mViewModel;
    VerificationCodeFragmentBinding verificationFragmentBinding;

    public static VerificationCodeFragment newInstance() {
        return new VerificationCodeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        verificationFragmentBinding = VerificationCodeFragmentBinding.inflate(inflater, container, false);

        verificationFragmentBinding.enter.setOnClickListener(view -> {
            verificationFragmentBinding.linearLayout2.setAlpha(0.5f);
        });
        return verificationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).openDrawer();


        mViewModel = ViewModelProviders.of(this).get(VerificationCodeViewModel.class);
        mViewModel.Init(verificationFragmentBinding,getContext(),getArguments().getString("phoneNumber"));
    }
}
