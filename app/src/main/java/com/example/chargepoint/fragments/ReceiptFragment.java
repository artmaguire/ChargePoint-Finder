package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReceiptFragment extends Fragment {
    private static final String TAG = "GET RECEIPT";

    private TextView dateView;
    private TextView invoiceView;
    private TextView electricityView;
    private TextView timeView;
    private TextView locationView;
    private TextView cardView;
    private TextView euroView;


    public ReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        String time = String.valueOf(timeToDate.getTime());
        Log.d(TAG, "onCreate: " + timeView);

        Date date = receipt.getDatetime().toDate();
        String pattern = "MMMM dd, yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        dateView = view.findViewById(R.id.receiptDate);
        invoiceView = view.findViewById(R.id.receiptInvoiceID);
        electricityView = view.findViewById(R.id.receiptAmountElectricity);
        timeView = view.findViewById(R.id.receiptTime);
        locationView = view.findViewById(R.id.receiptLocation);
        cardView = view.findViewById(R.id.receiptPayment);
        euroView = view.findViewById(R.id.receiptAmountEuro);

        dateView.setText(dateString);
        invoiceView.setText(receipt.getInvoiceID());
        electricityView.setText(receipt.getElectricity() + "kWh");
        timeView.setText(time);
        locationView.setText("No where");
        cardView.setText("Paid with: ".concat(receipt.getCard()));
        euroView.setText("€" + receipt.getCost());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
