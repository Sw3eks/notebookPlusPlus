package de.mobicom.notebookplusplus.notebook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.note.NoteActivity;
import de.mobicom.notebookplusplus.note.NoteFragment;
import de.mobicom.notebookplusplus.notebook.adapter.NotebookRecyclerViewAdapter;
import de.mobicom.notebookplusplus.notebook.model.Notebook;
import de.mobicom.notebookplusplus.settings.SettingsActivity;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class NotebooksFragment extends Fragment implements NotebookRecyclerViewAdapter.ItemClickListener {

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private EditText mEditTextNotebookName;
    private NotebookRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notebooks, container, false);

        FloatingActionButton fab = view.findViewById(R.id.addNewNotebook);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                CreateNotebookDialogFragment createNotebookDialogFragment = CreateNotebookDialogFragment.newInstance(getResources().getString(R.string.create_a_new_notebook));
                createNotebookDialogFragment.show(manager, "CreateNotebookDialog");
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextNotebookName = view.findViewById(R.id.edit_text_new_notebook);

        List<Notebook> notebookList = new ArrayList<>();
        notebookList.add(new Notebook(1, "Work", "#99182e", null));
        notebookList.add(new Notebook(2, "Personal\nStuff", "#447ac4", null));
        notebookList.add(new Notebook(3, "Good\nJokes", "#447825", null));

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

    public void save(String fileName) {
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
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoteFragment()).commit();

        //Intent intent = new Intent(getContext(), NoteActivity.class);
        //getContext().startActivity(intent);
    }

}
