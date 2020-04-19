package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
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
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Receipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.firebase.Timestamp.now;

public class BuyPowerFragment extends BackFragment {

    private final String TAG = "BUY_POWER";

    private ChargePoint cp;

    private Spinner timeSpinner;
    private EditText amountEditText;
    private Button payButton;
    private List<String> spinnerTimes;

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
        TextView chargePointOperator = view.findViewById(R.id.chargePointOperator);
        TextView chargePointAddress = view.findViewById(R.id.chargePointAddress);
        TextView kWhView = view.findViewById(R.id.kiloWattTextView);
        TextView voltsView = view.findViewById(R.id.voltageTextView);
        TextView ampsView = view.findViewById(R.id.ampsTextView);
        // TODO: Get rates from db
        TextView rateView = view.findViewById(R.id.rateTextView);

        timeSpinner = view.findViewById(R.id.timeSpinner);
        amountEditText = view.findViewById(R.id.amountCostEditText);
        payButton = view.findViewById(R.id.payButton);

        Bundle b = getArguments();
        if (b != null) {
            cp = (ChargePoint) b.getSerializable("ChargePoint");
        }

        Log.d(TAG, cp.getOperator());

        chargePointOperator.setText(cp.getOperator());
        chargePointAddress.setText(cp.getSimpleAddress());

        kwhr = cp.getConnections().get(0).getPowerKW();
        kWhView.setText(getString(R.string.kilowatt_hour, kwhr));

        String voltage = cp.getConnections().get(0).getVoltage() != -1 ? String.valueOf(cp.getConnections().get(0).getVoltage()) : "--";
        String ampere = cp.getConnections().get(0).getAmps() != -1 ? String.valueOf(cp.getConnections().get(0).getAmps()) : "--";

        voltsView.setText(getString(R.string.voltage, voltage));
        ampsView.setText(getString(R.string.amp, ampere));

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
            ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getString(R.string.generateing_receipt), true);

            double cost = Double.parseDouble(amountEditText.getText().toString());
            int time = Integer.parseInt(timeSpinner.getSelectedItem().toString());

            FirebaseHelper fbHelper = FirebaseHelper.getInstance();
            fbHelper.getCards(task -> {
                Log.d(TAG, "Task: " + task.isSuccessful());
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Receipt r = new Receipt(UUID.randomUUID().toString(), cost, time, now(),
                                document.getString("cardno"),
                                Double.parseDouble(amountEditText.getText().toString()),
                                cp.getMap_id(), FirebaseAuth.getInstance().getCurrentUser().getUid());

                        fbHelper.addReceiptToDB(r, task1 -> {
                            dialog.dismiss();
                            Navigation.findNavController(view).popBackStack();
                        });
                        Log.d(TAG, "name" + " " + document.getString("cardno") + "\n" + document.getString("uid") + "\n" + document.getString("cardname"));
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), getString(R.string.no_cards_available), Toast.LENGTH_SHORT).show();
                    // TODO: Got to payment details page, will complete after merge
                    Log.d(TAG, "onViewCreated: No card for this user.");
                }
            });
        });
    }

    private void setTimeSpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerTimes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(arrayAdapter);
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
