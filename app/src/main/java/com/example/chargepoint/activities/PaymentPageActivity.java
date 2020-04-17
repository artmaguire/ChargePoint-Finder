package com.example.chargepoint.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.craftman.cardform.CardForm;
import com.example.chargepoint.R;

public class PaymentPageActivity extends AppCompatActivity {

    private CardForm cardform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        cardform = findViewById(R.id.credit_card);
        TextView textDes = findViewById(R.id.payment_amount);
        Button btnpay = findViewById(R.id.btn_pay);

        textDes.setText("€20");
        btnpay.setText(String.format("Payer Name is:", textDes.getText()));
        cardform.setPayBtnClickListner(card -> Toast.makeText(PaymentPageActivity.this, "Name :" + card.getName() + "Last 4 Digits" + card.getLast4(), Toast.LENGTH_SHORT).show());
    }
}
