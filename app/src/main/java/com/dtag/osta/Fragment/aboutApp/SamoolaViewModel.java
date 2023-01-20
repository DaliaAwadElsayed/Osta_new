package com.dtag.osta.Fragment.aboutApp;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.SamoolaFragmentBinding;
import com.dtag.osta.utility.Sal7haSharedPreference;

public class SamoolaViewModel extends ViewModel {
    SamoolaFragmentBinding samoolaFragmentBinding;
    Context context;

    public void init(SamoolaFragmentBinding samoolaFragmentBinding,
                     Context context) {
        this.samoolaFragmentBinding = samoolaFragmentBinding;
        this.context = context;
        final Handler handler = new Handler();
        new Thread(() -> {
            try {
                samoolaFragmentBinding.imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                Thread.sleep(2500);
            } catch (Exception e) {
            }
            handler.post(() -> {
                Log.i("lang", Sal7haSharedPreference.getSelectedLanguageId(samoolaFragmentBinding.getRoot().getContext()));
                if (!Sal7haSharedPreference.getSelectedLanguageId(samoolaFragmentBinding.getRoot().getContext()).isEmpty()) {
                    Navigation.findNavController(samoolaFragmentBinding.getRoot()).navigate(R.id.offersFragment);
                } else {
                    Navigation.findNavController(samoolaFragmentBinding.getRoot()).navigate(R.id.chooseLangFragment);
                }
            });

        }).start();
    }
}