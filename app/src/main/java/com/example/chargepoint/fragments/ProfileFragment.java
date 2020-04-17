package com.example.chargepoint.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.chargepoint.R;

public class ProfileFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String pk = preference.getKey();

        if (pk.equals(getString(R.string.dark_theme)))
            getActivity().recreate();
        else
            return super.onPreferenceTreeClick(preference);

        return true;
    }
}