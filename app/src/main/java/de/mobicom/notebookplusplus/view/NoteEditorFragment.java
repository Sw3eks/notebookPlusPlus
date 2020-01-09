package de.mobicom.notebookplusplus.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteListItemRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.databinding.FragmentNoteEditorBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

import static android.graphics.Color.parseColor;

public class NoteEditorFragment extends Fragment implements NoteListItemRecyclerViewAdapter.ItemClickListener {

    private NotebookViewModel notebookViewModel;
    private FragmentNoteEditorBinding fragmentNoteEditorBinding;
    private NoteListItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    private List<NoteListItem> currentList;
    private String currentTitle;
    private boolean currentNotificationStatus;
    private LocalDate selectedDate;
    private boolean isChanged;
    private String message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNoteEditorBinding = FragmentNoteEditorBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        isChanged = false;
        message = getResources().getString(R.string.changes_saved);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        fragmentNoteEditorBinding.colorBar.setBackgroundColor(parseColor(notebookViewModel.getNotebook().getColor()));
        fragmentNoteEditorBinding.setNote(notebookViewModel.getNote());
        currentTitle = notebookViewModel.getNote().getName();
        currentNotificationStatus = notebookViewModel.getNote().isNotificationEnabled();

        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_text) {
            setupTextLayout();
        }

        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_todo) {
            setupListLayout();
        }
        setupDatePicker();

        return fragmentNoteEditorBinding.getRoot();
    }

    private void setupTextLayout() {
        fragmentNoteEditorBinding.editNote.requestFocus();
        fragmentNoteEditorBinding.editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals(notebookViewModel.getNote().getDescription())) {
                    isChanged = false;
                } else {
                    notebookViewModel.getNote().setDescription(s.toString().trim());
                    isChanged = true;
                }
            }
        });
    }

    private void setupListLayout() {
        fragmentNoteEditorBinding.editNote.setVisibility(View.GONE);
        fragmentNoteEditorBinding.rvNoteListItem.setVisibility(View.VISIBLE);

        recyclerView = fragmentNoteEditorBinding.rvNoteListItem;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NoteListItemRecyclerViewAdapter();
        adapter.setCheckBoxListener(this);
        adapter.setEnterKeyListener(this);
        adapter.setTextChangeListener(this);
        recyclerView.setAdapter(adapter);

        NoteListItem defaultItem = new NoteListItem(notebookViewModel.getNote().getNoteId(), "", false);
        if (notebookViewModel.getAllNoteListItemsAsync(notebookViewModel.getNote().getNoteId()) != null) {
            currentList = new ArrayList<>(notebookViewModel.getAllNoteListItemsAsync(notebookViewModel.getNote().getNoteId()));
        } else {
            currentList = new ArrayList<>();
        }
        currentList.add(defaultItem);
        adapter.submitList(currentList);
    }

    private void setupDatePicker() {
        // ClickListener instead of checkChangeListener to prevent firing message when view is opened
        fragmentNoteEditorBinding.enableNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notebookViewModel.getNote().setNotificationEnabled(!notebookViewModel.getNote().isNotificationEnabled());
                Toast.makeText(getContext(),
                        notebookViewModel.getNote().isNotificationEnabled() ? getResources().getString(R.string.notification_enabled)
                                : getResources().getString(R.string.notification_disabled), Toast.LENGTH_LONG)
                        .show();
            }
        });

        // set min Date to Today (cause notification has to be in future
        fragmentNoteEditorBinding.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        fragmentNoteEditorBinding.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notebookViewModel.getNote().getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_editor, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
            case R.id.create_notification:
                if (fragmentNoteEditorBinding.cardView.getVisibility() == View.GONE) {
                    fragmentNoteEditorBinding.cardView.setVisibility(View.VISIBLE);
                } else {
                    fragmentNoteEditorBinding.cardView.setVisibility(View.GONE);
                }
                break;
            case R.id.editNoteTitle:
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(NoteEditorFragmentDirections.actionNoteEditorFragmentToCreateNotebookDialogFragment().setDialogType("Note Editor"));
                break;
            case R.id.moveToArchive:
                notebookViewModel.getNote().setArchived(true);
                isChanged = true;
                message = getResources().getString(R.string.moved_note_to_archive);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
            case R.id.deleteNote:
                notebookViewModel.getNote().setMarkedForDelete(true);
                isChanged = true;
                message = getResources().getString(R.string.moved_note_to_deleted);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                break;
        }

        return true;
    }

    @Override
    public void onPause() {
        if (notebookViewModel.getNote().getType() == R.drawable.ic_note_type_todo) {
            saveList();
        }
        shouldSave();
        super.onPause();
    }

    private void saveList() {
        for (int i = 0; i < currentList.size(); i++) {
            // save only entries with content
            if (!currentList.get(i).getContent().equals("")) {
                System.out.println("SAVED");
                notebookViewModel.insert(currentList.get(i));
            }
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void shouldSave() {
        if (!notebookViewModel.getNote().getName().equals(currentTitle) ||
                notebookViewModel.getNote().isNotificationEnabled() != currentNotificationStatus) {
            isChanged = true;
        }
        if (selectedDate != null && !notebookViewModel.getNote().getNotificationDate().isEqual(selectedDate)) {
            notebookViewModel.getNote().setNotificationDate(selectedDate);
            isChanged = true;
        }
        if (isChanged) {
            notebookViewModel.getNote().setLastModifiedAt(LocalDateTime.now());
            notebookViewModel.update(notebookViewModel.getNote());
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCheckBoxClick(View view, boolean isChecked, int position) {
        currentList.get(position).setChecked(isChecked);
        adapter.submitList(currentList);
    }

    @Override
    public void onEnterClicked(int actionId, KeyEvent event, String content, int position) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
            return;
        }

        if (currentList.indexOf(adapter.getNoteItemAt(position)) == currentList.size() - 1 && content.trim().length() > 0 && actionId == KeyEvent.KEYCODE_ENTER) {
            NoteListItem item = new NoteListItem(notebookViewModel.getNote().getNoteId(), "", false);
            currentList.add(item);
            adapter.submitList(currentList);
            adapter.notifyItemInserted(position + 1);
            recyclerView.smoothScrollToPosition(position + 1);
            return;
        }
        if (content.equals("") && actionId == KeyEvent.KEYCODE_DEL) {
            currentList.remove(adapter.getNoteItemAt(position));
            adapter.submitList(currentList);
            adapter.notifyItemRemoved(position);
            return;
        }

        // to keep back button working
        if (actionId == KeyEvent.KEYCODE_BACK) {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        }
    }

    @Override
    public void onTextChange(CharSequence s, int position) {
        if (adapter.getNoteItemAt(position).getNoteListItemId() != 0) {
            NoteListItem item = adapter.getNoteItemAt(position);
            item.setContent(s.toString().trim());
            currentList.get(position).setContent(s.toString().trim());
            adapter.submitList(currentList);
        }
    }
}
