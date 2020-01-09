package de.mobicom.notebookplusplus.data.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import de.mobicom.notebookplusplus.data.model.NoteListItem;

@Dao
public interface NoteListItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NoteListItem noteListItem);

    @Query("DELETE FROM note_list_item_table WHERE note_list_item_id = :noteListItemId")
    void delete(long noteListItemId);

    @Query("SELECT * FROM note_list_item_table WHERE note_parent_id = :noteParentId ORDER BY note_list_item_id ASC")
    List<NoteListItem> getAllNoteListItems(long noteParentId);
}
