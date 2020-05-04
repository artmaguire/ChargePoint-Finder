package com.example.chargepoint.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.services.ChargingService;
import com.example.chargepoint.viewmodel.ReceiptViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private static final String TAG = "CP_HOME_FRAG";
    private Receipt receipt;
    private TextView receiptTimer;
    private ProgressBar chargeProgress;
    private View root;

    private BroadcastReceiver bReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    private static String formatMilli(long milli) {
        Date date = new Date(milli);
        // TODO: Change to new locale formatting
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receiptTimer = view.findViewById(R.id.receiptTimer);
        chargeProgress = view.findViewById(R.id.chargeProgress);

        // Dashboard Navigation by navGraph
        view.findViewById(R.id.aboutCard).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragment_about));

        view.findViewById(R.id.newsCard).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragment_news));

        root.findViewById(R.id.previousReceiptsCard)
                .setOnClickListener(v -> Navigation.findNavController(root)
                        .navigate(R.id.action_navigation_home_to_fragment_previous_receipts));

        view.findViewById(R.id.carDetailsCard).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragment_car_details));

        // If no timer, the 'Charge Time' card goes to the map
        view.findViewById(R.id.durationCard).setOnClickListener(v -> {
            if (receipt != null && receipt.isCharging()) {
                Bundle b = new Bundle();
                b.putParcelable("Receipt", receipt);
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_fragment_receipt, b);
            } else
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_map);
        });

        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long millis = intent.getLongExtra(ChargingService.CHARGE_COUNTDOWN, 0);

                if (millis <= 0)
                    finishCharging();
                else
                    updateChargeTimer(millis);
            }
        };
    }

    private void getLatestReceipt() {
        // if we currently don't have a receipt, or or current one isn't charging -> check for new receipt
        if (receipt == null || !receipt.isCharging()) {
            ReceiptViewModel receiptViewModel = new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class);
            receiptViewModel.getObservableReceipts().observe(getViewLifecycleOwner(), receipts -> {
                if (!receipts.isEmpty()) {
                    receipt = receipts.get(0);
                    if (receipt.isCharging()) {
                        root.findViewById(R.id.chargeProgress).setVisibility(View.VISIBLE);
                        Intent i = new Intent(requireActivity(), ChargingService.class);
                        i.putExtra(ChargingService.CHARGE_RECEIPT, receipt);
                        requireActivity().startService(i);
                    }
                }
            });
        } else {
            // Stop service in case it was started and couldn't be stopped by receiver
            requireActivity().stopService(new Intent(requireActivity(), ChargingService.class));
        }
    }

    private void updateChargeTimer(long millisUntilFinished) {
        if (receipt == null) return;

        long durationMillis = receipt.getDuration() * 60000;
        receiptTimer.setText(root.getContext().getString(R.string.time_remaining, formatMilli(millisUntilFinished)));

        long timePast = durationMillis - millisUntilFinished;
        double percentage = (timePast / (double) durationMillis) * 100;

        chargeProgress.setProgress((int) Math.round(percentage));
    }

    private void finishCharging() {
        receiptTimer.setText(R.string.charge_complete);
    }

    @Override
    public void onResume() {
        requireActivity().registerReceiver(bReceiver, new IntentFilter(ChargingService.BROADCAST_RECEIVER));
        Receipt r = ChargingService.getReceipt();

        if (r != null)
            receipt = r;
        else
            getLatestReceipt();

        if (receipt != null && receipt.isCharging())
            root.findViewById(R.id.chargeProgress).setVisibility(View.VISIBLE);
        else
            root.findViewById(R.id.chargeProgress).setVisibility(View.INVISIBLE);

        super.onResume();
    }

    @Override
    public void onPause() {
        receipt = null;
        requireActivity().unregisterReceiver(bReceiver);
        super.onPause();
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
