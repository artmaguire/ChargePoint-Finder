package com.example.chargepoint.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;

import java.util.Calendar;

public class PaymentDetailsActivity extends AppCompatActivity {
    EditText textcardnumber, textcardname;
    DatePickerDialog datePickerDialog;
    EditText txtMonthYear;
    Button savecardbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        textcardnumber = (EditText) findViewById(R.id.editTextcardnumber);
        textcardname = (EditText) findViewById(R.id.editcardname);

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

        textcardnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textcardname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}