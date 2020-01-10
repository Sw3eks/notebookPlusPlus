package de.mobicom.notebookplusplus.view;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.databinding.FragmentCalendarBinding;
import de.mobicom.notebookplusplus.utils.decorators.EventDecorator;
import de.mobicom.notebookplusplus.utils.decorators.TodayDecorator;
import de.mobicom.notebookplusplus.utils.decorators.HighlightWeekendsDecorator;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

public class CalendarFragment extends Fragment {
    public static final String CALENDAR_FRAGMENT = CalendarFragment.class.getSimpleName();

    private FragmentCalendarBinding fragmentCalendarBinding;
    private NotebookViewModel notebookViewModel;
    private List<CalendarDay> eventDays;
    private MaterialCalendarView calendarView;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        calendarView = fragmentCalendarBinding.calendarView;
        setup();

        // Get event days in background
        new EventDaysAsyncTask().executeOnExecutor(Executors.newSingleThreadExecutor());
        return fragmentCalendarBinding.getRoot();
    }

    private void setup() {
        calendarView.addDecorator(new HighlightWeekendsDecorator(this));
        calendarView.addDecorator(new TodayDecorator());
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (eventDays.contains(date)) {
                Toast.makeText(getContext(), "TEST", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), R.string.calendar_no_events, Toast.LENGTH_LONG).show();
            }
        });
        //fragmentCalendarBinding.calendarView.setDateSelected(CalendarDay.today(), true);
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
                fragmentCalendarBinding.calendarView.setCurrentDate(CalendarDay.today());
                break;
            case R.id.to_notebook:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_calendarFragment_to_notebooksFragment);
                break;
        }

        return true;
    }

    private class EventDaysAsyncTask extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            eventDays = new ArrayList<>();
            List<Note> noteList = notebookViewModel.getAllNotesNotificationEnabled();

            for (int i = 0; i < noteList.size(); i++) {
                final CalendarDay day = CalendarDay.from(LocalDate.of(noteList.get(i).getNotificationDate().getYear(),
                        noteList.get(i).getNotificationDate().getMonthValue(), noteList.get(i).getNotificationDate().getDayOfMonth()));
                eventDays.add(day);
            }

            return eventDays;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            fragmentCalendarBinding.calendarView.addDecorator(new EventDecorator(Color.RED, eventDays));
        }
    }
}
