package com.example.chargepoint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chargepoint.adapter.RateAdapter;
import com.example.chargepoint.pojo.Rate;

import java.util.ArrayList;

public class RateActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RateAdapter adapter;
    private ArrayList<Rate> rateArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateArrayList = new ArrayList<>();
        adapter = new RateAdapter(this, rateArrayList);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle("ChargePoint - Rates");

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

    }
}
