package de.mobicom.notebookplusplus.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import de.mobicom.notebookplusplus.data.model.Note;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note_list_item_table",
        foreignKeys = {
                @ForeignKey(entity = Note.class,
                        parentColumns = "note_id",
                        childColumns = "note_parent_id",
                        onUpdate = CASCADE,
                        onDelete = CASCADE)},
        indices = {
                @Index("note_parent_id")}
)
public class NoteListItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_list_item_id")
    private long noteListItemId;

    @ColumnInfo(name = "note_parent_id")
    private long noteParentId;

    private String content;

    @ColumnInfo(name = "is_checked")
    private boolean isChecked;

    public NoteListItem(long noteParentId, String content, boolean isChecked) {
        this.noteParentId = noteParentId;
        this.content = content;
        this.isChecked = isChecked;
    }

    public long getNoteListItemId() {
        return noteListItemId;
    }

    public void setNoteListItemId(long noteListItemId) {
        this.noteListItemId = noteListItemId;
    }

    public long getNoteParentId() {
        return noteParentId;
    }

    public void setNoteParentId(long noteParentId) {
        this.noteParentId = noteParentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @NonNull
    @Override
    public String toString() {
        return "NoteListItem{" +
                "noteListItemId=" + noteListItemId +
                ", noteParentId=" + noteParentId +
                ", content='" + content + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
