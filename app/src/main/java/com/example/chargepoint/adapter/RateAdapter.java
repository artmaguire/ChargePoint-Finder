package com.example.chargepoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Rate;

import java.util.ArrayList;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateHolder> {

    private Context context;
    private ArrayList<Rate> rates;

    public RateAdapter(Context context, ArrayList<Rate> rates) {
        this.context = context;
        this.rates = rates;
    }

    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_rate, parent, false);
        return new RateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateHolder holder, int position) {
        Rate rate = rates.get(position);
        holder.setDetails(rate);
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }


    class RateHolder extends RecyclerView.ViewHolder {

        private TextView txtName, a, b, c;

        RateHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            a = itemView.findViewById(R.id.txtA);
            b = itemView.findViewById(R.id.txtB);
            c = itemView.findViewById(R.id.txtC);
        }

        void setDetails(Rate rate) {
            txtName.setText(rate.getRateName());
            a.setText(rate.getRateA());
            b.setText(rate.getRateB());
            c.setText(rate.getRateC());
        }
    }



}
