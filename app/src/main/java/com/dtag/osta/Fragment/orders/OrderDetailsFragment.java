package com.dtag.osta.Fragment.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.databinding.OrderDetailsFragmentBinding;
import com.dtag.osta.Fragment.ViewModel.orders.OrderDetailsViewModel;

public class OrderDetailsFragment extends Fragment {

    private OrderDetailsViewModel mViewModel;
    OrderDetailsFragmentBinding orderDetailsFragmentBinding;

    public static OrderDetailsFragment newInstance() {
        return new OrderDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        orderDetailsFragmentBinding = OrderDetailsFragmentBinding.inflate(inflater, container, false);
        return orderDetailsFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderDetailsViewModel.class);
        ((MainActivity) getActivity()).openDrawer();
        mViewModel.Init(orderDetailsFragmentBinding, getContext(),getArguments().getInt("order_id"),getArguments().getBoolean("isNotification",false),getArguments().getString("body"));
    }

}
