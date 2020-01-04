package de.mobicom.notebookplusplus.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.data.NoteRepository;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.data.NotebookRepository;

public class NotebookViewModel extends AndroidViewModel {
    private NotebookRepository notebookRepository;
    private NoteRepository noteRepository;
    private LiveData<List<Notebook>> allNotebooks;

    private Notebook selectedNotebook;
    private Note selectedNote;

    public NotebookViewModel(@NonNull Application application) {
        super(application);
        notebookRepository = new NotebookRepository(application);
        noteRepository = new NoteRepository(application);
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
}
