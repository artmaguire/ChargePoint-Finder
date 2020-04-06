package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.ChargePoint;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyPowerFragment extends Fragment {

    private String TAG = "BUY_POWER";

    public BuyPowerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);

        Bundle b = getArguments();
        if (b != null) {
            ChargePoint cp = (ChargePoint) b.getSerializable("ChargePoint");
            Log.d(TAG, cp.toString());
        }

        return inflater.inflate(R.layout.fragment_buy_power, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
