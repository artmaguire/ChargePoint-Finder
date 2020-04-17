package com.example.chargepoint.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.MainActivity;
import com.firebase.ui.auth.AuthUI;

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

        if (pk.equals(getString(R.string.about_us)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_about);
        else if (pk.equals(getString(R.string.account_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_update_information);
        else if (pk.equals(getString(R.string.payment_details)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_payment_details);
        else if (pk.equals(getString(R.string.payments_receipts)))
            Navigation.findNavController(view).navigate(R.id.action_navigation_profile_to_fragment_previous_receipts);
        else if (pk.equals(getString(R.string.dark_theme)))
            getActivity().recreate();
        else if (pk.equals(getString(R.string.sign_out)))
            signOut();

        return true;
    }

    private void signOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to sign out?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", null);

        builder.setPositiveButton("Yes", (dialog, which) -> AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    // TODO: Intent to new splash screen, if applicable
                    getActivity().onBackPressed();
                    ((MainActivity) getActivity()).showSignInOptions();
                }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()));

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}