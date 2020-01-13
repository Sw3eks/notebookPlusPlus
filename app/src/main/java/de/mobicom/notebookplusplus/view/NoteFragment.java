package de.mobicom.notebookplusplus.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.adapter.NotebookListAdapter;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.data.model.Notebook;
import de.mobicom.notebookplusplus.databinding.FragmentNoteBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

import static android.graphics.Color.parseColor;

public class NoteFragment extends Fragment implements NoteRecyclerViewAdapter.ItemClickListener {
    static final String NOTE_FRAGMENT = NoteFragment.class.getSimpleName();

    private NoteRecyclerViewAdapter adapter;
    private NotebookViewModel notebookViewModel;
    private FragmentNoteBinding fragmentNoteBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNoteBinding = FragmentNoteBinding.inflate(inflater, container, false);
        fragmentNoteBinding.setHandler(this);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = fragmentNoteBinding.rvNotes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setHasFixedSize(true);
        adapter = new NoteRecyclerViewAdapter(NOTE_FRAGMENT);
        adapter.setClickListener(this);
        adapter.setPopupMenuItemClickListener(this);
        adapter.setBookmarkClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return fragmentNoteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        notebookViewModel.getAllNotesFromNotebook(notebookViewModel.getNotebook().getNotebookId())
                .observe(this, notes -> {
                    if (!notes.isEmpty()) {
                        adapter.submitList(notes);
                        fragmentNoteBinding.setIsEmpty(false);
                    } else {
                        fragmentNoteBinding.setIsEmpty(true);
                    }
                });

        fragmentNoteBinding.colorBar.setBackgroundColor(parseColor(notebookViewModel.getNotebook().getColor()));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notebookViewModel.getNotebook().getName());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(View view, int position) {
        notebookViewModel.setNote(adapter.getNoteAt(position));

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_noteFragment_to_noteEditorFragment);
    }

    @Override
    public void onPopupMenuItemClick(MenuItem item, int position) {
        Note tmpNote = adapter.getNoteAt(position);
        switch (item.getItemId()) {
            case R.id.moveNote:
                moveNoteDialog(tmpNote);
                break;
            case R.id.archiveNote:
                tmpNote.setArchived(true);
                notebookViewModel.update(tmpNote);
                Toast.makeText(getContext(), R.string.moved_note_to_archive, Toast.LENGTH_LONG).show();
                break;
            case R.id.cloneNote:
                notebookViewModel.insert(new Note(
                        tmpNote.getNotebookParentId(),
                        tmpNote.getName(),
                        tmpNote.getType(),
                        tmpNote.getDescription(),
                        tmpNote.isBookmarked(),
                        tmpNote.getNotificationDate(),
                        tmpNote.isNotificationEnabled()));
                break;
            case R.id.deleteNote:
                tmpNote.setMarkedForDelete(true);
                notebookViewModel.update(tmpNote);
                Toast.makeText(getContext(), R.string.moved_note_to_deleted, Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void moveNoteDialog(Note note) {
        View view = getLayoutInflater().inflate(R.layout.dialog_list, null, false);

        List<Notebook> notebookList = notebookViewModel.getAllNotebooks().getValue();
        if (notebookList != null) {
            notebookList = notebookList.stream().filter(notebook -> notebook.getNotebookId() != notebookViewModel.getNotebook().getNotebookId()).collect(Collectors.toList());
        }

        NotebookListAdapter adapter = new NotebookListAdapter(notebookList,
                getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                .setView(view)
                .setTitle(R.string.select_notebook_to_move)
                .setNegativeButton(R.string.cancel_button,
                        (dialog, arg1) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ListView listView = view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            note.setNotebookParentId(adapter.getItem(position).getNotebookId());
            notebookViewModel.update(note);
            alertDialog.dismiss();
        });
    }

    @Override
    public void onBookmarkClick(View view, int position) {
        Note tmpNote = adapter.getNoteAt(position);
        tmpNote.setBookmarked(!tmpNote.isBookmarked());
        notebookViewModel.update(tmpNote);
        adapter.notifyItemChanged(position);
    }

    public void onAddNote() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(NoteFragmentDirections.actionNoteFragmentToCreateNotebookDialogFragment().setDialogType(NOTE_FRAGMENT));
    }
}
