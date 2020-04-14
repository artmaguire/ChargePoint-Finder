package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Receipt;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.firebase.Timestamp.now;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyPowerFragment extends Fragment {

    private String TAG = "BUY_POWER";

    private ChargePoint cp;

    private Spinner timeSpinner;
    private EditText amountEditText;
    private Button payButton;
    private List<String> spinnerTimes;
    private double cost;

    // TODO: Get rate from db
    private double rate = 0.33;
    private double kwhr;

    public BuyPowerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.setHasOptionsMenu(true);

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
            Log.d(TAG, cp.toString());
        }

        Log.d(TAG, cp.getOperator());

        chargePointOperator.setText(cp.getOperator());
        chargePointAddress.setText(cp.getSimpleAddress());

        kwhr = cp.getConnections().get(0).getPowerKW();
        kWhView.setText(kwhr + " kWh");

        String voltage = cp.getConnections().get(0).getVoltage() != -1 ? String.valueOf(cp.getConnections().get(0).getVoltage()) : "--";
        String ampere = cp.getConnections().get(0).getAmps() != -1 ? String.valueOf(cp.getConnections().get(0).getAmps()) : "--";

        voltsView.setText(voltage + " V");
        ampsView.setText(ampere + " A");

        spinnerTimes = new ArrayList<>();
        spinnerTimes.add("--");
        for (int i = 20; i <= 300; i += 20)
            spinnerTimes.add(String.valueOf(i));

        setTimeSpinnerAdapter();

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cost = Double.parseDouble(spinnerTimes.get(position));
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
            Log.d(TAG, "Cost: " + cost);
            FirebaseHelper fbHelper = FirebaseHelper.getInstance();
            fbHelper.getCards(task -> {
                Log.d(TAG, "Task: " + task.isSuccessful());
                if (!task.getResult().isEmpty()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Receipt r = new Receipt(UUID.randomUUID().toString(), cost, now(),
                                document.getString("cardno"),
                                Double.parseDouble(amountEditText.getText().toString()),
                                cp.getMap_id());

                        fbHelper.addReceiptToDB(r, task1 -> Log.d(TAG, "Receipt added to Firestore."));
                        Log.d(TAG, "name" + " " + document.getString("cardno") + "\n" + document.getString("uid") + "\n" + document.getString("cardname"));
                    }
                } else {
                    Toast.makeText(getContext(), "No card available for this user.", Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, String.valueOf(cost));
        if (cost <= 0) {
            payButton.setEnabled(false);
            return;
        }

        int time = (int) Math.round(cost / kwhr / rate * 60);
        spinnerTimes.set(0, String.valueOf(time));

        setTimeSpinnerAdapter();
        payButton.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
