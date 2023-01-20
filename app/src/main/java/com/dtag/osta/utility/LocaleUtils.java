package com.dtag.osta.utility;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleUtils {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, ARABIC})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = {ENGLISH, ARABIC};
    }

    public static final String ENGLISH = "en";
    public static final String ARABIC = "ar";


    public static void initialize(Context context) {
        setLocale(context, ENGLISH);
    }

    public static void initialize(Context context, @LocaleDef String defaultLanguage) {
        setLocale(context, defaultLanguage);
    }


    public static boolean setLocale(Context context, @LocaleDef String language) {
        return updateResources(context, language);
    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return true;
    }
}
