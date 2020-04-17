package com.example.chargepoint.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Mail;
import com.example.chargepoint.pojo.Receipt;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {
    private static final String TAG = "GET RECEIPT";
    private Receipt receipt;

    private TextView dateView;
    private TextView invoiceView;
    private TextView electricityView;
    private TextView timeView;
    private TextView locationView;
    private TextView cardView;
    private TextView euroView;
    private TextView operatorView;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Receipt receipt = getArguments().getParcelable("Receipt");
        Log.d(TAG, "onCreate: " + receipt.toString());

        Date timeToDate = receipt.getDatetime().toDate();
        Format formatter = new SimpleDateFormat("HH:mm:ss");
        String time = formatter.format(timeToDate);

        Log.d(TAG, "onCreateTime: " + time);

        Date date = receipt.getDatetime().toDate();
        String pattern = "MMMM dd, yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        view.findViewById(R.id.button_email).setOnClickListener(v -> showEmail(view));
        view.findViewById(R.id.button_send).setOnClickListener(v -> sendEmail(view));

        dateView = view.findViewById(R.id.receiptDate);
        invoiceView = view.findViewById(R.id.receiptInvoiceID);
        electricityView = view.findViewById(R.id.receiptAmountElectricity);
        timeView = view.findViewById(R.id.receiptTime);
        locationView = view.findViewById(R.id.receiptLocation);
        cardView = view.findViewById(R.id.receiptPayment);
        euroView = view.findViewById(R.id.receiptAmountEuro);
        operatorView = view.findViewById(R.id.chargePointOperator);

        dateView.setText(dateString);
        invoiceView.setText("Receipt ID: " + receipt.getInvoice_id());
        electricityView.setText("Amount (kWh): " + receipt.getElectricity() + "kWh");
        timeView.setText(time);
        cardView.setText("Paid with: ".concat(receipt.getCard()));
        euroView.setText("Amount (€): " + "€" + receipt.getCost());

        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
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
                    county = "County not provided.";

                locationView.setText("Address:\n" + title + ",\n" + line1 + ",\n" + town + ",\n" + county);
                operatorView.setText("Operator:\n" + cp.getOperator());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("SetTextI18n")
    private void sendEmail(View view) {
        EditText email = view.findViewById(R.id.email);
        TextView statement = view.findViewById(R.id.statement);
        statement.setVisibility(View.VISIBLE);

        if (isEmailAdress(email.getText().toString())) {
            String to = email.getText().toString().trim();
            Mail.sendMail(to, receipt);

            statement.setText("Email sent !");
        } else {
            statement.setText("Wrong email address.");
        }
    }

    private boolean isEmailAdress(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();
    }
}
