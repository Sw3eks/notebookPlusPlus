package de.mobicom.notebookplusplus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import de.mobicom.notebookplusplus.notebook.model.Notebook;

public class NotebookViewModel extends ViewModel {

    private MutableLiveData<Notebook> selectedNotebook = new MutableLiveData<>();
    private int noteId;

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

}
