package de.mobicom.notebookplusplus.data.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import de.mobicom.notebookplusplus.data.model.NoteListItem;

@Dao
public interface NoteListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NoteListItem noteListItem);

    @Update
    void update(NoteListItem noteListItem);

    @Delete
    void delete(NoteListItem noteListItem);

    @Query("SELECT * FROM note_list_item_table WHERE note_parent_id = :noteParentId ORDER BY note_list_item_id ASC")
    LiveData<List<NoteListItem>> getAllNoteListItems(long noteParentId);
}
