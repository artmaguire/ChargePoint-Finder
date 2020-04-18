package com.example.chargepoint.fragments;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

abstract class BackFragment extends Fragment {

    BackFragment() {
        this.setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Activity activity = getActivity();
        if (activity != null)
            activity.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
