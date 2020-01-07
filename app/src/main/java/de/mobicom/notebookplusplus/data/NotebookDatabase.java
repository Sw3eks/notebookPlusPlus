package de.mobicom.notebookplusplus.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import de.mobicom.notebookplusplus.data.dao.NoteDao;
import de.mobicom.notebookplusplus.data.dao.NoteListItemDao;
import de.mobicom.notebookplusplus.data.dao.NotebookDao;
import de.mobicom.notebookplusplus.data.model.Note;
import de.mobicom.notebookplusplus.data.model.NoteListItem;
import de.mobicom.notebookplusplus.data.model.Notebook;
import de.mobicom.notebookplusplus.utils.LocalDateTimeConverter;

@Database(entities = {Notebook.class, Note.class, NoteListItem.class}, version = 6, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class NotebookDatabase extends RoomDatabase {

    private static NotebookDatabase instance;

    public abstract NotebookDao notebookDao();

    public abstract NoteDao noteDao();

    public abstract NoteListItemDao noteListItemDao();

    public static synchronized NotebookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotebookDatabase.class, "notebook_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
