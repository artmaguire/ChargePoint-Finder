package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.utils.ChargePointNotificationManager;
import com.example.chargepoint.viewmodel.ReceiptViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME_FRAG";
    private Receipt receipt;
    private TextView receiptTimer;
    private ProgressBar chargeProgress;
    private View root;

    private static String formatMilli(long milli) {
        Date date = new Date(milli);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        ChargePointNotificationManager.createNotificationChannels(requireActivity());
        //ChargePointNotificationManager.displayCarChargingNotification(requireActivity());

        receiptTimer = root.findViewById(R.id.receiptTimer);
        chargeProgress = root.findViewById(R.id.chargeProgress);

        // Dashboard Navigation by navGraph
        root.findViewById(R.id.aboutCard).setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_about));

        root.findViewById(R.id.newsCard).setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_news));

        root.findViewById(R.id.termsCard).setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_terms));

        root.findViewById(R.id.carDetailsCard).setOnClickListener(v ->
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_car_details));

        // If no timer, the 'Charge Time' card goes to the map
        root.findViewById(R.id.durationCard).setOnClickListener(v -> {
            if (receipt != null && receipt.isCharging()) {
                Bundle b = new Bundle();
                b.putParcelable("Receipt", receipt);
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_receipt, b);
            } else
                Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_map);
        });

        ReceiptViewModel receiptViewModel = new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class);
        receiptViewModel.getObservableReceipts().observe(getViewLifecycleOwner(), receipts -> {
            if (!receipts.isEmpty()) {
                receipt = receipts.get(0);
                if (receipt.isCharging()) {
                    startChargeTimer(receipt.millisUntilChargingOver());
                }
            }
        });

        return root;
    }

    private void startChargeTimer(long millis) {
        if (millis < 0)
            return;

        ChargePointNotificationManager.displayCarChargingNotification(requireActivity(), millis);

        long durationMillis = receipt.getDuration() * 60000;

        new CountDownTimer(millis, 200) {

            public void onTick(long millisUntilFinished) {
                receiptTimer.setText(root.getContext().getString(R.string.time_remaining, formatMilli(millisUntilFinished)));

                long timePast = durationMillis - millisUntilFinished;
                double percentage = (timePast / (double) durationMillis) * 100;

                chargeProgress.setProgress((int) Math.round(percentage));
            }

            public void onFinish() {
                receiptTimer.setText(R.string.charge_complete);
                ChargePointNotificationManager.displayCarChargedNotification(requireActivity());
            }
        }.start();
    }

    @Override
    public void onStart() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
