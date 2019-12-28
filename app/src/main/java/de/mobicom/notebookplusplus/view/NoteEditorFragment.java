package de.mobicom.notebookplusplus.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.mobicom.notebookplusplus.viewmodel.NotebookViewModel;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.data.Note;

public class NoteEditorFragment extends Fragment {

    private NotebookViewModel notebookViewModel;
    private EditText mEditTextEditNote;
    private Note selectedNote;
    private String mTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);

        mEditTextEditNote = view.findViewById(R.id.editNote);
        mEditTextEditNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selectedNote.setDescription(s.toString());
                notebookViewModel.getNote().setValue(selectedNote);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        notebookViewModel = ViewModelProviders.of(requireActivity()).get(NotebookViewModel.class);

        final Observer<Note> noteObserver = new Observer<Note>() {
            @Override
            public void onChanged(@Nullable final Note note) {
                if (note != null) {
                    selectedNote = note;
                    mEditTextEditNote.setText(note.getDescription());
                    //mTitle = note.getName();
                }
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        notebookViewModel.getNote().observe(getViewLifecycleOwner(), noteObserver);
        mEditTextEditNote.setText(selectedNote.getDescription());

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
