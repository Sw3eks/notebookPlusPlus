package de.mobicom.notebookplusplus.data;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import de.mobicom.notebookplusplus.utils.LocalDateTimeConverter;

@Entity(tableName = "note_table",
        foreignKeys = {
                @ForeignKey(entity = Notebook.class,
                        parentColumns = "notebook_id",
                        childColumns = "notebook_parent_id")},
        indices = {
                @Index("notebook_parent_id")}
)
public class Note {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private long noteId;

    @ColumnInfo(name = "notebook_parent_id")
    private long notebookParentId;

    @NonNull
    private String name;

    private int type;

    private String description;

    private boolean archived;

    @ColumnInfo(name = "delete_mark")
    private boolean deleteMark;

    @NonNull
    @ColumnInfo(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnInfo(name = "last_modified_date")
    private LocalDateTime lastModifiedAt;

    public Note(long notebookParentId, @NonNull String name, int type, String description) {
        this.notebookParentId = notebookParentId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.archived = false;
        this.deleteMark = false;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getNotebookParentId() {
        return notebookParentId;
    }

    public void setNotebookParentId(long notebookParentId) {
        this.notebookParentId = notebookParentId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(boolean deleteMark) {
        this.deleteMark = deleteMark;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
