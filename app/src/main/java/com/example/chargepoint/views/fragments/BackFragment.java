package com.example.chargepoint.views.fragments;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by Art
 */
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
