package com.example.chargepoint.views.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chargepoint.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by Art
 * User can update their name, email and password
 */
public class UpdateInformationFragment extends BackFragment {

    private final static String TAG = "USER";

    private FirebaseUser currentUser;
    private TextView name;
    private TextView email;

    public UpdateInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = view.findViewById(R.id.usersName);
        email = view.findViewById(R.id.userEmail);

        view.findViewById(R.id.NameCard).setOnClickListener(this::changeName);
        view.findViewById(R.id.EmailCard).setOnClickListener(this::changeEmail);
        view.findViewById(R.id.PasswordCard).setOnClickListener(this::changePassword);

        updateUserInfo();
    }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }

        return result;
    }

    private void changeName(View v) {
        name.setHint(currentUser.getDisplayName());
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle(getString(R.string.change_name));

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            if (input.getText().toString().isEmpty())
                return;

            ProgressDialog pg = ProgressDialog.show(getContext(), "", getString(R.string.updating), true);
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(input.getText().toString()).build();

            currentUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.user_name_changed), Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                } else {
                    Toast.makeText(getContext(), getString(R.string.user_name_change_fail), Toast.LENGTH_SHORT).show();
                }
                pg.dismiss();
            });
        });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton(R.string.back, null);

        alertDialog.show();
    }

    private void changeEmail(View v) {
        email.setHint(currentUser.getEmail());

        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle(getString(R.string.user_change_email));

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            if (input.getText().toString().isEmpty())
                return;
            String emailAddress = input.getText().toString();

            if (isValidEmailAddress(emailAddress))
                reAuthenticateEmail(emailAddress);
            else
                Toast.makeText(getContext(), getString(R.string.user_incorrect_email_format), Toast.LENGTH_SHORT).show();
        });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton(getString(R.string.back), null);

        alertDialog.show();
    }

    private void changePassword(View v) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle(getString(R.string.user_change_password));

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            if (input.getText().toString().isEmpty())
                return;

            String password = input.getText().toString();
            reAuthenticatePassword(password);
        });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton(getString(R.string.back), null);

        alertDialog.show();
    }

    private void reAuthenticateEmail(String emailAddress) {
        ProgressDialog pg = ProgressDialog.show(getContext(), "", getString(R.string.updating), true);
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), "pass1234");
        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail(emailAddress).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    updateUserInfo();
                    email.setHint(emailAddress);
                    Toast.makeText(getContext(), getString(R.string.user_email_changed), Toast.LENGTH_SHORT).show();
                }

                pg.dismiss();
            });
        });
    }

    private void reAuthenticatePassword(String password) {
        ProgressDialog pg = ProgressDialog.show(getContext(), "", getString(R.string.updating), true);
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), "pass1234");
        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(password).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.user_password_changed), Toast.LENGTH_SHORT).show();
                }

                pg.dismiss();
            });
        });
    }

    private void updateUserInfo() {
        name.setHint(currentUser.getDisplayName());
        email.setHint(currentUser.getEmail());
    }
}
