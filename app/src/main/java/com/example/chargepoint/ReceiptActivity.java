package com.example.chargepoint;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "GET RECEIPT";

    private TextView dateView;
    private TextView invoiceView;
    private TextView electricityView;
    private TextView timeView;
    private TextView locationView;
    private TextView cardView;
    private TextView euroView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Receipt receipt = getIntent().getParcelableExtra("receipt");
        Log.d(TAG, "onCreate: " + receipt.toString());

        Date timeToDate = receipt.getDatetime().toDate();
        String time = String.valueOf(timeToDate.getTime());
        Log.d(TAG, "onCreate: " + timeView);

        Date date = receipt.getDatetime().toDate();
        String pattern = "MMMM dd, yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        dateView = findViewById(R.id.receiptDate);
        invoiceView = findViewById(R.id.receiptInvoiceID);
        electricityView = findViewById(R.id.receiptAmountElectricity);
        timeView = findViewById(R.id.receiptTime);
        locationView = findViewById(R.id.receiptLocation);
        cardView = findViewById(R.id.receiptPayment);
        euroView = findViewById(R.id.receiptAmountEuro);

        dateView.setText(dateString);
        invoiceView.setText(receipt.getInvoiceID());
        electricityView.setText(receipt.getElectricity() + "kWh");
        timeView.setText(time);
        locationView.setText("No where");
        cardView.setText("Paid with: ".concat(receipt.getCard()));
        euroView.setText("€" + receipt.getCost());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
