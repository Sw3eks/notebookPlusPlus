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
import android.widget.ListView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteEventAdapter;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.databinding.FragmentCalendarBinding;
import de.mobicom.notebookplusplus.utils.decorators.EventDecorator;
import de.mobicom.notebookplusplus.utils.decorators.OneDayDecorator;
import de.mobicom.notebookplusplus.utils.decorators.TodayDecorator;
import de.mobicom.notebookplusplus.utils.decorators.HighlightWeekendsDecorator;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

/**
 * Fragment with a calendar widget, where saved notification dates for specific notes are highlighted
 */
public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding fragmentCalendarBinding;
    private NotebookViewModel notebookViewModel;
    private List<CalendarDay> eventDays;
    private List<Note> noteList;
    private MaterialCalendarView calendarView;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        calendarView = fragmentCalendarBinding.calendarView;
        setup();
        return fragmentCalendarBinding.getRoot();
    }

    private void setup() {
        noteList = notebookViewModel.getAllNotesNotificationEnabled();
        calendarView.addDecorators(
                new HighlightWeekendsDecorator(this),
                new TodayDecorator(),
                oneDayDecorator);
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            oneDayDecorator.setDate(date.getDate());
            widget.invalidateDecorators();
            if (eventDays.contains(date)) {

                View view = getLayoutInflater().inflate(R.layout.dialog_list, null, false);

                NoteEventAdapter adapter = new NoteEventAdapter(noteList.stream().filter(note -> LocalDate.of(note.getNotificationDate().getYear(),
                        note.getNotificationDate().getMonthValue(),
                        note.getNotificationDate().getDayOfMonth()).isEqual(date.getDate())).collect(Collectors.toList()),
                        getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                        .setView(view)
                        .setTitle(R.string.note_events_title)
                        .setNegativeButton(R.string.cancel_button,
                                (dialog, arg1) -> dialog.dismiss());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ListView listView = view.findViewById(R.id.list_view);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view1, position, id) -> {
                    notebookViewModel.setNote(adapter.getItem(position));
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_calendarFragment_to_noteEditorFragment);
                    alertDialog.dismiss();
                });


            } else {
                Toast.makeText(getContext(), R.string.calendar_no_events, Toast.LENGTH_LONG).show();
            }
        });

        // Get event days in background
        try {
            eventDays = new EventDaysAsyncTask(noteList).executeOnExecutor(Executors.newSingleThreadExecutor()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        fragmentCalendarBinding.calendarView.addDecorator(new EventDecorator(Color.RED, eventDays));
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

    private static class EventDaysAsyncTask extends AsyncTask<Void, Void, List<CalendarDay>> {
        private List<Note> noteList;

        EventDaysAsyncTask(List<Note> noteList) {
            this.noteList = noteList;
        }

        @Override
        protected final List<CalendarDay> doInBackground(Void... voids) {
            List<CalendarDay> eventDays = new ArrayList<>();

            for (int i = 0; i < noteList.size(); i++) {
                final CalendarDay day = CalendarDay.from(LocalDate.of(noteList.get(i).getNotificationDate().getYear(),
                        noteList.get(i).getNotificationDate().getMonthValue(), noteList.get(i).getNotificationDate().getDayOfMonth()));
                eventDays.add(day);
            }

            return eventDays;
        }
    }
}
