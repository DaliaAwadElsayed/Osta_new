package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.databinding.SplashFragmentBinding;
import com.dtag.osta.utility.Sal7haSharedPreference;

public class SplashViewModel extends ViewModel {
    SplashFragmentBinding splashFragmentBinding;
    private FragmentActivity activity;
    Context context;

    public void Init(SplashFragmentBinding splashFragmentBinding, Context context) {
        this.activity = (FragmentActivity) context;
        this.context = context;
        this.splashFragmentBinding = splashFragmentBinding;
        final Handler handler = new Handler();
        new Thread(() -> {
            try {

                Thread.sleep(2500);
            } catch (Exception e) {
            }
            handler.post(() -> {
                ((MainActivity) activity).splash();
                Log.i("lang", Sal7haSharedPreference.getSelectedLanguageId(splashFragmentBinding.getRoot().getContext()));
//                if (!Sal7haSharedPreference.getSelectedLanguageId(splashFragmentBinding.getRoot().getContext()).isEmpty()) {
//                    Navigation.findNavController(splashFragmentBinding.getRoot()).navigate(R.id.samoolaFragment);
//                } else {
//                    Navigation.findNavController(splashFragmentBinding.getRoot()).navigate(R.id.samoolaFragment);
//
//                }
            });

        }).start();

    }


}

