package com.example.chargepoint.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    private Button startpayment;
    private EditText orderamount;
    private String TAG = "main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        startpayment = findViewById(R.id.startpayment);
        orderamount = findViewById(R.id.orderamount);

        startpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderamount.getText().toString().equals("")){

                    Toast.makeText(PaymentDetailsActivity.this, "Amount is empty", Toast.LENGTH_LONG).show();
                } else{
                    startPayment();
                }
            }
        });
    }

    public void startPayment(){

        final Activity activity = this;

        final Checkout co = new Checkout();

        try{
            JSONObject options = new JSONObject();
            options.put("name","ChargePoint");
            options.put("description", "App Payment");
            options.put("image","https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "EUR");

            String payment = orderamount.getText().toString();

            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject prefill = new JSONObject();
            prefill.put("email","melesheku@hotmail.com");
            prefill.put("contact","353894552959");

            options.put("prefill", prefill);

            co.open(activity, options);
        }catch(Exception e){
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, "Payment successful " + s);
       Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "error code" + i + " -- Payment Failed " + s);
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e("OnPaymentError","Exception in OnPaymentError", e);
        }

    }
}
