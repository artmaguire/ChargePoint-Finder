package com.example.chargepoint.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chargepoint.R;
import com.example.chargepoint.utils.PreferenceConfiguration;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseUiException;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceConfiguration.updateConfiguration(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (savedInstanceState == null) {
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                showSignInOptions();
            else
                goToMainActivity();
        }
    }

    private void goToMainActivity() {
        findViewById(R.id.splash_screen).setVisibility(View.INVISIBLE);
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showSignInOptions() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setTheme(R.style.FirebaseUITheme).build(), getResources().getInteger(R.integer.FIREBASE_REQUEST_CODE)
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getResources().getInteger(R.integer.FIREBASE_REQUEST_CODE)) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                goToMainActivity();
            } else {
                if (response != null) {
                    FirebaseUiException error = response.getError();
                    if (error != null) {
                        Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    finish();
                }
            }
        }
    }
}
