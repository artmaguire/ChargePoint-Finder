package com.example.chargepoint;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsRecyclerAdapter extends RecyclerView.Adapter<ReceiptsRecyclerAdapter.ViewHolder> {

    private List<Receipt> receipts;
    private Context context;

    public ReceiptsRecyclerAdapter(Context context) {
        this.context = context;
        this.receipts = new ArrayList<>();
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
        Log.d("TAG", "setReceipts: " + receipts.size());
    }

    @NonNull
    @Override
    public ReceiptsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_receipts_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);

        /*Date date = receipt.getDatetime();
        String pattern = "MMMM dd yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        holder.receiptDate.setText(dateString);*/
        holder.receiptDate.setText("hello world");
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView receiptDate;

        ViewHolder(CardView view) {
            super(view);
            cv = view;
            receiptDate = view.findViewById(R.id.receiptDate);
        }
    }
}
