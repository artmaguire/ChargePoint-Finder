package com.example.chargepoint.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.MainActivity;

public class ChargePointNotificationManager {

    public static void displayCarChargedNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.chargepoint_car_charged))
                .setContentTitle("Your car is ready!")
                .setContentText("Please disconnect the charger")
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setContentIntent(PendingIntent.getActivity(context, 11, new Intent(context, MainActivity.class), 0))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_ev_station_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher_round))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    public static void displayCarChargingNotification(Context context, long finishMillis) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.chargepoint_car_charging))
                .setContentTitle("Your car is charging")
                .setContentText("Tap for more info")
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setWhen(finishMillis)
                .setContentIntent(PendingIntent.getActivity(context, 10, new Intent(context, MainActivity.class), 0))
                .addExtras(new Bundle())
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_ev_station_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher_round))
                .setPriority(NotificationCompat.PRIORITY_MIN);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    public static void cancelAll(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    public static void createNotificationChannels(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chargedName = context.getString(R.string.chargepoint_car_charged);
            String ChargedDescription = context.getString(R.string.chargepoint_car_charged_description);
            int chargedImportance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel chargedChannel = new NotificationChannel(chargedName, chargedName, chargedImportance);
            chargedChannel.setDescription(ChargedDescription);

            String chargingName = context.getString(R.string.chargepoint_car_charging);
            String chargingDescription = context.getString(R.string.chargepoint_car_charging_description);
            int chargingImportance = NotificationManager.IMPORTANCE_MIN;

            NotificationChannel chargingChannel = new NotificationChannel(chargingName, chargingName, chargingImportance);
            chargingChannel.setDescription(chargingDescription);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(chargedChannel);
                notificationManager.createNotificationChannel(chargingChannel);
            }
        }
    }
}
