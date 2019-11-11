package de.mobicom.notebookplusplus.notebooks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import de.mobicom.notebookplusplus.R;

public class NotebooksFragment extends Fragment {

    private EditText mEditTextNotebookName;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextNotebookName = view.findViewById(R.id.edit_text_new_notebook);
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

}
