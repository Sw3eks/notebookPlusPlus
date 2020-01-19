package de.mobicom.notebookplusplus.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.time.DayOfWeek;
import java.time.LocalDate;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

public class AlertReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_DAY_ID = 1;
    public static final int NOTIFICATION_WEEK_ID = 2;

    /**
     * Creates a notification after receiving an intent
     * If notifications are turned on, it sends:
     * daily -> reminder for upcoming events tomorrows
     * weekly on sunday -> reminder for upcoming events next week
     *
     * @param context current context
     * @param intent  intent which triggered the receiver
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("calendar_notifications", false) &&
                prefs.getBoolean("tomorrow_reminder", false)) {

            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();

            NotificationHelper notificationHelper = new NotificationHelper(context);
            if (dayOfWeek != DayOfWeek.SUNDAY) {
                // check if notification should be created
                if (notificationHelper.shouldCreateNotification(false)) {
                    NotificationCompat.Builder nb = notificationHelper.getChannelDayNotification();
                    notificationHelper.getManager().notify(NOTIFICATION_DAY_ID, nb.build());
                }
            } else {
                if (notificationHelper.shouldCreateNotification(true)) {
                    NotificationCompat.Builder nb = notificationHelper.getChannelWeekNotification();
                    notificationHelper.getManager().notify(NOTIFICATION_WEEK_ID, nb.build());
                }
            }
        }
    }
}
