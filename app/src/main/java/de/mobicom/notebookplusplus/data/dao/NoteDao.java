package de.mobicom.notebookplusplus.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.mobicom.notebookplusplus.data.model.Note;

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

    @Query("SELECT * FROM note_table WHERE notebook_parent_id = :notebookParentId AND is_archived = 0 AND is_marked_for_delete = 0 ORDER BY is_bookmarked DESC, last_modified_date DESC")
    LiveData<List<Note>> getAllNotes(long notebookParentId);

    @Query("SELECT * FROM note_table WHERE is_archived = 1 AND is_marked_for_delete = 0 ORDER BY last_modified_date DESC")
    LiveData<List<Note>> getAllNotesArchived();

    @Query("SELECT * FROM note_table WHERE is_marked_for_delete = 1 ORDER BY last_modified_date DESC")
    LiveData<List<Note>> getAllNotesDeleted();

    @Query("UPDATE note_table SET is_marked_for_delete = 1 WHERE notebook_parent_id = :notebookParentId")
    void updateNotesWithNotebookId(long notebookParentId);
}