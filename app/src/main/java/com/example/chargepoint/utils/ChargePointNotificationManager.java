package com.example.chargepoint.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.chargepoint.R;
import com.example.chargepoint.activities.MainActivity;

public class ChargePointNotificationManager {

    public static void displayCarChargedNotification(Activity activity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, activity.getString(R.string.chargepoint_car_charged)).setSmallIcon(R.drawable.ic_map_blue_24dp)
                .setContentTitle("Your car is ready")
                .setContentText("Tap for more info")
                .setColor(activity.getResources().getColor(R.color.colorAccent))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    public static void displayCarChargingNotification(Activity activity, long millis) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, activity.getString(R.string.chargepoint_car_charging)).setSmallIcon(R.drawable.ic_map_blue_24dp)
                .setContentTitle("Your car is charging")
                .setContentText("Tap for more info")
                .setColor(activity.getResources().getColor(R.color.colorAccent))
                .setWhen(System.currentTimeMillis() + millis)
                .setContentIntent(PendingIntent.getActivity(activity, 10, new Intent(activity, MainActivity.class), 0))
                .addExtras(new Bundle())
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    public static void createNotificationChannels(Activity activity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chargedName = activity.getString(R.string.chargepoint_car_charged);
            String ChargedDescription = activity.getString(R.string.chargepoint_car_charged_description);
            int chargedImportance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel chargedChannel = new NotificationChannel(chargedName, chargedName, chargedImportance);
            chargedChannel.setDescription(ChargedDescription);

            String chargingName = activity.getString(R.string.chargepoint_car_charging);
            String chargingDescription = activity.getString(R.string.chargepoint_car_charging_description);
            int chargingImportance = NotificationManager.IMPORTANCE_MIN;

            NotificationChannel chargingChannel = new NotificationChannel(chargingName, chargingName, chargingImportance);
            chargingChannel.setDescription(chargingDescription);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(chargedChannel);
                notificationManager.createNotificationChannel(chargingChannel);
            }
        }
    }
}
