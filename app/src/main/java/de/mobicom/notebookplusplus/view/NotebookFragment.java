package de.mobicom.notebookplusplus.view;

import android.content.res.Configuration;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NotebookRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.model.Notebook;
import de.mobicom.notebookplusplus.databinding.FragmentNotebooksBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

public class NotebookFragment extends Fragment implements NotebookRecyclerViewAdapter.ItemClickListener {
    public static final String NOTEBOOK_FRAGMENT = NotebookFragment.class.getSimpleName();
    public static final String NOTEBOOK_FRAGMENT_EDIT = "NOTEBOOK_EDIT_DIALOG";

    private NotebookRecyclerViewAdapter adapter;
    private NotebookViewModel notebookViewModel;
    private FragmentNotebooksBinding fragmentNotebooksBinding;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNotebooksBinding = FragmentNotebooksBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        fragmentNotebooksBinding.setHandler(this);

        recyclerView = fragmentNotebooksBinding.rvNotebooks;
        int numberOfColumns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            numberOfColumns = 2;
        } else {
            numberOfColumns = 3;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setHasFixedSize(true);
        adapter = new NotebookRecyclerViewAdapter();
        adapter.setClickListener(this);
        adapter.setContextMenuItemClickListener(this);
        recyclerView.setAdapter(adapter);

        return fragmentNotebooksBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(recyclerView);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        notebookViewModel.getAllNotebooks().observe(this, notebooks -> {
            if (!notebooks.isEmpty()) {
                fragmentNotebooksBinding.setIsEmpty(false);
                adapter.submitList(notebooks);
            } else {
                fragmentNotebooksBinding.setIsEmpty(true);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notebooks, menu);

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

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(final MenuItem item) {
                menu.findItem(R.id.to_calendar).setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(final MenuItem item) {
                menu.findItem(R.id.to_calendar).setVisible(true);
                return true;
            }
        });

        MenuItem toCalendar = menu.findItem(R.id.to_calendar);
        toCalendar.setOnMenuItemClickListener(item -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_notebooksFragment_to_calendarFragment);
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(View view, int position) {
        notebookViewModel.setNotebook(adapter.getNotebookAt(position));

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_notebooksFragment_to_noteFragment);
    }

    @Override
    public void onContextMenuItemClick(MenuItem item, int position) {
        notebookViewModel.setNotebook(adapter.getNotebookAt(position));
        switch (item.getItemId()) {
            case R.string.edit_notebook:
                Navigation.findNavController(
                        getActivity(), R.id.nav_host_fragment).navigate(NotebookFragmentDirections.actionNotebooksFragmentToCreateNotebookDialogFragment().setDialogType(NOTEBOOK_FRAGMENT_EDIT));
                break;
            case R.string.notebook_delete:
                notebookViewModel.updateNotesOfDeletedNotebook(adapter.getNotebookAt(position).getNotebookId());
                Notebook notebook = notebookViewModel.getNotebook();
                notebook.setMarkedForDelete(true);
                notebookViewModel.update(notebook);
                Toast.makeText(getContext(), R.string.notebook_deleted, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void onAddNotebook() {
        System.out.println(NOTEBOOK_FRAGMENT);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(
                NotebookFragmentDirections.actionNotebooksFragmentToCreateNotebookDialogFragment().setDialogType(NOTEBOOK_FRAGMENT));
    }
}
