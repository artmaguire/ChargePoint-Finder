package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.NewsAdapter;

import java.util.ArrayList;

public class NewsFragment extends BackFragment {

    private ArrayList<Items> items = new ArrayList<>();



    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);


       // boolean recycleviewboolean = true;


      RecyclerView recyclerView = v.findViewById(R.id.rv);



        items.add(new Items(R.drawable.newsimagea, getString(R.string.news_title_1), getString(R.string.news_desc_1)));
        items.add(new Items(R.drawable.newsimagebx, getString(R.string.news_title_2), getString(R.string.news_desc_2)));
        items.add(new Items(R.drawable.newsimagec, getString(R.string.news_title_3), getString(R.string.news_desc_3)));
        items.add(new Items(R.drawable.newsimaged, getString(R.string.news_title_4), getString(R.string.news_desc_4)));

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new NewsAdapter(items, getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }
}
