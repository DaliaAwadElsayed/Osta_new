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
import com.dtag.osta.Fragment.ViewModel.aboutApp.NotificationViewModel;
import com.dtag.osta.databinding.NotificationFragmentBinding;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;
    NotificationFragmentBinding notificationFragmentBinding;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        notificationFragmentBinding = NotificationFragmentBinding.inflate(inflater, container, false);
        return notificationFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        mViewModel.init(notificationFragmentBinding, getContext());
        ((MainActivity) getActivity()).showBottomMenu();

    }

}