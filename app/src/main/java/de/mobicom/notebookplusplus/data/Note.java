package de.mobicom.notebookplusplus.data;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    private int type;

    private int priority;

    private String description;

    @NonNull
    private LocalDate createdAt;

    private LocalDate lastModifiedAt;

    public Note(String name, int type, int priority, String description) {
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.description = description;
        this.createdAt = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDate lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
