package com.example.chargepoint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateInformationActivity extends AppCompatActivity {

    private static String TAG = "User";

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Button backBtn;
    CardView nameCard;
    CardView emailCard;
    CardView passwordCard;
    LinearLayout nameLayout;
    LinearLayout emailLayout;
    LinearLayout passwordLayout;
    TextView name;
    TextView email;
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        setTitle("Update Information");

        updateUserInfo();

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

        nameCard = findViewById(R.id.NameCard);
        nameLayout = findViewById(nameCard.getChildAt(0).getId());
        name = findViewById(nameLayout.getChildAt(1).getId());
        name.setHint(currentUser.getDisplayName());

        alertDialog.setTitle("Change Name");

        final EditText input = new EditText(this);

        input.setHint(currentUser.getDisplayName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(input.getText().toString())
                                .build();

                        currentUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User Name updated.");

                                            Toast.makeText(getApplicationContext(),
                                                    "Name updated", Toast.LENGTH_SHORT)
                                                    .show();

                                            updateUserInfo();
                                        }
                                    }
                                });
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        alertDialog.show();
    }

    public void changeEmail(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateInformationActivity.this);

        emailCard = findViewById(R.id.EmailCard);
        emailLayout = findViewById(emailCard.getChildAt(0).getId());
        email = findViewById(emailLayout.getChildAt(1).getId());
        name.setHint(currentUser.getEmail());

        alertDialog.setTitle("Change Email");

        final EditText input = new EditText(this);

        input.setHint(currentUser.getEmail());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String emailAddress = input.getText().toString();

                        currentUser.updateEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User email address updated.");
                                            updateUserInfo();
                                        }
                                    }
                                });
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        alertDialog.show();
    }

    public void changePassword(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateInformationActivity.this);

        passwordCard = findViewById(R.id.PasswordCard);
        passwordLayout = findViewById(passwordCard.getChildAt(0).getId());
        password = findViewById(passwordLayout.getChildAt(1).getId());

        alertDialog.setTitle("Change Password");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        password.setText(input.getText().toString());

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String newPassword = input.getText().toString();

                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                            updateUserInfo();
                                        }
                                    }
                                });
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        alertDialog.show();
    }

    private void updateUserInfo() {
        nameCard = findViewById(R.id.NameCard);
        nameLayout = findViewById(nameCard.getChildAt(0).getId());
        name = findViewById(nameLayout.getChildAt(1).getId());
        name.setHint(currentUser.getDisplayName());

        emailCard = findViewById(R.id.EmailCard);
        emailLayout = findViewById(emailCard.getChildAt(0).getId());
        email = findViewById(emailLayout.getChildAt(1).getId());
        email.setHint(currentUser.getEmail());
    }
}
