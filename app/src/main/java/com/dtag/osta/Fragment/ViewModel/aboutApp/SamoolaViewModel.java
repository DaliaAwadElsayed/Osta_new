package com.dtag.osta.Fragment.ViewModel.aboutApp;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.ViewModel;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.R;
import com.dtag.osta.databinding.SamoolaFragmentBinding;
import com.dtag.osta.utility.Sal7haSharedPreference;

public class SamoolaViewModel extends ViewModel {
    SamoolaFragmentBinding samoolaFragmentBinding;
    Context context;
Activity activity;
    public void init(SamoolaFragmentBinding samoolaFragmentBinding,
                     Context context,Activity activity) {
        this.samoolaFragmentBinding = samoolaFragmentBinding;
        this.context = context;
        this.activity=activity;
        final Handler handler = new Handler();
        new Thread(() -> {
            try {
                samoolaFragmentBinding.imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            handler.post(() -> {
                Log.i("langggg", Sal7haSharedPreference.getSelectedLanguageId(samoolaFragmentBinding.getRoot().getContext()));
                ((MainActivity) activity).languageWlc();
            });

        }).start();
    }
}