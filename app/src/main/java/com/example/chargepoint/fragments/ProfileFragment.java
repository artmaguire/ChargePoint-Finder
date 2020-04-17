package com.example.chargepoint.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.SplashScreen;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends PreferenceFragmentCompat {

    private View view;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String pk = preference.getKey();

        if (pk.equals(getString(R.string.account_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_update_information);
        else if (pk.equals(getString(R.string.payment_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_payment_details);
        else if (pk.equals(getString(R.string.car_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_car_details);
        else if (pk.equals(getString(R.string.payments_receipts)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_previous_receipts);
        else if (pk.equals(getString(R.string.dark_theme)))
            getActivity().recreate();
        else if (pk.equals(getString(R.string.about_us)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_about);
        else if (pk.equals(getString(R.string.sign_out)))
            signOut();

        return true;
    }

    private void signOut() {
        final MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());
        alertDialog.setMessage("Are you sure you want to sign out?");
        alertDialog.setCancelable(true);
        alertDialog.setNegativeButton("No", null);

        alertDialog.setPositiveButton("Yes", (dialog, which) -> AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    // TODO: Intent to new splash screen, if applicable
                    Intent i = new Intent(getActivity(), SplashScreen.class);
                    startActivity(i);
                    getActivity().finish();
                }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()));

        alertDialog.show();
    }
}