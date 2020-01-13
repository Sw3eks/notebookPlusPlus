package de.mobicom.notebookplusplus.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.core.app.NotificationCompat;
import de.mobicom.notebookplusplus.MainActivity;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.NotebookDatabase;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.view.CalendarFragment;

public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_DAY_ID = "SEND_DAILY_REMINDER";
    public static final String CHANNEL_WEEK_ID = "SEND_WEEKLY_REMINDER";
    public static final String CHANNEL_DAY_NAME = "Daily Reminder";
    public static final String CHANNEL_WEEK_NAME = "Weekly Reminder";
    public static final int CONTENT_INTENT_REQUEST_CODE = 3;

    private NotificationManager mManager;
    private List<Note> noteList;
    private Context context;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        this.context = base;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channelDay = new NotificationChannel(CHANNEL_DAY_ID, CHANNEL_DAY_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        NotificationChannel channelWeek = new NotificationChannel(CHANNEL_WEEK_ID, CHANNEL_WEEK_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        getManager().createNotificationChannel(channelDay);
        getManager().createNotificationChannel(channelWeek);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public boolean shouldCreateNotification() {
        return noteList.size() != 0;
    }

    public NotificationCompat.Builder getChannelDayNotification() {
        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        activityIntent.putExtra("dest", CalendarFragment.CALENDAR_FRAGMENT);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                CONTENT_INTENT_REQUEST_CODE, activityIntent, 0);

        try {
            noteList = new NotifyDayAsyncTask(context).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (int i = 0; i < noteList.size(); i++) {
            style.addLine(noteList.get(i).getName());
        }
        style.setSummaryText(getResources().getString(R.string.build_notification_summary));

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_DAY_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.build_notification_title_day))
                .setContentText(getResources().getString(R.string.build_notification_content_day))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(style)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
    }

    public NotificationCompat.Builder getChannelWeekNotification() {
        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        activityIntent.putExtra("dest", CalendarFragment.CALENDAR_FRAGMENT);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                CONTENT_INTENT_REQUEST_CODE, activityIntent, 0);

        try {
            noteList = new NotifyWeekAsyncTask(context).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (int i = 0; i < noteList.size(); i++) {
            style.addLine(noteList.get(i).getName());
        }
        style.setSummaryText(getResources().getString(R.string.build_notification_summary));

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_DAY_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.build_notification_title_week))
                .setContentText(getResources().getString(R.string.build_notification_content_week))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(style)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
    }

    private static class NotifyDayAsyncTask extends AsyncTask<Context, Void, List<Note>> {
        private NotebookDatabase notebookDatabase;

        NotifyDayAsyncTask(Context context) {
            notebookDatabase = NotebookDatabase.getInstance(context);
        }

        @Override
        protected List<Note> doInBackground(Context... params) {
            return notebookDatabase.noteDao().getAllNotesForNotificationDay(LocalDate.now().plusDays(1));
        }
    }

    private static class NotifyWeekAsyncTask extends AsyncTask<Context, Void, List<Note>> {
        private NotebookDatabase notebookDatabase;

        NotifyWeekAsyncTask(Context context) {
            notebookDatabase = NotebookDatabase.getInstance(context);
        }

        @Override
        protected List<Note> doInBackground(Context... params) {
            return notebookDatabase.noteDao().getAllNotesForNotificationWeek(LocalDate.now().plusDays(1), LocalDate.now().plusDays(7));
        }
    }

}