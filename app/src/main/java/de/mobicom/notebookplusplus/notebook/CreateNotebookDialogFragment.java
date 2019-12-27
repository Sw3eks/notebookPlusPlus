package de.mobicom.notebookplusplus.notebook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import de.mobicom.notebookplusplus.R;

public class CreateNotebookDialogFragment extends DialogFragment {

    private EditText mEditTextNotebookName;
    Button positiveButton;

    public CreateNotebookDialogFragment() {
    }

    static CreateNotebookDialogFragment newInstance(String title) {
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        AlertDialog d = (AlertDialog) getDialog();
//        if (d != null) {
//            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
//            positiveButton.setEnabled(false);
//        }
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditTextNotebookName = view.findViewById(R.id.edit_text_new_notebook);
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
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.create_notebook_dialog, null))
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

        View view = getActivity().getLayoutInflater().inflate(R.layout.create_notebook_dialog, null);

        Spinner spinner = view.findViewById(R.id.colorDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.color_Array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b.setView(view);
        return b.create();
    }

}
