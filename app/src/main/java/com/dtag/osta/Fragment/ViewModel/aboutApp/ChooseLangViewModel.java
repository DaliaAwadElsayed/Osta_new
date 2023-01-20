package com.dtag.osta.Fragment.ViewModel.aboutApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.dtag.osta.R;
import com.dtag.osta.databinding.ChooseLangFragmentBinding;
import com.dtag.osta.utility.LocaleUtils;
import com.dtag.osta.utility.Sal7haSharedPreference;

public class ChooseLangViewModel extends ViewModel {
    ChooseLangFragmentBinding chooseLangFragmentBinding;
    Context context;

    public void init(ChooseLangFragmentBinding chooseLangFragmentBinding, Context context) {
        this.chooseLangFragmentBinding = chooseLangFragmentBinding;
        this.context = context;
        chooseLangFragmentBinding.englishId.setOnClickListener(view -> {
            initAppLanguages(chooseLangFragmentBinding.getRoot().getContext(), LocaleUtils.ENGLISH);
            Sal7haSharedPreference.changeLanguage(context, 0);
        });
        chooseLangFragmentBinding.arabicId.setOnClickListener(view -> {
            initAppLanguages(chooseLangFragmentBinding.getRoot().getContext(), LocaleUtils.ARABIC);
            Sal7haSharedPreference.changeLanguage(context, 1);
        });
        chooseLangFragmentBinding.backId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(chooseLangFragmentBinding.getRoot()).navigate(R.id.splashFragment);
            }
        });
    }

    public void initAppLanguages(Context context, String lang) {
        Sal7haSharedPreference.setSelectedLanguageId(context, lang);
        LocaleUtils.setLocale(context, lang);
        Intent i = chooseLangFragmentBinding.getRoot().getContext().getPackageManager().getLaunchIntentForPackage(chooseLangFragmentBinding.getRoot().getContext().getPackageName());
        ((Activity) chooseLangFragmentBinding.getRoot().getContext()).finishAffinity();
        ((Activity) chooseLangFragmentBinding.getRoot().getContext()).finish();
        ((Activity) chooseLangFragmentBinding.getRoot().getContext()).startActivity(i);
    }
}