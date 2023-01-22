package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.NotificationFragmentBinding;

public class NotificationViewModel extends ViewModel {
    NotificationFragmentBinding notificationFragmentBinding;
    Context context;

    public void init(NotificationFragmentBinding notificationFragmentBinding, Context context) {
        this.notificationFragmentBinding = notificationFragmentBinding;
        this.context = context;

        notificationFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.offersFragment);
            }
        });
    }
}