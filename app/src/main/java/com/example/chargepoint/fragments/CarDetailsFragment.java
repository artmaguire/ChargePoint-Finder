package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chargepoint.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CarDetailsFragment extends BackFragment {

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_car_details, container, false);

        auth = FirebaseAuth.getInstance();

        savebutton = v.findViewById(R.id.savebutton);
        //Mapping of Dropdown list in the layout page to the Spinner objects
        spinnerManufacturer = v.findViewById(R.id.spinner1);
        spinnerModel = v.findViewById(R.id.spinner2);

        //Adding items of parent spinner "Select Manufacturer"
        String[] car_brands = getResources().getStringArray(R.array.car_brands);
        arrayList_parent = new ArrayList<>(Arrays.asList(car_brands));

        arrayAdapter_parent = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.textview_blue, arrayList_parent);

        spinnerManufacturer.setAdapter(arrayAdapter_parent);

        //child spinner process start
        String[] renault_models = getResources().getStringArray(R.array.renault_models);
        arrayList_Renault = new ArrayList<>(Arrays.asList(renault_models));

        String[] tesla_models = getResources().getStringArray(R.array.tesla_models);
        arrayList_tesla = new ArrayList<>(Arrays.asList(tesla_models));

        String[] volkswagen_models = getResources().getStringArray(R.array.volkswagen_models);
        arrayList_volkswagen = new ArrayList<>(Arrays.asList(volkswagen_models));

        String[] hyundai_models = getResources().getStringArray(R.array.hyundai_models);
        arrayList_hyundai = new ArrayList<>(Arrays.asList(hyundai_models));

        String[] mahindra_models = getResources().getStringArray(R.array.mahindra_models);
        arrayList_mahindra = new ArrayList<>(Arrays.asList(mahindra_models));

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

    private void savecardetails() {
        String model = spinnerModel.getSelectedItem().toString().trim();

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest userprofile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(model).build();

            user.updateProfile(userprofile).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), getString(R.string.model_type_saved), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
