package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.chargepoint.activities.SplashScreen;
import com.example.chargepoint.db.FirebaseHelper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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

    private void changeName(View v) {
        name.setHint(currentUser.getDisplayName());
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle(getString(R.string.change_name));

        final EditText input = new EditText(getContext());

        input.setHint(currentUser.getDisplayName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton(getString(R.string.save),
                (dialog, which) -> {
                    ProgressDialog pg = ProgressDialog.show(getContext(), "",
                            getString(R.string.updating), true);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(input.getText().toString())
                            .build();

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

        input.setHint(currentUser.getEmail());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton(getString(R.string.save), (dialog, which) -> {
            ProgressDialog pg = ProgressDialog.show(getContext(), "", getString(R.string.updating), true);
            String emailAddress = input.getText().toString();

            currentUser.updateEmail(emailAddress).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.user_email_changed), Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                    // TODO: refresh -> Refresh Token
                    signOut();
                } else {
                    Toast.makeText(getContext(), getString(R.string.user_email_change_fail), Toast.LENGTH_SHORT).show();
                }
                pg.dismiss();
            });
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
            ProgressDialog pg = ProgressDialog.show(getContext(), "", getString(R.string.updating), true);
            //password.setText(input.getText().toString());

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = input.getText().toString();

            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), getString(R.string.user_password_changed), Toast.LENGTH_SHORT).show();
                    updateUserInfo();
                    // TODO: refresh -> Refresh Token
                    signOut();
                } else {
                    Toast.makeText(getContext(), getString(R.string.user_password_change_fail), Toast.LENGTH_SHORT).show();
                }
                pg.dismiss();
            });
        });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton(getString(R.string.back), null);

        alertDialog.show();
    }

    private void updateUserInfo() {
        name.setHint(currentUser.getDisplayName());
        email.setHint(currentUser.getEmail());
    }

    private void signOut() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(task -> {
            FirebaseHelper.destroyInstance();
            Intent i = new Intent(getActivity(), SplashScreen.class);
            startActivity(i);
            getActivity().finish();
        }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
