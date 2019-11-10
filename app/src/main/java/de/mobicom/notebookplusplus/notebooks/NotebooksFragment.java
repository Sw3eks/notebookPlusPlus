package de.mobicom.notebookplusplus.notebooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import de.mobicom.notebookplusplus.R;

public class NotebooksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notebooks, container, false);

        FloatingActionButton fab = view.findViewById(R.id.addNewNotebook);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                CreateNotebookDialogFragment createNotebookDialogFragment = CreateNotebookDialogFragment.newInstance("Test");
                createNotebookDialogFragment.show(manager, "CreateNotebookDialog");
            }
        });

        return view;
    }

}
