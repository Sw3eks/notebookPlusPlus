package de.mobicom.notebookplusplus.view;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.databinding.FragmentNoteBinding;
import de.mobicom.notebookplusplus.utils.SimpleItemTouchHelperCallback;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.Notebook;


public class NoteFragment extends Fragment implements NoteRecyclerViewAdapter.ItemClickListener {
    public static final String TAG = "NoteFragmentTag";

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private NoteRecyclerViewAdapter adapter;
    private NotebookViewModel notebookViewModel;
    private FragmentNoteBinding fragmentNoteBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNoteBinding = FragmentNoteBinding.inflate(inflater, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setHasOptionsMenu(true);

        fragmentNoteBinding.setHandler(this);

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

        notebookViewModel.getNotebook().observe(this, new Observer<Notebook>() {
            @Override
            public void onChanged(Notebook notebook) {
                if (notebook != null) {
                    //adapter.setNoteList(notebook.getNotes());
                    fragmentNoteBinding.setIsEmpty(false);
                } else {
                    fragmentNoteBinding.setIsEmpty(true);
                }
            }
        });
//        final Observer<Notebook> notebookObserver = new Observer<Notebook>() {
//            @Override
//            public void onChanged(@Nullable final Notebook notebook) {
//                if (notebook != null) {
//                    adapter.setNoteList(notebook.getNotes());
//                }
//            }
//        };

        //notebookViewModel.getNotebook().observe(getViewLifecycleOwner(), notebookObserver);
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

        notebookViewModel.setNote(adapter.getItem(position));

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NoteEditorFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onLongItemClick(View view, int position) {
        Toast.makeText(getContext(), "Click at: " + position, Toast.LENGTH_LONG).show();
    }

    public void onAddNote() {
        showDialog();
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
