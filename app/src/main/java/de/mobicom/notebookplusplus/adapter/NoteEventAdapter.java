package de.mobicom.notebookplusplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.mobicom.notebookplusplus.data.model.Note;

public class NoteEventAdapter extends BaseAdapter {
    private List<Note> noteList;
    private Context context;


    public NoteEventAdapter(List<Note> notes, Context context) {
        this.noteList = notes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return noteList != null ? noteList.size() : 0;
    }

    @Override
    public Note getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position).getName());

        return convertView;
    }
}
