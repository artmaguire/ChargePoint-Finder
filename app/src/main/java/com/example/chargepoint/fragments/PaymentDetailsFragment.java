package com.example.chargepoint.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Card;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class PaymentDetailsFragment extends BackFragment {

    private String TAG = "PAYMENT_DETAILS";

    private EditText cardName;
    private EditText cardNumber;
    private EditText cardSecurityNumber;
    private EditText txtMonthYear;

    private DatePickerDialog datePickerDialog;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        cardName = view.findViewById(R.id.editcardname);
        cardNumber = view.findViewById(R.id.editTextcardnumber);
        txtMonthYear = view.findViewById(R.id.txtMonthYear);
        cardSecurityNumber = view.findViewById(R.id.securityNumber);
        Button saveCardButton = view.findViewById(R.id.saverbutton);

        //on click functionality to select the month and date of the card
        txtMonthYear.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(),
                    AlertDialog.THEME_HOLO_DARK,
                    (dateView, year, monthOfYear, dayOfMonth) -> txtMonthYear.setText((monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.show();
        });

        // Send card to Firestore when save is clicked
        saveCardButton.setOnClickListener(v -> {
            String name;
            String number;
            String date;
            String securityNumber;

            // Check if user inputted information
            if (cardName.getText().toString().equals("") || cardNumber.getText().toString().equals("") || cardSecurityNumber.getText()
                    .toString()
                    .equals("") || txtMonthYear.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Missing information.", Toast.LENGTH_SHORT).show();
            } else if (cardNumber.getText().length() < 16) {
                Toast.makeText(getContext(), "Card number provided is not long enough.", Toast.LENGTH_SHORT).show();
            } else {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Adding Card...", true);
                name = cardName.getText().toString();
                number = cardNumber.getText().toString();
                date = txtMonthYear.getText().toString();
                securityNumber = cardSecurityNumber.getText().toString();

                // Create bew card object
                Card card = new Card(name, number, date, securityNumber, mAuth.getUid());
                Log.d(TAG, "onViewCreated: " + card.toString());

                // Send card to database
                FirebaseHelper fbHelper = FirebaseHelper.getInstance();
                fbHelper.addCardToDB(card, task -> {
                    Toast.makeText(getContext(), getString(R.string.card_saved), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Navigation.findNavController(view).popBackStack();
                });
            }
        });
    }
}
