package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInformationFragment extends Fragment {

    private static String TAG = "USER";

    private FirebaseUser currentUser;
    private TextView name;
    private TextView email;

    public UpdateInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
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

        alertDialog.setTitle("Change Name");

        final EditText input = new EditText(getContext());

        input.setHint(currentUser.getDisplayName());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                (dialog, which) -> {
                    ProgressDialog pg = ProgressDialog.show(getContext(), "",
                            "Updating. Please wait...", true);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(input.getText().toString())
                            .build();

                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User Name updated.");

                                    Toast.makeText(getContext(),
                                            "Name updated", Toast.LENGTH_SHORT)
                                            .show();

                                    updateUserInfo();
                                } else {
                                    Toast.makeText(getContext(),
                                            "Failed to update name.", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                pg.dismiss();
                            });
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back", null);

        alertDialog.show();
    }

    private void changeEmail(View v) {
        email.setHint(currentUser.getEmail());

        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle("Change Email");

        final EditText input = new EditText(getContext());

        input.setHint(currentUser.getEmail());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                (dialog, which) -> {
                    ProgressDialog pg = ProgressDialog.show(getContext(), "",
                            "Updating. Please wait...", true);
                    String emailAddress = input.getText().toString();

                    currentUser.updateEmail(emailAddress)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                    updateUserInfo();
                                } else {
                                    Toast.makeText(getContext(),
                                            "Failed to update name.", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                pg.dismiss();
                            });
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back", null);

        alertDialog.show();
    }

    private void changePassword(View v) {
        MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getContext());

        alertDialog.setTitle("Change Password");

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(input);

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("Save",
                (dialog, which) -> {
                    ProgressDialog pg = ProgressDialog.show(getContext(), "",
                            "Updating. Please wait...", true);
                    //password.setText(input.getText().toString());

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = input.getText().toString();

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    updateUserInfo();
                                } else {
                                    Toast.makeText(getContext(),
                                            "Failed to update name.", Toast.LENGTH_SHORT)
                                            .show();
                                }
                                pg.dismiss();
                            });
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("Back", null);

        alertDialog.show();
    }

    private void updateUserInfo() {
        name.setHint(currentUser.getDisplayName());
        email.setHint(currentUser.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
