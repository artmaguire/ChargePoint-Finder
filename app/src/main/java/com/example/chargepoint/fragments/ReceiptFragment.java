package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Mail;
import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.utils.PreferenceConfiguration;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Art
 * Shows the details of a specific receipt selected in PreviousReceiptsFragment
 */
public class ReceiptFragment extends BackFragment {
    private static final String TAG = "GET RECEIPT";

    private Receipt receipt;

    private TextView locationView;
    private TextView operatorView;
    private TextView durationView;
    private ImageView receiptIcon;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receipt = getArguments().getParcelable("Receipt");
        int iconId = getArguments().getInt("icon");
        Log.d(TAG, "onCreate: " + receipt.toString());

        Date timeToDate = receipt.getDatetime().toDate();
        Locale locale = PreferenceConfiguration.getCurrentLocale(requireContext());
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        String time = tf.format(timeToDate);

        Date date = receipt.getDatetime().toDate();
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
        String dateString = df.format(date);

        view.findViewById(R.id.button_email).setOnClickListener(v -> showEmail(view));
        view.findViewById(R.id.button_send).setOnClickListener(v -> sendEmail(view));

        TextView dateView = view.findViewById(R.id.receiptDate);
        TextView invoiceView = view.findViewById(R.id.receiptInvoiceID);
        TextView electricityView = view.findViewById(R.id.receiptAmountElectricity);
        TextView timeView = view.findViewById(R.id.receiptTime);
        locationView = view.findViewById(R.id.receiptLocation);
        TextView cardView = view.findViewById(R.id.receiptPayment);
        TextView euroView = view.findViewById(R.id.receiptAmountEuro);
        operatorView = view.findViewById(R.id.chargePointOperator);
        durationView = view.findViewById(R.id.receiptDuration);
        receiptIcon = view.findViewById(R.id.receiptIcon);

        dateView.setText(dateString);
        invoiceView.setText(getString(R.string.receipt_id, receipt.getInvoice_id()));
        electricityView.setText(getString(R.string.receipt_power_amount, receipt.getElectricity()));
        timeView.setText(time);
        cardView.setText(getString(R.string.receipt_paid_with, receipt.getCard()));
        euroView.setText(getString(R.string.receipt_amount, receipt.getCost()));
        durationView.setText(getString(R.string.time_mins, receipt.getDuration()));
        receiptIcon.setImageResource(iconId);

        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        Log.d(TAG, "onViewCreated: " + receipt.getMap_id());
        fbHelper.getChargePoint(receipt.getMap_id(), task -> {
            if (task.isSuccessful()) {
                ChargePoint cp = task.getResult().toObject(ChargePoint.class);
                String title, line1, town, county;
                if (cp.getAddress().containsKey("title") && !cp.getAddress().get("title").equals(""))
                    title = cp.getAddress().get("title");
                else
                    title = "";
                if (cp.getAddress().containsKey("line1") && !cp.getAddress().get("line1").equals(""))
                    line1 = cp.getAddress().get("line1");
                else
                    line1 = "";
                if (cp.getAddress().containsKey("town") && !cp.getAddress().get("town").equals(""))
                    town = cp.getAddress().get("town");
                else
                    town = "";
                if (cp.getAddress().containsKey("county") && !cp.getAddress().get("county").equals(""))
                    county = cp.getAddress().get("county");
                else
                    county = getString(R.string.receipt_missing_county);

                locationView.setText(getString(R.string.receipt_address, title, line1, town, county));
                operatorView.setText(getString(R.string.receipt_operator, cp.getOperator()));

                view.findViewById(R.id.receipt_view).setVisibility(View.VISIBLE);
                view.findViewById(R.id.receiptPBar).setVisibility(View.GONE);
            }
        });
    }

    private void showEmail(View view) {
        TextView specified = view.findViewById(R.id.specified);
        EditText email = view.findViewById(R.id.email);
        Button button_send = view.findViewById(R.id.button_send);
        Button button_email = view.findViewById(R.id.button_email);

        specified.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        button_send.setVisibility(View.VISIBLE);
        button_email.setVisibility(View.INVISIBLE);
    }

    private void sendEmail(View view) {
        EditText email = view.findViewById(R.id.email);
        TextView statement = view.findViewById(R.id.statement);
        statement.setVisibility(View.VISIBLE);

        if (isEmailAddress(email.getText().toString())) {
            String to = email.getText().toString().trim();
            Mail.sendMail(to, receipt);

            statement.setText(getString(R.string.email_sent));
        } else {
            statement.setText(getString(R.string.email_wrong_address));
        }
    }

    private boolean isEmailAddress(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
}
