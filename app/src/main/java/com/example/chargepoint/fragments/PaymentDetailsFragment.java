package com.example.chargepoint.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailsFragment extends Fragment implements PaymentResultListener {

    private Button startpayment;
    private EditText orderamount;
    private String TAG = "main";


    public PaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startpayment = view.findViewById(R.id.startpayment);
        orderamount = view.findViewById(R.id.orderamount);

        startpayment.setOnClickListener(v -> {
            if (orderamount.getText().toString().equals("")) {

                Toast.makeText(getContext(), "Amount is empty", Toast.LENGTH_LONG).show();
            } else {
                startPayment();
            }
        });
    }

    private void startPayment() {

        final Activity activity = getActivity();

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "ChargePoint");
            options.put("description", "App Payment");
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "EUR");

            String payment = orderamount.getText().toString();

            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject prefill = new JSONObject();
            prefill.put("email", "melesheku@hotmail.com");
            prefill.put("contact", "353894552959");

            options.put("prefill", prefill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, "Payment successful " + s);
        Toast.makeText(getContext(), "Payment successfully done! " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e(TAG, "error code" + i + " -- Payment Failed " + s);
        try {
            Toast.makeText(getContext(), "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in OnPaymentError", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
