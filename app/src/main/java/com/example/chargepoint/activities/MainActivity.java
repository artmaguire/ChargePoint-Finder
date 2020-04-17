package com.example.chargepoint.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.chargepoint.R;
import com.example.chargepoint.fragments.TermsFragment;
import com.example.chargepoint.map.MapViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("Dark Theme", false);
        if (enabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        onFirst();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        MapViewModel mvm = ViewModelProviders.of(this).get(MapViewModel.class);
        mvm.loadChargePoints();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_rates, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


    // <------------------------------------------------------------------------------------>


    public void alert(View v){
    }

    public void onFirst(){
        boolean beginRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("beginRun", true);

        if (beginRun){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Terms&Condition")
                    .setMessage("Click on link below to see terms and conditions")
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //FragmentManager fm = getSupportFragmentManager();
                            //TermsFragment callfragment;
                            //fm.beginTransaction().replace(R.id.container, callfragment).commit();
                            finish();
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .edit()
                            .putBoolean("beginRun", false)
                            .apply();
                        }
                    }).show();
        }
    }
}
