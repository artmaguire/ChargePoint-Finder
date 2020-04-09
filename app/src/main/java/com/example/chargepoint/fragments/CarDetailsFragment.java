package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.chargepoint.R;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarDetailsFragment extends Fragment {

    Spinner spinnerManufacturer, spinnerModel   ;

    ArrayList<String> arrayList_parent;
    ArrayAdapter arrayAdapter_parent;

    ArrayList<String> arrayList_Renault,arrayList_tesla,arrayList_volkswagen,arrayList_hyundai,arrayList_mahindra ;
    ArrayAdapter<String> arrayAdapter_child;
    public CarDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v = inflater.inflate(R.layout.fragment_car_details, container, false);

        //Mapping of Dropdown list in the layout page to the Spinner objects
        spinnerManufacturer = (Spinner) v.findViewById(R.id.spinner1);
        spinnerModel = (Spinner) v.findViewById(R.id.spinner2);

        //Adding items of parent spinner "Select Manufacturer"
        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("Renault");
        arrayList_parent.add("Tesla");
        arrayList_parent.add("Volkswagen");
        arrayList_parent.add("Hyundai");
        arrayList_parent.add("Mahindra");

        arrayAdapter_parent = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_parent);

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

                if(position == 0){
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_Renault);
                }

                if(position == 1){
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_tesla);
                }

                if(position == 2){
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_volkswagen);

                }

                if(position == 3){
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_hyundai);

                }

                if(position == 4){
                    arrayAdapter_child = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList_mahindra);
                }

                spinnerModel.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //child process ends
        return v;



    }
}
