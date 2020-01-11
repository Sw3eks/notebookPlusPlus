package de.mobicom.notebookplusplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.mobicom.notebookplusplus.data.model.Notebook;

public class NotebookListAdapter extends BaseAdapter {
    private List<Notebook> notebookList;
    private Context context;


    public NotebookListAdapter(List<Notebook> notebooks, Context context) {
        this.notebookList = notebooks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notebookList != null ? notebookList.size() : 0;
    }

    @Override
    public Notebook getItem(int position) {
        return notebookList.get(position);
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
