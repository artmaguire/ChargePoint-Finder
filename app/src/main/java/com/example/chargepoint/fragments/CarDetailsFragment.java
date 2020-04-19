package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Car;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CarDetailsFragment extends BackFragment {

    private String TAG = "CAR_DETAILS";
    private ArrayList<String> renaultModels, teslaModels, volkswagenModels, hyundaiModels, mahindraModels;
    private ArrayAdapter<String> modelAdapter;
    private Spinner modelSpinner;
    private ArrayList<String> manufacturerList;
    private String manufacturer;
    private String model;

    public CarDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button saveCar = view.findViewById(R.id.savebutton);

        manufacturer = "";
        model = "";

        // Mapping of Dropdown list in the layout page to the Spinner objects
        Spinner manufacturerSpinner = view.findViewById(R.id.spinner1);
        modelSpinner = view.findViewById(R.id.spinner2);

        //Adding items of parent spinner "Select Manufacturer"
        String[] car_brands = getResources().getStringArray(R.array.car_brands);
        manufacturerList = new ArrayList<>(Arrays.asList(car_brands));

        ArrayAdapter arrayAdapter_parent = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, manufacturerList);
        manufacturerSpinner.setAdapter(arrayAdapter_parent);

        //child spinner process start
        String[] renault_models = getResources().getStringArray(R.array.renault_models);
        renaultModels = new ArrayList<>(Arrays.asList(renault_models));

        String[] tesla_models = getResources().getStringArray(R.array.tesla_models);
        teslaModels = new ArrayList<>(Arrays.asList(tesla_models));

        String[] volkswagen_models = getResources().getStringArray(R.array.volkswagen_models);
        volkswagenModels = new ArrayList<>(Arrays.asList(volkswagen_models));

        String[] hyundai_models = getResources().getStringArray(R.array.hyundai_models);
        hyundaiModels = new ArrayList<>(Arrays.asList(hyundai_models));

        String[] mahindra_models = getResources().getStringArray(R.array.mahindra_models);
        mahindraModels = new ArrayList<>(Arrays.asList(mahindra_models));

        manufacturerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, renaultModels);
                        manufacturer = manufacturerList.get(0);
                        break;
                    case 1:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, teslaModels);
                        manufacturer = manufacturerList.get(1);
                        break;
                    case 2:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, volkswagenModels);
                        manufacturer = manufacturerList.get(2);
                        break;
                    case 3:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, hyundaiModels);
                        manufacturer = manufacturerList.get(3);
                        break;
                    case 4:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mahindraModels);
                        manufacturer = manufacturerList.get(4);
                        break;
                    case 5:
                        modelAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, renaultModels);
                        manufacturer = manufacturerList.get(5);
                        break;
                }

                modelSpinner.setAdapter(modelAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (manufacturer) {
                    case "Renault":
                        model = renaultModels.get(position);
                        break;
                    case "Tesla":
                        model = teslaModels.get(position);
                        break;
                    case "Volkswagen":
                        model = volkswagenModels.get(position);
                        break;
                    case "Hyundai":
                        model = hyundaiModels.get(position);
                        break;
                    case "Mahindra":
                        model = mahindraModels.get(position);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveCar.setOnClickListener(v -> {
            ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Adding Car...", true);
            Car c = new Car(manufacturer, model, FirebaseAuth.getInstance().getUid());
            FirebaseHelper.getInstance().addCarToDB(c, listener -> {
                Log.d(TAG, "onViewCreated: Car Added to dB.");
                Toast.makeText(getContext(), "Car Added Successfully.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Navigation.findNavController(view).popBackStack();
            });
        });
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
