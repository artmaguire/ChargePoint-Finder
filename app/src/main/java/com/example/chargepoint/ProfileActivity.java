package com.example.chargepoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.preference.Preference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements Preference.OnPreferenceClickListener {

    private static final String TAG = "PROFILE_ACTIVITY";
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get current user details
        Log.d(TAG, "User Name: " + currentUser.getDisplayName());
        Log.d(TAG, "User Email: " + currentUser.getEmail());
        Log.d(TAG, "User ID: " + currentUser.getUid());

        setTitle("Profiles");

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTrans = fragMan.beginTransaction();

        SettingsFragment setFrag = new SettingsFragment();
        fragTrans.add(android.R.id.content, setFrag, "SETTINGS_FRAGMENT");
        fragTrans.commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    public boolean onPreferenceClick (Preference preference) {
        String key = preference.getKey();
        Log.d(TAG, "onPreferenceClick: " + key);
        return !key.equals("");
    }
}
