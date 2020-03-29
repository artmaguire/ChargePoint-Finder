package com.example.chargepoint.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "GET RECEIPT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Receipt receipt = getIntent().getParcelableExtra("receipt");

        Date timeToDate = receipt.getDatetime().toDate();
        String time = String.valueOf(timeToDate.getTime());

        Date date = receipt.getDatetime().toDate();
        String pattern = "MMMM dd, yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        TextView dateR = findViewById(R.id.date);
        TextView location = findViewById(R.id.location);
        TextView mapId = findViewById(R.id.mapId);
        TextView consumption = findViewById(R.id.consumption);
        TextView card = findViewById(R.id.card);
        TextView amount = findViewById(R.id.amount);

        dateR.setText(" Receipt from : " + dateString);
        location.setText(" Location : ");
        mapId.setText(" Map id : " + receipt.getMap_id());
        consumption.setText(" Consumption : " + receipt.getElectricity());
        card.setText(" Card : " + receipt.getCard());
        amount.setText(" Amount : €" + receipt.getCost());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
