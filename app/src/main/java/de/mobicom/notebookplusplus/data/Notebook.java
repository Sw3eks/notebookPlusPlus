package de.mobicom.notebookplusplus.data;

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

    //private List<Note> notes;


    public Notebook(@NonNull String name, @NonNull String color) {
        this.name = name;
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
}
