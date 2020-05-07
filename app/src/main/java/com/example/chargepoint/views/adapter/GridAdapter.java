package com.example.chargepoint.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chargepoint.R;

public class GridAdapter extends BaseAdapter {

    Context context;
    private final String [] names;
    private final int [] images;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context context, String[] names, int[] images) {
        this.context = context;
        this.names = names;
        this.images = images;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {

            view = new View(context);
            view = layoutInflater.inflate(R.layout.grid_item, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.name);
            imageView.setImageResource(images[position]);
            name.setText(names[position]);
        }
        return view;
    }
}
