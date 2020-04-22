package com.example.chargepoint.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class SplashScreen extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1234;
    private static final String TAG = "User_db";

    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();
    }

        /*new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                showSignInOptions();
            else
                goToMainActivity();
        }, 500);
    }*/

    public void next() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            showSignInOptions();
        else
            goToMainActivity();
    }

    private void goToMainActivity() {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void progressAnimation() {
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, textView, 0f, 100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
    }

    private void showSignInOptions() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.FirebaseUITheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();

                addUserToDb();

                goToMainActivity();
            } else {
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addUserToDb() {
        // Check if user is already in Users table, if not add to table
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore.collection("users").document(currentFirebaseUser.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: User already exists");
                    } else {
                        // Add user data to User Table
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("uid", currentFirebaseUser.getUid());
                        userData.put("name", currentFirebaseUser.getDisplayName());
                        userData.put("email", currentFirebaseUser.getEmail());

                        firebaseFirestore.collection("users")
                                .add(userData)
                                .addOnSuccessListener(documentReference ->
                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                                .addOnFailureListener(e ->
                                        Log.w(TAG, "Error adding document", e));
                    }
                });
    }

}
