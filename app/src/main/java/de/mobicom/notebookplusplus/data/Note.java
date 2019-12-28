package de.mobicom.notebookplusplus.data;

import java.util.Date;
import java.util.Objects;

public class Note {

  private int id;
  private String name;
  private String type;
  private String description;
  private Date createdAt;
  private Date lastModifiedAt;

  public Note() {
  }

  public Note(int id, String name, String type, String description, Date createdAt) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.description = description;
    this.createdAt = createdAt;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(Date lastModifiedAt) {
    this.lastModifiedAt = lastModifiedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Note)) return false;
    Note note = (Note) o;
    return getId() == note.getId() &&
            getType().equals(note.getType()) &&
            getName().equals(note.getName()) &&
            getDescription().equals(note.getDescription()) &&
            getCreatedAt().equals(note.getCreatedAt()) &&
            Objects.equals(getLastModifiedAt(), note.getLastModifiedAt());
  }

  @Override
  public String toString() {
    return "Note{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", description='" + description + '\'' +
            ", createdAt=" + createdAt +
            ", lastModifiedAt=" + lastModifiedAt +
            '}';
  }
}
