package de.mobicom.notebookplusplus.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.databinding.FragmentNoteBinding;
import de.mobicom.notebookplusplus.utils.SimpleItemTouchHelperCallback;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;


public class NoteFragment extends Fragment implements NoteRecyclerViewAdapter.ItemClickListener {

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
        adapter = new NoteRecyclerViewAdapter();
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return fragmentNoteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        notebookViewModel.getAllNotesFromNotebook(notebookViewModel.getNotebook().getNotebookId())
                .observe(this, new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> notes) {
                        if (notes != null) {
                            adapter.submitList(notes);
                            fragmentNoteBinding.setIsEmpty(false);
                        } else {
                            fragmentNoteBinding.setIsEmpty(true);
                        }
                    }
                });

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


    public void onLongItemClick(View view, int position) {
        Toast.makeText(getContext(), "Click at: " + position, Toast.LENGTH_LONG).show();
    }

    public void onAddNote() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(NoteFragmentDirections.actionNoteFragmentToCreateNotebookDialogFragment().setDialogType("Note"));
    }
}
