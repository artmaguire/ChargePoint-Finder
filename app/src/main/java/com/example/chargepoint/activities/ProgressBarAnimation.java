package com.example.chargepoint.activities;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {
    private SplashScreen activity;
    private ProgressBar progressBar;
    private TextView textView;
    private float from;
    private float to;

    public ProgressBarAnimation(SplashScreen activity, ProgressBar progressBar, TextView textView, float from, float to) {

        this.activity = activity;
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int) value + " %");

        if (value == to) {
            activity.next();
        }
    }
}
