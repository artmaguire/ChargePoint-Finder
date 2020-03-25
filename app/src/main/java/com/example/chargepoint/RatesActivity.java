package com.example.chargepoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.chargepoint.adapter.RateAdapter;
import com.example.chargepoint.pojo.Rate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RatesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RateAdapter adapter;
    private ArrayList<Rate> rateArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateArrayList = new ArrayList<>();
        adapter = new RateAdapter(this, rateArrayList);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle("ChargePoint - Rates");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("rates")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " - " + document.getData());
                                Rate rate = new Rate(document.getString("name"), document.getString("a"), document.getString("b"), document.getString("c"));
                                rateArrayList.add(rate);
                            }
                        }
                    }
                });


        Rate rate = new Rate("Test", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test1", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test2", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test3", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test4", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test5", "a", "b", "c");
        rateArrayList.add(rate);
        rate = new Rate("Test6", "a", "b", "c");
        rateArrayList.add(rate);

        adapter.notifyDataSetChanged();

        //Initialise and assign variable
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);

        //set home selected
        navigation.setSelectedItemId(R.id.rates);

        //perform itemselectedlistener

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.maps:
                        startActivity(new Intent(getApplicationContext()
                                ,MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext()
                                ,ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rates:
                        return true;
                }
                return false;
            }
        });
    }
}
