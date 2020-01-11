package de.mobicom.notebookplusplus.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import androidx.lifecycle.ViewModelProviders;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.data.model.Notebook;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;

/**
 * Dialog Fragment for different use cases to create/edit notebooks and notes
 */
public class CreateDialogFragment extends DialogFragment {

    private NotebookViewModel notebookViewModel;
    private EditText editText;
    private Spinner spinner;
    private Button positiveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_create, container);
    }

    /**
     * initially disables the create button in the create new notebook/note dialog
     */
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            if (CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType().equals(NotebookFragment.NOTEBOOK_FRAGMENT) ||
                    CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType().equals(NoteFragment.NOTE_FRAGMENT)) {
                positiveButton.setEnabled(false);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Creates different dialogs depending on then navigation source fragment
     *
     * @param savedInstanceState saved instance state
     * @return the dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog;
        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);

        if (CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType().equals(NotebookFragment.NOTEBOOK_FRAGMENT) ||
                CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType().equals(NotebookFragment.NOTEBOOK_FRAGMENT_EDIT)) {
            dialog = createNotebookDialog(CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType());
        } else if (CreateDialogFragmentArgs.fromBundle(getArguments()).getDialogType().equals(NoteFragment.NOTE_FRAGMENT)) {
            dialog = createNoteDialog();
        } else {
            dialog = createNoteEditorDialog();
        }

        return dialog;
    }

    /**
     * dialog to change the title of a note
     *
     * @return dialog
     */
    private Dialog createNoteEditorDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create, null))
                .setTitle(R.string.edit_note_title)
                .setPositiveButton(R.string.save_button,
                        (dialog, arg1) -> {
                            notebookViewModel.getNote().setName(editText.getText().toString().trim());
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notebookViewModel.getNote().getName());

                        })
                .setNegativeButton(R.string.cancel_button,
                        (dialog, arg1) -> dialog.dismiss());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create, null);

        editText = view.findViewById(R.id.dialogEditName);
        editText.requestFocus();
        editText.setText(notebookViewModel.getNote().getName());
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString().trim())) {

                    positiveButton.setEnabled(false);

                } else {

                    positiveButton.setEnabled(true);
                }

            }
        });
        view.findViewById(R.id.dialogDropdown).

                setVisibility(View.GONE);
        view.findViewById(R.id.dialogDropdownLabel).

                setVisibility(View.GONE);

        b.setView(view);

        return b.create();
    }

    /**
     * dialog to create a new note, contains
     * edittext: user input for note title
     * spinner: user input for note type
     *
     * @return dialog
     */
    private Dialog createNoteDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create, null))
                .setTitle(R.string.create_new_note)
                .setPositiveButton(R.string.create_button,
                        (dialog, arg1) -> {
                            int type;
                            switch (spinner.getSelectedItem().toString()) {
                                case "List":
                                    type = R.drawable.ic_note_type_todo;
                                    break;
                                case "Speech":
                                    type = R.drawable.ic_note_type_speech;
                                    break;
                                default:
                                    type = R.drawable.ic_note_type_text;
                            }
                            notebookViewModel.insert(
                                    new Note(notebookViewModel.getNotebook().getNotebookId(),
                                            editText.getText().toString(),
                                            type, getResources().getString(R.string.created_note_default_text)));
                            Toast.makeText(getContext(), R.string.note_created, Toast.LENGTH_LONG).show();
                        })
                .setNegativeButton(R.string.cancel_button,
                        (dialog, arg1) -> dialog.dismiss());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create, null);

        editText = view.findViewById(R.id.dialogEditName);
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString().trim())) {

                    positiveButton.setEnabled(false);

                } else {

                    positiveButton.setEnabled(true);
                }

            }
        });

        TextView spinnerLabel = view.findViewById(R.id.dialogDropdownLabel);
        spinnerLabel.setText(R.string.type_label);
        spinner = view.findViewById(R.id.dialogDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.note_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b.setView(view);

        return b.create();

    }

    /**
     * dialog to create or edit a notebook, contains
     * edittext: user input for notebook title
     * spinner: user input for notebook color
     *
     * @param type type to determine whether to create or edit a notebook
     * @return dialog
     */
    private AlertDialog createNotebookDialog(String type) {
        String buttonLabel;
        String title;
        if (type.equals(NotebookFragment.NOTEBOOK_FRAGMENT)) {
            title = getResources().getString(R.string.create_a_new_notebook);
            buttonLabel = getResources().getString(R.string.create_button);
        } else {
            title = getResources().getString(R.string.edit_notebook);
            buttonLabel = getResources().getString(R.string.save_button);
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create, null))
                .setTitle(title)
                .setPositiveButton(buttonLabel,
                        (dialog, whichButton) -> {
                            String color;
                            switch (spinner.getSelectedItem().toString()) {
                                case "Blue":
                                    color = "#3498db";
                                    break;
                                case "Red":
                                    color = "#e74c3c";
                                    break;
                                case "Purple":
                                    color = "#9b59b6";
                                    break;
                                case "Green":
                                    color = "#2ecc71";
                                    break;
                                case "Dark Grey":
                                    color = "#34495e";
                                    break;
                                case "Yellow":
                                    color = "#f1c40f";
                                    break;
                                default:
                                    color = "#f39c12";
                            }
                            if (type.equals(NotebookFragment.NOTEBOOK_FRAGMENT)) {
                                notebookViewModel.insert(
                                        new Notebook(editText.getText().toString(), color));
                            } else {
                                Notebook notebook = notebookViewModel.getNotebook();
                                notebook.setName(editText.getText().toString());
                                notebook.setColor(color);
                                notebookViewModel.update(notebook);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel_button,
                        (dialog, whichButton) -> dialog.dismiss()
                );

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create, null);

        editText = view.findViewById(R.id.dialogEditName);
        if (type.equals(NotebookFragment.NOTEBOOK_FRAGMENT_EDIT)) {
            editText.setText(notebookViewModel.getNotebook().getName());
        }
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s.toString().trim())) {

                    positiveButton.setEnabled(false);

                } else {

                    positiveButton.setEnabled(true);
                }

            }
        });

        spinner = view.findViewById(R.id.dialogDropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.color_Array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        b.setView(view);
        return b.create();
    }
}
