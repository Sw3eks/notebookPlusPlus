package de.mobicom.notebookplusplus.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.data.repository.NoteListItemRepository;
import de.mobicom.notebookplusplus.data.repository.NoteRepository;
import de.mobicom.notebookplusplus.data.model.Notebook;
import de.mobicom.notebookplusplus.data.repository.NotebookRepository;

public class NotebookViewModel extends AndroidViewModel {
    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;
    private NoteListItemRepository noteListItemRepository;
    private LiveData<List<Notebook>> allNotebooks;

    private Notebook selectedNotebook;
    private Note selectedNote;

    public NotebookViewModel(@NonNull Application application) {
        super(application);
        notebookRepository = new NotebookRepository(application);
        noteRepository = new NoteRepository(application);
        noteListItemRepository = new NoteListItemRepository(application);
        allNotebooks = notebookRepository.getAllNotebooks();
    }

    public void insert(Notebook notebook) {
        notebookRepository.insert(notebook);
    }

    public void update(Notebook notebook) {
        notebookRepository.update(notebook);
    }

    public void delete(Notebook notebook) {
        notebookRepository.delete(notebook);
    }

    public void deleteAllNotebooks() {
        notebookRepository.deleteAllNotebooks();
    }

    public LiveData<List<Notebook>> getAllNotebooks() {
        return allNotebooks;
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void deleteAllNotes() {
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotesFromNotebook(long notebookId) {
        return noteRepository.getAllNotes(notebookId);
    }

    public LiveData<List<Note>> getAllNotesWithArchiveTrue() {
        return noteRepository.getAllNotesArchived();
    }

    public LiveData<List<Note>> getAllNotesWithDeletedTrue() {
        return noteRepository.getAllNotesDeleted();
    }

    public void updateNotesOfDeletedNotebook(long notebookId) {
        noteRepository.updateNotesOfDeletedNotebook(notebookId);
    }

    public Notebook getNotebook() {
        return selectedNotebook;
    }

    public void setNotebook(Notebook notebook) {
        selectedNotebook = notebook;
    }

    public Note getNote() {
        return selectedNote;
    }

    public void setNote(Note note) {
        selectedNote = note;
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
}
