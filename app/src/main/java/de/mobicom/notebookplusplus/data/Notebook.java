package de.mobicom.notebookplusplus.data;

import java.util.List;
import java.util.Objects;

public class Notebook {

    private int id;
    private String name;
    private int color;
    private List<Note> notes;

    public Notebook() {
    }

    Notebook(int id, String name, int color, List<Note> notes) {
        this.id = id;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notebook)) return false;
        Notebook notebook = (Notebook) o;
        return getId() == notebook.getId() &&
                getColor() == notebook.getColor() &&
                getName().equals(notebook.getName()) &&
                getNotes().equals(notebook.getNotes());
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", notes=" + notes +
                '}';
    }
}
