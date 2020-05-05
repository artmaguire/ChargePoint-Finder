package com.example.chargepoint.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.SplashScreen;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.receivers.ChargingAlarmReceiver;
import com.example.chargepoint.services.ChargingService;
import com.example.chargepoint.utils.ChargePointNotificationManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends PreferenceFragmentCompat {

    private View view;
    private ListPreference languagePref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        languagePref = findPreference(getString(R.string.key_language));

        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener((preference, newValue) -> {
                String currLang = PreferenceManager.getDefaultSharedPreferences(requireActivity()).getString(getString(R.string.key_language), getString(R.string.en_gb));

                if (!currLang.equals(newValue)) {
                    requireActivity().recreate();
                }
                return true;
            });
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String pk = preference.getKey();

        if (pk.equals(getString(R.string.key_account_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_update_information);
        else if (pk.equals(getString(R.string.key_payment_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_payment_details);
        else if (pk.equals(getString(R.string.key_car_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_car_details);
        else if (pk.equals(getString(R.string.key_payment_receipts)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_previous_receipts);
        else if (pk.equals(getString(R.string.key_dark_mode)))
            requireActivity().recreate();
        else if (pk.equals(getString(R.string.key_about_us)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_about);
        else if (pk.equals(getString(R.string.title_terms)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_terms);
        else if (pk.equals(getString(R.string.key_sign_out)))
            signOut();

        return true;
    }

    private void signOut() {
        final MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(requireContext());
        alertDialog.setMessage(getString(R.string.sign_out_question));
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton(R.string.no, null);
        alertDialog.setPositiveButton(R.string.yes, (dialog, which) -> {
            requireActivity().stopService(new Intent(requireActivity(), ChargingService.class));
            ChargePointNotificationManager.cancelAll(requireContext());
            cancelAlarm();
            FirebaseAuth.getInstance().signOut();
            FirebaseHelper.destroyInstance();

            startActivity(new Intent(getActivity(), SplashScreen.class));
            requireActivity().finish();
        });

        alertDialog.show();
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(requireContext(), ChargingAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_NO_CREATE);
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}