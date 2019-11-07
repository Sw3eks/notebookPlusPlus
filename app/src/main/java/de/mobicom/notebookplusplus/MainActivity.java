package de.mobicom.notebookplusplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import de.mobicom.notebookplusplus.calendar.CalendarFragment;
import de.mobicom.notebookplusplus.notebooks.NotebooksFragment;
import de.mobicom.notebookplusplus.notebooks.archive.ArchiveFragment;
import de.mobicom.notebookplusplus.notebooks.deletedNotes.DeletedNotesFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotebooksFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_notebooks);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_notebooks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotebooksFragment()).commit();
                break;
            case R.id.nav_calendar:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                break;
            case R.id.nav_archive:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ArchiveFragment()).commit();
                break;
            case R.id.nav_deleted:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeletedNotesFragment()).commit();
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
