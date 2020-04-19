package com.example.chargepoint.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.example.chargepoint.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class SplashScreen extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1234;
    private static final String TAG = "SPLASH_SCREEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("Dark Theme", false);
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        next();
    }

    public void next() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(TAG, "onActivityResult: log me in boi");
            showSignInOptions();
        }
        else
            goToMainActivity();
    }

    private void goToMainActivity() {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showSignInOptions() {
        Log.d(TAG, "onActivityResult: gimme options boi");
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
        Log.d(TAG, "onActivityResult: yes boi");
        if (requestCode == MY_REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: Dank Requesty boi");
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Dank ass result");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();

                //                addUserToDb();

                Log.d(TAG, "onActivityResult: Going to Main Activity");
                goToMainActivity();
            } else {
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
