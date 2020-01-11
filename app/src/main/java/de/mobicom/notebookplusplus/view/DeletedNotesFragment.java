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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.databinding.FragmentArchiveBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

public class DeletedNotesFragment extends Fragment implements NoteRecyclerViewAdapter.ItemClickListener {
    public static final String DELETED_NOTES_FRAGMENT = DeletedNotesFragment.class.getSimpleName();

    private NoteRecyclerViewAdapter adapter;
    private NotebookViewModel notebookViewModel;
    private FragmentArchiveBinding fragmentArchiveBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentArchiveBinding = FragmentArchiveBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = fragmentArchiveBinding.rvNotes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        adapter = new NoteRecyclerViewAdapter(DELETED_NOTES_FRAGMENT);
        adapter.setPopupMenuItemClickListener(this);
        adapter.setBookmarkClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return fragmentArchiveBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        notebookViewModel.getAllNotesWithDeletedTrue()
                .observe(this, notes -> {
                    if (!notes.isEmpty()) {
                        adapter.submitList(notes);
                        fragmentArchiveBinding.setIsEmpty(false);
                    } else {
                        fragmentArchiveBinding.setIsEmpty(true);
                    }
                });
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

    }

    @Override
    public void onPopupMenuItemClick(MenuItem item, int position) {
        Note tmpNote = adapter.getNoteAt(position);
        switch (item.getItemId()) {
            case R.id.moveNote:
                tmpNote.setArchived(false);
                tmpNote.setMarkedForDelete(false);
                notebookViewModel.updateDeletedNotebook(tmpNote.getNotebookParentId());
                notebookViewModel.update(tmpNote);
                Toast.makeText(getContext(), R.string.moved_note_to_notebook, Toast.LENGTH_LONG).show();
                break;
            case R.id.archiveNote:
                tmpNote.setArchived(true);
                tmpNote.setMarkedForDelete(false);
                notebookViewModel.update(tmpNote);
                Toast.makeText(getContext(), R.string.moved_note_to_archive, Toast.LENGTH_LONG).show();
                break;
            case R.id.deleteNote:
                notebookViewModel.delete(adapter.getNoteAt(position));
                notebookViewModel.deleteAllNotebooksMarkedForDelete();
                Toast.makeText(getContext(), R.string.note_deleted, Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onBookmarkClick(View view, int position) {
        Note tmpNote = adapter.getNoteAt(position);
        tmpNote.setBookmarked(!tmpNote.isBookmarked());
        notebookViewModel.update(tmpNote);
        adapter.notifyItemChanged(position);
    }
}
