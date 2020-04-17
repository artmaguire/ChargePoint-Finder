package com.example.chargepoint.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Calendar;

public class PaymentDetailsActivity extends AppCompatActivity {
    EditText textcardnumber, textcardname;
    DatePickerDialog datePickerDialog;
    EditText txtMonthYear;
    Button savecardbutton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        mAuth = FirebaseAuth.getInstance();
        textcardnumber =  findViewById(R.id.editTextcardnumber);
        textcardname = findViewById(R.id.editcardname);

        txtMonthYear = findViewById(R.id.txtMonthYear);

        //on click functionality to select the month and date of the card
        txtMonthYear.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this,
                    AlertDialog.THEME_HOLO_DARK,
                    (view, year, monthOfYear, dayOfMonth) -> txtMonthYear.setText((monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.show();
        });
        try {
            savecardbutton.setOnClickListener(v -> saveUserInformation());
        }catch(NullPointerException ignored){
            
        }
    }

    public void saveUserInformation() {
        String displayName = textcardname.getText().toString();
        int cardnumber = textcardname.getInputType();
        if(displayName.isEmpty()){
            textcardname.setError("Name Required");
            textcardname.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setDisplayName(String.valueOf(cardnumber))

                    .build();

            user.updateProfile(profile).addOnCompleteListener(
                    task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(PaymentDetailsActivity.this, "Card Details saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        }
    }


}