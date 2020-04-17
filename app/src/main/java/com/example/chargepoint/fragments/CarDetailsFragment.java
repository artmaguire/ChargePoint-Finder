package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Objects;

public class CarDetailsFragment extends Fragment {

    private FirebaseAuth auth;
    private ArrayAdapter arrayAdapter_parent;
    private ArrayList<String> arrayList_Renault, arrayList_tesla, arrayList_volkswagen, arrayList_hyundai, arrayList_mahindra;
    private ArrayAdapter<String> arrayAdapter_child;
    private Button savebutton;
    private Spinner spinnerManufacturer, spinnerModel;
    private ArrayList<String> arrayList_parent;

    public CarDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_details, container, false);

        auth = FirebaseAuth.getInstance();

        savebutton = v.findViewById(R.id.savebutton);
        //Mapping of Dropdown list in the layout page to the Spinner objects
        spinnerManufacturer = v.findViewById(R.id.spinner1);
        spinnerModel = v.findViewById(R.id.spinner2);

        //Adding items of parent spinner "Select Manufacturer"
        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("Renault");
        arrayList_parent.add("Tesla");
        arrayList_parent.add("Volkswagen");
        arrayList_parent.add("Hyundai");
        arrayList_parent.add("Mahindra");

        arrayAdapter_parent = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_blue, arrayList_parent);

        spinnerManufacturer.setAdapter(arrayAdapter_parent);

        //child spinner process start
        arrayList_Renault = new ArrayList<>();
        arrayList_Renault.add("Fluence Z.E.");
        arrayList_Renault.add("Zoe");
        arrayList_Renault.add("Twizy");

        arrayList_tesla = new ArrayList<>();
        arrayList_tesla.add("Model X");
        arrayList_tesla.add("Model 3");
        arrayList_tesla.add("Model S");

        arrayList_volkswagen = new ArrayList<>();
        arrayList_volkswagen.add("e-Golf");
        arrayList_volkswagen.add("e-Up");

        arrayList_hyundai = new ArrayList<>();
        arrayList_hyundai.add("Ioniq Electric");
        arrayList_hyundai.add("Kona Electric");

        arrayList_mahindra = new ArrayList<>();
        arrayList_mahindra.add("e20 Plus");
        arrayList_mahindra.add("e-Verito");

        spinnerManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_red, arrayList_Renault);
                }

                if (position == 1) {
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_red, arrayList_tesla);
                }

                if (position == 2) {
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_red, arrayList_volkswagen);
                }

                if (position == 3) {
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_red, arrayList_hyundai);
                }

                if (position == 4) {
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_red, arrayList_mahindra);
                }
                spinnerModel.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //child process ends

        try {
            savebutton.setOnClickListener(v1 -> savecardetails());
        } catch (NullPointerException ignored) {

        }
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    private void savecardetails() {
        String model = spinnerModel.getSelectedItem().toString().trim();

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest userprofile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(model).build();

            user.updateProfile(userprofile).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Model Type Saved", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
