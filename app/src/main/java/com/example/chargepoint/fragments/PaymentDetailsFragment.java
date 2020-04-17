package com.example.chargepoint.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentDetailsFragment extends Fragment {

    private Button startpayment;
    private EditText orderamount;
    private String TAG = "main";

    DatePickerDialog datePickerDialog;
    EditText txtMonthYear;

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

        txtMonthYear = view.findViewById(R.id.txtMonthYear);

        txtMonthYear.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(),
                    AlertDialog.THEME_HOLO_DARK,
                    (viewD, year, monthOfYear, dayOfMonth) -> txtMonthYear.setText((monthOfYear + 1) + "/" + year), mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
