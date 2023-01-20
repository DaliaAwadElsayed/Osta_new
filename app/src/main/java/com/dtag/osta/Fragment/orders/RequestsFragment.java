package com.dtag.osta.Fragment.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.databinding.RequestsFragmentBinding;
import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.Fragment.ViewModel.orders.RequestsViewModel;

public class RequestsFragment extends Fragment {
    RequestsFragmentBinding requestsFragmentBinding;
    private RequestsViewModel mViewModel;

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requestsFragmentBinding = RequestsFragmentBinding.inflate(inflater, container, false);
        return requestsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).openDrawer();

        mViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);
        mViewModel.Init(requestsFragmentBinding, getContext());
    }

}
