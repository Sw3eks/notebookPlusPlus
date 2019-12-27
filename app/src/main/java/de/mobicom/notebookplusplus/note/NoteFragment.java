package de.mobicom.notebookplusplus.note;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.NotebookViewModel;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.note.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.note.model.Note;
import de.mobicom.notebookplusplus.notebook.model.Notebook;


public class NoteFragment extends Fragment implements NoteRecyclerViewAdapter.ItemClickListener {

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private NoteRecyclerViewAdapter adapter;
    private FloatingActionButton fab;
    private List<Note> noteList = new ArrayList<>();
    private NotebookViewModel notebookViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }
        setHasOptionsMenu(true);

        fab = rootView.findViewById(R.id.addNewNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);

        final Observer<Notebook> notebookObserver = new Observer<Notebook>() {
            @Override
            public void onChanged(@Nullable final Notebook notebook) {
                if (notebook != null) {
                    noteList.addAll(notebook.getNotes());
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        notebookViewModel.getNotebook().observe(this, notebookObserver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // mEditTextNoteName = view.findViewById(R.id.edit_text_new_notebook);

        RecyclerView recyclerView = view.findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        adapter = new NoteRecyclerViewAdapter(getActivity(), noteList);
        adapter.setClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    adapter.filter(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    adapter.filter(query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);

        notebookViewModel.setNoteId(position);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoteEditorFragment()).commit();
    }

    private void showDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create_note, null))
                .setTitle(R.string.create_new_note)
                .setPositiveButton(R.string.create_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                // DO TASK
                            }
                        })
                .setNegativeButton(R.string.cancel_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_note, null);

        Spinner spinner = view.findViewById(R.id.typeDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.note_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        EditText mEditTextNoteTitle = getActivity().findViewById(R.id.edit_text_new_note);
//        mEditTextNoteTitle.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (TextUtils.isEmpty(s)) {
//
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//
//                } else {
//
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                }
//
//            }
//        });
    }

}
