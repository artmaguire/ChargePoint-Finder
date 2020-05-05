package com.example.chargepoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chargepoint.R;
import com.example.chargepoint.adapter.CardsAdapter;
import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Card;

import java.util.List;

/**
 * Created By Art
 * Shows all user cards with an option to Create New Card
 */
public class UserCardsFragment extends BackFragment {
    private static final String TAG = "USER_CARDS";

    private ProgressBar pgsBar;
    private TextView noCardsView;
    private Button createNewCard;
    private List<Card> cards;
    private CardsAdapter adapter;

    public UserCardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createNewCard = view.findViewById(R.id.createNewCard);
        RecyclerView recyclerView = view.findViewById(R.id.userCardsView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CardsAdapter(view);
        adapter = new CardsAdapter(view);
        recyclerView.setAdapter(adapter);

        pgsBar = view.findViewById(R.id.cardsPBar);

        noCardsView = view.findViewById(R.id.noCardsView);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeCard(position);

                if (adapter.getItemCount() == 0)
                    noCardsView.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(), R.string.card_deleted, Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getCards(task -> {
            if (task.isSuccessful()) {
                cards = task.getResult().toObjects(Card.class);

                adapter.setCards(cards);

                if (adapter.getItemCount() == 0) {
                    noCardsView.setVisibility(View.VISIBLE);
                } else {
                    adapter.notifyDataSetChanged();
                }

                pgsBar.setVisibility(View.GONE);
            }
        });

        createNewCard.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putBoolean("New Card", true);
            Navigation.findNavController(view).navigate(R.id.action_userCardsFragment_to_fragment_payment_details, b);
        });
    }
}
