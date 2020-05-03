package com.example.chargepoint.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.utils.ChargePointNotificationManager;

public class ChargingService extends Service {
    public static final String BROADCAST_RECEIVER = "com.example.chargepoint.services.ChargingService";
    public static final String CHARGE_RECEIPT = "CHARGE_RECEIPT";
    public static final String CHARGE_COUNTDOWN = "CHARGE_COUNTDOWN";
    private static final String TAG = "CP_CHARGING_SERVICE";
    private static Receipt receipt;

    private Intent intent;
    private CountDownTimer cdt;

    public static Receipt getReceipt() {
        return receipt;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_RECEIVER);
    }

    private void startTimer(long finishMillis) {
        if (cdt != null) return;
        final Context context = this;

        long millis = finishMillis - System.currentTimeMillis();

        cdt = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millis);
                intent.putExtra(CHARGE_COUNTDOWN, millisUntilFinished);
                sendBroadcast(intent);
            }

            public void onFinish() {
                intent.putExtra(CHARGE_COUNTDOWN, -1);
                sendBroadcast(intent);

                ChargePointNotificationManager.displayCarChargedNotification(context);
                stopSelf();
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        receipt = intent.getParcelableExtra(CHARGE_RECEIPT);

        // Time since epoch when charging will be finished
        long finishMillis;
        if (receipt != null) {
            finishMillis = receipt.getFinishTimeInMillis();

            ChargePointNotificationManager.displayCarChargingNotification(this, finishMillis);

            startTimer(finishMillis);
        }

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (cdt != null)
            cdt.cancel();
        receipt = null;
        super.onDestroy();
    }
}
