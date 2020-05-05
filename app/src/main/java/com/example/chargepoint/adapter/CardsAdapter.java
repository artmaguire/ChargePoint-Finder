package com.example.chargepoint.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Card;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private static final String TAG = "Card_adapter";

    private List<Card> cards;
    private View root;

    public CardsAdapter(View root) {
        this.root = root;
        this.cards = new ArrayList<>();
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_details, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);

        Log.d(TAG, "onBindViewHolder: " + card.toString());
        String name = card.getCardName();
        String number = card.getCardNumber();
        String date = card.getCardDate();
        String securityNo = card.getCardSecurityNumber();

        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getCards(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "onBindViewHolder: " + task.getResult());
                try {
                    cards = task.getResult().toObjects(Card.class);
                } catch (NullPointerException e) {
                    Log.d(TAG, "onBindViewHolder: " + e);
                }
            }
        });

        holder.cardName.setText(name);
        holder.cardNumber.setText(number);
        holder.cardDate.setText(date);
        holder.cardSecurity.setText(securityNo);

        holder.cv.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putParcelable("Card", card);
            b.putBoolean("New Card", false);
            Navigation.findNavController(root).navigate(R.id.action_userCardsFragment_to_fragment_payment_details, b);
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView cardName;
        TextView cardNumber;
        TextView cardDate;
        TextView cardSecurity;

        ViewHolder(CardView view) {
            super(view);
            cv = view;
            cardName = view.findViewById(R.id.cardName);
            cardNumber = view.findViewById(R.id.cardNumber);
            cardDate = view.findViewById(R.id.expiryDate);
            cardSecurity = view.findViewById(R.id.securityNumber);
        }
    }
}
