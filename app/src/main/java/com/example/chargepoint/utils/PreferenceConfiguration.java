package com.example.chargepoint.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.chargepoint.R;

import java.util.Locale;

public class PreferenceConfiguration {

    public static void updateConfiguration(Context context) {
        setNightMode(context);

        Locale newLocale = getNewLocale(context);
        if (newLocale != null)
            setLocale(context, newLocale);
    }

    private static void setNightMode(Context context) {
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.key_dark_mode), false);

        /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Log.d("PREF_", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        Log.d("PREF_", String.valueOf(AppCompatDelegate.MODE_NIGHT_NO));
        Log.d("PREF_", String.valueOf(AppCompatDelegate.MODE_NIGHT_YES));
        Log.d("PREF_", String.valueOf(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY));
        Log.d("PREF_", String.valueOf(AppCompatDelegate.getDefaultNightMode()));*/

        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private static Locale getNewLocale(Context context) {
        String currLang = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.key_language), context.getString(R.string.language_default));

        Locale newLocale;

        // If selected language is default, set newLocale to the default system language, else build new locale from the selected language key
        if (currLang.equals(context.getString(R.string.language_default)))
            newLocale = Locale.getDefault();
        else {
            String[] lang_region = currLang.split("_");
            String lang = lang_region[0];
            String region = lang_region.length > 1 ? lang_region[1] : "";

            newLocale = new Locale(lang, region);
        }

        // Get the current set app locale
        Locale currentLocale = context.getResources().getConfiguration().locale;

        // If the newLocale and currentLocale are the same, no need to change locale
        return newLocale.equals(currentLocale) ? null : newLocale;
    }

    private static void setLocale(Context context, Locale locale) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.uiMode = (conf.uiMode & ~Configuration.UI_MODE_NIGHT_MASK);
        conf.setLocale(locale);
        res.updateConfiguration(conf, dm);
    }
}
