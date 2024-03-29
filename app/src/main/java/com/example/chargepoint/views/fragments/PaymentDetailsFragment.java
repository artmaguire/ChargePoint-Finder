package com.example.chargepoint.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Card;
import com.example.chargepoint.utils.PreferenceConfiguration;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Michael
 * User can edit a card in the database and add new card
 * DB implementation by Art
 */
public class PaymentDetailsFragment extends BackFragment implements TextWatcher {

    private String TAG = "PAYMENT_DETAILS";

    private EditText cardName;
    private EditText cardNumber;
    private EditText cardSecurityNumber;
    private EditText txtMonthYear;
    private String selectedDate;
    private boolean newCard = true;
    private Card cardSelected;
    private Button saveCardButton;

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

        cardName = view.findViewById(R.id.cardName);
        cardNumber = view.findViewById(R.id.cardNumber);
        txtMonthYear = view.findViewById(R.id.expiryDate);
        cardSecurityNumber = view.findViewById(R.id.securityNumber);
        saveCardButton = view.findViewById(R.id.saverbutton);

        if (cardName.getText().toString().equals("") || cardNumber.getText().toString().equals("") || cardSecurityNumber.getText()
                .toString()
                .equals("") || txtMonthYear.getText().toString().equals("")) {
            if (!cardName.getText().toString().matches("\\w+\\.?"))
                saveCardButton.setEnabled(false);
        }

        Bundle b = getArguments();

        if (b != null) {
            newCard = b.getBoolean("New Card", true);
            Log.d(TAG, "onViewCreated: " + newCard);
            if (!newCard) {
                cardSelected = b.getParcelable("Card");
                setCard(cardSelected);
                saveCardButton.setText(R.string.update_card);
            }
        } else {
            Log.d(TAG, "onViewCreated: No Card Bundle");
        }

        //on click functionality to select the month and date of the card
        txtMonthYear.setOnClickListener(y -> getMonth());
        cardName.addTextChangedListener(this);
        cardNumber.addTextChangedListener(this);
        txtMonthYear.addTextChangedListener(this);
        cardSecurityNumber.addTextChangedListener(this);

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
                Toast.makeText(getContext(), getString(R.string.missing_information), Toast.LENGTH_SHORT).show();
            } else if (cardNumber.getText().length() < 16) {
                Toast.makeText(getContext(), getString(R.string.card_num_not_long_enough), Toast.LENGTH_SHORT).show();
            } else {
                if (newCard) {
                    saveCardButton.setEnabled(true);
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getString(R.string.adding_card), true);
                    name = cardName.getText().toString();
                    number = cardNumber.getText().toString();
                    date = selectedDate;
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
                } else {
                    saveCardButton.setEnabled(true);
                    ProgressDialog dialog = ProgressDialog.show(getActivity(), "", getString(R.string.updating), true);
                    name = cardName.getText().toString();
                    number = cardNumber.getText().toString();
                    if (selectedDate == null) {
                        date = cardSelected.getCardDate();
                    } else
                        date = selectedDate;

                    securityNumber = cardSecurityNumber.getText().toString();

                    // Create bew card object
                    Card card = new Card(name, number, date, securityNumber, mAuth.getUid());

                    if (card.equals(cardSelected)) {
                        Toast.makeText(getContext(), R.string.identical_cards, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        return;
                    }

                    Log.d(TAG, "onViewCreated: " + card.toString());

                    // Send card to database
                    FirebaseHelper fbHelper = FirebaseHelper.getInstance();
                    fbHelper.updateCard(cardSelected, card, task -> {
                        Toast.makeText(getContext(), getString(R.string.card_saved), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Navigation.findNavController(view).popBackStack();
                    });
                }
            }
        });
    }

    private void getMonth() {
        int yearSelected;
        int monthSelected;

        //Set default values
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);

        // Use the calendar for create ranges
        Calendar calendar2 = Calendar.getInstance();

        // Min Range
        calendar2.clear();
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        calendar2.set(currYear, 0, 1); // Set minimum date to show in dialog
        long minDate = calendar2.getTimeInMillis(); // Get milliseconds of the modified date

        // Max Range
        calendar2.clear();
        calendar2.set(currYear + 10, 5, 4); // Set maximum date to show in dialog
        long maxDate = calendar2.getTimeInMillis(); // Get milliseconds of the modified date

        Locale locale = PreferenceConfiguration.getCurrentLocale(requireContext());

        MonthYearPickerDialogFragment dialogFragment;

        MonthFormat monthFormat = MonthFormat.SHORT; //MonthFormat.LONG or MonthFormat.SHORT
        //Simple way
        dialogFragment = MonthYearPickerDialogFragment.getInstance(monthSelected, yearSelected, minDate, maxDate, getString(R.string.select_date), locale, monthFormat);

        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            if (monthOfYear < 10)
                selectedDate = "0" + monthSelected;
            else
                selectedDate = String.valueOf(monthOfYear);

            selectedDate += "/";
            selectedDate += String.valueOf(year).substring(2);
            txtMonthYear.setText(selectedDate);
        });

        dialogFragment.show(requireActivity().getSupportFragmentManager(), null);
    }

    private void setCard(Card card) {
        cardName.setText(card.getCardName());
        cardNumber.setText(card.getCardNumber());
        txtMonthYear.setText(card.getCardDate());
        cardSecurityNumber.setText(card.getCardSecurityNumber());
    }

    private void changeButtonState() {
        if (!cardName.getText().toString().equals("") && cardNumber.getText().length() == 16 && cardSecurityNumber.getText()
                .length() == 3 && !txtMonthYear.getText().toString().equals("")) {
            saveCardButton.setEnabled(true);
        } else {
            saveCardButton.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        changeButtonState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
