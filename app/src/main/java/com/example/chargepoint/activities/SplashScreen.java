package com.example.chargepoint.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chargepoint.R;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);

      progressBar = findViewById(R.id.progressbar);
      textView = findViewById(R.id.text_view);

      progressBar.setMax(100);
      progressBar.setScaleY(3f);

      progressAnimation();
    }

    public void progressAnimation(){

        ProgressBarAnimation amin = new ProgressBarAnimation(this,progressBar,textView,0f,100f);
        amin.setDuration(8000);
        progressBar.setAnimation(amin);
    }
}
