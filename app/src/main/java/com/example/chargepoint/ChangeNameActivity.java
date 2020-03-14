package com.example.chargepoint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeNameActivity extends AppCompatActivity {
    EditText firstName;
    EditText SecondName;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        saveBtn = findViewById(R.id.saveNameButon);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Get Name and update in Firestore database
                //TODO add Toast to say name successfully updated
                finish();
            }
        });
    }
}
