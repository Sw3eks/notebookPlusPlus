package de.mobicom.notebookplusplus.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.note.adapter.NoteRecyclerViewAdapter;
import de.mobicom.notebookplusplus.note.model.Note;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity  implements NoteRecyclerViewAdapter.ItemClickListener {

    private NoteRecyclerViewAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.notebooks_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Note> noteList = new ArrayList<>();
        noteList.add(new Note(1, "Note 1", "text", "das ist eine Notiz"));
        noteList.add(new Note(2, "Note 2", "todo", "Das ist eine ToDo Liste"));
        noteList.add(new Note(3, "Note 3", "sprache", "Das ist eine Audionotiz"));

        RecyclerView recyclerView = findViewById(R.id.rvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteRecyclerViewAdapter(this, noteList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

}
