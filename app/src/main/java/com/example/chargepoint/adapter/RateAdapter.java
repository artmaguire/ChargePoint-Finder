package com.example.chargepoint.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.ChargePoint;

import java.util.ArrayList;
import java.util.Map;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateHolder> {

    private View view;
    private ArrayList<ChargePoint> chargePoints;

    public RateAdapter(View view, ArrayList<ChargePoint> chargePoints) {
        this.view = view;
        this.chargePoints = chargePoints;
    }

    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rate, parent, false);
        return new RateHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RateHolder holder, int position) {
        ChargePoint chargePoint = chargePoints.get(position);

        Map<String, String> address = chargePoint.getAddress();

        holder.town.setText(address.get("town"));
        holder.title.setText(address.get("title"));
        holder.line.setText(address.get("line1"));

        String op = chargePoint.isOperational() ? "✅" : "❌";
        holder.isOp.setText("Operational : " + op);

        String fast = chargePoint.isFastC() ? "✅" : "❌";
        holder.isFastC.setText("Fast charge : " + fast);

        holder.cardView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("ChargePoint", chargePoint);
            Navigation.findNavController(view).navigate(R.id.action_navigation_rates_to_fragment_buy_power, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return chargePoints.size();
    }

    static class RateHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView town, title, line, isOp, isFastC;

        RateHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
            town = itemView.findViewById(R.id.town);
            title = itemView.findViewById(R.id.title);
            line = itemView.findViewById(R.id.line);
            isOp = itemView.findViewById(R.id.isOp);
            isFastC = itemView.findViewById(R.id.isFastC);
        }
    }

}
