package com.example.chargepoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.ReceiptActivity;
import com.example.chargepoint.pojo.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder> {

    private static final String TAG = "Receipt Adapter";

    private final AdapterView.OnItemClickListener listener;

    private List<Receipt> receipts;
    private Context context;

    public ReceiptsAdapter(Context context) {
        this.context = context;
        this.receipts = new ArrayList<>();
        listener = null;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    @NonNull
    @Override
    public ReceiptsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_receipts_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);

        Date date = receipt.getDatetime().toDate();
        String pattern = "MMMM dd, yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String dateString = df.format(date);

        final double amount = receipt.getCost();

        holder.receiptDate.setText(dateString);
        holder.receiptAmount.setText("€".concat(String.valueOf(amount)));

        holder.cv.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReceiptActivity.class);
            intent.putExtra("receipt", receipt);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView receiptDate;
        TextView receiptAmount;

        ViewHolder(CardView view) {
            super(view);
            cv = view;
            receiptDate = view.findViewById(R.id.receiptDate);
            receiptAmount = view.findViewById(R.id.receiptAmount);
        }
    }
}
