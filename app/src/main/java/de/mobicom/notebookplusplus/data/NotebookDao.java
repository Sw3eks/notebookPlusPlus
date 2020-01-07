package de.mobicom.notebookplusplus.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import de.mobicom.notebookplusplus.data.Notebook;

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

    @Query("SELECT * FROM notebook_table")
    LiveData<List<Notebook>> getAllNotebooks();
}
