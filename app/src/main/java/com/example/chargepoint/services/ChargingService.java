package com.example.chargepoint.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import com.example.chargepoint.pojo.Receipt;
import com.example.chargepoint.receivers.ChargingAlarmReceiver;
import com.example.chargepoint.utils.ChargePointNotificationManager;

/**
 * Created by Art
 * Countdown timer for charging at a ChargePoint
 * Causes a notification
 */
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            receipt = intent.getParcelableExtra(CHARGE_RECEIPT);

            // Time since epoch when charging will be finished
            long finishMillis;
            if (receipt != null && receipt.isCharging()) {
                finishMillis = receipt.getFinishTimeInMillis();

                ChargePointNotificationManager.displayCarChargingNotification(this, finishMillis);

                startTimer(finishMillis);
                startAlarm(finishMillis);
            } else {
                finish();
            }
        } else
            finish();

        return Service.START_STICKY;
    }

    private void startTimer(long finishMillis) {
        if (cdt != null) return;

        long millis = finishMillis - System.currentTimeMillis();

        cdt = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                intent.putExtra(CHARGE_COUNTDOWN, millisUntilFinished);
                sendBroadcast(intent);
            }

            public void onFinish() {
                intent.putExtra(CHARGE_COUNTDOWN, Long.valueOf(-1));
                sendBroadcast(intent);

                finish();
            }
        }.start();
    }

    private void finish() {
        ChargePointNotificationManager.displayCarChargedNotification(this);

        // Release WakeLock if have it from AlarmManager
        PowerManager powerManager = (PowerManager) this.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ChargingAlarmReceiver.CHARGE_WAKE_LOCK);
            if (wakeLock.isHeld())
                wakeLock.release();
        }
        stopSelf();
    }

    private void startAlarm(long finishMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ChargingAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, finishMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, finishMillis, pendingIntent);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (cdt != null)
            cdt.cancel();
        receipt = null;
        super.onDestroy();
    }
}
