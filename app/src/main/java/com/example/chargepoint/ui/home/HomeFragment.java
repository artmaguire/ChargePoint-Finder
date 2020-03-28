package com.example.chargepoint.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final int MY_REQUEST_CODE = 1234;
    List<AuthUI.IdpConfig> providers;

    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = root.getContext();

        showSignInOptions();

        // Dashboard Navigation by navGraph
        root.findViewById(R.id.aboutCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_about);
        });

        root.findViewById(R.id.newsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_news);
        });

        root.findViewById(R.id.termsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_terms);
        });

        root.findViewById(R.id.carDetailsCard).setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_fragment_car_details);
        });

        return root;
    }

    private void showSignInOptions() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Theme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(context, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}
