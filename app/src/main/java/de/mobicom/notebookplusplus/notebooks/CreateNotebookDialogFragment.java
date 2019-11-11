package de.mobicom.notebookplusplus.notebooks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.create_notebook_dialog, null))
                .setTitle(R.string.create_a_new_notebook)
                .setPositiveButton(R.string.create_notebook_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                NotebooksFragment frag = (NotebooksFragment) getTargetFragment();
                                frag.save(mEditTextNotebookName.toString());
                            }
                        }
                )
                .setNegativeButton(R.string.cancel_notebook_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View view = getActivity().getLayoutInflater().inflate(R.layout.create_notebook_dialog, null);


        b.setView(view);
        return b.create();
    }

}
