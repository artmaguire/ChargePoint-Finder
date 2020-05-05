package com.example.chargepoint.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chargepoint.db.FirebaseHelper;
import com.example.chargepoint.pojo.Receipt;

import java.util.Collections;
import java.util.List;

/**
 * Created by Art
 */
public class ReceiptViewModel extends ViewModel {
    private final static String TAG = "RECEIPT_VM";

    private MutableLiveData<List<Receipt>> receipts;

    public LiveData<List<Receipt>> getObservableReceipts() {
        if (receipts == null) {
            receipts = new MutableLiveData<>();
            loadReceipts();
        }
        return receipts;
    }

    private void loadReceipts() {
        FirebaseHelper fbHelper = FirebaseHelper.getInstance();
        fbHelper.getAllReceiptsFromUser(task -> {
            if (task.getResult() == null)
                return;

            List<Receipt> receipts = task.getResult().toObjects(Receipt.class);
            Collections.sort(receipts);
            Log.d(TAG, "" + receipts.size());
            this.receipts.postValue(receipts);
        });
    }

    public void destroyReceipts() {
        receipts = null;
    }
}
