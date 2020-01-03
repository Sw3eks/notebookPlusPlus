package de.mobicom.notebookplusplus.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.databinding.FragmentNoteEditorBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;

public class NoteEditorFragment extends Fragment {

    private NotebookViewModel notebookViewModel;
    private FragmentNoteEditorBinding fragmentNoteEditorBinding;

    private EditText mEditTextNoteTitle;
    private Button positiveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentNoteEditorBinding = FragmentNoteEditorBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        fragmentNoteEditorBinding.editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                notebookViewModel.getNote().setDescription(s.toString().trim());
            }
        });

        return fragmentNoteEditorBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);
        fragmentNoteEditorBinding.setNote(notebookViewModel.getNote());

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(notebookViewModel.getNote().getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_editor, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.editNoteTitle:
                showDialog();
                break;
            case R.id.moveToArchive:
                Toast.makeText(getContext(), "Archived", Toast.LENGTH_LONG).show();
                break;
            case R.id.deleteNote:
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    private void showDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme)
                .setView(inflater.inflate(R.layout.dialog_create_note, null))
                .setTitle(R.string.edit_note_title)
                .setPositiveButton(R.string.save_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                notebookViewModel.getNote().setName(mEditTextNoteTitle.getText().toString().trim());
                            }
                        })
                .setNegativeButton(R.string.cancel_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        });

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_note, null);

        mEditTextNoteTitle = view.findViewById(R.id.edit_text_new_note);
        mEditTextNoteTitle.requestFocus();
        mEditTextNoteTitle.addTextChangedListener(new TextWatcher() {

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
        view.findViewById(R.id.typeDropdown).setVisibility(View.GONE);
        view.findViewById(R.id.labelTypeDropdown).setVisibility(View.GONE);

        builder.setView(view);

        final AlertDialog dialog = builder.create();

        positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);

        dialog.show();
        positiveButton.setEnabled(false);
    }

    @Override
    public void onDetach() {
        notebookViewModel.update(notebookViewModel.getNote());
        super.onDetach();
    }
}
