package de.mobicom.notebookplusplus.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.mobicom.notebookplusplus.R;
import de.mobicom.notebookplusplus.note.model.Note;

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder> {

    private List<Note> noteList;
    private List<Note> noteListFiltered = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public NoteRecyclerViewAdapter(Context context, List<Note> noteList) {
        this.mInflater = LayoutInflater.from(context);
        this.noteList = noteList;
        this.noteListFiltered.addAll(noteList);
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_note_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getName());
        holder.noteContent.setText(note.getDescription());
        //holder.noteTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_note_type_text, 0, 0, 0);
        holder.lastModifiedDate.setText("01.01.2019");

        switch (note.getType()) {
            case "todo":
                holder.noteType.setBackgroundResource(R.drawable.ic_note_type_todo);
                break;
            case "speech":
                holder.noteType.setBackgroundResource(R.drawable.ic_note_type_speech);
                break;
            default:
                holder.noteType.setBackgroundResource(R.drawable.ic_note_type_text);
        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle;
        TextView noteContent;
        TextView lastModifiedDate;
        ImageView noteType;

        ViewHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            lastModifiedDate = itemView.findViewById(R.id.noteModifiedDate);
            noteType = itemView.findViewById(R.id.noteType);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Note getItem(int id) {
        return noteList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String text) {
        noteList.clear();
        if (text.isEmpty()) {
            noteList.addAll(noteListFiltered);
        } else {
            text = text.toLowerCase();
            for (Note item : noteListFiltered) {
                if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) {
                    noteList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}