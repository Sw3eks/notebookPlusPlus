package de.mobicom.notebookplusplus.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(Application application) {
        NotebookDatabase database = NotebookDatabase.getInstance(application);
        noteDao = database.noteDao();
    }

    public void insert(Note note) {
        new NoteRepository.InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new NoteRepository.UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void updateNotesOfDeletedNotebook(long notebookId) {
        new NoteRepository.UpdateNotesWithNotebookIdAsyncTask(noteDao).execute(notebookId);
    }

    public void delete(Note note) {
        new NoteRepository.DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new NoteRepository.DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes(long notebookId) {
        try {
            return new SelectNotesAsyncTask(noteDao).execute(notebookId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Note>> getAllNotesArchived() {
        try {
            return new SelectNotesArchivedAsyncTask(noteDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Note>> getAllNotesDeleted() {
        try {
            return new SelectNotesDeletedAsyncTask(noteDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Async Tasks for Note operations
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

    private static class SelectNotesAsyncTask extends AsyncTask<Long, Void, LiveData<List<Note>>> {
        private NoteDao noteDao;

        private SelectNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected LiveData<List<Note>> doInBackground(Long... longs) {
            return noteDao.getAllNotes(longs[0]);
        }
    }

    private static class SelectNotesArchivedAsyncTask extends AsyncTask<Void, Void, LiveData<List<Note>>> {
        private NoteDao noteDao;

        private SelectNotesArchivedAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected LiveData<List<Note>> doInBackground(Void... voids) {
            return noteDao.getAllNotesArchived();
        }
    }

    private static class SelectNotesDeletedAsyncTask extends AsyncTask<Void, Void, LiveData<List<Note>>> {
        private NoteDao noteDao;

        private SelectNotesDeletedAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected LiveData<List<Note>> doInBackground(Void... voids) {
            return noteDao.getAllNotesDeleted();
        }
    }

    private static class UpdateNotesWithNotebookIdAsyncTask extends AsyncTask<Long, Void, Void> {
        private NoteDao noteDao;

        private UpdateNotesWithNotebookIdAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            noteDao.updateNotesWithNotebookId(longs[0]);
            return null;
        }
    }
}
