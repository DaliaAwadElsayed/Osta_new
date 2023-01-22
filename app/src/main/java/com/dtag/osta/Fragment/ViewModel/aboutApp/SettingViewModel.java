package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.Activity.MainActivity;
import com.dtag.osta.R;
import com.dtag.osta.databinding.SettingFragmentBinding;
import com.dtag.osta.utility.Sal7haSharedPreference;

public class SettingViewModel extends ViewModel {
    SettingFragmentBinding settingFragmentBinding;
    Context context;
    private FragmentActivity activity;

    public void Init(SettingFragmentBinding settingFragmentBinding, Context context) {
        this.context = context;
        this.activity = (FragmentActivity) context;
        this.settingFragmentBinding = settingFragmentBinding;
        settingFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.offersFragment);
            }
        });
        settingFragmentBinding.englishId.setOnClickListener(view -> {
            Sal7haSharedPreference.changeLanguage(context, 0);
            resetApplication();
        });
        settingFragmentBinding.arabicId.setOnClickListener(view -> {
            Sal7haSharedPreference.changeLanguage(context, 1);
            resetApplication();
        });

    }

    private void resetApplication() {
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }
}
