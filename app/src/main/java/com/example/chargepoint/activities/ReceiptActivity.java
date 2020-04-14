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
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Receipt;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiptActivity extends AppCompatActivity {

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Receipt receipt = getIntent().getParcelableExtra("receipt");
        Log.d(TAG, "onCreate: " + receipt.toString());

        Date timeToDate = receipt.getDatetime().toDate();
        Format formatter = new SimpleDateFormat("HH:mm:ss");
        String time = formatter.format(timeToDate);

        Log.d(TAG, "onCreateTime: " + time);

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
        operatorView = findViewById(R.id.chargePointOperator);

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
