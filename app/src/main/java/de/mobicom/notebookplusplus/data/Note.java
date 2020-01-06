package de.mobicom.notebookplusplus.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

// TODO: keep notes in Trash if notebook is deleted!!
@Entity(tableName = "note_table",
        foreignKeys = {
                @ForeignKey(entity = Notebook.class,
                        parentColumns = "notebook_id",
                        childColumns = "notebook_parent_id",
                        onUpdate = CASCADE,
                        onDelete = CASCADE)},
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

    @ColumnInfo(name = "is_archived")
    private boolean isArchived;

    @ColumnInfo(name = "is_marked_for_delete")
    private boolean isMarkedForDelete;

    @ColumnInfo(name = "is_bookmarked")
    private boolean isBookmarked;

    @ColumnInfo(name = "notification_date")
    private LocalDate notificationDate;

    @ColumnInfo(name = "is_notification_enabled")
    private boolean isNotificationEnabled;

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
        this.notificationDate = LocalDate.now();
        this.isArchived = false;
        this.isMarkedForDelete = false;
        this.isBookmarked = false;
        this.isNotificationEnabled = false;
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
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public boolean isMarkedForDelete() {
        return isMarkedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.isMarkedForDelete = markedForDelete;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteId=" + noteId +
                ", notebookParentId=" + notebookParentId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", isArchived=" + isArchived +
                ", isMarkedForDelete=" + isMarkedForDelete +
                ", isBookmarked=" + isBookmarked +
                ", notificationDate=" + notificationDate +
                ", isNotificationEnabled=" + isNotificationEnabled +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
