package de.mobicom.notebookplusplus.data;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

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

    public void delete(Note note) {
        new NoteRepository.DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new NoteRepository.DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes(long notebookId) {
        return noteDao.getAllNotes(notebookId);
    }

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

    private static class SelectNotesAsyncTask extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            return null;
        }
    }
}
