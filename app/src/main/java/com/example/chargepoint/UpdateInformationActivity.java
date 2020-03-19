package com.example.chargepoint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UpdateInformationActivity extends AppCompatActivity {
    Button backBtn;
    CardView card;
    LinearLayout nameLayout;
    LinearLayout emailLayout;
    LinearLayout passwordLayout;
    TextView username;
    TextView email;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        setTitle("Update Information");

        backBtn = findViewById(R.id.backToUpdateInformation);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateInformationActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void changeName(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateInformationActivity.this);

        card = findViewById(R.id.NameCard);
        nameLayout = findViewById(card.getChildAt(0).getId());
        username = findViewById(nameLayout.getChildAt(1).getId());

        // Setting Dialog Title
        alertDialog.setTitle("Change Name");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //TODO: Add user's name here
        input.setHint(R.string.users_name);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);


        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        username.setText(input.getText().toString());

                        //TODO: Add floating action button
                        Toast.makeText(getApplicationContext(),
                                "Name updated", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
//        Intent intent = new Intent(UpdateInformationActivity.this, ChangeNameActivity.class);
//        startActivity(intent);
    }

    public void changeEmail(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateInformationActivity.this);

        card = findViewById(R.id.EmailCard);
        nameLayout = findViewById(card.getChildAt(0).getId());
        username = findViewById(nameLayout.getChildAt(1).getId());

        // Setting Dialog Title
        alertDialog.setTitle("Change Name");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //TODO: Add user's name here
//        input.setHint(R.string.);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);


        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        username.setText(input.getText().toString());

                        //TODO: Add floating action button
                        Toast.makeText(getApplicationContext(),
                                "Name updated", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
//        Intent intent = new Intent(UpdateInformationActivity.this, ChangeNameActivity.class);
//        startActivity(intent);
    }

    public void changePassword(View v) {
        Intent intent = new Intent(UpdateInformationActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}
