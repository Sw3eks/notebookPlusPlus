package de.mobicom.notebookplusplus.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.data.NotebookRepository;

public class NotebookViewModel extends ViewModel {

    private NotebookRepository notebookRepository;

    private MutableLiveData<List<Note>> noteList = new MutableLiveData<>();

    private MutableLiveData<Notebook> selectedNotebook = new MutableLiveData<>();
    private MutableLiveData<Note> selectedNote = new MutableLiveData<>();

    public NotebookViewModel() {
        notebookRepository = new NotebookRepository();
    }

    public LiveData<List<Notebook>> getNotebookList() {
        return notebookRepository.getMutableLiveData();
    }

    public LiveData<List<Note>> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList.setValue(noteList);
    }

    public LiveData<Notebook> getNotebook() {
        return selectedNotebook;
    }

    public void setNotebook(Notebook notebook) {
        selectedNotebook.setValue(notebook);
    }

    public LiveData<Note> getNote() {
        return selectedNote;
    }

    public void setNote(Note note) {
        selectedNote.setValue(note);
    }
}
