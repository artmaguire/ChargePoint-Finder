package com.example.chargepoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.fragments.Items;

import java.util.ArrayList;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<Items> items;
    private Context context;

    public NewsAdapter(ArrayList<Items> items, Context context) {
        this.items = items;
        this.context = context;

    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_news_items, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {

        final Items item = items.get(position);
        holder.imageView.setImageResource(item.getImageResources());
        holder.titleTextView.setText(item.getTitle());
        holder.descTextView.setText(item.getDesc());
        holder.descTextView.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                Items contentItem = items.get(position);
                contentItem.setShrink(isShrink);
                items.set(position, contentItem);
            }
        });

        holder.descTextView.setText(item.getDesc());
        holder.descTextView.resetState(item.isShrink());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ExpandableTextView descTextView;
        TextView titleTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            descTextView = itemView.findViewById(R.id.descTextview);
            titleTextView = itemView.findViewById(R.id.titleText);
        }
    }
}