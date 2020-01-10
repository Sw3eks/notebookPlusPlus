package de.mobicom.notebookplusplus.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import de.mobicom.notebookplusplus.data.NotebookDatabase;
import de.mobicom.notebookplusplus.data.dao.NoteDao;
import de.mobicom.notebookplusplus.data.model.Note;

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
        return noteDao.getAllNotes(notebookId);
    }

    // LiveData is executed off the main thread by default, no AsyncTask needed.
    public LiveData<List<Note>> getAllNotesArchived() {
        return noteDao.getAllNotesArchived();
    }

    // LiveData is executed off the main thread by default, no AsyncTask needed.
    public LiveData<List<Note>> getAllNotesDeleted() {
        return noteDao.getAllNotesDeleted();
    }

    public List<Note> getAllNotesNotificationEnabled() {
        try {
            return new SelectNotesAsyncTask(noteDao).execute().get();
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

    private static class SelectNotesAsyncTask extends AsyncTask<Void, Void, List<Note>> {
        private NoteDao noteDao;

        private SelectNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return noteDao.getAllNotesNotificationEnabled();
        }
    }
}
