package de.mobicom.notebookplusplus.view;


import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.adapter.NotebookRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.Notebook;

import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class NotebooksFragment extends Fragment implements NotebookRecyclerViewAdapter.ItemClickListener {

    private static final int DIALOG_FRAGMENT = 1;
    private static final int RESULT_OK = 101;
    private static final String RESULT_KEY = "RESULT_KEY";

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private EditText mEditTextNotebookName;
    private NotebookRecyclerViewAdapter adapter;
    private List<Notebook> notebookList = new ArrayList<>();
    private NotebookViewModel notebookViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notebooks, container, false);

        FloatingActionButton fab = view.findViewById(R.id.addNewNotebook);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog();
                CreateNotebookDialogFragment createNotebookDialogFragment = CreateNotebookDialogFragment.newInstance(getResources().getString(R.string.create_a_new_notebook));
                createNotebookDialogFragment.setTargetFragment(NotebooksFragment.this, DIALOG_FRAGMENT);
                if (getFragmentManager() != null) {
                    createNotebookDialogFragment.show(getFragmentManager(), "CreateNotebookDialog");
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextNotebookName = view.findViewById(R.id.edit_text_new_notebook);
        //TextView testTextView = view.findViewById(R.id.testTextView);
        //notebookViewModel.setName(testTextView.getText().toString());

        List<Note> noteList = new ArrayList<>();
        List<Note> noteList2 = new ArrayList<>();
        Note note1 = new Note(1, "Note 1", "text", "das ist eine Notiz mit einem sehr langen text lalalalaa der is sehr lang und soll nicht komplett angezeigrt werden, denn es ist nur eine Vorschau auf diese Notiz und deshalbg ist das jetzt solange huhuhuhu", Date.valueOf("2019-05-31"));
        note1.setLastModifiedAt(Date.valueOf("2019-01-01"));
        noteList.add(note1);
        noteList.add(new Note(2, "Note 2", "todo", "Das ist eine ToDo Liste", Date.valueOf("2019-07-15")));
        noteList.add(new Note(3, "Note 3", "speech", "Das ist eine Audionotiz", Date.valueOf("2019-12-31")));

        noteList2.add(note1);
        noteList2.add(new Note(2, "Notelist2 note 2", "todo", "Notelist 2 todo liste", Date.valueOf("2019-07-15")));
        noteList2.add(new Note(3, "Note 3", "speech", "Audio von notelist 2", Date.valueOf("2019-12-31")));

        notebookList.add(new Notebook(1, "Work", "#3498db", noteList));
        notebookList.add(new Notebook(2, "Personal\nStuff", "#f39c12", noteList2));
        notebookList.add(new Notebook(3, "Good\nJokes", "#e74c3c", noteList));

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rvNotebooks);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new NotebookRecyclerViewAdapter(getActivity(), notebookList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notebooks, menu);

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

    private void save(String fileName) {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(getActivity().openFileOutput(fileName, Context.MODE_PRIVATE));
            out.write(mEditTextNotebookName.getText().toString());
            out.close();
            Toast.makeText(getContext(), "Note saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(getContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);

        notebookViewModel.setNotebook(adapter.getItem(position));

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoteFragment()).commit();
    }

    private void showDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create_notebook, null))
                .setTitle(R.string.create_a_new_notebook)
                .setPositiveButton(R.string.create_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                NotebooksFragment frag = (NotebooksFragment) getTargetFragment();
                                frag.save(mEditTextNotebookName.toString());
                            }
                        }
                )
                .setNegativeButton(R.string.cancel_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_notebook, null);

        Spinner spinner = view.findViewById(R.id.colorDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.color_Array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b.setView(view);

        final AlertDialog dialog = b.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String mEditTextNotebookName = data.getStringExtra(
                    RESULT_KEY);
            save(mEditTextNotebookName);
        }

    }
}
