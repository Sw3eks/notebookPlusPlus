package de.mobicom.notebookplusplus.notebooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import de.mobicom.notebookplusplus.R;

public class CreateNotebookDialogFragment extends DialogFragment {

    private EditText mEditTextNotebookName;

    public CreateNotebookDialogFragment() {
    }

    public static CreateNotebookDialogFragment newInstance(String title) {
        CreateNotebookDialogFragment frag = new CreateNotebookDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_notebook_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditTextNotebookName = (EditText) view.findViewById(R.id.edit_text_new_notebook);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        mEditTextNotebookName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
