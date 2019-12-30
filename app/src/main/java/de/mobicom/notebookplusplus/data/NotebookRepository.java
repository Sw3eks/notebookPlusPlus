package de.mobicom.notebookplusplus.data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import de.mobicom.notebookplusplus.R;

public class NotebookRepository {

    private List<Notebook> notebookList = new ArrayList<>();
    private MutableLiveData<List<Notebook>> mutableLiveData = new MutableLiveData<>();

    public NotebookRepository() {

    }

    public List<Notebook> getNotebookList() {
        return testData();
    }

    public MutableLiveData<List<Notebook>> getMutableLiveData() {
        mutableLiveData.setValue(testData());
        return mutableLiveData;
    }

    private List<Notebook> testData() {

        List<Notebook> notebookList = new ArrayList<>();
        List<Note> noteList = new ArrayList<>();
        List<Note> noteList2 = new ArrayList<>();
        Note note1 = new Note(1, "Note 1", R.drawable.ic_note_type_text, "das ist eine Notiz mit einem sehr langen text lalalalaa der is sehr lang und soll nicht komplett angezeigrt werden, denn es ist nur eine Vorschau auf diese Notiz und deshalbg ist das jetzt solange huhuhuhu", Date.valueOf("2019-05-31"));
        note1.setLastModifiedAt(Date.valueOf("2019-01-01"));
        noteList.add(note1);
        noteList.add(new Note(2, "Note 2", R.drawable.ic_note_type_todo, "Das ist eine ToDo Liste", Date.valueOf("2019-07-15")));
        noteList.add(new Note(3, "Note 3", R.drawable.ic_note_type_speech, "Das ist eine Audionotiz", Date.valueOf("2019-12-31")));

        noteList2.add(note1);
        noteList2.add(new Note(2, "Notelist2 note 2", R.drawable.ic_note_type_text, "Notelist 2 todo liste", Date.valueOf("2019-07-15")));
        noteList2.add(new Note(3, "Note 3", R.drawable.ic_note_type_speech, "Audio von notelist 2", Date.valueOf("2019-12-31")));

        notebookList.add(new Notebook(1, "Work", R.color.colorPeterRiver, noteList));
        notebookList.add(new Notebook(2, "Personal\nStuff", R.color.colorOrange, noteList2));
        notebookList.add(new Notebook(3, "Good\nJokes", R.color.colorAmethyst, noteList));

        return notebookList;
    }
}
