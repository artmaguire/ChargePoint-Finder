package com.example.chargepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeEmailActivity extends AppCompatActivity {
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        setTitle("Change Email");

        saveBtn = findViewById(R.id.saveEmailButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO update email address of user in the database
                //TODO add Toast to say email successfully updated
                Intent intent = new Intent(ChangeEmailActivity.this, UpdateInformationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
