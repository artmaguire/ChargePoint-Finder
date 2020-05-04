package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.services.ChargingService;
import com.example.chargepoint.viewmodel.ReceiptViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.firebase.Timestamp.now;

public class BuyPowerFragment extends BackFragment {

    private static final String TAG = "BUY_POWER";

    private ChargePoint cp;

    private Spinner timeSpinner;
    private EditText amountEditText;
    private Button payButton;
    private List<String> spinnerTimes;
    private List<String> spinnerCards;
    private Spinner cardSpinner;
    private String cardNumber;
    private TextView chargePointOperator;
    private TextView chargePointAddress;
    private TextView kWhView;
    private TextView voltsView;
    private TextView ampsView;
    private TextView rateView;

    // TODO: Get rate from db
    private final double rate = 0.33;
    private double kwhr;

    public BuyPowerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buy_power, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialise UI Elements
        chargePointOperator = view.findViewById(R.id.chargePointOperator);
        chargePointAddress = view.findViewById(R.id.chargePointAddress);
        kWhView = view.findViewById(R.id.kiloWattTextView);
        voltsView = view.findViewById(R.id.voltageTextView);
        ampsView = view.findViewById(R.id.ampsTextView);
        // TODO: Get rates from db
        rateView = view.findViewById(R.id.rateTextView);

        timeSpinner = view.findViewById(R.id.timeSpinner);
        amountEditText = view.findViewById(R.id.amountCostEditText);
        payButton = view.findViewById(R.id.payButton);
        cardSpinner = view.findViewById(R.id.cardSpinner);

        cardNumber = "";

        Bundle b = getArguments();

        if (b != null) {
            cp = (ChargePoint) b.getSerializable("ChargePoint");
            setFromChargePoint();
        } else {
            Log.d(TAG, "onViewCreated: No ChargePoint Bundle");
        }

        getCards();

        cardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                if (position == spinnerCards.size() - 1) {
                    Navigation.findNavController(view).navigate(R.id.action_fragment_buy_power_to_fragment_payment_details);
                    return;
                }

                cardNumber = spinnerCards.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTimes = new ArrayList<>();
        spinnerTimes.add("--");
        for (int i = 20; i <= 300; i += 20)
            spinnerTimes.add(String.valueOf(i));

        setTimeSpinnerAdapter();

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    setCost(Integer.parseInt(spinnerTimes.get(position)));
                } else if (spinnerTimes.get(0).equals("--"))
                    payButton.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (amountEditText.hasFocus())
                    if (!s.toString().trim().isEmpty())
                        setTime(Double.parseDouble(s.toString()));
                    else
                        payButton.setEnabled(false);
            }
        });

        payButton.setOnClickListener(v -> {
            if (!cardNumber.equals("")) {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getString(R.string.generateing_receipt), true);

                double cost = Double.parseDouble(amountEditText.getText().toString());
                int duration = Integer.parseInt(timeSpinner.getSelectedItem().toString());

                FirebaseHelper fbHelper = FirebaseHelper.getInstance();

                Receipt r = new Receipt(UUID.randomUUID()
                        .toString()
                        .substring(0, 17), cost, duration, now(), cardNumber, Double.parseDouble(amountEditText.getText()
                        .toString()), cp.getMap_id(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                fbHelper.addReceiptToDB(r, task -> {
                            dialog.dismiss();

                    Intent i = new Intent(requireActivity(), ChargingService.class);
                    i.putExtra(ChargingService.CHARGE_RECEIPT, r);
                    requireActivity().startService(i);

                            new ViewModelProvider(requireActivity()).get(ReceiptViewModel.class).destroyReceipts();
                            Navigation.findNavController(view).popBackStack();
                        });
            } else {
                Toast.makeText(getContext(), getString(R.string.no_cards_available), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFromChargePoint() {
        chargePointOperator.setText(cp.getOperator());
        chargePointAddress.setText(cp.getSimpleAddress());

        kwhr = cp.getConnections().get(0).getPowerKW();
        kWhView.setText(getString(R.string.kilowatt_hour, kwhr));

        String voltage = cp.getConnections().get(0).getVoltage() != -1 ? String.valueOf(cp.getConnections().get(0).getVoltage()) : "--";
        String ampere = cp.getConnections().get(0).getAmps() != -1 ? String.valueOf(cp.getConnections().get(0).getAmps()) : "--";

        voltsView.setText(getString(R.string.voltage, voltage));
        ampsView.setText(getString(R.string.amp, ampere));
    }

    private void setTimeSpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerTimes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(arrayAdapter);
    }

    private void getCards() {
        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getCards(task -> {
            spinnerCards = new ArrayList<>();
            spinnerCards.add(getString(R.string.select_card));
            if (!task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.contains("cardNumber"))
                        spinnerCards.add(document.getString("cardNumber"));
                }
                spinnerCards.add(getString(R.string.create_card));
            } else {
                spinnerCards.add(getString(R.string.create_card));
            }

            setCardSpinnerAdapter();
        });
    }

    private void setCardSpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerCards);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardSpinner.setAdapter(arrayAdapter);
    }

    private void setCost(int time) {
        double hrs = time / 60.0; // Convert mins to hrs
        double cost = hrs * rate * kwhr; // Rate in KW

        DecimalFormat df2 = new DecimalFormat("#.##");
        amountEditText.setText(df2.format(cost));
        payButton.setEnabled(true);
    }

    private void setTime(double cost) {
        if (cost <= 0) {
            payButton.setEnabled(false);
            return;
        }

        int time = (int) Math.round(cost / kwhr / rate * 60);
        spinnerTimes.set(0, String.valueOf(time));

        setTimeSpinnerAdapter();
        payButton.setEnabled(true);
    }
}
