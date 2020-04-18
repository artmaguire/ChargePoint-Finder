package com.example.chargepoint.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Car;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CarDetailsFragment extends Fragment {

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
        this.setHasOptionsMenu(true);

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

        // Adding items of parent spinner "Select Manufacturer"
        manufacturerList = new ArrayList<>();
        manufacturerList.add("Renault");
        manufacturerList.add("Tesla");
        manufacturerList.add("Volkswagen");
        manufacturerList.add("Hyundai");
        manufacturerList.add("Mahindra");

        ArrayAdapter arrayAdapter_parent = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, manufacturerList);
        manufacturerSpinner.setAdapter(arrayAdapter_parent);

        // Renault Models
        renaultModels = new ArrayList<>();
        renaultModels.add("Fluence Z.E.");
        renaultModels.add("Zoe");
        renaultModels.add("Twizy");

        // Tesla Models
        teslaModels = new ArrayList<>();
        teslaModels.add("Model X");
        teslaModels.add("Model 3");
        teslaModels.add("Model S");

        // Volkswagen Models
        volkswagenModels = new ArrayList<>();
        volkswagenModels.add("e-Golf");
        volkswagenModels.add("e-Up");

        // Hyundai Models
        hyundaiModels = new ArrayList<>();
        hyundaiModels.add("Ioniq Electric");
        hyundaiModels.add("Kona Electric");

        // Mahindra Models
        mahindraModels = new ArrayList<>();
        mahindraModels.add("e20 Plus");
        mahindraModels.add("e-Verito");

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
}
