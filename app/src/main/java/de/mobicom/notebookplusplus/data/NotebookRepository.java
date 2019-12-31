package de.mobicom.notebookplusplus.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NotebookRepository {

    private NotebookDao notebookDao;
    private LiveData<List<Notebook>> allNotebooks;

    public NotebookRepository(Application application) {
        NotebookDatabase database = NotebookDatabase.getInstance(application);
        notebookDao = database.notebookDao();
        allNotebooks = notebookDao.getAllNotebooks();

    }

    public void insert(Notebook notebook) {
        new InsertNotebookAsyncTask(notebookDao).execute(notebook);
    }

    public void update(Notebook notebook) {
        new UpdateNotebookAsyncTask(notebookDao).execute(notebook);
    }

    public void delete(Notebook notebook) {
        new DeleteNotebookAsyncTask(notebookDao).execute(notebook);
    }

    public void deleteAllNotebooks() {
        new DeleteAllNotebooksAsyncTask(notebookDao).execute();
    }

    public LiveData<List<Notebook>> getAllNotebooks() {
        return allNotebooks;
    }

    private static class InsertNotebookAsyncTask extends AsyncTask<Notebook, Void, Void> {
        private NotebookDao notebookDao;

        private InsertNotebookAsyncTask(NotebookDao notebookDao) {
            this.notebookDao = notebookDao;
        }

        @Override
        protected Void doInBackground(Notebook... notebooks) {
            notebookDao.insert(notebooks[0]);
            return null;
        }
    }

    private static class UpdateNotebookAsyncTask extends AsyncTask<Notebook, Void, Void> {
        private NotebookDao notebookDao;

        private UpdateNotebookAsyncTask(NotebookDao notebookDao) {
            this.notebookDao = notebookDao;
        }

        @Override
        protected Void doInBackground(Notebook... notebooks) {
            notebookDao.update(notebooks[0]);
            return null;
        }
    }

    private static class DeleteNotebookAsyncTask extends AsyncTask<Notebook, Void, Void> {
        private NotebookDao notebookDao;

        private DeleteNotebookAsyncTask(NotebookDao notebookDao) {
            this.notebookDao = notebookDao;
        }

        @Override
        protected Void doInBackground(Notebook... notebooks) {
            notebookDao.delete(notebooks[0]);
            return null;
        }
    }

    private static class DeleteAllNotebooksAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotebookDao notebookDao;

        private DeleteAllNotebooksAsyncTask(NotebookDao notebookDao) {
            this.notebookDao = notebookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            notebookDao.deleteAllNotebooks();
            return null;
        }
    }

//    private List<Notebook> testData() {
//
//        List<Notebook> notebookList = new ArrayList<>();
//        List<Note> noteList = new ArrayList<>();
//        List<Note> noteList2 = new ArrayList<>();
//        Note note1 = new Note(1, "Note 1", R.drawable.ic_note_type_text, "das ist eine Notiz mit einem sehr langen text lalalalaa der is sehr lang und soll nicht komplett angezeigrt werden, denn es ist nur eine Vorschau auf diese Notiz und deshalbg ist das jetzt solange huhuhuhu", Date.valueOf("2019-05-31"));
//        note1.setLastModifiedAt(Date.valueOf("2019-01-01"));
//        noteList.add(note1);
//        noteList.add(new Note(2, "Note 2", R.drawable.ic_note_type_todo, "Das ist eine ToDo Liste", Date.valueOf("2019-07-15")));
//        noteList.add(new Note(3, "Note 3", R.drawable.ic_note_type_speech, "Das ist eine Audionotiz", Date.valueOf("2019-12-31")));
//
//        noteList2.add(note1);
//        noteList2.add(new Note(2, "Notelist2 note 2", R.drawable.ic_note_type_text, "Notelist 2 todo liste", Date.valueOf("2019-07-15")));
//        noteList2.add(new Note(3, "Note 3", R.drawable.ic_note_type_speech, "Audio von notelist 2", Date.valueOf("2019-12-31")));
//
//        notebookList.add(new Notebook(1, "Work", "#f39c12", noteList));
//        notebookList.add(new Notebook(2, "Personal\nStuff", "#3498db", noteList2));
//        notebookList.add(new Notebook(3, "Good\nJokes", "#9b59b6", noteList));
//
//        return notebookList;
//    }
}
