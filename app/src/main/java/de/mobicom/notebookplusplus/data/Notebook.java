package de.mobicom.notebookplusplus.data;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notebook_table")
public class Notebook {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    private int priority;

    @NonNull
    private String color;

    //private List<Note> notes;


    public Notebook(@NonNull String name, int priority, @NonNull String color) {
        this.name = name;
        this.priority = priority;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    //public List<Note> getNotes() {
    //    return notes;
    //}
}
