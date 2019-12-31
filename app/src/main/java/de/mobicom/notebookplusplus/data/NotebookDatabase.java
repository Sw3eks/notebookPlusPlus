package de.mobicom.notebookplusplus.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Notebook.class, Note.class}, version = 1)
public abstract class NotebookDatabase extends RoomDatabase {
}
