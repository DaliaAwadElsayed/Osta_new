package com.dtag.osta.Fragment.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;import com.dtag.osta.databinding.SignInPhoneNumberFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.authentication.SignInPhoneNumberViewModel;

public class SignInPhoneNumberFragment extends Fragment {

    private SignInPhoneNumberViewModel mViewModel;
    SignInPhoneNumberFragmentBinding signInPhoneNumberFragmentBinding;

    public static SignInPhoneNumberFragment newInstance() {
        return new SignInPhoneNumberFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        signInPhoneNumberFragmentBinding = SignInPhoneNumberFragmentBinding.inflate(inflater, container, false);
        return signInPhoneNumberFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).openDrawer();

        mViewModel = ViewModelProviders.of(this).get(SignInPhoneNumberViewModel.class);
        mViewModel.Init(signInPhoneNumberFragmentBinding, getContext());
    }

}
