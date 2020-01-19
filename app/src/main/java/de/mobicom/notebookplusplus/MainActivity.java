package de.mobicom.notebookplusplus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import de.mobicom.notebookplusplus.databinding.ActivityMainBinding;
import de.mobicom.notebookplusplus.utils.AlertReceiver;
import de.mobicom.notebookplusplus.view.CalendarFragment;

public class MainActivity extends AppCompatActivity {
    public static final int NOTIFICATION_DAILY_REQUEST_CODE = 1;

    private ActivityMainBinding binding;
    private SharedPreferences prefs;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupNavigation();
        setupNotification();
    }

    /**
     * Sets up the navigation throughout the app
     */
    private void setupNavigation() {
        toolbar = binding.toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        drawerLayout = binding.drawerLayout;
        nvDrawer = binding.navView;

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(nvDrawer, navController);
        NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        nvDrawer.setCheckedItem(R.id.notebooksFragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            String extra = intent.getStringExtra("dest");
            if (extra != null && extra.equals(CalendarFragment.CALENDAR_FRAGMENT)) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.calendarFragment);
            }
        }
    }

    /**
     * creates pendingIntent for alarm manager to send a notification on a specific time
     */
    private void setupNotification() {
        Intent notificationIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_DAILY_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (prefs.getBoolean("calendar_notifications", false) &&
                (prefs.getBoolean("tomorrow_reminder", false) || prefs.getBoolean("week_reminder", false))) {
            Calendar fireCal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();

            fireCal.set(Calendar.HOUR_OF_DAY, 20);
            fireCal.set(Calendar.MINUTE, 0);
            fireCal.set(Calendar.SECOND, 0);

            long intendedTime = fireCal.getTimeInMillis();
            long currentTime = currentCal.getTimeInMillis();

            if (alarmManager != null) {
                if (intendedTime >= currentTime) {

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

                } else {

                    fireCal.add(Calendar.DAY_OF_MONTH, 1);
                    intendedTime = fireCal.getTimeInMillis();

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
                }
            }
        } else if (prefs.getBoolean("tomorrow_reminder", false) && prefs.getBoolean("week_reminder", false)) {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }
}
