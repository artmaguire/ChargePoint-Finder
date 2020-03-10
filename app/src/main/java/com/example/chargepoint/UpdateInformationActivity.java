package com.example.chargepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateInformationActivity extends AppCompatActivity {
    Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        setTitle("Update Information");

        backBtn = findViewById(R.id.backToUpdateInformation);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void changeName(View v) {
        Intent intent = new Intent(UpdateInformationActivity.this, ChangeNameActivity.class);
        startActivity(intent);
    }

    public void changeEmail(View v) {
        Intent intent = new Intent(UpdateInformationActivity.this, ChangeEmailActivity.class);
        startActivity(intent);
    }

    public void changePassword(View v) {
        Intent intent = new Intent(UpdateInformationActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}
