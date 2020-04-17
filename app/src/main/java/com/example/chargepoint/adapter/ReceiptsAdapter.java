package com.example.chargepoint.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.pojo.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder> {

    private static final String TAG = "Receipt Adapter";

    private List<Receipt> receipts;
    private View root;

    public ReceiptsAdapter(View root) {
        this.root = root;
        this.receipts = new ArrayList<>();
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
            Bundle b = new Bundle();
            b.putParcelable("Receipt", receipt);
            Navigation.findNavController(root).navigate(R.id.action_fragment_previous_receipts_to_fragment_receipt, b);
        });
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
