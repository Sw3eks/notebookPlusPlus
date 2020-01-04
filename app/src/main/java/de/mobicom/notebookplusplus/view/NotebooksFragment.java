package de.mobicom.notebookplusplus.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.databinding.FragmentNotebooksBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.adapter.NotebookRecyclerViewAdapter;
import de.mobicom.notebookplusplus.data.Notebook;

import java.util.List;

public class NotebooksFragment extends Fragment implements NotebookRecyclerViewAdapter.ItemClickListener {
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
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
        notebookViewModel.getAllNotebooks().observe(this, new Observer<List<Notebook>>() {
            @Override
            public void onChanged(List<Notebook> notebooks) {
                if (notebooks != null) {
                    fragmentNotebooksBinding.setIsEmpty(false);
                    adapter.submitList(notebooks);
                } else {
                    fragmentNotebooksBinding.setIsEmpty(true);
                }
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

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(View view, int position) {
        notebookViewModel.setNotebook(adapter.getNotebookAt(position));

        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_notebooksFragment_to_noteFragment);
    }

    @Override
    public void onContextMenuItemClick(MenuItem item, int position) {
        switch (item.getItemId()) {
            case R.string.notebook_change_name:
                Toast.makeText(getContext(), "Name", Toast.LENGTH_LONG).show();
                break;
            case R.string.notebook_change_color:
                Toast.makeText(getContext(), "Color", Toast.LENGTH_LONG).show();
                break;
            case R.string.notebook_delete:
                notebookViewModel.updateNotesOfDeletedNotebook(adapter.getNotebookAt(position).getNotebookId());
                notebookViewModel.delete(adapter.getNotebookAt(position));
                Toast.makeText(getContext(), R.string.notebook_deleted, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void onAddNotebook() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(NotebooksFragmentDirections.actionNotebooksFragmentToCreateNotebookDialogFragment().setDialogType("Notebook"));
    }
}
