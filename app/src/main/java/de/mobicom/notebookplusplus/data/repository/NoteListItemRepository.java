package de.mobicom.notebookplusplus.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import de.mobicom.notebookplusplus.data.NotebookDatabase;
import de.mobicom.notebookplusplus.data.dao.NoteListItemDao;
import de.mobicom.notebookplusplus.data.model.NoteListItem;

public class NoteListItemRepository {
    private NoteListItemDao noteListItemDao;

    public NoteListItemRepository(Application application) {
        NotebookDatabase database = NotebookDatabase.getInstance(application);
        noteListItemDao = database.noteListItemDao();
    }

    public void insert(NoteListItem noteListItem) {
        new InsertNoteListItemAsyncTask(noteListItemDao).execute(noteListItem);
    }

    public void update(NoteListItem noteListItem) {
        new UpdateNoteListItemAsyncTask(noteListItemDao).execute(noteListItem);
    }

    public void delete(NoteListItem noteListItem) {
        new DeleteNoteListItemAsyncTask(noteListItemDao).execute(noteListItem);
    }

    public LiveData<List<NoteListItem>> getAllNoteListItems(long notebookId) {
        try {
            return new SelectNoteListItemsAsyncTask(noteListItemDao).execute(notebookId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Async Tasks
    private static class InsertNoteListItemAsyncTask extends AsyncTask<NoteListItem, Void, Void> {
        private NoteListItemDao noteListItemDao;

        private InsertNoteListItemAsyncTask(NoteListItemDao noteListItemDao) {
            this.noteListItemDao = noteListItemDao;
        }

        @Override
        protected Void doInBackground(NoteListItem... items) {
            noteListItemDao.insert(items[0]);
            return null;
        }
    }

    private static class UpdateNoteListItemAsyncTask extends AsyncTask<NoteListItem, Void, Void> {
        private NoteListItemDao noteListItemDao;

        private UpdateNoteListItemAsyncTask(NoteListItemDao noteListItemDao) {
            this.noteListItemDao = noteListItemDao;
        }

        @Override
        protected Void doInBackground(NoteListItem... items) {
            noteListItemDao.update(items[0]);
            return null;
        }
    }

    private static class DeleteNoteListItemAsyncTask extends AsyncTask<NoteListItem, Void, Void> {
        private NoteListItemDao noteListItemDao;

        private DeleteNoteListItemAsyncTask(NoteListItemDao noteListItemDao) {
            this.noteListItemDao = noteListItemDao;
        }

        @Override
        protected Void doInBackground(NoteListItem... items) {
            noteListItemDao.delete(items[0]);
            return null;
        }
    }


    private static class SelectNoteListItemsAsyncTask extends AsyncTask<Long, Void, LiveData<List<NoteListItem>>> {
        private NoteListItemDao noteListItemDao;

        private SelectNoteListItemsAsyncTask(NoteListItemDao noteListItemDao) {
            this.noteListItemDao = noteListItemDao;
        }

        @Override
        protected LiveData<List<NoteListItem>> doInBackground(Long... longs) {
            return noteListItemDao.getAllNoteListItems(longs[0]);
        }
    }

}