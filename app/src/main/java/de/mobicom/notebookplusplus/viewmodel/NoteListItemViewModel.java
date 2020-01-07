package de.mobicom.notebookplusplus.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.data.NoteListItemRepository;

public class NoteListItemViewModel extends AndroidViewModel {

    private NoteListItemRepository noteListItemRepository;
    private Note selectedNote;

    public NoteListItemViewModel(@NonNull Application application) {
        super(application);
        noteListItemRepository = new NoteListItemRepository(application);
    }

    public void insert(NoteListItem noteListItem) {
        noteListItemRepository.insert(noteListItem);
    }

    public void update(NoteListItem noteListItem) {
        noteListItemRepository.update(noteListItem);
    }

    public void delete(NoteListItem noteListItem) {
        noteListItemRepository.delete(noteListItem);
    }

    public LiveData<List<NoteListItem>> getAllNoteListItems(long noteId) {
        return noteListItemRepository.getAllNoteListItems(noteId);
    }

    public Note getNote() {
        return selectedNote;
    }

    public void setNote(Note note) {
        selectedNote = note;
    }
}
