package com.example.chargepoint.ui.profile;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.chargepoint.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends PreferenceFragmentCompat {

    private static final String TAG = "PROFILE_ACTIVITY";
    private ProfileViewModel profileViewModel;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}