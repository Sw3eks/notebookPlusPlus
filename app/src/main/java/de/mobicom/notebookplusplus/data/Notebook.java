package de.mobicom.notebookplusplus.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    private int priority;

    @NonNull
    private String color;

    @ColumnInfo(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnInfo(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    //private List<Note> notes;


    public Notebook(@NonNull String name, int priority, @NonNull String color) {
        this.name = name;
        this.priority = priority;
        this.color = color;
        this.createdAt = LocalDateTime.now();
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

    public int getPriority() {
        return priority;
    }

    @NonNull
    public String getColor() {
        return color;
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

    //public List<Note> getNotes() {
    //    return notes;
    //}
}
