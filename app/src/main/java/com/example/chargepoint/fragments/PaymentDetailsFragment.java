package com.example.chargepoint.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chargepoint.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Calendar;

public class PaymentDetailsFragment extends BackFragment {

    private EditText textcardnumber, textcardname;
    private DatePickerDialog datePickerDialog;
    private EditText txtMonthYear;
    private Button savecardbutton;
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
        textcardnumber = view.findViewById(R.id.editTextcardnumber);
        textcardname = view.findViewById(R.id.editcardname);
        savecardbutton = view.findViewById(R.id.saverbutton);
        txtMonthYear = view.findViewById(R.id.txtMonthYear);

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

        try {
            savecardbutton.setOnClickListener(v -> saveUserInformation());
        } catch (NullPointerException ignored) {

        }
    }

    private void saveUserInformation() {
        String displayName = textcardname.getText().toString();
        int cardnumber = textcardname.getInputType();
        if (displayName.isEmpty()) {
            textcardname.setError("Name Required");
            textcardname.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setDisplayName(String.valueOf(cardnumber))

                    .build();

            user.updateProfile(profile).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Card Details saved successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
