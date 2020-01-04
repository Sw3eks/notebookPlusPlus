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
}
