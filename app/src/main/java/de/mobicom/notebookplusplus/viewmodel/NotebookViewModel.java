package de.mobicom.notebookplusplus.viewmodel;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.mobicom.notebookplusplus.data.Note;
import de.mobicom.notebookplusplus.data.Notebook;
import de.mobicom.notebookplusplus.data.NotebookRepository;

public class NotebookViewModel extends ViewModel {

    private NotebookRepository notebookRepository;
    private MutableLiveData<List<Notebook>> notebookList = new MutableLiveData<>();
    private MutableLiveData<Notebook> selectedNotebook = new MutableLiveData<>();
    private MutableLiveData<Note> selectedNote = new MutableLiveData<>();
    private int noteId;

    public NotebookViewModel() {
        notebookRepository = new NotebookRepository();
        loadNotebooks();
    }

    private void loadNotebooks() {
        notebookList.setValue(notebookRepository.getNotebookList());
    }

    public MutableLiveData<List<Notebook>> getNotebookList() {
        return notebookList;
    }

    public void setNotebookList(List<Notebook> notebookList) {
        this.notebookList.setValue(notebookList);
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public MutableLiveData<Notebook> getNotebook() {
        return selectedNotebook;
    }

    public void setNotebook(Notebook notebook) {
        selectedNotebook.setValue(notebook);
    }

    public MutableLiveData<Note> getNote() {
        return selectedNote;
    }

    public void setNote(Note note) {
        selectedNote.setValue(note);
    }
}
