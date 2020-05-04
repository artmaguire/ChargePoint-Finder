package com.example.chargepoint.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.example.chargepoint.services.ChargingService;

import static android.content.Context.POWER_SERVICE;

public class ChargingAlarmReceiver extends BroadcastReceiver {
    public static final String CHARGE_WAKE_LOCK = "CHARGEPOINT::WAKE_LOCK";
    private static final String TAG = "CP_CAR";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    CHARGE_WAKE_LOCK);
            wakeLock.acquire(2 * 60 * 1000L /*2 minutes*/);
        }

        Intent i = new Intent(context, ChargingService.class);
        context.startService(i);
    }
}
