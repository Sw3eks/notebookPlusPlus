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

    public static final String CHANNEL_ID = "SEND_EVENT_REMINDER";
    public static final String CHANNEL_NAME = "Events Reminder";
    public static final int CONTENT_INTENT_REQUEST_CODE = 2;

    private NotificationManager mManager;
    private List<Note> noteList;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        try {
            noteList = new NotifyAsyncTask(base).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        getManager().createNotificationChannel(channel);
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

    public NotificationCompat.Builder getChannelNotification() {
        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
        activityIntent.putExtra("dest", CalendarFragment.CALENDAR_FRAGMENT);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                CONTENT_INTENT_REQUEST_CODE, activityIntent, 0);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        for (int i = 0; i < noteList.size(); i++) {
            style.addLine(noteList.get(i).getName());
        }
        style.setSummaryText(getResources().getString(R.string.build_notification_summary));

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.build_notification_title))
                .setContentText(getResources().getString(R.string.build_notification_content))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setStyle(style)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
    }


    private static class NotifyAsyncTask extends AsyncTask<Context, Void, List<Note>> {
        private NotebookDatabase notebookDatabase;

        NotifyAsyncTask(Context context) {
            notebookDatabase = NotebookDatabase.getInstance(context);
        }

        @Override
        protected List<Note> doInBackground(Context... params) {
            return notebookDatabase.noteDao().getAllNotesForNotification(LocalDate.now().plusDays(1));
        }
    }


}