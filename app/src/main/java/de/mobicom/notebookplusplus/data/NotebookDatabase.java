package de.mobicom.notebookplusplus.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import de.mobicom.notebookplusplus.utils.LocalDateTimeConverter;

@Database(entities = {Notebook.class, Note.class}, version = 5, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class NotebookDatabase extends RoomDatabase {

    private static NotebookDatabase instance;

    public abstract NotebookDao notebookDao();

    public abstract NoteDao noteDao();

    public static synchronized NotebookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotebookDatabase.class, "notebook_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NotebookDao notebookDao;
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NotebookDatabase db) {
            notebookDao = db.notebookDao();
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            noteDao.insert(new Note(1, "Note 1", R.drawable.ic_note_type_text, "Note von Notebook 1"));
//            noteDao.insert(new Note(1, "Note 2", R.drawable.ic_note_type_text, "Text für Notebook 1"));
//            noteDao.insert(new Note(1, "Note 3", R.drawable.ic_note_type_speech, "Das ist ein Text"));
//            noteDao.insert(new Note(2, "Note 4", R.drawable.ic_note_type_text, "Das ist ein Text"));
//            noteDao.insert(new Note(2, "Note 5", R.drawable.ic_note_type_todo, "Das ist ein Text"));
//            noteDao.insert(new Note(3, "Note 6", R.drawable.ic_note_type_todo, "Das ist eine Liste"));
//            noteDao.insert(new Note(3, "Note 7", R.drawable.ic_note_type_speech, "Das ist eine Audionotiz"));
//
//            notebookDao.insert(new Notebook("Work", 1, "#f39c12"));
//            notebookDao.insert(new Notebook("Personal\nStuff", 3, "#3498db"));
//            notebookDao.insert(new Notebook("Good\nJokes", 2, "#9b59b6"));
            return null;
        }
    }
}
