package de.mobicom.notebookplusplus.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.mobicom.notebookplusplus.data.model.Notebook;

@Dao
public interface NotebookDao {

    @Insert
    void insert(Notebook notebook);

    @Update
    void update(Notebook notebook);

    @Delete
    void delete(Notebook notebook);

    @Query("DELETE FROM notebook_table")
    void deleteAllNotebooks();

    @Query("SELECT * FROM notebook_table WHERE is_marked_for_delete = 0")
    LiveData<List<Notebook>> getAllNotebooks();

    @Query("DELETE FROM notebook_table WHERE is_marked_for_delete = 0 AND notebook_id NOT IN (SELECT note.notebook_parent_id FROM note_table AS note WHERE note.is_marked_for_delete = 1)")
    void deleteAllNotebooksMarkedForDelete();

    @Query("UPDATE notebook_table SET is_marked_for_delete = 0 WHERE notebook_id = :notebookId")
    void updateDeletedNotebook(long notebookId);
}
