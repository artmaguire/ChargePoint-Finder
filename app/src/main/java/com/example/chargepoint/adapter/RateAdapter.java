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

        holder.town.setText(rate.getTown());
        holder.title.setText(rate.getTitle());
        holder.line.setText(rate.getLine());

        String op = rate.getIsOp() ? "✔" : "❌";
        holder.isOp.setText(context.getString(R.string.chargepoint_operational, op));

        String fast = rate.getIsFastC() ? "✔" : "❌";
        holder.isFastC.setText((context.getString(R.string.chargepoint_fast_charge, fast)));
    }

    @Override
    public int getItemCount() {
        return rates.size();
    }

    static class RateHolder extends RecyclerView.ViewHolder {

        private TextView town, title, line, isOp, isFastC;

        RateHolder(View itemView) {
            super(itemView);
            town = itemView.findViewById(R.id.town);
            title = itemView.findViewById(R.id.title);
            line = itemView.findViewById(R.id.line);
            isOp = itemView.findViewById(R.id.isOp);
            isFastC = itemView.findViewById(R.id.isFastC);
        }
    }

}
