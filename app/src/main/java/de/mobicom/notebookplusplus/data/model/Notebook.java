package de.mobicom.notebookplusplus.data.model;

import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notebook_table")
public class Notebook {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notebook_id")
    private long notebookId;

    @NonNull
    private String name;

    @NonNull
    private String color;

    @ColumnInfo(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnInfo(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @ColumnInfo(name = "is_marked_for_delete")
    private boolean isMarkedForDelete;

    public Notebook(@NonNull String name, @NonNull String color) {
        this.name = name;
        this.color = color;
        this.createdAt = LocalDateTime.now();
        this.isMarkedForDelete = false;
    }

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getColor() {
        return color;
    }

    public void setColor(@NonNull String color) {
        this.color = color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        isMarkedForDelete = markedForDelete;
    }

    public boolean isMarkedForDelete() {
        return isMarkedForDelete;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "notebookId=" + notebookId +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedDate=" + lastModifiedDate +
                ", isMarkedForDelete=" + isMarkedForDelete +
                '}';
    }
}
