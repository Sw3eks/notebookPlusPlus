package de.mobicom.notebookplusplus.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import de.mobicom.notebookplusplus.databinding.FragmentNoteEditorBinding;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;

public class NoteEditorFragment extends Fragment {

    private NotebookViewModel notebookViewModel;
    private FragmentNoteEditorBinding fragmentNoteEditorBinding;

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
                //TODO: Save changes of notes in the correct note/notebook
                //selectedNote.setDescription(s.toString());
                //notebookViewModel.getNote().setValue(selectedNote);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
            case R.id.moveToArchive:
                Toast.makeText(getContext(), "Archived", Toast.LENGTH_LONG).show();
                break;
            case R.id.deleteNote:
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }
}
