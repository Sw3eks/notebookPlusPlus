package de.mobicom.notebookplusplus.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table WHERE notebook_parent_id = :notebookParentId AND archived = 0 AND delete_mark = 0 ORDER BY last_modified_date DESC")
    LiveData<List<Note>> getAllNotes(long notebookParentId);

    @Query("SELECT * FROM note_table WHERE archived = 1 AND delete_mark = 0 ORDER BY last_modified_date DESC")
    LiveData<List<Note>> getAllNotesArchived();

    @Query("SELECT * FROM note_table WHERE archived = 0 AND delete_mark = 1 ORDER BY last_modified_date DESC")
    LiveData<List<Note>> getAllNotesDeleted();
}