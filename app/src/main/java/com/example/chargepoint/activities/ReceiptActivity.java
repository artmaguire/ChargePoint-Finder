package com.example.chargepoint.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Mail;
import com.example.chargepoint.pojo.Receipt;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptActivity extends AppCompatActivity {

    private static final String TAG = "GET RECEIPT";
    private Receipt receipt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receipt = getIntent().getParcelableExtra("receipt");

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

    @SuppressLint("SetTextI18n")
    public void sendEmail(View view) {

        EditText email = findViewById(R.id.email);
        TextView statement = findViewById(R.id.statement);
        statement.setVisibility(View.VISIBLE);

        if(isEmailAdress(email.getText().toString())) {

            Mail.sendMail("aarthur.francois@gmail.com", receipt);

            statement.setText("Email sent !");
        } else {
            statement.setText("Wrong email address.");
        }


    }

    public void showEmail(View view) {

        TextView specified = findViewById(R.id.specified);
        EditText email = findViewById(R.id.email);
        Button button_send = findViewById(R.id.button_send);

        specified.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        button_send.setVisibility(View.VISIBLE);

    }

    public boolean isEmailAdress(String email) {

        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
        Matcher m = p.matcher(email.toUpperCase());
        return m.matches();

    }
}
