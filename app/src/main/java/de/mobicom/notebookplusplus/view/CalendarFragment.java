package de.mobicom.notebookplusplus.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.databinding.FragmentCalendarBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

public class CalendarFragment extends Fragment {
    public static final String CALENDAR_FRAGMENT = "CALENDAR_FRAGMENT";

    private FragmentCalendarBinding fragmentCalendarBinding;
    private NotebookViewModel notebookViewModel;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        setupListeners();
        return fragmentCalendarBinding.getRoot();
    }

    private void setupListeners() {
        
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
            case R.id.reset_calendar:
                fragmentCalendarBinding.calendarView.setDate(System.currentTimeMillis());
                break;
            case R.id.to_notebook:
                Toast.makeText(getContext(), "notebook", Toast.LENGTH_LONG).show();
                break;

        }

        return true;
    }
}
