package com.example.chargepoint.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.ChargePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Art
 */
public class ChargePointAdapter extends RecyclerView.Adapter<ChargePointAdapter.ChargePointHolder> implements Filterable {

    private List<ChargePoint> chargePoints;
    private List<ChargePoint> chargePointSearchOriginal;
    private View root;
    private Filter filter = new Filter() {
        @Override
        public FilterResults performFiltering(CharSequence constraint) {
            List<ChargePoint> filteredList = new ArrayList<>();
            Log.d("TAG", "performFiltering: " + constraint.toString());

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(chargePointSearchOriginal);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ChargePoint cp : chargePointSearchOriginal) {
                    if (cp.getAddress().get("title").toLowerCase().contains(filterPattern) || cp.getAddress().get("line1").toLowerCase().contains(filterPattern) || cp.getAddress().get("town").toLowerCase().contains(filterPattern)) {
                        Log.d("TAG", "performFiltering: dhjsjdghsdjghasjdh sdjghsdgashdg");
                        filteredList.add(cp);
                        Log.d("TAG", "performFiltering: " + filteredList.toString());
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            chargePoints = (ArrayList<ChargePoint>) results.values;
            notifyDataSetChanged();
        }
    };

    public ChargePointAdapter(View root) {
        this.root = root;
        this.chargePoints = new ArrayList<>();
    }

    public void setChargePoints(List<ChargePoint> chargePoints) {
        this.chargePoints = chargePoints;
        this.chargePointSearchOriginal = new ArrayList<>(chargePoints);
    }

    @NonNull
    @Override
    public ChargePointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chargepoint, parent, false);

        return new ChargePointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChargePointHolder holder, int position) {
        ChargePoint chargePoint = chargePoints.get(position);

        holder.town.setText(chargePoint.getAddress().get("title"));
        if (chargePoint.getAddress().get("town").isEmpty())
            holder.title.setText(chargePoint.getAddress().get("title"));
        else
            holder.title.setText(chargePoint.getAddress().get("town").substring(0, 1).toUpperCase() + chargePoint.getAddress().get("town").substring(1));
        holder.line.setText(chargePoint.getAddress().get("line1"));

        String operator = chargePoint.getOperator();
        holder.operator.setText(operator);

        holder.cv.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putSerializable("ChargePoint", chargePoint);
            Navigation.findNavController(root).navigate(R.id.action_navigation_rates_to_fragment_buy_power, b);
        });
    }

    @Override
    public int getItemCount() {
        return chargePoints.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    static class ChargePointHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView town, title, line, operator;

        ChargePointHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.chargePointCardView);
            town = itemView.findViewById(R.id.town);
            title = itemView.findViewById(R.id.title);
            line = itemView.findViewById(R.id.line);
            operator = itemView.findViewById(R.id.operator);
        }
    }

    public void reset() {
        chargePoints = chargePointSearchOriginal;
        notifyDataSetChanged();
    }

    // TODO: filter by county
    public void filterChargePointsByCounty(String county) {
        List<ChargePoint> cpCounty = new ArrayList<>();

        for (ChargePoint cp : chargePointSearchOriginal) {
            if (!cp.getAddress().get("county").isEmpty()) {
                if (cp.getAddress().get("county").contains(county)) {
                    cpCounty.add(cp);
                }
            }
        }

        Log.d("TAG", "filterChargePointsByCounty: " + cpCounty.toString());
        chargePoints = cpCounty;
        notifyDataSetChanged();
    }
}
