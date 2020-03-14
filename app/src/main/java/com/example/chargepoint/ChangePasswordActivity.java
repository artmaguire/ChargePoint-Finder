package com.example.chargepoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setTitle("Change Password");

        saveBtn = findViewById(R.id.savePasswordButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO update password address of user in the database
                //TODO add Toast to say password successfully updated
                Intent intent = new Intent(ChangePasswordActivity.this, UpdateInformationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
