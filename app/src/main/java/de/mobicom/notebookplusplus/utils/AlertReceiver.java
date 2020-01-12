package de.mobicom.notebookplusplus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("calendar_notifications", false) &&
                prefs.getBoolean("tomorrow_reminder", false)) {

            NotificationHelper notificationHelper = new NotificationHelper(context);

            // check if notification should be created
            if (notificationHelper.shouldCreateNotification()) {
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                notificationHelper.getManager().notify(1, nb.build());
            }
        }
    }
}
