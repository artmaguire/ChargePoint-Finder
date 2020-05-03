package com.example.chargepoint.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.ChargePoint;
import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.utils.PreferenceConfiguration;

import java.text.DateFormat;
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
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_receipts_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Receipt receipt = receipts.get(position);

        Date date = receipt.getDatetime().toDate();
        Locale locale = PreferenceConfiguration.getCurrentLocale(root.getContext());
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        String dateString = df.format(date);

        final double amount = receipt.getCost();

        final String[] operator = {null};
        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getChargePoint(receipt.getMap_id(), task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onBindViewHolder: " + task.getResult());
                try {
                    ChargePoint cp = task.getResult().toObject(ChargePoint.class);
                    operator[0] = cp.getOperator();
                    int drawable = getIcon(operator[0]);
                    holder.receiptIcon.setImageResource(drawable);
                } catch (NullPointerException e) {
                    Log.d(TAG, "onBindViewHolder: " + e);
                }
            }
        });


        holder.receiptDate.setText(dateString);
        holder.receiptAmount.setText(root.getContext().getString(R.string.currency_symbol_with_amount, amount));
        holder.receiptDuration.setText(root.getContext().getString(R.string.time_mins, receipt.getDuration()));

        holder.cv.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putParcelable("Receipt", receipt);
            b.putInt("icon", getIcon(operator[0]));
            Navigation.findNavController(root).navigate(R.id.action_fragment_previous_receipts_to_fragment_receipt, b);
        });
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    private int getIcon(String title) {
        int drawableId;

        if (title == null) {
            drawableId = R.drawable.ic_generic_32dp;
        } else {
            Log.d(TAG, "getIcon: " + title);

            if (title.toLowerCase().contains("tesla"))
                drawableId = R.drawable.ic_tesla_32dp;
            else if (title.toLowerCase().toLowerCase().contains("esb"))
                drawableId = R.drawable.ic_esb_32dp;
            else if (title.toLowerCase().toLowerCase().contains("pod"))
                drawableId = R.drawable.ic_pod_32dp;
            else if (title.toLowerCase().toLowerCase().contains("nissan"))
                drawableId = R.drawable.ic_nissan_32dp;
            else if (title.toLowerCase().toLowerCase().contains("ionic"))
                drawableId = R.drawable.ic_ionic_32dp;
            else if (title.toLowerCase().toLowerCase().contains("private"))
                drawableId = R.drawable.ic_private_32dp;
            else
                drawableId = R.drawable.ic_generic_32dp;
        }

        return drawableId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView receiptIcon;
        TextView receiptDate;
        TextView receiptAmount;
        TextView receiptDuration;

        ViewHolder(CardView view) {
            super(view);
            cv = view;
            receiptIcon = view.findViewById(R.id.receiptIcon);
            receiptDate = view.findViewById(R.id.receiptDate);
            receiptAmount = view.findViewById(R.id.receiptAmount);
            receiptDuration = view.findViewById(R.id.receiptDuration);
        }
    }
}
