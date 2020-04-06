package com.example.chargepoint.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;
import com.example.chargepoint.R;

public class PaymentPageActivity extends AppCompatActivity {

    private CardForm cardform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        cardform=(CardForm)findViewById(R.id.credit_card);
        TextView textDes = (TextView)findViewById(R.id.payment_amount);
        Button btnpay = (Button)findViewById(R.id.btn_pay);

        textDes.setText("€20");
        btnpay.setText(String .format("Payer Name is:", textDes.getText()));
        cardform.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(PaymentPageActivity.this,"Name :"
                        +card.getName()+"Last 4 Digits"+card.getLast4(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
