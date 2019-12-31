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

    private List<Note> notes;

    public Notebook() {
    }

    public Notebook(String name, int priority, String color, List<Note> notes) {
        this.name = name;
        this.priority = priority;
        this.color = color;
        this.notes = notes;
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

    public List<Note> getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notebook)) return false;
        Notebook notebook = (Notebook) o;
        return getId() == notebook.getId() &&
                getPriority() == notebook.getPriority() &&
                getName().equals(notebook.getName()) &&
                getColor().equals(notebook.getColor()) &&
                Objects.equals(getNotes(), notebook.getNotes());
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", color='" + color + '\'' +
                ", notes=" + notes +
                '}';
    }
}
