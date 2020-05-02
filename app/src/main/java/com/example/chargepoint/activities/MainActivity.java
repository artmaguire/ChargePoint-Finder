package com.example.chargepoint.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.chargepoint.R;
import com.example.chargepoint.utils.PreferenceConfiguration;
import com.example.chargepoint.viewmodel.ChargePointViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceConfiguration.updateConfiguration(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onFirst();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        ChargePointViewModel vm = new ViewModelProvider(this).get(ChargePointViewModel.class);
        vm.loadChargePoints();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_map, R.id.navigation_rates, R.id.navigation_profile).build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    private void onFirst() {
        boolean beginRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("beginRun", true);

        if (beginRun) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.title_terms))
                    .setMessage(getString(R.string.see_terms))
                    .setNegativeButton(getString(R.string.decline), (dialog, which) -> {
                        finish();
                        System.exit(0);
                    }).setPositiveButton(R.string.accept, (dialog, which) -> getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("beginRun", false).apply()).show();
        }
    }
}